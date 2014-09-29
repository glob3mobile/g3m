package org.glob3.mobile.generated; 
public class DTT_NotFullProviderImageListener extends IImageListener
{
  private DTT_TileTextureBuilder _builder;
  private final String _imageId;

  public DTT_NotFullProviderImageListener(DTT_TileTextureBuilder builder, String imageId)
  {
     _builder = builder;
     _imageId = imageId;
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
    _builder.imageCreated(image, _imageId, TileImageContribution.fullCoverageOpaque());
  }
}