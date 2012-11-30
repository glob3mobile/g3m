

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.G3MContext;
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
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.UserData;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


public final class G3MWidget_WebGL
         extends
            Composite {


   private Canvas               _canvas;
   private MotionEventProcessor _motionEventProcessor;
   private int                  _width;
   private int                  _height;

   private IGLProgramId         _program;
   private JavaScriptObject     _webGLContext;

   private G3MWidget            _g3mWidget;


   public G3MWidget_WebGL() {

      initSingletons();

      _canvas = Canvas.createIfSupported();
      if (_canvas == null) {
         initWidget(createUnsupportedMessage("Your browser does not support the HTML5 Canvas."));

         return;
      }

      _webGLContext = jsGetWebGLContext(_canvas.getCanvasElement());
      if (_webGLContext == null) {
         initWidget(createUnsupportedMessage("Your browser does not support WebGL."));

         return;
      }

      initWidget(_canvas);
      onSizeChanged(1, 1);

      jsDefineG3MBrowserObjects();

      // CREATING SHADERS PROGRAM
      _program = new Shaders_WebGL(_webGLContext).createProgram();

      // Events
      sinkEvents(Event.MOUSEEVENTS | Event.ONCONTEXTMENU | Event.KEYEVENTS | Event.ONDBLCLICK | Event.ONMOUSEWHEEL);
   }


   private VerticalPanel createUnsupportedMessage(final String message) {
      final VerticalPanel panel = new VerticalPanel();

      panel.add(new Label(message));
      panel.add(new Label("Please upgrade your browser to get this running."));

      return panel;
   }


   public boolean isSupported() {
      return ((_canvas != null) && (_webGLContext != null));
   }


   public void initSingletons() {
      final ILogger logger = new Logger_WebGL(LogLevel.InfoLevel);
      final IFactory factory = new Factory_WebGL();
      final IStringUtils stringUtils = new StringUtils_WebGL();
      final IStringBuilder stringBuilder = new StringBuilder_WebGL();
      final IMathUtils mathUtils = new MathUtils_WebGL();
      final IJSONParser jsonParser = new JSONParser_WebGL();

      G3MWidget.initSingletons(logger, factory, stringUtils, stringBuilder, mathUtils, jsonParser);
   }


   @Override
   public void onBrowserEvent(final Event event) {
      _canvas.setFocus(true);

      if (_motionEventProcessor != null) {
         _motionEventProcessor.processEvent(event);
      }
      super.onBrowserEvent(event);
   }


   private native void jsAddResizeHandler(JavaScriptObject jsCanvas) /*-{
		//		debugger;
		var that = this;
		$wnd.g3mWidgetResize = function() {
			if ((jsCanvas.clientWidth != jsCanvas.parentNode.clientWidth)
					|| (jsCanvas.clientHeight != jsCanvas.parentNode.clientHeight)) {
				that.@org.glob3.mobile.specific.G3MWidget_WebGL::onSizeChanged(II)(jsCanvas.parentNode.clientWidth, jsCanvas.parentNode.clientHeight);
			}
		};

		$wnd.g3mWidgetResizeChecker = setInterval($wnd.g3mWidgetResize, 200);
   }-*/;


   protected void onSizeChanged(final int w,
                                final int h) {

      if ((_width != w) || (_height != h)) {
         _width = w;
         _height = h;

         setPixelSize(_width, _height);
         _canvas.setCoordinateSpaceWidth(_width);
         _canvas.setCoordinateSpaceHeight(_height);
         if (_g3mWidget != null) {
            _g3mWidget.onResizeViewportEvent(_width, _height);
            jsOnResizeViewport(_width, _height);
         }
      }
   }


   private native void jsOnResizeViewport(final int width,
                                          final int height) /*-{
		var webGLContext = this.@org.glob3.mobile.specific.G3MWidget_WebGL::_webGLContext;

		webGLContext.viewport(0, 0, width, height);
		webGLContext.clear(webGLContext.COLOR_BUFFER_BIT
				| webGLContext.DEPTH_BUFFER_BIT);
   }-*/;


   private native void jsDefineG3MBrowserObjects() /*-{
		//		debugger;
		var that = this;

		// URL Object
		$wnd.g3mURL = $wnd.URL || $wnd.webkitURL;

		// IndexedDB
		//		$wnd.g3mIDB = $wnd.indexedDB || $wnd.webkitIndexedDB
		//				|| $wnd.mozIndexedDB || $wnd.OIndexedDB || $wnd.msIndexedDB;
		//		$wnd.g3mIDBTransaction = $wnd.IDBTransaction
		//				|| $wnd.webkitIDBTransaction || $wnd.OIDBTransaction
		//				|| $wnd.msIDBTransaction;
		//		$wnd.g3mDBVersion = 1;

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


   private native JavaScriptObject jsGetWebGLContext(JavaScriptObject jsCanvas) /*-{
		//		debugger;
		var context = null;
		var contextNames = [ "experimental-webgl", "webgl", "webkit-3d",
				"moz-webgl" ];

		if (jsCanvas != null) {
			for ( var cn in contextNames) {
				try {
					context = jsCanvas.getContext(contextNames[cn]);
					//STORING SIZE FOR GLVIEWPORT
					context.viewportWidth = jsCanvas.width;
					context.viewportHeight = jsCanvas.height;
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


   public void initWidget(final INativeGL nativeGL,
                          final IStorage storage,
                          final IDownloader downloader,
                          final IThreadUtils threadUtils,
                          final Planet planet,
                          final ArrayList<ICameraConstrainer> cameraConstraints,
                          final CameraRenderer cameraRenderer,
                          final Renderer mainRenderer,
                          final Renderer busyRenderer,
                          final Color backgroundColor,
                          final boolean logFPS,
                          final boolean logDownloaderStatistics,
                          final GTask initializationTask,
                          final boolean autoDeleteInitializationTask,
                          final ArrayList<PeriodicalTask> periodicalTasks,
                          final UserData userData) {

      _g3mWidget = G3MWidget.create(//
               nativeGL, //
               storage, //
               downloader, //
               threadUtils, //
               planet, //
               cameraConstraints, //
               cameraRenderer, //
               mainRenderer, //
               busyRenderer, //
               backgroundColor, //
               logFPS, //
               logDownloaderStatistics, //
               initializationTask, //
               autoDeleteInitializationTask, //
               periodicalTasks);

      _g3mWidget.setUserData(userData);

      startWidget();
   }


   public void startWidget() {
      _motionEventProcessor = new MotionEventProcessor(_g3mWidget, _canvas.getCanvasElement());
      jsAddResizeHandler(_canvas.getCanvasElement());

      if (_program != null) {
         _g3mWidget.getGL().useProgram(_program);
      }
      else {
         throw new RuntimeException("PROGRAM INVALID");
      }

      jsStartRenderLoop();
   }


   private native void jsStartRenderLoop() /*-{
		//		debugger;

		$wnd.g3mTick();
   }-*/;


   private void renderG3MWidget() {
      //USING PROGRAM
      //      if (_program != null) {
      //jsGLInit();
      //         _g3mWidget.getGL().useProgram(_program);
      _g3mWidget.render(_width, _height);
      //      }
      //      else {
      //         throw new RuntimeException("PROGRAM INVALID");
      //      }
   }


   public JavaScriptObject getWebGLContext() {
      return _webGLContext;
   }


   public void setG3MWidget(final G3MWidget widget) {
      _g3mWidget = widget;
   }


   public G3MWidget getG3MWidget() {
      return _g3mWidget;
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


   public G3MContext getG3MContext() {
      return getG3MWidget().getG3MContext();
   }

}
