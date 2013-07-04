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
  if ([message isKindOfClass:[NSString class]]) {
    _listener->onMesssage( _websocket, toSTDString( message ) );
  }
  else {
    NSString* msg = [NSString stringWithFormat:@"Message type not supported: %@", [message class]];
    _listener->onError( _websocket, toSTDString(msg) );
  }
}

- (void) webSocketDidOpen: (SRWebSocket*) webSocket
{
  _listener->onOpen(_websocket);
}

- (void) webSocket: (SRWebSocket*) webSocket
  didFailWithError: (NSError*) error
{
//  NSInteger     code        = [error code];
//  NSString*     domain      = [error domain];
//  NSDictionary* userInfo    = [error userInfo];
  NSString*     description = [error localizedDescription];

  _listener->onError( _websocket, toSTDString(description) );
}

- (void) webSocket: (SRWebSocket*) webSocket
  didCloseWithCode: (NSInteger) code
            reason: (NSString*) reason
          wasClean: (BOOL) wasClean
{
  _listener->onClose( _websocket );
  if (_websocket->getAutodeleteWebSocket()) {
    delete _websocket;
    _websocket = NULL;
  }
}

@end

std::string toSTDString(NSString* nsString) {
  return [nsString cStringUsingEncoding: NSUTF8StringEncoding ];
}

NSString* toNSString(const std::string& cppStr) {
  return [ NSString stringWithCString: cppStr.c_str()
                             encoding: NSUTF8StringEncoding ];
}

WebSocket_iOS::WebSocket_iOS(const URL& url,
                             IWebSocketListener* listener,
                             bool autodeleteListener,
                             bool autodeleteWebSocket) :
IWebSocket(url, listener, autodeleteListener, autodeleteWebSocket)
{
  NSURL* nsURL = [NSURL URLWithString: toNSString(getURL().getPath())];


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
  [_srWebSocket send: toNSString(message) ];
}

void WebSocket_iOS::close() {
  [_srWebSocket close];
}
