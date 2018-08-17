package org.glob3.mobile.generated;//
//  BoundingVolume.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 16/07/12.
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
//class Sphere;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;

public abstract class BoundingVolume
{
  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double projectedArea(const G3MRenderContext* rc) const = 0;
  public abstract double projectedArea(G3MRenderContext rc);
  //virtual Vector2I projectedExtent(const G3MRenderContext* rc) const = 0;

  //virtual Vector3D intersectionWithRay(const Vector3D& origin,
  //                                     const Vector3D& direction) const = 0;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void render(const G3MRenderContext* rc, const GLState* parentState, const Color& color) const = 0;
  public abstract void render(G3MRenderContext rc, GLState parentState, Color color);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean touches(const BoundingVolume* that) const = 0;
  public abstract boolean touches(BoundingVolume that);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean touchesBox(const Box* that) const = 0;
  public abstract boolean touchesBox(Box that);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean touchesSphere(const Sphere* that) const = 0;
  public abstract boolean touchesSphere(Sphere that);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean touchesFrustum(const Frustum* that) const = 0;
  public abstract boolean touchesFrustum(Frustum that);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean contains(const Vector3D& point) const = 0;
  public abstract boolean contains(Vector3D point);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean fullContains(const BoundingVolume* that) const = 0;
  public abstract boolean fullContains(BoundingVolume that);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean fullContainedInBox(const Box* that) const = 0;
  public abstract boolean fullContainedInBox(Box that);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean fullContainedInSphere(const Sphere* that) const = 0;
  public abstract boolean fullContainedInSphere(Sphere that);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual BoundingVolume* mergedWith(const BoundingVolume* that) const = 0;
  public abstract BoundingVolume mergedWith(BoundingVolume that);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual BoundingVolume* mergedWithBox(const Box* that) const = 0;
  public abstract BoundingVolume mergedWithBox(Box that);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual BoundingVolume* mergedWithSphere(const Sphere* that) const = 0;
  public abstract BoundingVolume mergedWithSphere(Sphere that);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Sphere* createSphere() const = 0;
  public abstract Sphere createSphere();

}
