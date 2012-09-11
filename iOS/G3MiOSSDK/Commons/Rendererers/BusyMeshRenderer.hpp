//
//  BusyMeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_BusyMeshRenderer_hpp
#define G3MiOSSDK_BusyMeshRenderer_hpp

#include "Renderer.hpp"
#include "IndexedMesh.hpp"
#include "Effects.hpp"


//***************************************************************


class BusyMeshRenderer : public Renderer, EffectTarget {
private:
  Mesh    *_mesh;
  double  _degrees;
  
public:    
  BusyMeshRenderer(): _degrees(0) {}
  
  void initialize(const InitializationContext* ic);
  
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
  
  virtual ~BusyMeshRenderer() {}
  
  void incDegrees(double value) { 
    _degrees += value; 
    if (_degrees>360) _degrees -= 360;
  }

  void start();
  
  void stop();
  
  bool isEffectable() const{
    return true;
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
  
  virtual void start(const RenderContext *rc, const TimeInterval& now) {}
  
  virtual void doStep(const RenderContext *rc, const TimeInterval& now) {
    EffectWithForce::doStep(rc, now);
    _renderer->incDegrees(5);
  }
  
  virtual void stop(const RenderContext *rc, const TimeInterval& now) { }
  
  virtual void cancel(const TimeInterval& now) {
    // do nothing, just leave the effect in the intermediate state
  }
  
  bool isEffectable() const{
    return true;
  }
  
};


#endif
