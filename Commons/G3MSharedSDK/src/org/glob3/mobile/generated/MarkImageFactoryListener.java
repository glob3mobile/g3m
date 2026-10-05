package org.glob3.mobile.generated;
public class MarkImageFactoryListener implements IImageFactoryListener
{
  private IImageFactory _imageFactory;
  private Mark _mark;

  public MarkImageFactoryListener(IImageFactory imageFactory, Mark mark)
  {
     _imageFactory = imageFactory;
     _mark = mark;

  }

  public void dispose()
  {
    if (_imageFactory != null)
       _imageFactory.dispose();
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