//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "Frustum.h"
#include "Box.h"


bool Frustum::contains(const Vector3D& point) const {
  if (_leftPlane.evaluate(point)   > 0) return false;
  if (_rightPlane.evaluate(point)  > 0) return false;
  if (_bottomPlane.evaluate(point) > 0) return false;
  if (_topPlane.evaluate(point)    > 0) return false;
  if (_nearPlane.evaluate(point)   > 0) return false;
  if (_farPlane.evaluate(point)    > 0) return false;
  return true;
}


bool Frustum::touchesWithBox(const Box *box) const
{
  bool outside;
  
  // create an array with the 8 corners of the box
  Vector3D min = box->getMin();
  Vector3D max = box->getMax();   
  Vector3D corners[8] = {
    Vector3D (min.x(), min.y(), min.z()),
    Vector3D (min.x(), min.y(), max.z()),
    Vector3D (min.x(), max.y(), min.z()),
    Vector3D (min.x(), max.y(), max.z()),
    Vector3D (max.x(), min.y(), min.z()),
    Vector3D (max.x(), min.y(), max.z()),
    Vector3D (max.x(), max.y(), min.z()),
    Vector3D (max.x(), max.y(), max.z())
  };
   
  // test with left plane
  outside = true;
  for (unsigned int i=0; i<8; i++) 
    if (_leftPlane.evaluate(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with bottom plane
  outside = true;
  for (unsigned int i=0; i<8; i++) 
    if (_bottomPlane.evaluate(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with right plane
  outside = true;
  for (unsigned int i=0; i<8; i++) 
    if (_rightPlane.evaluate(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with top plane
  outside = true;
  for (unsigned int i=0; i<8; i++) 
    if (_topPlane.evaluate(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with near plane
  outside = true;
  for (unsigned int i=0; i<8; i++) 
    if (_nearPlane.evaluate(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  // test with far plane
  outside = true;
  for (unsigned int i=0; i<8; i++) 
    if (_farPlane.evaluate(corners[i])<0) {
      outside = false;
      break;
    }
  if (outside) return false;
  
  return true;
}
