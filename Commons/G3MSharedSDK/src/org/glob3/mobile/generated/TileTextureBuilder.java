package org.glob3.mobile.generated; 
public class TileTextureBuilder extends RCObject
{
  private MultiLayerTileTexturizer _texturizer;
  private Tile _tile;

  //  const TileKey             _tileKey;

  private java.util.ArrayList<Petition> _petitions = new java.util.ArrayList<Petition>();
  private int _petitionsCount;
  private int _stepsDone;

  private IFactory _factory; // FINAL WORD REMOVE BY CONVERSOR RULE
  private TexturesHandler _texturesHandler;
  private TextureBuilder _textureBuilder;
  private GL _gl;

  private final TilesRenderParameters _parameters;
  private IDownloader _downloader;

  private final Mesh _tessellatorMesh;

//  IFloatBuffer* _texCoords;
  private final TileTessellator _tessellator;

  private java.util.ArrayList<PetitionStatus> _status = new java.util.ArrayList<PetitionStatus>();
  private java.util.ArrayList<Long> _requestsIds = new java.util.ArrayList<Long>();


  private boolean _finalized;
  private boolean _canceled;
  private boolean _anyCanceled;
  private boolean _alreadyStarted;

  public LeveledTexturedMesh _mesh;

  public TileTextureBuilder(MultiLayerTileTexturizer texturizer, G3MRenderContext rc, LayerSet layerSet, TilesRenderParameters parameters, IDownloader downloader, Tile tile, Mesh tessellatorMesh, TileTessellator tessellator)
                     /*IFloatBuffer* texCoords*/
  //_tileKey(tile->getKey()),
//  _texCoords(texCoords),
  {
     _texturizer = texturizer;
     _factory = rc.getFactory();
     _texturesHandler = rc.getTexturesHandler();
     _textureBuilder = rc.getTextureBuilder();
     _gl = rc.getGL();
     _parameters = parameters;
     _downloader = downloader;
     _tile = tile;
     _tessellatorMesh = tessellatorMesh;
     _stepsDone = 0;
     _anyCanceled = false;
     _mesh = null;
     _tessellator = tessellator;
     _finalized = false;
     _canceled = false;
     _alreadyStarted = false;
    _petitions = layerSet.createTileMapPetitions(rc, tile, parameters._tileTextureWidth, parameters._tileTextureHeight);

    _petitionsCount = _petitions.size();

    for (int i = 0; i < _petitionsCount; i++)
    {
      _status.add(PetitionStatus.STATUS_PENDING);
    }

    _mesh = createMesh();
  }

  public final void start()
  {
    if (_canceled)
    {
      return;
    }
    if (_alreadyStarted)
    {
      return;
    }
    _alreadyStarted = true;

    if (_tile == null)
    {
      return;
    }

    for (int i = 0; i < _petitionsCount; i++)
    {
      final Petition petition = _petitions.get(i);

      //      const long long priority =  (_parameters->_incrementalTileQuality
      //                                   ? 1000 - _tile->getLevel()
      //                                   : _tile->getLevel());
      final long priority = DefineConstants.TILE_DOWNLOAD_PRIORITY + _tile.getLevel();

//      printf("%s\n", petition->getURL().getPath().c_str());

      final long requestId = _downloader.requestImage(new URL(petition.getURL()), priority, petition.getTimeToCache(), new BuilderDownloadStepDownloadListener(this, i), true);

      _requestsIds.add(requestId);
    }
  }

  public void dispose()
  {
    if (!_finalized && !_canceled)
    {
      cancel();
    }

    deletePetitions();
  }

  public final RectangleI getImageRectangleInTexture(Sector wholeSector, Sector imageSector, int textureWidth, int textureHeight)
  {
    final Vector2D lowerFactor = wholeSector.getUVCoordinates(imageSector.lower());

    final double widthFactor = imageSector.getDeltaLongitude().div(wholeSector.getDeltaLongitude());
    final double heightFactor = imageSector.getDeltaLatitude().div(wholeSector.getDeltaLatitude());

    return new RectangleI((int) IMathUtils.instance().round(lowerFactor._x * textureWidth), (int) IMathUtils.instance().round((1.0 - lowerFactor._y) * textureHeight), (int) IMathUtils.instance().round(widthFactor * textureWidth), (int) IMathUtils.instance().round(heightFactor * textureHeight));
  }

  public final void composeAndUploadTexture()
  {
    synchronized (this) {

      if (_mesh == null)
      {
        return;
      }

      java.util.ArrayList<IImage> images = new java.util.ArrayList<IImage>();
      java.util.ArrayList<RectangleI> rectangles = new java.util.ArrayList<RectangleI>();
      String textureId = _tile.getKey().tinyDescription();

      final int textureWidth = _parameters._tileTextureWidth;
      final int textureHeight = _parameters._tileTextureHeight;

      final Sector tileSector = _tile.getSector();

      for (int i = 0; i < _petitionsCount; i++)
      {
        final Petition petition = _petitions.get(i);
        IImage image = petition.getImage();

        if (image != null)
        {
          images.add(image);

          rectangles.add(getImageRectangleInTexture(tileSector, petition.getSector(), textureWidth, textureHeight));

          textureId += petition.getURL().getPath();
          textureId += "_";
        }
      }

      if (images.size() > 0)
      {
        _textureBuilder.createTextureFromImages(_gl, _factory, images, rectangles, textureWidth, textureHeight, new TextureUploader(this, rectangles, textureId), true);
      }

    }
  }

