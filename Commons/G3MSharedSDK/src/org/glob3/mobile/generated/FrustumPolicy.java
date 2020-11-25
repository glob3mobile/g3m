package org.glob3.mobile.generated;
//
//  FrustumPolicy.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/13/17.
//
//

//
//  FrustumPolicy.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/13/17.
//
//


//class Vector2D;
//class Camera;

public abstract class FrustumPolicy
{
  protected FrustumPolicy()
  {
  
  }

  public void dispose()
  {
  
  }

  public abstract Vector2D calculateFrustumZNearAndZFar(Camera camera);

  public abstract FrustumPolicy copy();

}