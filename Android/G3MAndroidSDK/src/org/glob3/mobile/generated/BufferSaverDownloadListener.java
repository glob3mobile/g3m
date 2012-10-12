package org.glob3.mobile.generated; 
public class BufferSaverDownloadListener implements IBufferDownloadListener
{
  private CachedDownloader _downloader;
  private IBufferDownloadListener _listener;
  private final boolean _deleteListener;
  private IStorage _cacheStorage;

  public BufferSaverDownloadListener(CachedDownloader downloader, IStorage cacheStorage, IBufferDownloadListener listener, boolean deleteListener)
  {
	  _downloader = downloader;
	  _cacheStorage = cacheStorage;
	  _listener = listener;
	  _deleteListener = deleteListener;

  }

  public final void deleteListener()
  {
	if (_deleteListener && (_listener != null))
	{
	  _listener = null;
	}
  }

  public final void saveBuffer(URL url, IByteBuffer buffer)
  {
	if (buffer != null)
	{
	  if (_cacheStorage.isAvailable())
	  {
		//if (!_cacheStorage->containsBuffer(url)) {
		_downloader.countSave();

		_cacheStorage.saveBuffer(url, buffer);
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