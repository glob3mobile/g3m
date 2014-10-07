package org.glob3.mobile.generated; 
public class MapBooBuilder_ApplicationTubeListener implements IWebSocketListener
{
  private MapBooBuilder _builder;

  public MapBooBuilder_ApplicationTubeListener(MapBooBuilder builder)
  {
     _builder = builder;
  }

  public void dispose()
  {
  }

  public final void onOpen(IWebSocket ws)
  {
    ILogger.instance().logInfo("Tube '%s' opened!", ws.getURL()._path);
    _builder.setApplicationTubeOpened(true);
  }

  public final void onError(IWebSocket ws, String error)
  {
    ILogger.instance().logError("Error '%s' on Tube '%s'", error, ws.getURL()._path);
    _builder.setApplicationTubeOpened(false);
  }

  public final void onMesssage(IWebSocket ws, String message)
  {
    //ILogger::instance()->logInfo(message);
    _builder.parseApplicationJSON(message, ws.getURL());
  }

  public final void onClose(IWebSocket ws)
  {
    ILogger.instance().logError("Tube '%s' closed!", ws.getURL()._path);
    _builder.setApplicationTubeOpened(false);
  }
}