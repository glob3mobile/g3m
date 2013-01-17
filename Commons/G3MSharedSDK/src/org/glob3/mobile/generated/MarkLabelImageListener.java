package org.glob3.mobile.generated; 
public class MarkLabelImageListener implements IImageListener
{
  private Mark _mark;

  public MarkLabelImageListener(Mark mark)
  {
	  _mark = mark;

  }

  public final void imageCreated(IImage image)
  {
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