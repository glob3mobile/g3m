//
//  GPUVariableValueSet.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#ifndef __G3MiOSSDK__GPUVariableSet__
#define __G3MiOSSDK__GPUVariableSet__

#include <iostream>

#include "GPUUniform.hpp"
#include "GPUAttribute.hpp"

class GPUVariableValueSet{
private:
  GPUUniformValue* _uniformValues[32];
  GPUAttributeValue* _attributeValues[32];
  int _highestUniformKey;
  int _highestAttributeKey;

  mutable int _uniformsCode;
  mutable int _attributeCode;

public:

  GPUVariableValueSet():
  _highestAttributeKey(0),
  _highestUniformKey(0),
  _uniformsCode(0),
  _attributeCode(0){
    for (int i = 0; i < 32; i++) {
      _uniformValues[i] = NULL;
      _attributeValues[i] = NULL;
    }
  }

  void addUniformValue(GPUUniformKey key, GPUUniformValue* v){
    _uniformValues[key] = v;
    if (key > _highestUniformKey){
      _highestUniformKey = key;
    }
  }

  void addAttributeValue(GPUAttributeKey key, GPUAttributeValue* v){
    _attributeValues[key] = v;
    if (key > _highestAttributeKey){
      _highestAttributeKey = key;
    }
  }

  GPUAttributeValue* getAttributeValue(int key) const{
    return _attributeValues[key];
  }

  GPUUniformValue* getUniformValue(int key) const{
    return _uniformValues[key];
  }

  void combineWith(const GPUVariableValueSet* vs);

  void applyValuesToProgram(GPUProgram* prog) const;

  int getUniformsCode() const;

  int getAttributesCode() const;

  
};

#endif /* defined(__G3MiOSSDK__GPUVariableSet__) */
