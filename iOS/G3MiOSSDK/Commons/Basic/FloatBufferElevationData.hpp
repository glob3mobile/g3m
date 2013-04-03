//
//  FloatBufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#ifndef __G3MiOSSDK__FloatBufferElevationData__
#define __G3MiOSSDK__FloatBufferElevationData__

#include "BufferElevationData.hpp"
class IFloatBuffer;

class FloatBufferElevationData : public BufferElevationData {
private:
  IFloatBuffer*  _buffer;

protected:
  double getValueInBufferAt(int index) const;

public:
  FloatBufferElevationData(const Sector& sector,
                           const Vector2I& resolution,
                           double noDataValue,
                           IFloatBuffer* buffer);

  virtual ~FloatBufferElevationData();

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageHeights() const;

};

#endif
