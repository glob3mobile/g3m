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

class GLFeature: public RCObject {
public:

  GLFeature(GLFeatureGroupName group): _group(group), _globalState(NULL){}

  virtual ~GLFeature(){
    delete _globalState;
  }

  void applyGLGlobalState(GL* gl) const{
    if (_globalState != NULL){
      _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());
    }
  }
  const GPUVariableValueSet* getGPUVariableValueSet() const{
    return &_values;
  }

  GLFeatureGroupName getGroup() const {
    return _group;
  }

protected:
  const GLFeatureGroupName _group;
  GPUVariableValueSet _values;
  GLGlobalState* _globalState;

};

class BillboardGLFeature: public GLFeature{

  GPUUniformValueVec2Float* _texExtent;
  GPUUniformValueVec2Float* _viewportExtent;
  

public:

  BillboardGLFeature(int textureWidth, int textureHeight, int viewportWidth, int viewportHeight);

  ~BillboardGLFeature();

};


class GeometryGLFeature: public GLFeature{
  //Position + cull + depth + polygonoffset + linewidth
  GPUAttributeValueVec4Float* _position;

public:

  GeometryGLFeature(IFloatBuffer* buffer, int arrayElementSize, int index, bool normalized, int stride,
                    bool depthTestEnabled,
                    bool cullFace, int culledFace,
                    bool  polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits,
                    float lineWidth,
                    bool needsPointSize, float pointSize);

  ~GeometryGLFeature();
  
};
///////////////////////////////////////////////////////////////////////////////////////////

class Matrix44DHolder{
  const Matrix44D* _matrix;
public:
  Matrix44DHolder(const Matrix44D* matrix):_matrix(matrix){
    matrix->_retain();
  }

  ~Matrix44DHolder(){
    _matrix->_release();
  }

  void setMatrix(const Matrix44D* matrix){
    if (matrix != _matrix){
      _matrix->_release();
      _matrix = matrix;
      _matrix->_retain();
    }
  }

  const Matrix44D* getMatrix() const{
    return _matrix;
  }
};

class GLCameraGroupFeature: public GLFeature{
  Matrix44DHolder _matrixHolder;
public:
  GLCameraGroupFeature(Matrix44D* matrix): GLFeature(CAMERA_GROUP), _matrixHolder(matrix){}
  ~GLCameraGroupFeature(){}
  const Matrix44D* getMatrix() const{ return _matrixHolder.getMatrix();}
  const void setMatrix(const Matrix44D* matrix){ return _matrixHolder.setMatrix(matrix);}
};

class ModelGLFeature: public GLCameraGroupFeature{
public:
  ModelGLFeature(Matrix44D* model): GLCameraGroupFeature(model){}
};

class ProjectionGLFeature: public GLCameraGroupFeature{
public:
  ProjectionGLFeature(Matrix44D* projection): GLCameraGroupFeature(projection){}
};

class ModelTransformGLFeature: public GLCameraGroupFeature{
public:
  ModelTransformGLFeature(Matrix44D* transform): GLCameraGroupFeature(transform){}
};
///////////////////////////////////////////////////////////////////////////////////////////
class GLColorGroupFeature: public GLFeature{
  const int _priority;
public:
  GLColorGroupFeature(int p, bool blend, int sFactor, int dFactor): GLFeature(COLOR_GROUP), _priority(p) {
    _globalState = new GLGlobalState();

    if (blend){
      _globalState->enableBlend();
      _globalState->setBlendFactors(sFactor, dFactor);
    } else{
      _globalState->disableBlend();
    }

  }
  int getPriority() const { return _priority;}
};

class TextureGLFeature: public GLColorGroupFeature{
public:
  TextureGLFeature(const IGLTextureId* texID,
                   IFloatBuffer* texCoords, int arrayElementSize, int index, bool normalized, int stride,
                   bool blend, int sFactor, int dFactor,
                   bool coordsTransformed, const Vector2D& translate, const Vector2D& scale);
};

class ColorGLFeature: public GLColorGroupFeature{
public:
  ColorGLFeature(IFloatBuffer* colors, int arrayElementSize, int index, bool normalized, int stride,
                 bool blend, int sFactor, int dFactor);
};

class FlatColorGLFeature: public GLColorGroupFeature{
public:
  FlatColorGLFeature(const Color& color,
                 bool blend, int sFactor, int dFactor);
};


#endif
