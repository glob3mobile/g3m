package org.glob3.mobile.generated;import java.util.*;

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





//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IThreadUtils;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IByteBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBaseObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONArray;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mark;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DPointGeometry;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class BoundingVolume;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Frustum;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOObject;


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
				children = new java.util.ArrayList<Node*>();
			  }
			  children.add(child);
			}
		  }
		}
	  }
    
	  return new Node(vectorSet, id, nodeSector, clustersCount, featuresCount, childrenIDs, children, verbose);
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D* getPosition() const
	public final Geodetic2D getPosition()
	{
	  return _position;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const long getSize() const
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
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
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
		  _children = new java.util.ArrayList<Node*>();
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



  public static class NodeChildrenDownloadListener implements IBufferDownloadListener
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
	}

	public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
	{
	  if (_verbose)
	  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		ILogger.instance().logInfo("\"%s\": Downloaded children (bytes=%ld)", _node.getFullName().c_str(), buffer.size());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		ILogger.instance().logInfo("\"%s\": Downloaded children (bytes=%d)", _node.getFullName(), buffer.size());
//#endif
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

	private java.util.ArrayList<VectorStreamingRenderer.Cluster> parseClusters(JSONArray clustersJson)
	{
	  if (clustersJson == null)
	  {
		return null;
	  }
    
	  java.util.ArrayList<VectorStreamingRenderer.Cluster> clusters = new java.util.ArrayList<VectorStreamingRenderer.Cluster*>();
	  final int clustersCount = clustersJson.size();
	  for (int i = 0; i < clustersCount; i++)
	  {
		final JSONObject clusterJson = clustersJson.getAsObject(i);
		final Geodetic2D position = GEOJSONUtils.parseGeodetic2D(clusterJson.getAsArray("position"));
		final long size = (long) clusterJson.getAsNumber("size").value();
    
		clusters.add(new Cluster(position, size));
	  }
    
	  return clusters;
	}
	private java.util.ArrayList<VectorStreamingRenderer.Node> parseChildren(JSONBaseObject jsonBaseObject)
	{
	  if (jsonBaseObject == null)
	  {
		return null;
	  }
    
	  final JSONArray jsonArray = jsonBaseObject.asArray();
	  if (jsonArray == null)
	  {
		return null;
	  }
    
	  java.util.ArrayList<Node> result = new java.util.ArrayList<Node*>();
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
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
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
    
		_clusters = parseClusters(jsonObject.get("clusters").asArray());
		_features = GEOJSONParser.parse(jsonObject.get("features").asObject(), _verbose);
		_children = parseChildren(jsonObject.get("children"));
    
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


  public static class NodeFeaturesDownloadListener implements IBufferDownloadListener
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
	}

	public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
	{
	  if (_verbose)
	  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		ILogger.instance().logInfo("\"%s\": Downloaded features (bytes=%ld)", _node.getFullName().c_str(), buffer.size());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		ILogger.instance().logInfo("\"%s\": Downloaded features (bytes=%d)", _node.getFullName(), buffer.size());
//#endif
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


  public static class NodeAllMarksFilter extends MarksFilter
  {
	private String _nodeClusterToken;
	private String _nodeFeatureToken;

	public NodeAllMarksFilter(Node node)
	{
	  _nodeClusterToken = node.getClusterMarkToken();
	  _nodeFeatureToken = node.getFeatureMarkToken();
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean test(const Mark* mark) const
	public final boolean test(Mark mark)
	{
	  final String token = mark.getToken();
	  return ((_nodeClusterToken.equals(token)) || (_nodeFeatureToken.equals(token)));
	}

  }

  public static class NodeClusterMarksFilter extends MarksFilter
  {
	private String _nodeClusterToken;

	public NodeClusterMarksFilter(Node node)
	{
	  _nodeClusterToken = node.getClusterMarkToken();
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean test(const Mark* mark) const
	public final boolean test(Mark mark)
	{
	  final String token = mark.getToken();
	  return (_nodeClusterToken.equals(token));
	}

  }



  public static class Node extends RCObject
  {
	private final VectorSet _vectorSet;
	private Node _parent;
	private final String _id;
	private final Sector _nodeSector;
	private final int _clustersCount;
	private final int _featuresCount;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final java.util.ArrayList<String> _childrenIDs = new java.util.ArrayList<String>();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final java.util.ArrayList<String> _childrenIDs = new internal();
//#endif

	private java.util.ArrayList<Node> _children;
	private int _childrenSize;

	private final boolean _verbose;

	private java.util.ArrayList<Cluster> _clusters;
	private GEOObject _features;

	private BoundingVolume _boundingVolume;
	private BoundingVolume getBoundingVolume(G3MRenderContext rc)
	{
	  if (_boundingVolume == null)
	  {
		final Planet planet = rc.getPlanet();
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		Vector3D[] c = { planet.toCartesian(_nodeSector.getNE()), planet.toCartesian(_nodeSector.getNW()), planet.toCartesian(_nodeSector.getSE()), planet.toCartesian(_nodeSector.getSW()), planet.toCartesian(_nodeSector.getCenter()) };
    
		java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(c, c+5);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(5);
		points.add(planet.toCartesian(_nodeSector.getNE()));
		points.add(planet.toCartesian(_nodeSector.getNW()));
		points.add(planet.toCartesian(_nodeSector.getSE()));
		points.add(planet.toCartesian(_nodeSector.getSW()));
		points.add(planet.toCartesian(_nodeSector.getCenter()));
//#endif
    
		_boundingVolume = Sphere.enclosingSphere(points);
	  }
    
	  return _boundingVolume;
	}

	private IDownloader _downloader;
	private boolean _loadingChildren;

	private boolean isVisible(G3MRenderContext rc, Frustum frustumInModelCoordinates)
	{
	  if ((_nodeSector._deltaLatitude._degrees > 80) || (_nodeSector._deltaLongitude._degrees > 80))
	  {
		return true;
	  }
    
	  return getBoundingVolume(rc).touchesFrustum(frustumInModelCoordinates);
	}

	private boolean _loadedFeatures;
	private boolean _loadingFeatures;

	private boolean isBigEnough(G3MRenderContext rc)
	{
	  if ((_nodeSector._deltaLatitude._degrees >= 80) || (_nodeSector._deltaLongitude._degrees >= 80))
	  {
		return true;
	  }
    
	  final double projectedArea = getBoundingVolume(rc).projectedArea(rc);
	  //return (projectedArea > 350000);
	  return (projectedArea > 1500000);
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
	  _featuresRequestID = _downloader.requestBuffer(_vectorSet.getNodeFeaturesURL(_id), _vectorSet.getDownloadPriority() + _featuresCount + _clustersCount, _vectorSet.getTimeToCache(), _vectorSet.getReadExpired(), new NodeFeaturesDownloadListener(this, rc.getThreadUtils(), _verbose), true);
	}
	private void unloadFeatures()
	{
	  _loadedFeatures = false;
	  _loadingFeatures = false;
    
	  if (_features != null)
		  _features.dispose();
	  _features = null;
    
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
		java.util.ArrayList<Node> children = new java.util.ArrayList<Node*>();
		parsedChildren(children);
		return;
	  }
    
	  //  if (_verbose) {
	  //    ILogger::instance()->logInfo("\"%s\": Downloading children for node \'%s\'",
	  //                                 _vectorSet->getName().c_str(),
	  //                                 _id.c_str());
	  //  }
    
	  _downloader = rc.getDownloader();
	  _childrenRequestID = _downloader.requestBuffer(_vectorSet.getNodeChildrenURL(_id, _childrenIDs), _vectorSet.getDownloadPriority(), _vectorSet.getTimeToCache(), _vectorSet.getReadExpired(), new NodeChildrenDownloadListener(this, rc.getThreadUtils(), _verbose), true);
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


	private void removeMarks()
	{
	  //  if (_verbose) {
	  //    ILogger::instance()->logInfo("\"%s\": Removing marks",
	  //                                 getFullName().c_str());
	  //  }
    
	  int removed = _vectorSet.getMarksRenderer().removeAllMarks(new NodeAllMarksFilter(this), true);
    
	  if (_verbose && removed > 0)
	  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		ILogger.instance().logInfo("\"%s\": Removed %ld marks", getFullName().c_str(), removed);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		ILogger.instance().logInfo("\"%s\": Removed %d marks", getFullName(), removed);
//#endif
	  }
	}

	private long _clusterMarksCount;
	private long _featureMarksCount;

	private void childRendered()
	{
	  if (_clusters != null)
	  {
		if (_clusters.size() > 0)
		{
		  if (_clusterMarksCount > 0)
		  {
			int removed = _vectorSet.getMarksRenderer().removeAllMarks(new NodeClusterMarksFilter(this), true);
    
			_clusterMarksCount -= removed;
    
			if (_verbose && removed > 0)
			{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
			  ILogger.instance().logInfo("\"%s\": Removed %ld cluster-marks", getFullName().c_str(), removed);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
			  ILogger.instance().logInfo("\"%s\": Removed %d cluster-marks", getFullName(), removed);
//#endif
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
		  if (_clusterMarksCount <= 0)
		  {
			createClusterMarks();
			//Checking to ensure no children marks are drawn.
			if (_children != null && _children.size() > 0)
			{
			  for (int i = 0; i<_children.size(); i++)
			  {
				Node child = _children.get(i);
				if (child._featureMarksCount > 0)
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
	  _clusterMarksCount = _vectorSet.createClusterMarks(this, _clusters);
    
	  if (_verbose && (_clusterMarksCount > 0))
	  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		ILogger.instance().logInfo("\"%s\": Created %ld cluster-marks", getFullName().c_str(), _clusterMarksCount);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		ILogger.instance().logInfo("\"%s\": Created %d cluster-marks", getFullName(), _clusterMarksCount);
//#endif
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
		THROW_EXCEPTION("Node already has a parent");
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

	public void dispose()
	{
	  unload();
    
	  if (_features != null)
		  _features.dispose();
    
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
	  if (_boundingVolume != null)
		  _boundingVolume.dispose();
    
	  if (_parent != null)
	  {
		_parent._release();
	  }
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
	}

	public ChildrenParserAsyncTask _childrenTask;
	public FeaturesParserAsyncTask _featuresTask;

	public Node(VectorSet vectorSet, String id, Sector nodeSector, int clustersCount, int featuresCount, java.util.ArrayList<String> childrenIDs, java.util.ArrayList<Node> children, boolean verbose)
	{
		_parent = null;
		_vectorSet = vectorSet;
		_id = id;
		_nodeSector = nodeSector;
		_clustersCount = clustersCount;
		_featuresCount = featuresCount;
		_childrenIDs = childrenIDs;
		_verbose = verbose;
		_loadedFeatures = false;
		_loadingFeatures = false;
		_children = children;
		_childrenSize = children == null ? 0 : children.size();
		_loadingChildren = false;
		_isBeingRendered = false;
		_boundingVolume = null;
		_featuresRequestID = -1;
		_childrenRequestID = -1;
		_downloader = null;
		_clusters = null;
		_features = null;
		_clusterMarksCount = 0;
		_featureMarksCount = 0;
		_childrenTask = null;
		_featuresTask = null;
	  setChildren(children);
	}

	public final void unload()
	{
	  cancelTasks();
    
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
    
	  unloadChildren();
    
	  if (_parent != null)
	  {
		_parent.childStopRendered();
	  }
    
	  removeMarks();
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const VectorSet* getVectorSet() const
	public final VectorSet getVectorSet()
	{
	  return _vectorSet;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getFullName() const
	public final String getFullName()
	{
	  return _vectorSet.getName() + "/" + _id;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getFeatureMarkToken() const
	public final String getFeatureMarkToken()
	{
	  return _id + "_F_" + _vectorSet.getName();
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getClusterMarkToken() const
	public final String getClusterMarkToken()
	{
	  return _id + "_C_" + _vectorSet.getName();
	}

	public final long render(G3MRenderContext rc, Frustum frustumInModelCoordinates, long cameraTS, GLState glState)
	{
    
	  long renderedCount = 0;
    
	  // #warning Show Bounding Volume
	  // getBoundingVolume(rc)->render(rc, glState, Color::red());
    
	  boolean wasRendered = false;
    
	  final boolean visible = isVisible(rc, frustumInModelCoordinates);
	  if (visible)
	  {
		final boolean bigEnough = isBigEnough(rc);
		if (bigEnough)
		{
		  wasRendered = true;
		  if (_loadedFeatures)
		  {
			renderedCount += _featureMarksCount + _clusterMarksCount;
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
    
	  if (_features != null)
		  _features.dispose();
	  _featureMarksCount = 0;
    
	  if (features != null)
	  {
		_features = features;
		_featureMarksCount = _features.createFeatureMarks(_vectorSet, this);
    
		if (_verbose && (_featureMarksCount > 0))
		{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		  ILogger.instance().logInfo("\"%s\": Created %ld feature-marks", getFullName().c_str(), _featureMarksCount);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		  ILogger.instance().logInfo("\"%s\": Created %d feature-marks", getFullName(), _featureMarksCount);
//#endif
		}
		if (_features != null)
			_features.dispose();
		_features = null;
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
		_clusterMarksCount = 0;
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

	private Sector _sector;
	private long _clustersCount;
	private long _featuresCount;
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
		_clustersCount = -1;
		_featuresCount = -1;
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
    
	  if (_rootNodes != null)
	  {
		for (int i = 0; i < _rootNodes.size(); i++)
		{
		  Node node = _rootNodes.get(i);
		  node._release();
		}
		_rootNodes = null;
	  }
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
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
			ILogger.instance().logError("\"%s\": %s", _vectorSet.getName().c_str(), errorCode.c_str());
		  }
		  else
		  {
			final String errorDescription = errorDescriptionJSON.value();
			ILogger.instance().logError("\"%s\": %s (%s)", _vectorSet.getName().c_str(), errorCode.c_str(), errorDescription.c_str());
		  }
		}
		else
		{
		  _sector = GEOJSONUtils.parseSector(jsonObject.getAsArray("sector"));
		  _clustersCount = (long) jsonObject.getAsNumber("clustersCount").value();
		  _featuresCount = (long) jsonObject.getAsNumber("featuresCount").value();
		  _nodesCount = (int) jsonObject.getAsNumber("nodesCount").value();
		  _minNodeDepth = (int) jsonObject.getAsNumber("minNodeDepth").value();
		  _maxNodeDepth = (int) jsonObject.getAsNumber("maxNodeDepth").value();
    
		  final JSONArray rootNodesJSON = jsonObject.getAsArray("rootNodes");
		  _rootNodes = new java.util.ArrayList<Node*>();
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
		_vectorSet.parsedMetadata(_sector, _clustersCount, _featuresCount, _nodesCount, _minNodeDepth, _maxNodeDepth, _rootNodes);
		_sector = null; // moved ownership to _vectorSet
		_rootNodes = null; // moved ownership to _vectorSet
	  }
	}

  }


  public static class MetadataDownloadListener implements IBufferDownloadListener
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		ILogger.instance().logInfo("\"%s\": Downloaded metadata (bytes=%ld)", _vectorSet.getName().c_str(), buffer.size());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		ILogger.instance().logInfo("\"%s\": Downloaded metadata (bytes=%d)", _vectorSet.getName(), buffer.size());
//#endif
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


  public abstract static class VectorSetSymbolizer
  {
	public void dispose()
	{
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mark* createFeatureMark(const VectorStreamingRenderer::Node* node, const GEO2DPointGeometry* geometry) const = 0;
	public abstract Mark createFeatureMark(VectorStreamingRenderer.Node node, GEO2DPointGeometry geometry);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mark* createClusterMark(const VectorStreamingRenderer::Node* node, const VectorStreamingRenderer::Cluster* cluster, long featuresCount) const = 0;
	public abstract Mark createClusterMark(VectorStreamingRenderer.Node node, VectorStreamingRenderer.Cluster cluster, long featuresCount);

  }


  public static class VectorSet
  {
	private VectorStreamingRenderer _renderer;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final URL _serverURL = new URL();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final URL _serverURL = new internal();
//#endif
	private final String _name;
	private final VectorSetSymbolizer _symbolizer;
	private final boolean _deleteSymbolizer;
	private final long _downloadPriority;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final TimeInterval _timeToCache = new TimeInterval();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final TimeInterval _timeToCache = new internal();
//#endif
	private final boolean _readExpired;
	private final boolean _verbose;
	private final boolean _haltOnError;
	private final Format _format;

	private final String _properties;

	private boolean _downloadingMetadata;
	private boolean _errorDownloadingMetadata;
	private boolean _errorParsingMetadata;

	private Sector _sector;
	private long _clustersCount;
	private long _featuresCount;
	private int _nodesCount;
	private int _minNodeDepth;
	private int _maxNodeDepth;
	private java.util.ArrayList<Node> _rootNodes;
	private int _rootNodesSize;

	private long _lastRenderedCount;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const URL getMetadataURL() const
	private URL getMetadataURL()
	{
	  if (_format == VectorStreamingRenderer.Format.SERVER)
	  {
		return new URL(_serverURL, _name);
	  }
	  return new URL(_serverURL, _name + "/metadata.json");
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String toNodesDirectories(const String& nodeID) const
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


	public VectorSet(VectorStreamingRenderer renderer, URL serverURL, String name, String properties, VectorSetSymbolizer symbolizer, boolean deleteSymbolizer, long downloadPriority, TimeInterval timeToCache, boolean readExpired, boolean verbose, boolean haltOnError, Format format)
	{
		_renderer = renderer;
		_serverURL = new URL(serverURL);
		_name = name;
		_properties = properties;
		_symbolizer = symbolizer;
		_deleteSymbolizer = deleteSymbolizer;
		_downloadPriority = downloadPriority;
		_timeToCache = new TimeInterval(timeToCache);
		_readExpired = readExpired;
		_verbose = verbose;
		_haltOnError = haltOnError;
		_format = format;
		_downloadingMetadata = false;
		_errorDownloadingMetadata = false;
		_errorParsingMetadata = false;
		_sector = null;
		_rootNodes = null;
		_rootNodesSize = 0;
		_lastRenderedCount = 0;

	}

	public void dispose()
	{
	  if (_deleteSymbolizer)
	  {
		_symbolizer = null;
	  }
    
	  if (_sector != null)
		  _sector.dispose();
    
	  if (_rootNodes != null)
	  {
		for (int i = 0; i < _rootNodes.size(); i++)
		{
		  Node node = _rootNodes.get(i);
		  node.unload();
		  node._release();
		}
		_rootNodes = null;
	  }
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const URL getNodeFeaturesURL(const String& nodeID) const
	public final URL getNodeFeaturesURL(String nodeID)
	{
	  if (_format == VectorStreamingRenderer.Format.SERVER)
	  {
		return new URL(_serverURL, _name + "/features" + "?node=" + nodeID + "&properties=" + _properties, true);
	  }
	  return new URL(_serverURL, _name + "/" + toNodesDirectories(nodeID) + "/features.json");
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const URL getNodeChildrenURL(const String& nodeID, const java.util.ArrayList<String>& childrenIDs) const
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		  nodes += childrenIDs.get(i);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		  nodes += childrenIDs.get(i);
//#endif
		}
    
		return new URL(_serverURL, _name + "?nodes=" + nodes, true);
    
	  }
	  return new URL(_serverURL, _name + "/" + toNodesDirectories(nodeID) + "/children.json");
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getName() const
	public final String getName()
	{
	  return _name;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getDownloadPriority() const
	public final long getDownloadPriority()
	{
	  return _downloadPriority;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TimeInterval getTimeToCache() const
	public final TimeInterval getTimeToCache()
	{
	  return _timeToCache;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean getReadExpired() const
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
		ILogger.instance().logInfo("\"%s\": Downloading metadata", _name.c_str());
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
	public final void parsedMetadata(Sector sector, long clustersCount, long featuresCount, int nodesCount, int minNodeDepth, int maxNodeDepth, java.util.ArrayList<Node> rootNodes)
	{
	  _downloadingMetadata = false;
    
	  _sector = sector;
	  _clustersCount = clustersCount;
	  _featuresCount = featuresCount;
	  _nodesCount = nodesCount;
	  _minNodeDepth = minNodeDepth;
	  _maxNodeDepth = maxNodeDepth;
	  _rootNodes = rootNodes;
	  _rootNodesSize = _rootNodes.size();
    
	  if (_verbose)
	  {
		ILogger.instance().logInfo("\"%s\": Metadata", _name.c_str());
		ILogger.instance().logInfo("   Sector        : %s", _sector.description().c_str());
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		ILogger.instance().logInfo("   Clusters Count: %ld", _clustersCount);
		ILogger.instance().logInfo("   Features Count: %ld", _featuresCount);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		ILogger.instance().logInfo("   Clusters Count: %d", _clustersCount);
		ILogger.instance().logInfo("   Features Count: %d", _featuresCount);
//#endif
		ILogger.instance().logInfo("   Nodes Count   : %d", _nodesCount);
		ILogger.instance().logInfo("   Depth         : %d/%d", _minNodeDepth, _maxNodeDepth);
		ILogger.instance().logInfo("   Root Nodes    : %d", _rootNodesSize);
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
			ILogger.instance().logInfo("\"%s\": Rendered %ld features", _name.c_str(), renderedCount);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
			ILogger.instance().logInfo("\"%s\": Rendered %d features", _name, renderedCount);
//#endif
		  }
		  _lastRenderedCount = renderedCount;
		}
    
	  }
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long createFeatureMark(const Node* node, const GEO2DPointGeometry* geometry) const
	public final long createFeatureMark(Node node, GEO2DPointGeometry geometry)
	{
	  Mark mark = _symbolizer.createFeatureMark(node, geometry);
	  if (mark == null)
	  {
		return 0;
	  }
    
	  mark.setToken(node.getFeatureMarkToken());
	  _renderer.getMarkRenderer().addMark(mark);
	  return 1;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long createClusterMarks(const Node* node, const java.util.ArrayList<Cluster*>* clusters) const
	public final long createClusterMarks(Node node, java.util.ArrayList<Cluster> clusters)
	{
	  long counter = 0;
	  if (clusters != null)
	  {
		final int clustersCount = clusters.size();
		for (int i = 0; i < clustersCount; i++)
		{
		  final Cluster cluster = clusters.get(i);
		  if (cluster != null)
		  {
			Mark mark = _symbolizer.createClusterMark(node, cluster, _featuresCount);
			if (mark != null)
			{
			  mark.setToken(node.getClusterMarkToken());
			  _renderer.getMarkRenderer().addMark(mark);
			  counter++;
			}
		  }
		}
	  }
    
	  return counter;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MarksRenderer* getMarksRenderer() const
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MarksRenderer* getMarkRenderer() const
  public final MarksRenderer getMarkRenderer()
  {
	return _markRenderer;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
	if (_vectorSetsSize > 0)
	{
	  final Camera camera = rc.getCurrentCamera();
	  final Frustum frustumInModelCoordinates = camera.getFrustumInModelCoordinates();
  
	  final long cameraTS = camera.getTimestamp();
  
	  updateGLState(camera);
	  _glState.setParent(glState);
  
	  for (int i = 0; i < _vectorSetsSize; i++)
	  {
		VectorSet vectorSector = _vectorSets.get(i);
		vectorSector.render(rc, frustumInModelCoordinates, cameraTS, _glState);
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

  public final void addVectorSet(URL serverURL, String name, String properties, VectorSetSymbolizer symbolizer, boolean deleteSymbolizer, long downloadPriority, TimeInterval timeToCache, boolean readExpired, boolean verbose, boolean haltOnError, Format format)
  {
	VectorSet vectorSet = new VectorSet(this, serverURL, name, properties, symbolizer, deleteSymbolizer, downloadPriority, timeToCache, readExpired, verbose, haltOnError, format);
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
		_errors.insert(_errors.end(), childErrors.iterator(), childErrors.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		_errors.addAll(childErrors);
//#endif
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MarksRenderer* getMarksRenderer() const
  public final MarksRenderer getMarksRenderer()
  {
	return _markRenderer;
  }

}
