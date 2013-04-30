package org.glob3.mobile.generated; 
public class BingMapsLayer_MetadataBufferDownloadListener extends IBufferDownloadListener
{
  private BingMapsLayer _bingMapsLayer;

  public BingMapsLayer_MetadataBufferDownloadListener(BingMapsLayer bingMapsLayer)
  {
     _bingMapsLayer = bingMapsLayer;

  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    _bingMapsLayer.onDowloadMetadata(buffer);
  }

  public final void onError(URL url)
  {
    _bingMapsLayer.onDownloadErrorMetadata();
  }

  public final void onCancel(URL url)
  {
    // do nothing, the request won't be cancelled
  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {
    // do nothing, the request won't be cancelled
  }
}