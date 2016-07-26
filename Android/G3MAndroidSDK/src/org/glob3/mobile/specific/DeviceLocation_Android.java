

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IDeviceLocation;
import org.glob3.mobile.generated.ILogger;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;


public class DeviceLocation_Android
   extends
      IDeviceLocation {

   private static class PositionListener
      implements
         LocationListener {

      private Geodetic3D _position = null;


      @Override
      public void onLocationChanged(final Location location) {
         _position = toGeodetic3D(location);
      }


      private static Geodetic3D toGeodetic3D(final Location loc) {
         return (loc == null) //
                             ? null //
                             : Geodetic3D.fromDegrees(loc.getLatitude(), loc.getLongitude(), loc.getAltitude());
      }


      @Override
      public void onProviderDisabled(final String provider) {
         _position = null;
         ILogger.instance().logInfo("Location Provider " + provider + " has been disabled.");
      }


      @Override
      public void onProviderEnabled(final String provider) {
         ILogger.instance().logInfo("Location Provider " + provider + " has been enabled.");
      }


      @Override
      public void onStatusChanged(final String provider,
                                  final int status,
                                  final Bundle extras) {
         ILogger.instance().logInfo("Location Provider " + provider + " set status " + status);
      }

   }

   private final PositionListener _locationListener = new PositionListener();

   private final Context          _ctx;
   private final long             _minTimeMS;                                // minimum time interval between location updates, in milliseconds
   private final float            _minDistanceMeters;                        // minimum distance between location updates, in meters

   private LocationManager        _locationManager;
   private boolean                _isTracking       = false;


   DeviceLocation_Android(final Context ctx,
                          final long minTimeMS,
                          final float minDistanceMeters) {
      _ctx = ctx;
      _minTimeMS = minTimeMS;
      _minDistanceMeters = minDistanceMeters;
   }


   private static Criteria createTrackingCriteria() {
      final Criteria criteria = new Criteria();
      criteria.setAccuracy(Criteria.ACCURACY_FINE);
      criteria.setBearingRequired(false);
      criteria.setSpeedRequired(false);
      return criteria;
   }


   private static String getBestProviderName(final LocationManager locationManager) {
      final boolean enabledOnly = true;
      return locationManager.getBestProvider(createTrackingCriteria(), enabledOnly);
   }


   @Override
   public boolean startTrackingLocation() {
      _locationManager = (LocationManager) _ctx.getSystemService(Context.LOCATION_SERVICE);
      if (_locationManager != null) {
         final String bestProviderName = getBestProviderName(_locationManager);
         if (bestProviderName != null) {
            _locationManager.requestLocationUpdates( //
                     bestProviderName, //
                     _minTimeMS, //
                     _minDistanceMeters, //
                     _locationListener, //
                     Looper.getMainLooper());
            _isTracking = true;
         }
      }
      return _isTracking;
   }


   @Override
   public void stopTrackingLocation() {
      if (_isTracking) {
         _locationManager.removeUpdates(_locationListener);
         _locationManager = null;
         _isTracking = false;
      }
   }


   @Override
   public boolean isTrackingLocation() {
      return _isTracking;
   }


   @Override
   public Geodetic3D getLocation() {
      if (_isTracking) {
         final Geodetic3D position = _locationListener._position;
         if (position != null) {
            return position;
         }
      }
      return Geodetic3D.nan();
   }


}
