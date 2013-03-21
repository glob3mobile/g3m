//
//  SubviewElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "SubviewElevationData.hpp"

#include "IStringBuilder.hpp"
#include "IFloatBuffer.hpp"
#include "IFactory.hpp"

SubviewElevationData::SubviewElevationData(const ElevationData *elevationData,
                                           bool ownsElevationData,
                                           const Sector& sector,
                                           const Vector2I& resolution,
                                           double noDataValue,
                                           bool useDecimation) :
ElevationData(sector, resolution, noDataValue),
_elevationData(elevationData),
_ownsElevationData(ownsElevationData)
{
  if (useDecimation) {
    _buffer = createDecimatedBuffer();
  }
  else {
    _buffer = NULL;
  }
}

const Vector2D SubviewElevationData::getParentXYAt(const Geodetic2D& position) const {
  const Sector parentSector = _elevationData->getSector();
  const Geodetic2D parentLower = parentSector.lower();

  const double parentX = (
                          ( position.longitude().radians() - parentLower.longitude().radians() )
                          / parentSector.getDeltaLongitude().radians()
                          * _elevationData->getExtentWidth()
                          );

  const double parentY = (
                          ( position.latitude().radians() - parentLower.latitude().radians() )
                          / parentSector.getDeltaLatitude().radians()
                          * _elevationData->getExtentHeight()
                          );

  return Vector2D(parentX, parentY);
}

double SubviewElevationData::getElevationBoxAt(double x0, double y0,
                                               double x1, double y1) const {
//  aaa;

  const IMathUtils* mu = IMathUtils::instance();

  const double floorY0 = mu->floor(y0);
  const double ceilY1  = mu->ceil(y1);
  const double floorX0 = mu->floor(x0);
  const double ceilX1  = mu->ceil(x1);

  if (floorY0 < 0 || ceilY1 >= _elevationData->getExtentHeight()) {
    return 0;
  }
  if (floorX0 < 0 || ceilX1 >= _elevationData->getExtentWidth()) {
    return 0;
  }

  int unusedType = -1;

  double heightSum = 0;
  double area = 0;

  const double maxX = _elevationData->getExtentWidth()  - 1;
  const double maxY = _elevationData->getExtentHeight() - 1;

  for (double y=floorY0; y <= ceilY1; y++) {
    double ysize = 1.0;
    if (y < y0) {
      ysize *= (1.0 - (y0-y));
    }
    if (y > y1) {
      ysize *= (1.0 - (y-y1));
    }

    for (double x=floorX0; x <= ceilX1; y++) {
      double size = ysize;
      const double height = _elevationData->getElevationAt((int) mu->min( x, maxX ),
                                                           (int) mu->min( y, maxY ),
                                                           &unusedType);
      if (x < x0) {
        size *= (1.0 - (x0-x));
      }
      if (x > x1) {
        size *= (1.0 - (x-x1));
      }
      heightSum += height * size;
      area += size;
    }
  }

  return heightSum/area;
}

IFloatBuffer* SubviewElevationData::createDecimatedBuffer() const {
  IFloatBuffer* buffer = IFactory::instance()->createFloatBuffer(_width * _height);

  const Vector2D parentXYAtLower = getParentXYAt(_sector.lower());
  const Vector2D parentXYAtUpper = getParentXYAt(_sector.upper());
  const Vector2D parentDeltaXY = parentXYAtUpper.sub(parentXYAtLower);

  for (int x = 0; x < _width; x++) {
    const double u0 = (double) x     / (_width - 1);
    const double u1 = (double) (x+1) / (_width - 1);
    const double x0 = u0 * parentDeltaXY._x + parentXYAtLower._x;
    const double x1 = u1 * parentDeltaXY._x + parentXYAtLower._x;

    for (int y = 0; y < _height; y++) {
      const double v0 = (double) y     / (_height - 1);
      const double v1 = (double) (y+1) / (_height - 1);
      const double y0 = v0 * parentDeltaXY._y + parentXYAtLower._y;
      const double y1 = v1 * parentDeltaXY._y + parentXYAtLower._y;

      const int index = ((_height-1-y) * _width) + x;

      const double height = getElevationBoxAt(x0, y0,
                                              x1, y1);
      buffer->put(index, (float) height);
    }
  }

  return buffer;
}

SubviewElevationData::~SubviewElevationData() {
  if (_ownsElevationData) {
    delete _elevationData;
  }
  delete _buffer;
}

double SubviewElevationData::getElevationAt(int x, int y,
                                            int *type) const {

  if (_buffer != NULL) {
    const int index = ((_height-1-y) * _width) + x;

    if ( (index < 0) || (index >= _buffer->size()) ) {
      printf("break point on me\n");
      *type = 0;
      return _noDataValue;
    }
    *type = 1;
    return _buffer->get(index);
  }

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
