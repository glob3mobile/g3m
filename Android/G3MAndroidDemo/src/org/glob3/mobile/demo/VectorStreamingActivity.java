

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapBoo;
import org.glob3.mobile.generated.MapBoo.MBHandler;
import org.glob3.mobile.generated.MapBoo.MBMap;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class VectorStreamingActivity
         extends
            Activity {


   private G3MWidget_Android _g3mWidget;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_vector_streamimg);

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());


      final MBHandler handler = new MBHandler() {

         @Override
         public void onSelectedMap(final MBMap map) {
            // TODO Auto-generated method stub

         }


         @Override
         public void onMapParseError() {
            // TODO Auto-generated method stub

         }


         @Override
         public void onMapDownloadError() {
            // TODO Auto-generated method stub

         }


         @Override
         public void onFeatureTouched(final String datasetName,
                                      final ArrayList<String> info,
                                      final JSONObject properties) {


            String propertiesSt = "";
            int i = 0;
            for (final String infoString : info) {
               propertiesSt = propertiesSt + properties.get(infoString).asString();
               if (i != (info.size() - 1)) {
                  propertiesSt = propertiesSt + ",";
               }
               i++;
            }
            final String p = propertiesSt;
            runOnUiThread(new Runnable() {

               @Override
               public void run() {
                  Toast.makeText(VectorStreamingActivity.this, p, Toast.LENGTH_LONG).show();

               }
            });

         }


         @Override
         public void dispose() {
            // TODO Auto-generated method stub

         }
      };


      final MapBoo mapboo = new MapBoo(builder, new URL("http://mapboo.com/server-mapboo/"), handler, true);
      mapboo.setMapID("55f920310c1bc2b9673c6082");
      _g3mWidget = builder.createWidget();


      //   _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(47.39987642274313d, 8.544022519285223d, 1300),
      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(40d, -3.1889d, 1000000), TimeInterval.fromSeconds(5));
      final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      placeHolder.addView(_g3mWidget);


   }


   private LayerSet createLayerSet() {
      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(new MapBoxLayer("examples.map-cnkhv76j", TimeInterval.fromDays(30), true, 2));


      return layerSet;
   }

}
