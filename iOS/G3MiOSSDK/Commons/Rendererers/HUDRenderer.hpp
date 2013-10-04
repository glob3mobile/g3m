//
//  HUDRendererer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/09/13.
//
//

#ifndef __G3MiOSSDK__HUDRendererer__
#define __G3MiOSSDK__HUDRendererer__

#include "LeafRenderer.hpp"
#include "Vector2D.hpp"
#include <vector>

class IImage;
class Mesh;
class IFactory;

class HUDRenderer : public LeafRenderer {
private:

  class ShownImage {
  private:
    const std::string _name;
    mutable IImage*   _image;
    const Vector2D    _size;
    const Vector2D    _position;

    mutable const IFactory* _factory;
    mutable Mesh*           _mesh;
    Mesh* createMesh(const G3MRenderContext* rc) const;

  public:
    ShownImage(const std::string& name,
               IImage*            image,
               const Vector2D&    size,
               const Vector2D&    position):
    _name(name),
    _image(image),
    _size(size),
    _position(position),
    _mesh(NULL),
    _factory(NULL)
    {
    }

    Mesh* getMesh(const G3MRenderContext* rc) const {
      if (_mesh == NULL) {
        _mesh = createMesh(rc);
      }
      return _mesh;
    }

    ~ShownImage();

    void clearMesh();

  };


  GLState*                 _glState;
  std::vector<ShownImage*> _images;

public:
  HUDRenderer();

  void addImage(const std::string name,
                IImage* image,
                const Vector2D& size,
                const Vector2D position) {
    _images.push_back(new ShownImage(name, image, size, position));
  }

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

  virtual ~HUDRenderer();

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
