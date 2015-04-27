//
//  SGTranslateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGTranslateNode.hpp"

Vector3D SGTranslateNode::getMax(const MutableMatrix44D& transformation){
  MutableVector3D res(-9e99,-9e99,-9e99);
  
  MutableMatrix44D t = transformation.multiply(_translationMatrix);
  const size_t s = getChildrenCount();
  for (int i = 0; i < s; i++) {
    Vector3D v = getChild(i)->getMax(t);
    res.copyFrom(Vector3D::maxOnAllAxis(v, res.asVector3D()));
  }
  return res.asVector3D();
}

Vector3D SGTranslateNode::getMin(const MutableMatrix44D& transformation){
  MutableVector3D res(9e99, 9e99, 9e99);
  
  MutableMatrix44D t = transformation.multiply(_translationMatrix);
  const size_t s = getChildrenCount();
  for (int i = 0; i < s; i++) {
    Vector3D v = getChild(i)->getMin(t);
    res.copyFrom(Vector3D::minOnAllAxis(v, res.asVector3D()));
  }
  return res.asVector3D();
}
