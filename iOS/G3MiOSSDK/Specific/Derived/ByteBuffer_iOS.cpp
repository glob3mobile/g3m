//
//  ByteBuffer_iOS.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


#include "ByteBuffer_iOS.hpp"

#include "IStringBuilder.hpp"


long long ByteBuffer_iOS::_nextID = 0;

GLuint ByteBuffer_iOS::_boundVertexBuffer = -1;

long long ByteBuffer_iOS::_newCounter    = 0;
long long ByteBuffer_iOS::_deleteCounter = 0;
long long ByteBuffer_iOS::_genBufferCounter  = 0;
long long ByteBuffer_iOS::_deleteBufferCounter = 0;

const std::string ByteBuffer_iOS::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(ByteBuffer_iOS: size=");
  isb->addInt(_size);

//  isb->addString(" [");
//  for (int i = 0; i < _size; i++) {
//    if (i != 0) {
//      isb->addString(",");
//    }
//    isb->addInt(_values[i]);
//  }
//  isb->addString("]");

  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

const std::string ByteBuffer_iOS::getAsString() const {
  return std::string(_values, _values + _size);
}

void ByteBuffer_iOS::bindAsVBOToGPU() const {
#warning NOT WORKING
  if (!_vertexBufferCreated) {
    _genBufferCounter++;
//    showStatistics();
    glGenBuffers(1, &_vertexBuffer);
    _vertexBufferCreated = true;
  }
  
  if (_vertexBuffer != _boundVertexBuffer) {
    glBindBuffer(GL_ARRAY_BUFFER, _vertexBuffer);
    _boundVertexBuffer = _vertexBuffer;
  }
  
  if (_vertexBufferTimeStamp != _timestamp) {
    _vertexBufferTimeStamp = _timestamp;
    
    unsigned char* vertices = getPointer();
    int vboSize = sizeof(unsigned char) * size();
    
    glBufferData(GL_ARRAY_BUFFER, vboSize, vertices, GL_STATIC_DRAW);
  }
}