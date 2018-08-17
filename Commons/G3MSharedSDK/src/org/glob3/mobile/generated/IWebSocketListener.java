package org.glob3.mobile.generated;//
//  IWebSocketListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IWebSocket;

public abstract class IWebSocketListener
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public void dispose()
  {
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void dispose();
//#endif


  public abstract void onOpen(IWebSocket ws);

  public abstract void onError(IWebSocket ws, String error);

  public abstract void onMessage(IWebSocket ws, String message);

  public abstract void onClose(IWebSocket ws);

}
