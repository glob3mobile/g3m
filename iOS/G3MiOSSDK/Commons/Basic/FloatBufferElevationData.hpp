//
//  FloatBufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#ifndef __G3MiOSSDK__FloatBufferElevationData__
#define __G3MiOSSDK__FloatBufferElevationData__

#include "ElevationData.hpp"
class Interpolator;
class IFloatBuffer;

class FloatBufferElevationData : public ElevationData {
private:
  IFloatBuffer*  _buffer;

  mutable Interpolator*  _interpolator;
  Interpolator*  getInterpolator() const;
  
public:
  FloatBufferElevationData(const Sector& sector,
                           const Vector2I& resolution,
                           IFloatBuffer* buffer);

  virtual ~FloatBufferElevationData();

  double getElevationAt(int x, int y) const;

  double getElevationAt(const Angle& latitude,
                        const Angle& longitude,
                        int* type) const;

  const std::string description(bool detailed) const;

};

#endif
