package org.glob3.mobile.generated; 
//
//  IWebSocketListener.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//

//
//  IWebSocketListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//


//class IWebSocket;

public interface IWebSocketListener
{
  public void dispose();


  void onOpen(IWebSocket ws);

  void onError(IWebSocket ws, String error);

  void onMesssage(IWebSocket ws, String message);

  void onClose(IWebSocket ws);

}