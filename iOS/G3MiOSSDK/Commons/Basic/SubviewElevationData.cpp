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
                                           const Vector2I& resolution) :
ElevationData(sector, resolution),
_elevationData(elevationData),
_ownsElevationData(ownsElevationData)
{
}

SubviewElevationData::~SubviewElevationData() {
  if (_ownsElevationData) {
    delete _elevationData;
  }
}

double SubviewElevationData::getElevationAt(int x, int y) const {
  const Angle latitude  = Angle::fromRadians( _stepInLatitudeRadians  * y );
  const Angle longitude = Angle::fromRadians( _stepInLongitudeRadians * x );

  int type = 0;
  return getElevationAt(latitude, longitude, &type);
}

double SubviewElevationData::getElevationAt(const Angle& latitude,
                                            const Angle& longitude,
                                            int* type) const {
  if (!_sector.contains(latitude, longitude)) {
    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
                                  _sector.description().c_str(),
                                  latitude.description().c_str(),
                                  longitude.description().c_str());

//    return IMathUtils::instance()->NanD();
    return -5000;
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
