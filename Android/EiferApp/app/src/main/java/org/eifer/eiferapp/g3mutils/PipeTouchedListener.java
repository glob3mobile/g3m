package org.eifer.eiferapp.g3mutils;

import org.glob3.mobile.generated.CityGMLBuilding;
import org.glob3.mobile.generated.Cylinder;

/**
 * Created by chano on 22/11/17.
 */

public abstract class PipeTouchedListener {

    public void dispose()
    {
    }

    public abstract void onPipeTouched(Cylinder pipe, Cylinder.CylinderMeshInfo info);
}