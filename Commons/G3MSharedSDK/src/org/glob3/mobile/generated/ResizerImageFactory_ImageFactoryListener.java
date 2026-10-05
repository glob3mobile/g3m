package org.glob3.mobile.generated;
public class ResizerImageFactory_ImageFactoryListener implements IImageFactoryListener
{
  private final G3MContext _context;

  private ResizerImageFactory _builder;

  private IImageFactoryListener _listener;
  private final boolean _deleteListener;


  public ResizerImageFactory_ImageFactoryListener(G3MContext context, ResizerImageFactory builder, IImageFactoryListener listener, boolean deleteListener)
  {
     _context = context;
     _builder = builder;
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
    _builder.imageCreated(image, imageName, _context, _listener, _deleteListener);
    _listener = null; // 'listener' ownership went to _builder
  }

  public final void onError(String error)
  {
    _builder.onError(error, _listener, _deleteListener);
    _listener = null; // 'listener' ownership went to _builder
  }

}