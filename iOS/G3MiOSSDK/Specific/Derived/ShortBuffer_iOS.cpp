//
//  ShortBuffer_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/18/13.
//
//

#include "ShortBuffer_iOS.hpp"


#include <sstream>

GLuint ShortBuffer_iOS::_boundIBO = -1;

long long ShortBuffer_iOS::_nextID = 0;

//long long ShortBuffer_iOS::_newCounter    = 0;
//long long ShortBuffer_iOS::_deleteCounter = 0;
//long long ShortBuffer_iOS::_genBufferCounter  = 0;
//long long ShortBuffer_iOS::_deleteBufferCounter = 0;
//
//void ShortBuffer_iOS::showStatistics() {
//  printf("ShortBuffer_iOS: new=%lld delete=%lld (delta=%lld)   genBuffer=%lld deleteBuffer=%lld (delta=%lld) \n",
//         _newCounter,
//         _deleteCounter,
//         _newCounter - _deleteCounter,
//         _genBufferCounter,
//         _deleteBufferCounter,
//         _genBufferCounter - _deleteBufferCounter);
//}

const std::string ShortBuffer_iOS::description() const {
  std::ostringstream oss;

  oss << "ShortBuffer_iOS(";
  oss << "size=";
  oss << _size;
  oss << ", timestamp=";
  oss << _timestamp;
  oss << ", values=";
  oss << _values;
  oss << ")";

  return oss.str();
}

ShortBuffer_iOS::~ShortBuffer_iOS() {
//  _deleteCounter++;
  if (_indexBufferCreated) {
//    _deleteBufferCounter++;
    glDeleteBuffers(1, &_indexBuffer);
    if (GL_NO_ERROR != glGetError()) {
      ILogger::instance()->logError("Problem deleting IBO");
    }
  }
  delete [] _values;
//  showStatistics();
}

void ShortBuffer_iOS::bindAsIBOToGPU() {
  if (!_indexBufferCreated) {
//    _genBufferCounter++;
//    showStatistics();
    glGenBuffers(1, &_indexBuffer);
    _indexBufferCreated = true;
  }

  if (_boundIBO != _indexBuffer) {
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _indexBuffer);
    _boundIBO = _indexBuffer;
  }
  //else {
  //  printf("REUSING");
  //}

  if (_indexBufferTimeStamp != _timestamp) {
    _indexBufferTimeStamp = _timestamp;
    short* index = getPointer();
    int iboSize = sizeof(short) * size();

    glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboSize, index, GL_STATIC_DRAW);
  }

  //if (GL_NO_ERROR != glGetError()) {
  //  ILogger::instance()->logError("Problem using IBO");
  //}
}
