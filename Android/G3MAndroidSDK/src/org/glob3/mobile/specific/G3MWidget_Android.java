

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BasicShadersGL2;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ErrorRenderer;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GL;
import org.glob3.mobile.generated.GPUProgramFactory;
import org.glob3.mobile.generated.GPUProgramManager;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraActivityListener;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.ITextUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.InfoDisplay;
import org.glob3.mobile.generated.InitialCameraPositionProvider;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.SceneLighting;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.Vector2F;
import org.glob3.mobile.generated.WidgetUserData;

import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;


public final class G3MWidget_Android
extends
GLSurfaceView
implements
OnGestureListener {

   private G3MWidget                  _g3mWidget;
   private ES2Renderer                _es2renderer;

   private final MotionEventProcessor _motionEventProcessor = new MotionEventProcessor();
   private final OnDoubleTapListener  _doubleTapListener;
   private final GestureDetector      _gestureDetector;
   private Thread                     _openGLThread         = null;


   public G3MWidget_Android(final android.content.Context context) {
      this(context, null);
   }


   private void setOpenGLThread(final Thread openGLThread) {
      _openGLThread = openGLThread;
   }


   public final void checkOpenGLThread() {
      if (_openGLThread != null) {
         final Thread currentThread = Thread.currentThread();
         if (currentThread != _openGLThread) {
            throw new RuntimeException("OpenGL code executed from a Non-OpenGL thread.  (OpenGLThread=" + _openGLThread
                     + ", CurrentThread=" + currentThread + ")");
         }
      }
   }


   // Needed to create widget from XML layout
   public G3MWidget_Android(final android.content.Context context,
                            final AttributeSet attrs) {
      super(context, attrs);

      initSingletons();

      setEGLContextClientVersion(2); // OPENGL ES VERSION MUST BE SPECIFED
      setEGLConfigChooser(8, 8, 8, 8, 16, 0);
      //     setEGLConfigChooser(true); // IT GIVES US A RGB DEPTH OF 8 BITS PER
      // CHANNEL, HAVING TO FORCE PROPER BUFFER
      // ALLOCATION

      _es2renderer = new ES2Renderer(this);
      setRenderer(_es2renderer);

      queueEvent(new Runnable() {
         @Override
         public void run() {
            final Thread openGLThread = Thread.currentThread();
            ILogger.instance().logInfo("== OpenGL-Thread=%s", openGLThread.toString());
            _es2renderer.setOpenGLThread(openGLThread);
            setOpenGLThread(openGLThread);
         }
      });

      setLongClickable(true);

      // setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
      // setDebugFlags(DEBUG_CHECK_GL_ERROR);

      if (!isInEditMode()) { // needed to allow visual edition of this widget
         //Double Tap Listener
         _gestureDetector = new GestureDetector(context, this);
         _doubleTapListener = new OnDoubleTapListener() {

            @Override
            public boolean onSingleTapConfirmed(final MotionEvent e) {
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
      else {
         _gestureDetector = null;
         _doubleTapListener = null;
      }
   }


   private void initSingletons() {
      final ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
      final IFactory factory = new Factory_Android(getContext());
      final IStringUtils stringUtils = new StringUtils_Android();
      final IStringBuilder stringBuilder = new StringBuilder_Android();
      final IMathUtils mathUtils = new MathUtils_Android();
      final IJSONParser jsonParser = new JSONParser_Android();
      final ITextUtils textUtils = new TextUtils_Android();

      G3MWidget.initSingletons(logger, factory, stringUtils, stringBuilder, mathUtils, jsonParser, textUtils);
   }


   @Override
   protected void onSizeChanged(final int w,
                                final int h,
                                final int oldw,
                                final int oldh) {
      super.onSizeChanged(w, h, oldw, oldh);

      //      if (_es2renderer == null) {
      //         _es2renderer = new ES2Renderer(this);
      //         setRenderer(_es2renderer);
      //      }
   }


   @Override
   public boolean onTouchEvent(final MotionEvent event) {

      //Notifying gestureDetector for DoubleTap recognition
      _gestureDetector.onTouchEvent(event);

      final TouchEvent te = _motionEventProcessor.processEvent(event);
      if (te == null) {
         return false;
      }

      queueEvent(new Runnable() {
         @Override
         public void run() {
            _g3mWidget.onTouchEvent(te);
         }
      });
      return true;
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
      final MotionEvent.PointerCoords pc = new MotionEvent.PointerCoords();
      e.getPointerCoords(0, pc);
      final Touch t = new Touch(new Vector2F(pc.x, pc.y), Vector2F.zero());
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
         throw new RuntimeException("LAZY INITIALIZATION NEEDED");
         //         initWidget();
         //         _g3mWidget.onResume();
      }
      return _g3mWidget;
   }


   private GPUProgramManager createGPUProgramManager() {
      final GPUProgramFactory factory = new BasicShadersGL2();

      /*
      factory.add(new GPUProgramSources("Billboard", GL2Shaders._billboardVertexShader, GL2Shaders._billboardFragmentShader));
      factory.add(new GPUProgramSources("Default", GL2Shaders._defaultVertexShader, GL2Shaders._defaultFragmentShader));

      factory.add(new GPUProgramSources("ColorMesh", GL2Shaders._colorMeshVertexShader, GL2Shaders._colorMeshFragmentShader));

      factory.add(new GPUProgramSources("TexturedMesh", GL2Shaders._texturedMeshVertexShader,
               GL2Shaders._texturedMeshFragmentShader));

      factory.add(new GPUProgramSources("TransformedTexCoorTexturedMesh", GL2Shaders._transformedTexCoortexturedMeshVertexShader,
               GL2Shaders._transformedTexCoortexturedMeshFragmentShader));

      factory.add(new GPUProgramSources("FlatColorMesh", GL2Shaders._flatColorMeshVertexShader,
               GL2Shaders._flatColorMeshFragmentShader));

      factory.add(new GPUProgramSources("NoColorMesh", GL2Shaders._noColorMeshVertexShader, GL2Shaders._noColorMeshFragmentShader));

      factory.add(new GPUProgramSources("TexturedMesh+DirectionLight", GL2Shaders._TexturedMesh_DirectionLightVertexShader,
               GL2Shaders._TexturedMesh_DirectionLightFragmentShader));

      factory.add(new GPUProgramSources("FlatColor+DirectionLight", GL2Shaders._FlatColorMesh_DirectionLightVertexShader,
               GL2Shaders._FlatColorMesh_DirectionLightFragmentShader));
       */

      return new GPUProgramManager(factory);
   }


   public void initWidget(final IStorage storage,
                          final IDownloader downloader,
                          final IThreadUtils threadUtils,
                          final ICameraActivityListener cameraActivityListener,
                          final Planet planet,
                          final java.util.ArrayList<ICameraConstrainer> cameraConstrainers,
                          final CameraRenderer cameraRenderer,
                          final org.glob3.mobile.generated.Renderer mainRenderer,
                          final org.glob3.mobile.generated.Renderer busyRenderer,
                          final ErrorRenderer errorRenderer,
                          final org.glob3.mobile.generated.Renderer hudRenderer,
                          final Color backgroundColor,
                          final boolean logFPS,
                          final boolean logDownloaderStatistics,
                          final GInitializationTask initializationTask,
                          final boolean autoDeleteInitializationTask,
                          final java.util.ArrayList<PeriodicalTask> periodicalTasks,
                          final SceneLighting sceneLighting,
                          final InitialCameraPositionProvider initialCameraPositionProvider,
                          final WidgetUserData userData,
                          final InfoDisplay infoDisplay) {

      _g3mWidget = G3MWidget.create(//
               getGL(), //
               storage, //
               downloader, //
               threadUtils, //
               cameraActivityListener,//
               planet, //
               cameraConstrainers, //
               cameraRenderer, //
               mainRenderer, //
               busyRenderer, //
               errorRenderer, //
               hudRenderer, //
               backgroundColor, //
               logFPS, //
               logDownloaderStatistics, //
               initializationTask, //
               autoDeleteInitializationTask, //
               periodicalTasks, //
               createGPUProgramManager(), //
               sceneLighting, //
               initialCameraPositionProvider, //
               infoDisplay);

      _g3mWidget.setUserData(userData);
   }


   @Override
   public void onPause() {
      if (_es2renderer != null) {
         _g3mWidget.onPause();
         super.onPause();
      }
   }


   @Override
   public void onResume() {
      if (_es2renderer != null) {
         super.onResume();
         _g3mWidget.onResume();
      }
   }


   public void onDestroy() {
      getG3MWidget().onDestroy();
   }


   //   public void closeStorage() {
   //      if (IDownloader.instance() != null) {
   //         IDownloader.instance().stop();
   //      }
   //      if (IStorage.instance() != null) {
   //         // _storage.onPause(null);
   //         ((SQLiteStorage_Android) IStorage.instance()).close();
   //      }
   //   }


   public Camera getNextCamera() {
      return getG3MWidget().getNextCamera();
   }


   public WidgetUserData getUserData() {
      return getG3MWidget().getUserData();
   }


   public void setAnimatedCameraPosition(final Geodetic3D position,
                                         final TimeInterval interval) {
      getG3MWidget().setAnimatedCameraPosition(interval, position);
   }


   public void setAnimatedCameraPosition(final Geodetic3D position) {
      getG3MWidget().setAnimatedCameraPosition(position);
   }


   public void setCameraPosition(final Geodetic3D position) {
      getG3MWidget().setCameraPosition(position);
   }


   public CameraRenderer getCameraRenderer() {
      return getG3MWidget().getCameraRenderer();
   }


   public void cancelCameraAnimation() {
      getG3MWidget().cancelCameraAnimation();
   }


   public void setCameraHeading(final Angle heading) {
      getG3MWidget().setCameraHeading(heading);
   }


   public void setCameraPitch(final Angle pitch) {
      getG3MWidget().setCameraPitch(pitch);
   }


   public void setCameraRoll(final Angle roll) {
      getG3MWidget().setCameraRoll(roll);
   }


   public void setCameraHeadingPitchRoll(final Angle heading,
                                         final Angle pitch,
                                         final Angle roll) {
      getG3MWidget().setCameraHeadingPitchRoll(heading, pitch, roll);
   }


   public void setWidget(final G3MWidget widget) {
      _g3mWidget = widget;
   }


   public GL getGL() {
      return _es2renderer.getGL();
   }


   public G3MContext getG3MContext() {
      return getG3MWidget().getG3MContext();
   }


}
