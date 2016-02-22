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
  double minX = _SWB._x;
  double maxX = _SWB._x;
  double minY = _SWB._y;
  double maxY = _SWB._y;
  double minZ = _SWB._z;
  double maxZ = _SWB._z;
  
  if (_SWT._x < minX) minX = _SWT._x;
  if (_SWT._x > maxX) maxX = _SWT._x;
  if (_SWT._y < minY) minY = _SWT._y;
  if (_SWT._y > maxY) maxY = _SWT._y;
  if (_SWT._z < minZ) minZ = _SWT._z;
  if (_SWT._z > maxZ) maxZ = _SWT._z;

  if (_SEB._x < minX) minX = _SEB._x;
  if (_SEB._x > maxX) maxX = _SEB._x;
  if (_SEB._y < minY) minY = _SEB._y;
  if (_SEB._y > maxY) maxY = _SEB._y;
  if (_SEB._z < minZ) minZ = _SEB._z;
  if (_SEB._z > maxZ) maxZ = _SEB._z;
  
  if (_SET._x < minX) minX = _SET._x;
  if (_SET._x > maxX) maxX = _SET._x;
  if (_SET._y < minY) minY = _SET._y;
  if (_SET._y > maxY) maxY = _SET._y;
  if (_SET._z < minZ) minZ = _SET._z;
  if (_SET._z > maxZ) maxZ = _SET._z;
  
  if (_NWB._x < minX) minX = _NWB._x;
  if (_NWB._x > maxX) maxX = _NWB._x;
  if (_NWB._y < minY) minY = _NWB._y;
  if (_NWB._y > maxY) maxY = _NWB._y;
  if (_NWB._z < minZ) minZ = _NWB._z;
  if (_NWB._z > maxZ) maxZ = _NWB._z;
  
  if (_NWT._x < minX) minX = _NWT._x;
  if (_NWT._x > maxX) maxX = _NWT._x;
  if (_NWT._y < minY) minY = _NWT._y;
  if (_NWT._y > maxY) maxY = _NWT._y;
  if (_NWT._z < minZ) minZ = _NWT._z;
  if (_NWT._z > maxZ) maxZ = _NWT._z;
  
  if (_NEB._x < minX) minX = _NEB._x;
  if (_NEB._x > maxX) maxX = _NEB._x;
  if (_NEB._y < minY) minY = _NEB._y;
  if (_NEB._y > maxY) maxY = _NEB._y;
  if (_NEB._z < minZ) minZ = _NEB._z;
  if (_NEB._z > maxZ) maxZ = _NEB._z;
  
  if (_NET._x < minX) minX = _NET._x;
  if (_NET._x > maxX) maxX = _NET._x;
  if (_NET._y < minY) minY = _NET._y;
  if (_NET._y > maxY) maxY = _NET._y;
  if (_NET._z < minZ) minZ = _NET._z;
  if (_NET._z > maxZ) maxZ = _NET._z;
  
  if (that->_SWB._x < minX) minX = that->_SWB._x;
  if (that->_SWB._x > maxX) maxX = that->_SWB._x;
  if (that->_SWB._y < minY) minY = that->_SWB._y;
  if (that->_SWB._y > maxY) maxY = that->_SWB._y;
  if (that->_SWB._z < minZ) minZ = that->_SWB._z;
  if (that->_SWB._z > maxZ) maxZ = that->_SWB._z;
  
  if (that->_SWT._x < minX) minX = that->_SWT._x;
  if (that->_SWT._x > maxX) maxX = that->_SWT._x;
  if (that->_SWT._y < minY) minY = that->_SWT._y;
  if (that->_SWT._y > maxY) maxY = that->_SWT._y;
  if (that->_SWT._z < minZ) minZ = that->_SWT._z;
  if (that->_SWT._z > maxZ) maxZ = that->_SWT._z;
  
  if (that->_SEB._x < minX) minX = that->_SEB._x;
  if (that->_SEB._x > maxX) maxX = that->_SEB._x;
  if (that->_SEB._y < minY) minY = that->_SEB._y;
  if (that->_SEB._y > maxY) maxY = that->_SEB._y;
  if (that->_SEB._z < minZ) minZ = that->_SEB._z;
  if (that->_SEB._z > maxZ) maxZ = that->_SEB._z;
  
  if (that->_NET._x < minX) minX = that->_NET._x;
  if (that->_NET._x > maxX) maxX = that->_NET._x;
  if (that->_NET._y < minY) minY = that->_NET._y;
  if (that->_NET._y > maxY) maxY = that->_NET._y;
  if (that->_NET._z < minZ) minZ = that->_NET._z;
  if (that->_NET._z > maxZ) maxZ = that->_NET._z;
  
  if (that->_NWB._x < minX) minX = that->_NWB._x;
  if (that->_NWB._x > maxX) maxX = that->_NWB._x;
  if (that->_NWB._y < minY) minY = that->_NWB._y;
  if (that->_NWB._y > maxY) maxY = that->_NWB._y;
  if (that->_NWB._z < minZ) minZ = that->_NWB._z;
  if (that->_NWB._z > maxZ) maxZ = that->_NWB._z;
  
  if (that->_NWT._x < minX) minX = that->_NWT._x;
  if (that->_NWT._x > maxX) maxX = that->_NWT._x;
  if (that->_NWT._y < minY) minY = that->_NWT._y;
  if (that->_NWT._y > maxY) maxY = that->_NWT._y;
  if (that->_NWT._z < minZ) minZ = that->_NWT._z;
  if (that->_NWT._z > maxZ) maxZ = that->_NWT._z;
  
  if (that->_NEB._x < minX) minX = that->_NEB._x;
  if (that->_NEB._x > maxX) maxX = that->_NEB._x;
  if (that->_NEB._y < minY) minY = that->_NEB._y;
  if (that->_NEB._y > maxY) maxY = that->_NEB._y;
  if (that->_NEB._z < minZ) minZ = that->_NEB._z;
  if (that->_NEB._z > maxZ) maxZ = that->_NEB._z;
  
  if (that->_NET._x < minX) minX = that->_NET._x;
  if (that->_NET._x > maxX) maxX = that->_NET._x;
  if (that->_NET._y < minY) minY = that->_NET._y;
  if (that->_NET._y > maxY) maxY = that->_NET._y;
  if (that->_NET._z < minZ) minZ = that->_NET._z;
  if (that->_NET._z > maxZ) maxZ = that->_NET._z;
  
  return new Box(Vector3D(minX, minY, minZ),
                 Vector3D(maxX, maxY, maxZ));
}

