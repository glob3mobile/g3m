package org.glob3.mobile.generated;
//
//  DynamicFrustumPolicy.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/13/17.
//
//

//
//  DynamicFrustumPolicy.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/13/17.
//
//




public class DynamicFrustumPolicy extends FrustumPolicy
{

  public void dispose()
  {
    super.dispose();
  }

  public final Vector2D calculateFrustumZNearAndZFar(Camera camera)
  {
    final double height = camera.getGeodeticPosition()._height;
    double zNear = height * 0.1;
  
    final double zFar = camera.getPlanet().distanceToHorizon(camera.getCartesianPosition());
  
    final double goalRatio = 1000;
    final double ratio = zFar / zNear;
    if (ratio < goalRatio)
    {
      zNear = zFar / goalRatio;
    }
  
    return new Vector2D(zNear, zFar);
  }

  public final FrustumPolicy copy()
  {
    return new DynamicFrustumPolicy();
  }

}
