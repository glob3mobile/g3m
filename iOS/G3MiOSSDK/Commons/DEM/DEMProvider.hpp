//
//  DEMProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#ifndef DEMProvider_hpp
#define DEMProvider_hpp

#include "RCObject.hpp"

class RenderState;
class G3MContext;
class Sector;
class Vector2S;
class DEMListener;
class DEMSubscription;


class DEMProvider : public RCObject {
protected:
  const double _deltaHeight;

  DEMProvider(const double deltaHeight);

  virtual ~DEMProvider();

public:

  virtual RenderState getRenderState() = 0;

  virtual void initialize(const G3MContext* context) = 0;

  virtual void cancel() = 0;

  virtual DEMSubscription* subscribe(const Sector&   sector,
                                     const Vector2S& extent,
                                     DEMListener*    listener,
                                     const bool      deleteListener) = 0;

  virtual void unsubscribe(DEMSubscription* subscription) = 0;
  
};

#endif
