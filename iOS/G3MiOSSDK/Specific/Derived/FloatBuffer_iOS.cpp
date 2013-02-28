//
//  FloatBuffer_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/09/12.
//
//

#include "FloatBuffer_iOS.hpp"

#include <sstream>
#include <OpenGLES/ES2/gl.h>


const std::string FloatBuffer_iOS::description() const {
  std::ostringstream oss;

  oss << "FloatBuffer_iOS(";
  oss << "size=";
  oss << _size;
  oss << ", timestamp=";
  oss << _timestamp;
  oss << ", values=(";
//  oss << _values;
  for (int i = 0; i < _size; i++) {
    if (i > 0) {
      oss << ",";
    }
    oss << _values[i];
  }
  oss << ")";

  oss << ")";

  return oss.str();
}

FloatBuffer_iOS::~FloatBuffer_iOS() {
  delete [] _values;
//  if (_glBufferBound) {
//    const unsigned int buffers[] = {
//      _glBuffer
//    };
//
//    glDeleteBuffers(1, buffers);
//  }
}

//unsigned int FloatBuffer_iOS::getGLBuffer(int size) {
//  if (!_glBufferBound) {
//    GLuint glBuffer = 0;
//    glGenBuffers(1, &glBuffer);
//
////    glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
////    glBufferData(GL_ARRAY_BUFFER, size * 4, _values, GL_STATIC_DRAW);
//
//    _glBuffer = glBuffer;
//    _glBufferBound = true;
//  }
//  return _glBuffer;
//}
