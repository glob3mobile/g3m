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

class GLFeature {
public:

  GLFeature(GLFeatureGroupName group): _group(group), _globalState(NULL){}

  virtual ~GLFeature(){
    delete _globalState;
    _values.releaseContainedValues();
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

  void applyGLGlobalState(GL* gl){}

};


class GeometryGLFeature: public GLFeature{
  //Position + cull + depth + polygonoffset + linewidth
  GPUAttributeValueVec4Float* _position;

public:

  GeometryGLFeature(IFloatBuffer* buffer, int arrayElementSize, int index, bool normalized, int stride,
                    bool depthTestEnabled,
                    bool cullFace, int culledFace,
                    bool  polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits,
                    float lineWidth);

  ~GeometryGLFeature();

  void applyGLGlobalState(GL* gl){}
  
};

#endif
