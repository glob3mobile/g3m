package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDeviceAttitude;
import org.glob3.mobile.generated.InterfaceOrientation;
import org.glob3.mobile.generated.MutableMatrix44D;

public class DeviceAttitude_Oculus extends IDeviceAttitude {

	@Override
	public void startTrackingDeviceOrientation() {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void stopTrackingDeviceOrientation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTracking() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void copyValueOfRotationMatrix(MutableMatrix44D rotationMatrix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InterfaceOrientation getCurrentInterfaceOrientation() {
		return InterfaceOrientation.LANDSCAPE_LEFT;
	}

}
