//
//  GPUAttributeValue.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeValue_hpp
#define GPUAttributeValue_hpp

#include "RCObject.hpp"

#include <string>

class GL;


class GPUAttributeValue : public RCObject {
protected:
  virtual ~GPUAttributeValue() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  const bool _enabled;
  const int  _type;
  const int  _attributeSize;
  const int  _index;
  const int  _stride;
  const bool _normalized;
  const int  _arrayElementSize;

  GPUAttributeValue(bool enabled) :
  _enabled(enabled),
  _type(0),
  _attributeSize(0),
  _index(0),
  _stride(0),
  _normalized(0),
  _arrayElementSize(0)
  {

  }

  GPUAttributeValue(int type,
                    int attributeSize,
                    int arrayElementSize,
                    int index,
                    int stride,
                    bool normalized) :
  _enabled(true),
  _type(type),
  _attributeSize(attributeSize),
  _index(index),
  _stride(stride),
  _normalized(normalized),
  _arrayElementSize(arrayElementSize)
  {

  }

  virtual void setAttribute(GL* gl, const int id) const = 0;

  virtual bool isEquals(const GPUAttributeValue* v) const = 0;

  virtual const std::string description() const = 0;

#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
