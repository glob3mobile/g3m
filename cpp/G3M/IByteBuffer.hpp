//
//  IByteBuffer.hpp
//  G3M
//
//  Created by José Miguel S N on 10/09/12.
//

#ifndef G3M_IByteBuffer
#define G3M_IByteBuffer

#include <string>


class IByteBuffer {
public:

  virtual ~IByteBuffer() {
  }

  virtual size_t size() const = 0;

  virtual int timestamp() const = 0;

  virtual unsigned char get(size_t i) const = 0;

  virtual void put(size_t i, unsigned char value) = 0;

  virtual void rawPut(size_t i, unsigned char value) = 0;

  virtual const std::string description() const = 0;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  virtual const std::string getAsString() const = 0;

};

#endif
