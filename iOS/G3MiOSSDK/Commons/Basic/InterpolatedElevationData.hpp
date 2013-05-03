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

  Interpolator* _interpolator;

  Interpolator* getInterpolator();

public:
  InterpolatedElevationData(const ElevationData* elevationData,
                            bool deleteElevationData);


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
