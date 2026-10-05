package org.glob3.mobile.generated;
public class StackLayoutImageFactory_ImageListener extends IImageListener
{
  private IImageFactoryListener _listener;
  private boolean _deleteListener;

  private final String _imageName;

  public StackLayoutImageFactory_ImageListener(String imageName, IImageFactoryListener listener, boolean deleteListener)
  {
     _imageName = imageName;
     _listener = listener;
     _deleteListener = deleteListener;
  }

  public final void imageCreated(IImage image)
  {
    _listener.imageCreated(image, _imageName);
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
  }
}