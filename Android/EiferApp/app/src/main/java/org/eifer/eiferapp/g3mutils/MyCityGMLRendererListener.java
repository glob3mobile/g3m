package org.eifer.eiferapp.g3mutils;

/**
 * Created by chano on 7/11/17.
 */

import java.util.ArrayList;

import org.eifer.eiferapp.GlobeFragment;
import org.eifer.eiferapp.MainActivity;
import org.glob3.mobile.generated.CityGMLBuilding;
import org.glob3.mobile.generated.CityGMLRendererListener;

public class MyCityGMLRendererListener extends CityGMLRendererListener{

    GlobeFragment vc;

    public MyCityGMLRendererListener(GlobeFragment vc){
        this.vc = vc;
    }

    @Override
    public void onBuildingsLoaded(ArrayList<CityGMLBuilding> buildings) {

        vc.onCityModelLoaded();
        //Decreasing consumed memory
        for (int i = 0; i < buildings.size(); i++) {
            buildings.get(i).removeSurfaceData();
        }
        // Reference not any longer needed: clean garbage!
        vc = null;
    }
}