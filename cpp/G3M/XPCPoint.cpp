//
//  XPCPoint.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/19/21.
//

#include "XPCPoint.hpp"

#include "ByteBufferIterator.hpp"


XPCPoint* XPCPoint::fromByteBufferIterator(ByteBufferIterator& it,
                                           const float centerLatitudeDegrees,
                                           const float centerLongitudeDegrees,
                                           const float centerHeight) {
  const double latitudeDegrees = (double) it.nextFloat() + centerLatitudeDegrees;
  const double longitueDegrees = (double) it.nextFloat() + centerLongitudeDegrees;
  const double height          = (double) it.nextFloat() + centerHeight;

  return new XPCPoint(latitudeDegrees, longitueDegrees, height);
}


XPCPoint::XPCPoint(const double latitudeDegrees,
                   const double longitueDegrees,
                   const double height) :
_latitudeDegrees(latitudeDegrees),
_longitueDegrees(longitueDegrees),
_height(height)
{

}

XPCPoint::~XPCPoint() {

}
