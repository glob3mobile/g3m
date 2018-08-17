package org.glob3.mobile.generated;public class DTT_TileTextureBuilder extends RCObject
{
  private LeveledTexturedMesh _texturedMesh;
  private Tile _tile;
  private final String _tileId;
  private TileImageProvider _tileImageProvider;
  private TexturesHandler _texturesHandler;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Vector2I _tileTextureResolution = new Vector2I();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Vector2I _tileTextureResolution = new internal();
//#endif
  private final boolean _logTilesPetitions;
  private final long _tileDownloadPriority;
  private boolean _canceled;
  private FrameTasksExecutor _frameTasksExecutor;
  private final IImage _backgroundTileImage;
  private final String _backgroundTileImageName;
  private final boolean _ownedTexCoords;
  private final boolean _transparent;
  private final boolean _generateMipmap;


  private static TextureIDReference getTopLevelTextureIdForTile(Tile tile)
  {
	LeveledTexturedMesh mesh = (LeveledTexturedMesh) tile.getTexturizedMesh();

	return (mesh == null) ? null : mesh.getTopLevelTextureId();
  }

  private static LeveledTexturedMesh createMesh(Tile tile, Mesh tessellatorMesh, Vector2S tileMeshResolution, TileTessellator tessellator, TexturesHandler texturesHandler, IImage backgroundTileImage, String backgroundTileImageName, boolean ownedTexCoords, boolean transparent, boolean generateMipmap)
  {
	java.util.ArrayList<LazyTextureMapping> mappings = new java.util.ArrayList<LazyTextureMapping*>();

	Tile ancestor = tile;
	boolean fallbackSolved = false;
	while (ancestor != null && !fallbackSolved)
	{

	  LazyTextureMapping mapping = new LazyTextureMapping(new DTT_LTMInitializer(tileMeshResolution, tile, ancestor, tessellator), ownedTexCoords, transparent);

	  if (ancestor != tile)
	  {
		final TextureIDReference glTextureId = getTopLevelTextureIdForTile(ancestor);
		if (glTextureId != null)
		{
		  TextureIDReference glTextureIdRetainedCopy = glTextureId.createCopy();

		  mapping.setGLTextureId(glTextureIdRetainedCopy);
		  fallbackSolved = true;
		}
	  }

	  mappings.add(mapping);

	  ancestor = ancestor.getParent();
	}

	if (!fallbackSolved && backgroundTileImage != null)
	{
	  LazyTextureMapping mapping = new LazyTextureMapping(new DTT_LTMInitializer(tileMeshResolution, tile, tile, tessellator), true, false);
	  final TextureIDReference glTextureId = texturesHandler.getTextureIDReference(backgroundTileImage, GLFormat.rgba(), backgroundTileImageName, generateMipmap);
	  mapping.setGLTextureId(glTextureId); //Mandatory to active mapping

	  mappings.add(mapping);

	}

	return new LeveledTexturedMesh(tessellatorMesh, false, mappings);
  }


  public DTT_TileTextureBuilder(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, TileImageProvider tileImageProvider, Tile tile, Mesh tessellatorMesh, TileTessellator tessellator, long tileDownloadPriority, boolean logTilesPetitions, FrameTasksExecutor frameTasksExecutor, IImage backgroundTileImage, String backgroundTileImageName)

  {
	  _tileImageProvider = tileImageProvider;
	  _texturesHandler = rc.getTexturesHandler();
	  _tileTextureResolution = new Vector2I(layerTilesRenderParameters._tileTextureResolution);
	  _tile = tile;
	  _tileId = tile._id;
	  _texturedMesh = null;
	  _canceled = false;
	  _tileDownloadPriority = tileDownloadPriority;
	  _logTilesPetitions = logTilesPetitions;
	  _frameTasksExecutor = frameTasksExecutor;
	  _backgroundTileImage = backgroundTileImage;
	  _backgroundTileImageName = backgroundTileImageName;
	  _ownedTexCoords = true;
	  _transparent = false;
	  _generateMipmap = true;
	_tileImageProvider._retain();

	_texturedMesh = createMesh(tile, tessellatorMesh, layerTilesRenderParameters._tileMeshResolution, tessellator, _texturesHandler, backgroundTileImage, backgroundTileImageName, _ownedTexCoords, _transparent, _generateMipmap);
  }

  public final LeveledTexturedMesh getTexturedMesh()
  {
	return _texturedMesh;
  }

  public final void start()
  {
	if (!_canceled)
	{
	  final TileImageContribution contribution = _tileImageProvider.contribution(_tile);
	  if (contribution == null)
	  {
		if (_tile != null)
		{
		  imageCreated(_backgroundTileImage.shallowCopy(), _backgroundTileImageName, TileImageContribution.fullCoverageOpaque());
		  //_tile->setTextureSolved(true);
		}
	  }
	  else
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _tileImageProvider->create(_tile, contribution, _tileTextureResolution, _tileDownloadPriority, _logTilesPetitions, new DTT_TileImageListener(this, _tile, _tileTextureResolution, _backgroundTileImage, _backgroundTileImageName), true, _frameTasksExecutor);
		_tileImageProvider.create(_tile, contribution, new Vector2I(_tileTextureResolution), _tileDownloadPriority, _logTilesPetitions, new DTT_TileImageListener(this, _tile, _tileTextureResolution, _backgroundTileImage, _backgroundTileImageName), true, _frameTasksExecutor);
	  }
	}
  }

  public final void cancel(boolean cleanTile)
  {
	_texturedMesh = null;
	if (cleanTile)
	{
	  _tile = null;
	}
	if (!_canceled)
	{
	  _canceled = true;
	  _tileImageProvider.cancel(_tileId);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isCanceled() const
  public final boolean isCanceled()
  {
	return _canceled;
  }

  public void dispose()
  {
	_tileImageProvider._release();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final boolean uploadTexture(IImage image, String imageId)
  {

	final TextureIDReference glTextureId = _texturesHandler.getTextureIDReference(image, GLFormat.rgba(), imageId, _generateMipmap);
	if (glTextureId == null)
	{
	  return false;
	}

	if (!_texturedMesh.setGLTextureIdForLevel(0, glTextureId))
	{
	  if (glTextureId != null)
		  glTextureId.dispose();
	}

	return true;
  }

  public final void imageCreated(IImage image, String imageId, TileImageContribution contribution)
  {
	if (!contribution.isFullCoverageAndOpaque())
	{
	  ILogger.instance().logWarning("Contribution isn't full covearge and opaque before to upload texture");
	}

	if (!_canceled && (_tile != null) && (_texturedMesh != null))
	{
	  if (uploadTexture(image, imageId))
	  {
		_tile.setTextureSolved(true);
	  }
	}

	if (image != null)
		image.dispose();
	TileImageContribution.releaseContribution(contribution);
  }

  public final void imageCreationError(String error)
  {
	// TODO: #warning propagate the error to the texturizer and change the render state if is necessary
	ILogger.instance().logError("%s", error.c_str());
  }

  public final void imageCreationCanceled()
  {
  }
}
