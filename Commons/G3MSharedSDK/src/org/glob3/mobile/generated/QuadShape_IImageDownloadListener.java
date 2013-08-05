package org.glob3.mobile.generated; 
public class QuadShape_IImageDownloadListener extends IImageDownloadListener
{
  private QuadShape _quadShape;


  public QuadShape_IImageDownloadListener(QuadShape quadShape)
  {
     _quadShape = quadShape;

  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    _quadShape.imageDownloaded(image);
  }

  public final void onError(URL url)
  {

  }

  public final void onCancel(URL url)
  {

  }

  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {

  }
}