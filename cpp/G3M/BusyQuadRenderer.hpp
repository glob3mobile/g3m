//
//  BusyQuadRenderer.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 13/08/12.
//

#ifndef G3M_BusyQuadRenderer
#define G3M_BusyQuadRenderer


#include "ProtoRenderer.hpp"
#include "Effects.hpp"
#include "Vector2D.hpp"
#include "MutableMatrix44D.hpp"
#include "GLState.hpp"

class IImage;
class Mesh;
class Color;


class BusyQuadRenderer : public ProtoRenderer, EffectTarget {
private:
  double      _degrees;
  //  const std::string _textureFilename;
  IImage*     _image;
  Mesh*       _quadMesh;
  
  const bool      _animated;
  const Vector2D  _size;
  Color*          _backgroundColor;
  
  bool initMesh(const G3MRenderContext* rc);
  
  mutable MutableMatrix44D _modelviewMatrix;
  MutableMatrix44D _projectionMatrix;
  
  GLState* _glState;
  void createGLState();
  
  
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
  _projectionMatrix(MutableMatrix44D::invalid()),
  _glState(new GLState())
  {
    createGLState();
  }
  
  void initialize(const G3MContext* context) {
    
  }
  
  void render(const G3MRenderContext* rc,
              GLState* glState);
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);
  
  virtual ~BusyQuadRenderer();
  
  void incDegrees(double value);
  
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

class BusyEffect : public EffectNeverEnding {
private:
  BusyQuadRenderer* _renderer;

public:

  BusyEffect(BusyQuadRenderer *renderer):
  EffectNeverEnding(),
  _renderer(renderer)
  { }

  void start(const G3MRenderContext* rc,
             const TimeInterval& when) {}

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when) {
    EffectNeverEnding::doStep(rc, when);
    _renderer->incDegrees(3);
  }

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when) { }

  void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }

};



#endif
