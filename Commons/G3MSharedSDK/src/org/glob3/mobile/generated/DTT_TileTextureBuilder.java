package org.glob3.mobile.generated; 
public class DTT_TileTextureBuilder extends RCObject
{
  private LeveledTexturedMesh _texturedMesh;

//  DefaultTileTexturizer* _texturizer;
//  TileRasterizer*        _tileRasterizer;
  private Tile _tile;

//  std::vector<Petition*> _petitions;
//  int                    _petitionsCount;
//  int                    _stepsDone;

  private TileImageProvider _tileImageProvider;

  private TexturesHandler _texturesHandler;

  private final Vector2I _tileTextureResolution;
//  private final Vector2I _tileMeshResolution;

//  IDownloader*     _downloader;

//  const Mesh* _tessellatorMesh;

//  const TileTessellator* _tessellator;

  private final boolean _logTilesPetitions;

  //  std::vector<TileTextureBuilder_PetitionStatus> _status;
  //  std::vector<long long>                         _requestsIds;


//  bool _finalized;
  private boolean _canceled;
//  bool _alreadyStarted;

  private long _tileDownloadPriority;



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


  public DTT_TileTextureBuilder(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, TileImageProvider tileImageProvider, Tile tile, Mesh tessellatorMesh, TileTessellator tessellator, long tileDownloadPriority, boolean logTilesPetitions) //DefaultTileTexturizer* texturizer,
//                         TileRasterizer*                   tileRasterizer,
//                         const std::vector<Petition*>&     petitions,
//                         IDownloader*                      downloader,
//  _texturizer(texturizer),
//  _tileRasterizer(tileRasterizer),
//  _tileMeshResolution( layerTilesRenderParameters->_tileMeshResolution ),
//  _downloader(downloader),
//  _tessellatorMesh(tessellatorMesh),
//  _stepsDone(0),
//  _tessellator(tessellator),
//  _finalized(false),
//  _alreadyStarted(false),
  {
     _tileImageProvider = tileImageProvider;
     _texturesHandler = rc.getTexturesHandler();
     _tileTextureResolution = layerTilesRenderParameters._tileTextureResolution;
     _tile = tile;
     _texturedMesh = null;
     _canceled = false;
     _tileDownloadPriority = tileDownloadPriority;
     _logTilesPetitions = logTilesPetitions;
    _texturedMesh = createMesh(tile, tessellatorMesh, layerTilesRenderParameters._tileMeshResolution, tessellator);
  }

  public final LeveledTexturedMesh getTexturedMesh()
  {
    return _texturedMesh;
  }

//  void cleanTexturedMesh() {
//    _texturedMesh = NULL;
//  }

//  void cleanTile() {
//    _tile = NULL;
//  }

  public final void start()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
    if (!_canceled)
    {
      final TileImageContribution contribution = _tileImageProvider.contribution(_tile);
      //if (contribution == NONE) {
      if (contribution.isNone())
      {
        if (_tile != null)
        {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning remove this!
          _tile.setTextureSolved(true);
        }
      }
      else
      {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO
        _tileImageProvider.create(_tile, contribution, _tileTextureResolution, _tileDownloadPriority, new DTT_TileImageListener(this), true);
      }
    }
  }

  public final void cancel(boolean cleanTile)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
    _texturedMesh = null;
    if (cleanTile)
    {
      _tile = null;
    }
    if (!_canceled)
    {
      _canceled = true;
      _tileImageProvider.cancel(_tile);
    }
//    if (!_canceled) {
//      _canceled = true;
//
//      if (!_finalized) {
//        for (int i = 0; i < _requestsIds.size(); i++) {
//          const long long requestId = _requestsIds[i];
//          _downloader->cancelRequest(requestId);
//        }
//      }
//      _requestsIds.clear();
//    }
  }

  public final boolean isCanceled()
  {
    return _canceled;
  }

  public void dispose()
  {
//    delete _tileImageProvider;
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