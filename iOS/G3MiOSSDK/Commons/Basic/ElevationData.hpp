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
#include "Sector.hpp"

#include <string>

class ElevationData {
private:
  IFloatBuffer* _buffer;
  const int _width;
  const int _height;
  const Sector _sector;

public:
  ElevationData(const Sector& sector,
                const Vector2I& resolution,
                IFloatBuffer* buffer);

  virtual ~ElevationData();

  virtual float getElevationAt(int x, int y) const;

  virtual Vector2I getExtent() const;

  virtual const std::string description() const;
  
};

#endif
