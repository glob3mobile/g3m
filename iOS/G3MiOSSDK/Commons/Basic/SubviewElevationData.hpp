//
//  SubviewElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#ifndef __G3MiOSSDK__SubviewElevationData__
#define __G3MiOSSDK__SubviewElevationData__

#include "ElevationData.hpp"
class IFloatBuffer;

class SubviewElevationData : public ElevationData {
private:
  const ElevationData* _elevationData;
  const bool           _ownsElevationData;
  const IFloatBuffer*  _buffer;
  
  bool                 _hasNoData;

  const Geodetic2D _resolution;

  IFloatBuffer* createDecimatedBuffer();
  IFloatBuffer* createInterpolatedBuffer();

  double getElevationBoxAt(double x0, double y0,
                           double x1, double y1) const;

  const Vector2D getParentXYAt(const Geodetic2D& position) const;

public:
  SubviewElevationData(const ElevationData *elevationData,
                       bool ownsElevationData,
                       const Sector& sector,
                       const Vector2I& extent,
                       bool useDecimation);

  virtual const Geodetic2D getResolution() const {
    return _resolution;
  }


  ~SubviewElevationData();

  double getElevationAt(int x, int y,
                        double valueForNoData) const;

  double getElevationAt(const Angle& latitude,
                        const Angle& longitude,
                        double valueForNoData) const;

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageHeights() const;
  
  bool hasNoData() const{ return _hasNoData;}

};

#endif
