package org.eifer.eiferapp.g3mutils;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.SGShape;
import org.glob3.mobile.generated.ShapeLoadListener;
/**
 * Created by chano on 12/12/17.
 */

public class MeteoListener implements ShapeLoadListener {
    @Override
    public void dispose() {}

    @Override
    public void onBeforeAddShape(SGShape shape) {

    }

    @Override
    public void onAfterAddShape(SGShape shape) {
        shape.setScale(5);
        shape.setPitch(Angle.fromDegrees(90));
        //shape->setHeading(Angle.fromDegrees(-4));
    }
}

