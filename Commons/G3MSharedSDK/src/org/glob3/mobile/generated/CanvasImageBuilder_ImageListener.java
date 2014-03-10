package org.glob3.mobile.generated; 
public class CanvasImageBuilder_ImageListener extends IImageListener
{
  private final String _imageName;
  private IImageBuilderListener _listener;
  private final boolean _deleteListener;


  public CanvasImageBuilder_ImageListener(String imageName, IImageBuilderListener listener, boolean deleteListener)
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