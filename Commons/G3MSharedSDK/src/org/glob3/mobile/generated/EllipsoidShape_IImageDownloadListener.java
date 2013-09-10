package org.glob3.mobile.generated; 
public class EllipsoidShape_IImageDownloadListener extends IImageDownloadListener
{
  private EllipsoidShape _ellipsoidShape;


  public EllipsoidShape_IImageDownloadListener(EllipsoidShape ellipsoidShape)
  {
     _ellipsoidShape = ellipsoidShape;

  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    _ellipsoidShape.imageDownloaded(image);
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