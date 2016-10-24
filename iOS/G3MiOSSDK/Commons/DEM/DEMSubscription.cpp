//
//  DEMSubscription.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#include "DEMSubscription.hpp"

#include "DEMListener.hpp"

#include "DEMProvider.hpp"


DEMSubscription::DEMSubscription(DEMProvider*    demProvider,
                                 const Sector&   sector,
                                 const Vector2S& extent,
                                 DEMListener*    listener,
                                 const bool      deleteListener) :
_demProvider(demProvider),
_sector(sector),
_extent(extent),
_resolution(sector._deltaLatitude.div(extent._y),
            sector._deltaLongitude.div(extent._x)),
_listener(listener),
_deleteListener(deleteListener)
{
  _demProvider->_retain();
}

DEMSubscription::~DEMSubscription() {
  if (_deleteListener) {
    delete _listener;
  }

  if (_demProvider != NULL) {
    _demProvider->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void DEMSubscription::cancel() {
  if (_demProvider != NULL) {
    _demProvider->unsubscribe(this);
    _demProvider->_release();
    _demProvider = NULL;
  }
}
