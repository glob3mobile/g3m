//
//  GPUVariableValueSet.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#ifndef __G3MiOSSDK__GPUVariableSet__
#define __G3MiOSSDK__GPUVariableSet__

class GPUUniformValue;
class GPUAttributeValue;
class GPUProgram;

#include "GPUUniformKey.hpp"
#include "GPUAttributeKey.hpp"


class GPUVariableValueSet {
private:
  GPUUniformValue*   _uniformValues[32];
  GPUAttributeValue* _attributeValues[32];

  int _highestUniformKey;
  int _highestAttributeKey;

  mutable int _uniformsCode;
  mutable int _attributeCode;

  GPUVariableValueSet(const GPUVariableValueSet& that);
  GPUVariableValueSet& operator=(const GPUVariableValueSet& that);

public:

  GPUVariableValueSet();

  ~GPUVariableValueSet();

  bool containsUniform(GPUUniformKey key) const;

  bool containsAttribute(GPUAttributeKey key) const;

  void addUniformValue(GPUUniformKey key,
                       GPUUniformValue* v,
                       bool mustRetain);

  void removeUniformValue(GPUUniformKey key);

  void addAttributeValue(GPUAttributeKey key,
                         GPUAttributeValue* v,
                         bool mustRetain);

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
  
};

#endif
