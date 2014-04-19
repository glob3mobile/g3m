package org.glob3.mobile.generated; 
//class DTT_TileTextureBuilder;

public class DTT_TileImageListener extends TileImageListener
{
  private DTT_TileTextureBuilder _builder;

  public DTT_TileImageListener(DTT_TileTextureBuilder builder)
  {
     _builder = builder;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final void imageCreated(Tile tile, IImage image, Sector imageSector, RectangleF imageRectangle, float alpha)
  {
    _builder.imageCreated(image, imageSector, imageRectangle, alpha);
  }

  public final void imageCreationError(Tile tile, String error)
  {
    _builder.imageCreationError(error);
  }

  public final void imageCreationCanceled(Tile tile)
  {
    _builder.imageCreationCanceled();
  }
}