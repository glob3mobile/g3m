package org.glob3.mobile.generated;
//class DTT_TileTextureBuilder;





public class DTT_TileImageListener extends TileImageListener
{
  private DTT_TileTextureBuilder _builder;
  private final Sector _tileSector ;
  private final IImage _backgroundTileImage;
  private final String _backgroundTileImageName;

  private final Vector2S _tileTextureResolution;

  public DTT_TileImageListener(DTT_TileTextureBuilder builder, Tile tile, Vector2S tileTextureResolution, IImage backgroundTileImage, String backgroundTileImageName)
  {
     _builder = builder;
     _tileSector = new Sector(tile._sector);
     _tileTextureResolution = tileTextureResolution;
     _backgroundTileImage = backgroundTileImage;
     _backgroundTileImageName = backgroundTileImageName;
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

  public final void imageCreated(String tileID, IImage image, String imageID, TileImageContribution contribution)
  {
  
    if (!contribution.isFullCoverageAndOpaque())
    {
  
      IStringBuilder auxImageID = IStringBuilder.newStringBuilder();
  
      //ILogger::instance()->logInfo("DTT_TileImageListener received image that does not fit tile. Building new Image....");
  
      ICanvas canvas = IFactory.instance().createCanvas(false);
  
      final int width = _tileTextureResolution._x;
      final int height = _tileTextureResolution._y;
  
      //ILogger::instance()->logInfo("Tile " + _tile->description());
  
      canvas.initialize(width, height);
  
      if (_backgroundTileImage != null)
      {
        auxImageID.addString(_backgroundTileImageName);
        auxImageID.addString("|");
        canvas.drawImage(_backgroundTileImage, 0, 0, width, height);
      }
  
      auxImageID.addString(imageID);
      auxImageID.addString("|");
  
      final float alpha = contribution._alpha;
  
      if (contribution.isFullCoverage())
      {
        auxImageID.addFloat(alpha);
        auxImageID.addString("|");
        canvas.drawImage(image, 0, 0, width, height, alpha);
      }
      else
      {
        final Sector imageSector = contribution.getSector();
  
        final Sector visibleContributionSector = imageSector.intersection(_tileSector);
  
        auxImageID.addString(visibleContributionSector.id());
        auxImageID.addString("|");
  
        final RectangleF srcRect = RectangleF.calculateInnerRectangleFromSector(image.getWidth(), image.getHeight(), imageSector, visibleContributionSector);
  
        final RectangleF destRect = RectangleF.calculateInnerRectangleFromSector(width, height, _tileSector, visibleContributionSector);
  
  
        //We add "destRect->id()" to "auxImageID" for to differentiate cases of same "visibleContributionSector" at different levels of tiles
        auxImageID.addString(destRect.id());
        auxImageID.addString("|");
  
  
        //ILogger::instance()->logInfo("destRect " + destRect->description());
  
  
        canvas.drawImage(image, srcRect._x, srcRect._y, srcRect._width, srcRect._height, destRect._x, destRect._y, destRect._width, destRect._height, alpha);
                          //SRC RECT
                          //DEST RECT
  
        if (destRect != null)
           destRect.dispose();
        if (srcRect != null)
           srcRect.dispose();
      }
  
      canvas.createImage(new CanvasOwnerImageListenerWrapper(canvas, new DTT_NotFullProviderImageListener(_builder, auxImageID.getString()), true), true);
  
      if (auxImageID != null)
         auxImageID.dispose();
      if (image != null)
         image.dispose();
      TileImageContribution.releaseContribution(contribution);
  
    }
    else
    {
      _builder.imageCreated(image, imageID, contribution);
    }
  }

  public final void imageCreationError(String tileID, String error)
  {
    _builder.imageCreationError(error);
  }

  public final void imageCreationCanceled(String tileID)
  {
    _builder.imageCreationCanceled();
  }


}
