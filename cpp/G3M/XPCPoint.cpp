//
//  XPCPoint.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/19/21.
//

#include "XPCPoint.hpp"

#include "ByteBufferIterator.hpp"


XPCPoint* XPCPoint::fromByteBufferIterator(ByteBufferIterator& it) {
  const double latitudeDegrees = it.nextDouble();
  const double longitueDegrees = it.nextDouble();
  const double height          = it.nextDouble();

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
