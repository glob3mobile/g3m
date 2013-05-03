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
class InterpolatedElevationData;

class SubviewElevationData : public ElevationData {
private:
//  const InterpolatedElevationData* _elevationData;
//  const bool                       _ownsElevationData;
  const IFloatBuffer*              _buffer;
  
  bool _hasNoData;

  const Geodetic2D _realResolution;

  IFloatBuffer* createDecimatedBuffer(const InterpolatedElevationData& elevationData);
  IFloatBuffer* createInterpolatedBuffer(const InterpolatedElevationData& elevationData);

  double getElevationBoxAt(const InterpolatedElevationData& elevationData,
                           double x0, double y0,
                           double x1, double y1) const;

  const Vector2D getParentXYAt(const InterpolatedElevationData& elevationData,
                               const Geodetic2D& position) const;

public:
  SubviewElevationData(const ElevationData* elevationData,
                       //bool ownsElevationData,
                       const Sector& sector,
                       const Vector2I& extent,
                       bool useDecimation);

  ~SubviewElevationData();

  const Geodetic2D getRealResolution() const {
    return _realResolution;
  }

  double getElevationAt(int x, int y) const;

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageHeights() const;
  
  bool hasNoData() const{ return _hasNoData;}

};

#endif
