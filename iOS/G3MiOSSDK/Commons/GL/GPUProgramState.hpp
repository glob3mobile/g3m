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
  
  struct attributeEnabledStruct{
    bool value;
    mutable GPUAttribute* attribute;
  };
  std::map<std::string, GPUUniformValue*> _uniformValues;
  std::map<std::string, GPUAttributeValue*> _attributesValues;
  std::map<std::string, attributeEnabledStruct> _attributesEnabled;
  
  void setUniformValue(const std::string& name, GPUUniformValue* v);
  void setAttributeValue(const std::string& name, GPUAttributeValue* v);
  
  void applyValuesToLinkedProgram(GL* gl) const;
  
  mutable GPUProgram* _lastProgramUsed;
  
public:
  
  GPUProgramState(): _lastProgramUsed(NULL){}
  
  ~GPUProgramState();
  
  void clear();
  
  void setUniformValue(const std::string& name, bool b);
  
  void setUniformValue(const std::string& name, float f);
  
  void setUniformValue(const std::string& name, const Vector2D& v);
  
  void setUniformValue(const std::string& name, double x, double y, double z, double w);
  
  void setUniformValue(const std::string& name, const MutableMatrix44D* m);
  
  void multiplyUniformValue(const std::string& name, const MutableMatrix44D* m);
  
  void setAttributeValue(const std::string& name,
                         IFloatBuffer* buffer, int attributeSize,
                         int arrayElementSize, int index, bool normalized, int stride);
  
  void setAttributeEnabled(const std::string& name, bool enabled);
  
  void applyChanges(GL* gl, GPUProgram& prog) const;
  
  void linkToProgram(GPUProgram& prog) const;
  
  bool isLinkedToProgram() const{
    return _lastProgramUsed = NULL;
  }
  
  GPUProgram* getLinkedProgram() const{
    return _lastProgramUsed;
  }
  
  std::vector<std::string> getUniformsNames() const;
  
  std::string description() const;
};

#endif /* defined(__G3MiOSSDK__GPUProgramState__) */
