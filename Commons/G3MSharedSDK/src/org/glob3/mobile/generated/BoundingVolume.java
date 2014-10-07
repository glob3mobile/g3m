package org.glob3.mobile.generated; 
//
//  BoundingVolume.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


///#include "IMathUtils.hpp"

//class Vector2D;
//class Vector3D;

//class Frustum;
//class Box;
//class Sphere;
//class GLState;
//class Color;

public abstract class BoundingVolume
{
  public void dispose()
  {

  }

  public abstract double projectedArea(G3MRenderContext rc);
  //virtual Vector2I projectedExtent(const G3MRenderContext* rc) const = 0;

  //virtual Vector3D intersectionWithRay(const Vector3D& origin,
  //                                     const Vector3D& direction) const = 0;

  public abstract void render(G3MRenderContext rc, GLState parentState, Color color);

  public abstract boolean touches(BoundingVolume that);
  public abstract boolean touchesBox(Box that);
  public abstract boolean touchesSphere(Sphere that);

  public abstract boolean touchesFrustum(Frustum that);

  public abstract boolean contains(Vector3D point);

  public abstract boolean fullContains(BoundingVolume that);
  public abstract boolean fullContainedInBox(Box that);
  public abstract boolean fullContainedInSphere(Sphere that);

  public abstract BoundingVolume mergedWith(BoundingVolume that);
  public abstract BoundingVolume mergedWithBox(Box that);
  public abstract BoundingVolume mergedWithSphere(Sphere that);

  public abstract Sphere createSphere();

}