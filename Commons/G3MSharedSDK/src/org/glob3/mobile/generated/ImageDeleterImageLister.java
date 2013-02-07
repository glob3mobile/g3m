package org.glob3.mobile.generated; 
public class ImageDeleterImageLister extends IImageListener
{
  private IImage _imageToDelete;
  private IImageListener _listener;
  private final boolean _autodelete;

  public ImageDeleterImageLister(IImage imageToDelete, IImageListener listener, boolean autodelete)
  {
     _imageToDelete = imageToDelete;
     _listener = listener;
     _autodelete = autodelete;

  }

  public final void imageCreated(IImage image)
  {
    if (_imageToDelete != null)
    {
      IFactory.instance().deleteImage(_imageToDelete);
      _imageToDelete = null;
    }

    if (_listener != null)
    {
      _listener.imageCreated(image);
      if (_autodelete)
      {
        if (_listener != null)
           _listener.dispose();
        _listener = null;
      }
    }
  }
}