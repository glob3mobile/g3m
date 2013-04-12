//
//  CompositeElevationData.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/04/13.
//
//

#ifndef __G3MiOSSDK__CompositeElevationData__
#define __G3MiOSSDK__CompositeElevationData__

#include <iostream>
#include <vector>
#include <string>

#include "ElevationData.hpp"
#include "BilinearInterpolator.hpp"

class CompositeElevationData: public ElevationData{
private:
  std::vector<ElevationData*> _data;
  bool _hasNoData;
  
  Interpolator* const _interpolator;
  
public:
  /** ElevationData's are giving in order. First the most significant one.
   */
  CompositeElevationData(ElevationData* data):
  ElevationData(data->getSector(), data->getResolution()),
  _hasNoData(data->hasNoData()),
  _interpolator(new BilinearInterpolator())
  {
    if (data == NULL){
      ILogger::instance()->logError("Invalid Elevation Data in Composite");
    }
    _data.push_back(data);
  };
  
  void addElevationData(ElevationData* data);
  
  virtual ~CompositeElevationData() {
    int s = _data.size();
    for (int i = 0; i < s; i++) {
      delete _data[i];
    }
    delete _interpolator;
  }
  
  Vector2I getExtent() const{
    return _data[0]->getExtent();
  }
  
  int getExtentWidth() const {
    return _data[0]->getExtent()._x;
  }
  
  int getExtentHeight() const {
    return _data[0]->getExtent()._y;
  }
  
  double getElevationAt(int x, int y, int *type) const;
  
  double getElevationAt(const Angle& latitude,const Angle& longitude, int *type) const;
  
  const std::string description(bool detailed) const;
  
  Vector3D getMinMaxAverageHeights() const;
  
  Mesh* createMesh(const Ellipsoid* ellipsoid,
                           float verticalExaggeration,
                           const Geodetic3D& positionOffset,
                   float pointSize) const{
    return NULL;
  }
  
  const Sector getSector() const {
    return _sector;
  }
  
  bool hasNoData() const{
    return _hasNoData;
  }
  
  
};



#endif /* defined(__G3MiOSSDK__CompositeElevationData__) */
