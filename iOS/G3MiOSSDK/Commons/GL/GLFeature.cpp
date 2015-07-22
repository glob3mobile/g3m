//
//  GLFeature.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

#include "GLFeature.hpp"
#include "Camera.hpp"
#include "Vector2F.hpp"

ViewportExtentGLFeature::ViewportExtentGLFeature(int viewportWidth,
                                                 int viewportHeight) :
GLFeature(NO_GROUP, GLF_VIEWPORT_EXTENT)
{
  _extent = new GPUUniformValueVec2FloatMutable(viewportWidth,
                                                viewportHeight);
  
  _values->addUniformValue(VIEWPORT_EXTENT,
                           _extent,
                           false);
}

ViewportExtentGLFeature::ViewportExtentGLFeature(const Camera* camera) :
GLFeature(NO_GROUP, GLF_VIEWPORT_EXTENT)
{
  _extent = new GPUUniformValueVec2FloatMutable(camera->getViewPortWidth(),
                                                camera->getViewPortHeight());
  
  _values->addUniformValue(VIEWPORT_EXTENT,
                           _extent,
                           false);
}

void ViewportExtentGLFeature::changeExtent(int viewportWidth,
                                           int viewportHeight){
  _extent->changeValue(viewportWidth, viewportHeight);
}

BillboardGLFeature::BillboardGLFeature(const Vector3D& position,
                                       float billboardWidth,
                                       float billboardHeight,
                                       float anchorU, float anchorV) :
GLFeature(NO_GROUP, GLF_BILLBOARD)
{
  
  _anchor = new GPUUniformValueVec2FloatMutable(anchorU, anchorV);
  _values->addUniformValue(BILLBOARD_ANCHOR,
                           _anchor,
                           false);
  
  
  _size = new GPUUniformValueVec2FloatMutable(billboardWidth, billboardHeight);
  _values->addUniformValue(TEXTURE_EXTENT,
                           _size,
                           false);
  
  _values->addUniformValue(BILLBOARD_POSITION,
                           new GPUUniformValueVec4Float((float) position._x,
                                                        (float) position._y,
                                                        (float) position._z,
                                                        1),
                           false);
}

void BillboardGLFeature::applyOnGlobalGLState(GLGlobalState* state)  const {
  state->disableDepthTest();
  state->disableCullFace();
  state->disablePolygonOffsetFill();
}

GeometryGLFeature::GeometryGLFeature(const IFloatBuffer* buffer,
                                     int arrayElementSize,
                                     int index,
                                     bool normalized,
                                     int stride,
                                     bool depthTestEnabled,
                                     bool cullFace, int culledFace,
                                     bool polygonOffsetFill,
                                     float polygonOffsetFactor,
                                     float polygonOffsetUnits,
                                     float lineWidth,
                                     bool needsPointSize,
                                     float pointSize) :
GLFeature(NO_GROUP, GLF_GEOMETRY),
_depthTestEnabled(depthTestEnabled),
_cullFace(cullFace),
_culledFace(culledFace),
_polygonOffsetFill(polygonOffsetFill),
_polygonOffsetFactor(polygonOffsetFactor),
_polygonOffsetUnits(polygonOffsetUnits),
_lineWidth(lineWidth)
{
  _position = new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized);
  _values->addAttributeValue(POSITION, _position, false);
  
  if (needsPointSize) {
    _values->addUniformValue(POINT_SIZE, new GPUUniformValueFloat(pointSize), false);
  }
}

void GeometryGLFeature::applyOnGlobalGLState(GLGlobalState* state) const {
  if (_depthTestEnabled) {
    state->enableDepthTest();
  }
  else {
    state->disableDepthTest();
  }
  
  if (_cullFace) {
    state->enableCullFace(_culledFace);
  }
  else {
    state->disableCullFace();
  }
  
  if (_polygonOffsetFill) {
    state->enablePolygonOffsetFill(_polygonOffsetFactor, _polygonOffsetUnits);
  }
  else {
    state->disablePolygonOffsetFill();
  }
  
  state->setLineWidth(_lineWidth);
}

