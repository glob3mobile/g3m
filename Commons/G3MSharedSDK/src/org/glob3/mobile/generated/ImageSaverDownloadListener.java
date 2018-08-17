package org.glob3.mobile.generated;public class ImageSaverDownloadListener implements IImageDownloadListener
{
  private CachedDownloader _downloader;

  private IImage _expiredImage;

  private IImageDownloadListener _listener;
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

  public ImageSaverDownloadListener(CachedDownloader downloader, IImage expiredImage, IImageDownloadListener listener, boolean deleteListener, IStorage storage, TimeInterval timeToCache)
  {
	  _downloader = downloader;
	  _expiredImage = expiredImage;
	  _listener = listener;
	  _deleteListener = deleteListener;
	  _storage = storage;
	  _timeToCache = new TimeInterval(timeToCache);

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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

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

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _storage->saveImage(url, image, _timeToCache, _downloader->saveInBackground());
		  _storage.saveImage(new URL(url), image, new TimeInterval(_timeToCache), _downloader.saveInBackground());
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: saveImage(url, image);
	  saveImage(new URL(url), image);
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: saveImage(url, image);
	  saveImage(new URL(url), image);
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
