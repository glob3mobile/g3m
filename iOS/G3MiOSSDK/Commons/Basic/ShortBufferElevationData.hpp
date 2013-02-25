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
  IShortBuffer*  _buffer;

protected:
  double getValueInBufferAt(int index) const;

public:
  ShortBufferElevationData(const Sector& sector,
                           const Vector2I& resolution,
                           double noDataValue,
                           IShortBuffer* buffer);

  virtual ~ShortBufferElevationData();

  const std::string description(bool detailed) const;

};

#endif
