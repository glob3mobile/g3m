//
//  GLFeature.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GLFeature_hpp
#define G3MiOSSDK_GLFeature_hpp

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

class BillboardGLFeature: public GLFeature{
public:
  BillboardGLFeature(const Vector3D& position,
                     int textureWidth,
                     int textureHeight);

  void applyOnGlobalGLState(GLGlobalState* state) const;
};

class ViewportExtentGLFeature: public GLFeature {
public:
  ViewportExtentGLFeature(int viewportWidth,
                          int viewportHeight);
  void applyOnGlobalGLState(GLGlobalState* state)  const {}
};


class GeometryGLFeature: public GLFeature {
  //Position + cull + depth + polygonoffset + linewidth
  GPUAttributeValueVec4Float* _position;

  const bool _depthTestEnabled;
  const bool _cullFace;
  const int _culledFace;
  const bool  _polygonOffsetFill;
  const float _polygonOffsetFactor;
  const float _polygonOffsetUnits;
  const float _lineWidth;

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

  ~GeometryGLFeature();

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
public:

  GLCameraGroupFeature(Matrix44D* matrix,
                       GLFeatureID id) :
  GLFeature(CAMERA_GROUP, id),
  _matrixHolder(new Matrix44DHolder(matrix))
  {
  }

  ~GLCameraGroupFeature() {
    _matrixHolder->_release();

#ifdef JAVA_CODE
    super.dispose();
#endif
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
public:
  ModelViewGLFeature(Matrix44D* modelview) :
  GLCameraGroupFeature(modelview, GLF_MODEL_VIEW)
  {
  }

  ModelViewGLFeature(const Camera* cam);
};

class ModelGLFeature: public GLCameraGroupFeature {
public:
  ModelGLFeature(Matrix44D* model) :
  GLCameraGroupFeature(model, GLF_MODEL)
  {
  }

  ModelGLFeature(const Camera* cam);
};

class ProjectionGLFeature: public GLCameraGroupFeature {
public:

  ProjectionGLFeature(Matrix44D* projection) :
  GLCameraGroupFeature(projection, GLF_PROJECTION)
  {
  }

  ProjectionGLFeature(const Camera* cam);
};

class ModelTransformGLFeature: public GLCameraGroupFeature {
public:

  ModelTransformGLFeature(Matrix44D* transform) :
  GLCameraGroupFeature(transform, GLF_MODEL_TRANSFORM)
  {
  }
};
///////////////////////////////////////////////////////////////////////////////////////////

class PriorityGLFeature: public GLFeature {
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

  const bool _blend;
  const int _sFactor;
  const int _dFactor;

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
#ifdef C_CODE
  IGLTextureId const* _texID;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _texID = null;
#endif

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
                   bool coordsTransformed,
                   const Vector2D& translate,
                   const Vector2D& scale);

  void applyOnGlobalGLState(GLGlobalState* state) const;
};

class ColorGLFeature: public GLColorGroupFeature{
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

class FlatColorGLFeature: public GLColorGroupFeature{
public:
  FlatColorGLFeature(const Color& color,
                     bool blend, int sFactor, int dFactor);
  void applyOnGlobalGLState(GLGlobalState* state) const{
    blendingOnGlobalGLState(state);
  }
};

class TextureIDGLFeature: public PriorityGLFeature {
#ifdef C_CODE
  IGLTextureId const* _texID;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _texID = null;
#endif

public:
  TextureIDGLFeature(const IGLTextureId* texID);
  void applyOnGlobalGLState(GLGlobalState* state) const;
};

class BlendingModeGLFeature: public GLColorGroupFeature{
public:
  BlendingModeGLFeature(bool blend, int sFactor, int dFactor);
  void applyOnGlobalGLState(GLGlobalState* state) const;
};

class TextureCoordsGLFeature: public PriorityGLFeature {
public:
  TextureCoordsGLFeature(IFloatBuffer* texCoords,
                         int arrayElementSize,
                         int index,
                         bool normalized,
                         int stride,
                         bool coordsTransformed,
                         const Vector2D& translate,
                         const Vector2D& scale);

  void applyOnGlobalGLState(GLGlobalState* state) const;
};

class DirectionLightGLFeature: public GLFeature {

  GPUUniformValueVec3FloatMutable* _lightDirectionUniformValue;

public:
  DirectionLightGLFeature(const Vector3D& dir,
                          const Color& lightColor,
                          float ambientLight);

  void applyOnGlobalGLState(GLGlobalState* state) const{}

  void setLightDirection(const Vector3D& lightDir);
};

class VertexNormalGLFeature: public GLFeature{
public:
  VertexNormalGLFeature(IFloatBuffer* buffer,
                        int arrayElementSize,
                        int index,
                        bool normalized,
                        int stride);
  
  void applyOnGlobalGLState(GLGlobalState* state) const{}
};


#endif
