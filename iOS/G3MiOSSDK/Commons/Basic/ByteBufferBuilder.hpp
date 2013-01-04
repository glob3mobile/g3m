//
//  ByteBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

#ifndef __G3MiOSSDK__ByteBufferBuilder__
#define __G3MiOSSDK__ByteBufferBuilder__

#include <vector>
class IByteBuffer;

class ByteBufferBuilder {
private:
  std::vector<unsigned char> _values;

public:

  void addInt64(long long value) {
    const unsigned char b1 = (value      ) & 0xFF;
    const unsigned char b2 = (value >>  8) & 0xFF;
    const unsigned char b3 = (value >> 16) & 0xFF;
    const unsigned char b4 = (value >> 24) & 0xFF;
    const unsigned char b5 = (value >> 32) & 0xFF;
    const unsigned char b6 = (value >> 40) & 0xFF;
    const unsigned char b7 = (value >> 48) & 0xFF;
    const unsigned char b8 = (value >> 56) & 0xFF;

    _values.push_back(b1);
    _values.push_back(b2);
    _values.push_back(b3);
    _values.push_back(b4);
    _values.push_back(b5);
    _values.push_back(b6);
    _values.push_back(b7);
    _values.push_back(b8);
  }

  void addDouble(double value);
  
  void addInt32(int value) {
    const unsigned char b1 = (value      ) & 0xFF;
    const unsigned char b2 = (value >>  8) & 0xFF;
    const unsigned char b3 = (value >> 16) & 0xFF;
    const unsigned char b4 = (value >> 24) & 0xFF;

    _values.push_back(b1);
    _values.push_back(b2);
    _values.push_back(b3);
    _values.push_back(b4);
  }

  void setInt32(int i, int value) {
    const unsigned char b1 = (value      ) & 0xFF;
    const unsigned char b2 = (value >>  8) & 0xFF;
    const unsigned char b3 = (value >> 16) & 0xFF;
    const unsigned char b4 = (value >> 24) & 0xFF;

    _values[i    ] = b1;
    _values[i + 1] = b2;
    _values[i + 2] = b3;
    _values[i + 3] = b4;
  }

  void addStringZeroTerminated(const std::string& string);

  void add(unsigned char value) {
    _values.push_back(value);
  }

  int size() const {
    return _values.size();
  }

  IByteBuffer* create() const;
  
};

#endif
