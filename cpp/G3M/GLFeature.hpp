//
//  GLFeature.hpp
//  G3M
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//

#ifndef G3M_GLFeature
#define G3M_GLFeature

#include "RCObject.hpp"

#include "GPUVariableValueSet.hpp"
#include "GLFeatureGroup.hpp"
#include "ViewMode.hpp"
#include "GPUUniform.hpp"


class Camera;
class Color;


class GLFeature: public RCObject {
protected:
  ~GLFeature() {
    delete _values;
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

protected:
  GPUVariableValueSet* _values;

public:
  const GLFeatureGroupName _group;
  const GLFeatureID _id;

  GLFeature(GLFeatureGroupName group,
            GLFeatureID id) :
  _group(group),
  _id(id)
  {
    _values = new GPUVariableValueSet();
  }

  const GPUVariableValueSet* getGPUVariableValueSet() const {
    return _values;
  }

  virtual void applyOnGlobalGLState(GLGlobalState* state) const = 0;

};


class BillboardGLFeature: public GLFeature {
private:
  ~BillboardGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  GPUUniformValueVec2FloatMutable* _size;
  GPUUniformValueVec2FloatMutable* _anchor;

public:
  BillboardGLFeature(float billboardWidth,
                     float billboardHeight,
                     float anchorU, float anchorV);

  void applyOnGlobalGLState(GLGlobalState* state) const;

  void changeSize(int textureWidth,
                  int textureHeight) {
    _size->changeValue(textureWidth, textureHeight);
  }

  void changeAnchor(float anchorU, float anchorV) {
    _anchor->changeValue(anchorU, anchorV);
  }
};


class ViewportExtentGLFeature: public GLFeature {
private:
  ~ViewportExtentGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  GPUUniformValueVec2FloatMutable* _extent;

public:
  ViewportExtentGLFeature(int viewportWidth,
                          int viewportHeight);

  ViewportExtentGLFeature(const Camera* camera,
                          ViewMode      viewMode);

  void applyOnGlobalGLState(GLGlobalState* state)  const {}

  void changeExtent(int viewportWidth,
                    int viewportHeight);
};


// Ribbon half-width is resolved in the vertex shader as max(widthInMeters, minWidthInPixels)
class RibbonWidthGLFeature: public GLFeature {
private:
  ~RibbonWidthGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  GPUUniformValueVec2FloatMutable* _width;

public:
  RibbonWidthGLFeature(float widthInMeters,
                       float minWidthInPixels);

  void applyOnGlobalGLState(GLGlobalState* state)  const {}

  void changeWidth(float widthInMeters,
                   float minWidthInPixels);
};


// Per-vertex unit vector the RibbonMesh shader offsets along. Deliberately not the NORMAL
// attribute: normals would drag the lighting path (MODEL uniform) into the GL state.
class RibbonSideGLFeature: public GLFeature {
private:
  ~RibbonSideGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  RibbonSideGLFeature(const IFloatBuffer* sides);

  void applyOnGlobalGLState(GLGlobalState* state)  const {}
};


class CameraPositionGLFeature: public GLFeature {
private:
  ~CameraPositionGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  const bool _groundHazePass;
  GPUUniformValueVec3FloatMutable* _camPos;
  GPUUniformValueVec3FloatMutable* _spaceColor;

public:
  CameraPositionGLFeature(const Camera* cam,
                          bool groundHazePass,
                          const Color& spaceColor);

  // The sky adds its light and dims what is behind it (the background, the stars) by its transmittance;
  // the haze is a fog colour faded in by its opacity
  void applyOnGlobalGLState(GLGlobalState* state)  const {
    state->enableBlend();
    state->setBlendFactors(_groundHazePass ? GLBlendFactor::srcAlpha() : GLBlendFactor::one(),
                           GLBlendFactor::oneMinusSrcAlpha());
  }

  void update(const Camera* cam,
              const Color& spaceColor);
};


// Multiplies the brightness of every star of the Stars program
class StarsGLFeature: public GLFeature {
private:
  ~StarsGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  GPUUniformValueFloatMutable* _fullStarMagnitude;

public:
  StarsGLFeature(float fullStarMagnitude,
                 float starSizeExponent);

  void applyOnGlobalGLState(GLGlobalState* state)  const {}

  void changeFullStarMagnitude(float fullStarMagnitude);
};


// Tile color = (colorMatrix * (r, g, b, 1)).rgb, clamped; alpha is untouched.
// The fourth column of the homogeneous matrix is the color offset.
class ColorGradeGLFeature: public GLFeature {
private:
#ifdef C_CODE
  Matrix44DHolder* _colorMatrixHolder;
#endif
#ifdef JAVA_CODE
  private Matrix44DHolder _colorMatrixHolder;
#endif

