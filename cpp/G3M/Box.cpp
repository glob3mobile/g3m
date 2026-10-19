//
//  Box.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 17/07/12.
//

#include "Box.hpp"
#include "Vector2D.hpp"
#include "Camera.hpp"
#include "Sphere.hpp"
#include "Vector2F.hpp"
#include "G3MRenderContext.hpp"
#include "Ray.hpp"
#include "IMathUtils.hpp"
#include "IStringBuilder.hpp"
#include "IStringUtils.hpp"


Box::~Box() {
#ifdef JAVA_CODE
  super.dispose();
#endif

};

const std::vector<Vector3D> Box::getCorners() const {
#ifdef C_CODE
  const Vector3D c[8] = {
    _lower,
    Vector3D(_lower._x, _lower._y, _upper._z),
    Vector3D(_lower._x, _upper._y, _lower._z),
    Vector3D(_lower._x, _upper._y, _upper._z),
    Vector3D(_upper._x, _lower._y, _lower._z),
    Vector3D(_upper._x, _lower._y, _upper._z),
    Vector3D(_upper._x, _upper._y, _lower._z),
    _upper
  };
  
  return std::vector<Vector3D>(c, c+8);
#endif
#ifdef JAVA_CODE
  if (_cornersD == null) {
    _cornersD = new java.util.ArrayList<Vector3D>(8);
    
    _cornersD.add(_lower);
    _cornersD.add(new Vector3D(_lower._x, _lower._y, _upper._z));
    _cornersD.add(new Vector3D(_lower._x, _upper._y, _lower._z));
    _cornersD.add(new Vector3D(_lower._x, _upper._y, _upper._z));
    _cornersD.add(new Vector3D(_upper._x, _lower._y, _lower._z));
    _cornersD.add(new Vector3D(_upper._x, _lower._y, _upper._z));
    _cornersD.add(new Vector3D(_upper._x, _upper._y, _lower._z));
    _cornersD.add(_upper);
  }
  return _cornersD;
#endif
}

const std::vector<Vector3F> Box::getCornersF() const {
#ifdef C_CODE
  const Vector3F c[8] = {
    Vector3F((float) _lower._x, (float) _lower._y, (float) _lower._z),
    Vector3F((float) _lower._x, (float) _lower._y, (float) _upper._z),
    Vector3F((float) _lower._x, (float) _upper._y, (float) _lower._z),
    Vector3F((float) _lower._x, (float) _upper._y, (float) _upper._z),
    Vector3F((float) _upper._x, (float) _lower._y, (float) _lower._z),
    Vector3F((float) _upper._x, (float) _lower._y, (float) _upper._z),
    Vector3F((float) _upper._x, (float) _upper._y, (float) _lower._z),
    Vector3F((float) _upper._x, (float) _upper._y, (float) _upper._z)
  };

  return std::vector<Vector3F>(c, c+8);
#endif
#ifdef JAVA_CODE
  if (_cornersF == null) {
    _cornersF = new java.util.ArrayList<Vector3F>(8);

    _cornersF.add(new Vector3F((float) _lower._x, (float) _lower._y, (float) _lower._z));
    _cornersF.add(new Vector3F((float) _lower._x, (float) _lower._y, (float) _upper._z));
    _cornersF.add(new Vector3F((float) _lower._x, (float) _upper._y, (float) _lower._z));
    _cornersF.add(new Vector3F((float) _lower._x, (float) _upper._y, (float) _upper._z));
    _cornersF.add(new Vector3F((float) _upper._x, (float) _lower._y, (float) _lower._z));
    _cornersF.add(new Vector3F((float) _upper._x, (float) _lower._y, (float) _upper._z));
    _cornersF.add(new Vector3F((float) _upper._x, (float) _upper._y, (float) _lower._z));
    _cornersF.add(new Vector3F((float) _upper._x, (float) _upper._y, (float) _upper._z));
  }
  return _cornersF;
#endif
}

Vector2F Box::projectedExtent(const G3MRenderContext* rc) const {
  const std::vector<Vector3F> corners = getCornersF();

  const Camera* currentCamera = rc->getCurrentCamera();

  const Vector2F pixel0 = currentCamera->point2Pixel(corners[0]);

  float lowerX = pixel0._x;
  float upperX = pixel0._x;
  float lowerY = pixel0._y;
  float upperY = pixel0._y;

  const size_t cornersSize = corners.size();
  for (size_t i = 1; i < cornersSize; i++) {
    const Vector2F pixel = currentCamera->point2Pixel(corners[i]);

    const float x = pixel._x;
    const float y = pixel._y;

    if (x < lowerX) { lowerX = x; }
    if (y < lowerY) { lowerY = y; }

    if (x > upperX) { upperX = x; }
    if (y > upperY) { upperY = y; }
  }

  const float width  = upperX - lowerX;
  const float height = upperY - lowerY;

  return Vector2F(width, height);
}

