

package com.glob3.mobile.g3mandroidtestingapplication;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.DebugTileImageProvider;
import org.glob3.mobile.generated.DefaultInfoDisplay;
import org.glob3.mobile.generated.Default_HUDRenderer;
import org.glob3.mobile.generated.DownloadPriority;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.InfoDisplay;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.PointCloudsRenderer;
import org.glob3.mobile.generated.PointCloudsRenderer.PointCloudMetadataListener;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
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


      _g3mWidget = createWidget();

      final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);


      placeHolder.addView(_g3mWidget);


      final Geodetic3D zurichPos = Geodetic3D.fromDegrees(40, -75, 80000);
      _g3mWidget.getG3MWidget().setAnimatedCameraPosition(TimeInterval.fromSeconds(5), zurichPos, Angle.zero(),
               Angle.fromDegrees(-90));

   }


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final PointCloudsRenderer pcr = new PointCloudsRenderer();

      final URL serverURL = new URL("http://glob3mobile.dyndns.org:8080");
      final String cloudName = "Loudoun-VA_simplified2_LOD";
      final long downloadPriority = DownloadPriority.LOWER;
      final TimeInterval timeToCache = TimeInterval.zero();
      final boolean readExpired = false;
      final float pointSize = 2;
      final float verticalExaggeration = 1;
      final PointCloudMetadataListener metadataListener = null;
      final boolean deleteListener = true;

      pcr.addPointCloud( //
               serverURL, cloudName, //
               downloadPriority, timeToCache, readExpired, //
               pointSize, //
               verticalExaggeration, //
               metadataListener, deleteListener);


      builder.addRenderer(pcr);

      final LayerSet layerSet = new LayerSet();
      final boolean forceFirstLevelTilesRenderOnStart = true;

      layerSet.createLayerTilesRenderParameters(forceFirstLevelTilesRenderOnStart, new ArrayList<String>());


      final WMSLayer pnoa = new WMSLayer("PNOA", new URL("http://www.idee.es/wms/PNOA/PNOA", false), WMSServerVersion.WMS_1_1_0,
               Sector.fromDegrees(40.1640143280790858, -5.8564874640814313, 40.3323148480663158, -5.5216079822178570),
               "image/png", "EPSG:4326", "", true, null, TimeInterval.fromDays(0), true, null, 1);


      final WMSLayer blueMarble = new WMSLayer("bmng200405", new URL("http://www.nasa.network.com/wms?", false),
               WMSServerVersion.WMS_1_1_0, //
               Sector.fromDegrees(40.1240143280790858, -5.8964874640814313, 40.3723148480663158, -5.4816079822178570),//
               //               Sector.fromDegrees(0, -90, 45, 0),
               "image/jpeg", "EPSG:4326", "", false, null, //new LevelTileCondition(0, 6),
               //NULL,
               TimeInterval.fromDays(0), true, null, 1);
      layerSet.addLayer(blueMarble);
      layerSet.addLayer(pnoa);

      layerSet.setTileImageProvider(new DebugTileImageProvider());

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);
      builder.getPlanetRendererBuilder().setRenderDebug(true);

      builder.getPlanetRendererBuilder().setDefaultTileBackGroundImage(
               new DownloaderImageBuilder(new URL("http://www.freelogovectors.net/wp-content/uploads/2013/02/sheep-b.png")));

      //  builder.getPlanetRendererBuilder()->setDefaultTileBackGroundImage(new DownloaderImageBuilder(URL("http://192.168.1.127:8080/web/img/tileNotFound.jpg")));
      //  const Sector sector = Sector::fromDegrees(40.1540143280790858, -5.8664874640814313,
      //                                                40.3423148480663158, -5.5116079822178570);
      //
      final Default_HUDRenderer hudRenderer = new Default_HUDRenderer();
      final InfoDisplay infoDisplay = new DefaultInfoDisplay(hudRenderer);
      //infoDisplay->showDisplay();

      builder.setHUDRenderer(hudRenderer);
      builder.setInfoDisplay(infoDisplay);


      // builder.getPlanetRendererBuilder().setRenderDebug(true);
      // builder.getPlanetRendererBuilder().setLogTilesPetitions(true);

      return builder.createWidget();
   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
