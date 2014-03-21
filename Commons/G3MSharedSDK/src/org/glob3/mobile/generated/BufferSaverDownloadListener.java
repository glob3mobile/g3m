package org.glob3.mobile.generated; 
public class BufferSaverDownloadListener extends IBufferDownloadListener
{
  private CachedDownloader _downloader;

  private IByteBuffer _expiredBuffer;

  private IBufferDownloadListener _listener;
  private final boolean _deleteListener;

  private IStorage _storage;

  private final TimeInterval _timeToCache;

  public BufferSaverDownloadListener(CachedDownloader downloader, IByteBuffer expiredBuffer, IBufferDownloadListener listener, boolean deleteListener, IStorage storage, TimeInterval timeToCache)
  {
     _downloader = downloader;
     _expiredBuffer = expiredBuffer;
     _listener = listener;
     _deleteListener = deleteListener;
     _storage = storage;
     _timeToCache = timeToCache;

  }

  public void dispose()
  {
    if (_expiredBuffer != null)
       _expiredBuffer.dispose();

  super.dispose();

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
    if (!url.isFileProtocol())
    {
      if (buffer != null)
      {
        if (_storage.isAvailable())
        {
          _downloader.countSave();

          _storage.saveBuffer(url, buffer, _timeToCache, _downloader.saveInBackground());
        }
        else
        {
          ILogger.instance().logWarning("The cacheStorage is not available, skipping buffer save.");
        }
      }
    }
  }

  public final void onDownload(URL url, IByteBuffer data, boolean expired)
  {
    if (!expired)
    {
      saveBuffer(url, data);
    }

    _listener.onDownload(url, data, expired);

    deleteListener();
  }

  public final void onError(URL url)
  {
    if (_expiredBuffer == null)
    {
      _listener.onError(url);
    }
    else
    {
      _listener.onDownload(url, _expiredBuffer, true);
      _expiredBuffer = null;
    }

    deleteListener();
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    if (!expired)
    {
      saveBuffer(url, buffer);
    }

    _listener.onCanceledDownload(url, buffer, expired);

    // no deleteListener() call, onCanceledDownload() is always called before onCancel().
  }

  public final void onCancel(URL url)
  {
    _listener.onCancel(url);

    deleteListener();
  }

}