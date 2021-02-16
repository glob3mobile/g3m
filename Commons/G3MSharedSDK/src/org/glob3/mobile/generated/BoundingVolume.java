package org.glob3.mobile.generated;
//
//  BoundingVolume.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 16/07/12.
//


//class G3MRenderContext;
//class GLState;
//class Color;
//class Box;
//class Sphere;
//class Frustum;
//class Vector3D;
//class MutableVector3D;


public abstract class BoundingVolume
{
  public void dispose()
  {

  }

  public abstract double projectedArea(G3MRenderContext rc);

  public abstract void render(G3MRenderContext rc, GLState parentState, Color color);

  public abstract boolean touches(BoundingVolume that);
  public abstract boolean touchesBox(Box that);
  public abstract boolean touchesSphere(Sphere that);

  public abstract boolean touchesFrustum(Frustum that);

  public abstract boolean contains(Vector3D point);
  public abstract boolean contains(MutableVector3D point);

  public abstract boolean fullContains(BoundingVolume that);
  public abstract boolean fullContainedInBox(Box that);
  public abstract boolean fullContainedInSphere(Sphere that);

  public abstract BoundingVolume mergedWith(BoundingVolume that);
  public abstract BoundingVolume mergedWithBox(Box that);
  public abstract BoundingVolume mergedWithSphere(Sphere that);

  public abstract Sphere createSphere();

  public abstract BoundingVolume copy();

}