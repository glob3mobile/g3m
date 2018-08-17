package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IWebSocketListener;

public abstract class IWebSocket
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final URL _url = new URL();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  private final internal URL _url = new final(); //Conversor creates class "Url"
//#endif

  private IWebSocketListener _listener;
  private final boolean _autodeleteListener;
  private final boolean _autodeleteWebSocket;


  protected IWebSocket(URL url, IWebSocketListener listener, boolean autodeleteListener, boolean autodeleteWebSocket)
  {
	  _url = new URL(url);
	  _listener = listener;
	  _autodeleteListener = autodeleteListener;
	  _autodeleteWebSocket = autodeleteWebSocket;

  }



//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getURL() const
  public final URL getURL()
  {
	return _url;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IWebSocketListener* getListener() const
  public final IWebSocketListener getListener()
  {
	return _listener;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean getAutodeleteWebSocket() const
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
