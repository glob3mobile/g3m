#ifndef WebSocket_Emscripten_hpp
#define WebSocket_Emscripten_hpp

#include "G3M/IWebSocket.hpp"

#include <emscripten/val.h>


class WebSocket_Emscripten : public IWebSocket {
private:
  emscripten::val _socket;
  emscripten::val _failure;

public:
  WebSocket_Emscripten(const URL& url,
                       IWebSocketListener* listener,
                       bool autodeleteListener,
                       bool autodeleteWebSocket,
                       bool verboseErrors);

  ~WebSocket_Emscripten();

  void send(const std::string& message);

  void close();

  void _onOpen();

  void _onError(const std::string& error);

  void _onMessage(const std::string& message);

  void _onClose();

};

#endif
