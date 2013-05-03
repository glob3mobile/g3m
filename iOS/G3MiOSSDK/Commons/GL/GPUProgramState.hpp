//
//  GPUProgramState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

#ifndef __G3MiOSSDK__GPUProgramState__
#define __G3MiOSSDK__GPUProgramState__

#include <iostream>
#include <map>
#include <string>

#include "GPUUniform.hpp"
#include "GPUAttribute.hpp"
#include "GL.hpp"
#include "GPUProgram.hpp"

class GPUProgramState{
  
  std::map<std::string, GPUUniformValue*> _uniformValues;
  std::map<std::string, GPUAttributeValue*> _attributesValues;
  
  const GPUProgramState* _parentState;
  
  void setUniformValue(const std::string& name, GPUUniformValue* v);
  void setAttributeValue(const std::string& name, GPUAttributeValue* v);
  
public:
  
  
  GPUProgramState(const GPUProgramState* parentState):_parentState(parentState){}
  
  
  ~GPUProgramState(){
    for(std::map<std::string, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
        it != _uniformValues.end();
        it++){
      delete it->second;
    }
  }
  
  
  void setValueToUniform(const std::string& name, bool b){
    setValueToUniform(name, new GPUUniformValueBool(b));
  }
  
  void setValueToUniform(const std::string& name, float f){
    setValueToUniform(name, new GPUUniformValueFloat(f));
  }
  
  void setValueToUniform(const std::string& name, const Vector2D& v){
    setValueToUniform(name, new GPUUniformValueVec2Float(v._x, v._y));
  }
  
  void setValueToUniform(const std::string& name, double x, double y, double z, double w){
    setValueToUniform(name, new GPUUniformValueVec4Float(x,y,z,w));
  }
  
  void setValueToUniform(const std::string& name, const MutableMatrix44D& m){
    setValueToUniform(name, new GPUUniformValueMatrix4Float(m));
  }
  
  void multiplyValueOfUniform(const std::string& name, const MutableMatrix44D& m){
    MutableMatrix44D parentsMatrix = getAccumulatedMatrixFromParent(name);
    setValueToUniform(name, new GPUUniformValueMatrix4Float(parentsMatrix.multiply(m)) );
  }
  
  void setAttributeValue(const std::string& name,
                         IFloatBuffer* buffer, int attributeSize,
                         int arrayElementSize, int index, bool normalized, int stride);
  
  void applyChanges(GL* gl, GPUProgram& prog) const;
  
  MutableMatrix44D getAccumulatedMatrixFromParent(const std::string name);
  
  //  GPUUniformValue* getUniformValue(const std::string name) const;
};

#endif /* defined(__G3MiOSSDK__GPUProgramState__) */
