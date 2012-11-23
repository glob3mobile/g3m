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





public class IG3MBuilder
{

	private INativeGL _nativeGL;
	private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE
	private java.util.ArrayList<ICameraConstrainer> _cameraConstraints = new java.util.ArrayList<ICameraConstrainer>();
	private CameraRenderer _cameraRenderer;
	private Color _backgroundColor;
	private LayerSet _layerSet;
	private TilesRenderParameters _parameters;
	private TileRenderer _tileRenderer;
	private Renderer _busyRenderer;
	private java.util.ArrayList<Renderer> _renderers = new java.util.ArrayList<Renderer>();
	private GTask _initializationTask;
	private boolean _autoDeleteInitializationTask;
	private java.util.ArrayList<PeriodicalTask> _periodicalTasks = new java.util.ArrayList<PeriodicalTask>();
	private boolean _logFPS;
	private boolean _logDownloaderStatistics;
	private UserData _userData;

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

	protected final G3MWidget create()
	{
    
		if (_nativeGL != null)
		{
			Color backgroundColor = Color.fromRGBA(_backgroundColor.getRed(), _backgroundColor.getGreen(), _backgroundColor.getBlue(), _backgroundColor.getAlpha());
    
			if (_cameraConstraints.size() == 0)
			{
				_cameraConstraints = createCameraConstraints();
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
				((CompositeRenderer)mainRenderer).addRenderer(_tileRenderer);
    
				for (int i = 0; i < _renderers.size(); i++)
				{
					((CompositeRenderer)mainRenderer).addRenderer(_renderers.get(i));
				}
			}
			else
			{
				mainRenderer = _tileRenderer;
			}
    
			G3MWidget g3mWidget = G3MWidget.create(_nativeGL, _planet, _cameraConstraints, _cameraRenderer, mainRenderer, _busyRenderer, backgroundColor, _logFPS, _logDownloaderStatistics, _initializationTask, _autoDeleteInitializationTask, _periodicalTasks);
			g3mWidget.setUserData(_userData);
    
			return g3mWidget;
		}
		return null;
	}

	public IG3MBuilder()
	{
		_planet = Planet.createEarth();
		_cameraRenderer = createCameraRenderer();
		_backgroundColor = Color.newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1);
		_layerSet = null;
		_parameters = null;
		_tileRenderer = null;
		_busyRenderer = new BusyMeshRenderer();
		_initializationTask = null;
		_logFPS = true;
		_logDownloaderStatistics = false;
		_autoDeleteInitializationTask = true;
		_userData = null;
	}
	public void dispose()
	{
		if (_cameraRenderer != null)
			_cameraRenderer.dispose();
		_backgroundColor = null;
		if (_layerSet != null)
			_layerSet.dispose();
		if (_tileRenderer != null)
			_tileRenderer.dispose();
		if (_busyRenderer != null)
			_busyRenderer.dispose();
		if (_initializationTask != null)
			_initializationTask.dispose();
		if (_userData != null)
			_userData.dispose();
	}
	public final void setNativeGL(INativeGL nativeGL)
	{
		_nativeGL = nativeGL;
	}
	public final void setPlanet(Planet planet)
	{
		_planet = planet;
	}
	public final void addCameraConstraint(ICameraConstrainer cameraConstraint)
	{
		_cameraConstraints.add(cameraConstraint);
	}
	public final void setCameraRenderer(CameraRenderer cameraRenderer)
	{
		_cameraRenderer = cameraRenderer;
	}
	public final void setBackgroundColor(Color backgroundColor)
	{
		if (_backgroundColor != backgroundColor)
		{
			_backgroundColor = null;
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
	public final void setInitializationTask(GTask initializationTask)
	{
		if (_initializationTask != initializationTask)
		{
			if (_initializationTask != null)
				_initializationTask.dispose();
			_initializationTask = initializationTask;
		}
	}
	public final void setAutoDeleteInitializationTask(boolean autoDeleteInitializationTask)
	{
		_autoDeleteInitializationTask = autoDeleteInitializationTask;
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
	public final void setUserData(UserData userData)
	{
		if (_userData != userData)
		{
			if (_userData != null)
				_userData.dispose();
			_userData = userData;
		}
	}

}