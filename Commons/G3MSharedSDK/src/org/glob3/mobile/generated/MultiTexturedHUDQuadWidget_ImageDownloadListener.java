package org.glob3.mobile.generated; 
public class MultiTexturedHUDQuadWidget_ImageDownloadListener extends IImageDownloadListener
{
  private MultiTexturedHUDQuadWidget _quadWidget;

  public MultiTexturedHUDQuadWidget_ImageDownloadListener(MultiTexturedHUDQuadWidget quadWidget)
  {
     _quadWidget = quadWidget;
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    _quadWidget.onImageDownload(image, url);
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