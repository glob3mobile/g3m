

package org.glob3.mobile.client;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CityGMLParser;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ColumnLayoutImageBuilder;
import org.glob3.mobile.generated.DeviceAttitudeCameraHandler;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.EllipsoidalPlanet;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.HUDAbsolutePosition;
import org.glob3.mobile.generated.HUDQuadWidget;
import org.glob3.mobile.generated.HUDRelativeSize;
import org.glob3.mobile.generated.HUDRenderer;
import org.glob3.mobile.generated.ICanvas;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LabelImageBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.NonOverlappingMark;
import org.glob3.mobile.generated.NonOverlappingMarksRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.QuadShape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class G3MWebGLTestingApplication
         implements
            EntryPoint {

   private static final String _g3mWidgetHolderId = "g3mWidgetHolder";
   private G3MWidget_WebGL     _g3mWidget         = null;


   private native void runUserPlugin() /*-{
		$wnd.onLoadG3M();
   }-*/;


   @Override
   public void onModuleLoad() {
      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      //      _g3mWidget = createWidget();
      _g3mWidget = createWidgetBuildings();
      //_g3mWidget = createWidgetVR();
      //_g3mWidget = createWidgetPlanetDebug();
      g3mWidgetHolder.add(_g3mWidget);


      // // Buenos Aires, there we go!
      // _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 35000));

      // Canarias
      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(49.0159538369538, 8.39245743376133, 69.1385));
   }


   private static NonOverlappingMark createMark(final Geodetic3D position) {
      final URL markBitmapURL = new URL("/g3m-marker.png");
      final URL anchorBitmapURL = new URL("/anchorWidget.png");

      return new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }


   private static NonOverlappingMark createMark(final String label,
                                                final Geodetic3D position) {
      final URL markBitmapURL = new URL("/g3m-marker.png");
      final URL anchorBitmapURL = new URL("/anchorWidget.png");

      final ColumnLayoutImageBuilder imageBuilderWidget = new ColumnLayoutImageBuilder( //
               new DownloaderImageBuilder(markBitmapURL), //
               new LabelImageBuilder(label, GFont.monospaced()) //
      );

      return new NonOverlappingMark( //
               imageBuilderWidget, //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }

   private class AnimateHUDWidgetsTask
            extends
               GTask {

      LabelImageBuilder _labelBuilder;
      G3MWidget         _widget;


      public AnimateHUDWidgetsTask(final G3MWidget widget,
                                   final LabelImageBuilder labelBuilder) {
         _labelBuilder = labelBuilder;
         _widget = widget;
      }


      @Override
      public void run(final G3MContext context) {
         // TODO Auto-generated method stub
         _labelBuilder.setText("H: " + _widget.getCurrentCamera().getHeading() + "P: " + _widget.getCurrentCamera().getPitch()
                               + "R: " + _widget.getCurrentCamera().getRoll());
      }

   }


   private G3MWidget_WebGL createWidgetVR() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final CameraRenderer cr = new CameraRenderer();
      cr.addHandler(new DeviceAttitudeCameraHandler(true));
      builder.setCameraRenderer(cr);

      final HUDRenderer hudRenderer = new HUDRenderer();
      builder.addRenderer(hudRenderer);
      final LabelImageBuilder labelBuilder = new LabelImageBuilder("glob3", // text
               GFont.monospaced(38), // font
               6, // margin
               Color.yellow(), // color
               Color.black(), // shadowColor
               3, // shadowBlur
               1, // shadowOffsetX
               -1, // shadowOffsetY
               Color.red(), // backgroundColor
               4, // cornerRadius
               true // mutable
      );

      final HUDQuadWidget label = new HUDQuadWidget(labelBuilder, new HUDAbsolutePosition(10), new HUDAbsolutePosition(10),
               new HUDRelativeSize(1, HUDRelativeSize.Reference.BITMAP_WIDTH), new HUDRelativeSize(1,
                        HUDRelativeSize.Reference.BITMAP_HEIGHT));
      hudRenderer.addWidget(label);

      final G3MWidget_WebGL widget = builder.createWidget();

      builder.addPeriodicalTask(new PeriodicalTask(TimeInterval.fromMilliseconds(200), new AnimateHUDWidgetsTask(
               widget.getG3MWidget(), labelBuilder)));


      return widget;
   }


   private static G3MWidget_WebGL createWidgetBuildings() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final String proxy = null; // "http://galileo.glob3mobile.com/" + "proxy.php?url="
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy));

      final MeshRenderer meshRenderer = new MeshRenderer();
      builder.addRenderer(meshRenderer);
      final MarksRenderer marksRenderer = new MarksRenderer(false);
      builder.addRenderer(marksRenderer);
      final Planet planet = EllipsoidalPlanet.createEarth();
      builder.setPlanet(planet);

      builder.setInitializationTask(new GInitializationTask() {
         @Override
         public void run(final G3MContext context) {

            final String[] cityGMLFiles = { "innenstadt_ost_4326_lod2.gml", "innenstadt_west_4326_lod2.gml",
                     "hagsfeld_4326_lod2.gml", "durlach_4326_lod2_PART_1.gml",
                     //"durlach_4326_lod2_PART_2.gml",
                     // "hohenwettersbach_4326_lod2.gml", "bulach_4326_lod2.gml", "daxlanden_4326_lod2.gml",
                     //"knielingen_4326_lod2_PART_1.gml", "knielingen_4326_lod2_PART_2.gml", "knielingen_4326_lod2_PART_3.gml"
            };

            for (final String s : cityGMLFiles) {
               CityGMLParser.addLOD2MeshAndMarksFromFile(s, context.getDownloader(), context.getPlanet(), meshRenderer,
                        marksRenderer);
            }
         }


         @Override
         public boolean isDone(final G3MContext context) {
            return true;
         }
      });

      //builder.getPlanetRendererBuilder().setRenderDebug(true);
      return builder.createWidget();
   }


   private static G3MWidget_WebGL createWidget() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final String proxy = null; // "http://galileo.glob3mobile.com/" + "proxy.php?url="
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy));


      final NonOverlappingMarksRenderer renderer = new NonOverlappingMarksRenderer(30);
      builder.addRenderer(renderer);

      renderer.addMark(createMark("Label #1", Geodetic3D.fromDegrees(28.131817, -15.440219, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.947345, -13.523105, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.473802, -13.859360, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.467706, -16.251426, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.701819, -17.762003, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.086595, -17.105796, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(27.810709, -17.917639, 0)));


      final boolean testCanvas = false;
      if (testCanvas) {
         final ShapesRenderer shapesRenderer = new ShapesRenderer();
         builder.addRenderer(shapesRenderer);


         builder.setInitializationTask(new GInitializationTask() {
            @Override
            public void run(final G3MContext context) {

               final IImageDownloadListener listener = new IImageDownloadListener() {
                  @Override
                  public void onError(final URL url) {
                     ILogger.instance().logError("ERROR DOWNLOADING CITYGML");
                  }


                  @Override
                  public void onDownload(final URL url,
                                         final IImage image,
                                         final boolean expired) {
                     final ICanvas canvas = context.getFactory().createCanvas();
                     final int width = 1024;
                     final int height = 1024;
                     canvas.initialize(width, height);

                     canvas.setFillColor(Color.fromRGBA(1f, 1f, 0f, 0.5f));
                     canvas.fillRectangle(0, 0, width, height);
                     canvas.setLineWidth(4);
                     canvas.setLineColor(Color.black());
                     canvas.strokeRectangle(0, 0, width, height);

                     final int steps = 8;
                     final float leftStep = (float) width / steps;
                     final float topStep = (float) height / steps;

                     canvas.setLineWidth(1);
                     canvas.setFillColor(Color.fromRGBA(0f, 0f, 0f, 0.75f));
                     for (int i = 1; i < steps; i++) {
                        canvas.fillRectangle(0, topStep * i, width, 1);
                        canvas.fillRectangle(leftStep * i, 0, 1, height);
                     }

                     canvas.setFont(GFont.monospaced());
                     canvas.setFillColor(Color.black());
                     // canvas.fillText("0,0", 0, 0);
                     // canvas.fillText("w,h", width, height);
                     for (int i = 0; i < steps; i++) {
                        canvas.fillText("Hellow World", leftStep * i, topStep * i);
                     }


                     final float width8 = (float) width / 8;
                     final float height8 = (float) height / 8;
                     canvas.drawImage(image, width8, height8); // ok
                     canvas.drawImage(image, width8 * 3, height8, 0.5f); // ok

                     final int imageWidth = image.getWidth();
                     final int imageHeight = image.getHeight();
                     canvas.drawImage(image, width8, height8 * 3, imageWidth * 2, imageHeight * 2); // ok
                     canvas.drawImage(image, width8 * 3, height8 * 3, imageWidth * 2, imageHeight * 2, 0.5f); //ok

                     // ok
                     canvas.drawImage(image, //
                              0, 0, imageWidth, imageHeight, //
                              width8 * 5, height8 * 5, imageWidth * 2, imageHeight * 2);
                     // ok
                     canvas.drawImage(image, //
                              0, 0, imageWidth, imageHeight, //
                              width8 * 7, height8 * 7, imageWidth * 2, imageHeight * 2, //
                              0.5f);


                     canvas.createImage(new IImageListener() {
                        @Override
                        public void imageCreated(final IImage canvasImage) {
                           final QuadShape quad = new QuadShape( //
                                    Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 1000), //
                                    AltitudeMode.ABSOLUTE, //
                                    canvasImage, //
                                    canvasImage.getWidth() * 15.0f, //
                                    canvasImage.getHeight() * 15.0f, //
                                    true);

                           shapesRenderer.addShape(quad);
                        }
                     }, true);

                     canvas.dispose();

                     image.dispose();
                  }


                  @Override
                  public void onCanceledDownload(final URL url,
                                                 final IImage image,
                                                 final boolean expired) {
                  }


                  @Override
                  public void onCancel(final URL url) {
                  }
               };


               context.getDownloader().requestImage( //
                        new URL("/g3m-marker.png"), //
                        1, // priority, //
                        TimeInterval.zero(), //
                        false, //
                        listener, //
                        true);
            }


            @Override
            public boolean isDone(final G3MContext context) {
               return true;
            }
         });
      }


      return builder.createWidget();
   }


   private static G3MWidget_WebGL createWidgetPlanetDebug() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);
      builder.getPlanetRendererBuilder().setRenderDebug(true);

      final String proxy = null; // "http://galileo.glob3mobile.com/" + "proxy.php?url="
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy));


      return builder.createWidget();
   }


}
