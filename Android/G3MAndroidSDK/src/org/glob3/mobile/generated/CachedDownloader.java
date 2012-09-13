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

  public final long requestBuffer(URL url, long priority, IBufferDownloadListener listener, boolean deleteListener)
  {
	_requestsCounter++;
  
	final IByteBuffer cachedBuffer = _cacheStorage.readBuffer(url);
	if (cachedBuffer == null)
	{
	  // cache miss
	  return _downloader.requestBuffer(url, priority, new BufferSaverDownloadListener(this, _cacheStorage, listener, deleteListener), true);
	}
	else
	{
	  // cache hit
	  _cacheHitsCounter++;
  
	  listener.onDownload(url, cachedBuffer);
  
	  if (deleteListener)
	  {
		listener = null;
	  }
  
	  if (cachedBuffer != null)
		  cachedBuffer.dispose();
	  return -1;
	}
  }

  public final long requestImage(URL url, long priority, IImageDownloadListener listener, boolean deleteListener)
  {
	_requestsCounter++;
  
	final IImage cachedImage = _cacheStorage.readImage(url);
	if (cachedImage == null)
	{
	  // cache miss
	  return _downloader.requestImage(url, priority, new ImageSaverDownloadListener(this, _cacheStorage, listener, deleteListener), true);
	}
	else
	{
	  // cache hit
	  _cacheHitsCounter++;
  
	  listener.onDownload(url, cachedImage);
  
	  if (deleteListener)
	  {
		listener = null;
	  }
  
	  if (cachedImage != null)
		  cachedImage.dispose();
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
	isb.add("CachedDownloader(cache hits=");
	isb.add(_cacheHitsCounter);
	isb.add("/");
	isb.add(_requestsCounter);
	isb.add(", saves=");
	isb.add(_savesCounter);
	isb.add(", downloader=");
	isb.add(_downloader.statistics());
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