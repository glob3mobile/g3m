package org.glob3.mobile.generated; 
public class IconDownloadListener implements IImageDownloadListener
{
  private Mark _mark;
  private final String _label;
  private final boolean _labelBottom;

  public IconDownloadListener(Mark mark, String label, boolean labelBottom)
  {
     _mark = mark;
     _label = label;
     _labelBottom = labelBottom;

  }

  public final void onDownload(URL url, IImage image)
  {
    final boolean hasLabel = (_label.length() != 0);

    if (hasLabel)
    {
      LabelPosition labelPosition = _labelBottom ? LabelPosition.Bottom : LabelPosition.Right;

      ITextUtils.instance().labelImage(image, _label, labelPosition, new MarkLabelImageListener(image, _mark), true);
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