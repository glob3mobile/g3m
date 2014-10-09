//
//  LocationManager_iOS.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 29/09/14.
//
//

#ifndef __G3MiOSSDK__LocationManager_iOS__
#define __G3MiOSSDK__LocationManager_iOS__

#include "ILocationManager.hpp"

#import <UIKit/UIKit.h>
#import <MobileCoreServices/MobileCoreServices.h>
#import <CoreLocation/CoreLocation.h>

@class CLLocationManager;
@class LocationManagerDelegate;

class LocationManager_iOS : public ILocationManager {
private:
  
  const CLLocationManager* _locationManager;
  
  LocationManagerDelegate* _delegate;
  
  Geodetic2D* _location;

public:
  
  LocationManager_iOS();
  
  const std::string getProvider() const;
  
  const bool serviceIsEneabled() const;
  
  const bool isAuthorized() const;
  
  void start(long long minTime,
             double minDistance,
             const Activity_Type activityType);
  
  void stop();
  
  Geodetic2D* getLocation() const;
  
  void setLocation(const double latitude,
                   const double longitude);

  
  //void notifyLocationChanged(CLLocation* location);
  
  
};

#endif /* defined(__G3MiOSSDK__LocationManager_iOS__) */
