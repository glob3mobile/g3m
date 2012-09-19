

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
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.SingleImageTileTexturizer;
import org.glob3.mobile.generated.TextureBuilder;
import org.glob3.mobile.generated.TexturesHandler;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileTexturizer;
import org.glob3.mobile.generated.TilesRenderParameters;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.UserData;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;


public class G3MWidget_WebGL
         extends
            Composite {


   public static final String         canvasId              = "g3m-canvas";
   private final FlowPanel            _panel                = new FlowPanel();
   private Canvas                     _canvas;
   private final MotionEventProcessor _motionEventProcessor = new MotionEventProcessor();
   ArrayList<ICameraConstrainer>      _cameraConstraints    = null;
   LayerSet                           _layerSet             = null;
   ArrayList<Renderer>                _renderers            = null;
   UserData                           _userData             = null;

   private IGLProgramId               _program              = null;

   G3MWidget                          _widget;
   int                                _width;
   int                                _height;
   final int                          _delayMillis          = 10;


   public G3MWidget_WebGL() {
      initWidget(_panel);

      _canvas = Canvas.createIfSupported();

      if (_canvas == null) {
         _panel.add(new Label("Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo."));
         return;
      }

      _canvas.getCanvasElement().setId(canvasId);
      _panel.add(_canvas);

      // Events
      sinkEvents(Event.MOUSEEVENTS | Event.ONMOUSEWHEEL | Event.ONCONTEXTMENU | Event.KEYEVENTS | Event.ONDBLCLICK);

      Window.addResizeHandler(new ResizeHandler() {

         @Override
         public void onResize(final ResizeEvent event) {
            onSizeChanged(event.getWidth(), event.getHeight());
         }
      });

      onSizeChanged(Window.getClientWidth(), Window.getClientHeight());

      jsDefineG3MBrowserObjects();
   }


   protected void onSizeChanged(final int w,
                                final int h) {
      _width = w;
      _height = h;
      _panel.setPixelSize(w, h);
      setPixelSize(w, h);
      _canvas.setCoordinateSpaceWidth(w);
      _canvas.setCoordinateSpaceHeight(h);
   }


   public G3MWidget getG3MWidget() {
      if (_widget == null) {
         initWidgetPrivate(_cameraConstraints, _layerSet, _renderers, _userData);
      }

      return _widget;
   }


   public void initWidget(final ArrayList<ICameraConstrainer> cameraConstraints,
                          final LayerSet layerSet,
                          final ArrayList<Renderer> renderers,
                          final UserData userData) {
      _cameraConstraints = cameraConstraints;
      _layerSet = layerSet;
      _renderers = renderers;
      _userData = userData;

      initWidgetPrivate(cameraConstraints, layerSet, renderers, userData);
   }


   private void initWidgetPrivate(final ArrayList<ICameraConstrainer> cameraConstraints,
                                  final LayerSet layerSet,
                                  final ArrayList<Renderer> renderers,
                                  final UserData userData) {
      final CameraRenderer cameraRenderer = new CameraRenderer();

      final boolean useInertia = true;
      cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));

      final boolean processRotataion = true;
      final boolean processZoom = true;
      cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotataion, processZoom));
      cameraRenderer.addHandler(new CameraRotationHandler());
      cameraRenderer.addHandler(new CameraDoubleTapHandler());

      final boolean renderDebug = true;
      final boolean useTilesSplitBudget = true;
      final boolean forceTopLevelTilesRenderOnStart = true;

      final TilesRenderParameters parameters = TilesRenderParameters.createDefault(renderDebug, useTilesSplitBudget,
               forceTopLevelTilesRenderOnStart);

      initWidget(cameraRenderer, cameraConstraints, layerSet, parameters, renderers, userData);
   }


   private void initWidget(final CameraRenderer cameraRenderer,
                           final ArrayList<ICameraConstrainer> cameraConstraints,
                           final LayerSet layerSet,
                           final TilesRenderParameters parameters,
                           final ArrayList<Renderer> renderers,
                           final UserData userData) {

      final IFactory factory = new Factory_WebGL();
      final ILogger logger = new Logger_WebGL(LogLevel.InfoLevel);
      final IStorage storage = new IndexedDBStorage_WebGL();
      final IDownloader downloader = new Downloader_WebGL(8, _delayMillis);
      final IStringUtils stringUtils = new StringUtils_WebGL();
      // TODO add delayMillis to G3MWidget constructor
      final IThreadUtils threadUtils = new ThreadUtils_WebGL(this, _delayMillis);

      final JavaScriptObject webGLContext = jsGetWebGLContext();
      if (webGLContext == null) {
         throw new RuntimeException("webGLContext null");
      }

      //CREATING SHADERS PROGRAM
      _program = new Shaders_WebGL(webGLContext).createProgram();

      final NativeGL_WebGL nGL = new NativeGL_WebGL(webGLContext);
      final GL gl = new GL(nGL);

      final CompositeRenderer composite = new CompositeRenderer();
      composite.addRenderer(cameraRenderer);

      if ((layerSet != null) && (layerSet.size() > 0)) {

         TileTexturizer texturizer;// = new MultiLayerTileTexturizer(layerSet);

         //         if (true) {
         //            texturizer = new MultiLayerTileTexturizer(layerSet);
         //         }
         //         else {
         //SINGLE IMAGE
         final IImage singleWorldImage = factory.createImageFromFileName("world.jpg");
         texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage, false);
         //         }


         final boolean showStatistics = false;

         final TileRenderer tr = new TileRenderer(new EllipsoidalTileTessellator(parameters._tileResolution, true), texturizer,
                  parameters, showStatistics);

         composite.addRenderer(tr);
      }

      for (int i = 0; i < renderers.size(); i++) {
         composite.addRenderer(renderers.get(i));
      }


      final TextureBuilder textureBuilder = new CPUTextureBuilder();
      final TexturesHandler texturesHandler = new TexturesHandler(gl, factory, false);

      final Planet planet = Planet.createEarth();

      final org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();

      final EffectsScheduler scheduler = new EffectsScheduler();

      final FrameTasksExecutor frameTasksExecutor = new FrameTasksExecutor();

      final IStringBuilder stringBuilder = new StringBuilder_WebGL();

      final IMathUtils mathUtils = new MathUtils_WebGL();

      _widget = G3MWidget.create(frameTasksExecutor, factory, stringUtils, threadUtils, stringBuilder, mathUtils, logger, gl,
               texturesHandler, textureBuilder, downloader, planet, cameraConstraints, composite, busyRenderer, scheduler,
               _width, _height, Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1), true, false);

      _widget.setUserData(userData);


      //CALLING widget.render()
      startRender(this);
   }


   /* old
      public void init() {
         //  Creating factory
         final IFactory factory = new Factory_WebGL();

         // RENDERERS
         final CompositeRenderer comp = new CompositeRenderer();

         // Camera must be first
         final CameraRenderer cr = new CameraRenderer();
         comp.addRenderer(cr);

         if (true) {
            final DummyRenderer dr = new DummyRenderer();
            comp.addRenderer(dr);
         }

         if (true) {
            //final SimplePlanetRenderer spr = new SimplePlanetRenderer("world.jpg");

            final SimplePlanetRenderer spr = new SimplePlanetRenderer("http://www.arkive.org/images/browse/world-map.jpg");
            comp.addRenderer(spr);
         }

         // Logger
         final ILogger logger = new Logger_WebGL(LogLevel.ErrorLevel);

         final INativeGL gl = new NativeGL_WebGL();

         //      final TexturesHandler texturesHandler = new TexturesHandler();

         //      _widget = G3MWidget.create(factory, logger, gl, texturesHandler, Planet.createEarth(), comp,
         //               _canvas.getCoordinateSpaceWidth(), _canvas.getCoordinateSpaceHeight(),
         //               Color.fromRGB((float) 0.0, (float) 0.1, (float) 0.2, (float) 1.0), true);

         //CALLING widget.render()
         startRender(this);
      }
   */

   @Override
   public void onBrowserEvent(final Event event) {
      _canvas.setFocus(true);

      final TouchEvent te = _motionEventProcessor.processEvent(event);

      if (te != null) {
         event.preventDefault();
         Scheduler.get().scheduleDeferred(new Command() {

            @Override
            public void execute() {
               // TODO: remove next function call when tests are finished
               //               writeOnCanvas("execute " + te.getNumTouch() + ": " + te.getType().toString() + " " + te.getTouch(0).getPos().x()
               //                             + " " + te.getTouch(0).getPos().y());
               // TODO: uncomment next line when _widget is properly created
               //               _widget.onTouchEvent(te);
            }
         });
      }

      super.onBrowserEvent(event);
   }


   // testing
   private int line = 30;


   public void writeOnCanvas(final String msg) {
      _canvas.getContext2d().fillText(msg, 25, line);
      line = line + 15;
   }


   private void renderWidget() {
      //USING PROGRAM
      if (_program != null) {
         _widget.getGL().useProgram(_program);
         _widget.render();
      }
      else {
         throw new RuntimeException("PROGRAM INVALID");
      }
   }


   private native void startRender(G3MWidget_WebGL instance) /*-{
		debugger;
		var tick = function() {
			//TODO CHECK DONT RUN
			//$wnd.requestAnimFrame(tick);
			$entry(instance.@org.glob3.mobile.specific.G3MWidget_WebGL::renderWidget()());
		};
		tick();
   }-*/;


   private native void jsDefineG3MBrowserObjects() /*-{
		//		debugger;
		// URL Object
		$wnd.g3mURL = $wnd.URL || $wnd.webkitURL;

		// IndexedDB
		$wnd.g3mIDB = $wnd.indexedDB || $wnd.webkitIndexedDB
				|| $wnd.mozIndexedDB || $wnd.OIndexedDB || $wnd.msIndexedDB;
		$wnd.g3mIDBTransaction = $wnd.IDBTransaction
				|| $wnd.webkitIDBTransaction || $wnd.OIDBTransaction
				|| $wnd.msIDBTransaction;
		$wnd.g3mDBVersion = 1;

   }-*/;


   private native JavaScriptObject jsGetWebGLContext() /*-{
		//		debugger;
		var canvas = $doc
				.getElementById(@org.glob3.mobile.specific.G3MWidget_WebGL::canvasId);

		if (canvas == null) {
			alert("NO CANVAS");
		}

		var context = canvas.getContext("experimental-webgl");
		if (context == null) {
			alert("NO GL CONTEXT");
		}

		return context;
   }-*/;
}
