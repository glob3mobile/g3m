//
//  IWebSocket.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//

#ifndef __G3MiOSSDK__IWebSocket__
#define __G3MiOSSDK__IWebSocket__

#include "URL.hpp"

class IWebSocketListener;

class IWebSocket {
private:
#ifdef C_CODE
  const URL     _url;
#endif
#ifdef JAVA_CODE
  final private URL _url; //Conversor creates class "Url"
#endif

  IWebSocketListener* _listener;
  bool                _autodeleteListener;

protected:

  IWebSocket(const URL& url,
             IWebSocketListener* listener,
             bool autodeleteListener) :
  _url(url),
  _listener(listener),
  _autodeleteListener(autodeleteListener)
  {

  }

public:

  virtual ~IWebSocket();
  
};

#endif
