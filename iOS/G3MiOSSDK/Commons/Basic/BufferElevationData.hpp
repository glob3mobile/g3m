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
protected:
  Interpolator*  getInterpolator() const;

public:
  BufferElevationData(const Sector& sector,
                      const Vector2I& resolution,
                      double noDataValue);

  virtual ~BufferElevationData();

  double getElevationAt(const Angle& latitude,
                        const Angle& longitude) const;

  virtual double getElevationAt(int x, int y) const = 0;

};

#endif
