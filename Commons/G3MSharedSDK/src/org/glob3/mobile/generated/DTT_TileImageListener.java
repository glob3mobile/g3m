package org.glob3.mobile.generated;//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class DTT_TileTextureBuilder;





public class DTT_TileImageListener extends TileImageListener
{
  private DTT_TileTextureBuilder _builder;
  private final Sector _tileSector = new Sector();
  private final IImage _backgroundTileImage;
  private final String _backgroundTileImageName;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Vector2I _tileTextureResolution = new Vector2I();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Vector2I _tileTextureResolution = new internal();
//#endif

  public DTT_TileImageListener(DTT_TileTextureBuilder builder, Tile tile, Vector2I tileTextureResolution, IImage backgroundTileImage, String backgroundTileImageName)
  {
	  _builder = builder;
	  _tileSector = new Sector(tile._sector);
	  _tileTextureResolution = new Vector2I(tileTextureResolution);
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final void imageCreated(String tileId, IImage image, String imageId, TileImageContribution contribution)
  {
  
	if (!contribution.isFullCoverageAndOpaque())
	{
  
	  IStringBuilder auxImageId = IStringBuilder.newStringBuilder();
  
	  //ILogger::instance()->logInfo("DTT_TileImageListener received image that does not fit tile. Building new Image....");
  
	  ICanvas canvas = IFactory.instance().createCanvas(false);
  
	  final int width = _tileTextureResolution._x;
	  final int height = _tileTextureResolution._y;
  
	  //ILogger::instance()->logInfo("Tile " + _tile->description());
  
	  canvas.initialize(width, height);
  
	  if (_backgroundTileImage != null)
	  {
		auxImageId.addString(_backgroundTileImageName);
		auxImageId.addString("|");
		canvas.drawImage(_backgroundTileImage, 0, 0, width, height);
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
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Sector visibleContributionSector = imageSector->intersection(_tileSector);
		final Sector visibleContributionSector = imageSector.intersection(new Sector(_tileSector));
  
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
