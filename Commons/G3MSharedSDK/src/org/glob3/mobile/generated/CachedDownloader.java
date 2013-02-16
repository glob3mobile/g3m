package org.glob3.mobile.generated; 
//
//  CachedDownloader.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

//
//  CachedDownloader.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//




public class CachedDownloader extends IDownloader
{
  private IDownloader _downloader;
  private IStorage _storage;

  private int _requestsCounter;
  private int _cacheHitsCounter;
  private int _savesCounter;

  private final boolean _saveInBackground;

  private IImage getCachedImage(URL url)
  {
    //return _storage->isAvailable() ? _storage->readImage(url) : NULL;
  
  
    if ((_lastImage != null) && (_lastImageURL != null))
    {
      if (_lastImageURL.isEqualsTo(url))
      {
        // ILogger::instance()->logInfo("Used chached image for %s", url.description().c_str());
        return _lastImage.shallowCopy();
      }
    }
  
    IImage cachedImage = _storage.isAvailable() ? _storage.readImage(url) : null;
  
    if (cachedImage != null)
    {
      if (_lastImage != null)
      {
        IFactory.instance().deleteImage(_lastImage);
      }
      _lastImage = cachedImage.shallowCopy();
  
      if (_lastImageURL != null)
         _lastImageURL.dispose();
      _lastImageURL = new URL(url);
    }
  
    return cachedImage;
  }

  private IImage _lastImage;

  private URL _lastImageURL;

///#ifdef C_CODE
//  const G3MContext* _context;
///#endif
///#ifdef JAVA_CODE
//  private G3MContext _context;
///#endif
//  FrameTasksExecutor* _frameTasksExecutor;

  public CachedDownloader(IDownloader downloader, IStorage storage, boolean saveInBackground)
//  _context(NULL),
//  _frameTasksExecutor(NULL)
  {
     _downloader = downloader;
     _storage = storage;
     _requestsCounter = 0;
     _cacheHitsCounter = 0;
     _savesCounter = 0;
     _saveInBackground = saveInBackground;
     _lastImage = null;
     _lastImageURL = null;

  }

  public final boolean saveInBackground()
  {
    return _saveInBackground;
  }

  public final void start()
  {
    _downloader.start();
  }

  public final void stop()
  {
    _downloader.stop();
  }

  public final long requestBuffer(URL url, long priority, TimeInterval timeToCache, IBufferDownloadListener listener, boolean deleteListener)
  {
    _requestsCounter++;
  
    IByteBuffer cachedBuffer = _storage.isAvailable() ? _storage.readBuffer(url) : null;
    if (cachedBuffer == null)
    {
      // cache miss
      return _downloader.requestBuffer(url, priority, TimeInterval.zero(), new BufferSaverDownloadListener(this, listener, deleteListener, _storage, timeToCache), true);
    }
  
    // cache hit
    _cacheHitsCounter++;
  
    listener.onDownload(url, cachedBuffer);
  
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  
    return -1;
  }


  //class CachedDownloader_InvokeRenderer : public FrameTask {
  //private:
  //  const URL               _url;
  //  IImage*                 _image;
  //  IImageDownloadListener* _listener;
  //  const bool              _deleteListener;
  //
  //public:
  //  CachedDownloader_InvokeRenderer(const URL               url,
  //                                  IImage*                 image,
  //                                  IImageDownloadListener* listener,
  //                                  const bool              deleteListener) :
  //  _url(url),
  //  _image(image),
  //  _listener(listener),
  //  _deleteListener(deleteListener)
  //  {
  //
  //  }
  //
  //  bool isCanceled(const G3MRenderContext *rc) {
  //    return false;
  //  }
  //
  //  void execute(const G3MRenderContext* rc) {
  //    _listener->onDownload(_url, _image);
  //
  //    if (_deleteListener) {
  //      delete _listener;
  //    }
  //  }
  //};
  
  
  public final long requestImage(URL url, long priority, TimeInterval timeToCache, IImageDownloadListener listener, boolean deleteListener)
  {
    _requestsCounter++;
  
    IImage cachedImage = getCachedImage(url);
    if (cachedImage != null)
    {
      // cache hit
      _cacheHitsCounter++;
  
      listener.onDownload(url, cachedImage);
  
      if (deleteListener)
      {
        if (listener != null)
           listener.dispose();
      }
  
      return -1;
    }
  
    // cache miss
    return _downloader.requestImage(url, priority, TimeInterval.zero(), new ImageSaverDownloadListener(this, listener, deleteListener, _storage, timeToCache), true);
  }

  public final void cancelRequest(long requestId)
  {
    _downloader.cancelRequest(requestId);
  }

  public void dispose()
  {
    if (_downloader != null)
       _downloader.dispose();
  
    if (_lastImage != null)
    {
      IFactory.instance().deleteImage(_lastImage);
    }
    if (_lastImageURL != null)
       _lastImageURL.dispose();
  }

  public final String statistics()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("CachedDownloader(cache hits=");
    isb.addInt(_cacheHitsCounter);
    isb.addString("/");
    isb.addInt(_requestsCounter);
    isb.addString(", saves=");
    isb.addInt(_savesCounter);
    isb.addString(", downloader=");
    //isb->addString(IDownloader::instance()->statistics());
    isb.addString(_downloader.statistics());
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final void countSave()
  {
    _savesCounter++;
  }

  public final void onResume(G3MContext context)
  {
    _downloader.onResume(context);
  }

  public final void onPause(G3MContext context)
  {
    _downloader.onPause(context);
  }

  public final void onDestroy(G3MContext context)
  {
    _downloader.onDestroy(context);
  }

  public final void initialize(G3MContext context, FrameTasksExecutor frameTasksExecutor)
  {
  //  _context = context;
  //  _frameTasksExecutor = frameTasksExecutor;
    _downloader.initialize(context, frameTasksExecutor);
  }

}