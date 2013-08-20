//
//  WebSocket_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//

#include "WebSocket_iOS.hpp"

#import "SRWebSocket.h"
#include "IWebSocketListener.hpp"

#import "NSString_CppAdditions.h"

@interface WebSocketDelegate : NSObject<SRWebSocketDelegate>
{
  IWebSocketListener* _listener;
  WebSocket_iOS*      _websocket;
}
@end


@implementation WebSocketDelegate

- (id) initWithListener: (IWebSocketListener*) listener
           andWebSocket: (WebSocket_iOS*) websocket
{
  self = [super init];
  if (self) {
    _listener  = listener;
    _websocket = websocket;
  }
  return self;
}

- (void) webSocket: (SRWebSocket*) webSocket
 didReceiveMessage: (id) message
{
  if (_websocket) {
    if ([message isKindOfClass:[NSString class]]) {
      _listener->onMesssage( _websocket, [message toCppString] );
    }
    else {
      NSString* msg = [NSString stringWithFormat:@"Message type not supported: %@", [message class]];
      _listener->onError( _websocket, [msg toCppString] );
    }
  }
}

- (void) webSocketDidOpen: (SRWebSocket*) webSocket
{
  _listener->onOpen(_websocket);
}

- (void) webSocket: (SRWebSocket*) webSocket
  didFailWithError: (NSError*) error
{
  if (_websocket) {
    NSString*     description = [error localizedDescription];
    _listener->onError( _websocket, [description toCppString] );
    if (_websocket->getAutodeleteWebSocket()) {
      delete _websocket;
      _websocket = NULL;
    }
  }
}

- (void) webSocket: (SRWebSocket*) webSocket
  didCloseWithCode: (NSInteger) code
            reason: (NSString*) reason
          wasClean: (BOOL) wasClean
{
  if (_websocket) {
    _listener->onClose( _websocket );
    if (_websocket->getAutodeleteWebSocket()) {
      delete _websocket;
      _websocket = NULL;
    }
  }
}

@end

WebSocket_iOS::WebSocket_iOS(const URL& url,
                             IWebSocketListener* listener,
                             bool autodeleteListener,
                             bool autodeleteWebSocket) :
IWebSocket(url, listener, autodeleteListener, autodeleteWebSocket)
{
  NSURL* nsURL = [NSURL URLWithString: [NSString stringWithCppString: getURL().getPath()] ];


  IWebSocketListener* list = getListener();

  if (nsURL) {
    _srWebSocket = [[SRWebSocket alloc] initWithURL: nsURL];
    _delegate = [[WebSocketDelegate alloc] initWithListener: list
                                               andWebSocket: this];
    [_srWebSocket setDelegate: _delegate];

    [_srWebSocket open];
  }
  else {
    listener->onError(this, "Invalid URL");
    if (getAutodeleteWebSocket()) {
      delete this;
    }
  }
}

WebSocket_iOS::~WebSocket_iOS() {
  _srWebSocket = nil;
  _delegate = nil;
}

void WebSocket_iOS::send(const std::string& message) {
  [_srWebSocket send: [NSString stringWithCppString: message] ];
}

void WebSocket_iOS::close() {
  [_srWebSocket close];
}
