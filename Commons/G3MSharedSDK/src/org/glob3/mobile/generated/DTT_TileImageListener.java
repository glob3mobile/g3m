package org.glob3.mobile.generated; 
//class DTT_TileTextureBuilder;





public class DTT_TileImageListener extends TileImageListener
{
  private DTT_TileTextureBuilder _builder;
  private final Sector _tileSector ;
  private final IImage _backGroundTileImage;
  private final String _backGroundTileImageName;

  private final Vector2I  _tileTextureResolution;

  public DTT_TileImageListener(DTT_TileTextureBuilder builder, Tile tile, Vector2I tileTextureResolution, IImage backGroundTileImage, String backGroundTileImageName)
  {
     _builder = builder;
     _tileSector = new Sector(tile._sector);
     _tileTextureResolution = tileTextureResolution;
     _backGroundTileImage = backGroundTileImage;
     _backGroundTileImageName = backGroundTileImageName;
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

  public final void imageCreated(String tileId, IImage image, String imageId, TileImageContribution contribution)
  {
  
    if (!contribution.isFullCoverageAndOpaque())
    {
  
      IStringBuilder auxImageId = IStringBuilder.newStringBuilder();
  
      //ILogger::instance()->logInfo("DTT_TileImageListener received image that does not fit tile. Building new Image....");
  
      ICanvas canvas = IFactory.instance().createCanvas();
  
      final int width = _tileTextureResolution._x;
      final int height = _tileTextureResolution._y;
  
      //ILogger::instance()->logInfo("Tile " + _tile->description());
  
      canvas.initialize(width, height);
  
      if (_backGroundTileImage != null)
      {
        auxImageId.addString(_backGroundTileImageName);
        auxImageId.addString("|");
        canvas.drawImage(_backGroundTileImage, 0, 0, width, height);
      }
  
      auxImageId.addString(imageId);
      auxImageId.addString("|");
  
      final float alpha = contribution._alpha;
  
      if (contribution.isFullCoverage())
      {
        auxImageId.addFloat(alpha);
        auxImageId.addString("|");
        canvas.drawImage(image, 0, 0, width, height, alpha);
      }
      else
      {
        final Sector imageSector = contribution.getSector();
  
        final Sector visibleContributionSector = imageSector.intersection(_tileSector);
  
        auxImageId.addString(visibleContributionSector.id());
        auxImageId.addString("|");
  
        final RectangleF srcRect = RectangleF.calculateInnerRectangleFromSector(image.getWidth(), image.getHeight(), imageSector, visibleContributionSector);
  
        final RectangleF destRect = RectangleF.calculateInnerRectangleFromSector(width, height, _tileSector, visibleContributionSector);
  
  
        //We add "destRect->id()" to "auxImageId" for to differentiate cases of same "visibleContributionSector" at different levels of tiles
        auxImageId.addString(destRect.id());
        auxImageId.addString("|");
  
  
        //ILogger::instance()->logInfo("destRect " + destRect->description());
  
  
        canvas.drawImage(image, srcRect._x, srcRect._y, srcRect._width, srcRect._height, destRect._x, destRect._y, destRect._width, destRect._height, alpha);
                          //SRC RECT
                          //DEST RECT
  
        if (destRect != null)
           destRect.dispose();
        if (srcRect != null)
           srcRect.dispose();
      }
  
      canvas.createImage(new DTT_NotFullProviderImageListener(_builder, auxImageId.getString()), true);
  
      if (auxImageId != null)
         auxImageId.dispose();
      if (canvas != null)
         canvas.dispose();
      if (image != null)
         image.dispose();
      TileImageContribution.releaseContribution(contribution);
  
    }
    else
    {
      _builder.imageCreated(image, imageId, contribution);
    }
  }

  public final void imageCreationError(String tileId, String error)
  {
    _builder.imageCreationError(error);
  }

  public final void imageCreationCanceled(String tileId)
  {
    _builder.imageCreationCanceled();
  }


}