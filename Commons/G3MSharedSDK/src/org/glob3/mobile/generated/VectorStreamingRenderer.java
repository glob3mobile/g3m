package org.glob3.mobile.generated; 
//
//  VectorStreamingRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/30/15.
//
//

//
//  VectorStreamingRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/30/15.
//
//





//class IThreadUtils;
//class IByteBuffer;
//class Sector;
//class Geodetic2D;
//class JSONArray;
//class JSONObject;
//class Mark;
//class GEO2DPointGeometry;
//class BoundingVolume;
//class Camera;
//class Frustum;
//class IDownloader;
//class GEOObject;


public class VectorStreamingRenderer extends DefaultRenderer
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class VectorSet;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class Node;


  public static class GEOJSONUtils
  {
    private GEOJSONUtils()
    {
    }

    public static Sector parseSector(JSONArray json)
    {
      final double lowerLat = json.getAsNumber(0).value();
      final double lowerLon = json.getAsNumber(1).value();
      final double upperLat = json.getAsNumber(2).value();
      final double upperLon = json.getAsNumber(3).value();
    
      return new Sector(Geodetic2D.fromDegrees(lowerLat, lowerLon), Geodetic2D.fromDegrees(upperLat, upperLon));
    }
    public static Geodetic2D parseGeodetic2D(JSONArray json)
    {
      final double lat = json.getAsNumber(0).value();
      final double lon = json.getAsNumber(1).value();
    
      return new Geodetic2D(Angle.fromDegrees(lat), Angle.fromDegrees(lon));
    }
    public static VectorStreamingRenderer.Node parseNode(JSONObject json, VectorSet vectorSet, boolean verbose)
    {
      final String id = json.getAsString("id").value();
      Sector nodeSector = GEOJSONUtils.parseSector(json.getAsArray("nodeSector"));
      Sector minimumSector = GEOJSONUtils.parseSector(json.getAsArray("minimumSector"));
      int featuresCount = (int) json.getAsNumber("featuresCount").value();
      Geodetic2D averagePosition = GEOJSONUtils.parseGeodetic2D(json.getAsArray("averagePosition"));
    
      java.util.ArrayList<String> children = new java.util.ArrayList<String>();
      final JSONArray childrenJSON = json.getAsArray("children");
      for (int i = 0; i < childrenJSON.size(); i++)
      {
        children.add(childrenJSON.getAsString(i).value());
      }
    
      return new Node(vectorSet, id, nodeSector, minimumSector, featuresCount, averagePosition, children, verbose);
    }

  }



  public static class ChildrenParserAsyncTask extends GAsyncTask
  {
    private Node _node;
    private boolean _verbose;
    private IByteBuffer _buffer;
    private final IThreadUtils _threadUtils;

    private java.util.ArrayList<Node> _children;

    public ChildrenParserAsyncTask(Node node, boolean verbose, IByteBuffer buffer, IThreadUtils threadUtils)
    {
       _node = node;
       _verbose = verbose;
       _buffer = buffer;
       _threadUtils = threadUtils;
       _children = null;
      _node._retain();
    }

    public void dispose()
    {
      _node._release();
      if (_buffer != null)
         _buffer.dispose();
    
      if (_children != null)
      {
        for (int i = 0; i > _children.size(); i++)
        {
          Node child = _children.get(i);
          child._release();
        }
        _children = null;
      }
    }

    public final void runInBackground(G3MContext context)
    {
      final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(_buffer);
      if (jsonBaseObject != null)
      {
        final JSONArray nodesJSON = jsonBaseObject.asArray();
        if (nodesJSON != null)
        {
          _children = new java.util.ArrayList<Node>();
          for (int i = 0; i < nodesJSON.size(); i++)
          {
            final JSONObject nodeJSON = nodesJSON.getAsObject(i);
            _children.add(GEOJSONUtils.parseNode(nodeJSON, _node.getVectorSet(), _verbose));
          }
        }
    
        if (jsonBaseObject != null)
           jsonBaseObject.dispose();
      }
    
      if (_buffer != null)
         _buffer.dispose();
      _buffer = null;
    }

    public final void onPostExecute(G3MContext context)
    {
      _node.parsedChildren(_children, _threadUtils);
      _children = null; // moved ownership to _node
    }

  }



  public static class NodeChildrenDownloadListener extends IBufferDownloadListener
  {
    private Node _node;
    private final IThreadUtils _threadUtils;
    private final boolean _verbose;

    public NodeChildrenDownloadListener(Node node, IThreadUtils threadUtils, boolean verbose)
    {
       _node = node;
       _threadUtils = threadUtils;
       _verbose = verbose;
      _node._retain();
    }

    public void dispose()
    {
      _node._release();
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("\"%s\": Downloaded children (bytes=%d)",
                                   _node.getFullName(),
                                   buffer.size());
      }
    
      _threadUtils.invokeAsyncTask(new ChildrenParserAsyncTask(_node, _verbose, buffer, _threadUtils), true);
    
    }

    public final void onError(URL url)
    {
      _node.errorDownloadingChildren();
    }

    public final void onCancel(URL url)
    {
      // do nothing
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing
    }

  }



  public static class FeaturesParserAsyncTask extends GAsyncTask
  {
    private Node _node;
    private boolean _verbose;
    private IByteBuffer _buffer;
    private final IThreadUtils _threadUtils;

    private GEOObject _features;

    public FeaturesParserAsyncTask(Node node, boolean verbose, IByteBuffer buffer, IThreadUtils threadUtils)
    {
       _node = node;
       _verbose = verbose;
       _buffer = buffer;
       _threadUtils = threadUtils;
       _features = null;
      _node._retain();
    }

    public void dispose()
    {
      _node._release();
      if (_buffer != null)
         _buffer.dispose();
      if (_features != null)
         _features.dispose();
    }

    public final void runInBackground(G3MContext context)
    {
      _features = GEOJSONParser.parseJSON(_buffer, _verbose);
    
      if (_buffer != null)
         _buffer.dispose();
      _buffer = null;
    }

    public final void onPostExecute(G3MContext context)
    {
      _node.parsedFeatures(_features, _threadUtils);
      _features = null; // moved ownership to _node
    }

  }


  public static class NodeFeaturesDownloadListener extends IBufferDownloadListener
  {
    private Node _node;
    private final IThreadUtils _threadUtils;
    private final boolean _verbose;

    public NodeFeaturesDownloadListener(Node node, IThreadUtils threadUtils, boolean verbose)
    {
       _node = node;
       _threadUtils = threadUtils;
       _verbose = verbose;
      _node._retain();
    }

    public void dispose()
    {
      _node._release();
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("\"%s\": Downloaded features (bytes=%d)",
                                   _node.getFullName(),
                                   buffer.size());
      }
    
      _threadUtils.invokeAsyncTask(new FeaturesParserAsyncTask(_node, _verbose, buffer, _threadUtils), true);
    
    }

    public final void onError(URL url)
    {
      _node.errorDownloadingFeatures();
    }

    public final void onCancel(URL url)
    {
      // do nothing
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing
    }

  }


  public static class NodeMarksFilter extends MarksFilter
  {
    private String _nodeToken;

    public NodeMarksFilter(Node node)
    {
      _nodeToken = node.getMarkToken();
    }

    public final boolean test(Mark mark)
    {
      return (_nodeToken.equals(mark.getToken()));
    }

  }


  public static class Node extends RCObject
  {
    private final VectorSet _vectorSet;
    private final String _id;
    private final Sector _nodeSector;
    private final Sector _minimumSector;
    private final int _featuresCount;
    private final Geodetic2D _averagePosition;
    private final java.util.ArrayList<String> _childrenIDs;

    private java.util.ArrayList<Node> _children;
    private int _childrenSize;

    private final boolean _verbose;

    private GEOObject _features;

    private BoundingVolume _boundingVolume;
    private BoundingVolume getBoundingVolume(G3MRenderContext rc)
    {
      if (_boundingVolume == null)
      {
        final Planet planet = rc.getPlanet();
    
        java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(5);
        points.add( planet.toCartesian( _minimumSector.getNE()     ) );
        points.add( planet.toCartesian( _minimumSector.getNW()     ) );
        points.add( planet.toCartesian( _minimumSector.getSE()     ) );
        points.add( planet.toCartesian( _minimumSector.getSW()     ) );
        points.add( planet.toCartesian( _minimumSector.getCenter() ) );
    
        _boundingVolume = Sphere.enclosingSphere(points);
      }
    
      return _boundingVolume;
    }

    private IDownloader _downloader;
    private boolean _loadingChildren;

    private boolean _wasVisible;
    private boolean isVisible(G3MRenderContext rc, Frustum frustumInModelCoordinates)
    {
      //  if ((_sector->_deltaLatitude._degrees  > 80) ||
      //      (_sector->_deltaLongitude._degrees > 80)) {
      //    return true;
      //  }
    
      return getBoundingVolume(rc).touchesFrustum(frustumInModelCoordinates);
    }

    private boolean _loadedFeatures;
    private boolean _loadingFeatures;

    private boolean _wasBigEnough;
    private boolean isBigEnough(G3MRenderContext rc)
    {
      if ((_nodeSector._deltaLatitude._degrees >= 80) || (_nodeSector._deltaLongitude._degrees >= 80))
      {
        return true;
      }
    
      final double projectedArea = getBoundingVolume(rc).projectedArea(rc);
      return (projectedArea > 350000);
    }

    private long _featuresRequestID;
    private void loadFeatures(G3MRenderContext rc)
    {
      final URL metadataURL = new URL(_vectorSet.getServerURL(), _vectorSet.getName() + "/features" + "?node=" + _id + "&properties=" + _vectorSet.getProperties(), true);
    
    //  if (_verbose) {
    //    ILogger::instance()->logInfo("\"%s\": Downloading features for node \'%s\'",
    //                                 _vectorSet->getName().c_str(),
    //                                 _id.c_str());
    //  }
    
      _downloader = rc.getDownloader();
      _featuresRequestID = _downloader.requestBuffer(metadataURL, _vectorSet.getDownloadPriority() + _featuresCount, _vectorSet.getTimeToCache(), _vectorSet.getReadExpired(), new NodeFeaturesDownloadListener(this, rc.getThreadUtils(), _verbose), true);
    }
    private void unloadFeatures()
    {
      _loadedFeatures = false;
      _loadingFeatures = false;
    
      if (_features != null)
         _features.dispose();
      _features = null;
    }
    private void cancelLoadFeatures()
    {
      if (_featuresRequestID != -1)
      {
        _downloader.cancelRequest(_featuresRequestID);
        _featuresRequestID = -1;
      }
    }

    private long _childrenRequestID;
    private void loadChildren(G3MRenderContext rc)
    {
    
      // http://192.168.1.12:8080/server-mapboo/public/VectorialStreaming/GEONames-PopulatedPlaces_LOD/?nodes=0|1|
    
      final int childrenIDsSize = _childrenIDs.size();
      if (childrenIDsSize == 0)
      {
        java.util.ArrayList<Node> children = new java.util.ArrayList<Node>();
        parsedChildren(children, rc.getThreadUtils());
        return;
      }
    
      String nodes = "";
      for (int i = 0; i < childrenIDsSize; i++)
      {
        if (i > 0)
        {
          nodes += "|";
        }
        nodes += _childrenIDs.get(i);
      }
    
      final URL childrenURL = new URL(_vectorSet.getServerURL(), _vectorSet.getName() + "?nodes=" + nodes, true);
    
    //  if (_verbose) {
    //    ILogger::instance()->logInfo("\"%s\": Downloading children for node \'%s\'",
    //                                 _vectorSet->getName().c_str(),
    //                                 _id.c_str());
    //  }
    
      _downloader = rc.getDownloader();
    
      _childrenRequestID = _downloader.requestBuffer(childrenURL, _vectorSet.getDownloadPriority(), _vectorSet.getTimeToCache(), _vectorSet.getReadExpired(), new NodeChildrenDownloadListener(this, rc.getThreadUtils(), _verbose), true);
    }
    private void unloadChildren()
    {
      if (_children != null)
      {
        for (int i = 0; i < _childrenSize; i++)
        {
          Node child = _children.get(i);
          child._release();
        }
        _children = null;
        _children = null;
        _childrenSize = 0;
      }
    }
    private void cancelLoadChildren()
    {
      if (_childrenRequestID != -1)
      {
        _downloader.cancelRequest(_childrenRequestID);
        _childrenRequestID = -1;
      }
    }

    private void unload()
    {
      removeMarks();
    
      if (_loadingFeatures)
      {
        cancelLoadFeatures();
        _loadingFeatures = false;
      }
    
      if (_loadingChildren)
      {
        _loadingChildren = true;
        cancelLoadChildren();
      }
    
      if (_loadedFeatures)
      {
        unloadFeatures();
        _loadedFeatures = false;
      }
    
      if (_children != null)
      {
        unloadChildren();
      }
    }

    private void removeMarks()
    {
      int removed = _vectorSet.getMarksRenderer().removeAllMarks(new NodeMarksFilter(this), true);
    
      if (_verbose && removed != 0)
      {
        ILogger.instance().logInfo("\"%s\": Removed %d marks",
                                   getFullName(),
                                   removed);
      }
    }

    private long _marksCount;

    public void dispose()
    {
      unload();
    
      if (_nodeSector != null)
         _nodeSector.dispose();
      if (_minimumSector != null)
         _minimumSector.dispose();
      if (_averagePosition != null)
         _averagePosition.dispose();
      if (_boundingVolume != null)
         _boundingVolume.dispose();
    
      super.dispose();
    }

    public Node(VectorSet vectorSet, String id, Sector nodeSector, Sector minimumSector, int featuresCount, Geodetic2D averagePosition, java.util.ArrayList<String> childrenIDs, boolean verbose)
    {
       _vectorSet = vectorSet;
       _id = id;
       _nodeSector = nodeSector;
       _minimumSector = minimumSector;
       _featuresCount = featuresCount;
       _averagePosition = averagePosition;
       _childrenIDs = childrenIDs;
       _verbose = verbose;
       _wasVisible = false;
       _loadedFeatures = false;
       _loadingFeatures = false;
       _children = null;
       _childrenSize = 0;
       _loadingChildren = false;
       _wasBigEnough = false;
       _boundingVolume = null;
       _featuresRequestID = -1;
       _childrenRequestID = -1;
       _downloader = null;
       _features = null;
       _marksCount = 0;

    }

    public final VectorSet getVectorSet()
    {
      return _vectorSet;
    }

    public final String getFullName()
    {
      return _vectorSet.getName() + "/" + _id;
    }

    public final String getMarkToken()
    {
      return _id + _vectorSet.getName();
    }

    public final long render(G3MRenderContext rc, Frustum frustumInModelCoordinates, long cameraTS, GLState glState)
    {
    
      long renderedCount = 0;
    
      // #warning Show Bounding Volume
      // getBoundingVolume(rc)->render(rc, glState, Color::red());
    
      final boolean visible = isVisible(rc, frustumInModelCoordinates);
      if (visible)
      {
        final boolean bigEnough = isBigEnough(rc);
        if (bigEnough)
        {
          renderedCount += _marksCount;
          if (_loadedFeatures)
          {
            // don't load nor render children until the features are loaded
            if (_children == null)
            {
              if (!_loadingChildren)
              {
                _loadingChildren = true;
                loadChildren(rc);
              }
            }
            if (_children != null)
            {
              for (int i = 0; i < _childrenSize; i++)
              {
                Node child = _children.get(i);
                renderedCount += child.render(rc, frustumInModelCoordinates, cameraTS, glState);
              }
            }
          }
          else
          {
            if (!_loadingFeatures)
            {
              _loadingFeatures = true;
              loadFeatures(rc);
            }
          }
    
        }
        else
        {
          if (_wasBigEnough)
          {
            unload();
          }
        }
        _wasBigEnough = bigEnough;
      }
      else
      {
        if (_wasVisible)
        {
          unload();
        }
      }
      _wasVisible = visible;
    
      return renderedCount;
    }

    public final void errorDownloadingFeatures()
    {
      // do nothing by now
    }

    public final void parsedFeatures(GEOObject features, IThreadUtils threadUtils)
    {
      _loadedFeatures = true;
      _loadingFeatures = false;
      _featuresRequestID = -1;
      if (features == null)
      {
        // do nothing by now
      }
      else
      {
        _features = features;
    
        _marksCount = _features.createMarks(_vectorSet, this);
        if (_verbose)
        {
          ILogger.instance().logInfo("\"%s\": Created %d marks",
                                     getFullName(),
                                     _marksCount);
        }
    
        //  Delete _features???
        if (_features != null)
           _features.dispose();
        _features = null;
      }
    }

    public final void errorDownloadingChildren()
    {
      // do nothing by now
    }

    public final void parsedChildren(java.util.ArrayList<Node> children, IThreadUtils threadUtils)
    {
      if (children == null)
      {
        // do nothing by now
      }
      else
      {
        _children = children;
        _loadingChildren = false;
        _childrenSize = _children.size();
      }
    }

  }


  public static class MetadataParserAsyncTask extends GAsyncTask
  {
    private VectorSet _vectorSet;
    private final boolean _verbose;
    private IByteBuffer _buffer;

    private boolean _parsingError;

    private Sector _sector;
    private long _featuresCount;
    private Geodetic2D _averagePosition;
    private int _nodesCount;
    private int _minNodeDepth;
    private int _maxNodeDepth;
    private java.util.ArrayList<Node> _rootNodes;

    public MetadataParserAsyncTask(VectorSet vectorSet, boolean verbose, IByteBuffer buffer)
    {
       _vectorSet = vectorSet;
       _verbose = verbose;
       _buffer = buffer;
       _parsingError = false;
       _sector = null;
       _featuresCount = -1;
       _averagePosition = null;
       _nodesCount = -1;
       _minNodeDepth = -1;
       _maxNodeDepth = -1;
       _rootNodes = null;
    }

    public void dispose()
    {
      if (_buffer != null)
         _buffer.dispose();
    
      if (_sector != null)
         _sector.dispose();
      if (_averagePosition != null)
         _averagePosition.dispose();
    
      if (_rootNodes != null)
      {
        for (int i = 0; i < _rootNodes.size(); i++)
        {
          Node node = _rootNodes.get(i);
          node._release();
        }
        _rootNodes = null;
      }
    }

    public final void runInBackground(G3MContext context)
    {
    
      final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(_buffer);
    
      if (_buffer != null)
         _buffer.dispose();
      _buffer = null;
    
      if (jsonBaseObject == null)
      {
        _parsingError = true;
        return;
      }
    
      final JSONObject jsonObject = jsonBaseObject.asObject();
      if (jsonObject == null)
      {
        _parsingError = true;
      }
      else
      {
        // check for errors
        final JSONString errorCodeJSON = jsonObject.getAsString("errorCode");
        if (errorCodeJSON != null)
        {
          _parsingError = true;
    
          final String errorCode = errorCodeJSON.value();
    
          final JSONString errorDescriptionJSON = jsonObject.getAsString("errorDescription");
          if (errorDescriptionJSON == null)
          {
            ILogger.instance().logError("\"%s\": %s", _vectorSet.getName(), errorCode);
          }
          else
          {
            final String errorDescription = errorDescriptionJSON.value();
            ILogger.instance().logError("\"%s\": %s (%s)", _vectorSet.getName(), errorCode, errorDescription);
          }
        }
        else
        {
          _sector = GEOJSONUtils.parseSector(jsonObject.getAsArray("sector"));
          _featuresCount = (long) jsonObject.getAsNumber("featuresCount").value();
          _averagePosition = GEOJSONUtils.parseGeodetic2D(jsonObject.getAsArray("averagePosition"));
          _nodesCount = (int) jsonObject.getAsNumber("featuresCount").value();
          _minNodeDepth = (int) jsonObject.getAsNumber("minNodeDepth").value();
          _maxNodeDepth = (int) jsonObject.getAsNumber("maxNodeDepth").value();
    
          final JSONArray rootNodesJSON = jsonObject.getAsArray("rootNodes");
          _rootNodes = new java.util.ArrayList<Node>();
          for (int i = 0; i < rootNodesJSON.size(); i++)
          {
            Node node = GEOJSONUtils.parseNode(rootNodesJSON.getAsObject(i), _vectorSet, _verbose);
            _rootNodes.add(node);
          }
        }
      }
    
      if (jsonBaseObject != null)
         jsonBaseObject.dispose();
    }

    public final void onPostExecute(G3MContext context)
    {
      if (_parsingError)
      {
        _vectorSet.errorParsingMetadata();
      }
      else
      {
        _vectorSet.parsedMetadata(_sector, _featuresCount, _averagePosition, _nodesCount, _minNodeDepth, _maxNodeDepth, _rootNodes);
        _sector = null; // moved ownership to _vectorSet
        _averagePosition = null; // moved ownership to _vectorSet
        _rootNodes = null; // moved ownership to _vectorSet
      }
    }

  }


  public static class MetadataDownloadListener extends IBufferDownloadListener
  {
    private VectorSet _vectorSet;
    private final IThreadUtils _threadUtils;
    private final boolean _verbose;

    public MetadataDownloadListener(VectorSet vectorSet, IThreadUtils threadUtils, boolean verbose)
    {
       _vectorSet = vectorSet;
       _threadUtils = threadUtils;
       _verbose = verbose;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("\"%s\": Downloaded metadata (bytes=%d)",
                                   _vectorSet.getName(),
                                   buffer.size());
      }
    
      _threadUtils.invokeAsyncTask(new MetadataParserAsyncTask(_vectorSet, _verbose, buffer), true);
    }

    public final void onError(URL url)
    {
      _vectorSet.errorDownloadingMetadata();
    }

    public final void onCancel(URL url)
    {
      // do nothing
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing
    }

  }


  public interface VectorSetSymbolizer
  {
    void dispose();

    Mark createMark(GEO2DPointGeometry geometry);

  }


  public static class VectorSet
  {
    private VectorStreamingRenderer _renderer;
    private final URL _serverURL;
    private final String _name;
    private final VectorSetSymbolizer _symbolizer;
    private final boolean _deleteSymbolizer;
    private final long _downloadPriority;
    private final TimeInterval _timeToCache;
    private final boolean _readExpired;
    private final boolean _verbose;
    private final boolean _haltOnError;

    private final String _properties;

    private boolean _downloadingMetadata;
    private boolean _errorDownloadingMetadata;
    private boolean _errorParsingMetadata;

    private Sector _sector;
    private long _featuresCount;
    private Geodetic2D _averagePosition;
    private int _nodesCount;
    private int _minNodeDepth;
    private int _maxNodeDepth;
    private java.util.ArrayList<Node> _rootNodes;
    private int _rootNodesSize;

    private long _lastRenderedCount;


    public VectorSet(VectorStreamingRenderer renderer, URL serverURL, String name, String properties, VectorSetSymbolizer symbolizer, boolean deleteSymbolizer, long downloadPriority, TimeInterval timeToCache, boolean readExpired, boolean verbose, boolean haltOnError)
    {
       _renderer = renderer;
       _serverURL = serverURL;
       _name = name;
       _properties = properties;
       _symbolizer = symbolizer;
       _deleteSymbolizer = deleteSymbolizer;
       _downloadPriority = downloadPriority;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
       _verbose = verbose;
       _haltOnError = haltOnError;
       _downloadingMetadata = false;
       _errorDownloadingMetadata = false;
       _errorParsingMetadata = false;
       _sector = null;
       _averagePosition = null;
       _rootNodes = null;
       _rootNodesSize = 0;
       _lastRenderedCount = 0;

    }

    public void dispose()
    {
      if (_deleteSymbolizer)
      {
        if (_symbolizer != null)
           _symbolizer.dispose();
      }
    
      if (_sector != null)
         _sector.dispose();
      if (_averagePosition != null)
         _averagePosition.dispose();
      if (_rootNodes != null)
      {
        for (int i = 0; i < _rootNodes.size(); i++)
        {
          Node node = _rootNodes.get(i);
          node._release();
        }
        _rootNodes = null;
      }
    }

    public final URL getServerURL()
    {
      return _serverURL;
    }

    public final String getName()
    {
      return _name;
    }

    public final long getDownloadPriority()
    {
      return _downloadPriority;
    }

    public final TimeInterval getTimeToCache()
    {
      return _timeToCache;
    }

    public final boolean getReadExpired()
    {
      return _readExpired;
    }

    public final String getProperties()
    {
      return _properties;
    }

    public final void initialize(G3MContext context)
    {
      _downloadingMetadata = true;
      _errorDownloadingMetadata = false;
      _errorParsingMetadata = false;
    
      final URL metadataURL = new URL(_serverURL, _name);
    
      if (_verbose)
      {
        ILogger.instance().logInfo("\"%s\": Downloading metadata", _name);
      }
    
      context.getDownloader().requestBuffer(metadataURL, _downloadPriority, _timeToCache, _readExpired, new MetadataDownloadListener(this, context.getThreadUtils(), _verbose), true);
    }

    public final RenderState getRenderState(G3MRenderContext rc)
    {
      if (_haltOnError)
      {
        if (_downloadingMetadata)
        {
          return RenderState.busy();
        }
    
        if (_errorDownloadingMetadata)
        {
          return RenderState.error("Error downloading metadata of \"" + _name + "\" from \"" + _serverURL.getPath() + "\"");
        }
    
        if (_errorParsingMetadata)
        {
          return RenderState.error("Error parsing metadata of \"" + _name + "\" from \"" + _serverURL.getPath() + "\"");
        }
      }
    
      return RenderState.ready();
    }

    public final void errorDownloadingMetadata()
    {
      _downloadingMetadata = false;
      _errorDownloadingMetadata = true;
    }
    public final void errorParsingMetadata()
    {
      _downloadingMetadata = false;
      _errorParsingMetadata = true;
    }
    public final void parsedMetadata(Sector sector, long featuresCount, Geodetic2D averagePosition, int nodesCount, int minNodeDepth, int maxNodeDepth, java.util.ArrayList<Node> rootNodes)
    {
      _downloadingMetadata = false;
    
      _sector = sector;
      _featuresCount = featuresCount;
      _averagePosition = averagePosition;
      _nodesCount = nodesCount;
      _minNodeDepth = minNodeDepth;
      _maxNodeDepth = maxNodeDepth;
      _rootNodes = rootNodes;
      _rootNodesSize = _rootNodes.size();
    
      if (_verbose)
      {
        ILogger.instance().logInfo("\"%s\": Metadata", _name);
        ILogger.instance().logInfo("   Sector           : %s", _sector.description());
        ILogger.instance().logInfo("   Features Count   : %d",   _featuresCount);
        ILogger.instance().logInfo("   Average Position : %s", _averagePosition.description());
        ILogger.instance().logInfo("   Nodes Count      : %d", _nodesCount);
        ILogger.instance().logInfo("   Depth            : %d/%d", _minNodeDepth, _maxNodeDepth);
        ILogger.instance().logInfo("   Root Nodes       : %d", _rootNodesSize);
      }
    
    }

    public final void render(G3MRenderContext rc, Frustum frustumInModelCoordinates, long cameraTS, GLState glState)
    {
      if (_rootNodesSize > 0)
      {
        long renderedCount = 0;
        for (int i = 0; i < _rootNodesSize; i++)
        {
          Node rootNode = _rootNodes.get(i);
          renderedCount += rootNode.render(rc, frustumInModelCoordinates, cameraTS, glState);
        }
    
        if (_lastRenderedCount != renderedCount)
        {
          if (_verbose)
          {
            ILogger.instance().logInfo("\"%s\": Rendered %d features", _name, renderedCount);
          }
          _lastRenderedCount = renderedCount;
        }
    
      }
    }

    public final long createMark(Node node, GEO2DPointGeometry geometry)
    {
      Mark mark = _symbolizer.createMark(geometry);
      if (mark == null)
      {
        return 0;
      }
    
      mark.setToken(node.getMarkToken());
      _renderer.getMarkRenderer().addMark(mark);
      return 1;
    }

    public final MarksRenderer getMarksRenderer()
    {
      return _renderer.getMarkRenderer();
    }

  }


  private MarksRenderer _markRenderer;

  private int _vectorSetsSize;
  private java.util.ArrayList<VectorSet> _vectorSets = new java.util.ArrayList<VectorSet>();

  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private GLState _glState;
  private void updateGLState(Camera camera)
  {
    ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glState.addGLFeature(new ModelViewGLFeature(camera), true);
    }
    else
    {
      f.setMatrix(camera.getModelViewMatrix44D());
    }
  }


  public VectorStreamingRenderer(MarksRenderer markRenderer)
  {
     _markRenderer = markRenderer;
     _vectorSetsSize = 0;
     _glState = new GLState();
  }

  public void dispose()
  {
    for (int i = 0; i < _vectorSetsSize; i++)
    {
      VectorSet vectorSet = _vectorSets.get(i);
      if (vectorSet != null)
         vectorSet.dispose();
    }
  
    _glState._release();
    //  delete _timer;
  
    super.dispose();
  }

  public final MarksRenderer getMarkRenderer()
  {
    return _markRenderer;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    for (int i = 0; i < _vectorSetsSize; i++)
    {
      final Camera camera = rc.getCurrentCamera();
      final Frustum frustumInModelCoordinates = camera.getFrustumInModelCoordinates();
  
      final long cameraTS = camera.getTimestamp();
  
      updateGLState(camera);
      _glState.setParent(glState);
  
      VectorSet vectorSector = _vectorSets.get(i);
      vectorSector.render(rc, frustumInModelCoordinates, cameraTS, _glState);
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void onChangedContext()
  {
    for (int i = 0; i < _vectorSetsSize; i++)
    {
      VectorSet vectorSet = _vectorSets.get(i);
      vectorSet.initialize(_context);
    }
  }

  public final void addVectorSet(URL serverURL, String name, String properties, VectorSetSymbolizer symbolizer, boolean deleteSymbolizer, long downloadPriority, TimeInterval timeToCache, boolean readExpired, boolean verbose, boolean haltOnError)
  {
    VectorSet vectorSet = new VectorSet(this, serverURL, name, properties, symbolizer, deleteSymbolizer, downloadPriority, timeToCache, readExpired, verbose, haltOnError);
    if (_context != null)
    {
      vectorSet.initialize(_context);
    }
    _vectorSets.add(vectorSet);
    _vectorSetsSize = _vectorSets.size();
  }

  public final void removeAllVectorSets()
  {
    for (int i = 0; i < _vectorSetsSize; i++)
    {
      VectorSet vectorSet = _vectorSets.get(i);
      if (vectorSet != null)
         vectorSet.dispose();
    }
    _vectorSets.clear();
    _vectorSetsSize = 0;
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _errors.clear();
    boolean busyFlag = false;
    boolean errorFlag = false;
  
    for (int i = 0; i < _vectorSetsSize; i++)
    {
      VectorSet vectorSet = _vectorSets.get(i);
      final RenderState childRenderState = vectorSet.getRenderState(rc);
  
      final RenderState_Type childRenderStateType = childRenderState._type;
  
      if (childRenderStateType == RenderState_Type.RENDER_ERROR)
      {
        errorFlag = true;
  
        final java.util.ArrayList<String> childErrors = childRenderState.getErrors();
        _errors.addAll(childErrors);
      }
      else if (childRenderStateType == RenderState_Type.RENDER_BUSY)
      {
        busyFlag = true;
      }
    }
  
    if (errorFlag)
    {
      return RenderState.error(_errors);
    }
    else if (busyFlag)
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
  }

  public final MarksRenderer getMarksRenderer()
  {
    return _markRenderer;
  }

}