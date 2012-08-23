package org.glob3.mobile.specific;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.CPUTextureBuilder;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.DummyRenderer;
import org.glob3.mobile.generated.EffectTarget;
import org.glob3.mobile.generated.EffectsScheduler;
import org.glob3.mobile.generated.EllipsoidalTileTessellator;
import org.glob3.mobile.generated.FrameTasksExecutor;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GL;
import org.glob3.mobile.generated.GLErrorRenderer;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.LatLonMeshRenderer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.SGCubeNode;
import org.glob3.mobile.generated.SampleEffect;
import org.glob3.mobile.generated.SceneGraphRenderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.SimplePlanetRenderer;
import org.glob3.mobile.generated.SingleImageTileTexturizer;
import org.glob3.mobile.generated.TextureBuilder;
import org.glob3.mobile.generated.TexturesHandler;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileTexturizer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;

import org.glob3.mobile.generated.*;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class G3MWidget_Android extends GLSurfaceView implements
		OnGestureListener {

	G3MWidget _widget;
	ES2Renderer _es2renderer;

	final MotionEventProcessor _motionEventProcessor = new MotionEventProcessor();

	public G3MWidget_Android(Context context) {
		super(context);

		setEGLContextClientVersion(2); // OPENGL ES VERSION MUST BE SPECIFED
		setEGLConfigChooser(true); // IT GIVES US A RGB DEPTH OF 8 BITS PER
									// CHANNEL, HAVING TO FORCE PROPER BUFFER
									// ALLOCATION

		// Detect Long-Press events
		setLongClickable(true);

		// Debug flags
		setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);

	}

	// The initialization of _widget occurs when the android widget is resized
	// to the screen size
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (_widget == null){
			// SETTING RENDERER
			_es2renderer = new ES2Renderer(this.getContext(), this);
			setRenderer(_es2renderer);
		}
	}

	public boolean onTouchEvent(MotionEvent event) {

		final TouchEvent te = _motionEventProcessor.processEvent(event);

		if (te != null) {
			// SEND MESSAGE TO RENDER THREAD
			queueEvent(new Runnable() {
				public void run() {
					_widget.onTouchEvent(te);
				}
			});
			return true;
		} else {
			return false;
		}
	}

	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public G3MWidget getWidget() {
		if (_widget == null) {
			initWidgetDemo();
			//initSimpleWidgetDemo();
		}
		return _widget;
	}
	
	private void initSimpleWidgetDemo()
	{
		IStringBuilder.setInstance(new StringBuilder_Android());
		
		int width = getWidth();
		int height = getHeight();
		Context ctx = getContext();
		
		File f = getContext().getExternalCacheDir();
		if (!f.exists()){
			f = getContext().getCacheDir();
		}
		String documentsDirectory = f.getAbsolutePath();
		
		IFactory factory = new Factory_Android(ctx);
		  ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
		  
		  NativeGL2_Android nGL = new NativeGL2_Android(); 
		  GL gl  = new GL(nGL);
		  
		  // composite renderer is the father of the rest of renderers
		  CompositeRenderer comp = new CompositeRenderer();
		  
		  // camera renderer and handlers
		  CameraRenderer cameraRenderer = new CameraRenderer();
		  cameraRenderer.addHandler(new CameraSingleDragHandler());
		  cameraRenderer.addHandler(new CameraDoubleDragHandler());
		  cameraRenderer.addHandler(new CameraRotationHandler());
		  cameraRenderer.addHandler(new CameraDoubleTapHandler());
		  comp.addRenderer(cameraRenderer);

		  
		  //STORAGE
		  IDownloader downloader = null;
//		  IDownloader downloader = new Downloader_Android(1 * 1024 * 1024,
//		                                               64 * 1024 * 1024,
//		                                               ".G3M_Cache",
//		                                               8);

		  if (true) {
		    // dummy renderer with a simple box
		    DummyRenderer dum = new DummyRenderer();
		    comp.addRenderer(dum);
		  }
		  
		  if (true) {
		    // simple planet renderer, with a basic world image
		    SimplePlanetRenderer spr = new SimplePlanetRenderer("world.jpg");
		    comp.addRenderer(spr);
		  }

		  EffectsScheduler scheduler = new EffectsScheduler();

		  comp.addRenderer(new GLErrorRenderer());
		  
		  
		  TextureBuilder texBuilder = new CPUTextureBuilder();
		  TexturesHandler texturesHandler = new TexturesHandler(gl, factory, texBuilder, false);
		  
		  Planet planet = Planet.createEarth();
		  
		  org.glob3.mobile.generated.Renderer busyRenderer = new NullBusyRender();
		  
		  ArrayList<ICameraConstrainer> cameraConstraint = new ArrayList<ICameraConstrainer>();
		  cameraConstraint.add(new SimpleCameraConstrainer());

		  FrameTasksExecutor frameTasksExecutor = new FrameTasksExecutor();
		  
		  _widget = G3MWidget.create(frameTasksExecutor,
                  factory,
                  logger,
                  gl,
                  texturesHandler,
                  downloader,
                  planet, 
                  cameraConstraint,
                  comp,
                  busyRenderer,
                  scheduler,
                  width, height,
                  Color.fromRGBA((float)0, (float)0.1, (float)0.2, (float)1),
                  true,
                  false);
		                              
		                             
	}

	private void initWidgetDemo()
	{
		
		IStringBuilder.setInstance(new StringBuilder_Android());

		int width = getWidth();
		int height = getHeight();
		Context ctx = getContext();
		
	  IFactory factory = new Factory_Android(ctx);
	  ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
	  
	  NativeGL2_Android nGL = new NativeGL2_Android(); 
	  GL gl  = new GL(nGL);
	  
	  // composite renderer is the father of the rest of renderers
	  CompositeRenderer comp = new CompositeRenderer();
	  
	  // camera renderer and handlers
	  CameraRenderer cameraRenderer = new CameraRenderer();
	  cameraRenderer.addHandler(new CameraSingleDragHandler());
	  cameraRenderer.addHandler(new CameraDoubleDragHandler());
	  cameraRenderer.addHandler(new CameraRotationHandler());
	  cameraRenderer.addHandler(new CameraDoubleTapHandler());
	  comp.addRenderer(cameraRenderer);

	  
	  //STORAGE
	  IDownloader downloader = null;
//	  IDownloader downloader = new Downloader_Android(1 * 1024 * 1024,
//	                                               64 * 1024 * 1024,
//	                                               ".G3M_Cache",
//	                                               8);

	  //LAYERS
	  LayerSet layerSet = new LayerSet();
	  WMSLayer blueMarble = new WMSLayer("bmng200405",
              "http://www.nasa.network.com/wms?",
              WMSServerVersion.WMS_1_1_0,
              "image/jpeg",
              Sector.fullSphere(),
              "EPSG:4326",
              "",
              false,
              Angle.nan(),
              Angle.nan());
	  layerSet.addLayer(blueMarble);
	  
	  // very basic tile renderer
	  if (false) {
	    boolean renderDebug = true;
	    
	    TilesRenderParameters parameters = TilesRenderParameters.createDefault(renderDebug);

	    TileTexturizer texturizer = null;
	    IImage singleWorldImage = factory.createImageFromFileName("world.jpg");
	    texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage);
	    
	    boolean showStatistics = false;
	    TileRenderer tr = new TileRenderer(new EllipsoidalTileTessellator(parameters._tileResolution, true),
	                                        texturizer,
	                                        parameters,
	                                        showStatistics);
	    comp.addRenderer(tr);
	  }
	  
	  if (false) {
	    // dummy renderer with a simple box
	    DummyRenderer dum = new DummyRenderer();
	    comp.addRenderer(dum);
	  }
	  
	  if (true) {
	    // simple planet renderer, with a basic world image
	    SimplePlanetRenderer spr = new SimplePlanetRenderer("world.jpg");
	    comp.addRenderer(spr);
	  }
	  
	  if (true) {
	    // marks renderer
	    MarksRenderer marks = new MarksRenderer();
	    comp.addRenderer(marks);
	    
	    Mark m1 = new Mark("Fuerteventura",
	                        "g3m-marker.png",
	                        new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0));
	    //m1->addTouchListener(listener);
	    marks.addMark(m1);
	    
	    
	    Mark m2 = new Mark("Las Palmas",
	                        "g3m-marker.png",
	                        new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-15.36), 0));
	    //m2->addTouchListener(listener);
	    marks.addMark(m2);
	    
	    if (false) {

	    	Random r = new Random();
	      for (int i = 0; i < 500; i++) {
	        final Angle latitude = Angle.fromDegrees( (int) (r.nextInt() % 180) - 90 );
	        final Angle longitude = Angle.fromDegrees( (int) (r.nextInt() % 360) - 180 );
	        //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());
	        
	        marks.addMark(new Mark("Random",
	                                "g3m-marker.png",
	                                new Geodetic3D(latitude, longitude, 0)));
	      }
	    }
	  }
	  
	  if (true) {
	    LatLonMeshRenderer renderer = new LatLonMeshRenderer();
	    comp.addRenderer(renderer);
	  }
	  
	  EffectsScheduler scheduler = new EffectsScheduler();
	  
	  if (false) {
	    EffectTarget target = null;
	    scheduler.startEffect(new SampleEffect(TimeInterval.fromSeconds(2)),
	                           target);
	  }
	  
	  if (false) {
	    SceneGraphRenderer sgr = new SceneGraphRenderer();
	    SGCubeNode cube = new SGCubeNode();
	    // cube->setScale(Vector3D(6378137.0, 6378137.0, 6378137.0));
	    sgr.getRootNode().addChild(cube);
	    comp.addRenderer(sgr);
	  }
	  
	  comp.addRenderer(new GLErrorRenderer());
	  
	  
	  TextureBuilder texBuilder = new CPUTextureBuilder();
	  TexturesHandler texturesHandler = new TexturesHandler(gl, factory, texBuilder, false);
	  
	  Planet planet = Planet.createEarth();
	  
	  org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();// new NullBusyRender();
	  
	  ArrayList<ICameraConstrainer> cameraConstraint = new ArrayList<ICameraConstrainer>();
	  cameraConstraint.add(new SimpleCameraConstrainer());
	  
	  FrameTasksExecutor frameTasksExecutor = new FrameTasksExecutor();

	  _widget = G3MWidget.create(frameTasksExecutor,
              factory,
              logger,
              gl,
              texturesHandler,
              downloader,
              planet, 
              cameraConstraint,
              comp,
              busyRenderer,
              scheduler,
              width, height,
              Color.fromRGBA((float)0, (float)0.1, (float)0.2, (float)1),
              true,
              false);
	                              
	                             
	}

}
