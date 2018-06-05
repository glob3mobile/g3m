package org.eifer.eiferapp.g3mutils;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.SGShape;
import org.glob3.mobile.generated.ShapeLoadListener;

/**
 * Created by chano on 5/6/18.
 */

public class TPKLoadListener implements ShapeLoadListener {

    @Override
    public void onAfterAddShape(SGShape shape) {
        shape.setTranslation(55,0, -90);
    }

    @Override
    public void onBeforeAddShape(SGShape shape) {
        shape.setPitch(Angle.fromDegrees(90));
    }

    @Override
    public void dispose() {

    }
}
