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
private:
  std::vector<float> _values;
  
public:
  
  void add(float value) {
    _values.push_back(value);
  }
  
  void add(double value) {
    _values.push_back((float) value);
  }
  
  void add(const Vector2D& vector) {
    _values.push_back( (float) vector.x() );
    _values.push_back( (float) vector.y() );
  }
  
  void add(const Vector3D& vector) {
    _values.push_back( (float) vector.x() );
    _values.push_back( (float) vector.y() );
    _values.push_back( (float) vector.z() );
  }
  
  IFloatBuffer* create() const;
  
};

#endif
