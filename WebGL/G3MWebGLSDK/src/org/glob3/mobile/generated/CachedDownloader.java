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




public class CachedDownloader extends IDownloader
{
  private IDownloader _downloader;

  private int _requestsCounter;
  private int _cacheHitsCounter;
  private int _savesCounter;

  private final boolean _saveInBackground;

  public CachedDownloader(IDownloader downloader, boolean saveInBackground)
  {
	  _downloader = downloader;
	  _requestsCounter = 0;
	  _cacheHitsCounter = 0;
	  _savesCounter = 0;
	  _saveInBackground = saveInBackground;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean saveInBackground() const
  public final boolean saveInBackground()
  {
	return _saveInBackground;
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
  
	final IByteBuffer cachedBuffer = IStorage.instance().isAvailable() ? IStorage.instance().readBuffer(url) : null;
	if (cachedBuffer == null)
	{
	  // cache miss
	  return _downloader.requestBuffer(url, priority, new BufferSaverDownloadListener(this, listener, deleteListener), true);
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
  
	final IImage cachedImage = IStorage.instance().isAvailable() ? IStorage.instance().readImage(url) : null;
	if (cachedImage == null)
	{
	  // cache miss
	  return _downloader.requestImage(url, priority, new ImageSaverDownloadListener(this, listener, deleteListener), true);
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
	isb.addString("CachedDownloader(cache hits=");
	isb.addInt(_cacheHitsCounter);
	isb.addString("/");
	isb.addInt(_requestsCounter);
	isb.addString(", saves=");
	isb.addInt(_savesCounter);
	isb.addString(", downloader=");
  //  isb->addString(IDownloader::instance()->statistics());
	  isb.addString(_downloader.statistics());
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

  public final void countSave()
  {
	_savesCounter++;
  }

  public final void onResume(InitializationContext ic)
  {
  //  IDownloader::instance()->onResume(ic);
	  _downloader.onResume(ic);
	  IStorage.instance().onResume(ic);
  }

  public final void onPause(InitializationContext ic)
  {
  //  IDownloader::instance()->onPause(ic);
	  _downloader.onPause(ic);
	IStorage.instance().onPause(ic);
  }

}