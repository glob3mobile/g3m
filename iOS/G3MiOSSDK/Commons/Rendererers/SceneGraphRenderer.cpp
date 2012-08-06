//
//  SceneGraphRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "SceneGraphRenderer.hpp"

void SGNode::setParent(SGGGroupNode* parent) {
  _parent = parent;
}

SGGGroupNode* SGNode::getParent() const {
  return _parent;
}

void SGNode::setTranslation(const Vector3D& translation) {
  _translation = translation.asMutableVector3D();
  transformationChanged();
}

Vector3D SGNode::getTranslation() const {
  return _translation.asVector3D();
}

void SGNode::setScale(const Vector3D& scale) {
  _scale = scale.asMutableVector3D();
  transformationChanged();
}

Vector3D SGNode::getScale() const {
  return _scale.asVector3D();
}

void SGNode::setHeading(const Angle& heading) {
  _headingInRadians = heading.radians();
  transformationChanged();
}

Angle SGNode::getHeading() const {
  return Angle::fromRadians(_headingInRadians);
}

void SGNode::setPitch(const Angle& pitch) {
  _pitchInRadians = pitch.radians();
  transformationChanged();
}

Angle SGNode::getPitch() const {
  return Angle::fromRadians(_pitchInRadians);
}

void SGNode::setRoll(const Angle& roll) {
  _rollInRadians = roll.radians();  
  transformationChanged();
}

Angle SGNode::getRoll() const {
  return Angle::fromRadians(_rollInRadians);
}

void SGNode::applyRotationToLocalMatrix() {
  if (_headingInRadians != 0) {
    _localMatrix = _localMatrix.multiply(MutableMatrix44D::fromRotationZ(Angle::fromRadians(_headingInRadians)));
  }
  if (_pitchInRadians != 0) {
    _localMatrix = _localMatrix.multiply(MutableMatrix44D::fromRotationX(Angle::fromRadians(_pitchInRadians)));
  }
  if (_rollInRadians != 0) {
    _localMatrix = _localMatrix.multiply(MutableMatrix44D::fromRotationY(Angle::fromRadians(_rollInRadians)));
  }
}


void SGNode::applyTranslationToLocalMatrix() {
  if (!_translation.isZero()) {
    _localMatrix = _localMatrix.multiply(MutableMatrix44D::fromTranslation(_translation));
  }
}


void SGNode::applyScaleToLocalMatrix() {
  if ((_scale.x() != 1) || (_scale.y() != 1) || (_scale.z() != 1)) {
    _localMatrix = _localMatrix.multiply(MutableMatrix44D::fromScale(_scale));
  }
}



MutableMatrix44D SGNode::getLocalMatrix() {
  if (!_localMatrixDirty) {
    return _localMatrix;
  }
  _localMatrixDirty = false;
  
  _localMatrix = MutableMatrix44D::identity();
  
//  switch (_transformOrder) {
//    case ROTATION_SCALE_TRANSLATION:
      applyRotationToLocalMatrix();
      applyScaleToLocalMatrix();
      applyTranslationToLocalMatrix();
//      break;
//      
//    case ROTATION_TRANSLATION_SCALE:
//      applyRotationToLocalMatrix();
//      applyTranslationToLocalMatrix();
//      applyScaleToLocalMatrix();
//      break;
//      
//    case TRANSLATION_ROTATION_SCALE:
//      applyTranslationToLocalMatrix();
//      applyRotationToLocalMatrix();
//      applyScaleToLocalMatrix();
//      break;
//      
//    case SCALE_TRANSLATION_ROTATION:
//      applyScaleToLocalMatrix();
//      applyTranslationToLocalMatrix();
//      applyRotationToLocalMatrix();
//      break;
//  }

  return _localMatrix;
}

MutableMatrix44D SGNode::getFullMatrix() {
  if (!_fullMatrixDirty) {
    return _fullMatrix;
  }
  _fullMatrixDirty = false;
  
  MutableMatrix44D localMatrix = getLocalMatrix();
  if (_parent != NULL) {
    localMatrix = _parent->getFullMatrix().multiply(localMatrix);
  }
  
  _fullMatrix = localMatrix;
  
  return _fullMatrix;
}



