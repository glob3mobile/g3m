//
//  DEMSubscription.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#ifndef DEMSubscription_hpp
#define DEMSubscription_hpp

#include "Sector.hpp"
#include "Vector2I.hpp"
class DEMListener;


class DEMSubscription {
private:
  static long long _instanceCounter;

  const Sector     _sector;
#ifdef C_CODE
  const Vector2I   _extent;
#endif
#ifdef JAVA_CODE
  private final Vector2I _extent;
#endif
  const Geodetic2D _resolution;

  DEMListener* _listener;
  const bool   _deleteListener;

public:
  const long long _id;

  DEMSubscription(const Sector&   sector,
               const Vector2I& extent,
               DEMListener*    listener,
               const bool      deleteListener);

  ~DEMSubscription();

};

#endif

