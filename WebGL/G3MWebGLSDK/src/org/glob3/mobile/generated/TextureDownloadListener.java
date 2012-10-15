package org.glob3.mobile.generated; 
public class TextureDownloadListener implements IImageDownloadListener
{
  private Mark _mark;

  public TextureDownloadListener(Mark mark)
  {
	  _mark = mark;

  }

  public final void onDownload(URL url, IImage image)
  {
	_mark.onTextureDownload(image);
  }

  public final void onError(URL url)
  {
	//    ILogger::instance()->logError("Error trying to download image \"%s\"", url.getPath().c_str());
	_mark.onTextureDownloadError();
  }

  public final void onCancel(URL url)
  {
	//    ILogger::instance()->logError("Download canceled for image \"%s\"", url.getPath().c_str());
	_mark.onTextureDownloadError();
  }

  public final void onCanceledDownload(URL url, IImage image)
  {
	// do nothing
  }
}