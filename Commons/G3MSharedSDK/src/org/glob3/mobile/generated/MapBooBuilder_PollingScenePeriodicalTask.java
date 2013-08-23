package org.glob3.mobile.generated; 
public class MapBooBuilder_PollingScenePeriodicalTask extends GTask
{
  private MapBooBuilder _builder;

  private long _requestId;


  private URL getURL()
  {
    final int applicationTimestamp = _builder.getApplicationTimestamp();

    final URL _sceneDescriptionURL = _builder.createPollingApplicationDescriptionURL();

    if (applicationTimestamp < 0)
    {
      return _sceneDescriptionURL;
    }

    IStringBuilder ib = IStringBuilder.newStringBuilder();

    ib.addString(_sceneDescriptionURL.getPath());
    ib.addString("?lastTs=");
    ib.addInt(applicationTimestamp);

    final String path = ib.getString();

    if (ib != null)
       ib.dispose();

    return new URL(path, false);
  }


  public MapBooBuilder_PollingScenePeriodicalTask(MapBooBuilder builder)
  {
     _builder = builder;
     _requestId = -1;

  }

  public final void run(G3MContext context)
  {
    IDownloader downloader = context.getDownloader();
    if (_requestId >= 0)
    {
      downloader.cancelRequest(_requestId);
    }

    _requestId = downloader.requestBuffer(getURL(), DownloadPriority.HIGHEST, TimeInterval.zero(), true, new MapBooBuilder_SceneDescriptionBufferListener(_builder), true);
  }
}