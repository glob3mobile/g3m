package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.CPUTextureBuilder;
import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.EffectsScheduler;
import org.glob3.mobile.generated.EllipsoidalTileTessellator;
import org.glob3.mobile.generated.FrameTasksExecutor;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GL;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.MultiLayerTileTexturizer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.SingleImageTileTexturizer;
import org.glob3.mobile.generated.TextureBuilder;
import org.glob3.mobile.generated.TexturesHandler;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileTexturizer;
import org.glob3.mobile.generated.TilesRenderParameters;
import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.UserData;
import org.glob3.mobile.generated.Vector2D;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;

public class G3MWidget_Android extends GLSurfaceView implements OnGestureListener {

   G3MWidget                  _widget;
   ES2Renderer                _es2renderer;

   final MotionEventProcessor _motionEventProcessor = new MotionEventProcessor();

   private OnDoubleTapListener _doubleTapListener = null;
   private GestureDetector _gestureDetector = null;

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
      
      
      //Double Tap Listener
 _gestureDetector = new GestureDetector(this);
  _doubleTapListener = new OnDoubleTapListener() {

		@Override
		public boolean onSingleTapConfirmed(final MotionEvent e) {
			// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(final MotionEvent event) {
		return true;
	}

	@Override
	public boolean onDoubleTap(final MotionEvent event) {

		queueEvent(new Runnable() {
			@Override
			public void run() {
			}
		});

		return true;
	}
};
_gestureDetector.setOnDoubleTapListener(_doubleTapListener);
   }


   // The initialization of _widget occurs when the android widget is resized
   // to the screen size
   @Override
   protected void onSizeChanged(int w,
                                int h,
                                int oldw,
                                int oldh) {
      super.onSizeChanged(w, h, oldw, oldh);

      if (_widget == null) {
         // SETTING RENDERER
         _es2renderer = new ES2Renderer(this.getContext(), this);
         setRenderer(_es2renderer);
      }
   }


   @Override
   public boolean onTouchEvent(MotionEvent event) {

      final TouchEvent te = _motionEventProcessor.processEvent(event);

      if (te != null) {
         // SEND MESSAGE TO RENDER THREAD
         queueEvent(new Runnable() {
            @Override
            public void run() {
               _widget.onTouchEvent(te);
            }
         });
         return true;
      }
      return false;
   }


   @Override
   public boolean onDown(MotionEvent arg0) {
   // TODO this method must be implemented
      return false;
   }


   @Override
   public boolean onFling(MotionEvent e1,
                          MotionEvent e2,
                          float velocityX,
                          float velocityY) {
   // TODO this method must be implemented
      return false;
   }


   @Override
   public void onLongPress(MotionEvent e) {
      PointerCoords pc = new PointerCoords();
      e.getPointerCoords(0, pc);
      Touch t = new Touch(new Vector2D(pc.x, pc.y), new Vector2D(0, 0));
      final TouchEvent te = TouchEvent.create(TouchEventType.LongPress, t);

      queueEvent(new Runnable() {
         @Override
         public void run() {
            _widget.onTouchEvent(te);
         }
      });
   }


   @Override
   public boolean onScroll(MotionEvent e1,
                           MotionEvent e2,
                           float distanceX,
                           float distanceY) {
   // TODO this method must be implemented
      return false;
   }


   @Override
   public void onShowPress(MotionEvent e) {
   // TODO this method must be implemented
   }


   @Override
   public boolean onSingleTapUp(MotionEvent e) {
   // TODO this method must be implemented
      return false;
   }


   public G3MWidget getWidget() {
      if (_widget == null) {
         //initWidgetDemo();
         //initSimpleWidgetDemo();
         initWidgetPrivate(_cameraConstraints, _layerSet, _renderers, _userData);
      }
      return _widget;
   }

   /*
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
   //		  IDownloader downloader = new Downloader_Android(1  1024  1024,
   //		                                               64  1024  1024,
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
   //	  IDownloader downloader = null;
     IDownloader downloader = new Downloader_Android(1  1024  1024,
                                                  64  1024  1024,
                                                  ".G3M_Cache",
                                                  8);

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
     if (true) {
       boolean renderDebug = true;
       
       TilesRenderParameters parameters = TilesRenderParameters.createDefault(renderDebug);

       TileTexturizer texturizer = null;
       if (false) {
         texturizer = new MultiLayerTileTexturizer(layerSet);
       }
       else {
         //SINGLE IMAGE
         IImage singleWorldImage = factory.createImageFromFileName("world.jpg");
         texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage);
       }
       
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
   
   */

   //THIS METHOD SAVES PARAMETERS FOR INITIALIZATION IN RENDER THREAD

   ArrayList<ICameraConstrainer>                  _cameraConstraints = null;
   LayerSet                                       _layerSet          = null;
   ArrayList<org.glob3.mobile.generated.Renderer> _renderers         = null;
   UserData                                       _userData          = null;


   public void initWidget(ArrayList<ICameraConstrainer> cameraConstraints,
                          LayerSet layerSet,
                          ArrayList<org.glob3.mobile.generated.Renderer> renderers,
                          UserData userData) {
      _cameraConstraints = cameraConstraints;
      _layerSet = layerSet;
      _renderers = renderers;
      _userData = userData;
   }


   private void initWidgetPrivate(final ArrayList<ICameraConstrainer> cameraConstraints,
                                  final LayerSet layerSet,
                                  final ArrayList<org.glob3.mobile.generated.Renderer> renderers,
                                  final UserData userData) {
      // creates default camera-renderer and camera-handlers
      CameraRenderer cameraRenderer = new CameraRenderer();

      boolean useInertia = true;
      cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));

      boolean processRotation = true;
      boolean processZoom = true;
      cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
      cameraRenderer.addHandler(new CameraRotationHandler());
      cameraRenderer.addHandler(new CameraDoubleTapHandler());

      boolean renderDebug = true;
      boolean useTilesSplitBudget = true;
      boolean forceTopLevelTilesRenderOnStart = true;

      TilesRenderParameters parameters = TilesRenderParameters.createDefault(renderDebug, useTilesSplitBudget,
               forceTopLevelTilesRenderOnStart);

      initWidget(cameraRenderer, cameraConstraints, layerSet, parameters, renderers, userData);

   }


   private void initWidget(CameraRenderer cameraRenderer,
                           ArrayList<ICameraConstrainer> cameraConstraints,
                           LayerSet layerSet,
                           TilesRenderParameters parameters,
                           ArrayList<org.glob3.mobile.generated.Renderer> renderers,
                           UserData userData) {

      // create GLOB3M WIDGET
      int width = getWidth();
      int height = getHeight();

      IStringBuilder.setInstance(new StringBuilder_Android()); //Setting StringBuilder

      IMathUtils.setInstance(new MathUtils_Android()); //Setting MathUtils

      IFactory factory = new Factory_Android(getContext());
      ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
      NativeGL2_Android nGL = new NativeGL2_Android();
      GL gl = new GL(nGL);

      IStorage storage = new SQLiteStorage_Android("g3m.cache", this.getContext());

      //TESTING DB
      if (false) {
         byte[] b = { 1, 0, 1 };
         ByteBuffer bb = new ByteBuffer(b, b.length);
         URL url = new URL("test");
         URL url2 = new URL("test2");

         if (storage.contains(url)) {
            ByteBuffer bb2 = storage.read(url);
         }

         storage.save(url, bb);

         if (storage.contains(url)) {
            ByteBuffer bb1 = storage.read(url);
         }

         storage.save(url, bb);

         if (storage.contains(url)) {
            ByteBuffer bb2 = storage.read(url);
         }

         if (storage.contains(url2)) {
            ByteBuffer bb2 = storage.read(url2);
         }


      }


      //		  IDownloader downloader = null;// new CachedDownloader(new Downloader_Android(8), storage);
      IDownloader downloader = new CachedDownloader(new Downloader_Android(8), storage);

      CompositeRenderer composite = new CompositeRenderer();

      composite.addRenderer(cameraRenderer);

      if (layerSet != null && layerSet.size() > 0) {

         TileTexturizer texturizer;// = new MultiLayerTileTexturizer(layerSet);

         if (true) {
            texturizer = new MultiLayerTileTexturizer(layerSet);
         }
         else {
            //SINGLE IMAGE
            IImage singleWorldImage = factory.createImageFromFileName("world.jpg");
            texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage);
         }


         final boolean showStatistics = false;

         TileRenderer tr = new TileRenderer(new EllipsoidalTileTessellator(parameters._tileResolution, true), texturizer,
                  parameters, showStatistics);

         composite.addRenderer(tr);
      }

      for (int i = 0; i < renderers.size(); i++) {
         composite.addRenderer(renderers.get(i));
      }


      TextureBuilder textureBuilder = new CPUTextureBuilder();
      TexturesHandler texturesHandler = new TexturesHandler(gl, factory, textureBuilder, false);

      Planet planet = Planet.createEarth();

      org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();

      EffectsScheduler scheduler = new EffectsScheduler();

      FrameTasksExecutor frameTasksExecutor = new FrameTasksExecutor();

      IStringUtils stringUtils = new StringUtils_Android();

      IThreadUtils threadUtils = new ThreadUtils_Android(this);

      _widget = G3MWidget.create(frameTasksExecutor, factory, stringUtils, threadUtils, logger, gl, texturesHandler, downloader,
               planet, cameraConstraints, composite, busyRenderer, scheduler, width, height,
               Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1), true, false);

      _widget.setUserData(userData);

   }

}