//
//  Box.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 17/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "Box.hpp"
#include "Vector2D.hpp"
#include "Camera.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "IntBufferBuilder.hpp"

#include "GLConstants.hpp"


const std::vector<Vector3D> Box::getCorners() const
{
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
  if (_corners == null) {
    _corners = new java.util.ArrayList<Vector3D>(8);
    
    _corners.add(_lower);
    _corners.add(new Vector3D(_lower._x, _lower._y, _upper._z));
    _corners.add(new Vector3D(_lower._x, _upper._y, _lower._z));
    _corners.add(new Vector3D(_lower._x, _upper._y, _upper._z));
    _corners.add(new Vector3D(_upper._x, _lower._y, _lower._z));
    _corners.add(new Vector3D(_upper._x, _lower._y, _upper._z));
    _corners.add(new Vector3D(_upper._x, _upper._y, _lower._z));
    _corners.add(_upper);
  }
  return _corners;
#endif
}

Vector2I Box::projectedExtent(const RenderContext *rc) const {
  const std::vector<Vector3D> corners = getCorners();

  const Camera* currentCamera = rc->getCurrentCamera();
  
  const Vector2I pixel0 = currentCamera->point2Pixel(corners[0]);

  int lowerX = pixel0._x;
  int upperX = pixel0._x;
  int lowerY = pixel0._y;
  int upperY = pixel0._y;
  
  const int cornersSize = corners.size();
  for (int i = 1; i < cornersSize; i++) {
    const Vector2I pixel = currentCamera->point2Pixel(corners[i]);
    
    const int x = pixel._x;
    const int y = pixel._y;
    
    if (x < lowerX) { lowerX = x; }
    if (y < lowerY) { lowerY = y; }
    
    if (x > upperX) { upperX = x; }
    if (y > upperY) { upperY = y; }
  }
  
  const int width = upperX - lowerX;
  const int height = upperY - lowerY;
  
  return Vector2I(width, height);
}

double Box::squaredProjectedArea(const RenderContext* rc) const {
  const Vector2I extent = projectedExtent(rc);
  return extent._x * extent._y;
}

bool Box::contains(const Vector3D& p) const{
  const static double margin = 1e-3;
  if (p._x < _lower._x - margin) return false;
  if (p._x > _upper._x + margin) return false;
  
  if (p._y < _lower._y - margin) return false;
  if (p._y > _upper._y + margin) return false;
  
  if (p._z < _lower._z - margin) return false;
  if (p._z > _upper._z + margin) return false;
  
  return true;
}

Vector3D Box::intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const{
  
  //MIN X
  {
    Plane p( Vector3D(1.0, 0.0, 0.0), _lower._x);
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MAX X
  {
    Plane p( Vector3D(1.0, 0.0, 0.0), _upper._x);
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MIN Y
  {
    Plane p( Vector3D(0.0, 1.0, 0.0), _lower._y);
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MAX Y
  {
    Plane p( Vector3D(0.0, 1.0, 0.0), _upper._y);
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MIN Z
  {
    Plane p( Vector3D(0.0, 0.0, 1.0), _lower._z);
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MAX Z
  {
    Plane p( Vector3D(0.0, 0.0, 1.0), _upper._z);
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  return Vector3D::nan();  
}


void Box::createMesh()
{
  unsigned int numVertices = 8;
  int numIndices = 48;
  
  float v[] = {
    (float) _lower._x, (float) _lower._y, (float) _lower._z,
    (float) _lower._x, (float) _upper._y, (float) _lower._z,
    (float) _lower._x, (float) _upper._y, (float) _upper._z,
    (float) _lower._x, (float) _lower._y, (float) _upper._z,
    (float) _upper._x, (float) _lower._y, (float) _lower._z,
    (float) _upper._x, (float) _upper._y, (float) _lower._z,
    (float) _upper._x, (float) _upper._y, (float) _upper._z,
    (float) _upper._x, (float) _lower._y, (float) _upper._z
  };
  
  int i[] = { 
    0, 1, 1, 2, 2, 3, 3, 0,
    1, 5, 5, 6, 6, 2, 2, 1,
    5, 4, 4, 7, 7, 6, 6, 5,
    4, 0, 0, 3, 3, 7, 7, 4,
    3, 2, 2, 6, 6, 7, 7, 3,
    0, 1, 1, 5, 5, 4, 4, 0
  };
  
  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());
  IntBufferBuilder indices;
  
  for (unsigned int n=0; n<numVertices; n++)
    vertices.add(v[n*3], v[n*3+1], v[n*3+2]);
  
  for (unsigned int n=0; n<numIndices; n++)
    indices.add(i[n]);
  
  Color *flatColor = new Color(Color::fromRGBA((float)1.0, (float)1.0, (float)0.0, (float)1.0));
    
  // create mesh
  _mesh = new IndexedMesh(GLPrimitive::triangleStrip(),
                          true,
                          vertices.getCenter(),
                          vertices.create(),
                          indices.create(),
                          1,
                          flatColor);
}


void Box::render(const RenderContext* rc)
{
  if (_mesh == NULL) createMesh(); 
  _mesh->render(rc);
}


bool Box::touchesBox(const Box* box) const
{
  if (_lower._x > box->_upper._x) return false;
  if (_upper._x < box->_lower._x) return false;
  if (_lower._y > box->_upper._y) return false;
  if (_upper._y < box->_lower._y) return false;
  if (_lower._z > box->_upper._z) return false;
  if (_upper._z < box->_lower._z) return false;
  return true;
}
