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
#include "MercatorUtils.hpp"
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
                                                          const Sector& innerSector,
                                                          bool mercator)  {
  if (wholeSector.isNan() || innerSector.isNan()) {
    //I think that this case doesn't exist
    ILogger::instance()->logError("Testing this case: view code");
    return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
  }

  if (wholeSector.isEquals(innerSector)) {
    return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
  }

  const double lowerU = wholeSector.getUCoordinate(innerSector._lower._longitude);
  const double upperU = wholeSector.getUCoordinate(innerSector._upper._longitude);

  double upperV;
  double lowerV;
  if (mercator) {
    // a mercator image is linear in mercator V, not in latitude
    const double wholeUpperGlobalV = MercatorUtils::getMercatorV(wholeSector._upper._latitude);
    const double wholeLowerGlobalV = MercatorUtils::getMercatorV(wholeSector._lower._latitude);
    upperV = MercatorUtils::getLocalMercatorV(innerSector._upper._latitude, wholeUpperGlobalV, wholeLowerGlobalV);
    lowerV = MercatorUtils::getLocalMercatorV(innerSector._lower._latitude, wholeUpperGlobalV, wholeLowerGlobalV);
  }
  else {
    upperV = wholeSector.getVCoordinate(innerSector._upper._latitude);
    lowerV = wholeSector.getVCoordinate(innerSector._lower._latitude);
  }

  return new RectangleF((float) (lowerU            * wholeSectorWidth),
                        (float) (upperV            * wholeSectorHeight),
                        (float) ((upperU - lowerU) * wholeSectorWidth),
                        (float) ((lowerV - upperV) * wholeSectorHeight));
}
