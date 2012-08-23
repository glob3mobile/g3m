package org.glob3.mobile.generated; 
public class SaverDownloadListener implements IDownloadListener
{
  private CachedDownloader _downloader;
  private IDownloadListener _listener;
  private final boolean _deleteListener;
  private IStorage _cacheStorage;

  private URL _url = new URL();

  public SaverDownloadListener(CachedDownloader downloader, IDownloadListener listener, boolean deleteListener, IStorage cacheStorage, URL url)
  {
	  _downloader = downloader;
	  _listener = listener;
	  _deleteListener = deleteListener;
	  _cacheStorage = cacheStorage;
	  _url = url;

  }

  public final void deleteListener()
  {
	if (_deleteListener && (_listener != null))
	{
	  _listener = null;
	}
  }

  public final void saveResponse(Response response)
  {
	if (!_cacheStorage.contains(_url))
	{
	  _downloader.countSave();

	  final ByteBuffer bb = response.getByteBuffer();
	  _cacheStorage.save(_url, bb);
	}
  }

  public final void onDownload(Response response)
  {
	saveResponse(response);

	_listener.onDownload(response);

	deleteListener();
  }

  public final void onError(Response response)
  {
	_listener.onError(response);

	deleteListener();
  }

  public final void onCanceledDownload(Response response)
  {
	saveResponse(response);

	_listener.onCanceledDownload(response);

	// no deleteListener() call, onCanceledDownload() is always called before onCancel().
  }

  public final void onCancel(URL url)
  {
	_listener.onCancel(url);

	deleteListener();
  }

}