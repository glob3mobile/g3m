//
//  HUDImageRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//

#ifndef __G3MiOSSDK__HUDImageRenderer__
#define __G3MiOSSDK__HUDImageRenderer__

#include "LeafRenderer.hpp"
#include "Vector2D.hpp"
class Mesh;
class ImageFactory;
#include "IImageListener.hpp"

class HUDImageRenderer : public LeafRenderer {
private:

  class ImageListener : public IImageListener {
  private:
    HUDImageRenderer* _hudImageRenderer;

  public:
    ImageListener(HUDImageRenderer* hudImageRenderer) :
    _hudImageRenderer(hudImageRenderer)
    {
    }

    void imageCreated(const IImage* image);

  };

  GLState*      _glState;
  Mesh*         _mesh;
  ImageFactory* _imageFactory;
  bool          _creatingMesh;

  Mesh* getMesh(const G3MRenderContext* rc);
  Mesh* createMesh(const G3MRenderContext* rc);

#ifdef C_CODE
  const IImage* _image;
#endif
#ifdef JAVA_CODE
  private IImage _image;
#endif
  void setImage(const IImage* image);

public:
  HUDImageRenderer(ImageFactory* imageFactory);

  void initialize(const G3MContext* context) {}

  bool isReadyToRender(const G3MRenderContext* rc) {
    int __rendererState;
    return true;
  }

  void render(const G3MRenderContext* rc,
              GLState* glState);


  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  virtual ~HUDImageRenderer();

  void start(const G3MRenderContext* rc) {
  }

  void stop(const G3MRenderContext* rc) {
  }

  void onResume(const G3MContext* context) {
  }

  void onPause(const G3MContext* context) {
  }

  void onDestroy(const G3MContext* context) {
  }

};

#endif
