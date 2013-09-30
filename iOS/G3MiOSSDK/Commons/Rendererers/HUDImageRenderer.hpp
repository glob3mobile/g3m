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
#include "IImageListener.hpp"
class Mesh;



class HUDImageRenderer : public LeafRenderer {
public:

  class ImageFactory {
//  private:
//    HUDImageRenderer* _hudImageRenderer;
//
//  protected:
//    HUDImageRenderer* getListener() const {
//      return _hudImageRenderer;
//    }

  public:
    ImageFactory()
//    _hudImageRenderer(NULL)
    {
    }

#ifdef C_CODE
    virtual ~ImageFactory() { }
#endif
#ifdef JAVA_CODE
    public void dispose();
#endif

//    void setListener(HUDImageRenderer* hudImageRenderer) {
//      if (_hudImageRenderer != NULL) {
//        ILogger::instance()->logError("Listener already set");
//      }
//      _hudImageRenderer = hudImageRenderer;
//    }

    virtual void create(const G3MRenderContext* rc,
                        int width,
                        int height,
                        IImageListener* listener,
                        bool deleteListener) = 0;
  };

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

  GLState*                        _glState;
  Mesh*                           _mesh;
  HUDImageRenderer::ImageFactory* _imageFactory;
  bool                            _creatingMesh;

  Mesh* getMesh(const G3MRenderContext* rc);
  Mesh* createMesh(const G3MRenderContext* rc);

#ifdef C_CODE
  const IImage* _image;
#endif
#ifdef JAVA_CODE
  private IImage _image;
#endif
  void setImage(const IImage* image);

protected:
  void recreateImage();

  HUDImageRenderer::ImageFactory* getImageFactory() const {
    return _imageFactory;
  }

public:
  HUDImageRenderer(HUDImageRenderer::ImageFactory* imageFactory);

  void initialize(const G3MContext* context) {}

  RenderState getRenderState(const G3MRenderContext* rc) {
    return RenderState::ready();
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
