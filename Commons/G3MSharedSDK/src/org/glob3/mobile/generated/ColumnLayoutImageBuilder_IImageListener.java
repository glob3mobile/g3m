package org.glob3.mobile.generated; 
public class ColumnLayoutImageBuilder_IImageListener extends IImageListener
{
  private IImageBuilderListener _listener;
  private boolean _deleteListener;

  private final String _imageName;

  public ColumnLayoutImageBuilder_IImageListener(String imageName, IImageBuilderListener listener, boolean deleteListener)
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