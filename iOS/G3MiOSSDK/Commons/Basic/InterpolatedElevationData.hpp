//
//  InterpolatedElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/13.
//
//

#ifndef __G3MiOSSDK__InterpolatedElevationData__
#define __G3MiOSSDK__InterpolatedElevationData__

#include "ElevationData.hpp"

class Interpolator;

class InterpolatedElevationData : public ElevationData {
private:
  const ElevationData* _elevationData;
  const bool           _deleteElevationData;

  const Interpolator* _interpolator;
  const bool          _deleteInterpolator;

public:
  InterpolatedElevationData(const ElevationData* elevationData,
                            bool deleteElevationData,
                            const Interpolator* interpolator,
                            bool deleteInterpolator);


  virtual ~InterpolatedElevationData();

  double getElevationAt(int x,
                        int y) const;

  double getElevationAt(const Angle& latitude,
                        const Angle& longitude) const;

  double getElevationAt(const Geodetic2D& position) const {
    return getElevationAt(position.latitude(),
                          position.longitude());
  }

  const Geodetic2D getRealResolution() const;

  bool hasNoData() const;

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageHeights() const;

};

#endif
