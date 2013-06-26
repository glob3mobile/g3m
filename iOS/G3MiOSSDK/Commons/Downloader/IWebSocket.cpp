//
//  IWebSocket.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//

#include "IWebSocket.hpp"

#include "IWebSocketListener.hpp"

#include "IStringBuilder.hpp"

IWebSocket::~IWebSocket() {
  if (_autodeleteListener) {
    delete _listener;
  }
}
