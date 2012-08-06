//
//  SceneGraphRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_SceneGraphRenderer_hpp
#define G3MiOSSDK_SceneGraphRenderer_hpp

#include "Renderer.hpp"
#include "MutableMatrix44D.hpp"
#include "MutableVector3D.hpp"

#include <vector>
#include "Context.hpp"
#include "GL.hpp"


class SGGGroupNode;

class SGNode {
protected:
  SGGGroupNode*    _parent;
  
  MutableMatrix44D _localMatrix;
  MutableMatrix44D _fullMatrix;
  bool             _localMatrixDirty;
  bool             _fullMatrixDirty;
  
  MutableVector3D  _translation;
  MutableVector3D  _scale;
  double           _headingInRadians;
  double           _pitchInRadians;
  double           _rollInRadians;
  
  
protected:
  SGNode() :
  _parent(NULL),
  _localMatrixDirty(true),
  _fullMatrixDirty(true),
  _translation(0, 0, 0), 
  _scale(1, 1, 1),
  _headingInRadians(0),
  _pitchInRadians(0),
  _rollInRadians(0)
  {
    
  }
  
  MutableMatrix44D getFullMatrix();
  MutableMatrix44D getLocalMatrix();
  
  void applyRotationToLocalMatrix();
  void applyScaleToLocalMatrix();
  void applyTranslationToLocalMatrix();
  
  
public:
  virtual void transformationChanged() {
    _localMatrixDirty = true;
    _fullMatrixDirty = true;
  }
  
  SGGGroupNode* getParent() const;
  void setParent(SGGGroupNode* parent);  
  
  Vector3D getTranslation() const;
  Vector3D getScale() const;
  Angle getHeading() const;
  Angle getPitch() const;
  Angle getRoll() const;
  
  void setTranslation(const Vector3D& translation);  
  void setScale(const Vector3D& scale);
  void setHeading(const Angle& heading);  
  void setPitch(const Angle& pitch);
  void setRoll(const Angle& roll);
  
  virtual int render(const RenderContext *rc) = 0;
  
  virtual ~SGNode() {}
};


class SGGGroupNode : public SGNode {
private:
  std::vector<SGNode*> _children;
  bool                 _boundsDirty;
  
  void clearBounds() {
    _boundsDirty = true;
  }
  
public:
  SGGGroupNode() : _boundsDirty(true) {
    
  }
  
  virtual void transformationChanged() {
    SGNode::transformationChanged();
    
    for (int i = 0; i < _children.size(); i++) {
      SGNode* child = _children[i];
      child->transformationChanged();
    }
  }
  
  void childrenChanged() {
    clearBounds();
    
    if (_parent != NULL) {
      _parent->childrenChanged();
    }
  }
  
  
  void addChild(SGNode* node);
  
  void removeChild(const int index);
  
  void removeChild(const SGNode* nodeToRemove);
  
  virtual int render(const RenderContext *rc) {
    int max = MAX_TIME_TO_RENDER;
    
    for (int i = 0; i < _children.size(); i++) {
      SGNode* child = _children[i];
      const int childTimeToRender = child->render(rc);
      if (childTimeToRender < max) {
        max = childTimeToRender;
      }
    }
    
    return max;
  }
  
};


class SGGLeafNode : public SGNode {
protected:
  virtual bool isVisible(const RenderContext *rc) {
    return true;
  };
  
  virtual int rawRender(const RenderContext *rc) = 0;
  
public:
  
  virtual int render(const RenderContext *rc) {
    
    if (!isVisible(rc)) {
      return MAX_TIME_TO_RENDER;
    }
    
    GL* gl = rc->getGL();
    
    gl->pushMatrix();
    MutableMatrix44D fullMatrix = getFullMatrix();
    gl->loadMatrixf(fullMatrix);
    
    int timeToRender = rawRender(rc);
    
    gl->pushMatrix();
    
    return timeToRender;
  }
  
};


class SGCubeNode : public SGGLeafNode {
private:
  bool   _initializedGL;
  
  int _numIndices;
  
  int* _index;
  float* _vertices;
  float _halfSize;
  
  
public:
  SGCubeNode() : _initializedGL(false), _halfSize(0.5) {
    
  }
  
  void initialize(const RenderContext *rc);
  
  virtual int rawRender(const RenderContext *rc);
};


class SceneGraphRenderer : public Renderer {
private:
  SGGGroupNode* _rootNode;
  
public:
  SceneGraphRenderer() : _rootNode(new SGGGroupNode()) {
    
  }
  
  SceneGraphRenderer(SGGGroupNode* rootNode) : _rootNode(rootNode) {
    
  }
  
  SGGGroupNode* getRootNode() const {
    return _rootNode;
  }
  
  virtual void initialize(const InitializationContext* ic);
  
  virtual int render(const RenderContext* rc);
  
  virtual bool onTouchEvent(const TouchEvent* touchEvent);
  
  virtual void onResizeViewportEvent(int width, int height);
  
  virtual ~SceneGraphRenderer() { }
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }

};


#endif
