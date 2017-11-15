package org.eifer.eiferapp.g3mutils;

import org.eifer.eiferapp.GlobeFragment;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.IElevationDataListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Vector2I;

/**
 * Created by chano on 9/11/17.
 */

public class MyHoleListener implements IElevationDataListener {

    GlobeFragment _demo;

    public MyHoleListener(GlobeFragment demo){
        _demo = demo;
    }

    @Override
    public void dispose() {
        _demo = null;
    }

    @Override
    public void onData(Sector sector, Vector2I extent, ElevationData elevationData) {
        _demo.setHoleElevationData(elevationData);
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
