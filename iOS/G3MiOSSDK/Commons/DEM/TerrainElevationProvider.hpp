//
//  TerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#ifndef TerrainElevationProvider_hpp
#define TerrainElevationProvider_hpp

#include "RCObject.hpp"

class RenderState;
class G3MContext;
class Sector;
class Vector2I;
class TerrainElevationListener;


class TerrainElevationProvider : public RCObject {
protected:
  virtual ~TerrainElevationProvider() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:

  virtual RenderState getRenderState() = 0;

  virtual void initialize(const G3MContext* context) = 0;

  virtual void cancel() = 0;

  virtual long long subscribe(const Sector&   sector,
                              const Vector2I& resolution,
                              TerrainElevationListener* listener) = 0;

  virtual void unsubscribe(long long subscriptionID,
                           bool deleteListener) = 0;

};

#endif
