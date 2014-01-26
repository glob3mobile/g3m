//
//  MultiTexturedHUDQuadWidget.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#include "MultiTexturedHUDQuadWidget.hpp"

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
#include "HUDPosition.hpp"
#include "RenderState.hpp"

class MultiTexturedHUDQuadWidget_ImageDownloadListener : public IImageDownloadListener {
private:
  MultiTexturedHUDQuadWidget* _quadWidget;
public:
  MultiTexturedHUDQuadWidget_ImageDownloadListener(MultiTexturedHUDQuadWidget* quadWidget) :
  _quadWidget(quadWidget)
  {
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired)  {
    _quadWidget->onImageDownload(image, url);
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


MultiTexturedHUDQuadWidget::~MultiTexturedHUDQuadWidget() {
  delete _image1;
  delete _image2;

  delete _mesh;

  delete _x;
  delete _y;
}

Mesh* MultiTexturedHUDQuadWidget::createMesh(const G3MRenderContext* rc) {
  if ((_image1 == NULL) ||
      (_image2 == NULL)) {
    return NULL;
  }

  const TextureIDReference* texId = rc->getTexturesHandler()->getTextureIDReference(_image1,
                                                                                    GLFormat::rgba(),
                                                                                    _imageURL1.getPath(),
                                                                                    false);

  if (texId == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }

  const TextureIDReference* texId2 = rc->getTexturesHandler()->getTextureIDReference(_image2,
                                                                                     GLFormat::rgba(),
                                                                                     _imageURL2.getPath(),
                                                                                     false);

  if (texId2 == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }

  const Camera* camera = rc->getCurrentCamera();
  const int viewPortWidth  = camera->getWidth();
  const int viewPortHeight = camera->getHeight();

  const float width  = _width;
  const float height = _height;
  const float x = _x->getPosition(viewPortWidth, viewPortHeight, width, height);
  const float y = _y->getPosition(viewPortWidth, viewPortHeight, width, height);

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

  _mtMapping = new MultiTextureMapping(texId,
                                       texCoords.create(),
                                       true,
                                       true,
                                       texId2,
                                       texCoords.create(),
                                       true,
                                       true,
                                       _texCoordsTranslationU,
                                       _texCoordsTranslationV,
                                       _texCoordsScaleU,
                                       _texCoordsScaleV,
                                       _texCoordsRotationInRadians,
                                       _texCoordsRotationCenterU,
                                       _texCoordsRotationCenterV);

  return new TexturedMesh(dm, true, _mtMapping, true, true);
}

void MultiTexturedHUDQuadWidget::setTexCoordsTranslation(float u, float v) {
  _texCoordsTranslationU = u;
  _texCoordsTranslationV = v;

  if (_mtMapping != NULL) {
    _mtMapping->setTranslation(_texCoordsTranslationU,
                               _texCoordsTranslationV);
  }
}

void MultiTexturedHUDQuadWidget::setTexCoordsScale(float u, float v) {
  _texCoordsScaleU = u;
  _texCoordsScaleV = v;

  if (_mtMapping != NULL) {
    _mtMapping->setScale(_texCoordsScaleU,
                         _texCoordsScaleV);
  }
}

void MultiTexturedHUDQuadWidget::setTexCoordsRotation(float angleInRadians,
                                                      float centerU,
                                                      float centerV) {
  _texCoordsRotationInRadians = angleInRadians;
  _texCoordsRotationCenterU = centerU;
  _texCoordsRotationCenterV = centerV;

  if (_mtMapping != NULL) {
    _mtMapping->setRotation(_texCoordsRotationInRadians,
                            _texCoordsRotationCenterU,
                            _texCoordsRotationCenterV);
  }
}


void MultiTexturedHUDQuadWidget::initialize(const G3MContext* context) {
  if (!_downloadingImage && (_image1 == NULL) && (_image2 == NULL)) {
    _downloadingImage = true;
    IDownloader* downloader = context->getDownloader();
    downloader->requestImage(_imageURL1,
                             1000000, // priority
                             TimeInterval::fromDays(30),
                             true, // readExpired
                             new MultiTexturedHUDQuadWidget_ImageDownloadListener(this),
                             true);

    downloader->requestImage(_imageURL2,
                             1000000, // priority
                             TimeInterval::fromDays(30),
                             true, // readExpired
                             new MultiTexturedHUDQuadWidget_ImageDownloadListener(this),
                             true);
  }
}

void MultiTexturedHUDQuadWidget::cleanMesh() {
  _mtMapping = NULL;

  delete _mesh;
  _mesh = NULL;
}

void MultiTexturedHUDQuadWidget::onResizeViewportEvent(const G3MEventContext* ec,
                                                       int width,
                                                       int height) {
  cleanMesh();
}

void MultiTexturedHUDQuadWidget::onImageDownload(IImage* image, const URL& url) {
  if (url.isEquals(_imageURL1)) {
    _image1 = image;
  }
  if (url.isEquals(_imageURL2)) {
    _image2 = image;
  }

  if ((_image1 != NULL) &&
      (_image2 != NULL)) {
    _downloadingImage = false;
  }
}

void MultiTexturedHUDQuadWidget::onImageDownloadError(const URL& url) {
  _errors.push_back("MultiTexturedHUDQuadWidget: Error downloading \"" + url.getPath() + "\"");
}

RenderState MultiTexturedHUDQuadWidget::getRenderState(const G3MRenderContext* rc) {
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

Mesh* MultiTexturedHUDQuadWidget::getMesh(const G3MRenderContext* rc) {
  if (_mesh == NULL) {
    _mesh = createMesh(rc);
  }
  return _mesh;
}

void MultiTexturedHUDQuadWidget::rawRender(const G3MRenderContext* rc,
                                           GLState* glState) {
  Mesh* mesh = getMesh(rc);
  if (mesh != NULL) {
    mesh->render(rc, glState);
  }
}
