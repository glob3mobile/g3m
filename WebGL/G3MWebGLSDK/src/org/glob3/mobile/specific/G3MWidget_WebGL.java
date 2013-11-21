

package org.glob3.mobile.specific;

import java.util.ArrayList;

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
import org.glob3.mobile.generated.GPUProgramSources;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraActivityListener;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.ITextUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.InitialCameraPositionProvider;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.SceneLighting;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.WidgetUserData;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


public class G3MWidget_WebGL
         extends
            Composite {

   private Canvas               _canvas;
   private JavaScriptObject     _webGLContext;
   private int                  _width;
   private int                  _height;
   private MotionEventProcessor _motionEventProcessor;
   private GL                   _gl;
   private G3MWidget            _g3mWidget;


   public G3MWidget_WebGL() {

      initSingletons();

      _canvas = Canvas.createIfSupported();
      _canvas.getCanvasElement().setId("_g3m_canvas");
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

      final INativeGL nativeGL = new NativeGL_WebGL(_webGLContext);
      _gl = new GL(nativeGL, false);

      jsDefineG3MBrowserObjects();

      // Events

      if (TouchEvent.isSupported()) {
         sinkEvents(Event.TOUCHEVENTS);
      }
      else {
         sinkEvents(Event.MOUSEEVENTS | Event.ONCONTEXTMENU | Event.ONDBLCLICK | Event.ONMOUSEWHEEL);
      }

      exportJSFunctions();
   }


   private native void exportJSFunctions() /*-{
		var that = this;
		if (!$wnd.G3M) {
			$wnd.G3M = {};
		}

		//	$wnd.Geodetic3D = $entry(@org.glob3.mobile.generated.Geodetic3D::new(Lorg/glob3/mobile/generated/Angle;Lorg/glob3/mobile/generated/Angle;D));
		//	$wnd.setAnimatedCameraPosition = $entry(function (widget, position) {
		//		widget.@org.glob3.mobile.specific.G3MWidget_WebGL::setAnimatedCameraPosition(Lorg/glob3/mobile/generated/Geodetic3D;)(position);
		//	});
		//	$wnd.angleFromDegrees = $entry(function (degrees) {
		//		return @org.glob3.mobile.generated.Angle::fromDegrees(D)(degrees);
		//	});

		$wnd.G3M.takeScreenshotAsImage = $entry(function() {
			return that.@org.glob3.mobile.specific.G3MWidget_WebGL::takeScreenshotAsImage()();
		});
		$wnd.G3M.takeScreenshotAsBase64 = $entry(function() {
			return that.@org.glob3.mobile.specific.G3MWidget_WebGL::takeScreenshotAsBase64()();
		});
		$wnd.G3M.getCameraData = $entry(function() {
			return that.@org.glob3.mobile.specific.G3MWidget_WebGL::getCameraData()();
		});
		// temp Java function with parameters and return value to test JS function calls
		$wnd.G3M.newGeodetic3D = $entry(function(latitude, longitude, height) {
			return that.@org.glob3.mobile.specific.G3MWidget_WebGL::newGeodetic3D(DDD)(latitude, longitude, height);
		});
		$wnd.G3M.moveCameraTo = $entry(function(position) {
			//return that.@org.glob3.mobile.specific.G3MWidget_WebGL::moveToSpain()();
			//return that.@org.glob3.mobile.specific.G3MWidget_WebGL::moveToSpain(D)(height);
			//return that.@org.glob3.mobile.specific.G3MWidget_WebGL::moveCameraTo(DDD)(latitude, longitude, height);
			that.@org.glob3.mobile.specific.G3MWidget_WebGL::moveCameraTo(Lorg/glob3/mobile/generated/Geodetic3D;)(position);
		});
   }-*/;


   public void moveCameraTo(final Geodetic3D position) {
      _g3mWidget.setAnimatedCameraPosition(TimeInterval.fromSeconds(5), position);
   }


   public Geodetic3D newGeodetic3D(final double latitude,
                                   final double longitude,
                                   final double height) {
      return new Geodetic3D(Angle.fromDegrees(latitude), Angle.fromDegrees(longitude), height);
   }


   public final native JavaScriptObject getCameraData() /*-{
		var widget = this.@org.glob3.mobile.specific.G3MWidget_WebGL::_g3mWidget;
		var camera = widget.@org.glob3.mobile.generated.G3MWidget::getCurrentCamera()();

		var position = camera.@org.glob3.mobile.generated.Camera::getGeodeticPosition()();
		var latitude = position.@org.glob3.mobile.generated.Geodetic3D::_latitude;
		var longitude = position.@org.glob3.mobile.generated.Geodetic3D::_longitude;
		var height = position.@org.glob3.mobile.generated.Geodetic3D::_height;

		var heading = camera.@org.glob3.mobile.generated.Camera::getHeading()();
		var pitch = camera.@org.glob3.mobile.generated.Camera::getPitch()();

		var result = new Object();
		result.latitude = latitude.@org.glob3.mobile.generated.Angle::_degrees;
		result.longitude = longitude.@org.glob3.mobile.generated.Angle::_degrees;
		result.height = height;

		result.heading = heading.@org.glob3.mobile.generated.Angle::_degrees;
		result.pitch = pitch.@org.glob3.mobile.generated.Angle::_degrees;

		return result;
   }-*/;


   public native String takeScreenshotAsBase64() /*-{
		var javaCanvas = this.@org.glob3.mobile.specific.G3MWidget_WebGL::_canvas;
		var canvas = javaCanvas.@com.google.gwt.canvas.client.Canvas::getCanvasElement()();
		var dataURL = canvas.toDataURL("image/jpeg");
		return dataURL.replace(/^data:image\/(png|jpg|jpeg);base64,/, "");
   }-*/;


   public native JavaScriptObject takeScreenshotAsImage() /*-{
		var javaCanvas = this.@org.glob3.mobile.specific.G3MWidget_WebGL::_canvas;
		var canvas = javaCanvas.@com.google.gwt.canvas.client.Canvas::getCanvasElement()();
		var image = new Image();
		image.width = canvas.width;
		image.height = canvas.height;
		image.src = canvas.toDataURL("image/jpeg");
		return image;
   }-*/;


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
      final ITextUtils textUtils = new TextUtils_WebGL();

      G3MWidget.initSingletons(logger, factory, stringUtils, stringBuilder, mathUtils, jsonParser, textUtils);
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
		webGLContext.clear(webGLContext.COLOR_BUFFER_BIT | webGLContext.DEPTH_BUFFER_BIT);
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
			return $wnd.requestAnimationFrame || $wnd.webkitRequestAnimationFrame
					|| $wnd.mozRequestAnimationFrame || $wnd.oRequestAnimationFrame
					|| $wnd.msRequestAnimationFrame || function(callback, element) {
						return $wnd.setTimeout(callback, 1000 / 60);
					};
		})();

		// Provides cancelAnimationFrame in a cross browser way.
		$wnd.cancelAnimFrame = (function() {
			return $wnd.cancelAnimationFrame || $wnd.webkitCancelAnimationFrame
					|| $wnd.mozCancelAnimationFrame || $wnd.oCancelAnimationFrame
					|| $wnd.msCancelAnimationFrame || $wnd.clearTimeout;
		})();

		$wnd.g3mTick = function() {
			$wnd.requestAnimFrame($wnd.g3mTick);
			that.@org.glob3.mobile.specific.G3MWidget_WebGL::renderG3MWidget()();
		};
   }-*/;


   private native JavaScriptObject jsGetWebGLContext(JavaScriptObject jsCanvas) /*-{
		var context = null;
		var contextNames = [ "experimental-webgl", "webgl", "webkit-3d", "moz-webgl" ];

		if (jsCanvas != null) {
			for ( var cn in contextNames) {
				try {
					context = jsCanvas.getContext(contextNames[cn], {
						preserveDrawingBuffer : true
					});
					//STORING SIZE FOR GLVIEWPORT
					context.viewportWidth = jsCanvas.width;
					context.viewportHeight = jsCanvas.height;
				}
				catch (e) {
				}
				if (context) {
					jsCanvas.addEventListener("webglcontextlost", function(event) {
						event.preventDefault();
						$wnd.alert("webglcontextlost");
					}, false);
					break;
				}
			}
			if (context == null) {
				alert("No WebGL context available");
			}
		}
		else {
			alert("No canvas available");
		}

		return context;
   }-*/;


   private GPUProgramManager createGPUProgramManager() {
      final GPUProgramFactory factory = new BasicShadersGL2();
      
      /*
      factory.add(new GPUProgramSources("Billboard", Shaders_WebGL._billboardVertexShader, Shaders_WebGL._billboardFragmentShader));
      factory.add(new GPUProgramSources("Default", Shaders_WebGL._defaultVertexShader, Shaders_WebGL._defaultFragmentShader));

      factory.add(new GPUProgramSources("ColorMesh", Shaders_WebGL._colorMeshVertexShader, Shaders_WebGL._colorMeshFragmentShader));

      factory.add(new GPUProgramSources("TexturedMesh", Shaders_WebGL._texturedMeshVertexShader,
               Shaders_WebGL._texturedMeshFragmentShader));

      factory.add(new GPUProgramSources("TransformedTexCoorTexturedMesh",
               Shaders_WebGL._transformedTexCoortexturedMeshVertexShader,
               Shaders_WebGL._transformedTexCoortexturedMeshFragmentShader));

      factory.add(new GPUProgramSources("FlatColorMesh", Shaders_WebGL._flatColorMeshVertexShader,
               Shaders_WebGL._flatColorMeshFragmentShader));

      factory.add(new GPUProgramSources("NoColorMesh", Shaders_WebGL._noColorMeshVertexShader,
               Shaders_WebGL._noColorMeshFragmentShader));

      factory.add(new GPUProgramSources("TexturedMesh+DirectionLight", Shaders_WebGL._TexturedMesh_DirectionLightVertexShader,
               Shaders_WebGL._TexturedMesh_DirectionLightFragmentShader));

      factory.add(new GPUProgramSources("FlatColorMesh+DirectionLight", Shaders_WebGL._FlatColorMesh_DirectionLightVertexShader,
               Shaders_WebGL._FlatColorMesh_DirectionLightFragmentShader));
               */

      return new GPUProgramManager(factory);
   }


   public void initWidget(/*final INativeGL nativeGL,*/
                          final IStorage storage,
                          final IDownloader downloader,
                          final IThreadUtils threadUtils,
                          final ICameraActivityListener cameraActivityListener,
                          final Planet planet,
                          final ArrayList<ICameraConstrainer> cameraConstraints,
                          final CameraRenderer cameraRenderer,
                          final Renderer mainRenderer,
                          final Renderer busyRenderer,
                          final ErrorRenderer errorRenderer,
                          final Color backgroundColor,
                          final boolean logFPS,
                          final boolean logDownloaderStatistics,
                          final GInitializationTask initializationTask,
                          final boolean autoDeleteInitializationTask,
                          final ArrayList<PeriodicalTask> periodicalTasks,
                          final WidgetUserData userData,
                          final SceneLighting sceneLighting,
                          final InitialCameraPositionProvider initialCameraPositionProvider) {


      _g3mWidget = G3MWidget.create(//
               _gl, //
               storage, //
               downloader, //
               threadUtils, //
               cameraActivityListener, //
               planet, //
               cameraConstraints, //
               cameraRenderer, //
               mainRenderer, //
               busyRenderer, //
               errorRenderer, //
               backgroundColor, //
               logFPS, //
               logDownloaderStatistics, //
               initializationTask, //
               autoDeleteInitializationTask, //
               periodicalTasks, //
               createGPUProgramManager(), //
               sceneLighting, //
               initialCameraPositionProvider);

      _g3mWidget.setUserData(userData);

      startWidget();
   }


   public void startWidget() {
      if (_g3mWidget != null) {
         //         _shaderProgram = new ShaderProgram(_g3mWidget.getGL());
         //         if (_shaderProgram.loadShaders(_vertexShader, _fragmentShader) == false) {
         //            ILogger.instance().logInfo("Failed to load shaders");
         //         }

         _motionEventProcessor = new MotionEventProcessor(_g3mWidget, _canvas.getCanvasElement());
         jsAddResizeHandler(_canvas.getCanvasElement());

         /*      if (_program != null) {
                  _widget.getGL().useProgram(_program);
               }
               else {
                  throw new RuntimeException("PROGRAM INVALID");
               }*/

         jsStartRenderLoop();
      }
   }


   private native void jsStartRenderLoop() /*-{
		//		debugger;

		$wnd.g3mTick();
   }-*/;


   private void renderG3MWidget() {
      //USING PROGRAM
      //      if (_program != null) {
      //jsGLInit();
      //      _g3mWidget.getGL().useProgram(_shaderProgram);
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


   public void setCameraHeading(final Angle angle) {
      getG3MWidget().setCameraHeading(angle);
   }


   public void setCameraPitch(final Angle angle) {
      getG3MWidget().setCameraPitch(angle);
   }


   public void stopCameraAnimation() {
      getG3MWidget().stopCameraAnimation();
   }


   public G3MContext getG3MContext() {
      return getG3MWidget().getG3MContext();
   }


   public GL getGL() {
      return _gl;
   }
}
