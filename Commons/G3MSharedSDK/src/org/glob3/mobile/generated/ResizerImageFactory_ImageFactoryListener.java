package org.glob3.mobile.generated;
public class ResizerImageFactory_ImageFactoryListener implements IImageFactoryListener
{
  private final G3MContext _context;

  private ResizerImageFactory _resizer;

  private IImageFactoryListener _listener;
  private final boolean _deleteListener;


  public ResizerImageFactory_ImageFactoryListener(G3MContext context, ResizerImageFactory resizer, IImageFactoryListener listener, boolean deleteListener)
  {
     _context = context;
     _resizer = resizer;
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
  }

  public final void imageCreated(IImage image, String imageName)
  {
    _resizer.imageCreated(image, imageName, _context, _listener, _deleteListener);
    _listener = null; // 'listener' ownership went to _resizer
  }

  public final void onError(String error)
  {
    _resizer.onError(error, _listener, _deleteListener);
    _listener = null; // 'listener' ownership went to _resizer
  }

}