package org.glob3.mobile.generated;import java.util.*;

public class MultiTexturedHUDQuadWidget_ImageDownloadListener implements IImageDownloadListener
{
  private MultiTexturedHUDQuadWidget _quadWidget;

  public MultiTexturedHUDQuadWidget_ImageDownloadListener(MultiTexturedHUDQuadWidget quadWidget)
  {
	  _quadWidget = quadWidget;
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _quadWidget->onImageDownload(image, url);
	_quadWidget.onImageDownload(image, new URL(url));
  }

  public final void onError(URL url)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _quadWidget->onImageDownloadError(url);
	_quadWidget.onImageDownloadError(new URL(url));
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
