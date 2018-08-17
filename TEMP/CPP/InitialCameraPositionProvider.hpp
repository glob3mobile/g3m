//
//  Geodetic3DProvider.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 12/09/13.
//
//

#ifndef __G3MiOSSDK__InitialCameraPositionProvider__
#define __G3MiOSSDK__InitialCameraPositionProvider__

#include "Geodetic3D.hpp"

class PlanetRenderer;
class Planet;

class InitialCameraPositionProvider{
public:

  virtual ~InitialCameraPositionProvider() {}
  virtual Geodetic3D getCameraPosition(const Planet* planet,
                                       const PlanetRenderer* planetRenderer) const = 0;
};

class SimpleInitialCameraPositionProvider: public InitialCameraPositionProvider{

public:

  Geodetic3D getCameraPosition(const Planet* planet,
                               const PlanetRenderer* planetRenderer) const;
};

#endif
