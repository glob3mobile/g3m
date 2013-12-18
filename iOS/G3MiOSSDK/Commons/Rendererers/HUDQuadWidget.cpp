//
//  HUDQuadWidget.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#include "HUDQuadWidget.hpp"

#include "Context.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"
#include "TexturesHandler.hpp"
#include "Camera.hpp"
#include "Vector3D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "DirectMesh.hpp"
#include "TexturedMesh.hpp"


class HUDQuadWidget_ImageDownloadListener : public IImageDownloadListener {
private:
  HUDQuadWidget* _quadWidget;

public:
  HUDQuadWidget_ImageDownloadListener(HUDQuadWidget* quadWidget) :
  _quadWidget(quadWidget)
  {
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired)  {
    _quadWidget->onImageDownload(image);
  }

  void onError(const URL& url) {
    _quadWidget->onImageDownloadError(url);
  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }

};


HUDQuadWidget::~HUDQuadWidget() {
  delete _image;
  delete _mesh;
}

void HUDQuadWidget::initialize(const G3MContext* context) {
  if (!_downloadingImage && (_image == NULL)) {
    _downloadingImage = true;
    IDownloader* downloader = context->getDownloader();
    downloader->requestImage(_imageURL,
                             1000000, // priority
                             TimeInterval::fromDays(30),
                             true, // readExpired
                             new HUDQuadWidget_ImageDownloadListener(this),
                             true);
  }
}

void HUDQuadWidget::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width,
                                          int height) {
  delete _mesh;
  _mesh = NULL;
}

void HUDQuadWidget::onImageDownload(IImage* image) {
  _downloadingImage = false;
  _image = image;
}

void HUDQuadWidget::onImageDownloadError(const URL& url) {
  _errors.push_back("HUDQuadWidget: Error downloading \"" + url.getPath() + "\"");
}

RenderState HUDQuadWidget::getRenderState(const G3MRenderContext* rc) {
  if (!_errors.empty()) {
    return RenderState::error(_errors);
  }
  else if (_downloadingImage) {
    return RenderState::busy();
  }
  else {
    return RenderState::ready();
  }
}

Mesh* HUDQuadWidget::createMesh(const G3MRenderContext* rc) const {
  if (_image == NULL) {
    return NULL;
  }

  //#ifdef C_CODE
  //  const TextureIDReference* texId;
  //#endif
  //#ifdef JAVA_CODE
  //  TextureIDReference texId;
  //#endif

  const TextureIDReference* texId = rc->getTexturesHandler()->getTextureIDReference(_image,
                                                                                    GLFormat::rgba(),
                                                                                    _imageURL.getPath(),
                                                                                    false);

  if (texId == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }

  const Camera* camera = rc->getCurrentCamera();
  const int viewportWidth  = camera->getWidth();
  const int viewportHeight = camera->getHeight();

  const Vector3D halfViewportAndPosition(viewportWidth  / 2 - _x,
                                         viewportHeight / 2 - _y,
                                         0);

  const double w = _width;
  const double h = _height;

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  vertices->add( Vector3D(0, h, 0).sub(halfViewportAndPosition) );
  vertices->add( Vector3D(0, 0, 0).sub(halfViewportAndPosition) );
  vertices->add( Vector3D(w, h, 0).sub(halfViewportAndPosition) );
  vertices->add( Vector3D(w, 0, 0).sub(halfViewportAndPosition) );

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add(0, 0);
  texCoords.add(0, 1);
  texCoords.add(1, 0);
  texCoords.add(1, 1);

  DirectMesh* dm = new DirectMesh(GLPrimitive::triangleStrip(),
                                  true,
                                  vertices->getCenter(),
                                  vertices->create(),
                                  1,
                                  1);

  delete vertices;

  TextureMapping* texMap = new SimpleTextureMapping(texId,
                                                    texCoords.create(),
                                                    true,
                                                    true);

  return new TexturedMesh(dm, true, texMap, true, true);
}

Mesh* HUDQuadWidget::getMesh(const G3MRenderContext* rc) {
  if (_mesh == NULL) {
    _mesh = createMesh(rc);
  }
  return _mesh;
}

void HUDQuadWidget::rawRender(const G3MRenderContext* rc,
                              GLState* glState) {
  Mesh* mesh = getMesh(rc);
  if (mesh != NULL) {
    mesh->render(rc, glState);
  }
}
