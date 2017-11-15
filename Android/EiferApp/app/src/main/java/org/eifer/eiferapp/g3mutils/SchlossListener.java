package org.eifer.eiferapp.g3mutils;

/**
 * Created by chano on 7/11/17.
 */
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.SGShape;
import org.glob3.mobile.generated.ShapeLoadListener;

public class SchlossListener implements ShapeLoadListener {

    @Override
    public void dispose() {}

    @Override
    public void onBeforeAddShape(SGShape shape) {
        shape.setPitch(Angle.fromDegrees(90));
    }

    @Override
    public void onAfterAddShape(SGShape shape) {
        shape.setScale(250);
        //shape->setHeading(Angle.fromDegrees(-4));
    }

}
