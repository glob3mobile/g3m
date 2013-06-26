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

public abstract class IWebSocket
{
  final private URL _url; //Conversor creates class "Url"

  private IWebSocketListener _listener;
  private final boolean _autodeleteListener;
  private final boolean _autodeleteWebSocket;


  protected IWebSocket(URL url, IWebSocketListener listener, boolean autodeleteListener, boolean autodeleteWebSocket)
  {
     _url = url;
     _listener = listener;
     _autodeleteListener = autodeleteListener;
     _autodeleteWebSocket = autodeleteWebSocket;

  }



  public final URL getURL()
  {
    return _url;
  }

  public final IWebSocketListener getListener()
  {
    return _listener;
  }

  public final boolean getAutodeleteWebSocket()
  {
    return _autodeleteWebSocket;
  }

  public void dispose()
  {
    if (_autodeleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
  }

  public abstract void send(String message);

  public abstract void close();

}