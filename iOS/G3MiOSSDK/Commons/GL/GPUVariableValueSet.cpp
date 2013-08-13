//
//  GPUVariableValueSet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#include "GPUVariableValueSet.hpp"

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

void GPUVariableValueSet::applyValuesToProgram(GPUProgram* prog) const{
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

int GPUVariableValueSet::getUniformsCode() const{
  if (_uniformsCode == 0) {
    for (int i = 0; i <= _highestUniformKey; i++) {
      if (_uniformValues[i] != NULL) {
        _uniformsCode = _uniformsCode | GPUVariable::getUniformCode(i);
      }
    }
  }
  return _uniformsCode;
}

int GPUVariableValueSet::getAttributesCode() const{
  if (_attributeCode == 0) {
    for (int i = 0; i <= _highestAttributeKey; i++) {
      if (_attributeValues[i] != NULL) {
        _attributeCode = _attributeCode | GPUVariable::getAttributeCode(i);
      }
    }
  }
  return _attributeCode;
}