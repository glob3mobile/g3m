package org.glob3.mobile.generated; 
public class TileTextureBuilder extends RCObject
{

  public void dispose()
  {
    if (!_finalized && !_canceled)
    {
      cancel();
    }

    deletePetitions();
  super.dispose();

  }

  private MultiLayerTileTexturizer _texturizer;
  private TileRasterizer _tileRasterizer;
  private Tile _tile;

  private java.util.ArrayList<Petition> _petitions = new java.util.ArrayList<Petition>();
  private int _petitionsCount;
  private int _stepsDone;

  private TexturesHandler _texturesHandler;

  private final Vector2I _tileTextureResolution;
  private final Vector2I _tileMeshResolution;
  private final boolean _mercator;

  private IDownloader _downloader;

  private final Mesh _tessellatorMesh;

  private final TileTessellator _tessellator;

//  const int    _firstLevel;

  private java.util.ArrayList<TileTextureBuilder_PetitionStatus> _status = new java.util.ArrayList<TileTextureBuilder_PetitionStatus>();
  private java.util.ArrayList<Long> _requestsIds = new java.util.ArrayList<Long>();


  private boolean _finalized;
  private boolean _canceled;
//  bool _anyCanceled;
  private boolean _alreadyStarted;

  private long _texturePriority;


  private java.util.ArrayList<Petition> cleanUpPetitions(java.util.ArrayList<Petition> petitions)
  {

    final int petitionsSize = petitions.size();
    if (petitionsSize <= 1)
    {
      return petitions;
    }

    java.util.ArrayList<Petition> result = new java.util.ArrayList<Petition>();
    for (int i = 0; i < petitionsSize; i++)
    {
      Petition currentPetition = petitions.get(i);
      final Sector currentSector = currentPetition.getSector();

      boolean coveredByFollowingPetition = false;
      for (int j = i+1; j < petitionsSize; j++)
      {
        Petition followingPetition = petitions.get(j);

        // only opaque petitions can cover
        if (!followingPetition.isTransparent())
        {
          if (followingPetition.getSector().fullContains(currentSector))
          {
            coveredByFollowingPetition = true;
            break;
          }
        }
      }

      if (coveredByFollowingPetition)
      {
        if (currentPetition != null)
           currentPetition.dispose();
      }
      else
      {
        result.add(currentPetition);
      }
    }

    return result;
  }


  public LeveledTexturedMesh _mesh;

  public TileTextureBuilder(MultiLayerTileTexturizer texturizer, TileRasterizer tileRasterizer, G3MRenderContext rc, LayerSet layerSet, IDownloader downloader, Tile tile, Mesh tessellatorMesh, TileTessellator tessellator, long texturePriority)
//  _firstLevel( layerSet->getLayerTilesRenderParameters()->_firstLevel ),
//  _anyCanceled(false),
  {
     _texturizer = texturizer;
     _tileRasterizer = tileRasterizer;
     _texturesHandler = rc.getTexturesHandler();
     _tileTextureResolution = layerSet.getLayerTilesRenderParameters()._tileTextureResolution;
     _tileMeshResolution = layerSet.getLayerTilesRenderParameters()._tileMeshResolution;
     _mercator = layerSet.getLayerTilesRenderParameters()._mercator;
     _downloader = downloader;
     _tile = tile;
     _tessellatorMesh = tessellatorMesh;
     _stepsDone = 0;
     _mesh = null;
     _tessellator = tessellator;
     _finalized = false;
     _canceled = false;
     _alreadyStarted = false;
     _texturePriority = texturePriority;
    _petitions = cleanUpPetitions(layerSet.createTileMapPetitions(rc, tile));

    _petitionsCount = _petitions.size();

    for (int i = 0; i < _petitionsCount; i++)
    {
      _status.add(TileTextureBuilder_PetitionStatus.STATUS_PENDING);
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

      final long priority = _texturePriority + _tile.getLevel();

      //      printf("%s\n", petition->getURL().getPath().c_str());

      final long requestId = _downloader.requestImage(new URL(petition.getURL()), priority, petition.getTimeToCache(), petition.getReadExpired(), new BuilderDownloadStepDownloadListener(this, i), true);
      if (requestId >= 0)
      {
        _requestsIds.add(requestId);
      }
    }
  }

  public final RectangleF getInnerRectangle(int wholeSectorWidth, int wholeSectorHeight, Sector wholeSector, Sector innerSector)
  {
    //printf("%s - %s\n", wholeSector.description().c_str(), innerSector.description().c_str());

    final double widthFactor = innerSector._deltaLongitude.div(wholeSector._deltaLongitude);
    final double heightFactor = innerSector._deltaLatitude.div(wholeSector._deltaLatitude);

    final Vector2D lowerUV = wholeSector.getUVCoordinates(innerSector.getNW());

    return new RectangleF((float)(lowerUV._x * wholeSectorWidth), (float)(lowerUV._y * wholeSectorHeight), (float)(widthFactor * wholeSectorWidth), (float)(heightFactor * wholeSectorHeight));
  }

