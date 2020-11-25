package org.glob3.mobile.generated;
public class ColumnLayoutImageBuilder_ImageListener extends IImageListener
{
  private IImageBuilderListener _listener;
  private boolean _deleteListener;

  private final String _imageName;

  public ColumnLayoutImageBuilder_ImageListener(String imageName, IImageBuilderListener listener, boolean deleteListener)
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