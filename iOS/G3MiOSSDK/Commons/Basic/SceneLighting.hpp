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
public:

  void modifyGLState(GLState* glState, const G3MRenderContext* rc);

};

#endif
