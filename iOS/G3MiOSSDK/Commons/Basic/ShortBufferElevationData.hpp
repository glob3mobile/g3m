//
//  ShortBufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

#ifndef __G3MiOSSDK__ShortBufferElevationData__
#define __G3MiOSSDK__ShortBufferElevationData__

#include "BufferElevationData.hpp"
class IShortBuffer;

class ShortBufferElevationData : public BufferElevationData {
private:
  short*  _buffer;
  bool    _hasNoData;

protected:
  double getValueInBufferAt(int index) const;

public:

  static const short NO_DATA_VALUE;

  ShortBufferElevationData(const Sector& sector,
                           const Vector2I& extent,
                           const Sector& realSector,
                           const Vector2I& realExtent,
                           short* buffer,
                           int bufferSize,
                           double deltaHeight);

  virtual ~ShortBufferElevationData();

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageElevations() const;
  
  bool hasNoData() const { return _hasNoData;}

};

#endif
