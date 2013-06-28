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
  
//  std::map<GPUUniformKey, GPUUniformValue*> _uniformValues;
//  std::map<GPUAttributeKey, GPUAttributeValue*> _attributesValues;

  GPUUniformValue* _uniformValues[32];
  GPUAttributeValue* _attributeValues[32];
  
  bool setGPUUniformValue(GPUUniformKey key, GPUUniformValue* v);
  bool setGPUAttributeValue(GPUAttributeKey key, GPUAttributeValue* v);

  mutable int _uniformsCode;
  mutable int _attributeCode;

  
  mutable std::vector<int>* _uniformKeys;
  mutable std::vector<int>* _attributeKeys;
  
  mutable GPUProgram* _linkedProgram;
  
  void onStructureChanged(){
    delete _uniformKeys;
    _uniformKeys = NULL;
    _linkedProgram = NULL;
    _uniformsCode = 0;
    _attributeCode = 0;
    
    if (_attributeKeys != NULL){
      delete _attributeKeys;
      _attributeKeys = NULL;
    }
  }
  
public:
  
  GPUProgramState(): _linkedProgram(NULL), _uniformKeys(NULL), _attributeKeys(NULL), _uniformsCode(0), _attributeCode(0){
    for (int i = 0; i < 32; i++) {
      _uniformValues[i] = NULL;
      _attributeValues[i] = NULL;
    }
  }
  
  ~GPUProgramState();
  
  void clear();
  
  bool setUniformValue(GPUUniformKey key, bool b);
  
  bool setUniformValue(GPUUniformKey key, float f);
  
  bool setUniformValue(GPUUniformKey key, const Vector2D& v);
  
  bool setUniformValue(GPUUniformKey key, double x, double y);
  
  bool setUniformValue(GPUUniformKey key, double x, double y, double z, double w);
  
  bool setUniformMatrixValue(GPUUniformKey key, const MutableMatrix44D& m, bool isTransform);
  
  bool setAttributeValue(GPUAttributeKey key,
                         IFloatBuffer* buffer, int attributeSize,
                         int arrayElementSize, int index, bool normalized, int stride);
  
  void setAttributeEnabled(GPUAttributeKey key, bool enabled);
  void setAttributeDisabled(GPUAttributeKey key);
  
  void applyChanges(GL* gl) const;
  
  void linkToProgram(GPUProgram* prog) const;
  
  bool isLinkedToProgram() const{
    return _linkedProgram != NULL;
  }
  
  GPUProgram* getLinkedProgram() const{
    return _linkedProgram;
  }
  
  std::vector<int>* getUniformsKeys() const;
  std::vector<int>* getAttributeKeys() const;
  
  std::string description() const;
  
  void applyValuesToLinkedProgram() const;
  
  bool removeGPUUniformValue(GPUUniformKey key);

  int getUniformsCode() const;

  int getAttributesCode() const;
  
  
  /*
   bool setUniformValue(const std::string& name, bool b){
   return setUniformValue(getKeyForName(name, UNIFORM), b);
   }
   
   bool setUniformValue(const std::string& name, float f){
   return setUniformValue(getKeyForName(name, UNIFORM), f);
   }
   
   bool setUniformValue(const std::string& name, const Vector2D& v){
   return setUniformValue(getKeyForName(name, UNIFORM), v);
   }
   
   bool setUniformValue(const std::string& name, double x, double y){
   return setUniformValue(getKeyForName(name, UNIFORM), x, y);
   }
   
   bool setUniformValue(const std::string& name, double x, double y, double z, double w){
   return setUniformValue(getKeyForName(name, UNIFORM), x, y, z, w);
   }
   
   bool setUniformMatrixValue(const std::string& name, const MutableMatrix44D& m, bool isTransform){
   return setUniformMatrixValue(getKeyForName(name, UNIFORM), m, isTransform);
   }
   
   bool setAttributeValue(const std::string& name,
   IFloatBuffer* buffer, int attributeSize,
   int arrayElementSize, int index, bool normalized, int stride){
   return setAttributeValue(getKeyForName(name, ATTRIBUTE),
   buffer, attributeSize,
   arrayElementSize, index, normalized, stride);
   }
   
   void setAttributeEnabled(const std::string& name, bool enabled){
   setAttributeEnabled(getKeyForName(name, ATTRIBUTE), enabled);
   }
   void setAttributeDisabled(const std::string& name){
   setAttributeDisabled(getKeyForName(name, ATTRIBUTE));
   }
   */
  
};

#endif /* defined(__G3MiOSSDK__GPUProgramState__) */