GeometryGLFeature::~GeometryGLFeature() {
  //  _position->_release();
#ifdef JAVA_CODE
  super.dispose();
#endif
}

///////////////////////////////
Geometry2DGLFeature::Geometry2DGLFeature(IFloatBuffer* buffer,
                                         int arrayElementSize,
                                         int index,
                                         bool normalized,
                                         int stride,
                                         float lineWidth,
                                         bool needsPointSize,
                                         float pointSize,
                                         const Vector2F& translation) :
GLFeature(NO_GROUP, GLF_GEOMETRY),
_lineWidth(lineWidth)
{
  _position = new GPUAttributeValueVec2Float(buffer, arrayElementSize, index, stride, normalized);
  _values->addAttributeValue(POSITION_2D, _position, false);
  
  _translation =  new GPUUniformValueVec2FloatMutable(translation._x, translation._y);
  _values->addUniformValue(TRANSLATION_2D, _translation, false);
  
  if (needsPointSize) {
    _values->addUniformValue(POINT_SIZE, new GPUUniformValueFloat(pointSize), false);
  }
}

void Geometry2DGLFeature::applyOnGlobalGLState(GLGlobalState* state) const {
  state->enableCullFace(GLCullFace::front());
  state->setLineWidth(_lineWidth);
}

Geometry2DGLFeature::~Geometry2DGLFeature() {
  //  _position->_release();
#ifdef JAVA_CODE
  super.dispose();
#endif
}

/////////////////////////////////

void TextureGLFeature::createBasicValues(IFloatBuffer* texCoords,
                                         int arrayElementSize,
                                         int index,
                                         bool normalized,
                                         int stride) {
  GPUAttributeValueVec2Float* value = new GPUAttributeValueVec2Float(texCoords,
                                                                     arrayElementSize,
                                                                     index,
                                                                     stride,
                                                                     normalized);
  
  GPUUniformValueInt* texUnit = new GPUUniformValueInt(_target);
  
  switch (_target) {
    case 0:
      _values->addUniformValue(SAMPLER, texUnit, false);
      _values->addAttributeValue(TEXTURE_COORDS, value, false);
      break;
      
    case 1:
      _values->addUniformValue(SAMPLER2, texUnit, false);
      _values->addAttributeValue(TEXTURE_COORDS_2, value, false);
      break;
      
    case 2:
      _values->addUniformValue(SAMPLER3, texUnit, false);
      _values->addAttributeValue(TEXTURE_COORDS_3, value, false);
      break;
      
    default:
      ILogger::instance()->logError("Wrong texture target.");
      
      break;
  }
}


TextureGLFeature::TextureGLFeature(const IGLTextureId* texID,
                                   IFloatBuffer* texCoords,
                                   int arrayElementSize,
                                   int index,
                                   bool normalized,
                                   int stride,
                                   bool blend,
                                   int sFactor,
                                   int dFactor,
                                   float translateU,
                                   float translateV,
                                   float scaleU,
                                   float scaleV,
                                   float rotationAngleInRadians,
                                   float rotationCenterU,
                                   float rotationCenterV,
                                   int target) :
GLColorGroupFeature(GLF_TEXTURE, 4, blend, sFactor, dFactor),
_texID(texID),
_target(target),
_translation(NULL),
_scale(NULL),
_rotationCenter(NULL),
_rotationAngle(NULL)
{
  createBasicValues(texCoords, arrayElementSize, index, normalized, stride);
  
  setTranslation(translateU, translateV);
  setScale(scaleU, scaleV);
  setRotationAngleInRadiansAndRotationCenter(rotationAngleInRadians, rotationCenterU, rotationCenterV);
}

TextureGLFeature::TextureGLFeature(const IGLTextureId* texID,
                                   IFloatBuffer* texCoords,
                                   int arrayElementSize,
                                   int index,
                                   bool normalized,
                                   int stride,
                                   bool blend,
                                   int sFactor,
                                   int dFactor,
                                   int target) :
