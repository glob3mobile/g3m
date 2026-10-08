package org.glob3.mobile.generated;
// owns the factory while the image is created: the renderer may go first
public class MarksRenderer_HintListener implements IImageFactoryListener
{
  private MarksRenderer _renderer;
  private IImageFactory _imageFactory;

  public MarksRenderer_HintListener(MarksRenderer renderer, IImageFactory imageFactory)
  {
     _renderer = renderer;
     _imageFactory = imageFactory;
  }

  public void dispose()
  {
    if (_imageFactory != null)
       _imageFactory.dispose();
  }

  public final void forgetRenderer()
  {
    _renderer = null;
  }

  public final void imageCreated(IImage image, String imageName)
  {
    if (_renderer == null)
    {
      if (image != null)
         image.dispose();
    }
    else
    {
      _renderer.onHintImageCreated(image, imageName);
    }
  }

  public final void onError(String error)
  {
    ILogger.instance().logError("Can't create the marks' hint image: \"%s\"", error);
  }
}