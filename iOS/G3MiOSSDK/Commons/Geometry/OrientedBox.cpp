//
//  OrientedBox.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 19/02/16.
//

#include "OrientedBox.hpp"
#include "Mesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "Sphere.hpp"
#include "Box.hpp"
#include "Camera.hpp"


OrientedBox::~OrientedBox() {
  delete _mesh;
  
#ifdef JAVA_CODE
  super.dispose();
#endif
};


Mesh* OrientedBox::createMesh(const Color& color) const {
  double v[] = {
    _SWB._x, _SWB._y, _SWB._z,
    _NWB._x, _NWB._y, _NWB._z,
    _NWT._x, _NWT._y, _NWT._z,
    _SWT._x, _SWT._y, _SWT._z,
    _SEB._x, _SEB._y, _SEB._z,
    _NEB._x, _NEB._y, _NEB._z,
    _NET._x, _NET._y, _NET._z,
    _SET._x, _SET._y, _SET._z,
  };
  
  short i[] = {
    0, 1, 1, 2, 2, 3, 3, 0,
    1, 5, 5, 6, 6, 2, 2, 1,
    5, 4, 4, 7, 7, 6, 6, 5,
    4, 0, 0, 3, 3, 7, 7, 4,
    3, 2, 2, 6, 6, 7, 7, 3,
    0, 1, 1, 5, 5, 4, 4, 0
  };
  
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  const int numVertices = 8;
  for (int n = 0; n < numVertices; n++) {
    vertices->add(v[n*3], v[n*3+1], v[n*3+2]);
  }
  
  ShortBufferBuilder indices;
  const int numIndices = 48;
  for (int n = 0; n < numIndices; n++) {
    indices.add(i[n]);
  }
  
  Mesh* mesh = new IndexedMesh(GLPrimitive::lines(),
                               vertices->getCenter(),
                               vertices->create(),
                               true,
                               indices.create(),
                               true,
                               2,
                               1,
                               new Color(color));
  
  delete vertices;
  
  return mesh;
}


void OrientedBox::render(const G3MRenderContext* rc,
                         const GLState* parentState,
                         const Color& color) const {
  if (_mesh == NULL) {
    _mesh = createMesh(color);
  }
  _mesh->render(rc, parentState);
}


bool OrientedBox::contains(const Vector3D& point) const {
  if (_westPlane.signedDistance(point)   > 0) return false;
  if (_eastPlane.signedDistance(point)   > 0) return false;
  if (_bottomPlane.signedDistance(point) > 0) return false;
  if (_topPlane.signedDistance(point)    > 0) return false;
  if (_northPlane.signedDistance(point)  > 0) return false;
  if (_southPlane.signedDistance(point)  > 0) return false;
  return true;
}

bool OrientedBox::fullContainsSphere(const Sphere* that) const {
  if (_westPlane.signedDistance(that->_center)   > -that->_radius) return false;
  if (_eastPlane.signedDistance(that->_center)   > -that->_radius) return false;
  if (_southPlane.signedDistance(that->_center)  > -that->_radius) return false;
  if (_northPlane.signedDistance(that->_center)  > -that->_radius) return false;
  if (_bottomPlane.signedDistance(that->_center) > -that->_radius) return false;
  if (_topPlane.signedDistance(that->_center)    > -that->_radius) return false;
  return true;
}

bool OrientedBox::fullContainsBox(const Box* that) const {
  double minx = that->_lower._x;
  double miny = that->_lower._y;
  double minz = that->_lower._z;
  double maxx = that->_upper._x;
  double maxy = that->_upper._y;
  double maxz = that->_upper._z;
  
  if (!contains(Vector3D(minx,miny,minz))) return false;
  if (!contains(Vector3D(minx,miny,maxz))) return false;
  if (!contains(Vector3D(minx,maxy,minz))) return false;
  if (!contains(Vector3D(minx,maxy,maxz))) return false;
  if (!contains(Vector3D(maxx,miny,minz))) return false;
  if (!contains(Vector3D(maxx,miny,maxz))) return false;
  if (!contains(Vector3D(maxx,maxy,minz))) return false;
  if (!contains(Vector3D(maxx,maxy,maxz))) return false;
  return true;
}

