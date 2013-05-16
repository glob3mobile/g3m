package org.glob3.mobile.generated; 
//
//  Planet.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//





// I'M SURE THERE ARE BETTER WAYS OF CREATING DIFFERENT PLANETS DEPENDING OF USING ELLIPSOID OR SPHERE 



public class Planet extends Sphere
{
  private final String _name;


  public Planet(String name, Vector3D radii)
  {
     super(radii.x());
     _name = name;
  }

  public static Planet createEarth()
  {
    return new Planet("Earth", new Vector3D(6378137.0, 6378137.0, 6356752.314245));
  }

  public final String getName()
  {
    return _name;
  }

}
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

