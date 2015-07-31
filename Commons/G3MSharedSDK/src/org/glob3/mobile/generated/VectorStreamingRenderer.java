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


public class VectorStreamingRenderer extends DefaultRenderer
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class VectorSet;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class Node;


  public static class GEOJSONUtils
  {
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    GEOJSONUtils();

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
    public static VectorStreamingRenderer.Node parseNode(JSONObject json)
    {
      final String id = json.getAsString("id").value();
      Sector sector = GEOJSONUtils.parseSector(json.getAsArray("sector"));
      int featuresCount = (int) json.getAsNumber("featuresCount").value();
      Geodetic2D averagePosition = GEOJSONUtils.parseGeodetic2D(json.getAsArray("averagePosition"));
    
      java.util.ArrayList<String> children = new java.util.ArrayList<String>();
      final JSONArray childrenJSON = json.getAsArray("children");
      for (int i = 0; i < childrenJSON.size(); i++)
      {
        children.add(childrenJSON.getAsString(i).value());
      }
    
      return new Node(id, sector, featuresCount, averagePosition, children);
    }

  }


  public static class Node
  {
    private final String _id;
    private final Sector _sector;
    private final int _featuresCount;
    private final Geodetic2D _averagePosition;
    private final java.util.ArrayList<String> _children;

    public Node(String id, Sector sector, int featuresCount, Geodetic2D averagePosition, java.util.ArrayList<String> children)
    {
       _id = id;
       _sector = sector;
       _featuresCount = featuresCount;
       _averagePosition = averagePosition;
       _children = children;

    }


    /*
    
     http: //localhost:8080/server-mapboo/public/VectorialStreaming/GEONames-PopulatedPlaces_LOD/features?node=&properties=name|population|featureClass|featureCode
    
     http: //localhost:8080/server-mapboo/public/VectorialStreaming/GEONames-PopulatedPlaces_LOD
    
     */
    
    
    public void dispose()
    {
      if (_sector != null)
         _sector.dispose();
      if (_averagePosition != null)
         _averagePosition.dispose();
    }

    public final long render(G3MRenderContext rc, long cameraTS, GLState glState)
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
      return 0;
    }

  }


  public static class MetadataParserAsyncTask extends GAsyncTask
  {
    private VectorSet _vectorSet;
    private IByteBuffer _buffer;
    private final boolean _verbose;

    private boolean _parsingError;

    private Sector _sector;
    private long _featuresCount;
    private Geodetic2D _averagePosition;
    private int _nodesCount;
    private int _minNodeDepth;
    private int _maxNodeDepth;
    private java.util.ArrayList<Node> _rootNodes;

    public MetadataParserAsyncTask(VectorSet vectorSet, IByteBuffer buffer, boolean verbose)
    {
       _vectorSet = vectorSet;
       _buffer = buffer;
       _verbose = verbose;
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
          if (node != null)
             node.dispose();
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
          Node node = GEOJSONUtils.parseNode(rootNodesJSON.getAsObject(i));
          _rootNodes.add(node);
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
        _sector = null; // moved ownership to pointCloud
        _averagePosition = null; // moved ownership to pointCloud
        _rootNodes = null; // moved ownership to pointCloud
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
        ILogger.instance().logInfo("Downloaded metadata for \"%s\" (bytes=%d)", _vectorSet.getName(), buffer.size());
      }
    
      _threadUtils.invokeAsyncTask(new MetadataParserAsyncTask(_vectorSet, buffer, _verbose), true);
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


  public static class VectorSet
  {
    private final URL _serverURL = new URL();
    private final String _name;
    private final long _downloadPriority;
    private final TimeInterval _timeToCache;
    private final boolean _readExpired;
    private final boolean _verbose;

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


    public VectorSet(URL serverURL, String name, long downloadPriority, TimeInterval timeToCache, boolean readExpired, boolean verbose)
    {
       _serverURL = new URL(serverURL);
       _name = name;
       _downloadPriority = downloadPriority;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
       _verbose = verbose;
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
      if (_sector != null)
         _sector.dispose();
      if (_averagePosition != null)
         _averagePosition.dispose();
      if (_rootNodes != null)
      {
        for (int i = 0; i < _rootNodes.size(); i++)
        {
          Node node = _rootNodes.get(i);
          if (node != null)
             node.dispose();
        }
        _rootNodes = null;
      }
    }

    public final String getName()
    {
      return _name;
    }

    public final void initialize(G3MContext context)
    {
      _downloadingMetadata = true;
      _errorDownloadingMetadata = false;
      _errorParsingMetadata = false;
    
      final URL metadataURL = new URL(_serverURL, _name);
    
      if (_verbose)
      {
        ILogger.instance().logInfo("Downloading metadata for \"%s\"", _name);
      }
    
      context.getDownloader().requestBuffer(metadataURL, _downloadPriority, _timeToCache, _readExpired, new VectorStreamingRenderer.MetadataDownloadListener(this, context.getThreadUtils(), _verbose), true);
    }

    public final RenderState getRenderState(G3MRenderContext rc)
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
    
      if (_verbose)
      {
        ILogger.instance().logInfo("Parsed metadata for \"%s\"", _name);
      }
    
      _sector = sector;
      _featuresCount = featuresCount;
      _averagePosition = averagePosition;
      _nodesCount = nodesCount;
      _minNodeDepth = minNodeDepth;
      _maxNodeDepth = maxNodeDepth;
      _rootNodes = rootNodes;
      _rootNodesSize = _rootNodes.size();
    }

    public final void render(G3MRenderContext rc, long cameraTS, GLState glState)
    {
      if (_rootNodesSize > 0)
      {
        long renderedCount = 0;
        for (int i = 0; i < _rootNodesSize; i++)
        {
          Node rootNode = _rootNodes.get(i);
          renderedCount += rootNode.render(rc, cameraTS, glState);
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

  }


  private int _vectorSetsSize;
  private java.util.ArrayList<VectorSet> _vectorSets = new java.util.ArrayList<VectorSet>();

  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();


  public VectorStreamingRenderer()
  {
     _vectorSetsSize = 0;
  }

  public void dispose()
  {
    for (int i = 0; i < _vectorSetsSize; i++)
    {
      VectorSet vectorSet = _vectorSets.get(i);
      if (vectorSet != null)
         vectorSet.dispose();
    }
  
    //  _glState->_release();
    //  delete _timer;
  
    super.dispose();
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    for (int i = 0; i < _vectorSetsSize; i++)
    {
      final Camera camera = rc.getCurrentCamera();
      final long cameraTS = camera.getTimestamp();
  
      VectorSet vectorSector = _vectorSets.get(i);
      vectorSector.render(rc, cameraTS, glState);
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

  public final void addVectorSet(URL serverURL, String name, long downloadPriority, TimeInterval timeToCache, boolean readExpired, boolean verbose)
  {
    VectorSet vectorSet = new VectorSet(serverURL, name, downloadPriority, timeToCache, readExpired, verbose);
    if (_context != null)
    {
      vectorSet.initialize(_context);
    }
    _vectorSets.add(vectorSet);
    _vectorSetsSize = _vectorSets.size();
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

}