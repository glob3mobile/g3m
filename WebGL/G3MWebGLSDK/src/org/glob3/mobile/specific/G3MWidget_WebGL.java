

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BasicShadersGL2;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GL;
import org.glob3.mobile.generated.GPUProgramFactory;
import org.glob3.mobile.generated.GPUProgramManager;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IDeviceAttitude;
import org.glob3.mobile.generated.IDeviceLocation;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.ITextUtils;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.WidgetUserData;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


public class G3MWidget_WebGL
         extends
            Composite {


   static {
      initSingletons();
   }


   public static void initSingletons() {
      final ILogger logger = new Logger_WebGL(LogLevel.InfoLevel);
      final IFactory factory = new Factory_WebGL();
      final IStringUtils stringUtils = new StringUtils_WebGL();
      final IStringBuilder stringBuilder = new StringBuilder_WebGL(IStringBuilder.DEFAULT_FLOAT_PRECISION);
      final IMathUtils mathUtils = new MathUtils_WebGL();
      final IJSONParser jsonParser = new JSONParser_WebGL();
      final ITextUtils textUtils = new TextUtils_WebGL();
      final IDeviceAttitude deviceAttitude = new DeviceAttitude_WebGL();
      final IDeviceLocation deviceLocation = new DeviceLocation_WebGL();

      G3MWidget.initSingletons( //
               logger, //
               factory, //
               stringUtils, //
               stringBuilder, //
               mathUtils, //
               jsonParser, //
               textUtils, //
               deviceAttitude, //
               deviceLocation);
   }


   private final Canvas         _canvas;
   private JavaScriptObject     _webGLContext;
   private int                  _width;
   private int                  _height;
   private int                  _physicalWidth;
   private int                  _physicalHeight;
   private MotionEventProcessor _motionEventProcessor;
   private GL                   _gl;
   private G3MWidget            _g3mWidget;
   private float                _devicePixelRatio = 1;


   public G3MWidget_WebGL() {
      _canvas = Canvas.createIfSupported();
      if (_canvas == null) {
         initWidget(createUnsupportedMessage("Your browser does not support the HTML5 Canvas."));
         return;
      }
      _canvas.getCanvasElement().setId("_g3m_canvas");

      _webGLContext = jsGetWebGLContext(_canvas.getCanvasElement());
      if (_webGLContext == null) {
         initWidget(createUnsupportedMessage("Your browser does not support WebGL."));
         return;
      }

      initWidget(_canvas);

      final INativeGL nativeGL = new NativeGL_WebGL(_webGLContext);
      _gl = new GL(nativeGL);

      jsDefineG3MBrowserObjects();

      sinkEvents(Event.TOUCHEVENTS | Event.MOUSEEVENTS | Event.ONCONTEXTMENU | Event.ONDBLCLICK | Event.ONMOUSEWHEEL);

      exportJSFunctions();
   }


   private native void exportJSFunctions() /*-{
		var that = this;
		if (!$wnd.G3M) {
			$wnd.G3M = {};
		}

		$wnd.G3M.takeScreenshotAsImage = $entry(function() {
			return that.@org.glob3.mobile.specific.G3MWidget_WebGL::takeScreenshotAsImage()();
		});
		$wnd.G3M.takeScreenshotAsBase64 = $entry(function() {
			return that.@org.glob3.mobile.specific.G3MWidget_WebGL::takeScreenshotAsBase64()();
		});
		$wnd.G3M.getCameraData = $entry(function() {
			return that.@org.glob3.mobile.specific.G3MWidget_WebGL::getCameraData()();
		});
		$wnd.G3M.newGeodetic3D = $entry(function(latitude, longitude, height) {
			return @org.glob3.mobile.specific.G3MWidget_WebGL::newGeodetic3D(DDD)(latitude, longitude, height);
		});
		$wnd.G3M.moveCameraTo = $entry(function(position) {
			that.@org.glob3.mobile.specific.G3MWidget_WebGL::moveCameraTo(Lorg/glob3/mobile/generated/Geodetic3D;)(position);
		});
   }-*/;


   public void moveCameraTo(final Geodetic3D position) {
      _g3mWidget.setAnimatedCameraPosition(TimeInterval.fromSeconds(5), position);
   }


   public static Geodetic3D newGeodetic3D(final double latitude,
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


   private static VerticalPanel createUnsupportedMessage(final String message) {
      final VerticalPanel panel = new VerticalPanel();

      panel.add(new Label(message));
      panel.add(new Label("Please upgrade your browser to get this running."));

      return panel;
   }


   public boolean isWebGLSupported() {
      return ((_canvas != null) && (_webGLContext != null));
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
		var that = this;
		$wnd.g3mWidgetResize = function() {
			if ((jsCanvas.clientWidth != jsCanvas.parentNode.clientWidth)
					|| (jsCanvas.clientHeight != jsCanvas.parentNode.clientHeight)) {
				that.@org.glob3.mobile.specific.G3MWidget_WebGL::onSizeChanged(II)(jsCanvas.parentNode.clientWidth, jsCanvas.parentNode.clientHeight);
			}
		};

		$wnd.g3mWidgetResizeChecker = setInterval($wnd.g3mWidgetResize, 200);
   }-*/;


   private static native float jsGetDevicePixelRatio() /*-{
		return $wnd.devicePixelRatio || 1;
   }-*/;


   public float getDevicePixelRatio() {
      return _devicePixelRatio;
   }


   private void onSizeChanged(final int width,
                              final int height) {
      if ((_width != width) || (_height != height)) {
         _width = width;
         _height = height;
         setPixelSize(_width, _height);

         _devicePixelRatio = jsGetDevicePixelRatio();
         _physicalWidth = Math.round(_width * _devicePixelRatio);
         _physicalHeight = Math.round(_height * _devicePixelRatio);
         _canvas.setCoordinateSpaceWidth(_physicalWidth);
         _canvas.setCoordinateSpaceHeight(_physicalHeight);
      }
   }


   private void renderG3MWidget() {
      _g3mWidget.render(_physicalWidth, _physicalHeight);
   }


   private native void jsDefineG3MBrowserObjects() /*-{
		var that = this;

		// URL Object
		$wnd.g3mURL = $wnd.URL || $wnd.webkitURL;

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
		var context = null;

		if (jsCanvas != null) {
			var contextNames = [ "webgl", "experimental-webgl", "webkit-3d",
					"moz-webgl" ];
			for ( var cn in contextNames) {
				try {
					context = jsCanvas.getContext(contextNames[cn], {
						preserveDrawingBuffer : true,
						alpha : false,
						preferLowPowerToHighPerformance : true,
						antialias : false
					});
				} catch (e) {
				}
				if (context) {
					jsCanvas.addEventListener("webglcontextlost", function(
							event) {
						event.preventDefault();
						$wnd.alert("webglcontextlost");
					}, false);
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


   static private GPUProgramManager createGPUProgramManager() {
      final GPUProgramFactory factory = new BasicShadersGL2();
      return new GPUProgramManager(factory);
   }


   public void startWidget() {
      if (_g3mWidget != null) {
         _motionEventProcessor = new MotionEventProcessor(this, _canvas.getCanvasElement());
         jsAddResizeHandler(_canvas.getCanvasElement());

         jsStartRenderLoop();
      }
   }


   private native void jsStartRenderLoop() /*-{
		$wnd.g3mTick();
   }-*/;


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


   public void cancelCameraAnimation() {
      getG3MWidget().cancelCameraAnimation();
   }


   public G3MContext getG3MContext() {
      return getG3MWidget().getG3MContext();
   }


   public GL getGL() {
      return _gl;
   }


   void onTouchEvent(final TouchEvent event) {
      if (_g3mWidget != null) {
         _g3mWidget.onTouchEvent(event);
      }
   }


}
