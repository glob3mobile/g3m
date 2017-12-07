package org.eifer.eiferapp.g3mutils;

import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ILocationModifier;
import org.glob3.mobile.generated.ILogger;

/**
 * Created by chano on 13/11/17.
 */

public class AltitudeFixerLM implements ILocationModifier{
    private final ElevationData _ed;

    public AltitudeFixerLM(final ElevationData ed){
        _ed = ed;
    }

    @Override
    public Geodetic3D modify(final Geodetic3D location) {
        final double heightDEM = _ed.getElevationAt(location._latitude, location._longitude);
        /*if (location._height < heightDEM + 1.6){
            return Geodetic3D.fromDegrees(location._latitude._degrees,
                    location._longitude._degrees,
                    heightDEM + 1.6);
        }

        if (location._height > heightDEM + 25){
            return Geodetic3D.fromDegrees(location._latitude._degrees,
                    location._longitude._degrees,
                    heightDEM + 25);
        }*/
        return Geodetic3D.fromDegrees(location._latitude._degrees,
                location._longitude._degrees,
                heightDEM + 1.6);
        //return location;
    }

}