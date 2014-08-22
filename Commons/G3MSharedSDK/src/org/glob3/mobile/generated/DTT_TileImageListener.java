package org.glob3.mobile.generated; 
//class DTT_TileTextureBuilder;





public class DTT_TileImageListener extends TileImageListener
{
  private DTT_TileTextureBuilder _builder;
  private final Tile _tile;
  private final IImage _backGroundTileImage;
  private final String _backGroundTileImageName;

  private final Vector2I _tileTextureResolution;

  private RectangleF getInnerRectangle(int wholeSectorWidth, int wholeSectorHeight, Sector wholeSector, Sector innerSector)
  {
    if (wholeSector.isNan() || innerSector.isNan() || wholeSector.isEquals(innerSector))
    {
      return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
    }
  
    final double widthFactor = innerSector._deltaLongitude.div(wholeSector._deltaLongitude);
    final double heightFactor = innerSector._deltaLatitude.div(wholeSector._deltaLatitude);
  
    final Vector2D lowerUV = wholeSector.getUVCoordinates(innerSector.getNW());
  
    return new RectangleF((float)(lowerUV._x * wholeSectorWidth), (float)(lowerUV._y * wholeSectorHeight), (float)(widthFactor * wholeSectorWidth), (float)(heightFactor * wholeSectorHeight));
  }

  public DTT_TileImageListener(DTT_TileTextureBuilder builder, Tile tile, Vector2I tileTextureResolution, IImage backGroundTileImage, String backGroundTileImageName)
  {
     _builder = builder;
     _tile = tile;
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning JM at WORK: CREAR CANVAS SOURCE RECTANGLE Y DEST RECTANGLE.EL LISTENER DEL CANVAS TIENE QUE HACER UN RETAIN Y UN RELEASE DEL BUILDER.METER DEFAULTIMAGEPROVIDER (CLASE QUE GENERA UNA IMAGEN).QUE RENDER STATE DEVUELVA BUSY HASTA QUE NO TENGA LA IMAGEN DEFAULT, PARA QUE PUEDA ARRANCAR EL GLOBO.
  
  
  
  
   if (!contribution.isFullCoverageAndOpaque())
   {
  
        String auxImageId = "";
  
        // retain the singleResult->_contribution as the _listener take full ownership of the contribution
        TileImageContribution.retainContribution(contribution);
  
        ILogger.instance().logInfo("DTT_TileImageListener received image that does not fit tile. Building new Image....");
  
        ICanvas canvas = IFactory.instance().createCanvas();
  
        final int _width = _tileTextureResolution._x;
  
        final int _height = _tileTextureResolution._y;
  
        final Sector tileSector = _tile._sector;
  
        ILogger.instance().logInfo("Tile " + _tile.description());
  
        canvas.initialize(_width, _height);
  
        if (_backGroundTileImage != null)
        {
          canvas.drawImage(_backGroundTileImage, 0, 0);
          auxImageId += _backGroundTileImageName + "|";
        }
  
        auxImageId += imageId + "|";
  
        final float alpha = contribution._alpha;
        final Sector imageSector = contribution.getSector();
  
        final Sector visibleContributionSector = imageSector.intersection(tileSector);
  
        auxImageId += visibleContributionSector.description()+ "|";
  
  
        final RectangleF srcRect = getInnerRectangle(_width, _height, imageSector, visibleContributionSector);
  
        final RectangleF destRect = getInnerRectangle(_width, _height, tileSector, imageSector);
  
        //We add "destRect->description()" to "auxImageId" for to differentiate cases of same "visibleContributionSector" at different levels of tiles
        auxImageId += destRect.description()+ "|";
  
  
        ILogger.instance().logInfo("destRect " + destRect.description());
  
  
        canvas.drawImage(image, srcRect._x, srcRect._y, srcRect._width, srcRect._height, destRect._x, destRect._y, destRect._width, destRect._height, alpha);
                          //SRC RECT
                          //DEST RECT
  
  
  
  
        if (destRect != null)
           destRect.dispose();
        if (srcRect != null)
           srcRect.dispose();
  
        canvas.createImage(new DTT_NotFullProviderImageListener(_builder, auxImageId, contribution), true);
  
        if (canvas != null)
           canvas.dispose();
  
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