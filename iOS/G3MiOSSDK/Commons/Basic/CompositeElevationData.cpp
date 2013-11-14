//
//  CompositeElevationData.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/04/13.
//
//

#include "CompositeElevationData.hpp"
#include "Vector3D.hpp"
#include "IStringBuilder.hpp"

double CompositeElevationData::getElevationAt(int x,
                                              int y) const{
  const int size = _data.size();
  for (int i = 0; i < size; i++) {
    const double h = _data[i]->getElevationAt(x, y);
    if (!ISNAN(h)) {
      return h;
    }
  }

  return NAND;
}


void CompositeElevationData::addElevationData(ElevationData* data) {
//  ElevationData* d0 = _data[0];

  if ((data->getExtentWidth()  != _width) ||
      (data->getExtentHeight() != _height)) {
    ILogger::instance()->logError("Extents don't match.");
  }

//  Sector s = data->getSector();
//  Sector s2 = d0->getSector();

  if (!data->getSector().isEquals(getSector())) {
    ILogger::instance()->logError("Sectors don't match.");
  }

  _data.push_back(data);
  
  //Checking NoData
  for (int i = 0; i < _width; i++) {
    for (int j = 0; j < _height; j++) {
      double height = getElevationAt(i, j);
      if (ISNAN(height)) {
        _hasNoData = true;
        return;
      }
    }
  }
}

const std::string CompositeElevationData::description(bool detailed) const{
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(CompositeElevationData extent=");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString(" sector=");
  isb->addString( _sector.description() );
  if (detailed) {
    isb->addString("\n");
    for (int row = 0; row < _width; row++) {
      //isb->addString("   ");
      for (int col = 0; col < _height; col++) {
        isb->addDouble( getElevationAt(col, row) );
        isb->addString(",");
      }
      isb->addString("\n");
    }
  }
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

Vector3D CompositeElevationData::getMinMaxAverageElevations() const{
  const IMathUtils* mu = IMathUtils::instance();
  double minHeight = mu->maxDouble();
  double maxHeight = mu->minDouble();
  double sumHeight = 0.0;

  for (int i = 0; i < _width; i++) {
    for (int j = 0; j < _height; j++) {
      const double height = getElevationAt(i, j);
      if (!ISNAN(height)) {
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
