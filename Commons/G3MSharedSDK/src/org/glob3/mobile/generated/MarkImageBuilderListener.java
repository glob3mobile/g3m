package org.glob3.mobile.generated;
public class MarkImageBuilderListener implements IImageBuilderListener
{
  private IImageBuilder _imageBuilder;
  private Mark _mark;

  public MarkImageBuilderListener(IImageBuilder imageBuilder, Mark mark)
  {
     _imageBuilder = imageBuilder;
     _mark = mark;

  }

  public void dispose()
  {
    if (_imageBuilder != null)
       _imageBuilder.dispose();
  }

  public final void forgetMark()
  {
    _mark = null;
  }

  public final void imageCreated(IImage image, String imageName)
  {
    if (_mark != null)
    {
      _mark.onImageCreated(image, imageName);
    }
  }

  public final void onError(String error)
  {
    if (_mark != null)
    {
      _mark.onImageCreationError(error);
    }
  }

}
