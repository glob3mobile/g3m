package org.glob3.mobile.generated; 
public class DTT_TileTextureBuilder extends RCObject
{
  private LeveledTexturedMesh _texturedMesh;
  private Tile _tile;
  private final String _tileId;
  private TileImageProvider _tileImageProvider;
  private TexturesHandler _texturesHandler;
  private final Vector2I _tileTextureResolution;
  private final boolean _logTilesPetitions;
  private final long _tileTextureDownloadPriority;
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
    java.util.ArrayList<LazyTextureMapping> mappings = new java.util.ArrayList<LazyTextureMapping>();

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


  public DTT_TileTextureBuilder(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, TileImageProvider tileImageProvider, Tile tile, Mesh tessellatorMesh, TileTessellator tessellator, long tileTextureDownloadPriority, boolean logTilesPetitions, FrameTasksExecutor frameTasksExecutor, IImage backgroundTileImage, String backgroundTileImageName)

  {
     _tileImageProvider = tileImageProvider;
     _texturesHandler = rc.getTexturesHandler();
     _tileTextureResolution = layerTilesRenderParameters._tileTextureResolution;
     _tile = tile;
     _tileId = tile._id;
     _texturedMesh = null;
     _canceled = false;
     _tileTextureDownloadPriority = tileTextureDownloadPriority;
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
        _tileImageProvider.create(_tile, contribution, _tileTextureResolution, _tileTextureDownloadPriority, _logTilesPetitions, new DTT_TileImageListener(this, _tile, _tileTextureResolution, _backgroundTileImage, _backgroundTileImageName), true, _frameTasksExecutor);
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

  public final boolean isCanceled()
  {
    return _canceled;
  }

  public void dispose()
  {
    _tileImageProvider._release();
    super.dispose();
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
    ILogger.instance().logError("%s", error);
  }

  public final void imageCreationCanceled()
  {
  }
}