package org.glob3.mobile.generated;import java.util.*;

public class MeshRenderer extends DefaultRenderer
{


  private static class LoadQueueItem
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	public final URL _url = new URL();
	public final TimeInterval _timeToCache = new TimeInterval();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final URL _url = new public();
	public final TimeInterval _timeToCache = new public();
//#endif
	public final long _priority;
	public final boolean _readExpired;
	public final float _pointSize;
	public final double _deltaHeight;
	public final Color _color;
	public MeshLoadListener _listener;
	public final boolean _deleteListener;
	public final boolean _isBSON;
	public final MeshType _meshType;

	public LoadQueueItem(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize, double deltaHeight, Color color, MeshLoadListener listener, boolean deleteListener, boolean isBSON, MeshType meshType)
	{
		_url = new URL(url);
		_priority = priority;
		_timeToCache = new TimeInterval(timeToCache);
		_readExpired = readExpired;
		_pointSize = pointSize;
		_deltaHeight = deltaHeight;
		_color = color;
		_listener = listener;
		_deleteListener = deleteListener;
		_isBSON = isBSON;
		_meshType = meshType;

	}

	public void dispose()
	{
	}
  }



  private java.util.ArrayList<Mesh> _meshes = new java.util.ArrayList<Mesh>();

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

  private java.util.ArrayList<LoadQueueItem> _loadQueue = new java.util.ArrayList<LoadQueueItem>();

  private void drainLoadQueue()
  {
	  final int loadQueueSize = _loadQueue.size();
	  for (int i = 0; i < loadQueueSize; i++)
	  {
		  LoadQueueItem item = _loadQueue.get(i);
		  requestMeshBuffer(item._url, item._priority, item._timeToCache, item._readExpired, item._pointSize, item._deltaHeight, item._color, item._listener, item._deleteListener, item._isBSON, item._meshType);
  
		  if (item != null)
			  item.dispose();
	  }
  
	  _loadQueue.clear();
  }

  private void cleanLoadQueue()
  {
	  final int loadQueueSize = _loadQueue.size();
	  for (int i = 0; i < loadQueueSize; i++)
	  {
		  LoadQueueItem item = _loadQueue.get(i);
		  if (item != null)
			  item.dispose();
	  }
  
	  _loadQueue.clear();
  }

  private void requestMeshBuffer(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize, double deltaHeight, Color color, MeshLoadListener listener, boolean deleteListener, boolean isBSON, MeshType meshType)
  {
	  IDownloader downloader = _context.getDownloader();
	  downloader.requestBuffer(url, priority, timeToCache, readExpired, new MeshRenderer_MeshBufferDownloadListener(this, pointSize, deltaHeight, color, listener, deleteListener, _context.getThreadUtils(), isBSON, meshType, _context), true);
  
  }

  private boolean _showNormals;



  public MeshRenderer()
  {
	  _glState = new GLState();
	  _showNormals = false;
	_context = null;
  }

  public void dispose()
  {
	  final int meshesCount = _meshes.size();
	  for (int i = 0; i < meshesCount; i++)
	  {
		  Mesh mesh = _meshes.get(i);
		  if (mesh != null)
			  mesh.dispose();
	  }
  
	  _glState._release();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
  }

  public final void addMesh(Mesh mesh)
  {
	  _meshes.add(mesh);
	  mesh.showNormals(_showNormals);
  }

  public final void clearMeshes()
  {
	  clearMeshes(true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void clearMeshes(boolean deleteMeshes = true)
  public final void clearMeshes(boolean deleteMeshes)
  {
	  if (deleteMeshes)
	  {
		  final int meshesCount = _meshes.size();
		  for (int i = 0; i < meshesCount; i++)
		  {
			  Mesh mesh = _meshes.get(i);
			  if (mesh != null)
				  mesh.dispose();
		  }
	  }
	  _meshes.clear();
  }

  public final void enableAll()
  {
	  final int meshesCount = _meshes.size();
	  for (int i = 0; i < meshesCount; i++)
	  {
		  Mesh mesh = _meshes.get(i);
		  mesh.setEnable(true);
	  }
  }

  public final void disableAll()
  {
	  final int meshesCount = _meshes.size();
	  for (int i = 0; i < meshesCount; i++)
	  {
		  Mesh mesh = _meshes.get(i);
		  mesh.setEnable(false);
	  }
  }

  public final void onChangedContext()
  {
	  if (_context != null)
	  {
		  drainLoadQueue();
	  }
  }

  public final void onLostContext()
  {
	  if (_context == null)
	  {
		  cleanLoadQueue();
	  }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
	  final int meshesCount = _meshes.size();
	  if (meshesCount > 0)
	  {
		  final Camera camera = rc.getCurrentCamera();
  
		  updateGLState(camera);
  
		  final Frustum frustum = camera.getFrustumInModelCoordinates();
  
		  _glState.setParent(glState);
  
		  for (int i = 0; i < meshesCount; i++)
		  {
			  Mesh mesh = _meshes.get(i);
			  final BoundingVolume boundingVolume = mesh.getBoundingVolume();
			  if ((boundingVolume != null) && boundingVolume.touchesFrustum(frustum))
			  {
				  mesh.render(rc, _glState);
			  }
		  }
	  }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public final void loadJSONPointCloud(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize, double deltaHeight, MeshLoadListener listener)
  {
	  loadJSONPointCloud(url, priority, timeToCache, readExpired, pointSize, deltaHeight, listener, true);
  }
  public final void loadJSONPointCloud(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize, double deltaHeight)
  {
	  loadJSONPointCloud(url, priority, timeToCache, readExpired, pointSize, deltaHeight, null, true);
  }
  public final void loadJSONPointCloud(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize)
  {
	  loadJSONPointCloud(url, priority, timeToCache, readExpired, pointSize, 0, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadJSONPointCloud(const URL& url, long priority, const TimeInterval& timeToCache, boolean readExpired, float pointSize, double deltaHeight =0, MeshLoadListener* listener =null, boolean deleteListener =true)
  public final void loadJSONPointCloud(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize, double deltaHeight, MeshLoadListener listener, boolean deleteListener)
  {
	  if (_context == null)
	  {
		  _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, pointSize, deltaHeight, null, listener, deleteListener, false, MeshType.POINT_CLOUD)); // isBson -  color
	  }
	  else
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: requestMeshBuffer(url, priority, timeToCache, readExpired, pointSize, deltaHeight, null, listener, deleteListener, false, POINT_CLOUD);
		  requestMeshBuffer(new URL(url), priority, new TimeInterval(timeToCache), readExpired, pointSize, deltaHeight, null, listener, deleteListener, false, MeshType.POINT_CLOUD); // isBson -  color
	  }
  }

  public final void loadJSONPointCloud(URL url, float pointSize, double deltaHeight, MeshLoadListener listener)
  {
	  loadJSONPointCloud(url, pointSize, deltaHeight, listener, true);
  }
  public final void loadJSONPointCloud(URL url, float pointSize, double deltaHeight)
  {
	  loadJSONPointCloud(url, pointSize, deltaHeight, null, true);
  }
  public final void loadJSONPointCloud(URL url, float pointSize)
  {
	  loadJSONPointCloud(url, pointSize, 0, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadJSONPointCloud(const URL& url, float pointSize, double deltaHeight=0, MeshLoadListener* listener=null, boolean deleteListener=true)
  public final void loadJSONPointCloud(URL url, float pointSize, double deltaHeight, MeshLoadListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadJSONPointCloud(url, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true, pointSize, deltaHeight, listener, deleteListener);
	loadJSONPointCloud(new URL(url), DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, pointSize, deltaHeight, listener, deleteListener);
  }

  public final void loadBSONPointCloud(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize, double deltaHeight, MeshLoadListener listener)
  {
	  loadBSONPointCloud(url, priority, timeToCache, readExpired, pointSize, deltaHeight, listener, true);
  }
  public final void loadBSONPointCloud(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize, double deltaHeight)
  {
	  loadBSONPointCloud(url, priority, timeToCache, readExpired, pointSize, deltaHeight, null, true);
  }
  public final void loadBSONPointCloud(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize)
  {
	  loadBSONPointCloud(url, priority, timeToCache, readExpired, pointSize, 0, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadBSONPointCloud(const URL& url, long priority, const TimeInterval& timeToCache, boolean readExpired, float pointSize, double deltaHeight =0, MeshLoadListener* listener =null, boolean deleteListener =true)
  public final void loadBSONPointCloud(URL url, long priority, TimeInterval timeToCache, boolean readExpired, float pointSize, double deltaHeight, MeshLoadListener listener, boolean deleteListener)
  {
	  if (_context == null)
	  {
		  _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, pointSize, deltaHeight, null, listener, deleteListener, true, MeshType.POINT_CLOUD)); // isBson -  color
	  }
	  else
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: requestMeshBuffer(url, priority, timeToCache, readExpired, pointSize, deltaHeight, null, listener, deleteListener, true, POINT_CLOUD);
		  requestMeshBuffer(new URL(url), priority, new TimeInterval(timeToCache), readExpired, pointSize, deltaHeight, null, listener, deleteListener, true, MeshType.POINT_CLOUD); // isBson -  color
	  }
  }

  public final void loadBSONPointCloud(URL url, float pointSize, double deltaHeight, MeshLoadListener listener)
  {
	  loadBSONPointCloud(url, pointSize, deltaHeight, listener, true);
  }
  public final void loadBSONPointCloud(URL url, float pointSize, double deltaHeight)
  {
	  loadBSONPointCloud(url, pointSize, deltaHeight, null, true);
  }
  public final void loadBSONPointCloud(URL url, float pointSize)
  {
	  loadBSONPointCloud(url, pointSize, 0, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadBSONPointCloud(const URL& url, float pointSize, double deltaHeight=0, MeshLoadListener* listener=null, boolean deleteListener=true)
  public final void loadBSONPointCloud(URL url, float pointSize, double deltaHeight, MeshLoadListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadBSONPointCloud(url, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true, pointSize, deltaHeight, listener, deleteListener);
	loadBSONPointCloud(new URL(url), DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, pointSize, deltaHeight, listener, deleteListener);
  }

  public final void loadJSONMesh(URL url, Color color, long priority, TimeInterval timeToCache, boolean readExpired, MeshLoadListener listener)
  {
	  loadJSONMesh(url, color, priority, timeToCache, readExpired, listener, true);
  }
  public final void loadJSONMesh(URL url, Color color, long priority, TimeInterval timeToCache, boolean readExpired)
  {
	  loadJSONMesh(url, color, priority, timeToCache, readExpired, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadJSONMesh(const URL& url, const Color* color, long priority, const TimeInterval& timeToCache, boolean readExpired, MeshLoadListener* listener =null, boolean deleteListener =true)
  public final void loadJSONMesh(URL url, Color color, long priority, TimeInterval timeToCache, boolean readExpired, MeshLoadListener listener, boolean deleteListener)
  {
	  if (_context == null)
	  {
		  _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, 1, 0, color, listener, deleteListener, false, MeshType.MESH)); // isBson -  deltaHeight -  pointSize
	  }
	  else
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: requestMeshBuffer(url, priority, timeToCache, readExpired, 1, 0, color, listener, deleteListener, false, MESH);
		  requestMeshBuffer(new URL(url), priority, new TimeInterval(timeToCache), readExpired, 1, 0, color, listener, deleteListener, false, MeshType.MESH); // isBson -  deltaHeight -  pointSize
	  }
  }

  public final void loadJSONMesh(URL url, Color color, MeshLoadListener listener)
  {
	  loadJSONMesh(url, color, listener, true);
  }
  public final void loadJSONMesh(URL url, Color color)
  {
	  loadJSONMesh(url, color, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadJSONMesh(const URL& url, const Color* color, MeshLoadListener* listener=null, boolean deleteListener=true)
  public final void loadJSONMesh(URL url, Color color, MeshLoadListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadJSONMesh(url, color, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true, listener, deleteListener);
	loadJSONMesh(new URL(url), color, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, listener, deleteListener);
  }

  public final void loadBSONMesh(URL url, Color color, long priority, TimeInterval timeToCache, boolean readExpired, MeshLoadListener listener)
  {
	  loadBSONMesh(url, color, priority, timeToCache, readExpired, listener, true);
  }
  public final void loadBSONMesh(URL url, Color color, long priority, TimeInterval timeToCache, boolean readExpired)
  {
	  loadBSONMesh(url, color, priority, timeToCache, readExpired, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadBSONMesh(const URL& url, const Color* color, long priority, const TimeInterval& timeToCache, boolean readExpired, MeshLoadListener* listener =null, boolean deleteListener =true)
  public final void loadBSONMesh(URL url, Color color, long priority, TimeInterval timeToCache, boolean readExpired, MeshLoadListener listener, boolean deleteListener)
  {
	  if (_context == null)
	  {
		  _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, 1, 0, color, listener, deleteListener, true, MeshType.MESH)); // isBson -  deltaHeight -  pointSize
	  }
	  else
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: requestMeshBuffer(url, priority, timeToCache, readExpired, 1, 0, color, listener, deleteListener, true, MESH);
		  requestMeshBuffer(new URL(url), priority, new TimeInterval(timeToCache), readExpired, 1, 0, color, listener, deleteListener, true, MeshType.MESH); // isBson -  deltaHeight -  pointSize
	  }
  }

  public final void loadBSONMesh(URL url, Color color, MeshLoadListener listener)
  {
	  loadBSONMesh(url, color, listener, true);
  }
  public final void loadBSONMesh(URL url, Color color)
  {
	  loadBSONMesh(url, color, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadBSONMesh(const URL& url, const Color* color, MeshLoadListener* listener=null, boolean deleteListener=true)
  public final void loadBSONMesh(URL url, Color color, MeshLoadListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadBSONMesh(url, color, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true, listener, deleteListener);
	loadBSONMesh(new URL(url), color, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, listener, deleteListener);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showNormals(boolean v) const
  public final void showNormals(boolean v)
  {
	  _showNormals = v;
	  final int meshesCount = _meshes.size();
	  for (int i = 0; i < meshesCount; i++)
	  {
		  Mesh mesh = _meshes.get(i);
		  mesh.showNormals(v);
	  }
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int meshCount() const
	public final int meshCount()
	{
		return _meshes.size();
	}

  public final java.util.ArrayList<Mesh> getMeshes()
  {
	  java.util.ArrayList<Mesh > copyMesh = new java.util.ArrayList<Mesh >();
	  final int meshesCount = _meshes.size();
  
	  for (int i = 0;i< meshesCount ;i++)
	  {
		  copyMesh.add(_meshes.get(i));
	  }
	  return copyMesh;
  }

	public final void eraseMesh(Mesh mesh)
	{

		java.util.Iterator<Mesh> it = _meshes.iterator();
		while(it.hasNext())
		{
			if (it.next() == mesh)
			{
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
				_meshes.erase(it);
				break;
			}
		}
	}


}
