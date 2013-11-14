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

long long FloatBuffer_iOS::_nextID = 0;
GLuint FloatBuffer_iOS::_boundVertexBuffer = -1;

const std::string FloatBuffer_iOS::description() const {
  std::ostringstream oss;

  oss << "FloatBuffer_iOS(";
  oss << "size=";
  oss << _size;
  oss << ", timestamp=";
  oss << _timestamp;
  oss << ", values=(";
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
  if (_vertexBufferCreated) {
    glDeleteBuffers(1, &_vertexBuffer);
    if (GL_NO_ERROR != glGetError()) {
      ILogger::instance()->logError("Problem deleting VBO");
    }

    if (_vertexBuffer == _boundVertexBuffer) {
      _boundVertexBuffer = -1;
    }
  }

  delete [] _values;
}
