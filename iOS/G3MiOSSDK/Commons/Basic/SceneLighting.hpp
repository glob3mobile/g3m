//
//  SceneLighting.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/08/13.
//
//

#ifndef __G3MiOSSDK__SceneLighting__
#define __G3MiOSSDK__SceneLighting__

class GLState;
class G3MRenderContext;
class Vector3D;
class MeshRenderer;
#include "Color.hpp"

class SceneLighting{
public:
  virtual ~SceneLighting() {}
  virtual void modifyGLState(GLState* glState, const G3MRenderContext* rc) = 0;
};

class FixedFocusSceneLighting: public SceneLighting {
public:

  void modifyGLState(GLState* glState, const G3MRenderContext* rc);
  
};


class CameraFocusSceneLighting: public SceneLighting {

  Color _ambientColor;
  Color _diffuseColor;

  MeshRenderer* _meshRenderer;

  double _cameraDirX, _cameraDirY, _cameraDirZ;
  double _upX, _upY, _upZ;

public:

  CameraFocusSceneLighting(const Color& ambient, const Color& diffuse);

  ~CameraFocusSceneLighting() {}

  void modifyGLState(GLState* glState, const G3MRenderContext* rc);

  void setLightDirectionsMeshRenderer(MeshRenderer* meshRenderer) {
    _meshRenderer = meshRenderer;
  }

};

#endif