GLColorGroupFeature(GLF_TEXTURE, 4, blend, sFactor, dFactor),
_texID(texID),
_target(target),
_translation(NULL),
_scale(NULL),
_rotationCenter(NULL),
_rotationAngle(NULL)
{
  createBasicValues(texCoords, arrayElementSize, index, normalized, stride);
}

void TextureGLFeature::setTranslation(float u, float v) {
  if (_translation == NULL) {
    _translation = new GPUUniformValueVec2FloatMutable(u, v);
    
    _values->addUniformValue(TRANSLATION_TEXTURE_COORDS,
                             _translation,
                             false);
  }
  else {
    if (u == 0.0 && v == 0.0) {
      _values->removeUniformValue(TRANSLATION_TEXTURE_COORDS);
    }
    else {
      _translation->changeValue(u, v);
    }
  }
}

void TextureGLFeature::setScale(float u, float v) {
  if (_scale == NULL) {
    _scale = new GPUUniformValueVec2FloatMutable(u, v);
    
    _values->addUniformValue(SCALE_TEXTURE_COORDS,
                             _scale,
                             false);
  }
  else {
    if (u == 1.0 && v == 1.0) {
      _values->removeUniformValue(SCALE_TEXTURE_COORDS);
    }
    else{
      _scale->changeValue(u, v);
    }
  }
}
void TextureGLFeature::setRotationAngleInRadiansAndRotationCenter(float angle, float u, float v) {
  if (_rotationAngle == NULL || _rotationCenter == NULL) {
    if (angle != 0.0) {
      _rotationCenter = new GPUUniformValueVec2FloatMutable(u, v);
      
      _values->addUniformValue(ROTATION_CENTER_TEXTURE_COORDS,
                               _rotationCenter,
                               false);
      
      _rotationAngle = new GPUUniformValueFloatMutable(angle);
      
      _values->addUniformValue(ROTATION_ANGLE_TEXTURE_COORDS,
                               _rotationAngle,
                               false);
    }
  }
  else {
    if (angle == 0.0) {
      _values->removeUniformValue(ROTATION_CENTER_TEXTURE_COORDS);
      _values->removeUniformValue(ROTATION_ANGLE_TEXTURE_COORDS);
    }
    else {
      _rotationCenter->changeValue(u, v);
      _rotationAngle->changeValue(angle);
    }
  }
}

void TextureGLFeature::applyOnGlobalGLState(GLGlobalState* state) const {
  blendingOnGlobalGLState(state);
  state->bindTexture(_target, _texID);
}

ColorGLFeature::ColorGLFeature(const IFloatBuffer* colors, int arrayElementSize, int index, bool normalized, int stride,
                               bool blend, int sFactor, int dFactor) :
GLColorGroupFeature(GLF_COLOR, 3, blend, sFactor, dFactor)
{
  GPUAttributeValueVec4Float* value = new GPUAttributeValueVec4Float(colors, arrayElementSize, index, stride, normalized);
  _values->addAttributeValue(COLOR, value, false);
}

FlatColorGLFeature::FlatColorGLFeature(const Color& color,
                                       bool blend, int sFactor, int dFactor) :
GLColorGroupFeature(GLF_FLATCOLOR, 2, blend, sFactor, dFactor)
{
  _values->addUniformValue(FLAT_COLOR, new GPUUniformValueVec4Float(color._red,
                                                                    color._green,
                                                                    color._blue,
                                                                    color._alpha), false);
}



//////////////////////////////////////////

TextureIDGLFeature::TextureIDGLFeature(const IGLTextureId* texID) :
PriorityGLFeature(COLOR_GROUP, GLF_TEXTURE_ID, 4),
_texID(texID)
{
}

void TextureIDGLFeature::applyOnGlobalGLState(GLGlobalState* state) const {
  state->bindTexture(_texID);
}

