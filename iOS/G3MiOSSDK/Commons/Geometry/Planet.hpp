//
//  Planet.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Planet_hpp
#define G3MiOSSDK_Planet_hpp

#include "PureEllipsoid.hpp"
#include "Sphere.hpp"

#include <string>


// I'M SURE THERE ARE BETTER WAYS OF CREATING DIFFERENT PLANETS DEPENDING OF USING ELLIPSOID OR SPHERE 
int _TESTING_PERFORMANCE_SPHERE_VS_ELLIPSOID;



class Planet : public Sphere {
private:
  const std::string _name;
  
public:
  
  Planet(const std::string& name,
         const Vector3D& radii) :
  Sphere(radii.x()),
  _name(name)
  {
  }
  
  static const Planet* createEarth() {
    return new Planet("Earth", Vector3D(6378137.0, 6378137.0, 6356752.314245));
  }
  
  std::string getName() const {
    return _name;
  }
  
};


/*
class Planet : public PureEllipsoid {
private:
  const std::string _name;

public:

  Planet(const std::string& name,
         const Vector3D& radii) :
  PureEllipsoid(radii),
  _name(name)
  {
  }

  static const Planet* createEarth() {
    return new Planet("Earth", Vector3D(6378137.0, 6378137.0, 6356752.314245));
  }

  std::string getName() const {
    return _name;
  }

};*/

#endif
