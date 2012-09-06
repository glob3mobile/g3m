

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.DummyRenderer;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.SimplePlanetRenderer;
import org.glob3.mobile.generated.TilesRenderParameters;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.UserData;

import com.google.gwt.canvas.client.Canvas;
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

   G3MWidget                          _widget;


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
      sinkEvents(Event.MOUSEEVENTS | Event.ONMOUSEWHEEL | Event.ONCONTEXTMENU | Event.KEYEVENTS);

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
                           final TilesRendererParameters parameters,
                           final ArrayList<Renderer> renderers,
                           final UserData userData) {

   }


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

      final INativeGL gl = new GL_WebGL();

      //      final TexturesHandler texturesHandler = new TexturesHandler();

      //      _widget = G3MWidget.create(factory, logger, gl, texturesHandler, Planet.createEarth(), comp,
      //               _canvas.getCoordinateSpaceWidth(), _canvas.getCoordinateSpaceHeight(),
      //               Color.fromRGB((float) 0.0, (float) 0.1, (float) 0.2, (float) 1.0), true);

      //CALLING widget.render()
      startRender(this);
   }


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
               _widget.onTouchEvent(te);
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
      _widget.render();
   }


   private native void startRender(G3MWidget_WebGL instance) /*-{
		var tick = function() {
			$wnd.requestAnimFrame(tick);
			$entry(instance.@org.glob3.mobile.specific.G3MWidget_WebGL::renderWidget()());
		};
		tick();
   }-*/;
}
