//
//  XPCPoint.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/19/21.
//

#include "XPCPoint.hpp"

#include "ByteBufferIterator.hpp"


XPCPoint* XPCPoint::fromByteBufferIterator(ByteBufferIterator& it) {
  const double x = it.nextDouble();
  const double y = it.nextDouble();
  const double z = it.nextDouble();

  return new XPCPoint(x, y, z);
}


XPCPoint::XPCPoint(const double x,
                   const double y,
                   const double z) :
_x(x),
_y(y),
_z(z)
{

}

XPCPoint::~XPCPoint() {

}
