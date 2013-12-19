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

void HUDQuadWidget::setTexCoordsTranslation(const Vector2D& translation) {
  _texCoordsTranslationX = translation._x;
  _texCoordsTranslationY = translation._y;
#warning update mesh
  delete _mesh;
  _mesh = NULL;
}

void HUDQuadWidget::setTexCoordsScale(const Vector2D& scale) {
  _texCoordsScaleX = scale._x;
  _texCoordsScaleY = scale._y;
#warning update mesh
  delete _mesh;
  _mesh = NULL;
}

void HUDQuadWidget::setTexCoordsRotation(const Angle& rotation,
                                         const Vector2D& center) {
  _texCoordsRotationInRadians = rotation._radians;

  _texCoordsRotationCenterX = center._x;
  _texCoordsRotationCenterY = center._y;

#warning update mesh
  delete _mesh;
  _mesh = NULL;
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

  const TextureIDReference* texId = rc->getTexturesHandler()->getTextureIDReference(_image,
                                                                                    GLFormat::rgba(),
                                                                                    _imageURL.getPath(),
                                                                                    false);

  if (texId == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }

  const double x = _x;
  const double y = _y;
  const double width = _width;
  const double height = _height;

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  vertices->add( x,       height+y, 0 );
  vertices->add( x,       y,        0 );
  vertices->add( width+x, height+y, 0 );
  vertices->add( width+x, y,        0 );

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add( 0, 0 );
  texCoords.add( 0, 1 );
  texCoords.add( 1, 0 );
  texCoords.add( 1, 1 );

  DirectMesh* dm = new DirectMesh(GLPrimitive::triangleStrip(),
                                  true,
                                  vertices->getCenter(),
                                  vertices->create(),
                                  1,
                                  1);

  delete vertices;

  SimpleTextureMapping* texMap = new SimpleTextureMapping(texId,
                                                          texCoords.create(),
                                                          true,
                                                          true);
  texMap->setTranslationAndScale(Vector2D(_texCoordsTranslationX, _texCoordsTranslationY),
                                 Vector2D(_texCoordsScaleX, _texCoordsScaleY));
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
