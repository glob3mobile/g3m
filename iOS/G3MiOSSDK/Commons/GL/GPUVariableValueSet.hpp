//
//  GPUVariableValueSet.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#ifndef __G3MiOSSDK__GPUVariableSet__
#define __G3MiOSSDK__GPUVariableSet__

#include "GPUUniform.hpp"
#include "GPUAttribute.hpp"

class GPUVariableValueSet {
private:
  GPUUniformValue*   _uniformValues[32];
  GPUAttributeValue* _attributeValues[32];
  int _highestUniformKey;
  int _highestAttributeKey;

  mutable int _uniformsCode;
  mutable int _attributeCode;
  mutable std::string _customShaderName;

  GPUVariableValueSet(const GPUVariableValueSet& that);
  GPUVariableValueSet& operator=(const GPUVariableValueSet& that);

public:

  GPUVariableValueSet():
  _highestAttributeKey(0),
  _highestUniformKey(0),
  _uniformsCode(0),
  _attributeCode(0),
  _customShaderName("") {
    for (int i = 0; i < 32; i++) {
      _uniformValues[i]   = NULL;
      _attributeValues[i] = NULL;
    }
  }

  ~GPUVariableValueSet();

  bool containsUniform(GPUUniformKey key) const {
#ifdef C_CODE
    const int index = key;
#endif
#ifdef JAVA_CODE
    final int index = key.getValue();
#endif

    return _uniformValues[index] != NULL;
  }

  bool containsAttribute(GPUAttributeKey key) const {
#ifdef C_CODE
    const int index = key;
#endif
#ifdef JAVA_CODE
    final int index = key.getValue();
#endif

    return _attributeValues[index] != NULL;
  }

  void addUniformValue(GPUUniformKey key,
                       GPUUniformValue* v,
                       bool mustRetain) {
#ifdef C_CODE
    const int index = key;
#endif
#ifdef JAVA_CODE
    final int index = key.getValue();
#endif

    _uniformValues[index] = v;
    if (mustRetain) {
      v->_retain();
    }
    if (index > _highestUniformKey) {
      _highestUniformKey = index;
    }
  }

  void removeUniformValue(GPUUniformKey key) {
#ifdef C_CODE
    const int index = key;
#endif
#ifdef JAVA_CODE
    final int index = key.getValue();
#endif

    if (_uniformValues[index] != NULL) {
      _uniformValues[index]->_release();
      _uniformValues[index] = NULL;
    }

    for (int i = 0; i < 32; i++) {
      if (_uniformValues[i] != NULL) {
        _highestUniformKey = i;
      }
    }
  }

  void addAttributeValue(GPUAttributeKey key, GPUAttributeValue* v, bool mustRetain) {
#ifdef C_CODE
    const int index = key;
#endif
#ifdef JAVA_CODE
    final int index = key.getValue();
#endif
    _attributeValues[index] = v;
    if (mustRetain) {
      v->_retain();
    }
    if (index > _highestAttributeKey) {
      _highestAttributeKey = index;
    }
  }

  GPUAttributeValue* getAttributeValue(int key) const {
    return _attributeValues[key];
  }

  GPUUniformValue* getUniformValue(int key) const {
    return _uniformValues[key];
  }

  void combineWith(const GPUVariableValueSet* vs);

  void applyValuesToProgram(GPUProgram* prog) const;

  int getUniformsCode() const;

  int getAttributesCode() const;
  
  bool hasCustomShader() const {
    return (_customShaderName.length() != 0);
  }
    
  void setCustomShaderName(const std::string& name) {
    _customShaderName = name;
  }
    
  const std::string getCustomShaderName() const {
    return _customShaderName;
  }
};

#endif