  public final void imageCreated(IImage image, java.util.ArrayList<RectangleI> rectangles, String textureId)
  {
    synchronized (this) {

      if (_mesh == null)
      {
        IFactory.instance().deleteImage(image);
        return;
      }

      final boolean isMipmap = false;

      final IGLTextureId glTextureId = _texturesHandler.getGLTextureId(image, GLFormat.rgba(), textureId, isMipmap);

      if (glTextureId != null)
      {
        if (!_mesh.setGLTextureIdForLevel(0, glTextureId))
        {
          _texturesHandler.releaseGLTextureId(glTextureId);
        }
      }

      IFactory.instance().deleteImage(image);

      for (int i = 0; i < rectangles.size(); i++)
      {
        if (rectangles.get(i) != null)
           rectangles.get(i).dispose();
      }

    }
  }

  public final void done()
  {
    if (!_finalized)
    {
      _finalized = true;

      if (!_canceled && (_tile != null) && (_mesh != null))
      {
        composeAndUploadTexture();
      }

      if (_tile != null)
      {
        _tile.setTextureSolved(true);
      }
    }
  }

  public final void deletePetitions()
  {
    for (int i = 0; i < _petitionsCount; i++)
    {
      Petition petition = _petitions.get(i);
      if (petition != null)
         petition.dispose();
    }
    _petitions.clear();
    _petitionsCount = 0;
  }

  public final void stepDone()
  {
    _stepsDone++;

    if (_stepsDone == _petitionsCount)
    {
      if (_anyCanceled)
      {
        ILogger.instance().logInfo("Completed with cancelation\n");
      }

      done();
    }
  }

  public final void cancel()
  {
    if (_canceled)
    {
      return;
    }

    _canceled = true;

    if (!_finalized)
    {
      for (int i = 0; i < _requestsIds.size(); i++)
      {
        final long requestId = _requestsIds.get(i);
        _downloader.cancelRequest(requestId);
      }
    }
    _requestsIds.clear();
  }

  public final boolean isCanceled()
  {
    return _canceled;
  }

//  void checkIsPending(int position) const {
//    if (_status[position] != STATUS_PENDING) {
//      ILogger::instance()->logError("Logic error: Expected STATUS_PENDING at position #%d but found status: %d\n",
//                                    position,
//                                    _status[position]);
//    }
//  }

  public final void stepDownloaded(int position, IImage image)
  {
    if (_canceled)
    {
      IFactory.instance().deleteImage(image);
      return;
    }
    //checkIsPending(position);

    _status.set(position, PetitionStatus.STATUS_DOWNLOADED);
    _petitions.get(position).setImage(image);

    stepDone();
  }

  public final void stepCanceled(int position)
  {
    if (_canceled)
    {
      return;
    }
    //checkIsPending(position);

    _anyCanceled = true;

    _status.set(position, PetitionStatus.STATUS_CANCELED);

    stepDone();
  }

  public final LeveledTexturedMesh createMesh()
  {
    java.util.ArrayList<LazyTextureMapping> mappings = new java.util.ArrayList<LazyTextureMapping>();

    Tile ancestor = _tile;
    boolean fallbackSolved = false;
    while (ancestor != null)
    {
      LazyTextureMapping mapping;
      if (fallbackSolved)
      {
        mapping = null;
      }
      else
      {
        final boolean ownedTexCoords = true;
        final boolean transparent = false;
        mapping = new LazyTextureMapping(new LTMInitializer(_tile, ancestor, _tessellator), _texturesHandler, ownedTexCoords, transparent);
      }

      if (ancestor != _tile)
      {
        if (!fallbackSolved)
        {
          final IGLTextureId glTextureId = _texturizer.getTopLevelGLTextureIdForTile(ancestor);
          if (glTextureId != null)
          {
            _texturesHandler.retainGLTextureId(glTextureId);
            mapping.setGLTextureId(glTextureId);
            fallbackSolved = true;
          }
        }
      }
      else
      {
        if (mapping != null)
        {
          if (mapping.getGLTextureId() != null)
          {
            ILogger.instance().logInfo("break (point) on me 3\n");
          }
        }
      }

      mappings.add(mapping);
      ancestor = ancestor.getParent();
    }

    if ((mappings != null) && (_tile != null))
    {
      if (mappings.size() != _tile.getLevel() + 1)
      {
        ILogger.instance().logInfo("pleae break (point) me\n");
      }
    }

    return new LeveledTexturedMesh(_tessellatorMesh, false, mappings);
  }

  public final LeveledTexturedMesh getMesh()
  {
    return _mesh;
  }

  public final void cleanMesh()
  {
    synchronized (this) {

      if (_mesh != null)
      {
        _mesh = null;
      }

    }
  }

  public final void cleanTile()
  {
    if (_tile != null)
    {
      _tile = null;
    }
  }

}