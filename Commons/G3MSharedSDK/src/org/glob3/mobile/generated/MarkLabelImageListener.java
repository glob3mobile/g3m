package org.glob3.mobile.generated; 
public class MarkLabelImageListener extends IImageListener
{
  private IImage _iconImage;
  private Mark _mark;

  public MarkLabelImageListener(IImage iconImage, Mark mark)
  {
     _iconImage = iconImage;
     _mark = mark;

  }

  public final void imageCreated(IImage image)
  {
    if (_iconImage != null)
       _iconImage.dispose();
    _iconImage = null;

    if (image == null)
    {
      _mark.onTextureDownloadError();
    }
    else
    {
      _mark.onTextureDownload(image);
    }
  }
}