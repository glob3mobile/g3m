package org.glob3.mobile.generated; 
public class CPUTextureBuilderSubImageImageLister implements IImageListener
{
  private final int _width;
  private final int _height;

  private IImageListener _listener;
  private final boolean _autodelete;

  public CPUTextureBuilderSubImageImageLister(int width, int height, IImageListener listener, boolean autodelete)
  {
	  _width = width;
	  _height = height;
	  _listener = listener;
	  _autodelete = autodelete;

  }

  public final void imageCreated(IImage image)
  {
//    image->scale(_width, _height,
//                 _listener, _autodelete);
	image.scale(_width, _height, new ImageDeleterImageLister(image, _listener, _autodelete), true);

//    IFactory::instance()->deleteImage(image);
  }
}