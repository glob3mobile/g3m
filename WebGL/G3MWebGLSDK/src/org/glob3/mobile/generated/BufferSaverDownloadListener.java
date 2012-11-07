package org.glob3.mobile.generated; 
public class BufferSaverDownloadListener implements IBufferDownloadListener
{
  private CachedDownloader _downloader;
  private IBufferDownloadListener _listener;
  private final boolean _deleteListener;

  public BufferSaverDownloadListener(CachedDownloader downloader, IBufferDownloadListener listener, boolean deleteListener)
  {
	  _downloader = downloader;
	  _listener = listener;
	  _deleteListener = deleteListener;

  }

  public final void deleteListener()
  {
	if (_deleteListener)
	{
	  _listener = null;
	}
  }

  public final void saveBuffer(URL url, IByteBuffer buffer)
  {
	if (buffer != null)
	{
	  if (IStorage.instance().isAvailable())
	  {
		//if (!_cacheStorage->containsBuffer(url)) {
		_downloader.countSave();

		IStorage.instance().saveBuffer(url, buffer, _downloader.saveInBackground());
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