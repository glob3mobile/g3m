package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDeviceAttitude;
import org.glob3.mobile.generated.InterfaceOrientation;
import org.glob3.mobile.generated.MutableMatrix44D;

import com.google.gwt.core.client.JavaScriptObject;

public class DeviceAttitude_WebGL extends IDeviceAttitude {

	InterfaceOrientation _currentIO = InterfaceOrientation.PORTRAIT;

	JavaScriptObject _deviceOrientationData = null;

	// JS Matrix
	private double _m[] = new double[9];

	boolean _isTracking;

	public DeviceAttitude_WebGL() {
		trackInterfaceOrientation();
	}

	private static native void trackGyroscope() /*-{

		try {
			$wnd
					.addEventListener(
							'deviceorientation',
							function(event) {
								this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_deviceOrientationData = event;
								// Chrome - Safari points alpha to the first position instead
								// of true north.
								//if ((navigator.userAgent.indexOf("Chrome") != -1 ) || (navigator.userAgent.indexOf("Safari") != -1 )){
								//$wnd.G3M.deviceOrientationData.alpha = event.webkitCompassHeading;
								//}

							}, false);
		} catch (err) {
		}

	}-*/;

	private static native void stopTrackingGyroscope() /*-{
		try {
			$wnd.addEventListener('deviceorientation', null, false);
		} catch (err) {
		}
	}-*/;

	private void storeInterfaceOrientation(String orientation){
		
		if (orientation.equalsIgnoreCase("portrait-primary")){
			this._currentIO = InterfaceOrientation.PORTRAIT;
		} else{
			if (orientation.equalsIgnoreCase("portrait-secondary")){
				this._currentIO = InterfaceOrientation.PORTRAIT_UPSIDEDOWN;
			} else{
				if (orientation.equalsIgnoreCase("landscape-primary")){
					this._currentIO = InterfaceOrientation.LANDSCAPE_RIGHT;
				} else {
					if (orientation.equalsIgnoreCase("landscape-secondary")){
						this._currentIO = InterfaceOrientation.LANDSCAPE_LEFT;
					}
				}
			}
		}
		
	};

	private native void trackInterfaceOrientation() /*-{

		try {
			$wnd.screen.orientation.onchange = function() {
				if ($wnd.screen.orientation != undefined) {
					this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.orientation.type);
				}
			};
		} catch (err) {
		}

		try {
			$wnd.screen.onmozorientationchange = function(event) {
				event.preventDefault();
				this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.orientation.type);
			}
		} catch (err) {
		}

	}-*/;

	@Override
	public void startTrackingDeviceOrientation() {
		trackGyroscope();
		_isTracking = true;
	}

	@Override
	public void stopTrackingDeviceOrientation() {
		stopTrackingDeviceOrientation();
		_deviceOrientationData = null;
		_isTracking = false;
	}

	@Override
	public boolean isTracking() {
		return _isTracking;
	}

	@Override
	public void copyValueOfRotationMatrix(MutableMatrix44D rotationMatrix) {
		if (_deviceOrientationData == null) {
			rotationMatrix.setValid(false);
		} else {
			getBaseRotationMatrix();

			for (int i = 0; i < 9; i++) {
				if (_m[i] <= -99998d) {
					rotationMatrix.setValid(false);
					return;
				}
			}

			rotationMatrix.setValue(_m[0], _m[1], _m[2], 0.0, _m[3], _m[4],
					_m[5], 0.0, _m[6], _m[7], _m[8], 0.0, 0.0, 0.0, 0.0, 1.0);
		}

	}

	@Override
	public InterfaceOrientation getCurrentInterfaceOrientation() {
		return _currentIO;
	}

	private static native void getBaseRotationMatrix() /*-{
		var data = this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_deviceOrientationData;

		var _beta = data.beta;
		var _gamma = data.gamma;
		var _alpha = data.alpha;
		var degtorad = Math.PI / 180;

		var _y = _beta ? _beta * degtorad : 0; // beta value
		var _z = _gamma ? _gamma * degtorad : 0; // gamma value _gamma ? _gamma * degtorad : 0;
		var _x = _alpha ? _alpha * degtorad : 0; // alpha value

		var cX = Math.cos(_x);
		var cY = Math.cos(_y);
		var cZ = Math.cos(_z);
		var sX = Math.sin(_x);
		var sY = Math.sin(_y);
		var sZ = Math.sin(_z);

		//
		// ZXY-ordered rotation matrix construction.
		//

		this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_m[0] = cZ * cY
				- sZ * sX * sY;
		this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_m[1] = -cX * sZ;
		this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_m[2] = cY * sZ
				* sX + cZ * sY;

		this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_m[3] = cY * sZ
				+ cZ * sX * sY;
		this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_m[4] = cZ * cX;
		this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_m[5] = sZ * sY
				- cZ * cY * sX;

		this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_m[6] = -cX * sY;
		this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_m[7] = sX;
		this.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_m[8] = cX * cY;
	}-*/;

}
