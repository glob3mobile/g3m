package org.eifer.eiferapp.g3mutils;

/**
 * Created by chano on 7/11/17.
 */

import android.content.Context;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.eifer.eiferapp.MainActivity;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CompositeElevationDataProvider;
import org.glob3.mobile.generated.Cylinder;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;

public class MyEDCamConstrainer implements ICameraConstrainer {

    ElevationData _ed;
    final Context _ctx;

    boolean _shouldCaptureMotion;
    MeshRenderer _mr;
    ArrayList<Cylinder.CylinderMeshInfo> _cylInfo;
    String _lastText;
    Mark positionMark;
    public Angle markHeading;

    public MyEDCamConstrainer(ElevationData ed,String text,Context ctx){
        _ed = ed;
        _lastText = text;
        _ctx = ctx;
    }

    public void setMark(Mark mark){
        positionMark = mark;
    }

    public void setED(ElevationData ed){
        _ed = ed;
    }

    public void shouldCaptureMotion(boolean capture, MeshRenderer meshRenderer,
                                    ArrayList<Cylinder.CylinderMeshInfo> cylinderInfo){
        _shouldCaptureMotion = capture;
        _mr = meshRenderer;
        _cylInfo = cylinderInfo;
    }

    @Override
    public void dispose() {}

    @Override
    public boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera) {

        if (previousCamera.computeZNear() < 5) {
            //We are using VR
            if (_shouldCaptureMotion && !nextCamera.getGeodeticPosition().isEquals(previousCamera.getGeodeticPosition())) {

                String text = Cylinder.adaptMeshes(_mr, _cylInfo, nextCamera, planet);
                if (!text.contentEquals("") && !text.contentEquals(_lastText)) {
                    Toast.makeText(_ctx, "ProximityAlert: \n" + text, Toast.LENGTH_SHORT).show();
                    _lastText = text;
                }
            }
            return true;
        }
        if (_ed != null) {
            Geodetic3D g = nextCamera.getGeodeticPosition();
            Geodetic2D g2D = g.asGeodetic2D();
            if (_ed.getSector().contains(g2D)) {
                double d = _ed.getElevationAt(g2D);
                final double limit = d + 1.1 * nextCamera.computeZNear();

                if (g._height < limit) {
                    nextCamera.copyFrom(previousCamera, false);
                }
            }
        }
        if (_shouldCaptureMotion && !nextCamera.getGeodeticPosition().isEquals(previousCamera.getGeodeticPosition())) {
            String text = Cylinder.adaptMeshes(_mr, _cylInfo, nextCamera, planet);
            if (!text.contentEquals("") && !text.contentEquals(_lastText)) {
                Toast.makeText(_ctx, "ProximityAlert: \n" + text, Toast.LENGTH_SHORT).show();
                _lastText = text;
            }
        }
        if (positionMark != null){
            Geodetic2D nAsG2D = nextCamera.getGeodeticPosition().asGeodetic2D();
            Geodetic3D mg = new Geodetic3D(nAsG2D,_ed.getElevationAt(nAsG2D));
            positionMark.setPosition(mg);
            markHeading = nextCamera.getHeading();
            updateMessage(mg);
        }
        return false;
    }

    void updateMessage(Geodetic3D mg){
        MainActivity mainActivity = (MainActivity) _ctx;
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(8);
        df.setMaximumFractionDigits(8);
        String message = "";
        try {
            message = "Lat: " + df.format(mg._latitude._degrees);
            message = message + ", lon: "+ df.format(mg._longitude._degrees);
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            message = message + ", hgt: "+ df.format(mg._height);
            message = message + ", heading: "+df.format(markHeading._degrees);
        }
        catch (Exception E){}

        mainActivity.updatePositionFixer(message);
    }

}


