//
//  FloatBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#ifndef __G3MiOSSDK__FloatBufferBuilder__
#define __G3MiOSSDK__FloatBufferBuilder__

#include <vector>

class IFloatBuffer;

class FloatBufferBuilder {
protected:
  std::vector<float> _values;
  
public:
  IFloatBuffer* create() const;
};

#endif
