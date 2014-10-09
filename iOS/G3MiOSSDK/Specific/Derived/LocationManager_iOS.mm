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


- (id) initWith: (LocationManager_iOS*) locationManager
{
  self = [super init];
  if (self)
  {
    _locationManager = locationManager;
  }
  
  return self;
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
  NSLog(@"didFailWithError: %@", error);
}


- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
  NSLog(@"didUpdateToLocation: %@", newLocation);
  _currentLocation = newLocation;
  _locationManager->setLocation(_currentLocation.coordinate.latitude, _currentLocation.coordinate.longitude);
  
  
//  if (_currentLocation != NULL) {
//    if (newLocation.coordinate.latitude != _currentLocation.coordinate.latitude ||
//        newLocation.coordinate.longitude != _currentLocation.coordinate.longitude)
//    {
//      _currentLocation = newLocation;
//      _locationManager->setLocation(_currentLocation.coordinate.latitude, _currentLocation.coordinate.longitude);
//    }
//    
//  } else {
//    _currentLocation = newLocation;
//    _locationManager->setLocation(_currentLocation.coordinate.latitude, _currentLocation.coordinate.longitude);
//  }
}

- (CLLocation*) getCurrentLocation
{
  return _currentLocation;
}


@end




LocationManager_iOS::LocationManager_iOS()
{
  //_location = NULL;
  //_listeners = new std::vector<const LocationChangedListener*>();
  _locationManager = [[CLLocationManager alloc] init];
  
  _delegate = [[LocationManagerDelegate alloc] initWith: this];
  
  [_locationManager setDelegate: _delegate];
  
  if ([_locationManager respondsToSelector:@selector(requestAlwaysAuthorization)]) {
    [_locationManager requestAlwaysAuthorization];
  }
}


const std::string LocationManager_iOS::getProvider() const {
  return "GPS";
}

bool const LocationManager_iOS::serviceIsEneabled() const {
  return [CLLocationManager locationServicesEnabled];
}

bool const LocationManager_iOS::isAuthorized() const {
  CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
//  if (status == kCLAuthorizationStatusNotDetermined) {
//    [_locationManager requestAlwaysAuthorization];
//  }
  return (status == kCLAuthorizationStatusAuthorized
          || status == kCLAuthorizationStatusAuthorizedAlways
          || status == kCLAuthorizationStatusAuthorizedWhenInUse);
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
    //[_locationManager startUpdatingLocation];
    [_locationManager stopUpdatingLocation];
  }
}

void LocationManager_iOS::stop() {
  if(serviceIsEneabled() && isAuthorized()) {
    [_locationManager stopUpdatingLocation];
  }
}

void LocationManager_iOS::setLocation(const double latitude,
                                      const double longitude) {
  delete _location;
  _location = NULL;
  _location = new Geodetic2D(Geodetic2D::fromDegrees(latitude, longitude));
  notifyLocationChanged();
}

Geodetic2D* LocationManager_iOS::getLocation() const {
  return _location;
}
