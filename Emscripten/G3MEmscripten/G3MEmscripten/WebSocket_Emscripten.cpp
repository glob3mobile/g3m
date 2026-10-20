

#include "WebSocket_Emscripten.hpp"

#include <emscripten/emscripten.h>

#include "G3M/IWebSocketListener.hpp"
#include "G3M/ILogger.hpp"

#include "EMStorage.hpp"

using namespace emscripten;


extern "C" {
EMSCRIPTEN_KEEPALIVE
void WebSocket_Emscripten_onOpen(void* webSocket) {
  ((WebSocket_Emscripten*) webSocket)->_onOpen();
}

EMSCRIPTEN_KEEPALIVE
void WebSocket_Emscripten_onError(void* webSocket,
                                  const char* error) {
  ((WebSocket_Emscripten*) webSocket)->_onError(error);
}

EMSCRIPTEN_KEEPALIVE
void WebSocket_Emscripten_onMessage(void* webSocket,
                                    const char* message) {
  ((WebSocket_Emscripten*) webSocket)->_onMessage(message);
}

EMSCRIPTEN_KEEPALIVE
void WebSocket_Emscripten_onClose(void* webSocket) {
  ((WebSocket_Emscripten*) webSocket)->_onClose();
}
}


// as WebSocket_WebGL.jsInitialize; a socket that cannot be created reports onError and onClose
// asynchronously, as the browser does for a failed connection, so the autodelete happens in one place
WebSocket_Emscripten::WebSocket_Emscripten(const URL& url,
                                           IWebSocketListener* listener,
                                           bool autodeleteListener,
                                           bool autodeleteWebSocket,
                                           bool verboseErrors) :
IWebSocket(url, listener, autodeleteListener, autodeleteWebSocket, verboseErrors),
_socket(val::null()),
_failure(val::null())
{
  const int pathID = EMStorage::put( val(getURL()._path) );

  const int resultID = EM_ASM_INT({
    var path          = document.EMStorage.take($0);
    var webSocket     = $1;
    var verboseErrors = $2;

    var fail = function(error) {
      var failure = { "cancelled" : false };
      setTimeout(function() {
        if (!failure.cancelled) {
          Module.ccall('WebSocket_Emscripten_onError', 'void', [ 'number', 'string' ], [ webSocket, error ]);
          Module.ccall('WebSocket_Emscripten_onClose', 'void', [ 'number' ], [ webSocket ]);
        }
      }, 0);
      return document.EMStorage.put({ "socket" : null, "failure" : failure });
    };

    if (typeof WebSocket === "undefined") {
      return fail("Your browser does not support WebSocket.");
    }

    var socket;
    try {
      socket = new WebSocket(path);
    }
    catch (e) {
      return fail("Invalid WebSocket URL \"" + path + "\": " + e.message);
    }

    socket.onopen = function(event) {
      Module.ccall('WebSocket_Emscripten_onOpen', 'void', [ 'number' ], [ webSocket ]);
    };

    socket.onclose = function(event) {
      if (verboseErrors) {
        console.log(event);
      }
      Module.ccall('WebSocket_Emscripten_onClose', 'void', [ 'number' ], [ webSocket ]);
    };

    socket.onerror = function(event) {
      if (verboseErrors) {
        console.log(event);
      }
      Module.ccall('WebSocket_Emscripten_onError', 'void', [ 'number', 'string' ], [ webSocket, JSON.stringify(event) ]);
    };

    socket.onmessage = function(event) {
      if (typeof event.data === "string") {
        Module.ccall('WebSocket_Emscripten_onMessage', 'void', [ 'number', 'string' ], [ webSocket, event.data ]);
      }
      else {
        Module.ccall('WebSocket_Emscripten_onError', 'void', [ 'number', 'string' ], [ webSocket, "Unsupported message type." ]);
      }
    };

    return document.EMStorage.put({ "socket" : socket, "failure" : null });
  }, pathID, this, verboseErrors);

  const val result = EMStorage::take(resultID);
  _socket  = result["socket"];
  _failure = result["failure"];
}

// a socket deleted before its close event must not call back into a deleted object
WebSocket_Emscripten::~WebSocket_Emscripten() {
  if (!_failure.isNull()) {
    _failure.set("cancelled", true);
  }
  if (!_socket.isNull()) {
    _socket.set("onopen",    val::null());
    _socket.set("onclose",   val::null());
    _socket.set("onerror",   val::null());
    _socket.set("onmessage", val::null());
    _socket.call<void>("close");
  }
}

void WebSocket_Emscripten::send(const std::string& message) {
  if (_socket.isNull() || (_socket["readyState"].as<int>() != _socket["OPEN"].as<int>())) {
    ILogger::instance()->logError("WebSocket_Emscripten: can't send on a WebSocket that is not open (%s)",
                                  getURL()._path.c_str());
    return;
  }
  _socket.call<void>("send", val(message));
}

void WebSocket_Emscripten::close() {
  if (!_socket.isNull()) {
    _socket.call<void>("close");
  }
}

void WebSocket_Emscripten::_onOpen() {
  getListener()->onOpen(this);
}

void WebSocket_Emscripten::_onError(const std::string& error) {
  getListener()->onError(this, error);
}

void WebSocket_Emscripten::_onMessage(const std::string& message) {
  getListener()->onMessage(this, message);
}

// as WebSocket_iOS: the browser always fires close after error, so this is the only autodelete point
void WebSocket_Emscripten::_onClose() {
  getListener()->onClose(this);
  if (getAutodeleteWebSocket()) {
    _socket  = val::null();
    _failure = val::null();
    delete this;
  }
}
