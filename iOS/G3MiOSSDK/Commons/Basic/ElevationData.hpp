//
//  ElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#ifndef __G3MiOSSDK__ElevationData__
#define __G3MiOSSDK__ElevationData__

class IFloatBuffer;
class Vector2I;

#include <string>

class ElevationData {
private:
  IFloatBuffer* _buffer;
  const int _width;
  const int _height;

public:

  ElevationData(const Vector2I& extent,
                IFloatBuffer* buffer);


  float getElevationAt(int x, int y) const;

  Vector2I getExtent() const;

  const std::string description() const;

};

#endif
