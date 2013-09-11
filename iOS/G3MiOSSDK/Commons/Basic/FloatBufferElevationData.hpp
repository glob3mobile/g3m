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
  bool           _hasNoData;

protected:
  double getValueInBufferAt(int index) const;

public:

  static const float NO_DATA_VALUE;

  FloatBufferElevationData(const Sector& sector,
                           const Vector2I& extent,
                           const Sector& realSector,
                           const Vector2I& realExtent,
                           IFloatBuffer* buffer,
                           double deltaHeight);

  virtual ~FloatBufferElevationData();

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageElevations() const;
  
  bool hasNoData() const { return _hasNoData;}

};

#endif
