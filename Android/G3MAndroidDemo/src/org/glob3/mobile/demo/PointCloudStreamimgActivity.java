

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.DownloadPriority;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.PointCloudsRenderer;
import org.glob3.mobile.generated.PointCloudsRenderer.ColorPolicy;
import org.glob3.mobile.generated.PointCloudsRenderer.PointCloudMetadataListener;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class PointCloudStreamimgActivity
         extends
            Activity {


   private G3MWidget_Android _g3mWidget;
   final Geodetic3D          position = Geodetic3D.fromDegrees(39.133, -77.54821668007953, 100000);
   final Angle               heading  = Angle.zero();
   final Angle               pitch    = Angle.fromDegrees(-24);


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_point_cloud_streamimg);

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());
      builder.addRenderer(createPointCloudsRenderer());


      _g3mWidget = builder.createWidget();
      _g3mWidget.setAnimatedCameraPosition(position, TimeInterval.fromSeconds(5));


      final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      placeHolder.addView(_g3mWidget);

   }


   private static PointCloudsRenderer createPointCloudsRenderer() {
      final float pointSize = 2;
      final float verticalExaggeration = 1;
      final double deltaHeight = 0;

      final PointCloudsRenderer renderer = new PointCloudsRenderer();

      final PointCloudMetadataListener listener = new PointCloudMetadataListener() {

         @Override
         public void onMetadata(final long pointsCount,
                                final Sector sector,
                                final double minHeight,
                                final double maxHeight,
                                final double averageHeight) {
            // TODO Auto-generated method stub

         }
      };

      renderer.addPointCloud( //
               new URL("http://glob3mobile.dyndns.org:8080"), //
               //"Loudoun-VA_fragment_LOD", //
               "Loudoun-VA_simplified2_LOD", //
               DownloadPriority.LOWER, //
               TimeInterval.zero(), //
               false, //
               ColorPolicy.MIN_AVERAGE3_HEIGHT, //
               pointSize, //
               verticalExaggeration, //
               deltaHeight, //
               listener, //
               true);

      return renderer;
   }


   private LayerSet createLayerSet() {
      final LayerSet layerSet = new LayerSet();

      layerSet.addLayer(new MapBoxLayer("examples.map-cnkhv76j", TimeInterval.fromDays(30), true, 2));

      return layerSet;
   }

}
