//
//  SGRotateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGRotateNode.hpp"

Vector3D SGRotateNode::getMax(const MutableMatrix44D& transformation){
  MutableVector3D res(-9e99,-9e99,-9e99);
  
  MutableMatrix44D t = transformation.multiply(_rotationMatrix);
  const size_t s = getChildrenCount();
  for (int i = 0; i < s; i++) {
    Vector3D v = getChild(i)->getMax(t);
    res.copyFrom(Vector3D::maxOnAllAxis(v, res.asVector3D()));
  }
  return res.asVector3D();
}

Vector3D SGRotateNode::getMin(const MutableMatrix44D& transformation){
  MutableVector3D res(9e99, 9e99, 9e99);
  
  MutableMatrix44D t = transformation.multiply(_rotationMatrix);
  const size_t s = getChildrenCount();
  for (int i = 0; i < s; i++) {
    Vector3D v = getChild(i)->getMin(t);
    res.copyFrom(Vector3D::minOnAllAxis(v, res.asVector3D()));
  }
  return res.asVector3D();
}