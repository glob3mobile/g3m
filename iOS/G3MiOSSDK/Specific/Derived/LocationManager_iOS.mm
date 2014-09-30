//
//  LocationManager_iOS.mm
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 29/09/14.
//
//

#include "LocationManager_iOS.hpp"

@interface LocationManagerDelegate : NSObject<CLLocationManagerDelegate>
{
  LocationManager_iOS*  _locationManager;
  CLLocation *          _currentLocation;
}

- (CLLocation*) getCurrentLocation;

@end

@implementation LocationManagerDelegate
- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
  NSLog(@"didFailWithError: %@", error);
}


- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
  NSLog(@"didUpdateToLocation: %@", newLocation);
  if (_currentLocation != NULL) {
    if (newLocation.coordinate.latitude != _currentLocation.coordinate.latitude ||
        newLocation.coordinate.longitude != _currentLocation.coordinate.longitude)
    {
      _locationManager->notifyLocationChanged(newLocation);
    }
    
    _currentLocation = newLocation;
  }
}

- (CLLocation*) getCurrentLocation
{
  return _currentLocation;
}


@end




LocationManager_iOS::LocationManager_iOS()
{
  _locationManager = [CLLocationManager new];
  _delegate = [[LocationManagerDelegate alloc] init];
  
  [_locationManager setDelegate: _delegate];

}


const std::string LocationManager_iOS::getProvider() const {
  return "GPS";
}

bool const LocationManager_iOS::serviceIsEneabled() const {
  return [CLLocationManager locationServicesEnabled];
}

bool const LocationManager_iOS::isAuthorized() const {
  return ([CLLocationManager authorizationStatus] == kCLAuthorizationStatusNotDetermined ||[CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorized || [CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedAlways);
}

void LocationManager_iOS::start(long long minTime, double minDistance, const Activity_Type activityType) {
  if(serviceIsEneabled() && isAuthorized()) {
  //  _locationManager = [CLLocationManager new];
//    _locationManager.delegate = self;
    
    switch (activityType) {
      case AUTOMOTIVE_NAVIGATION:
        _locationManager.activityType = CLActivityTypeAutomotiveNavigation;
        break;
      case FITNESS:
        _locationManager.activityType = CLActivityTypeFitness;
        break;
      case OTHER_NAVIGATION:
        _locationManager.activityType = CLActivityTypeOtherNavigation;
        break;
      default:
        _locationManager.activityType = CLActivityTypeFitness;
        break;
    }
    
    _locationManager.distanceFilter = minDistance;
    
    if (minDistance <20.0) {
      //TODO: kCLLocationAccuracyBestForNavigation
      _locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    } else if (minDistance < 300.0){
      _locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters;

    } else if (minDistance < 1500.0){
      _locationManager.desiredAccuracy = kCLLocationAccuracyKilometer;
      
    } else {
      _locationManager.desiredAccuracy = kCLLocationAccuracyThreeKilometers;
    }
    
    //TODO minTime
    
    [_locationManager startUpdatingLocation];
  } else {
    [_locationManager startUpdatingLocation];
    //[_locationManager stopUpdatingLocation];
  }
}

void LocationManager_iOS::stop() {
  if(serviceIsEneabled() && isAuthorized()) {
    [_locationManager stopUpdatingLocation];
  }
}

const Geodetic2D* LocationManager_iOS::getLocation() {
  return new Geodetic2D(Geodetic2D::fromDegrees([_delegate getCurrentLocation].coordinate.latitude, [_delegate getCurrentLocation].coordinate.longitude));
}

  void LocationManager_iOS::notifyLocationChanged(CLLocation* location) {
    const size_t s = _listeners->size();
    for( size_t i = 0; i < s; i++){
      _listeners->at(i)->onLocationChanged(Geodetic2D::fromDegrees(location.coordinate.latitude, location.coordinate.longitude));
    }
  }
