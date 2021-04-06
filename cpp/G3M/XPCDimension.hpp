//
//  XPCDimension.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCDimension_hpp
#define XPCDimension_hpp

#include <vector>
#include <string>

class IByteBuffer;
class ByteBufferIterator;


class XPCDimension {
public:
  const std::string   _name;
  const unsigned char _size;
  const std::string   _type;

  XPCDimension(const std::string& name,
               const unsigned char size,
               const std::string& type);

  ~XPCDimension();

  const IByteBuffer* readValues(ByteBufferIterator& it) const;

  const float getNormalizedValue(const IByteBuffer* values,
                                 const size_t valueIndex) const;

};

#endif
