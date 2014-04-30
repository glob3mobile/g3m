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
  private final long _tileDownloadPriority;
  private boolean _canceled;
  private FrameTasksExecutor _frameTasksExecutor;


  private static TextureIDReference getTopLevelTextureIdForTile(Tile tile)
  {
    LeveledTexturedMesh mesh = (LeveledTexturedMesh) tile.getTexturizedMesh();

    return (mesh == null) ? null : mesh.getTopLevelTextureId();
  }

  private static LeveledTexturedMesh createMesh(Tile tile, Mesh tessellatorMesh, Vector2I tileMeshResolution, TileTessellator tessellator)
  {
    java.util.ArrayList<LazyTextureMapping> mappings = new java.util.ArrayList<LazyTextureMapping>();

    Tile ancestor = tile;
    boolean fallbackSolved = false;
    while (ancestor != null && !fallbackSolved)
    {
      final boolean ownedTexCoords = true;
      final boolean transparent = false;
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

    return new LeveledTexturedMesh(tessellatorMesh, false, mappings);
  }


  public DTT_TileTextureBuilder(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, TileImageProvider tileImageProvider, Tile tile, Mesh tessellatorMesh, TileTessellator tessellator, long tileDownloadPriority, boolean logTilesPetitions, FrameTasksExecutor frameTasksExecutor)
  {
     _tileImageProvider = tileImageProvider;
     _texturesHandler = rc.getTexturesHandler();
     _tileTextureResolution = layerTilesRenderParameters._tileTextureResolution;
     _tile = tile;
     _tileId = tile._id;
     _texturedMesh = null;
     _canceled = false;
     _tileDownloadPriority = tileDownloadPriority;
     _logTilesPetitions = logTilesPetitions;
     _frameTasksExecutor = frameTasksExecutor;
    _tileImageProvider._retain();

    _texturedMesh = createMesh(tile, tessellatorMesh, layerTilesRenderParameters._tileMeshResolution, tessellator);
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
          _tile.setTextureSolved(true);
        }
      }
      else
      {
        _tileImageProvider.create(_tile, contribution, _tileTextureResolution, _tileDownloadPriority, _logTilesPetitions, new DTT_TileImageListener(this), true, _frameTasksExecutor);
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
    final boolean generateMipmap = true;

    final TextureIDReference glTextureId = _texturesHandler.getTextureIDReference(image, GLFormat.rgba(), imageId, generateMipmap);
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
    if (!_canceled && (_tile != null) && (_texturedMesh != null))
    {
      if (uploadTexture(image, imageId))
      {
        _tile.setTextureSolved(true);
      }
    }

    if (image != null)
       image.dispose();
    TileImageContribution.deleteContribution(contribution);
  }

  public final void imageCreationError(String error)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work
    ILogger.instance().logError("%s", error);
  }

  public final void imageCreationCanceled()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work
  }

}