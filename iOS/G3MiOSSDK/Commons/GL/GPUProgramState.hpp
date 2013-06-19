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
  
//  struct attributeEnabledStruct{
//    bool value;
//    mutable GPUAttribute* attribute;
//  };
  std::map<std::string, GPUUniformValue*> _uniformValues;
  std::map<std::string, GPUAttributeValue*> _attributesValues;
//  std::map<std::string, attributeEnabledStruct> _attributesEnabled;
  
  bool setGPUUniformValue(const std::string& name, GPUUniformValue* v);
  bool setGPUAttributeValue(const std::string& name, GPUAttributeValue* v);
  
  mutable std::vector<std::string>* _uniformNames;
  
  mutable GPUProgram* _lastProgramUsed;
  
  void onStructureChanged(){
    delete _uniformNames;
    _uniformNames = NULL;
    _lastProgramUsed = NULL;
  }
  
public:
  
  GPUProgramState(): _lastProgramUsed(NULL), _uniformNames(NULL){}
  
  ~GPUProgramState();
  
  void clear();
  
  bool setUniformValue(const std::string& name, bool b);
  
  bool setUniformValue(const std::string& name, float f);
  
  bool setUniformValue(const std::string& name, const Vector2D& v);
  
  bool setUniformValue(const std::string& name, double x, double y);
  
  bool setUniformValue(const std::string& name, double x, double y, double z, double w);
  
//  bool setUniformValue(const std::string& name, const MutableMatrix44D* m);
//  
//  bool multiplyUniformValue(const std::string& name, const MutableMatrix44D* m);
  
  bool setUniformMatrixValue(const std::string& name, const MutableMatrix44D& m, bool isTransform);
  
  bool setAttributeValue(const std::string& name,
                         IFloatBuffer* buffer, int attributeSize,
                         int arrayElementSize, int index, bool normalized, int stride);
  
  void setAttributeEnabled(const std::string& name, bool enabled);
  void setAttributeDisabled(const std::string& name);
  
  void applyChanges(GL* gl) const;
  
  void linkToProgram(GPUProgram& prog) const;
  
  bool isLinkedToProgram() const{
    return _lastProgramUsed != NULL;
  }
  
  GPUProgram* getLinkedProgram() const{
    return _lastProgramUsed;
  }
  
  std::vector<std::string>* getUniformsNames() const;
  
  std::string description() const;
  
  bool isLinkableToProgram(const GPUProgram& program) const;
  
  void applyValuesToLinkedProgram() const;
  
};

#endif /* defined(__G3MiOSSDK__GPUProgramState__) */
