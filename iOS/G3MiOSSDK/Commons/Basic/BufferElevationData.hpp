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
class Interpolator;

class BufferElevationData : public ElevationData {
private:
  mutable Interpolator*  _interpolator;

  const int _bufferSize;

  const Geodetic2D _resolution;

protected:
  Interpolator*  getInterpolator() const;

  virtual double getValueInBufferAt(int index) const = 0;

public:
  BufferElevationData(const Sector& sector,
                      const Vector2I& extent,
                      int bufferSize);

  virtual ~BufferElevationData();

  const Geodetic2D getResolution() const {
    return _resolution;
  }

  double getElevationAt(const Angle& latitude,
                        const Angle& longitude,
                        double valueForNoData) const;

  virtual double getElevationAt(int x,
                                int y,
                                double valueForNoData) const;

};

#endif
