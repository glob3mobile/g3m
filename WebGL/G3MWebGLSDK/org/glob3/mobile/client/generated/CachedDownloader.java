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




public class CachedDownloader implements IDownloader
{
  private IDownloader _downloader;
  private IStorage _cacheStorage;

  //  const URL getCacheFileName(const URL& url) const;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String removeInvalidChars(const String& path) const
  private String removeInvalidChars(String path)
  {
	return path.replaceAll("/", "_");
  }

  private int _requestsCounter;
  private int _cacheHitsCounter;
  private int _savesCounter;

  public CachedDownloader(IDownloader downloader, IStorage cacheStorage)
  {
	  _downloader = downloader;
	  _cacheStorage = cacheStorage;
	  _requestsCounter = 0;
	  _cacheHitsCounter = 0;
	  _savesCounter = 0;

  }

  public final void start()
  {
	_downloader.start();
  }

  public final void stop()
  {
	_downloader.stop();
  }


  //const URL CachedDownloader::getCacheFileName(const URL& url) const {
  //  return URL(_cacheDirectory, removeInvalidChars(url.getPath()));
  //}
  
  public final long request(URL url, long priority, IDownloadListener listener, boolean deleteListener)
  {
	_requestsCounter++;
  
	final ByteArrayWrapper cachedBuffer = _cacheStorage.read(url);
	if (cachedBuffer == null)
	{
	  // cache miss
	  return _downloader.request(url, priority, new SaverDownloadListener(this, _cacheStorage, url, listener, deleteListener), true);
	}
	else
	{
	  // cache hit
	  _cacheHitsCounter++;
  
	  Response response = new Response(url, cachedBuffer);
  
	  listener.onDownload(response);
  
	  if (deleteListener)
	  {
		listener = null;
	  }
  
	  if (cachedBuffer != null)
		  cachedBuffer.dispose();
	  return -1;
	}
  }

  public final void cancelRequest(long requestId)
  {
	_downloader.cancelRequest(requestId);
  }

  public void dispose()
  {
  }

  public final String statistics()
  {
  
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("CachedDownloader(cache hits=").add(_cacheHitsCounter).add("/").add(_requestsCounter).add(", saves=");
	isb.add(_savesCounter).add(", downloader=").add(_downloader.statistics());
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

  public final void countSave()
  {
	_savesCounter++;
  }

}