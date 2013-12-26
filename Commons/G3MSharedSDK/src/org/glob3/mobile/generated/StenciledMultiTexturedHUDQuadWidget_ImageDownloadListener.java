package org.glob3.mobile.generated; 
public class StenciledMultiTexturedHUDQuadWidget_ImageDownloadListener extends IImageDownloadListener
{
  private StenciledMultiTexturedHUDQuadWidget _quadWidget;
  public StenciledMultiTexturedHUDQuadWidget_ImageDownloadListener(StenciledMultiTexturedHUDQuadWidget quadWidget)
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