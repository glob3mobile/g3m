package org.glob3.mobile.generated; 
public class G3MCBuilder_TubeSceneListener implements IWebSocketListener
{
  private G3MCBuilder _builder;

  public G3MCBuilder_TubeSceneListener(G3MCBuilder builder)
  {
     _builder = builder;
  }

  public void dispose()
  {
  }

  public final void onOpen(IWebSocket ws)
  {
  }

  public final void onError(IWebSocket ws, String error)
  {
    ILogger.instance().logError("Error '%s' on Tube '%s' Error: ", error, ws.getURL().getPath());
  }

  public final void onMesssage(IWebSocket ws, String message)
  {
    _builder.parseSceneDescription(message, ws.getURL());
  }

  public final void onClose(IWebSocket ws)
  {
    ILogger.instance().logError("Tube '%s' Closed", ws.getURL().getPath());
    int TODO_reconnect_to_tube;
  }
}