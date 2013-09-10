//
//  HUDRendererer.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/09/13.
//
//

#ifndef __G3MiOSSDK__HUDRendererer__
#define __G3MiOSSDK__HUDRendererer__

#include "LeafRenderer.hpp"
#include "Mesh.hpp"
#include "Vector3D.hpp"
#include "TexturesHandler.hpp"

class HUDRenderer : public LeafRenderer{
private:
  GLState* _glState;
  class ShownImage{

    Mesh* createMesh(const G3MRenderContext* rc) const;

    const std::string _name;
    mutable IImage*     _image;
    const Vector2D  _size;
    mutable Mesh* _mesh;
    mutable const IFactory* _factory;

    const Vector2D _pos;

  public:
    ShownImage(const std::string& name, IImage* image, const Vector2D size, const Vector2D pos):
    _image(image), _size(size), _mesh(NULL), _name(name), _factory(NULL), _pos(pos){}

    Mesh* getMesh(const G3MRenderContext* rc) const{
      if (_mesh == NULL){
        _mesh = createMesh(rc);
      }
      return _mesh;
    }

    ~ShownImage(){
      _factory->deleteImage(_image);
      delete _mesh;
    }

    void clearMesh(){
      delete _mesh;
      _mesh = NULL;
    }

  };

  std::vector<ShownImage*> _images;

public:
  HUDRenderer():
  _glState(new GLState())
  {
  }

  void addImage(const std::string name,
                IImage* image,
                const Vector2D& size,
                const Vector2D position){
    _images.push_back(new ShownImage(name, image, size, position) );
  }

  void initialize(const G3MContext* context) {}

  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }

  void render(const G3MRenderContext* rc,
              GLState* glState);


  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
    const int halfWidth = width / 2;
    const int halfHeight = height / 2;
    MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth, halfWidth,
                                                                             -halfHeight, halfHeight,
                                                                             -halfWidth, halfWidth);

    ProjectionGLFeature* pr = (ProjectionGLFeature*) _glState->getGLFeature(GLF_PROJECTION);
    if (pr != NULL){
      pr->setMatrix(projectionMatrix.asMatrix44D());
    } else{
      _glState->addGLFeature(new ProjectionGLFeature(projectionMatrix.asMatrix44D()), false);
    }

    const int size = _images.size();
    for (int i = 0; i < size; i++) {
      _images[i]->clearMesh();
    }
  }

  virtual ~HUDRenderer() {
    _glState->_release();

    const int size = _images.size();
    for (int i = 0; i < size; i++) {
      delete _images[i];
    }

#ifdef JAVA_CODE
    super.dispose();
#endif

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




#endif /* defined(__G3MiOSSDK__HUDRendererer__) */
