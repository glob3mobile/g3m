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

class SubviewElevationData : public ElevationData {
private:
  const ElevationData* _elevationData;
  const bool           _ownsElevationData;

public:
  SubviewElevationData(const ElevationData *elevationData,
                       bool ownsElevationData,
                       const Sector& sector,
                       const Vector2I& resolution);

  ~SubviewElevationData();

  double getElevationAt(int x, int y) const;

  double getElevationAt(const Angle& latitude,
                        const Angle& longitude,
                        int* type) const;

  const std::string description(bool detailed) const;

};

#endif
