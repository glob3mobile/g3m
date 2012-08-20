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

#include <string>


class Planet : public Ellipsoid {
private:
  std::string _name;
  
public:
  
  Planet(const std::string name,
         const Vector3D& radii) :
  Ellipsoid(radii),
  _name(name)
  {
  }
  
  static const Planet* createEarth() {
    return new Planet("Earth", Vector3D(6378137.0, 6378137.0, 6356752.314245));
  }
  
//  static const Planet* createHugeEarth() {
//    return new Planet("Earth", Vector3D(6378137.0 * 1000, 6378137.0 * 1000, 6356752.314245 * 1000));
//  }

  
  std::string getName() const {
    return _name;
  }
  
};

#endif
