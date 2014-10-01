//
//  ByteBuffer_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ByteBuffer_iOS
#define G3MiOSSDK_ByteBuffer_iOS

#include "IByteBuffer.hpp"
#include "ILogger.hpp"

#include <OpenGLES/ES2/gl.h>

class ByteBuffer_iOS : public IByteBuffer {
private:
  const int            _size;
  unsigned char* const _values;
  int                  _timestamp;
  
  //ID
  const long long _id;
  static long long _nextID;
  
  //VBO
  static GLuint     _boundVertexBuffer;
  mutable bool      _vertexBufferCreated;
  mutable GLuint    _vertexBuffer; //VBO
  mutable int       _vertexBufferTimeStamp;
  
  //Counters
  static long long _newCounter;
  static long long _deleteCounter;
  static long long _genBufferCounter;
  static long long _deleteBufferCounter;

public:
  ByteBuffer_iOS(int size) :
  _values(new unsigned char[size]),
  _size(size),
  _timestamp(-1),
  _id(_nextID++)
  {
    if (_values == NULL) {
      ILogger::instance()->logError("Allocating error.");
    }
  }
  
  long long getID() const{
    return _id;
  }
  
  ByteBuffer_iOS(unsigned char* values,
                 int size) :
  _values(values),
  _size(size),
  _timestamp(-1),
  _id(_nextID++)
  {

  }

  virtual ~ByteBuffer_iOS() {
    _deleteCounter++;
    
    if (_vertexBufferCreated) {
      _deleteBufferCounter++;
      
      glDeleteBuffers(1, &_vertexBuffer);
      if (GL_NO_ERROR != glGetError()) {
        ILogger::instance()->logError("Problem deleting VBO");
      }
      
      if (_vertexBuffer == _boundVertexBuffer) {
        _boundVertexBuffer = -1;
      }
    }
    
    delete [] _values;
    
//    showStatistics();
  }

  int size() const {
    return _size;
  }

  int timestamp() const {
    return _timestamp;
  }

  unsigned char get(int i) const {
    if (i < 0 || i > _size) {
      ILogger::instance()->logError("Buffer Get error.");
    }

    return _values[i];
  }

  void put(int i,
           unsigned char value) {
    if (i < 0 || i > _size) {
      ILogger::instance()->logError("Buffer Put error.");
    }

    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }

  void rawPut(int i,
              unsigned char value) {
    if (i < 0 || i > _size) {
      ILogger::instance()->logError("Buffer Put error.");
    }

    _values[i] = value;
  }

  unsigned char* getPointer() const {
    return _values;
  }

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  const std::string getAsString() const;
  
  void bindAsVBOToGPU() const;
  
};

#endif
