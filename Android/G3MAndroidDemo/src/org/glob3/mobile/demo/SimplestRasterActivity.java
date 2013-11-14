

package org.glob3.mobile.demo;

import java.util.Arrays;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class SimplestRasterActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;

   private Spinner           _spinnerLayer;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_simplest_raster);
      final LayerSet layerset = SimpleRasterLayerBuilder.createLayerset();

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.setPlanet(Planet.createSphericalEarth());

      builder.getPlanetRendererBuilder().setLayerSet(layerset);

      _g3mWidget = builder.createWidget();

      _spinnerLayer = (Spinner) findViewById(R.id.spinnerLayers);
      final DataSourceAdapter viewAdapter = new DataSourceAdapter(SimplestRasterActivity.this, Arrays.asList("Map Box OSM",
               "Open Street Map", "Map Box Terrain", "Map Box Aerial", "CartoDB Meteorites", "MapQuest Aerial", "MapQuest OSM",
               "WMS Nasa Blue Marble", "ESRI ArcGis Online", "Bing Aerial", "Bing Aerial With Labels"));
      _spinnerLayer.setAdapter(viewAdapter);

      _spinnerLayer.setOnItemSelectedListener(new OnItemSelectedListener() {

         @Override
         public void onItemSelected(final AdapterView<?> arg0,
                                    final View arg1,
                                    final int arg2,
                                    final long arg3) {


            layerset.disableAllLayers();
            final Layer activeLayer = layerset.getLayerByTitle((String) ((TextView) arg1.findViewById(R.id.layername)).getText());


            if (activeLayer.getTitle().equals("CartoDB Meteorites")) {
               layerset.getLayerByTitle("MapQuest OSM").setEnable(true);

            }
            else if (activeLayer.getTitle().equals("ESRI ArcGis Online")) {
               layerset.getLayerByTitle("Map Box Aerial").setEnable(true);
               activeLayer.setEnable(true);

               _g3mWidget.setAnimatedCameraPosition(new Geodetic3D(Angle.fromDegrees(38.6), Angle.fromDegrees(-77.2), 30000),
                        TimeInterval.fromSeconds(5));
            }
            activeLayer.setEnable(true);

         }


         @Override
         public void onNothingSelected(final AdapterView<?> arg0) {
            // TODO Auto-generated method stub

         }
      });


      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);

   }


   @Override
   public void onBackPressed() {
      System.exit(0);
   }

}
