//
//  CompositeElevationData.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/04/13.
//
//

#include "CompositeElevationData.hpp"
#include "Vector3D.hpp"

double CompositeElevationData::getElevationAt(int x, int y, int *type) const{
  IMathUtils* mu = IMathUtils::instance();
  int s = _data.size();
  for (int i = 0; i < s; i++) {
    double h = _data[i]->getElevationAt(x, y, type);
    if (!mu->isNan(h)){
      return h;
    }
  }
  
  return mu->NanD();
}


void CompositeElevationData::addElevationData(ElevationData* data){
  
  ElevationData* d0 = _data[0];
  
  if (data->getExtent()._x != _width || data->getExtent()._y != _height){
    ILogger::instance()->logError("Extents don't match.");
  }
  
  Sector s = data->getSector();
  Sector s2 = d0->getSector();
  
  if (!data->getSector().isEqualsTo(getSector())){
    ILogger::instance()->logError("Sectors don't match.");
  }
  
  
  const IMathUtils* mu = IMathUtils::instance();
  _data.push_back(data);
  
  
  //Checking NoData
  int type;
  for (int i = 0; i < _width; i++) {
    for (int j = 0; j < _height; j++) {
      double height = getElevationAt(i, j, &type);
      if (mu->isNan(height)){
        _hasNoData = true;
        return;
      }
    }
  }
}

double CompositeElevationData::getElevationAt(const Angle& latitude,
                                           const Angle& longitude,
                                           int *type) const {
  
  
  if (!_sector.contains(latitude, longitude)) {
    //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
    //                                  _sector.description().c_str(),
    //                                  latitude.description().c_str(),
    //                                  longitude.description().c_str());
    return IMathUtils::instance()->NanD();
  }
  
  const IMathUtils* mu = IMathUtils::instance();
  
  int CAMBIAR;//!!!!!!!!!!!!!
  
  const Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
  const double u = mu->clamp(uv._x, 0, 1);
  const double v = mu->clamp(uv._y, 0, 1);
  const double dX = u * (_width - 1);
  //const double dY = (1.0 - v) * (_height - 1);
  const double dY = v * (_height - 1);
  
  const int x = (int) dX;
  const int y = (int) dY;
  //  const int nextX = (int) (dX + 1.0);
  //  const int nextY = (int) (dY + 1.0);
  const int nextX = x + 1;
  const int nextY = y + 1;
  const double alphaY = dY - y;
  const double alphaX = dX - x;
  
  //  if (alphaX < 0 || alphaX > 1 ||
  //      alphaY < 0 || alphaY > 1) {
  //    printf("break point\n");
  //  }
  
  
  IMathUtils *m = IMathUtils::instance();
  int unsedType = -1;
  double result;
  if (x == dX) {
    if (y == dY) {
      // exact on grid point
      result = getElevationAt(x, y, type);
    }
    else {
      
      *type = 2;
      // linear on Y
      const double heightY     = getElevationAt(x, y,     &unsedType);
      if (m->isNan(heightY)){return m->NanD();}
      const double heightNextY = getElevationAt(x, nextY, &unsedType);
      if (m->isNan(heightNextY)){return m->NanD();}
      
      if (m->isNan(heightY) || m->isNan(heightNextY)){
        return m->NanD();
      }
      
      result = mu->linearInterpolation(heightNextY, heightY, alphaY);
    }
  }
  else {
    if (y == dY) {
      
      *type = 3;
      // linear on X
      const double heightX     = getElevationAt(x,     y, &unsedType);
      if (m->isNan(heightX)){return m->NanD();}
      const double heightNextX = getElevationAt(nextX, y, &unsedType);
      if (m->isNan(heightNextX)){return m->NanD();}
      
      result = mu->linearInterpolation(heightX, heightNextX, alphaX);
    }
    else {
      
      *type = 4;
      // bilinear
      const double valueNW = getElevationAt(x,     y,     &unsedType);
      if (m->isNan(valueNW)){return m->NanD();}
      const double valueNE = getElevationAt(nextX, y,     &unsedType);
      if (m->isNan(valueNE)){return m->NanD();}
      const double valueSE = getElevationAt(nextX, nextY, &unsedType);
      if (m->isNan(valueSE)){return m->NanD();}
      const double valueSW = getElevationAt(x,     nextY, &unsedType);
      if (m->isNan(valueSW)){return m->NanD();}
      
      result = _interpolator->interpolation(valueSW,
                                                valueSE,
                                                valueNE,
                                                valueNW,
                                                alphaX,
                                                alphaY);
    }
  }
  
  return result;
}

Vector3D CompositeElevationData::getMinMaxAverageHeights() const {
  const IMathUtils* mu = IMathUtils::instance();
  double minHeight = mu->maxDouble();
  double maxHeight = mu->minDouble();
  double sumHeight = 0.0;
  
  int type;
  for (int i = 0; i < _width; i++) {
    for (int j = 0; j < _height; j++) {
      double height = getElevationAt(i, j, &type);
      if (!mu->isNan(height)){
        if (height < minHeight) {
          minHeight = height;
        }
        if (height > maxHeight) {
          maxHeight = height;
        }
        sumHeight += height;
      }
    }
  }
  
  if (minHeight == mu->maxDouble()) {
    minHeight = 0;
  }
  if (maxHeight == mu->minDouble()) {
    maxHeight = 0;
  }
  
  return Vector3D(minHeight,
                  maxHeight,
                  sumHeight / (_width * _height));
}

const std::string CompositeElevationData::description(bool detailed) const{
  return "";
}
