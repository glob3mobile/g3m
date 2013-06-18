//
//  IWebSocketListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//

#ifndef __G3MiOSSDK__IWebSocketListener__
#define __G3MiOSSDK__IWebSocketListener__

#include <string>

class IWebSocketListener {
public:

  virtual ~IWebSocketListener() {

  }

  virtual void onOpen() = 0;

  virtual void onError(const std::string& error) = 0;

  virtual void onMesssage(const std::string& message) = 0;

  virtual void onClose() = 0;
  
};

#endif
