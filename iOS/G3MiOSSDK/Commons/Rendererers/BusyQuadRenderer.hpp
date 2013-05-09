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
#include "Effects.hpp"
#include "Vector2D.hpp"
#include "Color.hpp"
#include "DirectMesh.hpp"

#include "GPUProgramState.hpp"


//***************************************************************


class BusyQuadRenderer : public LeafRenderer, EffectTarget {
private:
  double      _degrees;
  //  const std::string _textureFilename;
  IImage*     _image;
  Mesh*       _quadMesh;
  
  const bool      _animated;
  const Vector2D  _size;
  Color*          _backgroundColor;
  
  GPUProgramState _programState;
  
  bool initMesh(const G3MRenderContext* rc);
  
  
public:
  BusyQuadRenderer(IImage* image,
                   Color* backgroundColor,
                   const Vector2D& size,
                   const bool animated):
  _degrees(0),
  _quadMesh(NULL),
  _image(image),
  _backgroundColor(backgroundColor),
  _animated(animated),
  _size(size),
  _programState(NULL)
  {
  }
  
  void initialize(const G3MContext* context) {
    _programState.setUniformValue("BillBoard", false);
    _programState.setUniformValue("EnableTexture", false);
    _programState.setUniformValue("PointSize", (float)1.0);
    _programState.setUniformValue("ScaleTexCoord", Vector2D(1.0,1.0));
    _programState.setUniformValue("TextureExtent", Vector2D(0.0,0.0));
    _programState.setUniformValue("TranslationTexCoord", Vector2D(0.0,0.0));
    _programState.setUniformValue("ViewPortExtent", Vector2D(0.0,0.0));
    
    _programState.setUniformValue("ColorPerVertexIntensity", (float)0.0);
    _programState.setUniformValue("EnableFlatColor", false);
    _programState.setUniformValue("FlatColor", (float)0.0, (float)0.0, (float)0.0, (float)0.0);
    _programState.setUniformValue("FlatColorIntensity", (float)0.0);
    
    _programState.setAttributeEnabled("TextureCoord", false);
    _programState.setAttributeEnabled("Color", false);
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
  
  virtual ~BusyQuadRenderer() {
    delete _quadMesh;
    delete _backgroundColor;
  }
  
  void incDegrees(double value) {
    _degrees += value;
    if (_degrees>360) _degrees -= 360;
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
