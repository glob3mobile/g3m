package org.glob3.mobile.generated; 
///#include "GPUProgramState.hpp"


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
    {
      IFactory.instance().deleteImage(_iconImage);
      _iconImage = null;
    }

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