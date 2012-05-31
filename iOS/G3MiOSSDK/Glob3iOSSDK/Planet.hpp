//
//  Planet.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Planet_hpp
#define G3MiOSSDK_Planet_hpp

#include "Ellipsoid.hpp"


class Planet : Ellipsoid {
private:
  std::string _name;
  
public:

  static Planet createEarth() {
    return Planet("Earth", Vector3D(6378137.0, 6378137.0, 6356752.314245));
  }
  
  Planet(const sts::string name,
         const Vector3D& radii): Ellipsoid(radii), _name(name) { }

  std::string getName() const {
    return _name;
  }

}

#endif
