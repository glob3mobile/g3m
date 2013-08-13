//
//  IBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#ifndef __G3MiOSSDK__IBuffer__
#define __G3MiOSSDK__IBuffer__

#include <string>
#include "Disposable.hpp"

class IBuffer : public Disposable {
public:
  virtual ~IBuffer() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  /**
   Answer the size (the count of elements) of the buffer
   **/
  virtual int size() const = 0;
  
  /**
   Answer the timestamp of the buffer.
   
   This number will be different each time the buffer changes its contents.
   It provides a fast method to check if the Buffer has changed.
   **/
  virtual int timestamp() const = 0;

  virtual const std::string description() const = 0;

};

#endif
