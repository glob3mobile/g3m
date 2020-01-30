package org.glob3.mobile.generated;
public class LabelImageBuilder_ImageListener extends CanvasOwnerImageListener
{
  private IImageBuilderListener _listener;
  private boolean _deleteListener;
  private final String _imageName;

  public LabelImageBuilder_ImageListener(ICanvas canvas, IImageBuilderListener listener, boolean deleteListener, String imageName)
  {
     super(canvas);
     _listener = listener;
     _deleteListener = deleteListener;
     _imageName = imageName;
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

  public void dispose()
  {
    if (_listener != null)
       _listener.dispose();
    super.dispose();
  }
}
