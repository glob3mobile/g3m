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

class WebSocket_iOS : public IWebSocket {
public:
  WebSocket_iOS(const URL& url,
                IWebSocketListener* listener,
                bool autodeleteListener) :
  IWebSocket(url, listener, autodeleteListener)
  {

  }

  

};

#endif
