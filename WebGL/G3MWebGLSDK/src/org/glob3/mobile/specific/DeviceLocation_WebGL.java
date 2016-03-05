package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IDeviceLocation;

import com.google.gwt.core.client.JavaScriptObject;

public class DeviceLocation_WebGL extends IDeviceLocation {
	
	static private double _lat = Double.NaN;
	static private double _lon = Double.NaN;
	static private double _altitude = Double.NaN;

	int _watchId = -1;
	
	boolean _isTracking = false;

	private native boolean onPositionChanged(JavaScriptObject location)/*-{
		@org.glob3.mobile.specific.DeviceLocation_WebGL::_lat = location.coords.latitude;
		@org.glob3.mobile.specific.DeviceLocation_WebGL::_lon = location.coords.longitude;
		
		if (location.coords.altitude == null){
			console.log("Device altitude is undefined, assuming 0");
			@org.glob3.mobile.specific.DeviceLocation_WebGL::_altitude = 0;
		} else{
			@org.glob3.mobile.specific.DeviceLocation_WebGL::_altitude = location.coords.altitude;
		}
	}-*/;

	private native boolean onError(JavaScriptObject error)/*-{
		console.log(error);
	}-*/;


	native boolean startTrackingLocationJS(DeviceLocation_WebGL devLoc)/*-{
		if ("geolocation" in navigator) {
	    	// Request repeated updates.
			devLoc.@org.glob3.mobile.specific.DeviceLocation_WebGL::_watchId = 
	    	navigator.geolocation.watchPosition(devLoc.@org.glob3.mobile.specific.DeviceLocation_WebGL::onPositionChanged(Lcom/google/gwt/core/client/JavaScriptObject;),
	    										devLoc.@org.glob3.mobile.specific.DeviceLocation_WebGL::onError(Lcom/google/gwt/core/client/JavaScriptObject;));
	    
			return true;
		} 
		return false;
	}-*/;
	
	native void stopTrackingLocationJS(DeviceLocation_WebGL devLoc)/*-{
		if ("geolocation" in navigator) {
	    	navigator.geolocation.clearWatch(devLoc.@org.glob3.mobile.specific.DeviceLocation_WebGL::_watchId);
	    	devLoc.@org.glob3.mobile.specific.DeviceLocation_WebGL::_watchId = -1;
		} 
	}-*/;

	@Override
	public boolean startTrackingLocation() {
		_isTracking = startTrackingLocationJS(this);
		return _isTracking;
	}

	@Override
	public void stopTrackingLocation() {
		stopTrackingLocationJS(this);
		_isTracking = false;
		_lat = Double.NaN;
		_lon = Double.NaN;
		_altitude = Double.NaN;
	}

	@Override
	public boolean isTrackingLocation() {
		return _isTracking;
	}

	@Override
	public Geodetic3D getLocation() {
		
		return Geodetic3D.fromDegrees(_lat, _lon, _altitude);
	}

}
