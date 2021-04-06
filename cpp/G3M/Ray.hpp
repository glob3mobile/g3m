//
//  Ray.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//

#ifndef Ray_hpp
#define Ray_hpp

#include <string>

#include "Vector3D.hpp"

class Mesh;
class G3MRenderContext;
class GLState;
class Color;


class Ray {
private:
  mutable Mesh* _mesh;

public:
  const Vector3D _origin;
  const Vector3D _direction;

  Ray(const Vector3D& origin,
      const Vector3D& direction);

  ~Ray();

  double distanceTo(const Vector3D& point) const;
  double squaredDistanceTo(const Vector3D& point) const;

  double distanceTo(const MutableVector3D& point) const;
  double squaredDistanceTo(const MutableVector3D& point) const;

  void render(const G3MRenderContext* rc,
              const GLState* parentState,
              const Color& color,
              float lineWidth) const;
  
  Vector3D pointAtTime(double t) const{
    return _origin.add(_direction.times(t));
  }
  
  static bool closestPointsOnTwoRays(const Ray& ray1,
                                     const Ray& ray2,
                                     MutableVector3D& closestPointRay1,
                                     MutableVector3D& closestPointRay2);

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
