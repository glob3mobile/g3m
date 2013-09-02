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

  private IImageResult getCachedImageResult(URL url, boolean readExpired)
  {
    if ((_lastImageResult != null) && (_lastImageURL != null))
    {
      if (_lastImageURL.isEqualsTo(url))
      {
        // ILogger::instance()->logInfo("Used chached image for %s", url.description().c_str());
        return new IImageResult(_lastImageResult.getImage().shallowCopy(), _lastImageResult.isExpired());
      }
    }
  
    if (!_storage.isAvailable())
    {
      return new IImageResult(null, false);
    }
  
    IImageResult cachedImageResult = _storage.readImage(url, readExpired);
    IImage cachedImage = cachedImageResult.getImage();
  
    if (cachedImage != null)
    {
      if (_lastImageResult != null)
      {
        IFactory.instance().deleteImage(_lastImageResult.getImage());
        if (_lastImageResult != null)
           _lastImageResult.dispose();
      }
      _lastImageResult = new IImageResult(cachedImage.shallowCopy(), cachedImageResult.isExpired());
  
      if (_lastImageURL != null)
         _lastImageURL.dispose();
      _lastImageURL = new URL(url);
    }
  
    return new IImageResult(cachedImage, cachedImageResult.isExpired());
  }

  private IImageResult _lastImageResult;
  private URL _lastImageURL;

  public CachedDownloader(IDownloader downloader, IStorage storage, boolean saveInBackground)
  {
     _downloader = downloader;
     _storage = storage;
     _requestsCounter = 0;
     _cacheHitsCounter = 0;
     _savesCounter = 0;
     _saveInBackground = saveInBackground;
     _lastImageResult = null;
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

  public final long requestBuffer(URL url, long priority, TimeInterval timeToCache, boolean readExpired, IBufferDownloadListener listener, boolean deleteListener)
  {
  
    _requestsCounter++;
  
    IByteBufferResult cachedBufferResult = _storage.isAvailable() ? _storage.readBuffer(url, readExpired) : new IByteBufferResult(null, false);
    /*                                         */
    /*                                         */
  
    IByteBuffer cachedBuffer = cachedBufferResult.getBuffer();
  
    if (cachedBuffer != null && !cachedBufferResult.isExpired())
    {
      // cache hit
      _cacheHitsCounter++;
  
      listener.onDownload(url, cachedBuffer, false);
  
      if (deleteListener)
      {
        if (listener != null)
           listener.dispose();
      }
  
      return -1;
    }
  
    // cache miss
    return _downloader.requestBuffer(url, priority, TimeInterval.zero(), false, new BufferSaverDownloadListener(this, cachedBuffer, listener, deleteListener, _storage, timeToCache), true);
  }

  public final long requestImage(URL url, long priority, TimeInterval timeToCache, boolean readExpired, IImageDownloadListener listener, boolean deleteListener)
  {
    _requestsCounter++;
  
    IImageResult cachedImageResult = getCachedImageResult(url, readExpired);
    IImage cachedImage = cachedImageResult.getImage();
  
    if (cachedImage != null && !cachedImageResult.isExpired())
    {
      // cache hit
      _cacheHitsCounter++;
  
      listener.onDownload(url, cachedImage, false);
  
      if (deleteListener)
      {
        if (listener != null)
           listener.dispose();
      }
  
      return -1;
    }
  
    // cache miss
    return _downloader.requestImage(url, priority, TimeInterval.zero(), false, new ImageSaverDownloadListener(this, cachedImage, listener, deleteListener, _storage, timeToCache), true);
  
  }

  public final void cancelRequest(long requestId)
  {
    _downloader.cancelRequest(requestId);
  }

  public void dispose()
  {
    if (_downloader != null)
       _downloader.dispose();
  
    //  if (_lastImage != NULL) {
    //    IFactory::instance()->deleteImage(_lastImage);
    //  }
    if (_lastImageResult != null)
    {
      IFactory.instance().deleteImage(_lastImageResult.getImage());
      if (_lastImageResult != null)
         _lastImageResult.dispose();
    }
    if (_lastImageURL != null)
       _lastImageURL.dispose();
  
    super.dispose();
  
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
    _downloader.initialize(context, frameTasksExecutor);
  }

}