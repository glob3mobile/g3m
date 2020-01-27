package org.glob3.mobile.generated;
//
//  IWebSocketListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//


//class IWebSocket;

public interface IWebSocketListener
{
  void dispose();


  void onOpen(IWebSocket ws);

  void onError(IWebSocket ws, String error);

  void onMessage(IWebSocket ws, String message);

  void onClose(IWebSocket ws);

}
