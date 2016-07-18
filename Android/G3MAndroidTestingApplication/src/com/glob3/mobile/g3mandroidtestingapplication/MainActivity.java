

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CityGMLParser;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ColumnLayoutImageBuilder;
import org.glob3.mobile.generated.DeviceAttitudeCameraHandler;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.EllipsoidalPlanet;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.NonOverlappingMark;
import org.glob3.mobile.generated.NonOverlappingMarkTouchListener;
import org.glob3.mobile.generated.NonOverlappingMarksRenderer;
import org.glob3.mobile.generated.OSMLayer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.QuadShape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class MainActivity
   extends
      Activity {

   private G3MWidget_Android _g3mWidget;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_main);

      // _g3mWidget = createWidget();
      //_g3mWidget = createWidgetVR();
      _g3mWidget = createWidgetBuildings();


      final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);

      placeHolder.addView(_g3mWidget);

      //_g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 1634079));

      // Buenos Aires, there we go!
      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(39.933619, 116.393339, 35000), TimeInterval.fromMinutes(1));
   }


   //   private static NonOverlappingMark createMark(final Geodetic3D position) {
   //      final URL markBitmapURL = new URL("file:///g3m-marker.png");
   //      final URL anchorBitmapURL = new URL("file:///anchorWidget.png");
   //
   //      return new NonOverlappingMark( //
   //               new DownloaderImageBuilder(markBitmapURL), //
   //               new DownloaderImageBuilder(anchorBitmapURL), //
   //               position);
   //   }


   //   private static NonOverlappingMark createMark(final String label,
   //                                                final Geodetic3D position) {
   //      final URL markBitmapURL = new URL("file:///g3m-marker.png");
   //      final URL anchorBitmapURL = new URL("file:///anchorWidget.png");
   //
   //      final ColumnLayoutImageBuilder imageBuilderWidget = new ColumnLayoutImageBuilder( //
   //               new DownloaderImageBuilder(markBitmapURL), //
   //               new LabelImageBuilder(label, GFont.monospaced()) //
   //      );
   //
   //      return new NonOverlappingMark( //
   //               imageBuilderWidget, //
   //               new DownloaderImageBuilder(anchorBitmapURL), //
   //               position);
   //   }


      final ColumnLayoutImageBuilder imageBuilderWidget = new ColumnLayoutImageBuilder( //
               new DownloaderImageBuilder(markBitmapURL), //
               new LabelImageBuilder(label, GFont.monospaced()) //
      );

      return new NonOverlappingMark( //
               imageBuilderWidget, //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }


   private G3MWidget_Android createWidgetVR() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(new OSMLayer(TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final CameraRenderer cr = new CameraRenderer();
      cr.addHandler(new DeviceAttitudeCameraHandler(true));
      builder.setCameraRenderer(cr);

      return builder.createWidget();
   }


   private G3MWidget_Android createWidgetBuildings() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      //      final String proxy = null; // "http://galileo.glob3mobile.com/" + "proxy.php?url="
      //      builder.setDownloader(new Downloader_WebGL( //
      //               8, // maxConcurrentOperationCount
      //               10, // delayMillis
      //               proxy));

      final MeshRenderer meshRenderer = new MeshRenderer();
      builder.addRenderer(meshRenderer);
      final MarksRenderer marksRenderer = new MarksRenderer(false);
      builder.addRenderer(marksRenderer);
      final Planet planet = EllipsoidalPlanet.createEarth();
      builder.setPlanet(planet);

      builder.setInitializationTask(new GInitializationTask() {
         @Override
         public void run(final G3MContext context) {

            final String[] cityGMLFiles = { "file:///innenstadt_ost_4326_lod2.gml"
            //, "file:///innenstadt_west_4326_lod2.gml"
                     //,
            //"file:///hagsfeld_4326_lod2.gml", "file:///durlach_4326_lod2_PART_1.gml",
            //"file:///durlach_4326_lod2_PART_2.gml",
            // "file:///hohenwettersbach_4326_lod2.gml", "file:///bulach_4326_lod2.gml", "file:///daxlanden_4326_lod2.gml",
            //"file:///knielingen_4326_lod2_PART_1.gml", "file:///knielingen_4326_lod2_PART_2.gml", "file:///knielingen_4326_lod2_PART_3.gml"
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


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);


}
