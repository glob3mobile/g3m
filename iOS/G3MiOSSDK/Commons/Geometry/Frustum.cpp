//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "Frustum.hpp"
#include "Box.hpp"


Frustum::Frustum (const FrustumData& data):
_leftPlane(Plane(Vector3D(0, 0, 0), 
                 Vector3D(data._left, data._top, -data._znear), 
                 Vector3D(data._left, data._bottom, -data._znear))),
_bottomPlane(Plane(Vector3D(0, 0, 0), 
                   Vector3D(data._left, data._bottom, -data._znear), 
                   Vector3D(data._right, data._bottom, -data._znear))),
_rightPlane(Plane(Vector3D(0, 0, 0), 
                  Vector3D(data._right, data._bottom, -data._znear), 
                  Vector3D(data._right, data._top, -data._znear))),
_topPlane(Plane(Vector3D(0, 0, 0), 
                Vector3D(data._right, data._top, -data._znear), 
                Vector3D(data._left, data._top, -data._znear))),
_nearPlane(Plane(Vector3D(0, 0, 1), data._znear)),
_farPlane(Plane(Vector3D(0, 0, -1), -data._zfar))
{
}


bool Frustum::contains(const Vector3D& point) const {
  if (_leftPlane.signedDistance(point)   > 0) return false;
  if (_rightPlane.signedDistance(point)  > 0) return false;
  if (_bottomPlane.signedDistance(point) > 0) return false;
  if (_topPlane.signedDistance(point)    > 0) return false;
  if (_nearPlane.signedDistance(point)   > 0) return false;
  if (_farPlane.signedDistance(point)    > 0) return false;
  return true;
}


bool Frustum::touchesWithBox(const Box *box) const
{
  bool outside;
  
  // create an array with the 8 corners of the box
  Vector3D min = box->getLower();
  Vector3D max = box->getUpper();   
  Vector3D corners[8] = {
    Vector3D(min.x(), min.y(), min.z()),
    Vector3D(min.x(), min.y(), max.z()),
    Vector3D(min.x(), max.y(), min.z()),
    Vector3D(min.x(), max.y(), max.z()),
    Vector3D(max.x(), min.y(), min.z()),
    Vector3D(max.x(), min.y(), max.z()),
    Vector3D(max.x(), max.y(), min.z()),
    Vector3D(max.x(), max.y(), max.z())
  };
  
  // test with left plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_leftPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with bottom plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_bottomPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with right plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_rightPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with top plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_topPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with near plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_nearPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with far plane
  outside = true;
  for (int i=0; i<8; i++) 
    if (_farPlane.signedDistance(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  return true;
}
