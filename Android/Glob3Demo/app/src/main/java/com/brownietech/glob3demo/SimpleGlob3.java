package com.brownietech.glob3demo;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URLTemplateLayer;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

public class SimpleGlob3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_glob3);


        final G3MBuilder_Android builder = new G3MBuilder_Android(this);

        final LayerSet layerSet = new LayerSet();
        final URLTemplateLayer baseLayer = URLTemplateLayer.newMercator("https://[1234].aerial.maps.cit.api.here.com/maptile/2.1/maptile/newest/satellite.day/{level}/{x}/{y}/256/png8?app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg"
                ,Sector.fullSphere(),false,2,18,TimeInterval.fromDays(30));
        layerSet.addLayer(baseLayer);
        builder.getPlanetRendererBuilder().setLayerSet(layerSet);
        
        G3MWidget_Android g3mWidget = builder.createWidget();

        final ConstraintLayout layout =  findViewById(R.id.g3m);
        layout.addView(g3mWidget);


    }
}
