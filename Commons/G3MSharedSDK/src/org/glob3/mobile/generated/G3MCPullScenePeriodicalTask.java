package org.glob3.mobile.generated; 
//class G3MCInitializationTask : public GInitializationTask {
//private:
//  G3MCBuilder* _builder;
//  const URL    _sceneDescriptionURL;
//
//  bool _isInitialized;
//
//public:
//  G3MCInitializationTask(G3MCBuilder* builder,
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


public class G3MCPullScenePeriodicalTask extends GTask
{
  private G3MCBuilder _builder;
  private final URL _sceneDescriptionURL;

  private long _requestId;

  public G3MCPullScenePeriodicalTask(G3MCBuilder builder, URL sceneDescriptionURL)
  {
     _builder = builder;
     _sceneDescriptionURL = sceneDescriptionURL;
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

    _requestId = downloader.requestBuffer(_sceneDescriptionURL, DownloadPriority.HIGHEST, TimeInterval.zero(), true, new G3MCSceneDescriptionBufferListener(_builder), true);
  }
}