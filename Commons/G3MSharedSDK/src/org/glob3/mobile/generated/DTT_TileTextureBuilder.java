package org.glob3.mobile.generated;
public class DTT_TileTextureBuilder extends RCObject
{
  private LeveledTexturedMesh _texturedMesh;
  private Tile _tile;
  private final String _tileID;
  private TileImageProvider _tileImageProvider;
  private TexturesHandler _texturesHandler;
  private final Vector2S _tileTextureResolution;
  private final boolean _logTilesPetitions;
  private final long _tileTextureDownloadPriority;
  private boolean _canceled;
  private FrameTasksExecutor _frameTasksExecutor;
  private final IImage _backgroundTileImage;
  private final String _backgroundTileImageName;
  private final boolean _verboseErrors;


  private static TextureIDReference getTopLevelTextureIDForTile(Tile tile)
  {
    LeveledTexturedMesh mesh = (LeveledTexturedMesh) tile.getTexturizedMesh();

    return (mesh == null) ? null : mesh.getTopLevelTextureID();
  }

  private static LeveledTexturedMesh createMesh(Tile tile, Mesh tessellatorMesh, Vector2S tileMeshResolution, TileTessellator tessellator, TexturesHandler texturesHandler, IImage backgroundTileImage, String backgroundTileImageName, boolean ownedTexCoords, boolean transparent, boolean generateMipmap, int wrapS, int wrapT)
  {
    java.util.ArrayList<LazyTextureMapping> mappings = new java.util.ArrayList<LazyTextureMapping>();

    Tile ancestor = tile;
    boolean fallbackSolved = false;
    while (ancestor != null && !fallbackSolved)
    {

      LazyTextureMapping mapping = new LazyTextureMapping(new DTT_LTMInitializer(tileMeshResolution, tile, ancestor, tessellator), ownedTexCoords, transparent);

      if (ancestor != tile)
      {
        final TextureIDReference glTextureID = getTopLevelTextureIDForTile(ancestor);
        if (glTextureID != null)
        {
          TextureIDReference glTextureIDRetainedCopy = glTextureID.createCopy();

          mapping.setGLTextureID(glTextureIDRetainedCopy);
          fallbackSolved = true;
        }
      }

      mappings.add(mapping);

      ancestor = ancestor.getParent();
    }

    if (!fallbackSolved && backgroundTileImage != null)
    {
      LazyTextureMapping mapping = new LazyTextureMapping(new DTT_LTMInitializer(tileMeshResolution, tile, tile, tessellator), true, false);
      final TextureIDReference glTextureID = texturesHandler.getTextureIDReference(backgroundTileImage, GLFormat.rgba(), backgroundTileImageName, generateMipmap, wrapS, wrapT);
      mapping.setGLTextureID(glTextureID); //Mandatory to active mapping

      mappings.add(mapping);

    }

    return new LeveledTexturedMesh(tessellatorMesh, false, mappings);
  }


  public DTT_TileTextureBuilder(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, TileImageProvider tileImageProvider, Tile tile, Mesh tessellatorMesh, TileTessellator tessellator, long tileTextureDownloadPriority, boolean logTilesPetitions, FrameTasksExecutor frameTasksExecutor, IImage backgroundTileImage, String backgroundTileImageName, boolean verboseErrors)
  {
     _tileImageProvider = tileImageProvider;
     _texturesHandler = rc.getTexturesHandler();
     _tileTextureResolution = layerTilesRenderParameters._tileTextureResolution;
     _tile = tile;
     _tileID = tile._id;
     _texturedMesh = null;
     _canceled = false;
     _tileTextureDownloadPriority = tileTextureDownloadPriority;
     _logTilesPetitions = logTilesPetitions;
     _frameTasksExecutor = frameTasksExecutor;
     _backgroundTileImage = backgroundTileImage;
     _backgroundTileImageName = backgroundTileImageName;
     _verboseErrors = verboseErrors;
    _tileImageProvider._retain();

    _texturedMesh = createMesh(tile, tessellatorMesh, layerTilesRenderParameters._tileMeshResolution, tessellator, _texturesHandler, backgroundTileImage, backgroundTileImageName, true, false, true, GLTextureParameterValue.clampToEdge(), GLTextureParameterValue.clampToEdge()); // generateMipmap -  transparent, -  ownedTexCoords,
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
      _tileImageProvider.cancel(_tileID);
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

  public final boolean uploadTexture(IImage image, String imageID)
  {

    final TextureIDReference glTextureID = _texturesHandler.getTextureIDReference(image, GLFormat.rgba(), imageID, true, GLTextureParameterValue.clampToEdge(), GLTextureParameterValue.clampToEdge()); // wrapT -  wrapS -  generateMipmap
    if (glTextureID == null)
    {
      return false;
    }

    if (!_texturedMesh.setGLTextureIDForLevel(0, glTextureID))
    {
      if (glTextureID != null)
         glTextureID.dispose();
    }

    return true;
  }

  public final void imageCreated(IImage image, String imageID, TileImageContribution contribution)
  {
    if (!contribution.isFullCoverageAndOpaque())
    {
      ILogger.instance().logWarning("Contribution isn't full covearge and opaque before to upload texture");
    }

    if (!_canceled && (_tile != null) && (_texturedMesh != null))
    {
      if (uploadTexture(image, imageID))
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
    if (_verboseErrors)
    {
      ILogger.instance().logError("%s", error);
    }
  }

  public final void imageCreationCanceled()
  {
  }
}
