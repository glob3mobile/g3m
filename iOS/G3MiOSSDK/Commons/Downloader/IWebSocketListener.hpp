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
class IWebSocket;

class IWebSocketListener {
public:
#ifdef C_CODE
  virtual ~IWebSocketListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif


  virtual void onOpen(IWebSocket* ws) = 0;

  virtual void onError(IWebSocket* ws,
                       const std::string& error) = 0;

  virtual void onMesssage(IWebSocket* ws,
                          const std::string& message) = 0;

  virtual void onClose(IWebSocket* ws) = 0;
  
};

#endif
