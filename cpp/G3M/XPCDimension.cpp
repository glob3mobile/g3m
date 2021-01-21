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
//      const unsigned char b1 = values->get( i*2     );
//      const unsigned char b2 = values->get( i*2 + 1 );

      // LittleEndian
    #ifdef C_CODE
      const unsigned char b1 = values->get( i*2     );
      const unsigned char b2 = values->get( i*2 + 1 );
    #endif
    #ifdef JAVA_CODE
      final short b1 = (short) (values->get( i*2     ) & 0xFF);
      final short b2 = (short) (values->get( i*2 + 1 ) & 0xFF);
    #endif

      const int iResult = (((int) b1) |
                           ((int) (b2 << 8)));
      return  iResult / 65535.0f;
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
