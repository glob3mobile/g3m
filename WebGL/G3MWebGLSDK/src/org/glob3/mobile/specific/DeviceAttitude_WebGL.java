package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.CoordinateSystem;
import org.glob3.mobile.generated.IDeviceAttitude;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.InterfaceOrientation;
import org.glob3.mobile.generated.MutableMatrix44D;
import org.glob3.mobile.generated.Vector3D;

public class DeviceAttitude_WebGL extends IDeviceAttitude {

	InterfaceOrientation _currentIO = null;
	InterfaceOrientation _firstIO = null;

	// JavaScriptObject _deviceOrientationData = null;
	double _beta = Double.NaN;
	double _gamma = Double.NaN;
	double _alpha = Double.NaN;

//	private double _m[] = new double[9]; // JS Base Rotation Matrix

	boolean _isTracking;

	public DeviceAttitude_WebGL() {
		trackInterfaceOrientation(this);
		initInterfaceOrientation(this);
	}

	private static native void trackGyroscope(DeviceAttitude_WebGL devAtt) /*-{
		try {
			$wnd
					.addEventListener(
							'deviceorientation',
							function(event) {
								devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_beta = event.beta;
								devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_gamma = event.gamma;
								devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_alpha = event.alpha;

								console
										.log("TG EVENT B:"
												+ devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_beta
												+ " G:"
												+ devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_gamma
												+ " A:"
												+ devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_alpha);

								// Chrome - Safari points alpha to the first position instead
								// of true north.
								//								try{
								//									if ((navigator.userAgent.indexOf("Chrome") != -1 ) || (navigator.userAgent.indexOf("Safari") != -1 )){
								//										devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_alpha = event.webkitCompassHeading;
								//									}
								//								} catch(err){
								//								}

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

	private void storeInterfaceOrientation(String orientation) {
		if (orientation.equalsIgnoreCase("portrait-primary")) {
			_currentIO = InterfaceOrientation.PORTRAIT;
		} else {
			if (orientation.equalsIgnoreCase("portrait-secondary")) {
				_currentIO = InterfaceOrientation.PORTRAIT_UPSIDEDOWN;
			} else {
				if (orientation.equalsIgnoreCase("landscape-primary")) {
					_currentIO = InterfaceOrientation.LANDSCAPE_RIGHT;
				} else {
					if (orientation.equalsIgnoreCase("landscape-secondary")) {
						_currentIO = InterfaceOrientation.LANDSCAPE_LEFT;
					}
				}
			}
		}

		if (_firstIO == null) {
			_firstIO = _currentIO;
		}

		ILogger.instance().logError(
				"SIO " + orientation + " -> " + _currentIO.toString());

	};
	
	private native void initInterfaceOrientation(DeviceAttitude_WebGL devAtt) /*-{

		try {
			if ($wnd.screen.orientation !== undefined) { //CHROME, SAFARI
				console.log("IO CHROME");
				devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.orientation.type);
			} else {
				if ($wnd.screen.mozOrientation !== undefined) { //MOZILLA
					console.log("IO MOZ");
					devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.mozOrientation);
				}
			}
		} catch (err) {
			console.error("Unable to track Interface Orientation. " + err);
		}

	}-*/;

	private native void trackInterfaceOrientation(DeviceAttitude_WebGL devAtt) /*-{

		try {
			if ($wnd.screen.orientation !== undefined) { //CHROME, SAFARI
				console.log("IO CHROME");
				$wnd.screen.orientation.onchange = function() {
					devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.orientation.type);
				};
			} else {
				if ($wnd.screen.mozOrientation !== undefined) { //MOZILLA
					console.log("IO MOZ");
					$wnd.screen.onmozorientationchange = function(event) {
						event.preventDefault();
						devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.orientation.type);
					}
				}
			}
		} catch (err) {
			console.error("Unable to track Interface Orientation. " + err);
		}

	}-*/;

	@Override
	public void startTrackingDeviceOrientation() {
		trackGyroscope(this);
		_isTracking = true;
	}

	@Override
	public void stopTrackingDeviceOrientation() {
		stopTrackingGyroscope();
		_beta = _gamma = _alpha = Double.NaN;

		_isTracking = false;
	}

	@Override
	public boolean isTracking() {
		return _isTracking;
	}

	private Angle getHeading() {
		switch (_firstIO) {
		case LANDSCAPE_RIGHT:
			return Angle.fromDegrees(_alpha);
		case LANDSCAPE_LEFT:
			return Angle.fromDegrees(_alpha);
		case PORTRAIT:
			return Angle.fromDegrees(_alpha);
		case PORTRAIT_UPSIDEDOWN:
			return Angle.fromDegrees(_alpha);
		default:
			return Angle.fromDegrees(_alpha);	
		} 
	}
	
	private Angle getPitch() {
		switch (_firstIO) {
		case LANDSCAPE_RIGHT:
			return Angle.fromDegrees(_gamma);
		case LANDSCAPE_LEFT:
			return Angle.fromDegrees(_gamma);
		case PORTRAIT:
			return Angle.fromDegrees(_gamma);
		case PORTRAIT_UPSIDEDOWN:
			return Angle.fromDegrees(_gamma);
		default:
			return Angle.fromDegrees(_gamma);	
		} 
	}
	
	private Angle getRoll() {
		switch (_firstIO) {
		case LANDSCAPE_RIGHT:
			return Angle.fromDegrees(-_beta);
		case LANDSCAPE_LEFT:
			return Angle.fromDegrees(-_beta);
		case PORTRAIT:
			return Angle.fromDegrees(-_beta);
		case PORTRAIT_UPSIDEDOWN:
			return Angle.fromDegrees(-_beta);
		default:
			return Angle.fromDegrees(-_beta);	
		} 
	}

	@Override
	public void copyValueOfRotationMatrix(MutableMatrix44D rotationMatrix) {
		if (Double.isNaN(_beta) || Double.isNaN(_gamma) || Double.isNaN(_alpha)
				|| _firstIO == null) {
			rotationMatrix.setValid(false);
			ILogger.instance().logError("Browser attitude data is undefined.");
		} else {
			/*
			 * getBaseRotationMatrix(this);
			 * 
			 * for (int i = 0; i < 9; i++) { if (_m[i] <= -99998d) {
			 * rotationMatrix.setValid(false); return; } }
			 * 
			 * 
			 * rotationMatrix.setValue(_m[0], _m[1], _m[2], 0.0, _m[3], _m[4],
			 * _m[5], 0.0, _m[6], _m[7], _m[8], 0.0, 0.0, 0.0, 0.0, 1.0);
			 */
			

			CoordinateSystem cs = CoordinateSystem.global()
					.applyTaitBryanAngles(getHeading(), getPitch(), getRoll());
			rotationMatrix.copyValue(cs.getRotationMatrix());
		}

	}

	@Override
	public InterfaceOrientation getCurrentInterfaceOrientation() {
		return _currentIO;
	}

}
