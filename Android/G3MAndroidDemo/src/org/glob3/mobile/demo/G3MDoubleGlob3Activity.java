/**
 *
 */


package org.glob3.mobile.demo;

import org.glob3.mobile.generated.BingMapType;
import org.glob3.mobile.generated.BingMapsLayer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;


/**
 * @author mdelacalle
 *
 */
public class G3MDoubleGlob3Activity
         extends
            Activity {

   private G3MWidget_Android _widgetAndroidUp   = null;
   private G3MWidget_Android _widgetAndroidDown = null;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      //Don't forget to assign the layout and inflate it
      setContentView(R.layout.double_glob3_activity);

      final G3MBuilder_Android g3mBuilderDown = new G3MBuilder_Android(this);
      final LayerSet layerSet = new LayerSet();
      final BingMapsLayer bingMapsAerialLayer = new BingMapsLayer(BingMapType.Aerial(),
               "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc", TimeInterval.fromDays(30));
      bingMapsAerialLayer.setTitle("Bing Aerial");
      bingMapsAerialLayer.setEnable(true);

      final G3MBuilder_Android g3mBuilderUp = new G3MBuilder_Android(this);
      g3mBuilderUp.getPlanetRendererBuilder().setLayerSet(layerSet);
      _widgetAndroidUp = g3mBuilderUp.createWidget();
      final FrameLayout layoutUp = (FrameLayout) findViewById(R.id.glob3up);
      layoutUp.addView(_widgetAndroidUp);


      layerSet.addLayer(bingMapsAerialLayer);


      g3mBuilderDown.getPlanetRendererBuilder().setLayerSet(layerSet);
      _widgetAndroidDown = g3mBuilderDown.createWidget();

      final FrameLayout layoutDown = (FrameLayout) findViewById(R.id.glob3down);
      layoutDown.addView(_widgetAndroidDown);

   }


   @Override
   protected void onResume() {
      super.onResume();
      _widgetAndroidUp.onResume();
      _widgetAndroidDown.onResume();
   }


   @Override
   protected void onPause() {
      if (isFinishing()) {
         _widgetAndroidUp.onDestroy();
         _widgetAndroidDown.onDestroy();
      }
      else {
         _widgetAndroidUp.onPause();
         _widgetAndroidDown.onPause();
      }
      super.onPause();
   }


   @Override
   public void onBackPressed() {
      System.exit(0);
   }

}
