package org.glob3.mobile.generated; 
//
//  Extent.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector3D;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Frustum;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Box;

public abstract class Extent
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean touches(const Frustum *frustum) const = 0;
  public abstract boolean touches(Frustum frustum);


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double projectedArea(const RenderContext* rc) const
  public double projectedArea(RenderContext rc)
  {
	return IMathUtils.instance().sqrt(squaredProjectedArea(rc));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double squaredProjectedArea(const RenderContext* rc) const = 0;
  public abstract double squaredProjectedArea(RenderContext rc);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector2I projectedExtent(const RenderContext* rc) const = 0;
  public abstract Vector2I projectedExtent(RenderContext rc);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const = 0;
  public abstract Vector3D intersectionWithRay(Vector3D origin, Vector3D direction);

  public abstract void render(RenderContext rc);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean touchesBox(const Box *box) const = 0;
  public abstract boolean touchesBox(Box box);

}