BlendingModeGLFeature::BlendingModeGLFeature(bool blend, int sFactor, int dFactor) :
GLColorGroupFeature(GLF_BLENDING_MODE, 4, blend, sFactor, dFactor)
{
}

void BlendingModeGLFeature::applyOnGlobalGLState(GLGlobalState* state) const {
  blendingOnGlobalGLState(state);
}

TextureCoordsGLFeature::TextureCoordsGLFeature(IFloatBuffer* texCoords,
                                               int arrayElementSize,
                                               int index,
                                               bool normalized,
                                               int stride,
                                               bool coordsTransformed,
                                               const Vector2F& translate,
                                               const Vector2F& scale) :
PriorityGLFeature(COLOR_GROUP, GLF_TEXTURE_COORDS, 4)
{
  GPUAttributeValueVec2Float* value = new GPUAttributeValueVec2Float(texCoords,
                                                                     arrayElementSize,
                                                                     index,
                                                                     stride,
                                                                     normalized);
  _values->addAttributeValue(TEXTURE_COORDS, value, false);
  
#warning ONLY TARGET 0 FOR SGNODES
  GPUUniformValueInt* texUnit = new GPUUniformValueInt(0);
  _values->addUniformValue(SAMPLER, texUnit, false);
  
  if (coordsTransformed) {
    _values->addUniformValue(TRANSLATION_TEXTURE_COORDS,
                             new GPUUniformValueVec2Float(translate._x,
                                                          translate._y),
                             false);
    _values->addUniformValue(SCALE_TEXTURE_COORDS,
                             new GPUUniformValueVec2Float(scale._x,
                                                          scale._y),
                             false);
  }
}

void TextureCoordsGLFeature::applyOnGlobalGLState(GLGlobalState* state) const {
}

ProjectionGLFeature::ProjectionGLFeature(const Camera* camera) :
GLCameraGroupFeature(camera->getProjectionMatrix44D(), GLF_PROJECTION)
{
}

ModelGLFeature::ModelGLFeature(const Camera* camera) :
GLCameraGroupFeature(camera->getModelMatrix44D(), GLF_MODEL)
{
}

ModelViewGLFeature::ModelViewGLFeature(const Camera* camera) :
GLCameraGroupFeature(camera->getModelViewMatrix44D(), GLF_MODEL_VIEW)
{
}

DirectionLightGLFeature::DirectionLightGLFeature(const Vector3D& diffuseLightDirection,
                                                 const Color& diffuseLightColor,
                                                 const Color& ambientLightColor) :
GLFeature(LIGHTING_GROUP, GLF_DIRECTION_LIGTH)
{
  _values->addUniformValue(AMBIENT_LIGHT_COLOR,
                           new GPUUniformValueVec3Float(ambientLightColor), false);
  
  Vector3D dirN = diffuseLightDirection.normalized();
  
  _lightDirectionUniformValue = new GPUUniformValueVec3FloatMutable((float) dirN._x,
                                                                    (float) dirN._y,
                                                                    (float) dirN._z);
  
  _values->addUniformValue(DIFFUSE_LIGHT_DIRECTION,
                           _lightDirectionUniformValue,
                           false);
  _values->addUniformValue(DIFFUSE_LIGHT_COLOR,
                           new GPUUniformValueVec3Float(diffuseLightColor),
                           false);
}

void DirectionLightGLFeature::setLightDirection(const Vector3D& lightDir) {
  Vector3D dirN = lightDir.normalized();
  _lightDirectionUniformValue->changeValue((float)dirN._x,
                                           (float)dirN._y,
                                           (float)dirN._z);
}

VertexNormalGLFeature::VertexNormalGLFeature(const IFloatBuffer* buffer,
                                             int arrayElementSize,
                                             int index,
                                             bool normalized,
                                             int stride) :
GLFeature(LIGHTING_GROUP, GLF_VERTEX_NORMAL)
{
  _values->addAttributeValue(NORMAL,
                             new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized),
                             false);
}
