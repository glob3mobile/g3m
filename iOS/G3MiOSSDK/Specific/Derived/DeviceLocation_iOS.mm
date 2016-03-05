//
//  DeviceLocation_iOS.mm
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 25/6/15.
//
//

#include "DeviceLocation_iOS.hpp"

@implementation LocationDelegate

#pragma mark Location Delegate

-(BOOL) startTrackingLocation{
  
  CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
  if (status == kCLAuthorizationStatusRestricted ||
      status == kCLAuthorizationStatusDenied ||
      status == kCLAuthorizationStatusAuthorizedWhenInUse ||
      ![CLLocationManager locationServicesEnabled]){
    _requestingPermission = FALSE;
  }
  
  if (_requestingPermission){
    return FALSE;
  }
  
  if (_locationManager == nil){
    _locationManager = [[CLLocationManager alloc] init];
    _locationManager.delegate = self;
    _locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    _locationManager.distanceFilter = kCLDistanceFilterNone;
  }
  
  if (status == kCLAuthorizationStatusNotDetermined &&
      [_locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
    //Make sure to have the key NSLocationWhenInUseUsageDescription defined before calling this method
    [_locationManager requestWhenInUseAuthorization];
    _requestingPermission = TRUE;
  }
  
  status = [CLLocationManager authorizationStatus];
  if (status == kCLAuthorizationStatusAuthorized ||
      status == kCLAuthorizationStatusAuthorizedAlways ||
      status == kCLAuthorizationStatusAuthorizedWhenInUse){
    
    [_locationManager startUpdatingLocation];
    _requestingPermission = FALSE;
    return true;
  }
  
  return false;
}

-(void) stopTrackingLocation{
  [_locationManager stopUpdatingLocation];
  _lastLocation = nil;
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations{
  _lastLocation = [locations lastObject];
}

- (Geodetic3D) getLocation{
  if (_lastLocation != NULL){
    return Geodetic3D::fromDegrees(_lastLocation.coordinate.latitude, _lastLocation.coordinate.longitude, _lastLocation.altitude);
  }
  return Geodetic3D::nan();
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
  ILogger::instance()->logError("DeviceLocation_iOS produced an error.");
}

@end


DeviceLocation_iOS::DeviceLocation_iOS():
_isTracking(false){
  _delegate = [[LocationDelegate alloc] init];
}

bool DeviceLocation_iOS::startTrackingLocation(){
  _isTracking = [_delegate startTrackingLocation];
  return _isTracking;
}

void DeviceLocation_iOS::stopTrackingLocation(){
  [_delegate stopTrackingLocation];
  _isTracking = false;
}

bool DeviceLocation_iOS::isTrackingLocation(){
  return _isTracking;
}

Geodetic3D  DeviceLocation_iOS::getLocation(){
  return [_delegate getLocation];
}
