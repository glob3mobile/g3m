package org.glob3.mobile.generated;public class BufferSaverDownloadListener implements IBufferDownloadListener
{
  private CachedDownloader _downloader;

  private IByteBuffer _expiredBuffer;

  private IBufferDownloadListener _listener;
  private final boolean _deleteListener;

  private IStorage _storage;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final TimeInterval _timeToCache = new TimeInterval();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final TimeInterval _timeToCache = new internal();
//#endif

  public BufferSaverDownloadListener(CachedDownloader downloader, IByteBuffer expiredBuffer, IBufferDownloadListener listener, boolean deleteListener, IStorage storage, TimeInterval timeToCache)
  {
	  _downloader = downloader;
	  _expiredBuffer = expiredBuffer;
	  _listener = listener;
	  _deleteListener = deleteListener;
	  _storage = storage;
	  _timeToCache = new TimeInterval(timeToCache);

  }

  public void dispose()
  {
	if (_expiredBuffer != null)
		_expiredBuffer.dispose();

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

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

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _storage->saveBuffer(url, buffer, _timeToCache, _downloader->saveInBackground());
		  _storage.saveBuffer(new URL(url), buffer, new TimeInterval(_timeToCache), _downloader.saveInBackground());
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: saveBuffer(url, data);
	  saveBuffer(new URL(url), data);
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: saveBuffer(url, buffer);
	  saveBuffer(new URL(url), buffer);
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
