//
//  ILocationManager.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 29/09/14.
//
//

#include "ILocationManager.hpp"


void ILocationManager::addLocationChangedListener(const LocationChangedListener* locationChangedListener) {
  _listeners->push_back(locationChangedListener);
}

