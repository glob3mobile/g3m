//
//  BusyMeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_BusyMeshRenderer
#define G3MiOSSDK_BusyMeshRenderer

#include "ProtoRenderer.hpp"
#include "IndexedMesh.hpp"
#include "Effects.hpp"
#include "Color.hpp"
#include "GLState.hpp"


class BusyMeshRenderer : public ProtoRenderer, EffectTarget {
private:
  Mesh    *_mesh;
  double  _degrees;
  Color*  _backgroundColor;
  
  MutableMatrix44D _projectionMatrix;
  mutable MutableMatrix44D _modelviewMatrix;


  ProjectionGLFeature* _projectionFeature;
  ModelGLFeature* _modelFeature;
  
  GLState* _glState;
  
  void createGLState();

  Mesh* createMesh(const G3MRenderContext* rc);
  Mesh* getMesh(const G3MRenderContext* rc);

  
public:    
  BusyMeshRenderer(Color* backgroundColor):
  _degrees(0),
  _backgroundColor(backgroundColor),
  _projectionFeature(NULL),
  _modelFeature(NULL),
  _glState(new GLState()),
  _mesh(NULL)
  {

  }
  
  void initialize(const G3MContext* context) {
    
  }
 
  void render(const G3MRenderContext* rc, GLState* glState);
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
    const int halfWidth = width / 2;
    const int halfHeight = height / 2;
    _projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth,   halfWidth,
                                                                              -halfHeight, halfHeight,
                                                                              -halfWidth,  halfWidth);

    delete _mesh;
    _mesh = NULL;
  }
  
  virtual ~BusyMeshRenderer() {
    delete _mesh;
    delete _backgroundColor;

    _glState->_release();
  }

  void incDegrees(double value) {
    _degrees += value; 
    if (_degrees>360) {
      _degrees -= 360;
    }
    _modelviewMatrix = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, -1));
  }

  void start(const G3MRenderContext* rc);
  
  void stop(const G3MRenderContext* rc);
  
  void onResume(const G3MContext* context) {
    
  }
  
  void onPause(const G3MContext* context) {
    
  }

  void onDestroy(const G3MContext* context) {

  }
};

//***************************************************************

class BusyMeshEffect : public EffectWithForce {
private:
  BusyMeshRenderer* _renderer;

public:

  BusyMeshEffect(BusyMeshRenderer *renderer):
  EffectWithForce(1, 1),
  _renderer(renderer)
  { }

  void start(const G3MRenderContext* rc,
             const TimeInterval& when) {}

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when) {
    EffectWithForce::doStep(rc, when);
    _renderer->incDegrees(5);
  }

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when) { }

  void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }

};


#endif
