//
//  XPCDimension.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCDimension.hpp"

#include "ByteBufferIterator.hpp"


XPCDimension::XPCDimension(const std::string& name,
                           const unsigned char size,
                           const std::string& type) :
_name(name),
_size(size),
_type(type)
{

}

XPCDimension::~XPCDimension() {

}

const IByteBuffer* XPCDimension::readValues(ByteBufferIterator& it) const {
  const int pointsCount = it.nextInt32();

//  for (int i = 0; i < pointsCount; i++) {
//    dimensionValue;
//  }

  //  UNSIGNED 8, 4, 2, 1
  //  FLOATING 8, 4
  //
  //#error DIEGO AT WORK

  const IByteBuffer* dimensionValues = it.nextBuffer(pointsCount * _size);
  return dimensionValues;
}
