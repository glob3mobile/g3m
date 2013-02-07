package org.glob3.mobile.generated; 
public class ImageSaverDownloadListener implements IImageDownloadListener {
  private CachedDownloader _downloader;
  private IImageDownloadListener _listener;
  private final boolean _deleteListener;
  private IStorage _storage;
  private final TimeInterval _timeToCache;

  public ImageSaverDownloadListener(CachedDownloader downloader, IImageDownloadListener listener, boolean deleteListener, IStorage storage, TimeInterval timeToCache) {
     _downloader = downloader;
     _listener = listener;
     _deleteListener = deleteListener;
     _storage = storage;
     _timeToCache = timeToCache;

  }

  public final void deleteListener() {
    if (_deleteListener) {
      _listener = null;
    }
  }

  public final void saveImage(URL url, IImage image) {
    if (image != null) {
      if (_storage.isAvailable()) {
        //if (!_cacheStorage->containsImage(url)) {
        _downloader.countSave();

        _storage.saveImage(url, image, _timeToCache, _downloader.saveInBackground());
        //}
      }
      else {
        ILogger.instance().logWarning("The cacheStorage is not available, skipping image save.");
      }
    }
  }

  public final void onDownload(URL url, IImage image) {
    saveImage(url, image);

    _listener.onDownload(url, image);

    deleteListener();
  }

  public final void onError(URL url) {
    _listener.onError(url);

    deleteListener();
  }

  public final void onCanceledDownload(URL url, IImage image) {
    saveImage(url, image);

    _listener.onCanceledDownload(url, image);

    // no deleteListener() call, onCanceledDownload() is always called before onCancel().
  }

  public final void onCancel(URL url) {
    _listener.onCancel(url);

    deleteListener();
  }

}