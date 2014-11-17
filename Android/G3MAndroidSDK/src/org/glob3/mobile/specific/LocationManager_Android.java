

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Activity_Type;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.ILocationManager;
import org.glob3.mobile.generated.ILogger;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;


public class LocationManager_Android
         extends
            ILocationManager
         implements
            LocationListener {

   private final Context          _context;
   private final LocationManager  _locationManager;
   private LocationProvider       _selectedProvider;
   private final LocationListener _locationListener;

   private Geodetic2D             _location = null;


   //   class LocationListener_Android
   //            implements
   //               LocationListener {
   //      private final LocationListener _locationListener_android;
   //
   //
   //      LocationListener_Android(final LocationListener locationListener) {
   //         _locationListener_android = locationListener;
   //      }
   //
   //
   //      @Override
   //      public void onLocationChanged(final Location location) {
   //         if (_locationListener_android != null) {
   //            _locationListener_android.onLocationChanged(location);
   //         }
   //      }
   //
   //
   //      @Override
   //      public void onStatusChanged(final String provider,
   //                                  final int status,
   //                                  final Bundle extras) {
   //         if (_locationListener_android != null) {
   //            _locationListener_android.onStatusChanged(provider, status, extras);
   //         }
   //      }
   //
   //
   //      @Override
   //      public void onProviderEnabled(final String provider) {
   //         if (_locationListener_android != null) {
   //            _locationListener_android.onProviderEnabled(provider);
   //         }
   //      }
   //
   //
   //      @Override
   //      public void onProviderDisabled(final String provider) {
   //         if (_locationListener_android != null) {
   //            _locationListener_android.onProviderDisabled(provider);
   //         }
   //      }
   //
   //   }


   LocationManager_Android(final Context context,
                           final LocationListener locationListener) {
      _context = context;
      _locationListener = locationListener;

      _locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
      selectProvider();
   }


   private void selectProvider() {
      final Criteria req = new Criteria();
      //    req.setAccuracy(Criteria.ACCURACY_MEDIUM);
      //    req.setAltitudeRequired(false);
      final String bestProvider = _locationManager.getBestProvider(req, true);

      _locationManager.getAllProviders();
      for (int i = 0; i < _locationManager.getAllProviders().size(); i++) {
         ILogger.instance().logInfo("Provider: " + _locationManager.getAllProviders().get(i));
      }

      if ((bestProvider != null) && !bestProvider.equals(LocationManager.PASSIVE_PROVIDER)) {
         ILogger.instance().logInfo("BEST Provider is: " + bestProvider);
         _selectedProvider = _locationManager.getProvider(bestProvider);
      }
      else {
         ILogger.instance().logError("Provider is NULL");
         _selectedProvider = null;
      }
   }


   @Override
   public String getProvider() {
      if (_selectedProvider == null) {
         selectProvider();
      }

      return (_selectedProvider != null) ? _selectedProvider.getName() : null;
   }


   @Override
   public boolean serviceIsEneabled() {
      final String locProvider = getProvider();
      return (locProvider != null) ? _locationManager.isProviderEnabled(locProvider) : false;
   }


   @Override
   public boolean isAuthorized() {
      return serviceIsEneabled();
   }


   @Override
   public void start(final long minTime,
                     final double minDistance,
                     final Activity_Type activityType) {
      _locationManager.requestLocationUpdates(getProvider(), minTime, (float) minDistance, this);
   }


   @Override
   public void stop() {
      _locationManager.removeUpdates(this);
   }


   @Override
   public Geodetic2D getLocation() {
      return _location;
   }


   @Override
   public void onLocationChanged(final Location location) {
      _location = new Geodetic2D(Angle.fromDegrees(location.getLatitude()), Angle.fromDegrees(location.getLongitude()));
      notifyLocationChanged();
      if (_locationListener != null) {
         _locationListener.onLocationChanged(location);
      }


   }


   @Override
   public void onStatusChanged(final String provider,
                               final int status,
                               final Bundle extras) {
      if (_locationListener != null) {
         _locationListener.onStatusChanged(provider, status, extras);
      }
   }


   @Override
   public void onProviderEnabled(final String provider) {
      if (_locationListener != null) {
         _locationListener.onProviderEnabled(provider);
      }
   }


   @Override
   public void onProviderDisabled(final String provider) {
      if (_locationListener != null) {
         _locationListener.onProviderDisabled(provider);
      }
   }

}