bool OrientedBox::fullContainedInBox(const Box* that) const {
  if (!that->contains(_SWB)) return false;
  if (!that->contains(_SWT)) return false;
  if (!that->contains(_SEB)) return false;
  if (!that->contains(_SET)) return false;
  if (!that->contains(_NWB)) return false;
  if (!that->contains(_NWT)) return false;
  if (!that->contains(_NEB)) return false;
  if (!that->contains(_NET)) return false;
  return true;
}

bool OrientedBox::fullContainedInSphere(const Sphere* that) const {
  if (_SWB.squaredDistanceTo(that->_center) > that->_radiusSquared) return false;
  if (_SWT.squaredDistanceTo(that->_center) > that->_radiusSquared) return false;
  if (_SEB.squaredDistanceTo(that->_center) > that->_radiusSquared) return false;
  if (_SET.squaredDistanceTo(that->_center) > that->_radiusSquared) return false;
  if (_NWB.squaredDistanceTo(that->_center) > that->_radiusSquared) return false;
  if (_NWT.squaredDistanceTo(that->_center) > that->_radiusSquared) return false;
  if (_NEB.squaredDistanceTo(that->_center) > that->_radiusSquared) return false;
  if (_NET.squaredDistanceTo(that->_center) > that->_radiusSquared) return false;
  return true;
}

bool OrientedBox::fullContainedInOrientedBox(const OrientedBox* that) const {
  if (!that->contains(_SWB)) return false;
  if (!that->contains(_SWT)) return false;
  if (!that->contains(_SEB)) return false;
  if (!that->contains(_SET)) return false;
  if (!that->contains(_NWB)) return false;
  if (!that->contains(_NWT)) return false;
  if (!that->contains(_NEB)) return false;
  if (!that->contains(_NET)) return false;
  return true;
}

Sphere* OrientedBox::createSphere() const {
  const Vector3D center = _SWB.add(_NET).div(2);
  const double radius = center.distanceTo(_SWB);
  return new Sphere(center, radius);
}

Vector2F OrientedBox::projectedExtent(const G3MRenderContext* rc) const {
  const Camera* currentCamera = rc->getCurrentCamera();
  
  const Vector2F pixel0 = currentCamera->point2Pixel(_SWB);
  float lowerX = pixel0._x;
  float upperX = pixel0._x;
  float lowerY = pixel0._y;
  float upperY = pixel0._y;
  
  {
    const Vector2F pixel = currentCamera->point2Pixel(_SWT);
    if (pixel._x < lowerX) lowerX = pixel._x;
    if (pixel._y < lowerY) lowerY = pixel._y;
    if (pixel._x > upperX) upperX = pixel._x;
    if (pixel._y > upperY) upperY = pixel._y;
  }
  {
    const Vector2F pixel = currentCamera->point2Pixel(_SEB);
    if (pixel._x < lowerX) lowerX = pixel._x;
    if (pixel._y < lowerY) lowerY = pixel._y;
    if (pixel._x > upperX) upperX = pixel._x;
    if (pixel._y > upperY) upperY = pixel._y;
  }
  {
    const Vector2F pixel = currentCamera->point2Pixel(_SET);
    if (pixel._x < lowerX) lowerX = pixel._x;
    if (pixel._y < lowerY) lowerY = pixel._y;
    if (pixel._x > upperX) upperX = pixel._x;
    if (pixel._y > upperY) upperY = pixel._y;
  }
  {
    const Vector2F pixel = currentCamera->point2Pixel(_NWB);
    if (pixel._x < lowerX) lowerX = pixel._x;
    if (pixel._y < lowerY) lowerY = pixel._y;
    if (pixel._x > upperX) upperX = pixel._x;
    if (pixel._y > upperY) upperY = pixel._y;
  }
  {
    const Vector2F pixel = currentCamera->point2Pixel(_NWT);
    if (pixel._x < lowerX) lowerX = pixel._x;
    if (pixel._y < lowerY) lowerY = pixel._y;
    if (pixel._x > upperX) upperX = pixel._x;
    if (pixel._y > upperY) upperY = pixel._y;
  }
  {
    const Vector2F pixel = currentCamera->point2Pixel(_NEB);
    if (pixel._x < lowerX) lowerX = pixel._x;
    if (pixel._y < lowerY) lowerY = pixel._y;
    if (pixel._x > upperX) upperX = pixel._x;
    if (pixel._y > upperY) upperY = pixel._y;
  }
  {
    const Vector2F pixel = currentCamera->point2Pixel(_NET);
    if (pixel._x < lowerX) lowerX = pixel._x;
    if (pixel._y < lowerY) lowerY = pixel._y;
    if (pixel._x > upperX) upperX = pixel._x;
    if (pixel._y > upperY) upperY = pixel._y;
  }

  return Vector2F(upperX - lowerX, upperY - lowerY);
}

