package org.glob3.mobile.generated; 
//
//  IG3MBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 20/11/12.
//
//

//
//  IG3MBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 20/11/12.
//
//





public abstract class IG3MBuilder
{

  private GL _gl;
  private IDownloader _downloader;
  private IThreadUtils _threadUtils;
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE
  private java.util.ArrayList<ICameraConstrainer> _cameraConstraints = new java.util.ArrayList<ICameraConstrainer>();
  private CameraRenderer _cameraRenderer;
  private Color _backgroundColor;
  private LayerSet _layerSet;
  private TilesRenderParameters _parameters;
  private TileRenderer _tileRenderer;
  private Renderer _busyRenderer;
  private java.util.ArrayList<Renderer> _renderers = new java.util.ArrayList<Renderer>();
  private GInitializationTask _initializationTask;
  private boolean _autoDeleteInitializationTask;
  private java.util.ArrayList<PeriodicalTask> _periodicalTasks = new java.util.ArrayList<PeriodicalTask>();
  private boolean _logFPS;
  private boolean _logDownloaderStatistics;
  private WidgetUserData _userData;

  private java.util.ArrayList<ICameraConstrainer> createCameraConstraints()
  {
	java.util.ArrayList<ICameraConstrainer> cameraConstraints = new java.util.ArrayList<ICameraConstrainer>();
	SimpleCameraConstrainer scc = new SimpleCameraConstrainer();
	cameraConstraints.add(scc);
  
	return cameraConstraints;
  }
  private CameraRenderer createCameraRenderer()
  {
	CameraRenderer cameraRenderer = new CameraRenderer();
	final boolean useInertia = true;
	cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
	final boolean processRotation = true;
	final boolean processZoom = true;
	cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
	cameraRenderer.addHandler(new CameraRotationHandler());
	cameraRenderer.addHandler(new CameraDoubleTapHandler());
  
	return cameraRenderer;
  }

  private void pvtSetInitializationTask(GInitializationTask initializationTask, boolean autoDeleteInitializationTask)
  {
	if (_initializationTask != initializationTask)
	{
	  if (_initializationTask != null)
		  _initializationTask.dispose();
	  _initializationTask = initializationTask;
	}
	_autoDeleteInitializationTask = autoDeleteInitializationTask;
  }


  protected IStorage _storage;

  protected final G3MWidget create()
  {
  
	if (_gl == null)
	{
	  ILogger.instance().logError("Logic Error: _gl not initialized");
	  return null;
	}
  
	if (_storage == null)
	{
	  _storage = createStorage();
	}
  
	if (_downloader == null)
	{
	  _downloader = createDownloader();
	}
  
	if (_threadUtils == null)
	{
	  _threadUtils = createThreadUtils();
	}
  
	if (_cameraConstraints.size() == 0)
	{
	  _cameraConstraints = createCameraConstraints();
	}
  
	if (_cameraRenderer == null)
	{
	  _cameraRenderer = createCameraRenderer();
	}
  
  
	if (_tileRenderer == null)
	{
	  TileRendererBuilder tileRendererBuilder = new TileRendererBuilder();
	  if (_layerSet != null)
	  {
		tileRendererBuilder.setLayerSet(_layerSet);
	  }
	  if (_parameters != null)
	  {
		tileRendererBuilder.setTileRendererParameters(_parameters);
	  }
	  _tileRenderer = tileRendererBuilder.create();
	}
	else
	{
	  if (_layerSet != null)
	  {
		ILogger.instance().logWarning("LayerSet will be ignored because TileRenderer was also set");
	  }
	  if (_parameters != null)
	  {
		ILogger.instance().logWarning("TilesRendererParameters will be ignored because TileRenderer was also set");
	  }
	}
  
	Renderer mainRenderer = null;
	if (_renderers.size() > 0)
	{
	  mainRenderer = new CompositeRenderer();
	  ((CompositeRenderer) mainRenderer).addRenderer(_tileRenderer);
  
	  for (int i = 0; i < _renderers.size(); i++)
	  {
		((CompositeRenderer) mainRenderer).addRenderer(_renderers.get(i));
	  }
	}
	else
	{
	  mainRenderer = _tileRenderer;
	}
  
	if (_busyRenderer == null)
	{
	  _busyRenderer = new BusyMeshRenderer();
	}
  
	Color backgroundColor = Color.fromRGBA(_backgroundColor.getRed(), _backgroundColor.getGreen(), _backgroundColor.getBlue(), _backgroundColor.getAlpha());
  
	G3MWidget g3mWidget = G3MWidget.create(_gl, _storage, _downloader, _threadUtils, getPlanet(), _cameraConstraints, _cameraRenderer, mainRenderer, _busyRenderer, backgroundColor, _logFPS, _logDownloaderStatistics, _initializationTask, _autoDeleteInitializationTask, _periodicalTasks);
  
	g3mWidget.setUserData(_userData);
  
	return g3mWidget;
  
  }

  protected abstract IThreadUtils createThreadUtils();
  protected abstract IStorage createStorage();
  protected abstract IDownloader createDownloader();

