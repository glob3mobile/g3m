package org.glob3.mobile.generated;
public class CanvasImageFactory_ImageListener extends IImageListener
{
  private final String _imageName;
  private IImageFactoryListener _listener;
  private final boolean _deleteListener;


  public CanvasImageFactory_ImageListener(String imageName, IImageFactoryListener listener, boolean deleteListener)
  {
     _imageName = imageName;
     _listener = listener;
     _deleteListener = deleteListener;
  }

  public void dispose()
  {
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    super.dispose();
  }

  public final void imageCreated(IImage image)
  {
    _listener.imageCreated(image, _imageName);

    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    _listener = null;
  }
}