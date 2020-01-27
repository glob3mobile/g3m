//
//  FloatBufferBuilderFromCartesian2D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromCartesian2D
#define G3MiOSSDK_FloatBufferBuilderFromCartesian2D

#include "FloatBufferBuilder.hpp"

class Vector2F;


class FloatBufferBuilderFromCartesian2D : public FloatBufferBuilder {
public:
  void add(const Vector2D& vector);
  
  void add(const Vector2F& vector);
  
  void add(float x, float y);

};

#endif