double Box::projectedArea(const G3MRenderContext* rc) const {
//  const Vector2I extent = projectedExtent(rc);
//  return extent._x * extent._y;

  const std::vector<Vector3F> corners = getCornersF();

  const Camera* currentCamera = rc->getCurrentCamera();

  const Vector2F pixel0 = currentCamera->point2Pixel(corners[0]);

  float lowerX = pixel0._x;
  float upperX = pixel0._x;
  float lowerY = pixel0._y;
  float upperY = pixel0._y;

  const size_t cornersSize = corners.size();
  for (size_t i = 1; i < cornersSize; i++) {
    const Vector2F pixel = currentCamera->point2Pixel(corners[i]);

    const float x = pixel._x;
    const float y = pixel._y;

    if (x < lowerX) { lowerX = x; }
    if (y < lowerY) { lowerY = y; }

    if (x > upperX) { upperX = x; }
    if (y > upperY) { upperY = y; }
  }

  const float width = upperX - lowerX;
  const float height = upperY - lowerY;

  return width * height;
}

bool Box::contains(double x, double y, double z) const {
  // absolute tolerance for floating-point rounding: intersectionWithRay tests points computed on the faces
  const static double margin = 1e-3;
  if (x < _lower._x - margin) return false;
  if (x > _upper._x + margin) return false;

  if (y < _lower._y - margin) return false;
  if (y > _upper._y + margin) return false;

  if (z < _lower._z - margin) return false;
  if (z > _upper._z + margin) return false;

  return true;
}

bool Box::contains(const Vector3D& point) const {
  return contains(point._x, point._y, point._z);
}

bool Box::contains(const MutableVector3D& point) const {
  return contains(point._x, point._y, point._z);
}

Vector3D Box::intersectionWithRay(const Vector3D& origin,
                                  const Vector3D& direction) const {
  // MIN X
  {
    const Plane p( Vector3D::UP_X, _lower._x);
    const Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  // MAX X
  {
    const Plane p( Vector3D::UP_X, _upper._x);
    const Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  // MIN Y
  {
    const Plane p( Vector3D::UP_Y, _lower._y);
    const Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  // MAX Y
  {
    const Plane p( Vector3D::UP_Y, _upper._y);
    const Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  // MIN Z
  {
    const Plane p( Vector3D::UP_Z, _lower._z);
    const Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  // MAX Z
  {
    const Plane p( Vector3D::UP_Z, _upper._z);
    const Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  return Vector3D::NANV;
}

const bool Box::touchesRay(const Ray* ray) const {
  return !( intersectionWithRay(ray->_origin, ray->_direction).isNan() );
}

bool Box::touchesBox(const Box* that) const {
  if (_lower._x > that->_upper._x) { return false; }
  if (_upper._x < that->_lower._x) { return false; }
  if (_lower._y > that->_upper._y) { return false; }
  if (_upper._y < that->_lower._y) { return false; }
  if (_lower._z > that->_upper._z) { return false; }
  if (_upper._z < that->_lower._z) { return false; }
  return true;
}

bool Box::touchesSphere(const Sphere* that) const {
  return that->touchesBox(this);
}


Box* Box::mergedWithBox(const Box* that) const {
  const IMathUtils* mu = IMathUtils::instance();

  const double lowerX = mu->min(_lower._x, that->_lower._x);
  const double lowerY = mu->min(_lower._y, that->_lower._y);
  const double lowerZ = mu->min(_lower._z, that->_lower._z);

  const double upperX = mu->max(_upper._x, that->_upper._x);
  const double upperY = mu->max(_upper._y, that->_upper._y);
  const double upperZ = mu->max(_upper._z, that->_upper._z);

  return new Box(Vector3D(lowerX, lowerY, lowerZ),
                 Vector3D(upperX, upperY, upperZ));
}

BoundingVolume* Box::mergedWithSphere(const Sphere* that) const {
  return that->mergedWithBox(this);
}

bool Box::fullContainedInBox(const Box* box) const {
  return box->contains(_upper) && box->contains(_lower);
}

bool Box::fullContainedInSphere(const Sphere* that) const {
  return that->contains(_lower) && that->contains(_upper);
}

Vector3D Box::closestPoint(const Vector3D& point) const {
  return point.clamp(_lower, _upper);
}

Sphere* Box::createSphere() const {
  const Vector3D center = _lower.add(_upper).div(2);
  const double radius = center.distanceTo(_upper);
  return new Sphere(center, radius);
}


const std::string Box::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("[Box ");
  isb->addString(_lower.description());
  isb->addString(" / ");
  isb->addString(_upper.description());
  isb->addString("]");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

Box* Box::copy() const {
  // a new Box also in Java: the caller deletes the copy, and dispose() would delete this Box's mesh
  return new Box(_lower, _upper);
}
