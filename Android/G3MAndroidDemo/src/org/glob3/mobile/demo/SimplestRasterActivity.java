

package org.glob3.mobile.demo;

import java.util.Arrays;

import org.glob3.mobile.generated.BingMapType;
import org.glob3.mobile.generated.BingMapsLayer;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.LayerBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.SphericalPlanet;
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

   private static final String BING_MAPS_KEY = "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc";


   private static void addLayer(final LayerSet layerSet,
                                final Layer layer,
                                final String title,
                                final boolean enable) {
      layer.setTitle(title);
      layer.setEnable(enable);
      layerSet.addLayer(layer);
   }


   private static LayerSet createLayerSet() {
      final LayerSet layerSet = new LayerSet();
      addLayer(layerSet, LayerBuilder.createOSMLayer(), "Open Street Map", true);
      addLayer(layerSet, LayerBuilder.createSentinel2CloudlessLayer(), "Sentinel-2 cloudless", false);
      addLayer(layerSet, LayerBuilder.createBlueMarbleLayer(), "Blue Marble", false);
      addLayer(layerSet, new BingMapsLayer(BingMapType.Aerial(), BING_MAPS_KEY, TimeInterval.fromDays(30)), "Bing Aerial", false);
      addLayer(layerSet, new BingMapsLayer(BingMapType.AerialWithLabels(), BING_MAPS_KEY, TimeInterval.fromDays(30)),
               "Bing Aerial With Labels", false);
      return layerSet;
   }


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_simplest_raster);
      final LayerSet layerset = createLayerSet();

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.setPlanet(SphericalPlanet.createEarth());

      builder.getPlanetRendererBuilder().setLayerSet(layerset);

      _g3mWidget = builder.createWidget();

      _spinnerLayer = (Spinner) findViewById(R.id.spinnerLayers);
      final DataSourceAdapter viewAdapter = new DataSourceAdapter(SimplestRasterActivity.this,
               Arrays.asList("Open Street Map", "Sentinel-2 cloudless", "Blue Marble", "Bing Aerial", "Bing Aerial With Labels"));
      _spinnerLayer.setAdapter(viewAdapter);

      _spinnerLayer.setOnItemSelectedListener(new OnItemSelectedListener() {

         @Override
         public void onItemSelected(final AdapterView<?> arg0,
                                    final View arg1,
                                    final int arg2,
                                    final long arg3) {


            layerset.disableAllLayers();
            final Layer activeLayer = layerset.getLayerByTitle((String) ((TextView) arg1.findViewById(R.id.layername)).getText());
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
