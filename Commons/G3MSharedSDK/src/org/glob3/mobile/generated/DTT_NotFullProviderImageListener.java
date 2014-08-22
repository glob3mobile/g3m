package org.glob3.mobile.generated; 
public class DTT_NotFullProviderImageListener extends IImageListener
{
  private DTT_TileTextureBuilder _builder;
  private final IImage _image;
  private final String _imageId;
  private final TileImageContribution _contribution;

  public DTT_NotFullProviderImageListener(DTT_TileTextureBuilder builder, String imageId, TileImageContribution contribution)
  {
     _builder = builder;
     _imageId = imageId;
     _contribution = contribution;
    _builder._retain();
  }

  public void dispose()
  {
    if (_builder != null)
    {
      _builder._release();
    }
    super.dispose();
  }

  public final void imageCreated(IImage image)
  {
    ILogger.instance().logInfo("Image %s", image.description());
    _builder.imageCreated(image, _imageId, _contribution);
  }
}