  public final void composeAndUploadTexture()
  {
    synchronized (this) {

      if (_mesh == null)
      {
        return;
      }

      final java.util.ArrayList<IImage> images = new java.util.ArrayList<IImage>();
      java.util.ArrayList<RectangleF> sourceRects = new java.util.ArrayList<RectangleF>();
      java.util.ArrayList<RectangleF> destRects = new java.util.ArrayList<RectangleF>();
      String textureId = _tile.getKey().tinyDescription();

      final Sector tileSector = _tile.getSector();

      for (int i = 0; i < _petitionsCount; i++)
      {
        final Petition petition = _petitions.get(i);
        IImage image = petition.getImage();

        if (image != null)
        {
          final Sector imageSector = petition.getSector();
          //Finding intersection image sector - tile sector = srcReq
          final Sector intersectionSector = tileSector.intersection(imageSector);

          RectangleF sourceRect = null;
          if (!intersectionSector.isEqualsTo(imageSector))
          {
            sourceRect = getInnerRectangle(image.getWidth(), image.getHeight(), imageSector, intersectionSector);
          }
          else
          {
            sourceRect = new RectangleF(0, 0, image.getWidth(), image.getHeight());
          }

          //Part of the image we are going to draw
          sourceRects.add(sourceRect);

          images.add(image);

          //Where we are going to draw the image
          destRects.add(getInnerRectangle(_tileTextureResolution._x, _tileTextureResolution._y, tileSector, intersectionSector));
          textureId += petition.getURL().getPath();
          textureId += "_";
        }
      }

      if (images.size() > 0)
      {

        if (_tileRasterizer != null)
        {
          textureId += "_";
          textureId += _tileRasterizer.getId();
        }

        IImageUtils.combine(_tileTextureResolution, images, sourceRects, destRects, new TextureUploader(this, _tile, _mercator, _tileRasterizer, sourceRects, destRects, textureId), true);
      }

    }
  }

  public final void imageCreated(IImage image, java.util.ArrayList<RectangleF> srcRects, java.util.ArrayList<RectangleF> dstRects, String textureId)
  {
    synchronized (this) {

      if (_mesh != null)
      {
        final boolean isMipmap = false;

        final IGLTextureId glTextureId = _texturesHandler.getGLTextureId(image, GLFormat.rgba(), textureId, isMipmap);

        if (glTextureId != null)
        {
          if (!_mesh.setGLTextureIdForLevel(0, glTextureId))
          {
            _texturesHandler.releaseGLTextureId(glTextureId);
          }
        }
      }

      IFactory.instance().deleteImage(image);

      for (int i = 0; i < srcRects.size(); i++)
      {
        if (srcRects.get(i) != null)
           srcRects.get(i).dispose();
      }

      for (int i = 0; i < dstRects.size(); i++)
      {
        if (dstRects.get(i) != null)
           dstRects.get(i).dispose();
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
//      if (_anyCanceled) {
//        ILogger::instance()->logInfo("Completed with cancelation\n");
//      }

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

    _status.set(position, TileTextureBuilder_PetitionStatus.STATUS_DOWNLOADED);
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

//    _anyCanceled = true;
    _status.set(position, TileTextureBuilder_PetitionStatus.STATUS_CANCELED);

    stepDone();
  }

  public final LeveledTexturedMesh createMesh()
  {
    java.util.ArrayList<LazyTextureMapping> mappings = new java.util.ArrayList<LazyTextureMapping>();

    Tile ancestor = _tile;
    boolean fallbackSolved = false;
    while (ancestor != null && !fallbackSolved)
    {
      final boolean ownedTexCoords = true;
      final boolean transparent = false;
      LazyTextureMapping mapping = new LazyTextureMapping(new LTMInitializer(_tileMeshResolution, _tile, ancestor, _tessellator, _mercator), _texturesHandler, ownedTexCoords, transparent);

      if (ancestor != _tile)
      {
        final IGLTextureId glTextureId = _texturizer.getTopLevelGLTextureIdForTile(ancestor);
        if (glTextureId != null)
        {
          _texturesHandler.retainGLTextureId(glTextureId);
          mapping.setGLTextureId(glTextureId);
          fallbackSolved = true;
        }
      }

      mappings.add(mapping);

      ancestor = ancestor.getParent();
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