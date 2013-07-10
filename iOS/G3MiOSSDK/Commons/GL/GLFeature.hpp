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

class GLFeature {
public:

  GLFeature(GLFeatureGroupName group): _group(group){}

  virtual ~GLFeature(){}

  virtual void applyGLGlobalState(GL* gl) = 0;
  const GPUVariableValueSet* getGPUVariableValueSet() const{
    return &_values;
  }

  GLFeatureGroupName getGroup() const {
    return _group;
  }

protected:
  const GLFeatureGroupName _group;
  GPUVariableValueSet _values;

};

class BillboardFeature: public GLFeature{

  GPUUniformValueVec2Float* _texExtent;
  GPUUniformValueVec2Float* _viewportExtent;
  

public:

  BillboardFeature(int textureWidth, int textureHeight, int viewportWidth, int viewportHeight):
  GLFeature(NO_GROUP){

    _texExtent = new GPUUniformValueVec2Float(textureWidth, textureHeight);
    _values.addUniformValue(TEXTURE_EXTENT, _texExtent);

    _viewportExtent = new GPUUniformValueVec2Float(viewportWidth, viewportHeight);
    _values.addUniformValue(VIEWPORT_EXTENT, _viewportExtent);
  }

  ~BillboardFeature(){
    _texExtent->_release();
    _viewportExtent->_release();
  }

  void applyGLGlobalState(GL* gl){}

};

#endif