double OrientedBox::projectedArea(const G3MRenderContext* rc) const {
  // this is not exact.
  // now is returning the area of the bounding rectangle of the box projection
  const Vector2F extent = projectedExtent(rc);
  return (double) (extent._x * extent._y);
}

BoundingVolume* OrientedBox::mergedWith(const BoundingVolume* that) const {
  if (that == NULL) {
    return NULL;
  }
  return that->mergedWithOrientedBox(this);
}

Box* OrientedBox::mergedWithOrientedBox(const OrientedBox* that) const {
  const IMathUtils* mu = IMathUtils::instance();
  
  const double lowerX = mu->min(_minX, that->_minX);
  const double lowerY = mu->min(_minY, that->_minY);
  const double lowerZ = mu->min(_minZ, that->_minZ);
  
  const double upperX = mu->max(_maxX, that->_maxX);
  const double upperY = mu->max(_maxY, that->_maxY);
  const double upperZ = mu->max(_maxZ, that->_maxZ);
  
  return new Box(Vector3D(lowerX, lowerY, lowerZ),
                 Vector3D(upperX, upperY, upperZ));
}

Box* OrientedBox::mergedWithBox(const Box* that) const {
  const IMathUtils* mu = IMathUtils::instance();
  
  const double lowerX = mu->min(_minX, that->_lower._x);
  const double lowerY = mu->min(_minY, that->_lower._y);
  const double lowerZ = mu->min(_minZ, that->_lower._z);
  
  const double upperX = mu->max(_maxX, that->_upper._x);
  const double upperY = mu->max(_maxY, that->_upper._y);
  const double upperZ = mu->max(_maxZ, that->_upper._z);
  
  return new Box(Vector3D(lowerX, lowerY, lowerZ),
                 Vector3D(upperX, upperY, upperZ));
}

Box* OrientedBox::mergedWithSphere(const Sphere *that) const {
  double minX = that->_center._x - that->_radius;
  double maxX = that->_center._x + that->_radius;
  double minY = that->_center._y - that->_radius;
  double maxY = that->_center._y + that->_radius;
  double minZ = that->_center._z - that->_radius;
  double maxZ = that->_center._z + that->_radius;

  if (_minX < minX) minX = _minX;
  if (_maxX > maxX) maxX = _maxX;
  if (_minY < minY) minY = _minY;
  if (_maxY > maxY) maxY = _maxY;
  if (_minZ < minZ) minZ = _minZ;
  if (_maxZ > maxZ) maxZ = _maxZ;
  
  return new Box(Vector3D(minX, minY, minZ),
                 Vector3D(maxX, maxY, maxZ));
}

