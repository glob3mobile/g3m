//
//  SubviewElevationData.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#ifndef __G3M__SubviewElevationData__
#define __G3M__SubviewElevationData__

#include "ElevationData.hpp"

//class IFloatBuffer;

class SubviewElevationData : public ElevationData {
protected:
  float* _buffer;
  bool   _hasNoData;

  SubviewElevationData(const ElevationData* elevationData,
                       const Sector& sector,
                       const Vector2I& extent);

public:

  ~SubviewElevationData();

  double getElevationAt(int x, int y) const;

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageElevations() const;
  
  bool hasNoData() const { return _hasNoData;}

};

#endif
