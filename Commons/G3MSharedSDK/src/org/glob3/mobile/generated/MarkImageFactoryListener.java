package org.glob3.mobile.generated;
public class MarkImageFactoryListener implements IImageFactoryListener
{
  private IImageFactory _imageFactory;
  private Mark _mark;
  private final int _outfitIndex;

  public MarkImageFactoryListener(IImageFactory imageFactory, Mark mark, int outfitIndex)
  {
     _imageFactory = imageFactory;
     _mark = mark;
     _outfitIndex = outfitIndex;

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
      _mark.onImageCreated(_outfitIndex, image, imageName);
    }
  }

  public final void onError(String error)
  {
    if (_mark != null)
    {
      _mark.onImageCreationError(_outfitIndex, error);
    }
  }

}