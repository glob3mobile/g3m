//
//  BusyQuadRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 13/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_BusyQuadRenderer
#define G3MiOSSDK_BusyQuadRenderer


#include "ProtoRenderer.hpp"
#include "Effects.hpp"
#include "Vector2D.hpp"
#include "Color.hpp"
#include "DirectMesh.hpp"
#include "MutableMatrix44D.hpp"


//***************************************************************


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
                             int width, int height) {
    const int halfWidth = width / 2;
    const int halfHeight = height / 2;
    _projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth, halfWidth,
                                                                             -halfHeight, halfHeight,
                                                                             -halfWidth, halfWidth);
  }

  virtual ~BusyQuadRenderer() {
    //rc->getFactory()->deleteImage(_image);
    //_image = NULL;
    delete _image;
    delete _quadMesh;
    delete _backgroundColor;

    _glState->_release();
  }
  
  void incDegrees(double value) {
    _degrees += value;
    if (_degrees>360) _degrees -= 360;
    _modelviewMatrix = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, 1));
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

  void start(const G3MRenderContext* rc,
             const TimeInterval& when) {}

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when) {
    EffectWithForce::doStep(rc, when);
    _renderer->incDegrees(3);
  }

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when) { }

  void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }

};



#endif
