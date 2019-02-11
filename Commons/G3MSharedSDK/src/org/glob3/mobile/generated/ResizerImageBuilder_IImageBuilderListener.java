package org.glob3.mobile.generated;
public class ResizerImageBuilder_IImageBuilderListener implements IImageBuilderListener
{
  private final G3MContext _context;

  private ResizerImageBuilder _builder;

  private IImageBuilderListener _listener;
  private final boolean _deleteListener;


  public ResizerImageBuilder_IImageBuilderListener(G3MContext context, ResizerImageBuilder builder, IImageBuilderListener listener, boolean deleteListener)
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
    _listener = null;
  }

  public final void onError(String error)
  {
    _builder.onError(error, _listener, _deleteListener);
    _listener = null;
  }

}
