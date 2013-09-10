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
  const bool          _autodeleteListener;
  const bool          _autodeleteWebSocket;


protected:
  IWebSocket(const URL& url,
             IWebSocketListener* listener,
             bool autodeleteListener,
             bool autodeleteWebSocket) :
  _url(url),
  _listener(listener),
  _autodeleteListener(autodeleteListener),
  _autodeleteWebSocket(autodeleteWebSocket)
  {

  }



public:
  URL getURL() const {
    return _url;
  }
  
  IWebSocketListener* getListener() const {
    return _listener;
  }

  bool getAutodeleteWebSocket() const {
    return _autodeleteWebSocket;
  }

  virtual ~IWebSocket();

  virtual void send(const std::string& message) = 0;

  virtual void close() = 0;

};

#endif
