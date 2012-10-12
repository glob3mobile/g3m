

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.CPUTextureBuilder;
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
import org.glob3.mobile.generated.IGLProgramId;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.MultiLayerTileTexturizer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.SingleImageTileTexturizer;
import org.glob3.mobile.generated.TextureBuilder;
import org.glob3.mobile.generated.TexturesHandler;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileTexturizer;
import org.glob3.mobile.generated.TilesRenderParameters;
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

   private final FlowPanel               _panel                = new FlowPanel();
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
   private IFactory                      _factory;


   public G3MWidget_WebGL(final int delayMillis,
                          final String proxy) {
      initWidget(_panel);

      // downloader
      _delayMillis = delayMillis;
      _proxy = proxy;

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


   protected void onSizeChanged(final int w,
                                final int h) {
      _width = w;
      _height = h;
      _panel.setPixelSize(w, h);
      setPixelSize(w, h);
      _canvas.setCoordinateSpaceWidth(w);
      _canvas.setCoordinateSpaceHeight(h);
      if (_widget != null) {
         _widget.onResizeViewportEvent(w, h);
         jsOnResizeViewport(w, h);
      }
   }


   @Override
   public void onBrowserEvent(final Event event) {
      _canvas.setFocus(true);

      if (_motionEventProcessor != null) {
         _motionEventProcessor.processEvent(event);
      }

      super.onBrowserEvent(event);
   }


   //   public G3MWidget getG3MWidget() {
   //      if (_widget == null) {
   //         initWidgetPrivate(_cameraConstraints, _layerSet, _renderers, _userData);
   //      }
   //
   //      return _widget;
   //   }


   //   public void initWidget(final ArrayList<ICameraConstrainer> cameraConstraints,
   //                          final LayerSet layerSet,
   //                          final ArrayList<Renderer> renderers,
   //                          final UserData userData) {
   //      _cameraConstraints = cameraConstraints;
   //      _layerSet = layerSet;
   //      _renderers = renderers;
   //      _userData = userData;
   //
   //      initWidgetPrivate(cameraConstraints, layerSet, renderers, userData);
   //   }


   //   private void initWidgetPrivate(final ArrayList<ICameraConstrainer> cameraConstraints,
   //                                  final LayerSet layerSet,
   //                                  final ArrayList<Renderer> renderers,
   //                                  final UserData userData) {
   //      final CameraRenderer cameraRenderer = new CameraRenderer();
   //
   //      final boolean useInertia = true;
   //      cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
   //
   //      final boolean processRotataion = true;
   //      final boolean processZoom = true;
   //      cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotataion, processZoom));
   //      cameraRenderer.addHandler(new CameraRotationHandler());
   //      cameraRenderer.addHandler(new CameraDoubleTapHandler());
   //
   //      final boolean renderDebug = true;
   //      final boolean useTilesSplitBudget = true;
   //      final boolean forceTopLevelTilesRenderOnStart = true;
   //
   //      final TilesRenderParameters parameters = TilesRenderParameters.createDefault(renderDebug, useTilesSplitBudget,
   //               forceTopLevelTilesRenderOnStart);
   //
   //      initWidget(cameraRenderer, cameraConstraints, layerSet, parameters, renderers, userData);
   //   }


   //   private void initWidget(final CameraRenderer cameraRenderer,
   //                           final ArrayList<ICameraConstrainer> cameraConstraints,
   //                           final LayerSet layerSet,
   //                           final TilesRenderParameters parameters,
   //                           final ArrayList<Renderer> renderers,
   //                           final UserData userData) {
   //
   //      final IFactory factory = new Factory_WebGL();
   //      final ILogger logger = new Logger_WebGL(LogLevel.InfoLevel);
   //      final IStorage storage = new IndexedDBStorage_WebGL();
   //      final IDownloader downloader = new Downloader_WebGL(8, _delayMillis, _proxy);
   //      final IStringUtils stringUtils = new StringUtils_WebGL();
   //      // TODO add delayMillis to G3MWidget constructor
   //      final IThreadUtils threadUtils = new ThreadUtils_WebGL(this, _delayMillis);
   //
   //      _webGLContext = jsGetWebGLContext();
   //      if (_webGLContext == null) {
   //         throw new RuntimeException("webGLContext null");
   //      }
   //
   //      //CREATING SHADERS PROGRAM
   //      _program = new Shaders_WebGL(_webGLContext).createProgram();
   //
   //      final NativeGL_WebGL nGL = new NativeGL_WebGL(_webGLContext);
   //      final GL gl = new GL(nGL);
   //
   //      final CompositeRenderer composite = new CompositeRenderer();
   //      composite.addRenderer(cameraRenderer);
   //
   //      if ((layerSet != null) && (layerSet.size() > 0)) {
   //
   //         TileTexturizer texturizer = new MultiLayerTileTexturizer(layerSet);
   //
   //         //         if (true) {
   //         texturizer = new MultiLayerTileTexturizer(layerSet);
   //         //         }
   //         //         else {
   //         //SINGLE IMAGE
   //         //         final IImage singleWorldImage = factory.createImageFromFileName("world.jpg");
   //         //         texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage, false);
   //         //         }
   //
   //
   //         final boolean showStatistics = false;
   //
   //         final TileRenderer tr = new TileRenderer(new EllipsoidalTileTessellator(parameters._tileResolution, true), texturizer,
   //                  parameters, showStatistics);
   //
   //         composite.addRenderer(tr);
   //      }
   //
   //      for (int i = 0; i < renderers.size(); i++) {
   //         composite.addRenderer(renderers.get(i));
   //      }
   //
   //
   //      final TextureBuilder textureBuilder = new CPUTextureBuilder();
   //      final TexturesHandler texturesHandler = new TexturesHandler(gl, factory, false);
   //
   //      final Planet planet = Planet.createEarth();
   //
   //      final org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();
   //
   //      final EffectsScheduler scheduler = new EffectsScheduler();
   //
   //      final FrameTasksExecutor frameTasksExecutor = new FrameTasksExecutor();
   //
   //      final IStringBuilder stringBuilder = new StringBuilder_WebGL();
   //
   //      final IMathUtils mathUtils = new MathUtils_WebGL();
   //
   //      _widget = G3MWidget.create(frameTasksExecutor, factory, stringUtils, threadUtils, stringBuilder, mathUtils, logger, gl,
   //               texturesHandler, textureBuilder, downloader, planet, cameraConstraints, composite, busyRenderer, scheduler,
   //               _width, _height, Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1), true, false);
   //
   //      _widget.setUserData(userData);
   //
   //
   //      _motionEventProcessor = new MotionEventProcessor(_widget);
   //
   //      //CALLING widget.render()
   //      startRender(this);
   //   }


   //   //TODO INIT MOVE TO COMMON
   //   private native void jsGLInit(JavaScriptObject gl) /*-{
   //		var error = gl.getError()
   //		if (error != 0) {
   //			debugger;
   //			console.error("jsGLInit error=" + error);
   //		}
   //		gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);
   //		gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
   //   }-*/;
   //
   //
   //   private void renderWidget() {
   //      //USING PROGRAM
   //      if (_program != null) {
   //         jsGLInit(_webGLContext);
   //         _widget.getGL().useProgram(_program);
   //         _widget.render();
   //      }
   //      else {
   //         throw new RuntimeException("PROGRAM INVALID");
   //      }
   //   }
   //
   //
   //   private native void startRender(G3MWidget_WebGL instance) /*-{
   //		var tick = function() {
   //			//TODO CHECK DONT RUN
   //			$wnd.g3mRequestAnimFrame(tick);
   //			$entry(instance.@org.glob3.mobile.specific.G3MWidget_WebGL::renderWidget()());
   //		};
   //		tick();
   //   }-*/;


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


   // TODO TEMP HACK TO PRELOAD IMAGES

   public void initWidget(final ArrayList<ICameraConstrainer> cameraConstraints,
                          final LayerSet layerSet,
                          final ArrayList<Renderer> renderers,
                          final UserData userData,
                          final ArrayList<String> images) {
      jsDefineG3MBrowserObjects();

      _cameraConstraints = cameraConstraints;
      _layerSet = layerSet;
      _renderers = renderers;
      _userData = userData;
      _imagesToPreload = images;

      _factory = new Factory_WebGL();

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
               forceTopLevelTilesRenderOnStart);

      // final TilesRenderParameters parameters = TilesRenderParameters.createSingleSector(renderDebug, useTilesSplitBudget, forceTopLevelTilesRenderOnStart);

      final ILogger logger = new Logger_WebGL(LogLevel.InfoLevel);
      //      final IStorage storage = new IndexedDBStorage_WebGL();
      final IDownloader downloader = new Downloader_WebGL(8, _delayMillis, _proxy);
      final IStringUtils stringUtils = new StringUtils_WebGL();
      final IThreadUtils threadUtils = new ThreadUtils_WebGL(this, _delayMillis);

      _webGLContext = jsGetWebGLContext();
      if (_webGLContext == null) {
         throw new RuntimeException("webGLContext null");
      }

      //CREATING SHADERS PROGRAM
      _program = new Shaders_WebGL(_webGLContext).createProgram();

      final NativeGL_WebGL nGL = new NativeGL_WebGL(_webGLContext);
      final GL gl = new GL(nGL);

      final CompositeRenderer composite = new CompositeRenderer();
      composite.addRenderer(cameraRenderer);

      TileTexturizer texturizer = null;

      if ((_layerSet != null) && (_layerSet.size() > 0)) {
         texturizer = new MultiLayerTileTexturizer(_layerSet);
      }
      else {
         //SINGLE IMAGE
         final IImage singleWorldImage = _factory.createImageFromFileName("../images/world.jpg");
         if (singleWorldImage != null) {
            texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage, false);
         }
      }

      if (texturizer != null) {
         final boolean showStatistics = false;

         final TileRenderer tr = new TileRenderer(new EllipsoidalTileTessellator(parameters._tileResolution, true), texturizer,
                  parameters, showStatistics);

         composite.addRenderer(tr);

         for (int i = 0; i < _renderers.size(); i++) {
            composite.addRenderer(_renderers.get(i));
         }
      }

      final TextureBuilder textureBuilder = new CPUTextureBuilder();
      final TexturesHandler texturesHandler = new TexturesHandler(gl, false);

      final Planet planet = Planet.createEarth();

      final org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();

      final EffectsScheduler scheduler = new EffectsScheduler();

      final FrameTasksExecutor frameTasksExecutor = new FrameTasksExecutor();

      final IStringBuilder stringBuilder = new StringBuilder_WebGL();

      final IMathUtils mathUtils = new MathUtils_WebGL();

      final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1);
      final boolean logFPS = false;
      final boolean logDownloaderStatistics = false;

      // TODO: implements JSONParser_WebGL
      final IJSONParser jsonParser = null;
      _widget = G3MWidget.create( //
               frameTasksExecutor, //
               _factory, //
               stringUtils, //
               threadUtils, //
               stringBuilder, //
               mathUtils, //
               jsonParser, //
               logger, //
               gl, //
               texturesHandler, //
               textureBuilder, //
               downloader, //
               planet, //
               _cameraConstraints, //
               composite, //
               busyRenderer, //
               scheduler, //
               _width, //
               _height, //
               backgroundColor, //
               logFPS, //
               logDownloaderStatistics);

      _widget.setUserData(_userData);

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
      ((Factory_WebGL) _factory).storeDownloadedImage(url, imgJS);
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


   //   private native void jsGLInit() /*-{
   //		//    debugger;
   //
   //		var webGLContext = this.@org.glob3.mobile.specific.G3MWidget_WebGL::_webGLContext;
   //
   //		//		var error = 0;
   //		//		try {
   //		//			error = webGLContext.getError();
   //		//		} catch (e) {
   //		//			console.log("jsGLInit catch " + e);
   //		//		}
   //		//		if (error != 0) {
   //		//			//                        debugger;
   //		//			console.error("jsGLInit error=" + error);
   //		//		}
   //
   //		//    debugger;
   //		var viewportWidth = webGLContext.viewportWidth;
   //		var viewportHeight = webGLContext.viewportHeight;
   //		var COLOR_BUFFER_BIT = webGLContext.COLOR_BUFFER_BIT;
   //		var DEPTH_BUFFER_BIT = webGLContext.DEPTH_BUFFER_BIT;
   //
   //		webGLContext.viewport(0, 0, viewportWidth, viewportHeight);
   //		webGLContext.clear(COLOR_BUFFER_BIT | DEPTH_BUFFER_BIT);
   //   }-*/;


   private native void jsOnResizeViewport(final int width,
                                          final int height) /*-{
		var webGLContext = this.@org.glob3.mobile.specific.G3MWidget_WebGL::_webGLContext;

		webGLContext.viewport(0, 0, width, height);
		webGLContext.clear(webGLContext.COLOR_BUFFER_BIT
				| webGLContext.DEPTH_BUFFER_BIT);
   }-*/;
}
