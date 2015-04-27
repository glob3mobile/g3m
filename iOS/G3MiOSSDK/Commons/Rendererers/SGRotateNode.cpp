//
//  SGRotateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGRotateNode.hpp"

Vector3D SGRotateNode::getMax(){
  MutableVector3D res(-9e99,-9e99,-9e99);
  const size_t s = getChildrenCount();
  for (int i = 0; i < s; i++) {
    Vector3D v = getChild(i)->getMax();
    res.copyFrom(Vector3D::maxOnAllAxis(v.transformedBy(_rotationMatrix, 1.0), res.asVector3D()));
  }
  return res.asVector3D();
}

Vector3D SGRotateNode::getMin(){
  MutableVector3D res(9e99, 9e99, 9e99);
  const size_t s = getChildrenCount();
  for (int i = 0; i < s; i++) {
    Vector3D v = getChild(i)->getMin();
    res.copyFrom(Vector3D::minOnAllAxis(v.transformedBy(_rotationMatrix, 1.0), res.asVector3D()));
  }
  return res.asVector3D();
}