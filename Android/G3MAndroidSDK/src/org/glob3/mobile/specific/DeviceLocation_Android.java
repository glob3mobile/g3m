package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IDeviceLocation;
import org.glob3.mobile.generated.ILogger;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class DeviceLocation_Android extends IDeviceLocation{
	
	private class DeviceLocation_AndroidListener implements LocationListener{
		public Location _lastLocation = null;

		@Override
		public void onLocationChanged(Location location) {
			_lastLocation = location;
		}

		@Override
		public void onProviderDisabled(String provider) {
			_lastLocation = null;
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			ILogger.instance().logInfo("Location Provider " + provider + " set status " + status);
		}
		
	}
	
	DeviceLocation_AndroidListener _netListener = new DeviceLocation_AndroidListener();
	DeviceLocation_AndroidListener _gpsListener = new DeviceLocation_AndroidListener();
	
	LocationManager _locationManager = null;
	Context _ctx = null;
	
	long _minTime = (long) 10000.0; //Seconds between updates (ms.)
	float _minDistance = (float) 1.0; //Min meters between updates
	
	boolean _isTracking = false;
	
	DeviceLocation_Android(Context ctx, long minTime, float minDistance){
		_ctx = ctx;
		_minTime = minTime;
		_minDistance = minDistance;
	}

	@Override
	public boolean startTrackingLocation() {
		_locationManager = (LocationManager) _ctx.getSystemService(Context.LOCATION_SERVICE);
		
		boolean gpsActive = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean netActive = _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if (gpsActive) {
			_locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, _gpsListener, null);
			_locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, _gpsListener);
			_isTracking = true;
		}
		
		if (netActive){
			_locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, _netListener, null);
			_locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, _netListener);
			_isTracking = true;	
		}
		
		if (!netActive && !gpsActive){
			_isTracking = false;
		}
		return _isTracking;
	}

	@Override
	public void stopTrackingLocation() {
		if (_isTracking){
			_locationManager.removeUpdates(_gpsListener);
			_locationManager.removeUpdates(_netListener);
			_isTracking = false;
		}
	}

	@Override
	public boolean isTrackingLocation() {
		return _isTracking && (_gpsListener._lastLocation != null || _netListener._lastLocation != null);
	}

	@Override
	public Geodetic3D getLocation() {
		
		Location l = _gpsListener._lastLocation;
		if (l == null){
			l = _netListener._lastLocation;
		}
		
		if (_isTracking && l != null){
			return Geodetic3D.fromDegrees(l.getLatitude(),
										  l.getLongitude(),
										  l.getAltitude());
		}
		return Geodetic3D.nan();
	}

}
