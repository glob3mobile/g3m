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
  
  std::map<int, GPUUniformValue*> _uniformValues;
  std::map<int, GPUAttributeValue*> _attributesValues;
  
  bool setGPUUniformValue(int key, GPUUniformValue* v);
  bool setGPUAttributeValue(int key, GPUAttributeValue* v);
  
  mutable std::vector<int>* _uniformKeys;
  mutable std::vector<int>* _attributeKeys;
  
  mutable GPUProgram* _lastProgramUsed;
  
  void onStructureChanged(){
    delete _uniformKeys;
    _uniformKeys = NULL;
    _lastProgramUsed = NULL;
    
    if (_attributeKeys != NULL){
      delete _attributeKeys;
      _attributeKeys = NULL;
    }
  }
  
public:
  
  GPUProgramState(): _lastProgramUsed(NULL), _uniformKeys(NULL), _attributeKeys(NULL){}
  
  ~GPUProgramState();
  
  void clear();
  
  bool setUniformValue(int key, bool b);
  
  bool setUniformValue(int key, float f);
  
  bool setUniformValue(int key, const Vector2D& v);
  
  bool setUniformValue(int key, double x, double y);
  
  bool setUniformValue(int key, double x, double y, double z, double w);
  
  bool setUniformMatrixValue(int key, const MutableMatrix44D& m, bool isTransform);
  
  bool setAttributeValue(int key,
                         IFloatBuffer* buffer, int attributeSize,
                         int arrayElementSize, int index, bool normalized, int stride);
  
  void setAttributeEnabled(int key, bool enabled);
  void setAttributeDisabled(int key);
  
  void applyChanges(GL* gl) const;
  
  void linkToProgram(GPUProgram* prog) const;
  
  bool isLinkedToProgram() const{
    return _lastProgramUsed != NULL;
  }
  
  GPUProgram* getLinkedProgram() const{
    return _lastProgramUsed;
  }
  
  std::vector<int>* getUniformsKeys() const;
  std::vector<int>* getAttributeKeys() const;
  
  std::string description() const;
  
  void applyValuesToLinkedProgram() const;
  
  
  /*
   bool setUniformValue(const std::string& name, bool b){
   return setUniformValue(GPUVariable::getKeyForName(name, UNIFORM), b);
   }
   
   bool setUniformValue(const std::string& name, float f){
   return setUniformValue(GPUVariable::getKeyForName(name, UNIFORM), f);
   }
   
   bool setUniformValue(const std::string& name, const Vector2D& v){
   return setUniformValue(GPUVariable::getKeyForName(name, UNIFORM), v);
   }
   
   bool setUniformValue(const std::string& name, double x, double y){
   return setUniformValue(GPUVariable::getKeyForName(name, UNIFORM), x, y);
   }
   
   bool setUniformValue(const std::string& name, double x, double y, double z, double w){
   return setUniformValue(GPUVariable::getKeyForName(name, UNIFORM), x, y, z, w);
   }
   
   bool setUniformMatrixValue(const std::string& name, const MutableMatrix44D& m, bool isTransform){
   return setUniformMatrixValue(GPUVariable::getKeyForName(name, UNIFORM), m, isTransform);
   }
   
   bool setAttributeValue(const std::string& name,
   IFloatBuffer* buffer, int attributeSize,
   int arrayElementSize, int index, bool normalized, int stride){
   return setAttributeValue(GPUVariable::getKeyForName(name, ATTRIBUTE),
   buffer, attributeSize,
   arrayElementSize, index, normalized, stride);
   }
   
   void setAttributeEnabled(const std::string& name, bool enabled){
   setAttributeEnabled(GPUVariable::getKeyForName(name, ATTRIBUTE), enabled);
   }
   void setAttributeDisabled(const std::string& name){
   setAttributeDisabled(GPUVariable::getKeyForName(name, ATTRIBUTE));
   }
   */
  
};

#endif /* defined(__G3MiOSSDK__GPUProgramState__) */
