//
//  HUDBillboardWidget.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/12/26.
//
//

#include "HUDBillboardWidget.hpp"

#include "HUDWidgetImage.hpp"
#include "G3MRenderContext.hpp"
#include "G3MEventContext.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "GLState.hpp"
#include "GLFeature.hpp"
#include "GLConstants.hpp"
#include "TexturesHandler.hpp"
#include "TextureIDReference.hpp"
#include "IGLTextureID.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "MutableMatrix44D.hpp"
#include "Vector3D.hpp"
#include "RenderState.hpp"
#include "ILogger.hpp"
#include "IMathUtils.hpp"


HUDBillboardWidget::HUDBillboardWidget(IImageFactory* imageFactory,
                                       float          anchorU,
                                       float          anchorV) :
_image(new HUDWidgetImage(imageFactory)),
_anchorU(anchorU),
_anchorV(anchorV),
_x(0),
_y(0),
_width(0),
_height(0),
_alpha(1),
_mirrored(false),
_textureID(NULL),
_context(NULL),
_glState(NULL),
_billboardGLF(NULL),
_modelTransformGLF(NULL),
_textureGLF(NULL),
_viewportExtentGLF(NULL),
_texCoords(NULL)
{
}

HUDBillboardWidget::~HUDBillboardWidget() {
  releaseGLState();
  delete _texCoords;
  delete _image;
}

int HUDBillboardWidget::getImageWidth() const {
  return _image->getImageWidth();
}

int HUDBillboardWidget::getImageHeight() const {
  return _image->getImageHeight();
}

void HUDBillboardWidget::initialize(const G3MContext* context) {
  _context = context;
  _image->initialize(context, this);
}

void HUDBillboardWidget::changed() {
  releaseGLState();
  _image->rebuild(_context);
}

void HUDBillboardWidget::onResizeViewportEvent(const G3MEventContext* ec,
                                               int width,
                                               int height) {
  // the viewport extent is read when the GLState is created
  releaseGLState();
}

RenderState HUDBillboardWidget::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  const std::vector<std::string> imageErrors = _image->getErrors();
  for (size_t i = 0; i < imageErrors.size(); i++) {
    _errors.push_back("HUDBillboardWidget: " + imageErrors[i]);
  }

  if (!_errors.empty()) {
    return RenderState::error(_errors);
  }
  else if (_image->isBuilding()) {
    return RenderState::busy();
  }
  else {
    return RenderState::ready();
  }
}

void HUDBillboardWidget::releaseGLState() {
  if (_glState != NULL) {
    _glState->_release(); // releases its features
    _glState = NULL;
  }
  _billboardGLF      = NULL;
  _modelTransformGLF = NULL;
  _textureGLF        = NULL;
  _viewportExtentGLF = NULL;

  delete _textureID;
  _textureID = NULL;
}

bool HUDBillboardWidget::createGLState(const G3MRenderContext* rc) {
  _textureID = rc->getTexturesHandler()->getTextureIDReference(_image->getImage(),
                                                               GLFormat::rgba(),
                                                               _image->getImageName(),
                                                               false,
                                                               GLTextureParameterValue::clampToEdge(),
                                                               GLTextureParameterValue::clampToEdge());
  if (_textureID == NULL) {
    rc->getLogger()->logError("HUDBillboardWidget: can't upload texture to GPU");
    return false;
  }

  if (_texCoords == NULL) {
    // the corners in the order the Billboard shader expects (as MarksRenderer::getBillboardTexCoords)
    FloatBufferBuilderFromCartesian2D texCoords;
    texCoords.add(1, 1);
    texCoords.add(1, 0);
    texCoords.add(0, 1);
    texCoords.add(0, 0);
    _texCoords = texCoords.create();
  }

  _glState = new GLState();

  _billboardGLF = new BillboardGLFeature(_width,
                                         _height,
                                         _anchorU, _anchorV,
                                         _textureID->isPremultiplied());
  _glState->addGLFeature(_billboardGLF, false);

  _modelTransformGLF = new ModelTransformGLFeature(MutableMatrix44D::identity().asMatrix44D());
  _glState->addGLFeature(_modelTransformGLF, false);

  _viewportExtentGLF = new ViewportExtentGLFeature(rc->getCurrentCamera()->getViewPortWidth(rc->getViewMode()),
                                                   rc->getCurrentCamera()->getViewPortHeight());
  _glState->addGLFeature(_viewportExtentGLF, false);

  _textureGLF = new TextureGLFeature(_textureID->getID(),
                                     _texCoords,
                                     2,
                                     0,
                                     false,
                                     0,
                                     true,
                                     _textureID->getSourceBlendFactor(),
                                     GLBlendFactor::oneMinusSrcAlpha(),
                                     0, 0, // translation
                                     1, 1, // scale
                                     0,    // rotation
                                     0, 0);
  _glState->addGLFeature(_textureGLF, false);

  return true;
}

void HUDBillboardWidget::updateGLState(const G3MRenderContext* rc) {
  const IMathUtils* mu = IMathUtils::instance();

  // the HUD projection has y up
  const float hudY = rc->getCurrentCamera()->getViewPortHeight() - _y;
  const MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(Vector3D(_x, hudY, 0));
  _modelTransformGLF->setMatrix(translation.asMatrix44D());

  _billboardGLF->changeSize(mu->round(_width),
                            mu->round(_height));
  _billboardGLF->changeAlpha(_alpha);

  // the shader places the quad from its texture coordinates, so the mirror flips the image inside the quad and moves the anchor
  if (_mirrored) {
    _billboardGLF->changeAnchor(1 - _anchorU, _anchorV);
    _textureGLF->setTranslation(1, 0);
    _textureGLF->setScale(-1, 1);
  }
  else {
    _billboardGLF->changeAnchor(_anchorU, _anchorV);
    _textureGLF->setTranslation(0, 0);
    _textureGLF->setScale(1, 1);
  }
}

void HUDBillboardWidget::rawRender(const G3MRenderContext* rc,
                                   GLState* glState) {
  if (_image->getImage() == NULL) {
    return;
  }
  if ((_alpha <= 0) || (_width <= 0) || (_height <= 0)) {
    return;
  }

  if (_glState == NULL) {
    if (!createGLState(rc)) {
      return;
    }
  }

  updateGLState(rc);
  _glState->setParent(glState);

  rc->getGL()->drawArrays(GLPrimitive::triangleStrip(),
                          0,
                          4,
                          _glState,
                          *rc->getGPUProgramManager());
}
