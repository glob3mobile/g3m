package org.glob3.mobile.generated;import java.util.*;

public class MapBooOLDBuilder_ApplicationTubeListener implements IWebSocketListener
{
  private MapBooOLDBuilder _builder;

  public MapBooOLDBuilder_ApplicationTubeListener(MapBooOLDBuilder builder)
  {
	  _builder = builder;
  }

  public void dispose()
  {
  }

  public final void onOpen(IWebSocket ws)
  {
	ILogger.instance().logInfo("Tube '%s' opened!", ws.getURL()._path.c_str());
	_builder.setApplicationTubeOpened(true);
  }

  public final void onError(IWebSocket ws, String error)
  {
	ILogger.instance().logError("Error '%s' on Tube '%s'", error.c_str(), ws.getURL()._path.c_str());
	_builder.setApplicationTubeOpened(false);
  }

  public final void onMessage(IWebSocket ws, String message)
  {
	//ILogger::instance()->logInfo(message);
	_builder.parseApplicationJSON(message, ws.getURL());
  }

  public final void onClose(IWebSocket ws)
  {
	ILogger.instance().logError("Tube '%s' closed!", ws.getURL()._path.c_str());
	_builder.setApplicationTubeOpened(false);
  }
}
