//
//  ILocationManager.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 29/09/14.
//
//

#ifndef __G3MiOSSDK__ILocationManager__
#define __G3MiOSSDK__ILocationManager__

#include <string>
#include <cstdio>
#include <vector>

#include "ILogger.hpp"
#include "Geodetic2D.hpp"



enum Activity_Type {
  OTHER,
  AUTOMOTIVE_NAVIGATION,
  FITNESS,
  OTHER_NAVIGATION
};

class LocationChangedListener {
public:
  virtual void onLocationChanged(const Geodetic2D* newLocation) const = 0;
  
  virtual ~LocationChangedListener() {
    
  }
};


class ILocationManager {
  
private:
  
protected:
  
#warning with this converter add static
  std::vector<const LocationChangedListener*>* _listeners = new std::vector<const LocationChangedListener*>();
  
  
  //#ifdef C_CODE
  //  std::vector<const LocationChangedListener*>* _listeners = new std::vector<const LocationChangedListener*>();
  //#else
  //  protected final java.util.ArrayList<LocationChangedListener> _listeners = java.util.ArrayList<LocationChangedListener>();
  //#endif
public:
  
  
  
  virtual ~ILocationManager();
  
  virtual const std::string getProvider() const = 0;
  
  virtual const bool serviceIsEneabled() const = 0;
  
  virtual const bool isAuthorized() const = 0;
  
  virtual void start(long long minTime, double minDistance, const Activity_Type activityType) = 0;
  
  virtual void stop() = 0;
  
  virtual const Geodetic2D* getLocation() = 0;
  
  const void notifyLocationChanged(const Geodetic2D* newLocation);
  
  void addLocationChangedListener(const LocationChangedListener* locationChangedListener);
  
  
};

#endif /* defined(__G3MiOSSDK__ILocationManager__) */
