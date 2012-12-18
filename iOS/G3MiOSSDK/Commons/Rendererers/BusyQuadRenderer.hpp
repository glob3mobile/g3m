//
//  BusyQuadRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 13/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_BusyQuadRenderer_hpp
#define G3MiOSSDK_BusyQuadRenderer_hpp


#include "LeafRenderer.hpp"
#include "IndexedMesh.hpp"
#include "Effects.hpp"


//***************************************************************


class BusyQuadRenderer : public LeafRenderer, EffectTarget {
private:
  double  _degrees;
//  const std::string _textureFilename;
  IImage* _image;
  Mesh*   _quadMesh;
  
  bool initMesh(const G3MRenderContext* rc);
  
  
  
public:
  BusyQuadRenderer(IImage* image):
  _degrees(0),
  _quadMesh(NULL),
  _image(image)
  {
  }
  
  void initialize(const G3MContext* context) {
  }
  
  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }
  
  void render(const G3MRenderContext* rc,
              const GLState& parentState);
  
  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
    
  }
  
  virtual ~BusyQuadRenderer() {}
  
  void incDegrees(double value) {
    _degrees += value;
    if (_degrees>360) _degrees -= 360;
  }
  
  void start();
  
  void stop();
  
  void onResume(const G3MContext* context) {
    
  }
  
  void onPause(const G3MContext* context) {
    
  }

  void onDestroy(const G3MContext* context) {

  }

  void unusedMethod() const {
    
  }

};

//***************************************************************

class BusyEffect : public EffectWithForce {
private:
  BusyQuadRenderer* _renderer;
  
public:
  
  BusyEffect(BusyQuadRenderer *renderer):
  EffectWithForce(1, 1),
  _renderer(renderer)
  { }
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when) {}
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    EffectWithForce::doStep(rc, when);
    _renderer->incDegrees(3);
  }
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) { }
  
  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
};



#endif
