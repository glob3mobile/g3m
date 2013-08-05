package org.glob3.mobile.generated; 
public class G3MCBuilder_SceneTubeListener implements IWebSocketListener
{
  private G3MCBuilder _builder;

  public G3MCBuilder_SceneTubeListener(G3MCBuilder builder)
  {
     _builder = builder;
  }

  public void dispose()
  {
  }

  public final void onOpen(IWebSocket ws)
  {
    ILogger.instance().logError("Tube '%s' opened!", ws.getURL().getPath());
    _builder.setSceneTubeOpened(true);
  }

  public final void onError(IWebSocket ws, String error)
  {
    ILogger.instance().logError("Error '%s' on Tube '%s'", error, ws.getURL().getPath());
  }

  public final void onMesssage(IWebSocket ws, String message)
  {
    _builder.parseSceneDescription(message, ws.getURL());
  }

  public final void onClose(IWebSocket ws)
  {
    ILogger.instance().logError("Tube '%s' closed!", ws.getURL().getPath());
    _builder.setSceneTubeOpened(false);
  }
}