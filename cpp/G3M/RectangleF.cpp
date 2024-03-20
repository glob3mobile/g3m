//
//  RectangleF.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//

#include "RectangleF.hpp"

#include "ErrorHandling.hpp"
#include "IMathUtils.hpp"
#include "IStringBuilder.hpp"
#include "Sector.hpp"
#include "Vector2D.hpp"
#include "ILogger.hpp"


RectangleF::RectangleF(float x, float y,
                       float width, float height):
_x(x),
_y(y),
_width(width),
_height(height)
{
  if ((_width < 0) || (_height < 0)) {
    THROW_EXCEPTION("Invalid rectangle extent");
  }
}

bool RectangleF::fullContains(const RectangleF& that) const {
  const IMathUtils* mu = IMathUtils::instance();

  if (!mu->isBetween(that._x, _x, _x + _width)) {
    return false;
  }

  if (!mu->isBetween(that._x + that._width, _x, _x + _width)) {
    return false;
  }

  if (!mu->isBetween(that._y, _y, _y + _height)) {
    return false;
  }

  if (!mu->isBetween(that._y + that._height, _y, _y + _height)) {
    return false;
  }

  return true;
}

bool RectangleF::fullContains(float outerX, float outerY, float outerWidth, float outerHeight,
                              float innerX, float innerY, float innerWidth, float innerHeight) {
  const IMathUtils* mu = IMathUtils::instance();

  if (!mu->isBetween(innerX, outerX, outerX + outerWidth)) {
    return false;
  }

  if (!mu->isBetween(innerX + innerWidth, outerX, outerX + outerWidth)) {
    return false;
  }

  if (!mu->isBetween(innerY, outerY, outerY + outerHeight)) {
    return false;
  }

  if (!mu->isBetween(innerY + innerHeight, outerY, outerY + outerHeight)) {
    return false;
  }

  return true;
}

const std::string RectangleF::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(RectangleF x:");
  isb->addFloat(_x);
  isb->addString(", y:");
  isb->addFloat(_y);
  isb->addString(", width:");
  isb->addFloat(_width);
  isb->addString(", height:");
  isb->addFloat(_height);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

const std::string RectangleF::id() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("RectangleF|");
  isb->addFloat(_x);
  isb->addString("|");
  isb->addFloat(_y);
  isb->addString("|");
  isb->addFloat(_width);
  isb->addString("|");
  isb->addFloat(_height);
  const std::string s = isb->getString();
  delete isb;
  return s;
}


RectangleF* RectangleF::calculateInnerRectangleFromSector(int wholeSectorWidth,
                                                          int wholeSectorHeight,
                                                          const Sector& wholeSector,
                                                          const Sector& innerSector)  {
  if (wholeSector.isNan() || innerSector.isNan()) {
    //I think that this case doesn't exist
    ILogger::instance()->logError("Testing this case: view code");
    return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
  }

  if (wholeSector.isEquals(innerSector)) {
    return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
  }

  const double widthFactor2  = innerSector._deltaLongitude.div(wholeSector._deltaLongitude);
  const double heightFactor2 = innerSector._deltaLatitude.div(wholeSector._deltaLatitude);

  const Vector2D lowerUV = wholeSector.getUVCoordinates(innerSector.getNW());
  const Vector2D upperUV = wholeSector.getUVCoordinates(innerSector.getSE());

  const double widthFactor  = (upperUV._x - lowerUV._x);
  const double heightFactor = (upperUV._y - lowerUV._y);

  //Test factors:
  const double deltaWidthFactor  = (widthFactor  - widthFactor2);
  const double deltaHeightFactor = (heightFactor - heightFactor2);

  if ( deltaWidthFactor < -0.00001 || deltaWidthFactor > 0.00001 || deltaHeightFactor < -0.00001 || deltaHeightFactor  > 0.00001) {
    ILogger::instance()->logWarning("Testing this case (view code): factors are diferents: %f and %f", widthFactor - widthFactor2, heightFactor - heightFactor2);
  }

  return new RectangleF((float) (lowerUV._x   * wholeSectorWidth),
                        (float) (lowerUV._y   * wholeSectorHeight),
                        (float) (widthFactor  * wholeSectorWidth),
                        (float) (heightFactor * wholeSectorHeight));
}

