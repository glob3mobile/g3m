package org.glob3.mobile.generated; 
///#include <iostream>


public class TokenDownloadListener implements IBufferDownloadListener
{
  private BingLayer _bingLayer;

  public TokenDownloadListener(BingLayer bingLayer)
  {
	  _bingLayer = bingLayer;
  }

  public final void onDownload(URL url, IByteBuffer buffer)
  {
  
  
	//std::string string = buffer->getAsString();
	//std::cout<<string<<"\n";
  }

  public final void onError(URL url)
  {
  }

  public final void onCanceledDownload(URL url, IByteBuffer data)
  {
  }

  public final void onCancel(URL url)
  {
  }

  public void dispose()
  {
  }

}