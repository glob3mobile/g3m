//
//  BusyMeshRenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 20/07/12.
//

#ifndef G3M_BusyMeshRenderer
#define G3M_BusyMeshRenderer

#include "ProtoRenderer.hpp"
#include "IndexedMesh.hpp"
#include "Effects.hpp"
#include "Color.hpp"
#include "GLState.hpp"


class BusyMeshRenderer : public ProtoRenderer, EffectTarget {
private:
  Mesh    *_mesh;
  double  _degrees;
  const Color _backgroundColor;
  const Color _meshOuterColor;
  const Color _meshInnerColor;

  MutableMatrix44D _projectionMatrix;
  mutable MutableMatrix44D _modelviewMatrix;


  ProjectionGLFeature* _projectionFeature;
  ModelGLFeature* _modelFeature;

  GLState* _glState;

  void createGLState();

  Mesh* createMesh(const G3MRenderContext* rc);
  Mesh* getMesh(const G3MRenderContext* rc);


public:
  BusyMeshRenderer(const Color& backgroundColor,
                   const Color& meshOuterColor,
                   const Color& meshInnerColor):
  _degrees(0),
  _backgroundColor(backgroundColor),
  _meshOuterColor(meshOuterColor),
  _meshInnerColor(meshInnerColor),
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
                             int width, int height);

  virtual ~BusyMeshRenderer() {
    delete _mesh;

    _glState->_release();
  }

  void incDegrees(double value) {
    _degrees += value;
    if (_degrees>360) {
      _degrees -= 360;
    }
    _modelviewMatrix.copyValue(MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees),
                                                                      Vector3D(0, 0, -1)));
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

class BusyMeshEffect : public EffectNeverEnding {
private:
  BusyMeshRenderer* _renderer;
  long long _lastMS;

public:

  BusyMeshEffect(BusyMeshRenderer *renderer):
  EffectNeverEnding(),
  _renderer(renderer)
  { }

  void start(const G3MRenderContext* rc,
             const TimeInterval& when);

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when) { }

  void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
};


#endif
