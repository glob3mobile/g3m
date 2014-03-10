//
//  GLFeature.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GLFeature
#define G3MiOSSDK_GLFeature

#include "GPUVariableValueSet.hpp"
#include "GLFeatureGroup.hpp"
#include "GPUAttribute.hpp"

#include "RCObject.hpp"

class Camera;

enum GLFeatureID{
  GLF_BILLBOARD,
  GLF_VIEWPORT_EXTENT,
  GLF_GEOMETRY,
  GLF_MODEL,
  GLF_PROJECTION,
  GLF_MODEL_TRANSFORM,
  GLF_TEXTURE,
  GLF_COLOR,
  GLF_FLATCOLOR,
  GLF_TEXTURE_ID,
  GLF_TEXTURE_COORDS,
  GLF_DIRECTION_LIGTH,
  GLF_VERTEX_NORMAL,
  GLF_MODEL_VIEW,
  GLF_BLENDING_MODE
};

class GLFeature: public RCObject {
protected:
  ~GLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

protected:
  GPUVariableValueSet _values;

public:
  const GLFeatureGroupName _group;
  const GLFeatureID _id;

  GLFeature(GLFeatureGroupName group,
            GLFeatureID id) :
  _group(group),
  _id(id)
  {
  }

  const GPUVariableValueSet* getGPUVariableValueSet() const{
    return &_values;
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

public:
  BillboardGLFeature(const Vector3D& position,
                     int textureWidth,
                     int textureHeight);

  void applyOnGlobalGLState(GLGlobalState* state) const;
};

class ViewportExtentGLFeature: public GLFeature {
private:
  ~ViewportExtentGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  ViewportExtentGLFeature(int viewportWidth,
                          int viewportHeight);
  void applyOnGlobalGLState(GLGlobalState* state)  const {}
};


class GeometryGLFeature: public GLFeature {
private:
  //Position + cull + depth + polygonoffset + linewidth
  GPUAttributeValueVec4Float* _position;

  const bool _depthTestEnabled;
  const bool _cullFace;
  const int _culledFace;
  const bool  _polygonOffsetFill;
  const float _polygonOffsetFactor;
  const float _polygonOffsetUnits;
  const float _lineWidth;

  ~GeometryGLFeature();

public:

  GeometryGLFeature(IFloatBuffer* buffer,
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

};
///////////////////////////////////////////////////////////////////////////////////////////

class GLCameraGroupFeature: public GLFeature {
private:

#ifdef C_CODE
  Matrix44DHolder *_matrixHolder;
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

  const Matrix44DHolder* getMatrixHolder() const{
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

  ModelViewGLFeature(const Camera* cam);
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

  ModelGLFeature(const Camera* cam);
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

  ProjectionGLFeature(const Camera* cam);
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
///////////////////////////////////////////////////////////////////////////////////////////

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
  IGLTextureId const* _texID;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _texID = null;
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
  TextureGLFeature(const IGLTextureId* texID,
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

  TextureGLFeature(const IGLTextureId* texID,
                   IFloatBuffer* texCoords,
                   int arrayElementSize,
                   int index,
                   bool normalized,
                   int stride,
                   bool blend,
                   int sFactor,
                   int dFactor,
                   int target = 0);

  void setTranslation(float u, float v);
  void setScale(float u, float v);
  void setRotationAngleInRadiansAndRotationCenter(float angle, float u, float v);

  int getTarget() const{
    return _target;
  }

#ifdef C_CODE
  IGLTextureId const* getTextureID() const{
    return _texID;
  }
#endif
#ifdef JAVA_CODE
  public final IGLTextureId getTextureID() {
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
  ColorGLFeature(IFloatBuffer* colors,
                 int arrayElementSize,
                 int index,
                 bool normalized,
                 int stride,
                 bool blend,
                 int sFactor,
                 int dFactor);

  void applyOnGlobalGLState(GLGlobalState* state) const{
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
                     bool blend, int sFactor, int dFactor);
  void applyOnGlobalGLState(GLGlobalState* state) const{
    blendingOnGlobalGLState(state);
  }
};

class TextureIDGLFeature: public PriorityGLFeature {
private:
#ifdef C_CODE
  IGLTextureId const* _texID;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _texID = null;
#endif

  ~TextureIDGLFeature() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  TextureIDGLFeature(const IGLTextureId* texID);
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

  void applyOnGlobalGLState(GLGlobalState* state) const{}

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
  VertexNormalGLFeature(IFloatBuffer* buffer,
                        int arrayElementSize,
                        int index,
                        bool normalized,
                        int stride);
  
  void applyOnGlobalGLState(GLGlobalState* state) const{}
};


#endif