  ~ColorGradeGLFeature() {
    _colorMatrixHolder->_release();

#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  ColorGradeGLFeature(const Matrix44D* colorMatrix);

  void applyOnGlobalGLState(GLGlobalState* state)  const {}

  void changeColorMatrix(const Matrix44D* colorMatrix);
};


class GeometryGLFeature: public GLFeature {
private:
  //Position + cull + depth + polygonoffset + linewidth

  const bool  _depthTestEnabled;
  const bool  _cullFace;
  const int   _culledFace;
  const bool  _polygonOffsetFill;
  const float _polygonOffsetFactor;
  const float _polygonOffsetUnits;
  const float _lineWidth;

  float _pointSize;
  GPUUniformValueFloat* _pointSizeGPUUniformValueFloat;

  ~GeometryGLFeature();

public:

  GeometryGLFeature(const IFloatBuffer* buffer,
                    int arrayElementSize,
                    int index,
                    bool normalized,
                    int stride,
                    bool depthTestEnabled,
                    bool cullFace,
                    int culledFace,
                    bool  polygonOffsetFill,
                    float polygonOffsetFactor,
                    float polygonOffsetUnits,
                    float lineWidth,
                    bool needsPointSize,
                    float pointSize);


  void applyOnGlobalGLState(GLGlobalState* state) const ;

  void setPointSize(float pointSize);
};


class Geometry2DGLFeature: public GLFeature {
private:
  //Position + cull + depth + polygonoffset + linewidth

  const float _lineWidth;

  ~Geometry2DGLFeature();

  GPUUniformValueVec2FloatMutable* _translation;

public:

  Geometry2DGLFeature(IFloatBuffer* buffer,
                      int arrayElementSize,
                      int index,
                      bool normalized,
                      int stride,
                      float lineWidth,
                      bool needsPointSize,
                      float pointSize,
                      const Vector2F& translation);

  void setTranslation(float x, float y) {
    _translation->changeValue(x, y);
  }


  void applyOnGlobalGLState(GLGlobalState* state) const ;

};


class GLCameraGroupFeature: public GLFeature {
private:

#ifdef C_CODE
  Matrix44DHolder* _matrixHolder;
#endif
#ifdef JAVA_CODE
  private Matrix44DHolder _matrixHolder = null;
#endif

protected:
  ~GLCameraGroupFeature() {
    _matrixHolder->_release();

#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:

  GLCameraGroupFeature(Matrix44D* matrix,
                       GLFeatureID id) :
  GLFeature(CAMERA_GROUP, id),
  _matrixHolder(new Matrix44DHolder(matrix))
  {
  }

  const Matrix44D* getMatrix() const {
    return _matrixHolder->getMatrix();
  }

  const void setMatrix(const Matrix44D* matrix) {
    _matrixHolder->setMatrix(matrix);
  }

  const Matrix44DHolder* getMatrixHolder() const {
    return _matrixHolder;
  }

  void applyOnGlobalGLState(GLGlobalState* state) const {}
};


class ModelViewGLFeature: public GLCameraGroupFeature {
private:
  ~ModelViewGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  ModelViewGLFeature(Matrix44D* modelview) :
  GLCameraGroupFeature(modelview, GLF_MODEL_VIEW)
  {
  }

  ModelViewGLFeature(const Camera* camera);
};


class ModelGLFeature: public GLCameraGroupFeature {
private:
  ~ModelGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  ModelGLFeature(Matrix44D* model) :
  GLCameraGroupFeature(model, GLF_MODEL)
  {
  }

  ModelGLFeature(const Camera* camera);
};


class ProjectionGLFeature: public GLCameraGroupFeature {
private:
  ~ProjectionGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  ProjectionGLFeature(Matrix44D* projection) :
  GLCameraGroupFeature(projection, GLF_PROJECTION)
  {
  }

  ProjectionGLFeature(const Camera* camera);
};


class ModelTransformGLFeature: public GLCameraGroupFeature {
private:
  ~ModelTransformGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  ModelTransformGLFeature(Matrix44D* transform) :
  GLCameraGroupFeature(transform, GLF_MODEL_TRANSFORM)
  {
  }
};


class PriorityGLFeature: public GLFeature {
protected:
  ~PriorityGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  const int _priority;

  PriorityGLFeature(GLFeatureGroupName g,
                    GLFeatureID id,
                    int priority) :
  GLFeature(g, id),
  _priority(priority)
  {
  }
};


class GLColorGroupFeature: public PriorityGLFeature {
private:
  const bool _blend;
  const int _sFactor;
  const int _dFactor;

protected:
  ~GLColorGroupFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GLColorGroupFeature(GLFeatureID id,
                      int priority,
                      bool blend,
                      int sFactor,
                      int dFactor) :
  PriorityGLFeature(COLOR_GROUP, id, priority),
  _blend(blend),
  _sFactor(sFactor),
  _dFactor(dFactor)
  {
  }

