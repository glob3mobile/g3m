//
//  GPUVariableValueSet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#include "GPUVariableValueSet.hpp"

#include "GPUUniform.hpp"
#include "GPUAttributeValue.hpp"


GPUVariableValueSet::GPUVariableValueSet() :
_highestAttributeKey(0),
_highestUniformKey(0),
_uniformsCode(0),
_attributeCode(0)
{
  for (int i = 0; i < 32; i++) {
    _uniformValues[i]   = NULL;
    _attributeValues[i] = NULL;
  }
}

bool GPUVariableValueSet::containsUniform(GPUUniformKey key) const {
#ifdef C_CODE
  const int index = key;
#endif
#ifdef JAVA_CODE
  final int index = key.getValue();
#endif

  return _uniformValues[index] != NULL;
}

bool GPUVariableValueSet::containsAttribute(GPUAttributeKey key) const {
#ifdef C_CODE
  const int index = key;
#endif
#ifdef JAVA_CODE
  final int index = key.getValue();
#endif

  return _attributeValues[index] != NULL;
}

void GPUVariableValueSet::addUniformValue(GPUUniformKey key,
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

void GPUVariableValueSet::combineWith(const GPUVariableValueSet* vs) {
  for (int i = 0; i <= vs->_highestUniformKey; i++) {
    if (vs->_uniformValues[i] != NULL) {
      _uniformValues[i] = vs->_uniformValues[i];
      _uniformValues[i]->_retain();
      if (i > _highestUniformKey) {
        _highestUniformKey = i;
      }
    }
  }
  
  for (int i = 0; i <= vs->_highestAttributeKey; i++) {
    if (vs->_attributeValues[i] != NULL) {
      _attributeValues[i] = vs->_attributeValues[i];
      _attributeValues[i]->_retain();
      if (i > _highestAttributeKey) {
        _highestAttributeKey = i;
      }
    }
  }
}

void GPUVariableValueSet::applyValuesToProgram(GPUProgram* prog) const {
  for (int i = 0; i <= _highestUniformKey; i++) {
    GPUUniformValue* u = _uniformValues[i];
    if (u != NULL) {
      prog->setGPUUniformValue(i, u);
    }
  }
  
  for (int i = 0; i <= _highestAttributeKey; i++) {
    GPUAttributeValue* a = _attributeValues[i];
    if (a != NULL) {
      prog->setGPUAttributeValue(i, a);
    }
  }
}

GPUVariableValueSet::~GPUVariableValueSet() {
  for (int i = 0; i <= _highestUniformKey; i++) {
    GPUUniformValue* u = _uniformValues[i];
    if (u != NULL) {
      u->_release();
    }
  }
  
  for (int i = 0; i <= _highestAttributeKey; i++) {
    GPUAttributeValue* a = _attributeValues[i];
    if (a != NULL) {
      a->_release();
    }
  }
}

int GPUVariableValueSet::getUniformsCode() const {
  if (_uniformsCode == 0) {
    for (int i = 0; i <= _highestUniformKey; i++) {
      if (_uniformValues[i] != NULL) {
        _uniformsCode = _uniformsCode | GPUVariable::getUniformCode(i);
      }
    }
  }
  return _uniformsCode;
}

int GPUVariableValueSet::getAttributesCode() const {
  if (_attributeCode == 0) {
    for (int i = 0; i <= _highestAttributeKey; i++) {
      if (_attributeValues[i] != NULL) {
        _attributeCode = _attributeCode | GPUVariable::getAttributeCode(i);
      }
    }
  }
  return _attributeCode;
}

void GPUVariableValueSet::removeUniformValue(GPUUniformKey key) {
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

void GPUVariableValueSet::addAttributeValue(GPUAttributeKey key,
                                            GPUAttributeValue* v,
                                            bool mustRetain) {
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
