

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.BusyMeshRenderer;
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
import org.glob3.mobile.generated.UserData;
import org.glob3.mobile.generated.Vector2D;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;


public class G3MWidget_Android
         extends
            GLSurfaceView
         implements
            OnGestureListener {

   private G3MWidget                                      _g3mWidget;
   private ES2Renderer                                    _es2renderer;

   private final MotionEventProcessor                     _motionEventProcessor = new MotionEventProcessor();
   private final OnDoubleTapListener                      _doubleTapListener;
   private final GestureDetector                          _gestureDetector;

   private ArrayList<ICameraConstrainer>                  _cameraConstraints;
   private LayerSet                                       _layerSet;
   private ArrayList<org.glob3.mobile.generated.Renderer> _renderers;
   private UserData                                       _userData;


   public G3MWidget_Android(final Context context) {
      super(context);

      setEGLContextClientVersion(2); // OPENGL ES VERSION MUST BE SPECIFED
      setEGLConfigChooser(true); // IT GIVES US A RGB DEPTH OF 8 BITS PER
      // CHANNEL, HAVING TO FORCE PROPER BUFFER
      // ALLOCATION

      // Detect Long-Press events
      setLongClickable(true);

      // Debug flags
      if (false) {
         setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
      }

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

            final TouchEvent te = _motionEventProcessor.processDoubleTapEvent(event);

            queueEvent(new Runnable() {
               @Override
               public void run() {
                  _g3mWidget.onTouchEvent(te);
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
   protected void onSizeChanged(final int w,
                                final int h,
                                final int oldw,
                                final int oldh) {
      super.onSizeChanged(w, h, oldw, oldh);

      if (_g3mWidget == null) {
         // SETTING RENDERER
         _es2renderer = new ES2Renderer(this.getContext(), this);
         setRenderer(_es2renderer);
      }
   }


   @Override
   public boolean onTouchEvent(final MotionEvent event) {

      //Notifing gestureDetector for DoubleTap recognition
      _gestureDetector.onTouchEvent(event);

      final TouchEvent te = _motionEventProcessor.processEvent(event);

      if (te != null) {
         // SEND MESSAGE TO RENDER THREAD
         queueEvent(new Runnable() {
            @Override
            public void run() {
               _g3mWidget.onTouchEvent(te);
            }
         });
         return true;
      }
      return false;
   }


   @Override
   public boolean onDown(final MotionEvent arg0) {
      return false;
   }


   @Override
   public boolean onFling(final MotionEvent e1,
                          final MotionEvent e2,
                          final float velocityX,
                          final float velocityY) {
      return false;
   }


   @Override
   public void onLongPress(final MotionEvent e) {
      final PointerCoords pc = new PointerCoords();
      e.getPointerCoords(0, pc);
      final Touch t = new Touch(new Vector2D(pc.x, pc.y), new Vector2D(0, 0));
      final TouchEvent te = TouchEvent.create(TouchEventType.LongPress, t);

      queueEvent(new Runnable() {
         @Override
         public void run() {
            _g3mWidget.onTouchEvent(te);
         }
      });
   }


   @Override
   public boolean onScroll(final MotionEvent e1,
                           final MotionEvent e2,
                           final float distanceX,
                           final float distanceY) {
      return false;
   }


   @Override
   public void onShowPress(final MotionEvent e) {

   }


   @Override
   public boolean onSingleTapUp(final MotionEvent e) {
      return false;
   }


   public G3MWidget getG3MWidget() {
      if (_g3mWidget == null) {
         initWidget();
      }
      return _g3mWidget;
   }


   public void initWidget(final ArrayList<ICameraConstrainer> cameraConstraints,
                          final LayerSet layerSet,
                          final ArrayList<org.glob3.mobile.generated.Renderer> renderers,
                          final UserData userData) {
      _cameraConstraints = cameraConstraints;
      _layerSet = layerSet;
      _renderers = renderers;
      _userData = userData;
   }


   private void initWidget() {
      // creates default camera-renderer and camera-handlers
      final CameraRenderer cameraRenderer = new CameraRenderer();

      final boolean useInertia = true;
      cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));

      final boolean processRotation = true;
      final boolean processZoom = true;
      cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
      cameraRenderer.addHandler(new CameraRotationHandler());
      cameraRenderer.addHandler(new CameraDoubleTapHandler());

      final boolean renderDebug = false;
      final boolean useTilesSplitBudget = true;
      final boolean forceTopLevelTilesRenderOnStart = true;

      final TilesRenderParameters parameters = TilesRenderParameters.createDefault(renderDebug, useTilesSplitBudget,
               forceTopLevelTilesRenderOnStart);

      initWidget(cameraRenderer, parameters);
   }


   private void initWidget(final CameraRenderer cameraRenderer,
                           final TilesRenderParameters parameters) {

      // create GLOB3M WIDGET
      final int width = getWidth();
      final int height = getHeight();

      final IFactory factory = new Factory_Android(getContext());
      final ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
      final NativeGL2_Android nGL = new NativeGL2_Android();
      final GL gl = new GL(nGL);

      final IStorage storage = new SQLiteStorage_Android("g3m.cache", this.getContext());

      final int connectTimeout = 60000;
      final int readTimeout = 60000;
      final IDownloader downloader = new CachedDownloader(new Downloader_Android(8, connectTimeout, readTimeout), storage);

      final CompositeRenderer composite = new CompositeRenderer();

      composite.addRenderer(cameraRenderer);

      if ((_layerSet != null) && (_layerSet.size() > 0)) {

         TileTexturizer texturizer;// = new MultiLayerTileTexturizer(layerSet);

         if (true) {
            texturizer = new MultiLayerTileTexturizer(_layerSet);
         }
         else {
            //SINGLE IMAGE
            final IImage singleWorldImage = factory.createImageFromFileName("world.jpg");
            texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage, false);
         }


         final boolean showStatistics = false;

         final TileRenderer tr = new TileRenderer(new EllipsoidalTileTessellator(parameters._tileResolution, true), texturizer,
                  parameters, showStatistics);

         composite.addRenderer(tr);
      }

      for (final org.glob3.mobile.generated.Renderer renderer : _renderers) {
         composite.addRenderer(renderer);
      }

      final TextureBuilder textureBuilder = new CPUTextureBuilder();
      final TexturesHandler texturesHandler = new TexturesHandler(gl, factory, false);

      final Planet planet = Planet.createEarth();

      final org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();

      final EffectsScheduler scheduler = new EffectsScheduler();

      final FrameTasksExecutor frameTasksExecutor = new FrameTasksExecutor();

      final IStringUtils stringUtils = new StringUtils_Android();

      final IThreadUtils threadUtils = new ThreadUtils_Android(this);

      final StringBuilder_Android stringBuilder = new StringBuilder_Android();

      final IMathUtils math = new MathUtils_Android();

      _g3mWidget = G3MWidget.create(frameTasksExecutor, factory, stringUtils, threadUtils, stringBuilder, math, logger, gl,
               texturesHandler, textureBuilder, downloader, planet, _cameraConstraints, composite, busyRenderer, scheduler,
               width, height, Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1), true, false);

      _g3mWidget.setUserData(_userData);
   }


   @Override
   public void onPause() {

      final int __TODO_check_onpause;
      //      if (_g3mWidget != null) {
      //         _g3mWidget.onPause();
      //      }

      /*
      if (_g3mWidget == null) {
         System.err.println("break (point) on me");
      }
      */
      super.onPause();
   }


   @Override
   public void onResume() {
      if (_g3mWidget != null) {
         super.onResume();
         _g3mWidget.onResume();
      }
   }

}
