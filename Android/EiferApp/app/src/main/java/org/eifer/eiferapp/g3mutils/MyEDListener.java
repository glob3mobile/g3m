package org.eifer.eiferapp.g3mutils;

/**
 * Created by chano on 7/11/17.
 */

import org.eifer.eiferapp.GlobeFragment;
import org.eifer.eiferapp.MainActivity;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.IElevationDataListener;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Vector2I;

public class MyEDListener implements IElevationDataListener {

    GlobeFragment _demo;

    public MyEDListener(GlobeFragment demo){
        _demo = demo;
    }

    @Override
    public void dispose() {
        _demo = null;
    }

    @Override
    public void onData(Sector sector, Vector2I extent, ElevationData elevationData) {
        _demo.cityGMLRenderer.setElevationData(elevationData);
        _demo.camConstrainer.setED(elevationData);
        _demo.setElevationData(elevationData);
        _demo.loadCityModel();

        _demo = null;
    }

    @Override
    public void onError(Sector sector, Vector2I extent) {
        _demo = null;
    }

    @Override
    public void onCancel(Sector sector, Vector2I extent) {
        _demo = null;
    }

}

