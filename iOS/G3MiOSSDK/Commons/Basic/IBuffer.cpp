//
//  IBuffer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#include "IBuffer.hpp"
#include "ILogger.hpp"

long long IBuffer::_nextbufferID = 0;

IBuffer::IBuffer(): _id(_nextbufferID++) //The id helps us to identify unambiguously the buffer
{}

IBuffer::~IBuffer(){
  ILogger::instance()->logInfo("Deleting IBuffer with id: %d", _id);
}

long long IBuffer::getID() const{
  return _id;
}