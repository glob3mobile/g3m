//
//  DEMSubscription.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#include "DEMSubscription.hpp"

#include "DEMListener.hpp"


long long DEMSubscription::_instanceCounter = 0;

DEMSubscription::DEMSubscription(const Sector&   sector,
                                 const Vector2I& extent,
                                 DEMListener*    listener,
                                 const bool      deleteListener) :
_id(++_instanceCounter),
_sector(sector),
_extent(extent),
_resolution(sector._deltaLatitude.div(extent._y),
            sector._deltaLongitude.div(extent._x)),
_listener(listener),
_deleteListener(deleteListener)
{
}

DEMSubscription::~DEMSubscription() {
  if (_deleteListener) {
    delete _listener;
  }
}
