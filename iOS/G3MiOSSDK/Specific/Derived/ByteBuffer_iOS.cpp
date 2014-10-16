//
//  ByteBuffer_iOS.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


#include "ByteBuffer_iOS.hpp"

#include "IStringBuilder.hpp"

#include "INativeGL.hpp"


long long ByteBuffer_iOS::_nextID = 0;

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

int ByteBuffer_iOS::bindAsVBOToGPU(const INativeGL* gl) const {
  
  _gl = gl;
  
  _gl->getError();
  
  bool gen = false;
  bool data = false;
  
  if (_vertexBuffer < 0) {
    _genBufferCounter++;
    //showStatistics();
    _vertexBuffer = _gl->genBuffer();
    
    gen = true;
  }
  
  _gl->getError();
  
 _gl->bindVBO(_vertexBuffer);
  
  _gl->getError();
  
  if (_vertexBufferTimeStamp != _timestamp) {
    _vertexBufferTimeStamp = _timestamp;
    
    unsigned char* vertices = getPointer();
    int vboSize = sizeof(unsigned char) * size();
    
    glBufferData(GL_ARRAY_BUFFER, vboSize, vertices, GL_STATIC_DRAW);
    
    _gl->getError();
    
    data = true;
  }
  
  if (gen && !data){
    ILogger::instance()->logError("Byte VBO generated without associated data");
  }
  
  return _vertexBuffer;
}

ByteBuffer_iOS::~ByteBuffer_iOS() {
  _deleteCounter++;
  
  if (_vertexBuffer > -1) {
    _deleteBufferCounter++;
    
    if (_gl != NULL){
      _gl->deleteVBO(_vertexBuffer);
    }
  }
  
  delete [] _values;
  
  //showStatistics();
}