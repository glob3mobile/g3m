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
  const std::string _textureFilename;
  Mesh *  _quadMesh;
  
  bool initMesh(const RenderContext* rc);
  
  
  
public:
  BusyQuadRenderer(const std::string textureFilename):
  _degrees(0),
  _quadMesh(NULL),
  _textureFilename(textureFilename)
  {}
  
  void initialize(const InitializationContext* ic) {}
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  
  void render(const RenderContext* rc);
  
  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }
  
  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height) {
    
  }
  
  virtual ~BusyQuadRenderer() {}
  
  void incDegrees(double value) {
    _degrees += value;
    if (_degrees>360) _degrees -= 360;
  }
  
  void start();
  
  void stop();
  
  void onResume(const InitializationContext* ic) {
    
  }
  
  void onPause(const InitializationContext* ic) {
    
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
  
  virtual void start(const RenderContext *rc, const TimeInterval& now) {}
  
  virtual void doStep(const RenderContext *rc, const TimeInterval& now) {
    EffectWithForce::doStep(rc, now);
    _renderer->incDegrees(3);
  }
  
  virtual void stop(const RenderContext *rc, const TimeInterval& now) { }
  
  virtual void cancel(const TimeInterval& now) {
    // do nothing, just leave the effect in the intermediate state
  }
  
};



#endif
