//
//  SubviewElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "SubviewElevationData.hpp"

#include "IStringBuilder.hpp"

SubviewElevationData::SubviewElevationData(const ElevationData *elevationData,
                                           bool ownsElevationData,
                                           const Sector& sector,
                                           const Vector2I& resolution,
                                           double noDataValue) :
ElevationData(sector, resolution, noDataValue),
_elevationData(elevationData),
_ownsElevationData(ownsElevationData)
{
}

SubviewElevationData::~SubviewElevationData() {
  if (_ownsElevationData) {
    delete _elevationData;
  }
}

double SubviewElevationData::getElevationAt(int x, int y,
                                            int *type) const {
  const double u = (double) x / (_width - 1);
  const double v = (double) y / (_height - 1);
  const Geodetic2D position = _sector.getInnerPoint(u, v);

  return getElevationAt(position.latitude(),
                        position.longitude(), type);
}

double SubviewElevationData::getElevationAt(const Angle& latitude,
                                            const Angle& longitude,
                                            int *type) const {
  if (!_sector.contains(latitude, longitude)) {
    //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
    //                                  _sector.description().c_str(),
    //                                  latitude.description().c_str(),
    //                                  longitude.description().c_str());
    return _noDataValue;
  }
  return _elevationData->getElevationAt(latitude, longitude, type);
}

const std::string SubviewElevationData::description(bool detailed) const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(SubviewElevationData extent=");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString(" sector=");
  isb->addString( _sector.description() );
  isb->addString(" on ElevationData=");
  isb->addString( _elevationData->description(detailed) );
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

Vector2D SubviewElevationData::getMinMaxHeights() const {

  const IMathUtils* mu = IMathUtils::instance();

  double minHeight = mu->maxDouble();
  double maxHeight = mu->minDouble();

  int unusedType = 0;

  for (int x = 0; x < _width; x++) {
    for (int y = 0; y < _height; y++) {
      const double height = getElevationAt(x, y, &unusedType);
      if (height != _noDataValue) {
        if (height < minHeight) {
          minHeight = height;
        }
        if (height > maxHeight) {
          maxHeight = height;
        }
      }
    }
  }
  
  if (minHeight == mu->maxDouble()) {
    minHeight = 0;
  }
  if (maxHeight == mu->minDouble()) {
    maxHeight = 0;
  }

  return Vector2D(minHeight, maxHeight);
}
