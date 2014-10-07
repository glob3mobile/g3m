package org.glob3.mobile.generated; 
//
//  Extent.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//



//class Vector2D;
//class Vector3D;

//class Frustum;
//class Box;
//class GLState;

public abstract class Extent
{
  public void dispose()
  {
  }



  public double projectedArea(G3MRenderContext rc)
  {
    return IMathUtils.instance().sqrt(squaredProjectedArea(rc));
  }

  public abstract double squaredProjectedArea(G3MRenderContext rc);

  public abstract Vector2I projectedExtent(G3MRenderContext rc);

  public abstract Vector3D intersectionWithRay(Vector3D origin, Vector3D direction);

  public abstract void render(G3MRenderContext rc, GLState parentState);

  public abstract boolean touches(Frustum frustum);

  public abstract boolean touchesBox(Box box);

  public abstract boolean fullContains(Extent that);

  public abstract boolean fullContainedInBox(Box box);

  public abstract Extent mergedWith(Extent that);

  public abstract Extent mergedWithBox(Box that);

}