void SGGGroupNode::addChild(SGNode *node)  {
  _children.push_back(node);
  node->setParent(this);
  childrenChanged();
}

void SGGGroupNode::removeChild(const int index) {
#ifdef C_CODE
  _children.erase(_children.begin() + index);
#endif
#ifdef JAVA_CODE
  _children.remove(index);
#endif
}

void SGGGroupNode::removeChild(const SGNode* nodeToRemove) {
  for (int i = 0; i < _children.size(); i++) {
    SGNode* child = _children[i];
    if (child == nodeToRemove) {
      removeChild(i);
      break;
    }
  }
}

void SceneGraphRenderer::initialize(const InitializationContext *ic) {

}

bool SceneGraphRenderer::onTouchEvent(const TouchEvent *touchEvent) {
  return false;
}

void SceneGraphRenderer::onResizeViewportEvent(int width, int height) {
  
}

int SceneGraphRenderer::render(const RenderContext *rc) {
  return _rootNode->render(rc);
}



void SGCubeNode::initialize(const RenderContext *rc)
{
  int res = 12;
  _vertices = new float[res * res * 3];
  _numIndices = 2 * (res - 1) * (res + 1);
  _index = new int[_numIndices];
  
  // create vertices
  
//  if (ic != NULL && ic->getPlanet() != NULL)
//    _halfSize = ic->getPlanet()->getRadii().x() / 2.0;
//  else     
//  _halfSize = 7e6;
  
  int n = 0;
  for (int j = 0; j < res; j++) {
    for (int i = 0; i < res; i++) {
      _vertices[n++] = (float) 0;
      _vertices[n++] = (float) (-_halfSize + i / (float) (res - 1) * 2*_halfSize);
      _vertices[n++] = (float) (_halfSize - j / (float) (res - 1) * 2*_halfSize);
    }
  }
  
  n = 0;
  for (int j = 0; j < res - 1; j++) {
    if (j > 0) _index[n++] = (char) (j * res);
    for (int i = 0; i < res; i++) {
      _index[n++] = (char) (j * res + i);
      _index[n++] = (char) (j * res + i + res);
    }
    _index[n++] = (char) (j * res + 2 * res - 1);
  }
}  

int SGCubeNode::rawRender(const RenderContext *rc) {
  GL* gl = rc->getGL();

  if (!_initializedGL) {
    initialize(rc);
    _initializedGL = true;
  }
  

  
//  gl->depthTest(true);
//  
//  gl->enableVertices();
//  
//  // insert pointers
//  gl->disableTextures();
//  gl->vertexPointer(3, 0, _vertices);
//  
//  {
//    // draw a red square
//    gl->color((float) 1, (float) 0, (float) 0, 1);
////    gl->pushMatrix();
////    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(_halfSize,0,0));
////    gl->multMatrixf(T);
//    gl->drawTriangleStrip(_numIndices, _index);
////    gl->popMatrix();
//  }
//  
//  
//  gl->depthTest(false);
//  
//  int __complete_cube;
  
  
  
  gl->enableVerticesPosition();
  
  // insert pointers
  gl->disableTextures();
  gl->vertexPointer(3, 0, _vertices);
  
  {
    // draw a red square
    gl->color((float) 1, (float) 0, (float) 0, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(_halfSize,0,0));
    gl->multMatrixf(T);
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  {
    // draw a green square
    gl->color((float) 0, (float) 1, (float) 0, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(0,_halfSize,0));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(90), Vector3D(0,0,1));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  {
    // draw a blue square
    gl->color((float) 0, (float) 0, (float) 1, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(0,-_halfSize,0));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(-90), Vector3D(0,0,1));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  {
    // draw a purple square
    gl->color((float) 1, (float) 0, (float) 1, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(0,0,-_halfSize));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(90), Vector3D(0,1,0));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  {
    // draw a cian square
    gl->color((float) 0, (float) 1, (float) 1, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(0,0,_halfSize));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(-90), Vector3D(0,1,0));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  {
    // draw a grey square
    gl->color((float) 0.5, (float) 0.5, (float) 0.5, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(-_halfSize,0,0));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(180), Vector3D(0,0,1));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  gl->enableTextures();

  
  
  return MAX_TIME_TO_RENDER;
}
