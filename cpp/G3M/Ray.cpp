//
//  Ray.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//

#include "Ray.hpp"

#include "IStringBuilder.hpp"

#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "Color.hpp"
#include "MutableVector3D.hpp"


Ray::Ray(const Vector3D& origin,
         const Vector3D& direction):
_origin(origin),
_direction(direction.normalized()),
_mesh(NULL)
{
}

Ray::~Ray() {
  delete _mesh;
}

double Ray::distanceTo(const Vector3D& point) const {
  return _direction.cross(point._x - _origin._x,
                          point._y - _origin._y,
                          point._z - _origin._z).length();
}

double Ray::squaredDistanceTo(const Vector3D& point) const {
  return _direction.cross(point._x - _origin._x,
                          point._y - _origin._y,
                          point._z - _origin._z).squaredLength();
}

double Ray::distanceTo(const MutableVector3D& point) const {
  return _direction.cross(point._x - _origin._x,
                          point._y - _origin._y,
                          point._z - _origin._z).length();
}

double Ray::squaredDistanceTo(const MutableVector3D& point) const {
  return _direction.cross(point._x - _origin._x,
                          point._y - _origin._y,
                          point._z - _origin._z).squaredLength();
}

const std::string Ray::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(Ray origin=");
  isb->addString(_origin.description());
  isb->addString(", direction=");
  isb->addString(_direction.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

void Ray::render(const G3MRenderContext* rc,
                 const GLState* parentState,
                 const Color& color,
                 float lineWidth) const {
  if (_mesh == NULL) {
    FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithGivenCenter(_origin);
    
    vertices->add( _origin );
    vertices->add( _origin.add(_direction.times(100000)) );
    
    _mesh = new DirectMesh(GLPrimitive::lineStrip(),
                           true,
                           vertices->getCenter(),
                           vertices->create(),
                           lineWidth,
                           1,
                           new Color(color),
                           NULL, // const IFloatBuffer* colors
                           true  // bool depthTest
                           );
    
    delete vertices;
  }
  
  _mesh->render(rc, parentState);
}

bool Ray::closestPointsOnTwoRays(const Ray& ray1,
                                 const Ray& ray2,
                                 MutableVector3D& closestPointRay1,
                                 MutableVector3D& closestPointRay2) {
  
  closestPointRay1.set(0,0,0);
  closestPointRay2.set(0,0,0);
  
  const double a = ray1._direction.dot(ray1._direction);
  const double b = ray1._direction.dot(ray2._direction);
  const double e = ray2._direction.dot(ray2._direction);
  
  const double d = a*e - b*b;
  
  //lines are not parallel
  if (d != 0.0) {
    const Vector3D r = ray1._origin.sub(ray2._origin);
    const double c = ray1._direction.dot(r);
    const double f = ray2._direction.dot(r);
    
    const double s = (b*f - c*e) / d;
    const double t = (a*f - c*b) / d;
    
    closestPointRay1.set( ray1.pointAtTime(s) );
    closestPointRay2.set( ray2.pointAtTime(t) );
    
    return true;
  }
  
  return false;
}
