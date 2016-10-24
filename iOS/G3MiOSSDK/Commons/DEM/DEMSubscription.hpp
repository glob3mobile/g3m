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
#include "Vector2S.hpp"
class DEMProvider;
class DEMListener;


class DEMSubscription : public RCObject {
private:
  DEMProvider* _demProvider;

#ifdef C_CODE
  const Vector2S   _extent;
#endif
#ifdef JAVA_CODE
  private final Vector2S _extent;
#endif

  DEMListener* _listener;
  const bool   _deleteListener;

protected:
  ~DEMSubscription();

public:
  const Sector     _sector;
  const Geodetic2D _resolution;

  DEMSubscription(DEMProvider*    demProvider,
                  const Sector&   sector,
                  const Vector2S& extent,
                  DEMListener*    listener,
                  const bool      deleteListener);

  void cancel();

};

#endif

