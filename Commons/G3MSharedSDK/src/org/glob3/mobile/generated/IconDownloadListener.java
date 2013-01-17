package org.glob3.mobile.generated; 
public class IconDownloadListener implements IImageDownloadListener
{
  private Mark _mark;
  private final String _label;

  public IconDownloadListener(Mark mark, String label)
  {
	  _mark = mark;
	  _label = label;

  }

  public final void onDownload(URL url, IImage image)
  {
	final boolean hasLabel = (_label.length() != 0);

	if (hasLabel)
	{
	  ITextUtils.instance().labelImage(image, _label, LabelPosition.Bottom, new MarkLabelImageListener(_mark), true);
										 // Right,
	}
	else
	{
	  _mark.onTextureDownload(image);
	}
  }

  public final void onError(URL url)
  {
	ILogger.instance().logError("Error trying to download image \"%s\"", url.getPath());
	_mark.onTextureDownloadError();
  }

  public final void onCancel(URL url)
  {
	// ILogger::instance()->logError("Download canceled for image \"%s\"", url.getPath().c_str());
	_mark.onTextureDownloadError();
  }

  public final void onCanceledDownload(URL url, IImage image)
  {
	// do nothing
  }
}