package org.glob3.mobile.generated; 
public class DTT_TileTextureBuilder extends RCObject
{
  private LeveledTexturedMesh _mesh;

  private DefaultTileTexturizer _texturizer;
  private TileRasterizer _tileRasterizer;
  private Tile _tile;

//  std::vector<Petition*> _petitions;
//  int                    _petitionsCount;
//  int                    _stepsDone;

  private TexturesHandler _texturesHandler;

  private final Vector2I _tileTextureResolution;
  private final Vector2I _tileMeshResolution;

  private IDownloader _downloader;

  private final Mesh _tessellatorMesh;

  private final TileTessellator _tessellator;

  private final boolean _logTilesPetitions;

  //  std::vector<TileTextureBuilder_PetitionStatus> _status;
  //  std::vector<long long>                         _requestsIds;


  private boolean _finalized;
  private boolean _canceled;
  private boolean _alreadyStarted;

  private long _texturePriority;



  private TextureIDReference getTopLevelTextureIdForTile(Tile tile)
  {
    LeveledTexturedMesh mesh = (LeveledTexturedMesh) tile.getTexturizedMesh();

    return (mesh == null) ? null : mesh.getTopLevelTextureId();
  }

  private LeveledTexturedMesh createMesh()
  {
    java.util.ArrayList<LazyTextureMapping> mappings = new java.util.ArrayList<LazyTextureMapping>();

    Tile ancestor = _tile;
    boolean fallbackSolved = false;
    while (ancestor != null && !fallbackSolved)
    {
      final boolean ownedTexCoords = true;
      final boolean transparent = false;
      LazyTextureMapping mapping = new LazyTextureMapping(new DTT_LTMInitializer(_tileMeshResolution, _tile, ancestor, _tessellator), ownedTexCoords, transparent);

      if (ancestor != _tile)
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

    return new LeveledTexturedMesh(_tessellatorMesh, false, mappings);
  }


  public DTT_TileTextureBuilder(DefaultTileTexturizer texturizer, TileRasterizer tileRasterizer, G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, IDownloader downloader, Tile tile, Mesh tessellatorMesh, TileTessellator tessellator, long texturePriority, boolean logTilesPetitions)
//                         const std::vector<Petition*>&     petitions,
//  _stepsDone(0),
  {
     _texturizer = texturizer;
     _tileRasterizer = tileRasterizer;
     _texturesHandler = rc.getTexturesHandler();
     _tileTextureResolution = layerTilesRenderParameters._tileTextureResolution;
     _tileMeshResolution = layerTilesRenderParameters._tileMeshResolution;
     _downloader = downloader;
     _tile = tile;
     _tessellatorMesh = tessellatorMesh;
     _mesh = null;
     _tessellator = tessellator;
     _finalized = false;
     _canceled = false;
     _alreadyStarted = false;
     _texturePriority = texturePriority;
     _logTilesPetitions = logTilesPetitions;
    _mesh = createMesh();
  }

  public final LeveledTexturedMesh getMesh()
  {
    return _mesh;
  }

  public final void cleanMesh()
  {
    _mesh = null;
  }

  public final void cleanTile()
  {
    _tile = null;
  }

  public final void start()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  }

  public final void cancel()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
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

}