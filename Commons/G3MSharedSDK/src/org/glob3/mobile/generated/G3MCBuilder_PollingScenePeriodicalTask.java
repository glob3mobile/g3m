package org.glob3.mobile.generated; 
public class G3MCBuilder_PollingScenePeriodicalTask extends GTask
{
  private G3MCBuilder _builder;

  private long _requestId;


  private URL getURL()
  {
    final int sceneTimestamp = _builder.getSceneTimestamp();

    final URL _sceneDescriptionURL = _builder.createPollingSceneDescriptionURL();

    if (sceneTimestamp < 0)
    {
      return _sceneDescriptionURL;
    }

    IStringBuilder ib = IStringBuilder.newStringBuilder();

    ib.addString(_sceneDescriptionURL.getPath());
    ib.addString("?lastTs=");
    ib.addInt(sceneTimestamp);

    final String path = ib.getString();

    if (ib != null)
       ib.dispose();

    return new URL(path, false);
  }


  public G3MCBuilder_PollingScenePeriodicalTask(G3MCBuilder builder)
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

    _requestId = downloader.requestBuffer(getURL(), DownloadPriority.HIGHEST, TimeInterval.zero(), true, new G3MCBuilder_SceneDescriptionBufferListener(_builder), true);
  }
}