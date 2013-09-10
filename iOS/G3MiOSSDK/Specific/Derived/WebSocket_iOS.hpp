//
//  WebSocket_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//

#ifndef __G3MiOSSDK__WebSocket_iOS__
#define __G3MiOSSDK__WebSocket_iOS__

#include "IWebSocket.hpp"
@class SRWebSocket;
@class WebSocketDelegate;


class WebSocket_iOS : public IWebSocket {
private:
  SRWebSocket*       _srWebSocket;
  WebSocketDelegate* _delegate;

public:
  WebSocket_iOS(const URL& url,
                IWebSocketListener* listener,
                bool autodeleteListener,
                bool autodeleteWebSocket);

  void send(const std::string& message);

  void close();

  ~WebSocket_iOS();

};

#endif
