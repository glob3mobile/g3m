//
//  DEMSubscription.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#ifndef DEMSubscription_hpp
#define DEMSubscription_hpp

#include "RCObject.hpp"

#include "Sector.hpp"
#include "Vector2I.hpp"
class DEMListener;


class DEMSubscription : public RCObject {
private:
  static long long _instanceCounter;

#ifdef C_CODE
  const Vector2I   _extent;
#endif
#ifdef JAVA_CODE
  private final Vector2I _extent;
#endif

  DEMListener* _listener;
  const bool   _deleteListener;

protected:
  ~DEMSubscription();

public:
  const long long  _id;
  const Sector     _sector;
  const Geodetic2D _resolution;

  DEMSubscription(const Sector&   sector,
                  const Vector2I& extent,
                  DEMListener*    listener,
                  const bool      deleteListener);

};

#endif

