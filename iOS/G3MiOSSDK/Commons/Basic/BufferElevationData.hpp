//
//  BufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

#ifndef __G3MiOSSDK__BufferElevationData__
#define __G3MiOSSDK__BufferElevationData__

#include "ElevationData.hpp"

class BufferElevationData : public ElevationData {
protected:
  const int _bufferSize;

  virtual double getValueInBufferAt(int index) const = 0;

public:
  BufferElevationData(const Sector& sector,
                      const Vector2I& extent,
                      const Sector& realSector,
                      const Vector2I& realExtent,
                      int bufferSize);

  virtual ~BufferElevationData() {

  }

//  const Geodetic2D getRealResolution() const {
//    return _realResolution;
//  }

  double getElevationAt(int x,
                        int y) const;

};

#endif
