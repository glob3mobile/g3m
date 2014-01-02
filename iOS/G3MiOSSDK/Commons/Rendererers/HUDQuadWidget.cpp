//
//  HUDQuadWidget.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#include "HUDQuadWidget.hpp"

#include "Context.hpp"
#include "TexturesHandler.hpp"
#include "Camera.hpp"
#include "Vector3D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "DirectMesh.hpp"
#include "TexturedMesh.hpp"
#include "HUDPosition.hpp"
#include "HUDSize.hpp"
#include "RenderState.hpp"
#include "IImageBuilder.hpp"
#include "IImageBuilderListener.hpp"

class HUDQuadWidget_ImageBuilderListener : public IImageBuilderListener {
private:
  HUDQuadWidget* _quadWidget;

public:
  HUDQuadWidget_ImageBuilderListener(HUDQuadWidget* quadWidget) :
  _quadWidget(quadWidget)
  {
  }

  ~HUDQuadWidget_ImageBuilderListener() {
  }

  void imageCreated(const IImage* image,
                    const std::string& imageName) {
    _quadWidget->onImageDownload(image,
                                 imageName);
  }

  void onError(const std::string& error) {
    _quadWidget->onImageDownloadError(error);
  }

};


HUDQuadWidget::~HUDQuadWidget() {
  delete _imageBuilder;

  delete _image;
  delete _mesh;

  delete _xPosition;
  delete _yPosition;

  delete _widthSize;
  delete _heightSize;
}

Mesh* HUDQuadWidget::createMesh(const G3MRenderContext* rc) {
  if (_image == NULL) {
    return NULL;
  }

  const TextureIDReference* texId = rc->getTexturesHandler()->getTextureIDReference(_image,
                                                                                    GLFormat::rgba(),
                                                                                    //_imageURL.getPath(),
                                                                                    //_imageBuilder->getImageName(),
                                                                                    _imageName,
                                                                                    false);

  if (texId == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }
  const Camera* camera = rc->getCurrentCamera();
  const int viewPortWidth  = camera->getWidth();
  const int viewPortHeight = camera->getHeight();

  const float width  = _widthSize->getSize(viewPortWidth, viewPortHeight, _imageWidth, _imageHeight);
  const float height = _heightSize->getSize(viewPortWidth, viewPortHeight, _imageWidth, _imageHeight);

  const float x = _xPosition->getPosition(viewPortWidth, viewPortHeight, width, height);
  const float y = _yPosition->getPosition(viewPortWidth, viewPortHeight, width, height);

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

  _simpleTextureMapping = new SimpleTextureMapping(texId,
                                                   texCoords.create(),
                                                   true,
                                                   true);
  _simpleTextureMapping->setTranslation(_texCoordsTranslationU,
                                        _texCoordsTranslationV);

  _simpleTextureMapping->setScale(_texCoordsScaleU,
                                  _texCoordsScaleV);

  _simpleTextureMapping->setRotation(_texCoordsRotationInRadians,
                                     _texCoordsRotationCenterU,
                                     _texCoordsRotationCenterV);

  return new TexturedMesh(dm, true, _simpleTextureMapping, true, true);
}

void HUDQuadWidget::setTexCoordsTranslation(float u, float v) {
  _texCoordsTranslationU = u;
  _texCoordsTranslationV = v;

  if (_simpleTextureMapping != NULL) {
    _simpleTextureMapping->setTranslation(_texCoordsTranslationU,
                                          _texCoordsTranslationV);
  }
}

void HUDQuadWidget::setTexCoordsScale(float u, float v) {
  _texCoordsScaleU = u;
  _texCoordsScaleV = v;

  if (_simpleTextureMapping != NULL) {
    _simpleTextureMapping->setScale(_texCoordsScaleU,
                                    _texCoordsScaleV);
  }
}

void HUDQuadWidget::setTexCoordsRotation(float angleInRadians,
                                         float centerU,
                                         float centerV) {
  _texCoordsRotationInRadians = angleInRadians;
  _texCoordsRotationCenterU = centerU;
  _texCoordsRotationCenterV = centerV;

  if (_simpleTextureMapping != NULL) {
    _simpleTextureMapping->setRotation(_texCoordsRotationInRadians,
                                       _texCoordsRotationCenterU,
                                       _texCoordsRotationCenterV);
  }
}


void HUDQuadWidget::initialize(const G3MContext* context) {
  if (!_downloadingImage && (_image == NULL)) {
    _downloadingImage = true;
//    IDownloader* downloader = context->getDownloader();
//    downloader->requestImage(_imageURL,
//                             1000000, // priority
//                             TimeInterval::fromDays(30),
//                             true, // readExpired
//                             new HUDQuadWidget_ImageDownloadListener(this),
//                             true);
    _imageBuilder->build(context,
                         new HUDQuadWidget_ImageBuilderListener(this),
                         true);
  }
}

void HUDQuadWidget::cleanMesh() {
  _simpleTextureMapping = NULL;

  delete _mesh;
  _mesh = NULL;
}

void HUDQuadWidget::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width,
                                          int height) {
  cleanMesh();
}

void HUDQuadWidget::onImageDownload(const IImage*      image,
                                    const std::string& imageName) {
  _downloadingImage = false;
  _image = image;
  _imageName = imageName;
  _imageWidth  = _image->getWidth();
  _imageHeight = _image->getHeight();
  delete _imageBuilder;
  _imageBuilder = NULL;
}

void HUDQuadWidget::onImageDownloadError(const std::string& error) {
  _errors.push_back("HUDQuadWidget: Error downloading \"" + error + "\"");
  delete _imageBuilder;
  _imageBuilder = NULL;
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
