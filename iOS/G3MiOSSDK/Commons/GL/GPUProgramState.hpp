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
#include "MutableMatrix44D.hpp"

class GPUProgramState{
  
  std::map<std::string, GPUUniformValue*> _uniformValues;
  std::map<std::string, GPUAttributeValue*> _attributesValues;
  std::map<std::string, bool> _attributesEnabled;
  
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
  
  
  void setUniformValue(const std::string& name, bool b);
  
  void setUniformValue(const std::string& name, float f);
  
  void setUniformValue(const std::string& name, const Vector2D& v);
  
  void setUniformValue(const std::string& name, double x, double y, double z, double w);
  
  void setUniformValue(const std::string& name, const MutableMatrix44D& m);
  
  void multiplyUniformValue(const std::string& name, const MutableMatrix44D& m);
  
  void setAttributeValue(const std::string& name,
                         IFloatBuffer* buffer, int attributeSize,
                         int arrayElementSize, int index, bool normalized, int stride);
  
  void setAttributeEnabled(const std::string& name, bool enabled);
  
  void applyChanges(GL* gl, GPUProgram& prog) const;
  
  MutableMatrix44D getMatrixValue(const std::string name) const;
  
  void setValuesOntoGPUProgram(GPUProgram& prog) const;
  
  //  GPUUniformValue* getUniformValue(const std::string name) const;
};

#endif /* defined(__G3MiOSSDK__GPUProgramState__) */
