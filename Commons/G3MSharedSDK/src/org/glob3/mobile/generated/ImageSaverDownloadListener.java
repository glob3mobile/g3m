package org.glob3.mobile.generated; 
public class ImageSaverDownloadListener extends IImageDownloadListener
{
  private CachedDownloader _downloader;

  private IImage _expiredImage;

  private IImageDownloadListener _listener;
  private final boolean _deleteListener;

  private IStorage _storage;

  private final TimeInterval _timeToCache;

  public ImageSaverDownloadListener(CachedDownloader downloader, IImage expiredImage, IImageDownloadListener listener, boolean deleteListener, IStorage storage, TimeInterval timeToCache)
  {
     _downloader = downloader;
     _expiredImage = expiredImage;
     _listener = listener;
     _deleteListener = deleteListener;
     _storage = storage;
     _timeToCache = timeToCache;

  }

  public final void deleteListener()
  {
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
      _listener = null;
    }
  }

  public void dispose()
  {
    if (_expiredImage != null)
       _expiredImage.dispose();

  super.dispose();

  }

  public final void saveImage(URL url, IImage image)
  {
    if (!url.isFileProtocol())
    {
      if (image != null)
      {
        if (_storage.isAvailable())
        {
          _downloader.countSave();

          _storage.saveImage(url, image, _timeToCache, _downloader.saveInBackground());
        }
        else
        {
          ILogger.instance().logWarning("The cacheStorage is not available, skipping image save.");
        }
      }
    }
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    if (!expired)
    {
      saveImage(url, image);
    }

    _listener.onDownload(url, image, expired);

    deleteListener();
  }

  public final void onError(URL url)
  {
    if (_expiredImage == null)
    {
      _listener.onError(url);
    }
    else
    {
      _listener.onDownload(url, _expiredImage, true);
      _expiredImage = null;
    }

    deleteListener();
  }

  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {
    if (!expired)
    {
      saveImage(url, image);
    }

    _listener.onCanceledDownload(url, image, expired);

    // no deleteListener() call, onCanceledDownload() is always called before onCancel().
  }

  public final void onCancel(URL url)
  {
    _listener.onCancel(url);

    deleteListener();
  }

}