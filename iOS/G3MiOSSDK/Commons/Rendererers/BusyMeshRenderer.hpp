//
//  BusyMeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_BusyMeshRenderer_hpp
#define G3MiOSSDK_BusyMeshRenderer_hpp

#include "LeafRenderer.hpp"
#include "IndexedMesh.hpp"
#include "Effects.hpp"
#include "Color.hpp"

#include "GPUProgramState.hpp"

#include "GLClient.hpp"


//***************************************************************


class BusyMeshRenderer : public LeafRenderer, EffectTarget, GLClient {
private:
  Mesh    *_mesh;
  double  _degrees;
  Color*  _backgroundColor;
  
  MutableMatrix44D _projectionMatrix;
  mutable MutableMatrix44D _modelviewMatrix;
  
public:    
  BusyMeshRenderer(Color* backgroundColor):
  _degrees(0),
  _backgroundColor(backgroundColor)
  {
    _modelviewMatrix = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, -1));
    _projectionMatrix = MutableMatrix44D::invalid();
  }
  
  void initialize(const G3MContext* context);
  
  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }
  
  void render(const G3MRenderContext* rc);
  
  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
    const int halfWidth = width / 2;
    const int halfHeight = height / 2;
    _projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth, halfWidth,
                                                                              -halfHeight, halfHeight,
                                                                              -halfWidth, halfWidth);
  }
  
  virtual ~BusyMeshRenderer() {
    delete _mesh;
    delete _backgroundColor;
  }
  
  void incDegrees(double value) { 
    _degrees += value; 
    if (_degrees>360) _degrees -= 360;
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
  
  void notifyGLClientChildrenParentHasChanged(){
    _mesh->actualizeGLGlobalState(this);
  }
  
  void modifyGLGlobalState(GLGlobalState& GLGlobalState) const;
  
  void modifyGPUProgramState(GPUProgramState& progState) const;
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
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when) {}
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    EffectWithForce::doStep(rc, when);
    _renderer->incDegrees(5);
  }
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) { }
  
  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
 
};


#endif
