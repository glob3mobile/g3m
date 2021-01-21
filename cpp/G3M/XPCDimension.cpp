//
//  XPCDimension.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCDimension.hpp"

#include "ByteBufferIterator.hpp"
#include "IByteBuffer.hpp"
#include "ErrorHandling.hpp"
#include "IMathUtils.hpp"


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


const float XPCDimension::getNormalizedValue(const IByteBuffer* values,
                                             const size_t i) const {

  if (_type == "unsigned") {
    if (_size == 1) {
      return values->get(i) / 255.0f;
    }
    else if (_size == 2) {
      const unsigned char b1 = values->get( i*2     );
      const unsigned char b2 = values->get( i*2 + 1 );

      return IMathUtils::instance()->toUInt16(b1, b2) / 65535.0f;
    }
//    else if (_size == 4) {
//
//    }
//    else if (_size == 8) {
//
//    }
  }
  else if (_type == "floating") {
//    if (_size == 4) {
//
//    }
//    else if (_size == 8) {
//
//    }
  }

  THROW_EXCEPTION("Unsupported dimension type or size.");
}
