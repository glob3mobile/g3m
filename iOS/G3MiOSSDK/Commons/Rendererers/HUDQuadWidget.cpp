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
#include "SimpleTextureMapping.hpp"
#include "MultiTextureMapping.hpp"
#include "TextureIDReference.hpp"

class HUDQuadWidget_ImageBuilderListener : public IImageBuilderListener {
private:
  HUDQuadWidget* _quadWidget;
  const int      _imageRole;

public:
  HUDQuadWidget_ImageBuilderListener(HUDQuadWidget* quadWidget,
                                     int            imageRole) :
  _quadWidget(quadWidget),
  _imageRole(imageRole)
  {
  }

  ~HUDQuadWidget_ImageBuilderListener() {
  }

  void imageCreated(const IImage* image,
                    const std::string& imageName) {
    _quadWidget->imageCreated(image, imageName, _imageRole);
  }

  void onError(const std::string& error) {
    _quadWidget->onImageBuildError(error, _imageRole);
  }
};


HUDQuadWidget::~HUDQuadWidget() {
  delete _imageBuilder;
  delete _backgroundImageBuilder;

  delete _image;
  delete _backgroundImage;

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

  const bool hasBackground = (_backgroundImageBuilder != NULL);

  if (hasBackground && (_backgroundImage == NULL)) {
    return NULL;
  }

  TexturesHandler* texturesHandler = rc->getTexturesHandler();

  const TextureIDReference* textureID = texturesHandler->getTextureIDReference(_image,
                                                                               GLFormat::rgba(),
                                                                               _imageName,
                                                                               false);
  if (textureID == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }

#ifdef C_CODE
  const TextureIDReference* backgroundTextureID = NULL;
#endif
#ifdef JAVA_CODE
  TextureIDReference backgroundTextureID = null;
#endif
  if (hasBackground) {
    backgroundTextureID = texturesHandler->getTextureIDReference(_backgroundImage,
                                                                 GLFormat::rgba(),
                                                                 _backgroundImageName,
                                                                 false);

    if (backgroundTextureID == NULL) {
      delete textureID;

      rc->getLogger()->logError("Can't background upload texture to GPU");
      return NULL;
    }
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

  if (hasBackground) {
    _textureMapping = new MultiTextureMapping(textureID,
                                              texCoords.create(),
                                              true,
                                              true,
                                              backgroundTextureID,
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
  }
  else {
    _textureMapping = new SimpleTextureMapping(textureID,
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
  }

  return new TexturedMesh(dm, true, _textureMapping, true, true);
}

void HUDQuadWidget::setTexCoordsTranslation(float u, float v) {
  _texCoordsTranslationU = u;
  _texCoordsTranslationV = v;

  if (_textureMapping != NULL) {
    _textureMapping->setTranslation(_texCoordsTranslationU,
                                    _texCoordsTranslationV);
  }
}

void HUDQuadWidget::setTexCoordsScale(float u, float v) {
  _texCoordsScaleU = u;
  _texCoordsScaleV = v;

  if (_textureMapping != NULL) {
    _textureMapping->setScale(_texCoordsScaleU,
                              _texCoordsScaleV);
  }
}

void HUDQuadWidget::setTexCoordsRotation(float angleInRadians,
                                         float centerU,
                                         float centerV) {
  _texCoordsRotationInRadians = angleInRadians;
  _texCoordsRotationCenterU = centerU;
  _texCoordsRotationCenterV = centerV;

  if (_textureMapping != NULL) {
    _textureMapping->setRotation(_texCoordsRotationInRadians,
                                 _texCoordsRotationCenterU,
                                 _texCoordsRotationCenterV);
  }
}


void HUDQuadWidget::initialize(const G3MContext* context) {
  _context = context;

  if (!_buildingImage && (_image == NULL)) {
    _buildingImage = true;
    _imageBuilder->build(context,
                         new HUDQuadWidget_ImageBuilderListener(this, 0),
                         true);
    if (_imageBuilder->isMutable()) {
      _imageBuilder->setChangeListener( this );
    }
  }

  if (_backgroundImageBuilder != NULL) {
    if (!_buildingBackgroundImage && (_backgroundImage == NULL)) {
      _buildingBackgroundImage = true;
      _backgroundImageBuilder->build(context,
                                     new HUDQuadWidget_ImageBuilderListener(this, 1),
                                     true);
      if (_backgroundImageBuilder->isMutable()) {
        _backgroundImageBuilder->setChangeListener( this );
      }
    }
  }
}

void HUDQuadWidget::cleanMesh() {
  _textureMapping = NULL; // just nullify the pointer, the instance will be deleted from the Mesh destructor

  delete _mesh;
  _mesh = NULL;
}

void HUDQuadWidget::changed() {
//#warning Diego at work!
  cleanMesh();

  delete _image;
  _image = NULL;
  _imageName = "";
  _imageWidth = 0;
  _imageHeight = 0;

  _buildingImage = true;
  _imageBuilder->build(_context,
                       new HUDQuadWidget_ImageBuilderListener(this, 0),
                       true);

  delete _backgroundImage;
  _backgroundImage = NULL;
  _backgroundImageName = "";

  if (_backgroundImageBuilder != NULL) {
    _buildingBackgroundImage = true;
    _backgroundImageBuilder->build(_context,
                                   new HUDQuadWidget_ImageBuilderListener(this, 1),
                                   true);
  }
}

void HUDQuadWidget::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width,
                                          int height) {
  cleanMesh();
}

void HUDQuadWidget::imageCreated(const IImage*      image,
                                 const std::string& imageName,
                                 int                imageRole) {

  if (imageRole == 0) {
    _buildingImage = false;
    _image = image;
    _imageName = imageName;
    _imageWidth  = _image->getWidth();
    _imageHeight = _image->getHeight();
  }
  else if (imageRole == 1) {
    _buildingBackgroundImage = false;
    _backgroundImage = image;
    _backgroundImageName = imageName;
  }

  //  delete _imageBuilder;
  //  _imageBuilder = NULL;
}

void HUDQuadWidget::onImageBuildError(const std::string& error,
                                      int                imageRole) {
  _errors.push_back("HUDQuadWidget: " + error);

  //  if (imageRole == 0) {
  //    _buildingImage0 = false;
  //  }
  //  else if (imageRole == 1) {
  //    _buildingBackgroundImage = false;
  //  }

  //  delete _imageBuilder;
  //  _imageBuilder = NULL;
}

RenderState HUDQuadWidget::getRenderState(const G3MRenderContext* rc) {
  if (!_errors.empty()) {
    return RenderState::error(_errors);
  }
  else if (_buildingImage || _buildingBackgroundImage) {
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
