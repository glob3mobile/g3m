package org.glob3.mobile.generated; 
public class HUDQuadWidget_ImageDownloadListener extends IImageDownloadListener
{
  private HUDQuadWidget _quadWidget;

  public HUDQuadWidget_ImageDownloadListener(HUDQuadWidget quadWidget)
  {
     _quadWidget = quadWidget;
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    _quadWidget.onImageDownload(image);
  }

  public final void onError(URL url)
  {
    _quadWidget.onImageDownloadError(url);
  }

  public final void onCancel(URL url)
  {
    // do nothing
  }

  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {
    // do nothing
  }

}