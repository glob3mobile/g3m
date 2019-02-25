package org.glob3.mobile.generated;
//
//  Ray.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//

//
//  Ray.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//




public class Ray
{
  public final Vector3D _origin ;
  public final Vector3D _direction ;

  public Ray(Vector3D origin, Vector3D direction)
  {
     _origin = origin;
     _direction = direction.normalized();
  }

  public void dispose()
  {
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(Ray origin=");
    isb.addString(_origin.description());
    isb.addString(", direction=");
    isb.addString(_direction.description());
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

}
