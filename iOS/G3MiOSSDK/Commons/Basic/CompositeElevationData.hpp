//
//  CompositeElevationData.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/04/13.
//
//

#ifndef __G3MiOSSDK__CompositeElevationData__
#define __G3MiOSSDK__CompositeElevationData__

#include <vector>
#include <string>

#include "ElevationData.hpp"
#include "BilinearInterpolator.hpp"

#include "Vector2I.hpp"

class CompositeElevationData: public ElevationData {
private:
  std::vector<ElevationData*> _data;
  bool _hasNoData;

  Interpolator* const _interpolator;

public:
  /** ElevationData's are giving in order. First the most significant one.
   */
  CompositeElevationData(ElevationData* data):
  ElevationData(data->getSector(), data->getExtent()),
  _hasNoData(data->hasNoData()),
  _interpolator(new BilinearInterpolator())
  {
    _data.push_back(data);
  }

  void addElevationData(ElevationData* data);

  virtual ~CompositeElevationData() {
    int s = _data.size();
    for (int i = 0; i < s; i++) {
      delete _data[i];
    }
    delete _interpolator;
    
#ifdef JAVA_CODE
  super.dispose();
#endif
  }

  double getElevationAt(int x,
                        int y) const;

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageElevations() const;

  const Sector getSector() const {
    return _sector;
  }

  bool hasNoData() const{
    return _hasNoData;
  }

};

#endif
