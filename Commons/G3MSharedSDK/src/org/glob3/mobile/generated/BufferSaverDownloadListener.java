package org.glob3.mobile.generated; 
///#include "Context.hpp"
///#include "GTask.hpp"
///#include "IThreadUtils.hpp"
///#include "FrameTasksExecutor.hpp"

public class BufferSaverDownloadListener extends IBufferDownloadListener
{
  private CachedDownloader _downloader;
  private IBufferDownloadListener _listener;
  private final boolean _deleteListener;
  private IStorage _storage;
  private final TimeInterval _timeToCache;

  public BufferSaverDownloadListener(CachedDownloader downloader, IBufferDownloadListener listener, boolean deleteListener, IStorage storage, TimeInterval timeToCache)
  {
     _downloader = downloader;
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

  public final void saveBuffer(URL url, IByteBuffer buffer)
  {
    if (buffer != null)
    {
      if (_storage.isAvailable())
      {
        //if (!_cacheStorage->containsBuffer(url)) {
        _downloader.countSave();

        _storage.saveBuffer(url, buffer, _timeToCache, _downloader.saveInBackground());
        //}
      }
      else
      {
        ILogger.instance().logWarning("The cacheStorage is not available, skipping buffer save.");
      }
    }
  }

  public final void onDownload(URL url, IByteBuffer data)
  {
    saveBuffer(url, data);

    _listener.onDownload(url, data);

    deleteListener();
  }

  public final void onError(URL url)
  {
    _listener.onError(url);

    deleteListener();
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer)
  {
    saveBuffer(url, buffer);

    _listener.onCanceledDownload(url, buffer);

    // no deleteListener() call, onCanceledDownload() is always called before onCancel().
  }

  public final void onCancel(URL url)
  {
    _listener.onCancel(url);

    deleteListener();
  }

}