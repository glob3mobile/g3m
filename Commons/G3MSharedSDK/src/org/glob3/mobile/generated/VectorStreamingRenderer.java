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




//class Sector;
//class JSONArray;
//class Geodetic2D;
//class JSONObject;
//class IByteBuffer;
//class GEOObject;
//class IThreadUtils;
//class JSONBaseObject;
//class BoundingVolume;
//class Sphere;
//class IDownloader;
//class Frustum;
//class GEO2DPointGeometry;
//class GEO3DPointGeometry;
//class MarksRenderer;
//class Camera;
//class GEOMeshes;
//class Planet;
//class Mesh;
//class MeshRenderer;


public class VectorStreamingRenderer extends DefaultRenderer
{

  public enum Format
  {
    SERVER,
    PLAIN_FILES;

     public int getValue()
     {
        return this.ordinal();
     }

     public static Format forValue(int value)
     {
        return values()[value];
     }
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class VectorSet;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class Metadata;
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
      final double lon = json.getAsNumber(0).value();
      final double lat = json.getAsNumber(1).value();
    
      return new Geodetic2D(Angle.fromDegrees(lat), Angle.fromDegrees(lon));
    }
    public static VectorStreamingRenderer.Node parseNode(JSONObject json, VectorSet vectorSet, boolean verbose)
    {
      final String id = json.getAsString("id").value();
      final Sector nodeSector = GEOJSONUtils.parseSector(json.getAsArray("nodeSector"));
      //  const double      minHeight     = json->getAsNumber("minHeight", 0);
      final double minHeight = 0;
      final double maxHeight = json.getAsNumber("maxHeight", 0);
      final int clustersCount = (int) json.getAsNumber("clustersCount", 0.0);
      final int featuresCount = (int) json.getAsNumber("featuresCount", 0.0);
    
      java.util.ArrayList<String> childrenIDs = new java.util.ArrayList<String>();
      java.util.ArrayList<Node> children = null;
      final JSONArray childrenJSON = json.getAsArray("children");
      for (int i = 0; i < childrenJSON.size(); i++)
      {
        final JSONString childID = childrenJSON.getAsString(i);
        if (childID != null)
        {
          childrenIDs.add(childID.value());
        }
        else
        {
          final JSONObject jsonChild = childrenJSON.getAsObject(i);
          if (jsonChild != null)
          {
            Node child = parseNode(jsonChild, vectorSet, verbose);
            if (child != null)
            {
              if (children == null)
              {
                children = new java.util.ArrayList<Node>();
              }
              children.add(child);
            }
          }
        }
      }
    
      return new Node(vectorSet, id, nodeSector, minHeight, maxHeight, clustersCount, featuresCount, childrenIDs, children, verbose);
    }

  }


  public static class Cluster
  {
    private final Geodetic2D _position;
    private final long _size;
    public Cluster(Geodetic2D position, long size)
    {
       _position = position;
       _size = size;

    }

    public final Geodetic2D getPosition()
    {
      return _position;
    }

    public final long getSize()
    {
      return _size;
    }

    public void dispose()
    {
      if (_position != null)
         _position.dispose();
    }

  }


  public static class ChildrenParserAsyncTask extends GAsyncTask
  {
    private Node _node;
    private boolean _verbose;
    private boolean _isCanceled;
    private IByteBuffer _buffer;

    private java.util.ArrayList<Node> _children;

    public ChildrenParserAsyncTask(Node node, boolean verbose, IByteBuffer buffer)
    {
       _node = node;
       _verbose = verbose;
       _buffer = buffer;
       _isCanceled = false;
       _children = null;
      _node._retain();
    }

    public void dispose()
    {
      _node._childrenTask = null;
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
    
      super.dispose();
    }

    public final void cancel()
    {
      _isCanceled = true;
    }

    public final void runInBackground(G3MContext context)
    {
      if (_isCanceled)
      {
        return;
      }
    
      final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(_buffer);
    
      if (_buffer != null)
         _buffer.dispose();
      _buffer = null;
    
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
    }

    public final void onPostExecute(G3MContext context)
    {
      if (_isCanceled)
      {
        return;
      }
    
      _node.parsedChildren(_children);
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
      super.dispose();
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("\"%s\": Downloaded children (bytes=%d)",
                                   _node.getFullName(),
                                   buffer.size());
      }
      _node._childrenTask = new ChildrenParserAsyncTask(_node, _verbose, buffer);
      _threadUtils.invokeAsyncTask(_node._childrenTask, true);
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
    private boolean _isCanceled;
    private IByteBuffer _buffer;

    private java.util.ArrayList<Cluster> _clusters;
    private GEOObject _features;
    private java.util.ArrayList<Node> _children;

    private java.util.ArrayList<VectorStreamingRenderer.Cluster> parseClusters(JSONArray jsonArray)
    {
      if ((jsonArray == null) || (jsonArray.size() == 0))
      {
        return null;
      }
    
      java.util.ArrayList<VectorStreamingRenderer.Cluster> clusters = new java.util.ArrayList<VectorStreamingRenderer.Cluster>();
      final int clustersCount = jsonArray.size();
      for (int i = 0; i < clustersCount; i++)
      {
        final JSONObject clusterJson = jsonArray.getAsObject(i);
        final Geodetic2D position = GEOJSONUtils.parseGeodetic2D(clusterJson.getAsArray("position"));
        final long size = (long) clusterJson.getAsNumber("size").value();
    
        clusters.add(new Cluster(position, size));
      }
    
      return clusters;
    }
    private GEOObject parseFeatures(JSONObject jsonObject, Planet planet)
    {
      if (jsonObject == null)
      {
        return null;
      }
    
      final String type = jsonObject.getAsString("type", "");
      if (type.equals("MeshCollection"))
      {
        return parseMeshes(jsonObject.getAsObject("meshes"), planet);
      }
    
      return GEOJSONParser.parse(jsonObject, _verbose);
    }
    private GEOMeshes parseMeshes(JSONObject jsonObject, Planet planet)
    {
      if (jsonObject == null)
      {
        return null;
      }
    
      java.util.ArrayList<Mesh> meshes = G3MMeshParser.parse(jsonObject, planet);
      if (meshes.isEmpty())
      {
        return null;
      }
    
      return new GEOMeshes(meshes);
    }
    private java.util.ArrayList<VectorStreamingRenderer.Node> parseChildren(JSONArray jsonArray)
    {
      if ((jsonArray == null) || (jsonArray.size() == 0))
      {
        return null;
      }
    
      java.util.ArrayList<Node> result = new java.util.ArrayList<Node>();
      for (int i = 0; i < jsonArray.size(); i++)
      {
        final JSONObject nodeJSON = jsonArray.getAsObject(i);
        result.add(GEOJSONUtils.parseNode(nodeJSON, _node.getVectorSet(), _verbose));
      }
    
      return result;
    }

    public FeaturesParserAsyncTask(Node node, boolean verbose, IByteBuffer buffer)
    {
       _node = node;
       _verbose = verbose;
       _buffer = buffer;
       _isCanceled = false;
       _clusters = null;
       _features = null;
       _children = null;
      _node._retain();
    }

    public void dispose()
    {
      _node._featuresTask = null;
      _node._release();
    
      if (_buffer != null)
         _buffer.dispose();
    
      if (_clusters != null)
      {
        for (int i = 0; i < _clusters.size(); i++)
        {
          Cluster cluster = _clusters.get(i);
          if (cluster != null)
             cluster.dispose();
        }
        _clusters = null;
      }
    
      if (_features != null)
         _features.dispose();
    
      if (_children != null)
      {
        for (int i = 0; i < _children.size(); i++)
        {
          Node child = _children.get(i);
          child._release();
        }
        _children = null;
      }
    
      super.dispose();
    }

    public final void cancel()
    {
      _isCanceled = true;
    }

    public final void runInBackground(G3MContext context)
    {
      if (_isCanceled)
      {
        return;
      }
    
      final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(_buffer);
    
      if (_buffer != null)
         _buffer.dispose();
      _buffer = null;
    
      if (jsonBaseObject != null)
      {
        final JSONObject jsonObject = jsonBaseObject.asObject();
    
        _clusters = parseClusters(jsonObject.getAsArray("clusters"));
        _features = parseFeatures(jsonObject.getAsObject("features"), context.getPlanet());
        _children = parseChildren(jsonObject.getAsArray("children"));
    
        if (jsonBaseObject != null)
           jsonBaseObject.dispose();
      }
    }

    public final void onPostExecute(G3MContext context)
    {
      if (_isCanceled)
      {
        return;
      }
      _node.parsedFeatures(_clusters, _features, _children);
      _clusters = null; // moved ownership to _node
      _features = null; // moved ownership to _node
      _children = null; // moved ownership to _node
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
      super.dispose();
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("\"%s\": Downloaded features (bytes=%d)",
                                   _node.getFullName(),
                                   buffer.size());
      }
      _node._featuresTask = new FeaturesParserAsyncTask(_node, _verbose, buffer);
      _threadUtils.invokeAsyncTask(_node._featuresTask, true);
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


  public static class NodeAllMarkFilter extends MarkFilter
  {
    private final String _clusterToken;
    private final String _featureToken;

    public NodeAllMarkFilter(Node node)
    {
       _clusterToken = node.getClusterToken();
       _featureToken = node.getFeatureToken();
    }

    public final boolean test(Mark mark)
    {
      final String token = mark.getToken();
      return ((_clusterToken.equals(token)) || (_featureToken.equals(token)));
    }

  }


  public static class NodeAllMeshFilter extends MeshFilter
  {
    private final String _featureToken;

    public NodeAllMeshFilter(Node node)
    {
       _featureToken = node.getFeatureToken();
    }

    public final boolean test(Mesh mesh)
    {
      final String token = mesh.getToken();
      return (_featureToken.equals(token));
    }

  }


  public static class NodeClusterMarkFilter extends MarkFilter
  {
    private final String _clusterToken;

    public NodeClusterMarkFilter(Node node)
    {
       _clusterToken = node.getClusterToken();
    }

    public final boolean test(Mark mark)
    {
      final String token = mark.getToken();
      return (_clusterToken.equals(token));
    }

  }



  public static class Node extends RCObject
  {
    private final VectorSet _vectorSet;
    private Node _parent;
    private final String _id;
    private final Sector _nodeSector;
    private final double _minHeight;
    private final double _maxHeight;
    private final int _clustersCount;
    private final int _featuresCount;

    private final java.util.ArrayList<String> _childrenIDs;

    private java.util.ArrayList<Node> _children;
    private int _childrenSize;

    private final boolean _verbose;

    private java.util.ArrayList<Cluster> _clusters;

    private Sphere _boundingSphere;
    private BoundingVolume getBoundingVolume(G3MRenderContext rc)
    {
      if (_boundingSphere == null)
      {
        final Planet planet = rc.getPlanet();
    
        java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(10);
        points.add( planet.toCartesian( _nodeSector.getNE()    , _minHeight ) );
        points.add( planet.toCartesian( _nodeSector.getNE()    , _maxHeight ) );
        points.add( planet.toCartesian( _nodeSector.getNW()    , _minHeight ) );
        points.add( planet.toCartesian( _nodeSector.getNW()    , _maxHeight ) );
        points.add( planet.toCartesian( _nodeSector.getSE()    , _minHeight ) );
        points.add( planet.toCartesian( _nodeSector.getSE()    , _maxHeight ) );
        points.add( planet.toCartesian( _nodeSector.getSW()    , _minHeight ) );
        points.add( planet.toCartesian( _nodeSector.getSW()    , _maxHeight ) );
        points.add( planet.toCartesian( _nodeSector.getCenter(), _minHeight ) );
        points.add( planet.toCartesian( _nodeSector.getCenter(), _maxHeight ) );
    
        _boundingSphere = Sphere.enclosingSphere(points, 0.1);
    
        if (_parent != null)
        {
          _parent.updateBoundingSphereWith(rc, _boundingSphere);
        }
      }
    
      return _boundingSphere;
    }

    private IDownloader _downloader;
    private boolean _loadingChildren;

    private boolean isVisible(G3MRenderContext rc, VectorStreamingRenderer.VectorSet vectorSet, Frustum frustumInModelCoordinates)
    {
      if ((_nodeSector._deltaLatitude._degrees >= vectorSet._minSectorSize._degrees) || (_nodeSector._deltaLongitude._degrees >= vectorSet._minSectorSize._degrees))
      {
        return true;
      }
    
      return getBoundingVolume(rc).touchesFrustum(frustumInModelCoordinates);
    }

    private boolean _loadedFeatures;
    private boolean _loadingFeatures;

    private boolean isBigEnough(G3MRenderContext rc, VectorStreamingRenderer.VectorSet vectorSet)
    {
      if ((_nodeSector._deltaLatitude._degrees >= vectorSet._minSectorSize._degrees) || (_nodeSector._deltaLongitude._degrees >= vectorSet._minSectorSize._degrees))
      {
        return true;
      }
    
      final double projectedArea = getBoundingVolume(rc).projectedArea(rc);
      return (projectedArea >= vectorSet._minProjectedArea);
    }

    private boolean _isBeingRendered;

    private long _featuresRequestID;
    private void loadFeatures(G3MRenderContext rc)
    {
      //  if (_verbose) {
      //    ILogger::instance()->logInfo("\"%s\": Downloading features for node \'%s\'",
      //                                 _vectorSet->getName().c_str(),
      //                                 _id.c_str());
      //  }
    
      _downloader = rc.getDownloader();
      final long depthPriority = 10000 - getDepth();
      _featuresRequestID = _downloader.requestBuffer(_vectorSet.getNodeFeaturesURL(_id), _vectorSet.getDownloadPriority() + depthPriority + _featuresCount + _clustersCount, _vectorSet.getTimeToCache(), _vectorSet.getReadExpired(), new NodeFeaturesDownloadListener(this, rc.getThreadUtils(), _verbose), true);
    }
    private void unloadFeatures()
    {
      _loadedFeatures = false;
      _loadingFeatures = false;
    
      if (_clusters != null)
      {
        for (int i = 0; i < _clusters.size(); i++)
        {
          Cluster cluster = _clusters.get(i);
          if (cluster != null)
             cluster.dispose();
        }
        _clusters = null;
        _clusters = null;
      }
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
      final int childrenIDsSize = _childrenIDs.size();
      if (childrenIDsSize == 0)
      {
        java.util.ArrayList<Node> children = new java.util.ArrayList<Node>();
        parsedChildren(children);
        return;
      }
    
      //  if (_verbose) {
      //    ILogger::instance()->logInfo("\"%s\": Downloading children for node \'%s\'",
      //                                 _vectorSet->getName().c_str(),
      //                                 _id.c_str());
      //  }
    
      _downloader = rc.getDownloader();
      final long depthPriority = 10000 - getDepth();
      _childrenRequestID = _downloader.requestBuffer(_vectorSet.getNodeChildrenURL(_id, _childrenIDs), _vectorSet.getDownloadPriority() + depthPriority, _vectorSet.getTimeToCache(), _vectorSet.getReadExpired(), new NodeChildrenDownloadListener(this, rc.getThreadUtils(), _verbose), true);
    }
    private void unloadChildren()
    {
      if (_children != null)
      {
        for (int i = 0; i < _childrenSize; i++)
        {
          Node child = _children.get(i);
          child.unload();
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


    private void removeFeaturesSymbols()
    {
      int removed = 0;
    
      removed += _vectorSet.getMarksRenderer().removeAllMarks(new NodeAllMarkFilter(this), true, true); // deleteMarks -  animated
    
      removed += _vectorSet.getMeshRenderer().removeAllMeshes(new NodeAllMeshFilter(this), true); // deleteMeshes
    
      if (_verbose && (removed > 0))
      {
        ILogger.instance().logInfo("\"%s\": Removed %d features symbols",
                                   getFullName(),
                                   removed);
      }
    }

    private int _featureSymbolsCount;
    private int _clusterSymbolsCount;

    private void childRendered()
    {
      if (_clusters != null)
      {
        if (_clusters.size() > 0)
        {
          if (_clusterSymbolsCount > 0)
          {
            int removed = 0;
            removed = _vectorSet.getMarksRenderer().removeAllMarks(new NodeClusterMarkFilter(this), true, true); // deleteMarks -  animated
    
            _clusterSymbolsCount -= removed;
    
            if (_verbose && (removed > 0))
            {
              ILogger.instance().logInfo("\"%s\": Removed %d cluster-marks",
                                         getFullName(),
                                         removed);
            }
    
          }
        }
      }
    }
    private void childStopRendered()
    {
      if (_clusters != null)
      {
        if (_clusters.size() > 0)
        {
          if (_clusterSymbolsCount <= 0)
          {
            createClusterMarks();
            // Checking to ensure no children marks are drawn.
            if ((_children != null) && (_children.size() > 0))
            {
              for (int i = 0; i < _children.size(); i++)
              {
                Node child = _children.get(i);
                if (child._featureSymbolsCount > 0)
                {
                  child.unload();
                }
              }
            }
          }
        }
      }
    }

    private void createClusterMarks()
    {
      _clusterSymbolsCount = _vectorSet.symbolizeClusters(this, _clusters);
    
      if (_verbose && (_clusterSymbolsCount > 0))
      {
        ILogger.instance().logInfo("\"%s\": Created %d cluster-symbols",
                                   getFullName(),
                                   _clusterSymbolsCount);
      }
    }

    private void cancelTasks()
    {
      if (_featuresTask != null)
      {
        _featuresTask.cancel();
      }
      if (_childrenTask != null)
      {
        _childrenTask.cancel();
      }
    }

    private void setParent(Node parent)
    {
      if (_parent != null)
      {
        throw new RuntimeException("Node already has a parent");
      }
      _parent = parent;
      _parent._retain();
    }

    private void setChildren(java.util.ArrayList<Node> children)
    {
      _loadingChildren = false;
    
      if ((children == null) && (_children != null))
      {
        return;
      }
    
      if (children != _children)
      {
        if (_children != null)
        {
          for (int i = 0; i < _childrenSize; i++)
          {
            Node child = _children.get(i);
            child.unload();
            child._release();
          }
          _children = null;
        }
    
        _children = children;
        _childrenSize = (children == null) ? 0 : _children.size();
        if (_children != null)
        {
          for (int i = 0; i < _childrenSize; i++)
          {
            Node child = _children.get(i);
            child.setParent(this);
          }
        }
      }
    }

    private int getDepth()
    {
      return (_parent == null) ? 1 : (_parent.getDepth() + 1);
    }

    private void updateBoundingSphereWith(G3MRenderContext rc, Sphere childSphere)
    {
      getBoundingVolume(rc); // force _boundingSphere creation
      if ((_boundingSphere == null) || childSphere.fullContainedInSphere(_boundingSphere))
      {
        return;
      }
    
      Sphere old = _boundingSphere;
      _boundingSphere = _boundingSphere.mergedWithSphere(childSphere, 0.1);
      if (old != null)
         old.dispose();
      if (_parent != null)
      {
        _parent.updateBoundingSphereWith(rc, _boundingSphere);
      }
    }

    public void dispose()
    {
      unload();
    
      if (_clusters != null)
      {
        for (int i = 0; i < _clusters.size(); i++)
        {
          Cluster cluster = _clusters.get(i);
          if (cluster != null)
             cluster.dispose();
        }
        _clusters = null;
      }
    
      if (_nodeSector != null)
         _nodeSector.dispose();
      if (_boundingSphere != null)
         _boundingSphere.dispose();
    
      if (_parent != null)
      {
        _parent._release();
      }
    
      _vectorSet._release();
    
      super.dispose();
    }

    public ChildrenParserAsyncTask _childrenTask;
    public FeaturesParserAsyncTask _featuresTask;

    public Node(VectorSet vectorSet, String id, Sector nodeSector, double minHeight, double maxHeight, int clustersCount, int featuresCount, java.util.ArrayList<String> childrenIDs, java.util.ArrayList<Node> children, boolean verbose)
    {
       _parent = null;
       _vectorSet = vectorSet;
       _id = id;
       _nodeSector = nodeSector;
       _minHeight = minHeight;
       _maxHeight = maxHeight;
       _clustersCount = clustersCount;
       _featuresCount = featuresCount;
       _childrenIDs = childrenIDs;
       _verbose = verbose;
       _loadedFeatures = false;
       _loadingFeatures = false;
       _children = children;
       _childrenSize = (children == null) ? 0 : children.size();
       _loadingChildren = false;
       _isBeingRendered = false;
       _boundingSphere = null;
       _featuresRequestID = -1;
       _childrenRequestID = -1;
       _downloader = null;
       _clusters = null;
       _clusterSymbolsCount = 0;
       _featureSymbolsCount = 0;
       _childrenTask = null;
       _featuresTask = null;
      setChildren(children);
      _vectorSet._retain();
    }

    public final void unload()
    {
      cancelTasks();
    
      if (_loadingFeatures)
      {
        _loadingFeatures = false;
        cancelLoadFeatures();
      }
    
      if (_loadingChildren)
      {
        _loadingChildren = false;
        cancelLoadChildren();
      }
    
      if (_loadedFeatures)
      {
        _loadedFeatures = false;
        unloadFeatures();
      }
    
      unloadChildren();
    
      if (_parent != null)
      {
        _parent.childStopRendered();
      }
    
      removeFeaturesSymbols();
    
    }

    public final VectorSet getVectorSet()
    {
      return _vectorSet;
    }

    public final String getFullName()
    {
      return _vectorSet.getName() + "/" + _id;
    }

    public final String getFeatureToken()
    {
      return _id + "_F_" + _vectorSet.getName();
    }

    public final String getClusterToken()
    {
      return _id + "_C_" + _vectorSet.getName();
    }

    public final long render(G3MRenderContext rc, VectorStreamingRenderer.VectorSet vectorSet, Frustum frustumInModelCoordinates, GLState glState)
    {
      long renderedCount = 0;
    
      boolean wasRendered = false;
    
      final boolean visible = isVisible(rc, vectorSet, frustumInModelCoordinates);
      if (visible)
      {
        final boolean bigEnough = isBigEnough(rc, vectorSet);
        if (bigEnough)
        {
          wasRendered = true;
          if (_loadedFeatures)
          {
            renderedCount += _featureSymbolsCount + _clusterSymbolsCount;
            if (_parent != null)
            {
              _parent.childRendered();
            }
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
                renderedCount += child.render(rc, vectorSet, frustumInModelCoordinates, glState);
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
      }
    
      if (_isBeingRendered != wasRendered)
      {
        if (_isBeingRendered)
        {
          unload();
        }
        _isBeingRendered = wasRendered;
      }
    
      return renderedCount;
    }

    public final void errorDownloadingFeatures()
    {
      // do nothing by now
    }

    public final void parsedFeatures(java.util.ArrayList<Cluster> clusters, GEOObject features, java.util.ArrayList<Node> children)
    {
      _loadedFeatures = true;
      _loadingFeatures = false;
      _featuresRequestID = -1;
    
      parsedChildren(children);
    
      _featureSymbolsCount = 0;
    
      if (features != null)
      {
        _featureSymbolsCount = features.symbolize(_vectorSet, this);
        if (features != null)
           features.dispose();
    
        if (_verbose && (_featureSymbolsCount > 0))
        {
          ILogger.instance().logInfo("\"%s\": Created %d feature-symbols",
                                     getFullName(),
                                     _featureSymbolsCount);
        }
      }
    
      if (_clusters != null)
      {
        for (int i = 0; i < _clusters.size(); i++)
        {
          Cluster cluster = _clusters.get(i);
          if (cluster != null)
             cluster.dispose();
        }
        _clusters = null;
        _clusters = null;
        _clusterSymbolsCount = 0;
      }
    
      if (clusters != null)
      {
        _clusters = clusters;
        createClusterMarks();
      }
    }

    public final void errorDownloadingChildren()
    {
      // do nothing by now
    }

    public final void parsedChildren(java.util.ArrayList<Node> children)
    {
      setChildren(children);
    }

  }


  public static class MetadataParserAsyncTask extends GAsyncTask
  {
    private VectorSet _vectorSet;
    private final boolean _verbose;
    private IByteBuffer _buffer;

    private boolean _parsingError;

    private Metadata _metadata;
    private java.util.ArrayList<Node> _rootNodes;

    public MetadataParserAsyncTask(VectorSet vectorSet, boolean verbose, IByteBuffer buffer)
    {
       _vectorSet = vectorSet;
       _verbose = verbose;
       _buffer = buffer;
       _parsingError = false;
       _metadata = null;
       _rootNodes = null;
      _vectorSet._retain();
    }

    public void dispose()
    {
      if (_buffer != null)
         _buffer.dispose();
    
      _metadata = null;
    
      if (_rootNodes != null)
      {
        for (int i = 0; i < _rootNodes.size(); i++)
        {
          Node node = _rootNodes.get(i);
          node._release();
        }
        _rootNodes = null;
      }
    
      _vectorSet._release();
    
      super.dispose();
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
          final Sector sector = GEOJSONUtils.parseSector(jsonObject.getAsArray("sector"));
          final long clustersCount = (long) jsonObject.getAsNumber("clustersCount", 0);
          final long featuresCount = (long) jsonObject.getAsNumber("featuresCount", 0);
          final int nodesCount = (int) jsonObject.getAsNumber("nodesCount").value();
          final int minNodeDepth = (int) jsonObject.getAsNumber("minNodeDepth").value();
          final int maxNodeDepth = (int) jsonObject.getAsNumber("maxNodeDepth").value();
          final String language = jsonObject.getAsString("language").value();
          final String nameFieldName = jsonObject.getAsString("nameFieldName").value();
          final String urlFieldName = jsonObject.getAsString("urlFieldName").value();
          final MagnitudeMetadata magnitudeMetadata = MagnitudeMetadata.fromJSON(jsonObject.getAsObject("magnitude"));
    
          _metadata = new Metadata(sector, clustersCount, featuresCount, nodesCount, minNodeDepth, maxNodeDepth, language, nameFieldName, urlFieldName, magnitudeMetadata);
    
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
        _vectorSet.parsedMetadata(_metadata, _rootNodes);
        _metadata = null; // moved ownership to _vectorSet
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
      _vectorSet._retain();
    }

    public void dispose()
    {
      _vectorSet._release();
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


  public static class MagnitudeMetadata
  {
    public static VectorStreamingRenderer.MagnitudeMetadata fromJSON(JSONObject jsonObject)
    {
      if (jsonObject == null)
      {
        return null;
      }
    
      final String name = jsonObject.getAsString("name").value();
      final double min = jsonObject.getAsNumber("min").value();
      final double max = jsonObject.getAsNumber("max").value();
      final double average = jsonObject.getAsNumber("average").value();
    
      return new MagnitudeMetadata(name, min, max, average);
    }

    public final String _name;
    public final double _min;
    public final double _max;
    public final double _average;

    public void dispose()
    {
    }

    private MagnitudeMetadata(String name, double min, double max, double average)
    {
       _name = name;
       _min = min;
       _max = max;
       _average = average;

    }

  }


  public abstract static class VectorSetSymbolizer
  {
    public void dispose()
    {
    }

    public abstract Mark createGeometryMark(VectorStreamingRenderer.Metadata metadata, VectorStreamingRenderer.Node node, GEO2DPointGeometry geometry);

    public abstract Mark createGeometryMark(VectorStreamingRenderer.Metadata metadata, VectorStreamingRenderer.Node node, GEO3DPointGeometry geometry);

    public abstract Mark createClusterMark(VectorStreamingRenderer.Metadata metadata, VectorStreamingRenderer.Node node, VectorStreamingRenderer.Cluster cluster);
  }


  public static class Metadata
  {
    public final Sector _sector;
    public final long _clustersCount;
    public final long _featuresCount;
    public final int _nodesCount;
    public final int _minNodeDepth;
    public final int _maxNodeDepth;
    public final String _language;
    public final String _nameFieldName;
    public final String _urlFieldName;
    public final MagnitudeMetadata _magnitudeMetadata;

    public Metadata(Sector sector, long clustersCount, long featuresCount, int nodesCount, int minNodeDepth, int maxNodeDepth, String language, String nameFieldName, String urlFieldName, MagnitudeMetadata magnitudeMetadata)
    {
       _sector = sector;
       _clustersCount = clustersCount;
       _featuresCount = featuresCount;
       _nodesCount = nodesCount;
       _minNodeDepth = minNodeDepth;
       _maxNodeDepth = maxNodeDepth;
       _language = language;
       _nameFieldName = nameFieldName;
       _urlFieldName = urlFieldName;
       _magnitudeMetadata = magnitudeMetadata;

    }

    public void dispose()
    {
      if (_sector != null)
         _sector.dispose();
    }

  }

  public static class VectorSet extends RCObject
  {
    private VectorStreamingRenderer _renderer;
    private final URL _serverURL;
    private VectorSetSymbolizer _symbolizer;
    private final String _name;
    private final boolean _deleteSymbolizer;
    private final long _downloadPriority;
    private final TimeInterval _timeToCache;
    private final boolean _readExpired;
    private final boolean _verbose;
    private final boolean _haltOnError;
    private final Format _format;

    private final String _properties;

    private boolean _downloadingMetadata;
    private boolean _errorDownloadingMetadata;
    private boolean _errorParsingMetadata;

    private Metadata _metadata;
    private java.util.ArrayList<Node> _rootNodes;
    private int _rootNodesSize;

    private long _lastRenderedCount;

    private URL getMetadataURL()
    {
      if (_format == VectorStreamingRenderer.Format.SERVER)
      {
        return new URL(_serverURL, _name);
      }
      return new URL(_serverURL, _name + "/metadata.json");
    }

    private String toNodesDirectories(String nodeID)
    {
      IStringBuilder isb = IStringBuilder.newStringBuilder();
      final IStringUtils su = IStringUtils.instance();
      isb.addString("nodes/");
      final int length = nodeID.length();
      for (int i = 0; i < length; i++)
      {
        final String c = su.substring(nodeID, i, i+1);
        isb.addString(c);
        isb.addString("/");
      }
      final String result = isb.getString();
      if (isb != null)
         isb.dispose();
      return result;
    }

    public void dispose()
    {
      if (_rootNodes != null)
      {
        for (int i = 0; i < _rootNodesSize; i++)
        {
          Node node = _rootNodes.get(i);
          node.unload();
          node._release();
        }
        _rootNodes = null;
      }
    
      if (_deleteSymbolizer)
      {
        _symbolizer = null;
      }
    
      _metadata = null;
    }

    public final Angle _minSectorSize ;
    public final double _minProjectedArea;

    public VectorSet(VectorStreamingRenderer renderer, URL serverURL, String name, String properties, VectorSetSymbolizer symbolizer, boolean deleteSymbolizer, long downloadPriority, TimeInterval timeToCache, boolean readExpired, boolean verbose, boolean haltOnError, Format format, Angle minSectorSize, double minProjectedArea)
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
       _format = format;
       _downloadingMetadata = false;
       _errorDownloadingMetadata = false;
       _errorParsingMetadata = false;
       _metadata = null;
       _rootNodes = null;
       _rootNodesSize = 0;
       _lastRenderedCount = 0;
       _minSectorSize = new Angle(minSectorSize);
       _minProjectedArea = minProjectedArea;

    }

    public final URL getNodeFeaturesURL(String nodeID)
    {
      if (_format == VectorStreamingRenderer.Format.SERVER)
      {
        return new URL(_serverURL, _name + "/features" + "?node=" + nodeID + "&properties=" + _properties, true);
      }
      return new URL(_serverURL, _name + "/" + toNodesDirectories(nodeID) + "/features.json");
    }

    public final URL getNodeChildrenURL(String nodeID, java.util.ArrayList<String> childrenIDs)
    {
      if (_format == VectorStreamingRenderer.Format.SERVER)
      {
        String nodes = "";
        final int childrenIDsSize = childrenIDs.size();
    
        for (int i = 0; i < childrenIDsSize; i++)
        {
          if (i > 0)
          {
            nodes += "|";
          }
          nodes += childrenIDs.get(i);
        }
    
        return new URL(_serverURL, _name + "?nodes=" + nodes, true);
    
      }
      return new URL(_serverURL, _name + "/" + toNodesDirectories(nodeID) + "/children.json");
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

    public final void initialize(G3MContext context)
    {
      _downloadingMetadata = true;
      _errorDownloadingMetadata = false;
      _errorParsingMetadata = false;
    
      if (_verbose)
      {
        ILogger.instance().logInfo("\"%s\": Downloading metadata", _name);
      }
    
      context.getDownloader().requestBuffer(getMetadataURL(), _downloadPriority, _timeToCache, _readExpired, new MetadataDownloadListener(this, context.getThreadUtils(), _verbose), true);
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
          return RenderState.error("Error downloading metadata of \"" + _name + "\" from \"" + _serverURL._path + "\"");
        }
    
        if (_errorParsingMetadata)
        {
          return RenderState.error("Error parsing metadata of \"" + _name + "\" from \"" + _serverURL._path + "\"");
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
    public final void parsedMetadata(Metadata metadata, java.util.ArrayList<Node> rootNodes)
    {
      _downloadingMetadata = false;
    
      _metadata = metadata;
      _rootNodes = rootNodes;
      _rootNodesSize = _rootNodes.size();
    
      if (_verbose)
      {
        ILogger.instance().logInfo("\"%s\": Metadata", _name);
        ILogger.instance().logInfo("   Sector        : %s", _metadata._sector.description());
        ILogger.instance().logInfo("   Clusters Count: %d",   _metadata._clustersCount);
        ILogger.instance().logInfo("   Features Count: %d",   _metadata._featuresCount);
        ILogger.instance().logInfo("   Nodes Count   : %d", _metadata._nodesCount);
        ILogger.instance().logInfo("   Depth         : %d/%d", _metadata._minNodeDepth, _metadata._maxNodeDepth);
        ILogger.instance().logInfo("   Root Nodes    : %d", _rootNodesSize);
      }
    
    }

    public final void render(G3MRenderContext rc, Frustum frustumInModelCoordinates, GLState glState)
    {
      if (_rootNodesSize > 0)
      {
        long renderedCount = 0;
        for (int i = 0; i < _rootNodesSize; i++)
        {
          Node rootNode = _rootNodes.get(i);
          renderedCount += rootNode.render(rc, this, frustumInModelCoordinates, glState);
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

    public final int symbolizeGeometry(Node node, GEO2DPointGeometry geometry)
    {
      int count = 0;
    
      {
        Mark mark = _symbolizer.createGeometryMark(_metadata, node, geometry);
        if (mark != null)
        {
          count++;
          mark.setToken(node.getFeatureToken());
          getMarksRenderer().addMark(mark);
        }
      }
    
      return count;
    }

    public final int symbolizeGeometry(Node node, GEO3DPointGeometry geometry)
    {
      int count = 0;
    
      {
        Mark mark = _symbolizer.createGeometryMark(_metadata, node, geometry);
        if (mark != null)
        {
          count++;
          mark.setToken(node.getFeatureToken());
          getMarksRenderer().addMark(mark);
        }
      }
    
      return count;
    }

    public final int symbolizeClusters(Node node, java.util.ArrayList<Cluster> clusters)
    {
      int counter = 0;
      if (clusters != null)
      {
        final int clustersCount = clusters.size();
        for (int i = 0; i < clustersCount; i++)
        {
          final Cluster cluster = clusters.get(i);
          if (cluster != null)
          {
            Mark mark = _symbolizer.createClusterMark(_metadata, node, cluster);
            if (mark != null)
            {
              mark.setToken(node.getClusterToken());
              _renderer.getMarksRenderer().addMark(mark);
              counter++;
            }
          }
        }
      }
    
      return counter;
    }

    public final int symbolizeMeshes(Node node, java.util.ArrayList<Mesh> meshes)
    {
      int count = 0;
    
      for (int i = 0; i < meshes.size(); i++)
      {
        Mesh mesh = meshes.get(i);
        if (mesh != null)
        {
          count++;
          mesh.setToken(node.getFeatureToken());
          getMeshRenderer().addMesh(mesh);
        }
      }
    
      return count;
    }

    public final MarksRenderer getMarksRenderer()
    {
      return _renderer.getMarksRenderer();
    }

    public final MeshRenderer getMeshRenderer()
    {
      return _renderer.getMeshRenderer();
    }

  }




  private MarksRenderer _marksRenderer;
  private MeshRenderer _meshRenderer;

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


  public VectorStreamingRenderer(MarksRenderer marksRenderer, MeshRenderer meshRenderer)
  {
     _marksRenderer = marksRenderer;
     _meshRenderer = meshRenderer;
     _vectorSetsSize = 0;
     _glState = new GLState();
  }

  public void dispose()
  {
    for (int i = 0; i < _vectorSetsSize; i++)
    {
      VectorSet vectorSet = _vectorSets.get(i);
      vectorSet._release();
    }
  
    _glState._release();
  
    super.dispose();
  }

  public final MarksRenderer getMarksRenderer()
  {
    return _marksRenderer;
  }

  public final MeshRenderer getMeshRenderer()
  {
    return _meshRenderer;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    if (_vectorSetsSize > 0)
    {
      final Camera camera = rc.getCurrentCamera();
      final Frustum frustumInModelCoordinates = camera.getFrustumInModelCoordinates();
  
      updateGLState(camera);
      _glState.setParent(glState);
  
      for (int i = 0; i < _vectorSetsSize; i++)
      {
        VectorSet vectorSector = _vectorSets.get(i);
        vectorSector.render(rc, frustumInModelCoordinates, _glState);
      }
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

  public final void addVectorSet(URL serverURL, String name, String properties, VectorSetSymbolizer symbolizer, boolean deleteSymbolizer, long downloadPriority, TimeInterval timeToCache, boolean readExpired, boolean verbose, boolean haltOnError, Format format, Angle minSectorSize, double minProjectedArea)
  {
    VectorSet vectorSet = new VectorSet(this, serverURL, name, properties, symbolizer, deleteSymbolizer, downloadPriority, timeToCache, readExpired, verbose, haltOnError, format, minSectorSize, minProjectedArea);
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
      vectorSet._release();
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


}
