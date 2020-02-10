//
//  GPUAttributeValueVecFloat.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeValueVecFloat_hpp
#define GPUAttributeValueVecFloat_hpp

#include "GPUAttributeValue.hpp"

class IFloatBuffer;


class GPUAttributeValueVecFloat : public GPUAttributeValue {
private:
  const IFloatBuffer* _buffer;
  const int           _timestamp;
  const long long     _id;

protected:
  ~GPUAttributeValueVecFloat() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueVecFloat(const IFloatBuffer* buffer,
                            int attributeSize,
                            int arrayElementSize,
                            int index,
                            int stride,
                            bool normalized);

  void setAttribute(GL* gl,
                    const int id) const;

  bool isEquals(const GPUAttributeValue* v) const;

  const std::string description() const;

};

#endif
