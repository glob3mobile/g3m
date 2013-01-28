

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.LayerBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


public class G3MSwitchLayerActivity
         extends
            Activity {

   private G3MWidget_Android _widgetAndroid;
   final WMSLayer            _OSMLayer  = LayerBuilder.createBingLayer(true);
   final WMSLayer            _BingLayer = LayerBuilder.createOSMLayer(false);


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      //Don't forget to assign the layout and inflate it
      setContentView(R.layout.bar_glob3_template);

      initializeToolbar();

      final G3MBuilder_Android g3mBuilder = new G3MBuilder_Android(this);

      final LayerSet layers = new LayerSet();
      layers.addLayer(_BingLayer);
      layers.addLayer(_OSMLayer);
      g3mBuilder.getTileRendererBuilder().setLayerSet(layers);

      _widgetAndroid = g3mBuilder.createWidget();
      final LinearLayout layout = (LinearLayout) findViewById(R.id.glob3);
      layout.addView(_widgetAndroid);


      //      super.onCreate(savedInstanceState);
      //      //Don't forget to assign the layout and inflate it
      //      setContentView(R.layout.bar_glob3_template);
      //
      //      initializeToolbar();
      //
      //      final G3MBuilder glob3Builder = new G3MBuilder();
      //
      //      final LayerSet layers = new LayerSet();
      //
      //      _BingLayer.setEnable(true);
      //      _OSMLayer.setEnable(false);
      //
      //      layers.addLayer(_BingLayer);
      //      layers.addLayer(_OSMLayer);
      //
      //      _widgetAndroid = glob3Builder.getCustomLayersGlob3(getApplicationContext(), layers);
      //      final LinearLayout layout = (LinearLayout) findViewById(R.id.glob3);
      //      layout.addView(_widgetAndroid);
   }


   private ToggleButton createToggleButton(final int backgroundResource,
                                           final OnCheckedChangeListener listener,
                                           final boolean checked) {
      final ToggleButton toggleBtn = new ToggleButton(this);
      toggleBtn.setTextOn(" ");
      toggleBtn.setTextOff(" ");
      toggleBtn.setChecked(checked);
      toggleBtn.setBackgroundResource(backgroundResource);
      toggleBtn.setOnCheckedChangeListener(listener);
      toggleBtn.setGravity(Gravity.LEFT);

      return toggleBtn;
   }


   private void initializeToolbar() {
      final LinearLayout toolbarPanel = (LinearLayout) findViewById(R.id.toolbar);


      toolbarPanel.addView(createToggleButton(R.drawable.layer_toggle_br, new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(final CompoundButton buttonView,
                                      final boolean isChecked) {

            if (_BingLayer.isEnable()) {
               _BingLayer.setEnable(false);
               _OSMLayer.setEnable(true);
            }
            else {
               _OSMLayer.setEnable(false);
               _BingLayer.setEnable(true);
            }
         }
      }, true));


      final TextView switchInstructionsText = new TextView(getApplicationContext());
      switchInstructionsText.setText(R.string.switchText);
      switchInstructionsText.setTextSize(15);
      switchInstructionsText.setGravity(Gravity.CENTER_VERTICAL);
      switchInstructionsText.setTextColor(getResources().getColor(R.color.yellowIberia));
      toolbarPanel.addView(switchInstructionsText);

   }
}
