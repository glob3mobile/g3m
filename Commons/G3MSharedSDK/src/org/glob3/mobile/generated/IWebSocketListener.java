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

public abstract class IWebSocketListener
{

  public void dispose()
  {

  }

  public abstract void onOpen(IWebSocket ws);

  public abstract void onError(IWebSocket ws, String error);

  public abstract void onMesssage(IWebSocket ws, String message);

  public abstract void onClose(IWebSocket ws);

}