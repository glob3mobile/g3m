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
    
  short _max, _min;

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
    
  ShortBufferElevationData(const Sector& sector,
                           const Vector2I& extent,
                           const Sector& realSector,
                           const Vector2I& realExtent,
                           short* buffer,
                           int bufferSize,
                           double deltaHeight,
                           short max,
                           short min,
                           short children,
                           short similarity);

  virtual ~ShortBufferElevationData();

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageElevations() const;
  
  bool hasNoData() const { return _hasNoData;}

  bool hasChildren() const { if (_children > 0) return true; return false; }
  short getSimilarity() const {return _similarity; }

};

#endif
