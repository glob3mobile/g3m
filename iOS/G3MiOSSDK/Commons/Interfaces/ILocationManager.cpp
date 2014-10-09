//
//  ILocationManager.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 29/09/14.
//
//

#include "ILocationManager.hpp"

ILocationManager::~ILocationManager() {
  const size_t size = _listeners->size();
  for (size_t i = 0; i < size; i++) {
    delete _listeners->at(i);
  }
}

void ILocationManager::notifyLocationChanged() {
  if (_listeners != NULL) {
    const size_t size = _listeners->size();
    for (size_t i = 0; i < size; i++) {
      _listeners->at(i)->onLocationChanged(getLocation());
    }
  }
}
void ILocationManager::addLocationChangedListener(LocationChangedListener* locationChangedListener) {
  _listeners->push_back(locationChangedListener);
}