  void blendingOnGlobalGLState(GLGlobalState* state) const {
    if (_blend) {
      state->enableBlend();
      state->setBlendFactors(_sFactor, _dFactor);
    }
    else {
      state->disableBlend();
    }
  }
};


class TextureGLFeature: public GLColorGroupFeature {
private:
#ifdef C_CODE
  IGLTextureID const* _texID;
#endif
#ifdef JAVA_CODE
  private IGLTextureID _texID = null;
#endif

  ~TextureGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  const int _target;

  void createBasicValues(IFloatBuffer* texCoords,
                         int arrayElementSize,
                         int index,
                         bool normalized,
                         int stride);

  GPUUniformValueVec2FloatMutable* _translation;
  GPUUniformValueVec2FloatMutable* _scale;
  GPUUniformValueVec2FloatMutable* _rotationCenter;
  GPUUniformValueFloatMutable* _rotationAngle;

public:
  TextureGLFeature(const IGLTextureID* texID,
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
                   int target = 0);

  TextureGLFeature(const IGLTextureID* texID,
                   IFloatBuffer* texCoords,
                   int arrayElementSize,
                   int index,
                   bool normalized,
                   int stride,
                   bool blend,
                   int sFactor,
                   int dFactor,
                   int target = 0);

  bool hasTranslateAndScale() const { return _translation != NULL && _scale != NULL;}
  bool hasRotation() const { return _rotationAngle != NULL && _rotationCenter != NULL; }

  void setTranslation(float u, float v);
  void setScale(float u, float v);
  void setRotation(float angleInRadians,
                   float centerU, float centerV);

  int getTarget() const {
    return _target;
  }

#ifdef C_CODE
  IGLTextureID const* getTextureID() const {
    return _texID;
  }
#endif
#ifdef JAVA_CODE
  public final IGLTextureID getTextureID() {
    return _texID;
  }
#endif

  void applyOnGlobalGLState(GLGlobalState* state) const;
};


class ColorGLFeature: public GLColorGroupFeature {
private:
  ~ColorGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  ColorGLFeature(const IFloatBuffer* colors,
                 int arrayElementSize,
                 int index,
                 bool normalized,
                 int stride,
                 bool blend,
                 int sFactor,
                 int dFactor);

  void applyOnGlobalGLState(GLGlobalState* state) const {
    blendingOnGlobalGLState(state);
  }
};


class FlatColorGLFeature: public GLColorGroupFeature {
private:
  ~FlatColorGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  FlatColorGLFeature(const Color& color,
                     bool blend = false,
                     int sFactor = GLBlendFactor::srcAlpha(),
                     int dFactor = GLBlendFactor::oneMinusSrcAlpha());
  void applyOnGlobalGLState(GLGlobalState* state) const {
    blendingOnGlobalGLState(state);
  }
};


class TextureIDGLFeature: public PriorityGLFeature {
private:
#ifdef C_CODE
  IGLTextureID const* _texID;
#endif
#ifdef JAVA_CODE
  private IGLTextureID _texID = null;
#endif

  ~TextureIDGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  TextureIDGLFeature(const IGLTextureID* texID);
  void applyOnGlobalGLState(GLGlobalState* state) const;
};


class BlendingModeGLFeature: public GLColorGroupFeature {
private:
  ~BlendingModeGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  BlendingModeGLFeature(bool blend, int sFactor, int dFactor);
  void applyOnGlobalGLState(GLGlobalState* state) const;
};


class TextureCoordsGLFeature: public PriorityGLFeature {
private:
  ~TextureCoordsGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  TextureCoordsGLFeature(IFloatBuffer* texCoords,
                         int arrayElementSize,
                         int index,
                         bool normalized,
                         int stride,
                         bool coordsTransformed,
                         const Vector2F& translate,
                         const Vector2F& scale);

  void applyOnGlobalGLState(GLGlobalState* state) const;
};


class DirectionLightGLFeature: public GLFeature {
private:
  GPUUniformValueVec3FloatMutable* _lightDirectionUniformValue;

  ~DirectionLightGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  DirectionLightGLFeature(const Vector3D& diffuseLightDirection,
                          const Color& diffuseLightColor,
                          const Color& ambientLightColor);

  void applyOnGlobalGLState(GLGlobalState* state) const {}

  void setLightDirection(const Vector3D& lightDir);
};


class VertexNormalGLFeature: public GLFeature {
private:
  ~VertexNormalGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  VertexNormalGLFeature(const IFloatBuffer* buffer,
                        int arrayElementSize,
                        int index,
                        bool normalized,
                        int stride);
  
  void applyOnGlobalGLState(GLGlobalState* state) const {}
};

#endif
