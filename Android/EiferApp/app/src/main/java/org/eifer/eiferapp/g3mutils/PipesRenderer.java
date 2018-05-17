package org.eifer.eiferapp.g3mutils;

import android.util.Log;

import org.eifer.eiferapp.GlobeFragment;
import org.glob3.mobile.generated.Box;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.Cylinder;
import org.glob3.mobile.generated.DefaultRenderer;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MEventContext;
import org.glob3.mobile.generated.G3MRenderContext;
import org.glob3.mobile.generated.GLState;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IndexedMesh;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Sphere;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.Vector2F;
import org.glob3.mobile.generated.Vector3D;

import java.util.ArrayList;

/**
 * Created by chano on 22/11/17.
 */

public class PipesRenderer extends DefaultRenderer {

    private MeshRenderer _meshRenderer;
    private Camera _lastCamera;
    private PipeTouchedListener _touchListener;
    private boolean _holeMode;
    private GlobeFragment gf;

    public PipesRenderer(MeshRenderer meshRenderer, GlobeFragment globeFragment) {
        _meshRenderer = meshRenderer;
        gf = globeFragment;

        //Seguramente vas a tener que insertar el Cylinder y CylinderInfo por algún lado.
        //Están siempre disponibles en PipesModel.
        //Quizá eso te libre de hacerlo en la pantalla principal.
    }

    public void dispose(){
        gf = null;
    }

    public final void setTouchListener(PipeTouchedListener touchListener)
    {
        _touchListener = touchListener;
    }

    public final void setHoleMode (boolean holeMode){
        _holeMode = holeMode;
    }

    public void initialize(G3MContext context)
    {
        super.initialize(context);
        _meshRenderer.initialize(context);
    }

    public void render(G3MRenderContext rc, GLState glState){
        //Do stuff here!
        _lastCamera = rc.getCurrentCamera();

        _meshRenderer.render(rc, glState);

    }

    public final void onResizeViewportEvent(G3MEventContext ec, int width, int height) {}

    public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent){
        //Do stuff also here
        // For now: return false => another guy takes care of this!
        /*if (gf.isHole() && gf.getMapMode() == 3){
            Vector3D cameraDir = _lastCamera.getViewDirection();
            Camera c = new Camera(_lastCamera.getTimestamp());
            c.copyFrom(_lastCamera,true);
            c.translateCamera(cameraDir.normalized().times(25));
            Geodetic3D cameraPos = c.getGeodeticPosition();
            gf.changeHole(cameraPos);
        }*/
        //Forzar Alpha Mode en modo mapa.
        //ArrayList<Mesh> meshes = _meshRenderer.getMeshes();

        if (_lastCamera == null || _touchListener == null )
        {
            return false;
        }

        if (touchEvent.getType() == TouchEventType.LongPress)
        {
            final Vector2F pixel = touchEvent.getTouch(0).getPos();
            final Vector3D ray = _lastCamera.pixel2Ray(pixel);
            final Vector3D origin = _lastCamera.getCartesianPosition();
            double minDis = 1e20;
            Cylinder touchedB = null;
            Cylinder.CylinderMeshInfo touchedInfoB = null;
            for (int i=0; i< PipesModel.cylinders.size(); i++){
                final Sphere s = PipesModel.cylinders.get(i)._sphere;
                if (s != null){
                    final java.util.ArrayList<Double> dists = s.intersectionsDistances(origin._x,origin._y,origin._z,ray._x,ray._y,ray._z);
                    for (int j = 0; j < dists.size(); j++) {
                        if (dists.get(j) < minDis && dists.get(j) > 0) {
                            minDis = dists.get(j);
                            touchedB = PipesModel.cylinders.get(i);
                            touchedInfoB = PipesModel.cylinderInfo.get(i);
                            break;
                        }
                    }
                }
            }
            if (touchedB != null)
            {
                _touchListener.onPipeTouched(touchedB,touchedInfoB);
               // return true;
                return !_holeMode;
            }

        }
        return false;
    }
}
