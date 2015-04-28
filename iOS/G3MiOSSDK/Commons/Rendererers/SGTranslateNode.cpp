//
//  SGTranslateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGTranslateNode.hpp"

Vector3D SGTranslateNode::getMax(const MutableMatrix44D& transformation){
  MutableMatrix44D t = transformation.multiply(_translationMatrix);
  return SGNode::getMax(t);
}

Vector3D SGTranslateNode::getMin(const MutableMatrix44D& transformation){
  MutableMatrix44D t = transformation.multiply(_translationMatrix);
  return SGNode::getMin(t);
}
