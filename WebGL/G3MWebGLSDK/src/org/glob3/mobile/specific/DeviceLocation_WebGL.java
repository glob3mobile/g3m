

package org.glob3.mobile.specific;


import org.glob3.mobile.generated.*;

import com.google.gwt.core.client.*;


public class DeviceLocation_WebGL
                                  extends
                                     IDeviceLocation {

   private double _lat;
   private double _lon;
   private double _altitude;

   private int     _watchId;
   private boolean _isTracking;


   public DeviceLocation_WebGL() {
      reset();
   }


   private void reset() {
      _watchId    = -1;
      _isTracking = false;
      _lat        = Double.NaN;
      _lon        = Double.NaN;
      _altitude   = Double.NaN;
   }


   private native boolean onPositionChanged(JavaScriptObject location)/*-{
		this.@org.glob3.mobile.specific.DeviceLocation_WebGL::_lat = location.coords.latitude;
		this.@org.glob3.mobile.specific.DeviceLocation_WebGL::_lon = location.coords.longitude;

		if (location.coords.altitude == null) {
			console.log("Device altitude is undefined, assuming 0");
			this.@org.glob3.mobile.specific.DeviceLocation_WebGL::_altitude = 0;
		} else {
			this.@org.glob3.mobile.specific.DeviceLocation_WebGL::_altitude = location.coords.altitude;
		}
   }-*/;


   private native boolean onError(JavaScriptObject error)/*-{
		console.log(error);
   }-*/;


   private native boolean startTrackingLocationJS(DeviceLocation_WebGL devLoc)/*-{
		if ("geolocation" in navigator) {
	    	// Request repeated updates.
			devLoc.@org.glob3.mobile.specific.DeviceLocation_WebGL::_watchId = 
	    	navigator.geolocation.watchPosition(devLoc.@org.glob3.mobile.specific.DeviceLocation_WebGL::onPositionChanged(Lcom/google/gwt/core/client/JavaScriptObject;),
	    										devLoc.@org.glob3.mobile.specific.DeviceLocation_WebGL::onError(Lcom/google/gwt/core/client/JavaScriptObject;));
	    
			return true;
		} 
		return false;
	}-*/;


   private native void stopTrackingLocationJS(DeviceLocation_WebGL devLoc)/*-{
		if ("geolocation" in navigator) {
			navigator.geolocation
					.clearWatch(devLoc.@org.glob3.mobile.specific.DeviceLocation_WebGL::_watchId);
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

      reset();
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
