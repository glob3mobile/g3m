//
//  IWebSocketListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//

#ifndef __G3M__IWebSocketListener__
#define __G3M__IWebSocketListener__

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

  virtual void onMessage(IWebSocket* ws,
                         const std::string& message) = 0;

  virtual void onClose(IWebSocket* ws) = 0;

};

#endif
