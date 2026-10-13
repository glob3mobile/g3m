//
//  HUDQuadWidget.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#include "HUDQuadWidget.hpp"

#include "G3MContext.hpp"
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
#include "SimpleTextureMapping.hpp"
#include "MultiTextureMapping.hpp"
#include "TextureIDReference.hpp"
#include "G3MRenderContext.hpp"
#include "ILogger.hpp"
#include "GLConstants.hpp"
#include "IImage.hpp"
#include "GLConstants.hpp"


HUDQuadWidget::~HUDQuadWidget() {
  delete _image;
  delete _backgroundImage;

  delete _mesh;

  delete _xPosition;
  delete _yPosition;

  delete _widthSize;
  delete _heightSize;
}

Mesh* HUDQuadWidget::createMesh(const G3MRenderContext* rc) {
  if (_image->getImage() == NULL) {
    return NULL;
  }

  const bool hasBackground = (_backgroundImage != NULL);

  if (hasBackground && (_backgroundImage->getImage() == NULL)) {
    return NULL;
  }

  TexturesHandler* texturesHandler = rc->getTexturesHandler();

  const TextureIDReference* textureID = texturesHandler->getTextureIDReference(_image->getImage(),
                                                                               GLFormat::rgba(),
                                                                               _image->getImageName(),
                                                                               false,
                                                                               GLTextureParameterValue::clampToEdge(),
                                                                               GLTextureParameterValue::clampToEdge());
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
    backgroundTextureID = texturesHandler->getTextureIDReference(_backgroundImage->getImage(),
                                                                 GLFormat::rgba(),
                                                                 _backgroundImage->getImageName(),
                                                                 false,
                                                                 GLTextureParameterValue::clampToEdge(),
                                                                 GLTextureParameterValue::clampToEdge());

    if (backgroundTextureID == NULL) {
      delete textureID;
      rc->getLogger()->logError("Can't upload background texture to GPU");
      return NULL;
    }
  }

  const Camera* camera = rc->getCurrentCamera();
  int viewPortWidth = camera->getViewPortWidth();
  if (rc->getViewMode() == STEREO) {
    viewPortWidth /= 2;
  }
  const int viewPortHeight = camera->getViewPortHeight();

  const int imageWidth  = _image->getImageWidth();
  const int imageHeight = _image->getImageHeight();

  const float width  = _widthSize->getSize(viewPortWidth, viewPortHeight, imageWidth, imageHeight);
  const float height = _heightSize->getSize(viewPortWidth, viewPortHeight, imageWidth, imageHeight);

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
                                              _texCoordsTranslationU, _texCoordsTranslationV,
                                              _texCoordsScaleU, _texCoordsScaleV,
                                              _texCoordsRotationInRadians,
                                              _texCoordsRotationCenterU, _texCoordsRotationCenterV);
  }
  else {
    _textureMapping = new SimpleTextureMapping(textureID,
                                               texCoords.create(),
                                               true,
                                               true,
                                               _texCoordsTranslationU, _texCoordsTranslationV,
                                               _texCoordsScaleU, _texCoordsScaleV,
                                               _texCoordsRotationInRadians,
                                               _texCoordsRotationCenterU, _texCoordsRotationCenterV);
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
  _texCoordsRotationCenterU   = centerU;
  _texCoordsRotationCenterV   = centerV;

  if (_textureMapping != NULL) {
    _textureMapping->setRotation(_texCoordsRotationInRadians,
                                 _texCoordsRotationCenterU,
                                 _texCoordsRotationCenterV);
  }
}

void HUDQuadWidget::initialize(const G3MContext* context) {
  _context = context;

  _image->initialize(context, this);
  if (_backgroundImage != NULL) {
    _backgroundImage->initialize(context, this);
  }
}

void HUDQuadWidget::cleanMesh() {
  _textureMapping = NULL; // just nullify the pointer, the instance will be deleted from the Mesh destructor

  delete _mesh;
  _mesh = NULL;
}

void HUDQuadWidget::changed() {
  cleanMesh();

  _image->rebuild(_context);
  if (_backgroundImage != NULL) {
    _backgroundImage->rebuild(_context);
  }
}

void HUDQuadWidget::addErrors(const HUDWidgetImage* widgetImage) {
  const std::vector<std::string> errors = widgetImage->getErrors();
  for (size_t i = 0; i < errors.size(); i++) {
    _errors.push_back("HUDQuadWidget: " + errors[i]);
  }
}

void HUDQuadWidget::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width,
                                          int height) {
  cleanMesh();
}

RenderState HUDQuadWidget::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  addErrors(_image);
  if (_backgroundImage != NULL) {
    addErrors(_backgroundImage);
  }

  if (!_errors.empty()) {
    return RenderState::error(_errors);
  }
  else if (_image->isBuilding() || ((_backgroundImage != NULL) && _backgroundImage->isBuilding())) {
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
