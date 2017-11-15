package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CompositeElevationDataProvider;
import org.glob3.mobile.generated.Cylinder;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Vector3D;

public class MyEDCamConstrainer implements ICameraConstrainer {
	
	ElevationData _ed;
	  
	boolean _shouldCaptureMotion;
	MeshRenderer _mr;
	ArrayList<Cylinder.CylinderMeshInfo> _cylInfo;
	String _lastText;
	CompositeElevationDataProvider _edp;
	IThreadUtils _tUtils;
	
	public MyEDCamConstrainer(ElevationData ed, String text){
		_ed = ed;
		_lastText = text;
	}
	
	public void setED(ElevationData ed){
		  _ed = ed;
	}
	
	public void setEDP(CompositeElevationDataProvider edp){
		_edp = edp;
	}
	
	public void setThreadUtils (IThreadUtils tUtils){
		_tUtils = tUtils;
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
		if (previousCamera.computeZNear() < 5){
		    //We are using VR
			 if (_shouldCaptureMotion && !nextCamera.getGeodeticPosition().isEquals(previousCamera.getGeodeticPosition())){
			      
			      String text = Cylinder.adaptMeshes(_mr,_cylInfo,nextCamera,planet);
			      if (!text.contentEquals("") && !text.contentEquals(_lastText)){
			    	  NativeUtils.assignHtmlToElementId("ProximityAlert: \n"+text,"LastEvent");
			    	  _lastText = text;
			      }
			  }
			NativeUtils.assignHtmlToElementId(generateStringForPosition(nextCamera.getGeodeticPosition()),
					"Position");
		    return true;
		  }
		  if (_ed != null){
		    Geodetic3D g = nextCamera.getGeodeticPosition();
		    Geodetic2D g2D = g.asGeodetic2D();
		    if (_ed.getSector().contains(g2D)){
		      double d = _ed.getElevationAt(g2D);
		      final double limit = d + 1.1 * nextCamera.computeZNear();
		      
		      if (g._height < limit){
		        nextCamera.copyFrom(previousCamera, false);
		      }
		    }
		  }
		  if (_shouldCaptureMotion && !nextCamera.getGeodeticPosition().isEquals(previousCamera.getGeodeticPosition())){
		      
		      String text = Cylinder.adaptMeshes(_mr,_cylInfo,nextCamera,planet);
		      if (!text.contentEquals("") && !text.contentEquals(_lastText)){
		    	  
				/*Vector3D v1= planet.toCartesian(nextCamera.getGeodeticCenterOfView());
				Vector3D v2 = new Vector3D(v1._x - 10, v1._y - 10, v1._z);
				Vector3D v3 = new Vector3D(v1._x + 10, v1._y + 10, v1._z);
				Geodetic3D l = planet.toGeodetic3D(v2);
				Geodetic3D u = planet.toGeodetic3D(v3);
				Geodetic2D lower = Geodetic2D.fromDegrees(
							Math.min(l._latitude._degrees, u._latitude._degrees), 
							Math.min(l._longitude._degrees, u._longitude._degrees));
				Geodetic2D upper = Geodetic2D.fromDegrees(
							Math.max(l._latitude._degrees, u._latitude._degrees), 
							Math.max(l._longitude._degrees, u._longitude._degrees));
				final Sector holeSector = new Sector(lower,upper);
				
				_tUtils.invokeInRendererThread(new GTask(){
					
					@Override
					public void run(G3MContext context) {
						// TODO Auto-generated method stub
						_ed.setSector(holeSector);
						_edp.changeProviderSector(0, holeSector);
					}
				}, true);*/

				
		    	NativeUtils.assignHtmlToElementId("ProximityAlert: \n"+text,"LastEvent");
		    	_lastText = text;
		      }
		  }
		NativeUtils.assignHtmlToElementId(generateStringForPosition(nextCamera.getGeodeticPosition()),
					"Position");
		return false;
	}
	
	private String generateStringForPosition(Geodetic3D pos){
		return pos.description();
	}
}

