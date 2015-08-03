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

long long FloatBuffer_iOS::_newCounter    = 0;
long long FloatBuffer_iOS::_deleteCounter = 0;
long long FloatBuffer_iOS::_genBufferCounter  = 0;
long long FloatBuffer_iOS::_deleteBufferCounter = 0;

void FloatBuffer_iOS::showStatistics() {
//  printf("FloatBuffer_iOS: new=%lld delete=%lld (delta=%lld)   genBuffer=%lld deleteBuffer=%lld (delta=%lld) \n",
//         _newCounter,
//         _deleteCounter,
//         _newCounter - _deleteCounter,
//         _genBufferCounter,
//         _deleteBufferCounter,
//         _genBufferCounter - _deleteBufferCounter);
}

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

void FloatBuffer_iOS::bindAsVBOToGPU() const {
  if (!_vertexBufferCreated) {
    _genBufferCounter++;
    showStatistics();
    glGenBuffers(1, &_vertexBuffer);
    _vertexBufferCreated = true;
  }

  if (_vertexBuffer != _boundVertexBuffer) {
    glBindBuffer(GL_ARRAY_BUFFER, _vertexBuffer);
    _boundVertexBuffer = _vertexBuffer;
  }

  if (_vertexBufferTimestamp != _timestamp) {
    _vertexBufferTimestamp = _timestamp;

    float* vertices = getPointer();
    int vboSize = (int) ( sizeof(float) * size() );

    glBufferData(GL_ARRAY_BUFFER, vboSize, vertices, GL_STATIC_DRAW);
  }
}

FloatBuffer_iOS::~FloatBuffer_iOS() {
  _deleteCounter++;

  if (_vertexBufferCreated) {
    _deleteBufferCounter++;

    glDeleteBuffers(1, &_vertexBuffer);
    if (GL_NO_ERROR != glGetError()) {
      THROW_EXCEPTION("Problem deleting VBO");
    }

    if (_vertexBuffer == _boundVertexBuffer) {
      _boundVertexBuffer = -1;
    }
  }

  delete [] _values;

  showStatistics();
}


void FloatBuffer_iOS::rawPut(size_t i,
                             const IFloatBuffer* srcBuffer,
                             size_t srcFromIndex,
                             size_t count) {
  if ((i + count) > _size) {
    THROW_EXCEPTION("buffer put error");
  }

  FloatBuffer_iOS* iosSrcBuffer = (FloatBuffer_iOS*) srcBuffer;
  for (int j = 0; j < count; j++) {
    _values[i + j] = iosSrcBuffer->_values[srcFromIndex + j];
  }
}
