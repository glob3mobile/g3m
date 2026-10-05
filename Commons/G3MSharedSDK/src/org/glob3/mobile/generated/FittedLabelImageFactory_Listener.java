package org.glob3.mobile.generated;
public class FittedLabelImageFactory_Listener implements IImageFactoryListener
{
  private IImageFactory _fitted;
  private IImageFactoryListener _listener;
  private final boolean _deleteListener;

  public FittedLabelImageFactory_Listener(IImageFactory fitted, IImageFactoryListener listener, boolean deleteListener)
  {
     _fitted = fitted;
     _listener = listener;
     _deleteListener = deleteListener;
  }

  public final void imageCreated(IImage image, String imageName)
  {
    _listener.imageCreated(image, imageName);
  }

  public final void onError(String error)
  {
    _listener.onError(error);
  }

  public void dispose()
  {
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    if (_fitted != null)
       _fitted.dispose();
  }

}