package org.glob3.mobile.client;

import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.IElevationDataListener;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Vector2I;

public class MyEDListener implements IElevationDataListener {

	G3MWebGLTestingApplication _demo;
	
	MyEDListener(G3MWebGLTestingApplication demo){
		_demo = demo;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onData(Sector sector, Vector2I extent, ElevationData elevationData) {
		 _demo.cityGMLRenderer.setElevationData(elevationData);
		 _demo.camConstrainer.setED(elevationData);
		 _demo.setElevationData(elevationData);
		 _demo.loadCityModel();
	}

	@Override
	public void onError(Sector sector, Vector2I extent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel(Sector sector, Vector2I extent) {
		// TODO Auto-generated method stub
		
	}

}