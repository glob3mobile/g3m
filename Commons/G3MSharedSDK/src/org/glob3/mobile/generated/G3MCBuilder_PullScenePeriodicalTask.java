package org.glob3.mobile.generated; 
//class G3MCBuilder_InitializationTask : public GInitializationTask {
//private:
//  G3MCBuilder* _builder;
//  const URL    _sceneDescriptionURL;
//
//  bool _isInitialized;
//
//public:
//  G3MCBuilder_InitializationTask(G3MCBuilder* builder,
//                         const URL& sceneDescriptionURL) :
//  _builder(builder),
//  _sceneDescriptionURL(sceneDescriptionURL),
//  _isInitialized(false)
//  {
//
//  }
//
//  void run(const G3MContext* context) {
//    IDownloader* downloader = context->getDownloader();
//
//    downloader->requestBuffer(_sceneDescriptionURL,
//                              DownloadPriority::HIGHEST,
//                              TimeInterval::zero(),
//                              true,
//                              new G3MCSceneDescriptionBufferListener(_builder),
//                              true);
//  }
//
//  bool isDone(const G3MContext* context) {
//    //return _isInitialized;
//    int __FIX_IT;
//    return true;
//  }
//};


public class G3MCBuilder_PullScenePeriodicalTask extends GTask
{
  private G3MCBuilder _builder;

  private long _requestId;


  private URL getURL()
  {
    final int sceneTimestamp = _builder.getSceneTimestamp();

    final URL _sceneDescriptionURL = _builder.createSceneDescriptionURL();

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


  public G3MCBuilder_PullScenePeriodicalTask(G3MCBuilder builder)
  {
     _builder = builder;
     _requestId = -1;

  }

  public final void run(G3MContext context)
  {
    //ILogger::instance()->logInfo("G3MCPeriodicalTask executed");

    IDownloader downloader = context.getDownloader();
    if (_requestId >= 0)
    {
      downloader.cancelRequest(_requestId);
    }

    _requestId = downloader.requestBuffer(getURL(), DownloadPriority.HIGHEST, TimeInterval.zero(), true, new G3MCBuilder_SceneDescriptionBufferListener(_builder), true);
  }
}