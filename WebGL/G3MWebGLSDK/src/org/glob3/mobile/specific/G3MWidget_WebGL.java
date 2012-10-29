

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.EllipsoidalTileTessellator;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IGLProgramId;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.MultiLayerTileTexturizer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TilesRenderParameters;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.UserData;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;


public final class G3MWidget_WebGL
         extends
            Composite {


   public static final String            CANVAS_ID             = "g3m-canvas";

   private final FlowPanel               _panel;
   private Canvas                        _canvas;
   private MotionEventProcessor          _motionEventProcessor = null;
   private ArrayList<ICameraConstrainer> _cameraConstraints    = null;
   private LayerSet                      _layerSet             = null;
   private ArrayList<Renderer>           _renderers            = null;
   private UserData                      _userData             = null;

   private IGLProgramId                  _program              = null;
   private JavaScriptObject              _webGLContext         = null;

   private G3MWidget                     _widget;

   private int                           _width;
   private int                           _height;
   private final int                     _delayMillis;
   private final String                  _proxy;

   private ArrayList<String>             _imagesToPreload;

   private GTask                         _initializationTask;

   private ArrayList<PeriodicalTask>     _periodicalTasks;

   private boolean                       _incrementalTileQuality;


   public G3MWidget_WebGL(final String proxy) {
      // downloader
      _delayMillis = 10;
      _proxy = proxy;

      initSingletons();

      _panel = new FlowPanel();

      initWidget(_panel);

      _canvas = Canvas.createIfSupported();

      if (_canvas == null) {
         _panel.add(new Label("Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo."));
         return;
      }

      _canvas.getCanvasElement().setId(CANVAS_ID);
      _panel.add(_canvas);

      // Events
      sinkEvents(Event.MOUSEEVENTS | Event.ONCONTEXTMENU | Event.KEYEVENTS | Event.ONDBLCLICK | Event.ONMOUSEWHEEL);

      Window.addResizeHandler(new ResizeHandler() {
         @Override
         public void onResize(final ResizeEvent event) {
            onSizeChanged(event.getWidth(), event.getHeight());
         }
      });

      onSizeChanged(Window.getClientWidth(), Window.getClientHeight());
   }


   public void initSingletons() {
      final ILogger logger = new Logger_WebGL(LogLevel.InfoLevel);
      final IFactory factory = new Factory_WebGL();
      final IStringUtils stringUtils = new StringUtils_WebGL();
      final IThreadUtils threadUtils = new ThreadUtils_WebGL(_delayMillis);
      final IStringBuilder stringBuilder = new StringBuilder_WebGL();
      final IMathUtils mathUtils = new MathUtils_WebGL();
      final IJSONParser jsonParser = new JSONParser_WebGL();

      G3MWidget.initSingletons(logger, factory, stringUtils, threadUtils, stringBuilder, mathUtils, jsonParser);
   }


   protected void onSizeChanged(final int w,
                                final int h) {
      _width = w;
      _height = h;

      _panel.setPixelSize(_width, _height);
      setPixelSize(_width, _height);
      _canvas.setCoordinateSpaceWidth(_width);
      _canvas.setCoordinateSpaceHeight(_height);
      if (_widget != null) {
         _widget.onResizeViewportEvent(_width, _height);
         jsOnResizeViewport(_width, _height);
      }
   }


   public String getProxy() {
      return _proxy;
   }


   @Override
   public void onBrowserEvent(final Event event) {
      _canvas.setFocus(true);

      if (_motionEventProcessor != null) {
         _motionEventProcessor.processEvent(event);
      }

      super.onBrowserEvent(event);
   }


   private native void jsDefineG3MBrowserObjects() /*-{
		//		debugger;
		var that = this;

		// URL Object
		$wnd.g3mURL = $wnd.URL || $wnd.webkitURL;

		// IndexedDB
		$wnd.g3mIDB = $wnd.indexedDB || $wnd.webkitIndexedDB
				|| $wnd.mozIndexedDB || $wnd.OIndexedDB || $wnd.msIndexedDB;
		$wnd.g3mIDBTransaction = $wnd.IDBTransaction
				|| $wnd.webkitIDBTransaction || $wnd.OIDBTransaction
				|| $wnd.msIDBTransaction;
		$wnd.g3mDBVersion = 1;

		// Animation
		// Provides requestAnimationFrame in a cross browser way.
		$wnd.requestAnimFrame = (function() {
			return $wnd.requestAnimationFrame
					|| $wnd.webkitRequestAnimationFrame
					|| $wnd.mozRequestAnimationFrame
					|| $wnd.oRequestAnimationFrame
					|| $wnd.msRequestAnimationFrame
					|| function(callback, element) {
						return $wnd.setTimeout(callback, 1000 / 60);
					};
		})();

		// Provides cancelAnimationFrame in a cross browser way.
		$wnd.cancelAnimFrame = (function() {
			return $wnd.cancelAnimationFrame || $wnd.webkitCancelAnimationFrame
					|| $wnd.mozCancelAnimationFrame
					|| $wnd.oCancelAnimationFrame
					|| $wnd.msCancelAnimationFrame || $wnd.clearTimeout;
		})();

		$wnd.g3mTick = function() {
			$wnd.requestAnimFrame($wnd.g3mTick);
			that.@org.glob3.mobile.specific.G3MWidget_WebGL::renderG3MWidget()();
		};
   }-*/;


   private native JavaScriptObject jsGetWebGLContext() /*-{
		//		debugger;
		var canvas = null, context = null;
		var contextNames = [ "experimental-webgl", "webgl", "webkit-3d",
				"moz-webgl" ];

		var canvas = $doc
				.getElementById(@org.glob3.mobile.specific.G3MWidget_WebGL::CANVAS_ID);

		if (canvas != null) {
			for ( var cn in contextNames) {
				try {
					context = canvas.getContext(contextNames[cn]);
					//STORING SIZE FOR GLVIEWPORT
					context.viewportWidth = canvas.width;
					context.viewportHeight = canvas.height;
				} catch (e) {
				}
				if (context) {
					break;
				}
			}
			if (context == null) {
				alert("No WebGL context available");
			}
		} else {
			alert("No canvas available");
		}

		return context;
   }-*/;


   public void initWidget(final ArrayList<ICameraConstrainer> cameraConstraints,
                          final LayerSet layerSet,
                          final ArrayList<Renderer> renderers,
                          final UserData userData,
                          final ArrayList<String> images,
                          final GTask initializationTask,
                          final ArrayList<PeriodicalTask> periodicalTasks,
                          final boolean incrementalTileQuality) {
      jsDefineG3MBrowserObjects();

      _cameraConstraints = cameraConstraints;
      _layerSet = layerSet;
      _renderers = renderers;
      _userData = userData;
      _imagesToPreload = images;
      _initializationTask = initializationTask;
      _periodicalTasks = (periodicalTasks == null) ? new ArrayList<PeriodicalTask>() : periodicalTasks;

      _incrementalTileQuality = incrementalTileQuality;

      // TODO TEMP HACK TO PRELOAD IMAGES
      preloadImagesAndInitWidget();
   }


   private void preloadImagesAndInitWidget() {
      if ((_imagesToPreload != null) && (_imagesToPreload.size() > 0)) {
         jsDownloadImage(_imagesToPreload.remove(0));
      }
      else {
         initG3MWidget();
      }
   }


   private void initG3MWidget() {
      final CameraRenderer cameraRenderer = new CameraRenderer();

      final boolean useInertia = true;
      cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));

      final boolean processRotataion = true;
      final boolean processZoom = true;
      cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotataion, processZoom));
      cameraRenderer.addHandler(new CameraRotationHandler());
      cameraRenderer.addHandler(new CameraDoubleTapHandler());

      final boolean renderDebug = false;
      final boolean useTilesSplitBudget = true;
      final boolean forceTopLevelTilesRenderOnStart = true;

      final TilesRenderParameters parameters = TilesRenderParameters.createDefault(renderDebug, useTilesSplitBudget,
               forceTopLevelTilesRenderOnStart, _incrementalTileQuality);

      //      final IStorage storage = new IndexedDBStorage_WebGL();
      final IDownloader downloader = new Downloader_WebGL(8, _delayMillis, _proxy);

      _webGLContext = jsGetWebGLContext();
      if (_webGLContext == null) {
         throw new RuntimeException("webGLContext null");
      }

      //CREATING SHADERS PROGRAM
      _program = new Shaders_WebGL(_webGLContext).createProgram();

      final NativeGL_WebGL nativeGL = new NativeGL_WebGL(_webGLContext);

      final CompositeRenderer mainRenderer = new CompositeRenderer();
      //      composite.addRenderer(cameraRenderer);

      if (_layerSet != null) {
         final boolean showStatistics = false;

         final TileRenderer tr = new TileRenderer( //
                  new EllipsoidalTileTessellator(parameters._tileResolution, true), //
                  new MultiLayerTileTexturizer(), //
                  _layerSet, //
                  parameters, //
                  showStatistics);

         mainRenderer.addRenderer(tr);
      }

      for (int i = 0; i < _renderers.size(); i++) {
         mainRenderer.addRenderer(_renderers.get(i));
      }

      final Planet planet = Planet.createEarth();
      final BusyMeshRenderer busyRenderer = new BusyMeshRenderer();

      final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1);
      final boolean logFPS = false;
      final boolean logDownloaderStatistics = false;

      _widget = G3MWidget.create( //
               nativeGL, //
               downloader, //
               planet, //
               _cameraConstraints, //
               cameraRenderer, //
               mainRenderer, //
               busyRenderer, //
               _width, //
               _height, //
               backgroundColor, //
               logFPS, //
               logDownloaderStatistics, //
               _initializationTask, //
               true, //
               _periodicalTasks);

      _widget.setUserData(_userData);

      //      //Testing Periodical Tasks
      //      if (true) {
      //         class PeriodicTask
      //                  extends
      //                     GTask {
      //            private long      _lastExec;
      //            private final int _number;
      //
      //
      //            public PeriodicTask(final int n) {
      //               _number = n;
      //            }
      //
      //
      //            @Override
      //            public void run() {
      //               final ITimer t = IFactory.instance().createTimer();
      //               final long now = t.now().milliseconds();
      //               ILogger.instance().logInfo("Running periodical Task " + _number + " - " + (now - _lastExec) + " ms.\n");
      //               _lastExec = now;
      //               IFactory.instance().deleteTimer(t);
      //            }
      //         }
      //
      //         _widget.addPeriodicalTask(TimeInterval.fromMilliseconds(4000), new PeriodicTask(1));
      //         _widget.addPeriodicalTask(TimeInterval.fromMilliseconds(6000), new PeriodicTask(2));
      //         _widget.addPeriodicalTask(TimeInterval.fromMilliseconds(500), new PeriodicTask(3));
      //      }

      _motionEventProcessor = new MotionEventProcessor(_widget);

      if (_program != null) {
         _widget.getGL().useProgram(_program);
      }
      else {
         throw new RuntimeException("PROGRAM INVALID");
      }

      startRenderLoop();
   }


   private void storeDownloadedImage(final String url,
                                     final JavaScriptObject imgJS) {
      ((Factory_WebGL) IFactory.instance()).storeDownloadedImage(url, imgJS);
   }


   private native void jsDownloadImage(String url) /*-{
		//		debugger;
		var that = this;

		var imgObject = new Image();
		imgObject.onload = function() {
			that.@org.glob3.mobile.specific.G3MWidget_WebGL::storeDownloadedImage(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(url, imgObject);
			that.@org.glob3.mobile.specific.G3MWidget_WebGL::preloadImagesAndInitWidget()();
		}
		imgObject.onabort = function() {
			that.@org.glob3.mobile.specific.G3MWidget_WebGL::preloadImagesAndInitWidget()();
		}
		imgObject.onerror = function() {
			that.@org.glob3.mobile.specific.G3MWidget_WebGL::preloadImagesAndInitWidget()();
		}

		imgObject.src = url;
   }-*/;


   private native void startRenderLoop() /*-{
		//		debugger;

		$wnd.g3mTick();
   }-*/;


   private void renderG3MWidget() {
      //USING PROGRAM
      //      if (_program != null) {
      //jsGLInit();
      //         _widget.getGL().useProgram(_program);
      _widget.render();
      //      }
      //      else {
      //         throw new RuntimeException("PROGRAM INVALID");
      //      }
   }


   private native void jsOnResizeViewport(final int width,
                                          final int height) /*-{
		var webGLContext = this.@org.glob3.mobile.specific.G3MWidget_WebGL::_webGLContext;

		webGLContext.viewport(0, 0, width, height);
		webGLContext.clear(webGLContext.COLOR_BUFFER_BIT
				| webGLContext.DEPTH_BUFFER_BIT);
   }-*/;


   public G3MWidget getG3MWidget() {
      return _widget;
   }


   public Camera getNextCamera() {
      return getG3MWidget().getNextCamera();
   }


   public UserData getUserData() {
      return getG3MWidget().getUserData();
   }


   public void setAnimatedCameraPosition(final Geodetic3D position,
                                         final TimeInterval interval) {
      getG3MWidget().setAnimatedCameraPosition(position, interval);
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


   public void setCameraHeading(final Angle angle) {
      getG3MWidget().setCameraHeading(angle);
   }


   public void setCameraPitch(final Angle angle) {
      getG3MWidget().setCameraPitch(angle);
   }


   public void resetCameraPosition() {
      getG3MWidget().resetCameraPosition();
   }

}
