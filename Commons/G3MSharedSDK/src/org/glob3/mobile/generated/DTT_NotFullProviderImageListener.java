package org.glob3.mobile.generated;
public class DTT_NotFullProviderImageListener extends CanvasOwnerImageListener
{
  private DTT_TileTextureBuilder _builder;
  private final String _imageID;

  public DTT_NotFullProviderImageListener(ICanvas canvas, DTT_TileTextureBuilder builder, String imageID)
  {
     super(canvas);
     _builder = builder;
     _imageID = imageID;
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
    _builder.imageCreated(image, _imageID, TileImageContribution.fullCoverageOpaque());
  }
}
