package org.glob3.mobile.generated; 
//
//  IWebSocket.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//

//
//  IWebSocket.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/18/13.
//
//



//class IWebSocketListener;

public class IWebSocket
{
  final private URL _url; //Conversor creates class "Url"

  private IWebSocketListener _listener;
  private boolean _autodeleteListener;


  protected IWebSocket(URL url, IWebSocketListener listener, boolean autodeleteListener)
  {
     _url = url;
     _listener = listener;
     _autodeleteListener = autodeleteListener;

  }


  public void dispose()
  {
    if (_autodeleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
  }

}