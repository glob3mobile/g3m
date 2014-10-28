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
  virtual void onLocationChanged(Geodetic2D& newLocation) = 0;
#ifdef C_CODE
  virtual ~LocationChangedListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
};


class ILocationManager {
  
private:
  
protected:
  
  std::vector<LocationChangedListener*>* _listeners = new std::vector<LocationChangedListener*>();
  
public:
  
  
  
  virtual ~ILocationManager();
  
  virtual const std::string getProvider() const = 0;
  
  virtual const bool serviceIsEneabled() const = 0;
  
  virtual const bool isAuthorized() const = 0;
  
  virtual void start(long long minTime, double minDistance, const Activity_Type activityType) = 0;
  
  virtual void stop() = 0;
  
  virtual Geodetic2D* getLocation() const = 0;
  
  void notifyLocationChanged();
  
  void addLocationChangedListener(LocationChangedListener* locationChangedListener);
  
  
};

#endif /* defined(__G3MiOSSDK__ILocationManager__) */
