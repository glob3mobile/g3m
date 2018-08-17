package org.glob3.mobile.generated;//
//  Extent.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector3D;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Frustum;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Box;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;

public abstract class Extent
{
  public void dispose()
  {
  }



//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double projectedArea(const G3MRenderContext* rc) const
  public double projectedArea(G3MRenderContext rc)
  {
	return IMathUtils.instance().sqrt(squaredProjectedArea(rc));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double squaredProjectedArea(const G3MRenderContext* rc) const = 0;
  public abstract double squaredProjectedArea(G3MRenderContext rc);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector2I projectedExtent(const G3MRenderContext* rc) const = 0;
  public abstract Vector2I projectedExtent(G3MRenderContext rc);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const = 0;
  public abstract Vector3D intersectionWithRay(Vector3D origin, Vector3D direction);

  public abstract void render(G3MRenderContext rc, GLState parentState);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean touches(const Frustum *frustum) const = 0;
  public abstract boolean touches(Frustum frustum);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean touchesBox(const Box* box) const = 0;
  public abstract boolean touchesBox(Box box);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean fullContains(const Extent* that) const = 0;
  public abstract boolean fullContains(Extent that);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean fullContainedInBox(const Box* box) const = 0;
  public abstract boolean fullContainedInBox(Box box);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Extent* mergedWith(const Extent* that) const = 0;
  public abstract Extent mergedWith(Extent that);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Extent* mergedWithBox(const Box* that) const = 0;
  public abstract Extent mergedWithBox(Box that);

}
