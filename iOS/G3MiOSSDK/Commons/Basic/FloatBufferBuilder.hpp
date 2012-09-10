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
#include "Vector2D.hpp"
#include "Vector3D.hpp"

class IFloatBuffer;
class IFactory;

class FloatBufferBuilder {
protected:
  std::vector<float> _values;
  
public:
  IFloatBuffer* create() const;
};

#endif
