//
//  AtmosphereRenderer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//

#ifndef AtmosphereRenderer_hpp
#define AtmosphereRenderer_hpp

#include <stdio.h>

#include "DefaultRenderer.hpp"

#include "Color.hpp"

class Mesh;
class IFloatBuffer;
class CameraPositionGLFeature;
class Camera;


class AtmosphereRenderer : public DefaultRenderer {
private:
  GLState* _glState;

  Mesh* _directMesh;

  IFloatBuffer* _vertices;
  CameraPositionGLFeature* _camPosGLF;

  void updateGLState(const Camera* camera);

  Color _blueSky;
  Color _darkSpace;
  bool _overPresicionThreshold;
  const double _minHeight;

public:
  AtmosphereRenderer();

  void start(const G3MRenderContext* rc);

  void removeAllTrails(bool deleteTrails = true);

  ~AtmosphereRenderer();

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void render(const G3MRenderContext* rc, GLState* glState);

};


#endif /* AtmosphereRenderer_hpp */