  public IG3MBuilder()
  {
	  _gl = null;
	  _storage = null;
	  _downloader = null;
	  _threadUtils = null;
	  _planet = null;
	  _cameraRenderer = null;
	  _backgroundColor = Color.newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1);
	  _layerSet = null;
	  _parameters = null;
	  _tileRenderer = null;
	  _busyRenderer = null;
	  _initializationTask = null;
	  _autoDeleteInitializationTask = true;
	  _logFPS = false;
	  _logDownloaderStatistics = false;
	  _userData = null;
	/*
	 _gl = NULL;
	 _storage = NULL;
	 _downloader = NULL;
	 _threadUtils = NULL;
	 _planet = NULL;
	 _cameraRenderer = NULL;
	 _backgroundColor = Color::newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1);
	 _layerSet = NULL;
	 _parameters = NULL;
	 _tileRenderer = NULL;
	 _busyRenderer = new BusyMeshRenderer();
	 _initializationTask = NULL;
	 _logFPS = false;
	 _logDownloaderStatistics = false;
	 _autoDeleteInitializationTask = true;
	 _userData = NULL;
	 */
  }
  public void dispose()
  {
	if (_backgroundColor != null)
		_backgroundColor.dispose();
  
	///#ifdef C_CODE
	//  delete _planet;
	//  delete _parameters;
	///#endif
	//  delete _gl;
	//  delete _storage;
	//  delete _downloader;
	//  delete _threadUtils;
	//  delete _cameraRenderer;
	//  delete _layerSet;
	//  delete _tileRenderer;
	//  delete _busyRenderer;
	//  delete _initializationTask;
	//  delete _userData;
  }
  public final void setGL(GL gl)
  {
	if (_gl != gl)
	{
	  if (_gl != null)
		  _gl.dispose();
	  _gl = gl;
	}
  }
  public final void setStorage(IStorage storage)
  {
	if (_storage != storage)
	{
	  if (_storage != null)
		  _storage.dispose();
	  _storage = storage;
	}
  }
  public final void setDownloader(IDownloader downloader)
  {
	if (_downloader != downloader)
	{
	  if (_downloader != null)
		  _downloader.dispose();
	  _downloader = downloader;
	}
  }
  public final void setThreadUtils(IThreadUtils threadUtils)
  {
	if (_threadUtils != threadUtils)
	{
	  if (_threadUtils != null)
		  _threadUtils.dispose();
	  _threadUtils = threadUtils;
	}
  }
  public final void setPlanet(Planet planet)
  {
	if (_planet != planet)
	{
	  _planet = planet;
	}
  }
  public final Planet getPlanet()
  {
	if (_planet == null)
	{
	  _planet = Planet.createEarth();
	}
	return _planet;
  }
  public final void addCameraConstraint(ICameraConstrainer cameraConstraint)
  {
	_cameraConstraints.add(cameraConstraint);
  }
  public final void setCameraRenderer(CameraRenderer cameraRenderer)
  {
	if (_cameraRenderer != cameraRenderer)
	{
	  if (_cameraRenderer != null)
		  _cameraRenderer.dispose();
	  _cameraRenderer = cameraRenderer;
	}
  }
  public final void setBackgroundColor(Color backgroundColor)
  {
	if (_backgroundColor != backgroundColor)
	{
	  if (_backgroundColor != null)
		  _backgroundColor.dispose();
	  _backgroundColor = backgroundColor;
	}
  }
  public final void setLayerSet(LayerSet layerSet)
  {
	if (_tileRenderer == null)
	{
	  if (_layerSet != layerSet)
	  {
		if (_layerSet != null)
			_layerSet.dispose();
		_layerSet = layerSet;
	  }
	}
	else
	{
	  ILogger.instance().logWarning("LayerSet will be ignored because TileRenderer was previously set");
	}
  }
  public final void setTileRendererParameters(TilesRenderParameters parameters)
  {
	if (_tileRenderer == null)
	{
	  if (_parameters != parameters)
	  {
		_parameters = parameters;
	  }
	}
	else
	{
	  ILogger.instance().logWarning("TilesRendererParameters will be ignored because TileRenderer was previously set");
	}
  }
  public final void setTileRenderer(TileRenderer tileRenderer)
  {
	if (_tileRenderer != tileRenderer)
	{
	  if (_tileRenderer != null)
		  _tileRenderer.dispose();
	  _tileRenderer = tileRenderer;
	}
  }
  public final void setBusyRenderer(Renderer busyRenderer)
  {
	if (_busyRenderer != busyRenderer)
	{
	  if (_busyRenderer != null)
		  _busyRenderer.dispose();
	  _busyRenderer = busyRenderer;
	}
  }
  public final void addRenderer(Renderer renderer)
  {
	_renderers.add(renderer);
  }
  public final void addPeriodicalTask(PeriodicalTask periodicalTask)
  {
	_periodicalTasks.add(periodicalTask);
  }
  public final void setLogFPS(boolean logFPS)
  {
	_logFPS = logFPS;
  }
  public final void setLogDownloaderStatistics(boolean logDownloaderStatistics)
  {
	_logDownloaderStatistics = logDownloaderStatistics;
  }
  public final void setUserData(WidgetUserData userData)
  {
	if (_userData != userData)
	{
	  if (_userData != null)
		  _userData.dispose();
	  _userData = userData;
	}
  }

  public final void setInitializationTask(GInitializationTask initializationTask) {
	pvtSetInitializationTask(initializationTask,
							 true // parameter ignored in Java code 
);
  }

}