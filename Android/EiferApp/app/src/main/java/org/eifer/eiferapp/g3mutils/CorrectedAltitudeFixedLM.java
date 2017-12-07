package org.eifer.eiferapp.g3mutils;

import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ILocationModifier;

/**
 * Created by chano on 6/12/17.
 */

public class CorrectedAltitudeFixedLM implements ILocationModifier {
    private final ElevationData _ed;
    private Geodetic2D _initialPosition;
    private Geodetic2D _markPosition;
    double _viewHeight;

    public CorrectedAltitudeFixedLM(ElevationData ed, Geodetic2D markPosition){
        _ed = ed;
        _markPosition = markPosition;
        _viewHeight = 2.0;
    }

    @Override
    public Geodetic3D modify(final Geodetic3D location){
        if (_initialPosition == null){
            _initialPosition = new Geodetic2D(location._latitude, location._longitude);
        }

        // compute what I have walked from initial position
        final Geodetic2D incGeo = location.asGeodetic2D().sub(_initialPosition);

        Geodetic2D fakePos = _markPosition.add(incGeo);
        double h = _ed.getElevationAt(fakePos);
        if (Double.isNaN(h)) {
            h = 0;
        }

        return new Geodetic3D(fakePos, h + _viewHeight);
    }

}

/*
public class AltitudeFixerLM implements ILocationModifier{
    private final ElevationData _ed;

    public AltitudeFixerLM(final ElevationData ed){
        _ed = ed;
    }

    @Override
    public Geodetic3D modify(final Geodetic3D location) {
        final double heightDEM = _ed.getElevationAt(location._latitude, location._longitude);

        return Geodetic3D.fromDegrees(location._latitude._degrees,
                location._longitude._degrees,
                heightDEM + 1.6);
                //return location;
                }

    }
}
 */

/*
public class KarlsruheVirtualWalkLM implements ILocationModifier{

    private final ElevationData _ed;
    private Geodetic2D _initialPosition;

    private final Geodetic2D _karlsruhePos;
    private final double _viewHeight;

    public KarlsruheVirtualWalkLM(final ElevationData ed){
        _ed = ed;
        _initialPosition = null;
        _karlsruhePos = Geodetic2D.fromDegrees(49.011059,8.404109);
        _viewHeight = 2.0;

    }

    @Override
    public Geodetic3D modify(final Geodetic3D location) {
        if (_initialPosition == null){
            _initialPosition = new Geodetic2D(location._latitude, location._longitude);
        }

        // compute what I have walked from initial position
        final Geodetic2D incGeo = location.asGeodetic2D().sub(_initialPosition);

        Geodetic2D fakePos = _karlsruhePos.add(incGeo.times(100));
        final double h = _ed.getElevationAt(fakePos);


        return new Geodetic3D(fakePos, h + _viewHeight);
    }
}
 */
