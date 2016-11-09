//
//  ShortBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/13.
//
//

#include "ShortBufferBuilder.hpp"

#include "IFactory.hpp"
#include "IShortBuffer.hpp"
#include "IStringBuilder.hpp"


IShortBuffer* ShortBufferBuilder::create() const {
#ifdef C_CODE
  const size_t size = _values.size();

  IShortBuffer* result = IFactory::instance()->createShortBuffer(size);

  for (size_t i = 0; i < size; i++) {
    result->rawPut(i, _values.at(i));
  }

  return result;
#endif
#ifdef JAVA_CODE
  return IFactory.instance().createShortBuffer( _values._array, _values._size );
#endif
}

const std::string ShortBufferBuilder::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("ShortBufferBuilder: ");
  for (int i = 0; i < (int)_values.size(); i++) {

#ifdef C_CODE
    short v = _values[i];
#endif
#ifdef JAVA_CODE
    short v = _values.get(i);
#endif
    isb->addInt(v);
    isb->addString(", ");
  }
  const std::string s = isb->getString();
  delete isb;
  return s;
}
