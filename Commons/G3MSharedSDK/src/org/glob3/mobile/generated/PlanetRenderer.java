package org.glob3.mobile.generated; 
public class PlanetRenderer extends DefaultRenderer implements ChangedListener, ChangedInfoListener, SurfaceElevationProvider
{
  private TileTessellator _tessellator;
  private ElevationDataProvider _elevationDataProvider;
  private boolean _ownsElevationDataProvider;
  private TileTexturizer _texturizer;
  private LayerSet _layerSet;
  private final TilesRenderParameters _tilesRenderParameters;
  private final boolean _showStatistics;
  private final boolean _logTilesPetitions;
  private ITileVisitor _tileVisitor = null;

  private TileRenderingListener _tileRenderingListener;
  private final java.util.ArrayList<Tile> _tilesStartedRendering;
  private java.util.ArrayList<String> _tilesStoppedRendering;

  private TilesStatistics _statistics = new TilesStatistics();

  private Camera     _lastCamera;

  private java.util.ArrayList<Tile> _firstLevelTiles = new java.util.ArrayList<Tile>();
  private boolean _firstLevelTilesJustCreated;
  private boolean _allFirstLevelTilesAreTextureSolved;

  private ITimer _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

  private void clearFirstLevelTiles()
  {
    final int firstLevelTilesCount = _firstLevelTiles.size();
    for (int i = 0; i < firstLevelTilesCount; i++)
    {
      Tile tile = _firstLevelTiles.get(i);
      tile.toBeDeleted(_texturizer, _elevationDataProvider, _tilesStoppedRendering);
      if (tile != null)
         tile.dispose();
    }
  
    if (_tileRenderingListener != null)
    {
      if (!_tilesStartedRendering.isEmpty() || !_tilesStoppedRendering.isEmpty())
      {
        _tileRenderingListener.changedTilesRendering(_tilesStartedRendering, _tilesStoppedRendering);
        _tilesStartedRendering.clear();
        _tilesStoppedRendering.clear();
      }
    }
  
    _firstLevelTiles.clear();
  }
  private void createFirstLevelTiles(G3MContext context)
  {
  
    final LayerTilesRenderParameters parameters = getLayerTilesRenderParameters();
    if (parameters == null)
    {
      //ILogger::instance()->logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create first-level tiles");
      return;
    }
  
    java.util.ArrayList<Tile> topLevelTiles = new java.util.ArrayList<Tile>();
  
    final Angle fromLatitude = parameters._topSector._lower._latitude;
    final Angle fromLongitude = parameters._topSector._lower._longitude;
  
    final Angle deltaLan = parameters._topSector._deltaLatitude;
    final Angle deltaLon = parameters._topSector._deltaLongitude;
  
    final int topSectorSplitsByLatitude = parameters._topSectorSplitsByLatitude;
    final int topSectorSplitsByLongitude = parameters._topSectorSplitsByLongitude;
  
    final Angle tileHeight = deltaLan.div(topSectorSplitsByLatitude);
    final Angle tileWidth = deltaLon.div(topSectorSplitsByLongitude);
  
    for (int row = 0; row < topSectorSplitsByLatitude; row++)
    {
      final Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
      final Angle tileLatTo = tileLatFrom.add(tileHeight);
  
      for (int col = 0; col < topSectorSplitsByLongitude; col++)
      {
        final Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
        final Angle tileLonTo = tileLonFrom.add(tileWidth);
  
        final Geodetic2D tileLower = new Geodetic2D(tileLatFrom, tileLonFrom);
        final Geodetic2D tileUpper = new Geodetic2D(tileLatTo, tileLonTo);
        final Sector sector = new Sector(tileLower, tileUpper);
  
        if (_renderedSector == null || sector.touchesWith(_renderedSector)) //Do not create innecesary tiles
        {
          Tile tile = new Tile(_texturizer, null, sector, parameters._mercator, 0, row, col, this, _tileCache, _deleteTexturesOfInvisibleTiles);
          if (parameters._firstLevel == 0)
          {
            _firstLevelTiles.add(tile);
          }
          else
          {
            topLevelTiles.add(tile);
          }
        }
      }
    }
  
    if (parameters._firstLevel > 0)
    {
      final int topLevelTilesSize = topLevelTiles.size();
      for (int i = 0; i < topLevelTilesSize; i++)
      {
        Tile tile = topLevelTiles.get(i);
        createFirstLevelTiles(_firstLevelTiles, tile, parameters._firstLevel);
      }
    }
  
    sortTiles(_firstLevelTiles);
  
    context.getLogger().logInfo("Created %d first level tiles", _firstLevelTiles.size());
  
    if (_firstLevelTiles.size() > 64)
    {
      context.getLogger().logWarning("%d tiles are many for the first level. We recommend a number of those less than 64. You can review some parameters (Render Sector and/or First Level) to reduce the number of tiles.", _firstLevelTiles.size());
    }
  
    _firstLevelTilesJustCreated = true;
  }
  private void createFirstLevelTiles(java.util.ArrayList<Tile> firstLevelTiles, Tile tile, int firstLevel)
  {
  
    if (tile._level == firstLevel)
    {
      firstLevelTiles.add(tile);
    }
    else
    {
      final Sector sector = tile._sector;
      final Geodetic2D lower = sector._lower;
      final Geodetic2D upper = sector._upper;
  
      final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
  
      final Angle splitLatitude = (tile._mercator ? MercatorUtils.calculateSplitLatitude(lower._latitude, upper._latitude) : Angle.midAngle(lower._latitude, upper._latitude));
  
      java.util.ArrayList<Tile> children = tile.createSubTiles(splitLatitude, splitLongitude, false);
  
      final int childrenSize = children.size();
      for (int i = 0; i < childrenSize; i++)
      {
        Tile child = children.get(i);
        createFirstLevelTiles(firstLevelTiles, child, firstLevel);
      }
  
      children = null;
      if (tile != null)
         tile.dispose();
    }
  }

  private void sortTiles(java.util.ArrayList<Tile> tiles)
  {
    java.util.Collections.sort(tiles,
                               new java.util.Comparator<Tile>() {
                                 @Override
                                 public int compare(final Tile i,
                                                    final Tile j) {
                                   final int rowI = i._row;
                                   final int rowJ = j._row;
                                   if (rowI < rowJ) {
                                     return -1;
                                   }
                                   if (rowI > rowJ) {
                                     return 1;
                                   }
  
                                   final int columnI = i._column;
                                   final int columnJ = j._column;
                                   if (columnI < columnJ) {
                                     return -1;
                                   }
                                   if (columnI > columnJ) {
                                     return 1;
                                   }
                                   return 0;
                                 }
                               });
  }

  private boolean _firstRender;

  private void pruneFirstLevelTiles()
  {
    final int firstLevelTilesCount = _firstLevelTiles.size();
    for (int i = 0; i < firstLevelTilesCount; i++)
    {
      Tile tile = _firstLevelTiles.get(i);
      tile.prune(_texturizer, _elevationDataProvider, _tilesStoppedRendering);
    }
    if (_tileRenderingListener != null)
    {
      if (!_tilesStartedRendering.isEmpty() || !_tilesStoppedRendering.isEmpty())
      {
        _tileRenderingListener.changedTilesRendering(_tilesStartedRendering, _tilesStoppedRendering);
        _tilesStartedRendering.clear();
        _tilesStoppedRendering.clear();
      }
    }
  }

  private Sector _lastVisibleSector;

  private java.util.ArrayList<VisibleSectorListenerEntry> _visibleSectorListeners = new java.util.ArrayList<VisibleSectorListenerEntry>();

  private void visitTilesTouchesWith(Sector sector, int firstLevel, int maxLevel)
  {
    if (_tileVisitor != null)
    {
      final LayerTilesRenderParameters parameters = getLayerTilesRenderParameters();
      if (parameters == null)
      {
        ILogger.instance().logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create first-level tiles");
        return;
      }
  
      final int firstLevelToVisit = (firstLevel < parameters._firstLevel) ? parameters._firstLevel : firstLevel;
      if (firstLevel < firstLevelToVisit)
      {
        ILogger.instance().logError("Can only visit from level %d", firstLevelToVisit);
        return;
      }
  
      final int maxLevelToVisit = (maxLevel > parameters._maxLevel) ? parameters._maxLevel : maxLevel;
      if (maxLevel > maxLevelToVisit)
      {
        ILogger.instance().logError("Can only visit to level %d", maxLevelToVisit);
        return;
      }
  
      if (firstLevelToVisit > maxLevelToVisit)
      {
        ILogger.instance().logError("Can't visit, first level is gratter than max level");
        return;
      }
  
      java.util.ArrayList<Layer> layers = new java.util.ArrayList<Layer>();
      final int layersCount = _layerSet.size();
      for (int i = 0; i < layersCount; i++)
      {
        Layer layer = _layerSet.getLayer(i);
        if (layer.isEnable() && layer.getRenderState()._type == RenderState_Type.RENDER_READY)
        {
          layers.add(layer);
        }
      }
  
      final int firstLevelTilesCount = _firstLevelTiles.size();
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        Tile tile = _firstLevelTiles.get(i);
        if (tile._sector.touchesWith(sector))
        {
          _tileVisitor.visitTile(layers, tile);
          visitSubTilesTouchesWith(layers, tile, sector, firstLevelToVisit, maxLevelToVisit);
        }
      }
    }
    else
    {
      ILogger.instance().logError("TileVisitor is NULL");
    }
  }

  private void visitSubTilesTouchesWith(java.util.ArrayList<Layer> layers, Tile tile, Sector sectorToVisit, int topLevel, int maxLevel)
  {
    if (tile._level < maxLevel)
    {
      java.util.ArrayList<Tile> subTiles = tile.getSubTiles();
  
      final int subTilesCount = subTiles.size();
      for (int i = 0; i < subTilesCount; i++)
      {
        Tile tl = subTiles.get(i);
        if (tl._sector.touchesWith(sectorToVisit))
        {
          if ((tile._level >= topLevel))
          {
            _tileVisitor.visitTile(layers, tl);
          }
          visitSubTilesTouchesWith(layers, tl, sectorToVisit, topLevel, maxLevel);
        }
      }
    }
  }

  private long _tileDownloadPriority;

  private float _verticalExaggeration;

  private boolean _recreateTilesPending;

  private GLState _glState;
  private void updateGLState(G3MRenderContext rc)
  {
  
    final Camera cam = rc.getCurrentCamera();
    ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glState.addGLFeature(new ModelViewGLFeature(cam), true);
    }
    else
    {
      f.setMatrix(cam.getModelViewMatrix44D());
    }
  }

  private SurfaceElevationProvider_Tree _elevationListenersTree = new SurfaceElevationProvider_Tree();

  private boolean _renderTileMeshes;

  private Sector _renderedSector;
  //  bool _validLayerTilesRenderParameters;
  private boolean _layerTilesRenderParametersDirty;
  private LayerTilesRenderParameters _layerTilesRenderParameters;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private java.util.ArrayList<TerrainTouchListener> _terrainTouchListeners = new java.util.ArrayList<TerrainTouchListener>();

  G3MRenderContext _renderContext;
  //  std::list<Tile*> _tilesRenderedInLastFrame;

  private long _renderedTilesListFrame;
  private java.util.LinkedList<Tile> _renderedTiles = new java.util.LinkedList<Tile>();
  private java.util.LinkedList<Tile> getRenderedTilesList(G3MRenderContext rc)
  {
  
    long frameCounter = rc.frameCounter();
    if (frameCounter != _renderedTilesListFrame)
    {
      _renderedTilesListFrame = frameCounter;
  
      final LayerTilesRenderParameters layerTilesRenderParameters = getLayerTilesRenderParameters();
      if (layerTilesRenderParameters == null)
      {
        return null;
      }
  
      final IDeviceInfo deviceInfo = IFactory.instance().getDeviceInfo();
      final float deviceQualityFactor = deviceInfo.getQualityFactor();
  
      final int firstLevelTilesCount = _firstLevelTiles.size();
  
      _lastCamera = rc.getCurrentCamera();
  
      final Planet planet = rc.getPlanet();
      final Vector3D cameraNormalizedPosition = _lastCamera.getNormalizedPosition();
      double cameraAngle2HorizonInRadians = _lastCamera.getAngle2HorizonInRadians();
      final Frustum cameraFrustumInModelCoordinates = _lastCamera.getFrustumInModelCoordinates();
      final Frustum cameraWiderFrustumInModelCoordinates = _lastCamera.getWiderFrustumInModelCoordinates(_frustumCullingFactor);
  
      _renderedTiles.clear();
  
      //Texture Size for every tile
      int texWidth = layerTilesRenderParameters._tileTextureResolution._x;
      int texHeight = layerTilesRenderParameters._tileTextureResolution._y;
  
      final double factor = _tilesRenderParameters._texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)
      final double correctionFactor = (deviceInfo.getDPI() * deviceQualityFactor) / factor;
  
      texWidth *= correctionFactor;
      texHeight *= correctionFactor;
  
      final double texWidthSquared = texWidth * texWidth;
      final double texHeightSquared = texHeight * texHeight;
  
      final double nowInMS = _lastSplitTimer.now().milliseconds(); //Getting now from _lastSplitTimer
  
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
<<<<<<< HEAD
        _firstLevelTiles.get(i).updateQuadTree(rc, _renderedTiles, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates, cameraWiderFrustumInModelCoordinates, _statistics, _verticalExaggeration, layerTilesRenderParameters, _texturizer, _tilesRenderParameters, _lastSplitTimer, _elevationDataProvider, _tessellator, _layerSet, _renderedSector, _firstRender, _tileDownloadPriority, texWidthSquared, texHeightSquared, nowInMS, _tileRenderingListener); // if first render, force full render
=======
        _firstLevelTiles.get(i).updateQuadTree(rc, _renderedTiles, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates, _statistics, _verticalExaggeration, layerTilesRenderParameters, _texturizer, _tilesRenderParameters, _lastSplitTimer, _elevationDataProvider, _tessellator, _layerSet, _renderedSector, _firstRender, _tileDownloadPriority, texWidthSquared, texHeightSquared, nowInMS, _tilesStartedRendering, _tilesStoppedRendering); // if first render, force full render
>>>>>>> zrender-touchhandlers
      }
    }
    else
    {
      //ILogger::instance()->logInfo("Reusing Render Tiles List");
    }
  
    return _renderedTiles;
  }

  private TouchEventType _touchEventTypeOfTerrainTouchListener;


  private java.util.ArrayList<Tile> _toVisit = new java.util.ArrayList<Tile>();
  private java.util.ArrayList<Tile> _toVisitInNextIteration = new java.util.ArrayList<Tile>();

<<<<<<< HEAD
  private void addLayerSetURLForSector(java.util.LinkedList<URL> urls, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = _layerSet.createTileMapPetitions(_renderContext, _layerTilesRenderParameters, tile);
    for (int i = 0; i < petitions.size(); i++)
    {
      urls.addLast(petitions.get(i).getURL());
      if (petitions.get(i) != null)
         petitions.get(i).dispose();
    }
  }
  private boolean sectorCloseToRoute(Sector sector, java.util.LinkedList<Geodetic2D> route, double angularDistanceFromCenterInRadians)
  {
  
    Geodetic2D geoCenter = sector.getCenter();
    Vector2D center = new Vector2D(geoCenter._longitude._radians, geoCenter._latitude._radians);
  
  
    java.util.Iterator<Geodetic2D> iterator = route.iterator();
  
  	Geodetic2D geoA = null;
  	Geodetic2D geoB = iterator.next();
  
    while (iterator.hasNext())
    {
      geoA = geoB;
      geoB = iterator.next();
  
      final Vector2D A = new Vector2D(geoA._longitude._radians, geoA._latitude._radians);
      final Vector2D B = new Vector2D(geoB._longitude._radians, geoB._latitude._radians);
  
      double dist = center.distanceToSegment(A, B);
  
      if (dist <= angularDistanceFromCenterInRadians)
      {
        return true;
      }
    }
  
    return false;
  }

  private float _frustumCullingFactor;

  private TileCache _tileCache;
  private boolean _deleteTexturesOfInvisibleTiles;

  private ElevationDataProviderReadyListener _elevationDataProviderReadyListener;
  private boolean _elevationDataProviderReadyListenerAutoDelete;

  public PlanetRenderer(TileTessellator tessellator, ElevationDataProvider elevationDataProvider, boolean ownsElevationDataProvider, float verticalExaggeration, TileTexturizer texturizer, LayerSet layerSet, TilesRenderParameters tilesRenderParameters, boolean showStatistics, long tileDownloadPriority, Sector renderedSector, boolean renderTileMeshes, boolean logTilesPetitions, TileRenderingListener tileRenderingListener, ChangedRendererInfoListener changedInfoListener, int sizeOfTileCache, boolean deleteTexturesOfInvisibleTiles, TouchEventType touchEventTypeOfTerrainTouchListener)
                                 //                               TileRasterizer*              tileRasterizer,
  //_tileRasterizer(tileRasterizer),
=======
  public PlanetRenderer(TileTessellator tessellator, ElevationDataProvider elevationDataProvider, boolean ownsElevationDataProvider, float verticalExaggeration, TileTexturizer texturizer, LayerSet layerSet, TilesRenderParameters tilesRenderParameters, boolean showStatistics, long tileDownloadPriority, Sector renderedSector, boolean renderTileMeshes, boolean logTilesPetitions, TileRenderingListener tileRenderingListener, ChangedRendererInfoListener changedInfoListener, TouchEventType touchEventTypeOfTerrainTouchListener)
>>>>>>> zrender-touchhandlers
  {
     _tessellator = tessellator;
     _elevationDataProvider = elevationDataProvider;
     _ownsElevationDataProvider = ownsElevationDataProvider;
     _verticalExaggeration = verticalExaggeration;
     _texturizer = texturizer;
     _layerSet = layerSet;
     _tilesRenderParameters = tilesRenderParameters;
     _showStatistics = showStatistics;
     _firstLevelTilesJustCreated = false;
     _lastSplitTimer = null;
     _lastCamera = null;
     _firstRender = false;
     _lastVisibleSector = null;
     _tileDownloadPriority = tileDownloadPriority;
     _allFirstLevelTilesAreTextureSolved = false;
     _recreateTilesPending = false;
     _glState = new GLState();
     _renderedSector = renderedSector.isEquals(Sector.fullSphere())? null : new Sector(renderedSector);
     _layerTilesRenderParameters = null;
     _layerTilesRenderParametersDirty = true;
     _renderContext = null;
     _renderedTilesListFrame = -1;
     _renderTileMeshes = renderTileMeshes;
     _logTilesPetitions = logTilesPetitions;
     _tileRenderingListener = tileRenderingListener;
     _deleteTexturesOfInvisibleTiles = deleteTexturesOfInvisibleTiles;
     _touchEventTypeOfTerrainTouchListener = touchEventTypeOfTerrainTouchListener;
     _elevationDataProviderReadyListenerAutoDelete = true;
     _elevationDataProviderReadyListener = null;
    _context = null;
    _layerSet.setChangeListener(this);
  
    _layerSet.setChangedInfoListener(this);
    _changedInfoListener = changedInfoListener;
  
<<<<<<< HEAD
    _frustumCullingFactor = 1.0F;
    _tileCache = sizeOfTileCache < 1? null : new TileCache(sizeOfTileCache);
=======
    if (_tileRenderingListener == null)
    {
      _tilesStartedRendering = null;
      _tilesStoppedRendering = null;
    }
    else
    {
      _tilesStartedRendering = new java.util.ArrayList<Tile>();
      _tilesStoppedRendering = new java.util.ArrayList<String>();
    }
  
    _rendererIdentifier = -1;
>>>>>>> zrender-touchhandlers
  }

  public void dispose()
  {
    pruneFirstLevelTiles();
    clearFirstLevelTiles();
  
    _layerTilesRenderParameters = null;
  
    if (_tessellator != null)
       _tessellator.dispose();
    if (_elevationDataProvider != null)
       _elevationDataProvider.dispose();
    if (_texturizer != null)
       _texturizer.dispose();
    if (_tilesRenderParameters != null)
       _tilesRenderParameters.dispose();
  
    if (_lastSplitTimer != null)
       _lastSplitTimer.dispose();
  
    if (_lastVisibleSector != null)
       _lastVisibleSector.dispose();
  
    final int visibleSectorListenersCount = _visibleSectorListeners.size();
    for (int i = 0; i < visibleSectorListenersCount; i++)
    {
      VisibleSectorListenerEntry entry = _visibleSectorListeners.get(i);
      if (entry != null)
         entry.dispose();
    }
  
    if (_renderedSector != null)
       _renderedSector.dispose();
  
    if (_tileRenderingListener != null)
       _tileRenderingListener.dispose();
  
  
    super.dispose();
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
    pruneFirstLevelTiles();
  
    clearFirstLevelTiles();
    createFirstLevelTiles(context);
  
    if (_lastSplitTimer != null)
       _lastSplitTimer.dispose();
    _lastSplitTimer = context.getFactory().createTimer();
  
    _layerSet.initialize(context);
    _texturizer.initialize(context, _tilesRenderParameters);
    if (_elevationDataProvider != null)
    {
      _elevationDataProvider.initialize(context);
    }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
  
    final LayerTilesRenderParameters layerTilesRenderParameters = getLayerTilesRenderParameters();
    if (layerTilesRenderParameters == null)
    {
      return;
    }
  
    updateGLState(rc);
  
<<<<<<< HEAD
=======
    ///#warning Testing Terrain Normals
>>>>>>> zrender-touchhandlers
    _glState.setParent(glState);
  
    // Saving camera for use in onTouchEvent
    _lastCamera = rc.getCurrentCamera();
  
    _statistics.clear();
<<<<<<< HEAD
=======
  
>>>>>>> zrender-touchhandlers
    if (_firstRender && _tilesRenderParameters._forceFirstLevelTilesRenderOnStart)
    {
      // force one render pass of the firstLevelTiles tiles to make the (toplevel) textures
      // loaded as they will be used as last-chance fallback texture for any tile.
      _firstRender = false;
  
      final int firstLevelTilesCount = _firstLevelTiles.size();
  
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        Tile tile = _firstLevelTiles.get(i);
        tile.performRawRender(rc, _glState, _texturizer, _elevationDataProvider, _tessellator, _layerTilesRenderParameters, _layerSet, _tilesRenderParameters, _firstRender, _tileDownloadPriority, _statistics, _logTilesPetitions);
      }
  
    }
    else
    {
  
      java.util.LinkedList<Tile> renderedTiles = getRenderedTilesList(rc);
  
      for (java.util.Iterator<Tile> iter = renderedTiles.iterator(); iter.hasNext();)
      {
        Tile tile = iter.next();
  
        tile.performRawRender(rc, _glState, _texturizer, _elevationDataProvider, _tessellator, _layerTilesRenderParameters, _layerSet, _tilesRenderParameters, _firstRender, _tileDownloadPriority, _statistics, _logTilesPetitions);
      }
    }
  
    if (_showStatistics)
    {
      _statistics.log(rc.getLogger());
    }
      _lastVisibleSector = _statistics.updateVisibleSector(_lastVisibleSector);
      // ILogger::instance()->logInfo("=> visibleSector: %s", _lastVisibleSector->description().c_str());
      if (_lastVisibleSector != null)
      {
        final int visibleSectorListenersCount = _visibleSectorListeners.size();
        for (int i = 0; i < visibleSectorListenersCount; i++)
        {
          VisibleSectorListenerEntry entry = _visibleSectorListeners.get(i);
          entry.tryToNotifyListener(_lastVisibleSector, rc);
        }
      }
  
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    if (_lastCamera == null)
    {
      return false;
    }
  
    if (touchEvent.getType() == _touchEventTypeOfTerrainTouchListener)
    {
      final Vector2I pixel = touchEvent.getTouch(0).getPos();
  
      Vector3D positionCartesian = null;
  
      final Planet planet = ec.getPlanet();
      //    if (ec->getWidget() != NULL){
      positionCartesian = new Vector3D(ec.getWidget().getScenePositionForPixel(pixel._x, pixel._y));
      //    } else{
      //      const Vector3D ray = _lastCamera->pixel2Ray(pixel);
      //      const Vector3D origin = _lastCamera->getCartesianPosition();
      //      positionCartesian = new Vector3D(planet->closestIntersection(origin, ray));
      //    }
  
      if (positionCartesian == null || positionCartesian.isNan())
      {
        /*
         =======
         const Vector3D positionCartesian = planet->closestIntersection(origin, ray);
         if (positionCartesian.isNan()) {
         ILogger::instance()->logWarning("PlanetRenderer::onTouchEvent: positionCartesian ( - planet->closestIntersection(origin, ray) - ) is NaN");
         >>>>>>> origin/purgatory
         */
  
        return false;
      }
  
      Geodetic3D position = planet.toGeodetic3D(positionCartesian);
  
      final int firstLevelTilesCount = _firstLevelTiles.size();
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        final Tile tile = _firstLevelTiles.get(i).getDeepestTileContaining(position);
        if (tile != null)
        {
<<<<<<< HEAD
=======
  
          final Vector2I tileDimension = new Vector2I(256, 256);
          final Vector2I normalizedPixel = tile.getNormalizedPixelsFromPosition(position.asGeodetic2D(), tileDimension);
          ILogger.instance().logInfo("Touched on %s", tile.description());
          ILogger.instance().logInfo("Touched on position %s", position.description());
          ILogger.instance().logInfo("Touched on pixels %s", normalizedPixel.description());
>>>>>>> zrender-touchhandlers
          ILogger.instance().logInfo("Camera position=%s heading=%f pitch=%f", _lastCamera.getGeodeticPosition().description(), _lastCamera.getHeading()._degrees, _lastCamera.getPitch()._degrees);
  
  //        ILogger::instance()->logInfo("Touched on %s", tile->description().c_str());
  //        Vector3D NW = planet->toCartesian(tile->_sector.getNW());
  //        Vector3D SW = planet->toCartesian(tile->_sector.getSW());
  //        double distanceNS = NW.distanceTo(SW);
  //        double distancePerVertex = distanceNS / (tile->getLastTileMeshResolution()._y-1);
  //        ILogger::instance()->logInfo("-- Tile level %d: approx. 1 vertex every %f meters\n", tile->_level, distancePerVertex);
  
          if (_texturizer.onTerrainTouchEvent(ec, position, tile, _layerSet))
          {
            return true;
          }
  
          final int terrainTouchListenersSize = _terrainTouchListeners.size();
          for (int j = terrainTouchListenersSize-1; j >= 0; j--)
          {
            TerrainTouchListener listener = _terrainTouchListeners.get(j);
            if (listener.onTerrainTouch(ec, pixel, _lastCamera, position, tile))
            {
              return true;
            }
          }
  
          return false;
        }
      }
    }
  
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
<<<<<<< HEAD
  
    if (_elevationDataProvider != null)
    {
      if (!_elevationDataProvider.isReadyToRender(rc))
      {
        return RenderState.busy();
      }
      else
      {
        _elevationDataProviderReadyListener.onReady();
        if (_elevationDataProviderReadyListenerAutoDelete)
        {
          if (_elevationDataProviderReadyListener != null)
             _elevationDataProviderReadyListener.dispose();
        }
      }
=======
    if (_tessellator == null)
    {
      return RenderState.error("Tessellator is null");
    }
  
    if (_texturizer == null)
    {
      return RenderState.error("Texturizer is null");
>>>>>>> zrender-touchhandlers
    }
  
    final LayerTilesRenderParameters layerTilesRenderParameters = getLayerTilesRenderParameters();
    if (layerTilesRenderParameters == null)
    {
      if (_errors.isEmpty())
      {
        if (_tilesRenderParameters._forceFirstLevelTilesRenderOnStart)
        {
          return RenderState.busy();
        }
      }
      else
      {
        return RenderState.error(_errors);
      }
    }
  
    final RenderState layerSetRenderState = _layerSet.getRenderState();
    if (layerSetRenderState._type != RenderState_Type.RENDER_READY)
    {
      return layerSetRenderState;
    }
  
<<<<<<< HEAD
=======
    if (_elevationDataProvider != null)
    {
      if (!_elevationDataProvider.isReadyToRender(rc))
      {
        return RenderState.busy();
      }
    }
  
    final RenderState texturizerRenderState = _texturizer.getRenderState(_layerSet);
    if (texturizerRenderState._type != RenderState_Type.RENDER_READY)
    {
      return texturizerRenderState;
    }
  
>>>>>>> zrender-touchhandlers
    if (_firstLevelTilesJustCreated)
    {
      _firstLevelTilesJustCreated = false;
  
      final int firstLevelTilesCount = _firstLevelTiles.size();
  
      if (_tilesRenderParameters._forceFirstLevelTilesRenderOnStart)
      {
        _statistics.clear();
  
        for (int i = 0; i < firstLevelTilesCount; i++)
        {
          Tile tile = _firstLevelTiles.get(i);
          tile.prepareForFullRendering(rc, _texturizer, _elevationDataProvider, _tessellator, layerTilesRenderParameters, _layerSet, _tilesRenderParameters, true, _tileDownloadPriority, _verticalExaggeration, _logTilesPetitions); // forceFullRender
        }
      }
  //<<<<<<< HEAD
  //
  //    if (_texturizer != NULL) {
  //      for (int i = 0; i < firstLevelTilesCount; i++) {
  //        Tile* tile = _firstLevelTiles[i];
  //        _texturizer->justCreatedTopTile(rc, tile, _layerSet);
  //      }
  //    }
  //  }
  //
  //  if (_tilesRenderParameters->_forceFirstLevelTilesRenderOnStart) {
  //    if (!_allFirstLevelTilesAreTextureSolved) {
  //      const int firstLevelTilesCount = _firstLevelTiles.size();
  //      for (int i = 0; i < firstLevelTilesCount; i++) {
  //        Tile* tile = _firstLevelTiles[i];
  //        if (!tile->isTextureSolved()) {
  //          return RenderState::busy();
  //        }
  //      }
  //
  //      if (_tessellator != NULL) {
  //        if (!_tessellator->isReady(rc)) {
  //          return RenderState::busy();
  //        }
  //      }
  //
  //      if (_texturizer != NULL) {
  //        const RenderState texturizerRenderState = _texturizer->getRenderState(_layerSet);
  //        if (texturizerRenderState._type != RENDER_READY) {
  //          return texturizerRenderState;
  //        }
  //      }
  //
  //      _allFirstLevelTilesAreTextureSolved = true;
  //    }
  //=======
  
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        Tile tile = _firstLevelTiles.get(i);
        _texturizer.justCreatedTopTile(rc, tile, _layerSet);
      }
    }
  
    if (_tilesRenderParameters._forceFirstLevelTilesRenderOnStart && !_allFirstLevelTilesAreTextureSolved)
    {
      final int firstLevelTilesCount = _firstLevelTiles.size();
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        Tile tile = _firstLevelTiles.get(i);
        if (!tile.isTextureSolved())
        {
          return RenderState.busy();
        }
      }
  
      _allFirstLevelTilesAreTextureSolved = true;
  //>>>>>>> purgatory
    }
  
    return RenderState.ready();
  }

  public final void acceptTileVisitor(ITileVisitor tileVisitor, Sector sector, int topLevel, int maxLevel)
  {
    _tileVisitor = tileVisitor;
    visitTilesTouchesWith(sector, topLevel, maxLevel);
  }

  public final void start(G3MRenderContext rc)
  {
    _firstRender = true;
  }

  public final void stop(G3MRenderContext rc)
  {
    _firstRender = false;
  }

  public final void onPause(G3MContext context)
  {
    recreateTiles();
  }

  public final void setEnable(boolean enable)
  {
    super.setEnable(enable);

    if (!enable)
    {
      pruneFirstLevelTiles();
    }
  }

  public final void changed()
  {
    if (!_recreateTilesPending)
    {
      _recreateTilesPending = true;
      // recreateTiles() delete tiles, then meshes, and delete textures from the GPU
      //   so it has to be executed in the OpenGL thread
  
      if (_tileCache != null)
      {
        _tileCache.cropTileCache(0);
      }
  
      if (_context == null)
      {
        ILogger.instance().logError("_context is not initialized");
      }
      else
      {
        _context.getThreadUtils().invokeInRendererThread(new RecreateTilesTask(this), true);
      }
    }
  }

  public final void recreateTiles()
  {
    pruneFirstLevelTiles();
    clearFirstLevelTiles();
  
    _layerTilesRenderParameters = null;
    _layerTilesRenderParameters = null;
    _layerTilesRenderParametersDirty = true;
  
    _firstRender = true;
    _allFirstLevelTilesAreTextureSolved = false;
    createFirstLevelTiles(_context);
  
    _recreateTilesPending = false;
  }

  /**
   Answer the visible-sector, it can be null if globe was not yet rendered.
   */
  public final Sector getVisibleSector()
  {
    return _lastVisibleSector;
  }

  /**
   Add a listener for notification of visible-sector changes.

   @param stabilizationInterval How many time the visible-sector has to be settled (without changes) before triggering the event.  Useful for avoid process while the camera is being moved (as in animations).  If stabilizationInterval is zero, the event is triggered immediately.
   */
  public final void addVisibleSectorListener(VisibleSectorListener listener, TimeInterval stabilizationInterval)
  {
    _visibleSectorListeners.add(new VisibleSectorListenerEntry(listener, stabilizationInterval));
  }

  /**
   Add a listener for notification of visible-sector changes.

   The event is triggered immediately without waiting for the visible-sector get settled.
   */
  public final void addVisibleSectorListener(VisibleSectorListener listener)
  {
    addVisibleSectorListener(listener, TimeInterval.zero());
  }

  /**
   * Set the download-priority used by Tiles (for downloading textures).
   *
   * @param tileDownloadPriority: new value for download priority of textures
   */
  public final void setTileDownloadPriority(long tileDownloadPriority)
  {
    _tileDownloadPriority = tileDownloadPriority;
  }

  /**
   * Return the current value for the download priority of textures
   *
   * @return _tileDownloadPriority: long
   */
  public final long getTileDownloadPriority()
  {
    return _tileDownloadPriority;
  }

  /**
   * @see Renderer#isPlanetRenderer()
   */
  public final boolean isPlanetRenderer()
  {
    return true;
  }

  public final SurfaceElevationProvider getSurfaceElevationProvider()
  {
    return (_elevationDataProvider == null) ? null : this;
  }

  public final PlanetRenderer getPlanetRenderer()
  {
    return this;
  }

  public final void addListener(Angle latitude, Angle longitude, SurfaceElevationListener listener)
  {
    _elevationListenersTree.add(new Geodetic2D(latitude, longitude), listener);
  }

  public final void addListener(Geodetic2D position, SurfaceElevationListener listener)
  {
    _elevationListenersTree.add(position, listener);
  }

  public final boolean removeListener(SurfaceElevationListener listener)
  {
    return _elevationListenersTree.remove(listener);
  }

  public final void sectorElevationChanged(ElevationData elevationData)
  {
    if (elevationData != null)
    {
      _elevationListenersTree.notifyListeners(elevationData, _verticalExaggeration);
    }
  }

  public final Sector getRenderedSector()
  {
    return _renderedSector;
  }

  public final boolean setRenderedSector(Sector sector)
  {
    if ((_renderedSector != null && !_renderedSector.isEquals(sector)) || (_renderedSector == null && !sector.isEquals(Sector.fullSphere())))
    {
      if (_renderedSector != null)
         _renderedSector.dispose();
  
      if (sector.isEquals(Sector.fullSphere()))
      {
        _renderedSector = null;
      }
      else
      {
        _renderedSector = new Sector(sector);
      }
  
      _tessellator.setRenderedSector(sector);
  
      changed();
  
      return true;
    }
    return false;
  }

  public final void addTerrainTouchListener(TerrainTouchListener listener)
  {
    if (listener != null)
    {
      _terrainTouchListeners.add(listener);
    }
  }

  public final java.util.LinkedList<String> getTilesURL(Geodetic2D lower, Geodetic2D upper, int maxLOD)
  {
  
    for (int i = 0; i < 20; i++)
    {
      GlobalMembersPlanetRenderer.TILES_VISITED[i] = 0;
    }
  
    Sector sector = new Sector(lower, upper);
    final LayerTilesRenderParameters parameters = getLayerTilesRenderParameters();
    GetTilesURLVisitor visitor = new GetTilesURLVisitor(_renderContext, _layerTilesRenderParameters);
  
    acceptTileVisitor(visitor, sector, parameters._firstLevel, maxLOD);
  
    java.util.LinkedList<String> urls = visitor._urls;
  
    for (int i = 0; i < 20; i++)
    {
      ILogger.instance().logInfo("TILES_VISITED LOD:%d -> %d\n", i, GlobalMembersPlanetRenderer.TILES_VISITED[i]);
      //printf("TILES_VISITED LOD:%d -> %d\n", i, TILES_VISITED[i]);
    }
  
    if (visitor != null)
       visitor.dispose();
    return urls;
  }

  public final void zRender(G3MRenderContext rc, GLState glState)
  {
  
    final LayerTilesRenderParameters layerTilesRenderParameters = getLayerTilesRenderParameters();
    if (layerTilesRenderParameters == null)
    {
      return;
    }
  
    GLState zRenderGLState = new GLState();
    zRenderGLState.addGLFeature(new ModelViewGLFeature(rc.getCurrentCamera()), false);
    zRenderGLState.setParent(glState);
  
    java.util.LinkedList<Tile> renderedTiles = getRenderedTilesList(rc);
  
    for (java.util.Iterator<Tile> iter = renderedTiles.iterator(); iter.hasNext();)
    {
      Tile tile = iter.next();
  
      tile.zRender(rc, zRenderGLState);
    }
  
  
  
    zRenderGLState._release();
  }

  public final void setElevationDataProvider(ElevationDataProvider elevationDataProvider, boolean owned)
  {
    if (_elevationDataProvider != elevationDataProvider)
    {
      if (_ownsElevationDataProvider)
      {
        if (_elevationDataProvider != null)
           _elevationDataProvider.dispose();
      }
  
      _ownsElevationDataProvider = owned;
      _elevationDataProvider = elevationDataProvider;
  
      if (_elevationDataProvider != null)
      {
        _elevationDataProvider.setChangedListener(this);
        if (_context != null)
        {
          _elevationDataProvider.initialize(_context); //Initializing EDP in case it wasn't
        }
      }
  
      changed();
    }
  }

  public final void setVerticalExaggeration(float verticalExaggeration)
  {
    if (_verticalExaggeration != verticalExaggeration)
    {
      _verticalExaggeration = verticalExaggeration;
      changed();
    }
  }

  public final ElevationDataProvider getElevationDataProvider()
  {
    return _elevationDataProvider;
  }

  public final void setRenderTileMeshes(boolean renderTileMeshes)
  {
    _renderTileMeshes = renderTileMeshes;
  }

  public final boolean getRenderTileMeshes()
  {
    return _renderTileMeshes;
  }

<<<<<<< HEAD
  public final java.util.LinkedList<URL> getResourcesURL(Sector sector, int minLOD, int maxLOD)
=======
  public final void changedInfo(java.util.ArrayList<Info> info)
>>>>>>> zrender-touchhandlers
  {
     return getResourcesURL(sector, minLOD, maxLOD, null);
  }
  public final java.util.LinkedList<URL> getResourcesURL(Sector sector, int minLOD, int maxLOD, java.util.LinkedList<Geodetic2D> route)
  {
  
    for (int i = 0; i < 20; i++)
    {
      GlobalMembersPlanetRenderer.TILES_VISITED[i] = 0;
    }
  
    java.util.LinkedList<URL> urls = new java.util.LinkedList<URL>();
  
    java.util.LinkedList<Tile> _tiles = new java.util.LinkedList<Tile>(); //List of tiles to check
    final int ftSize = _firstLevelTiles.size();
    for (int i = 0; i < ftSize; i++)
    {
      if (_firstLevelTiles.get(i)._sector.touchesWith(sector))
      {
        _tiles.addLast(_firstLevelTiles.get(i));
      }
    }
  
    while (!_tiles.isEmpty())
    {
      Tile tile = _tiles.getFirst();
      _tiles.removeFirst();
  
  
      if (tile._sector.touchesWith(sector))
      {
  
        //Checking Route if any
        if (route != null)
        {
          if (!sectorCloseToRoute(tile._sector, route, tile._sector.getDeltaRadiusInRadians() * 4.0))
          {
            continue;
          }
        }
  
        if (tile._level >= minLOD)
        {
          GlobalMembersPlanetRenderer.TILES_VISITED[tile._level]++;
  
          addLayerSetURLForSector(urls, tile);
        }
  
        if (tile._level < maxLOD)
        {
          //std::vector<Tile*>* newTiles = tile->getSubTiles(_layerTilesRenderParameters->_mercator);
          java.util.ArrayList<Tile> newTiles = tile.getSubTiles();
          for (int i = 0; i < newTiles.size(); i++)
          {
            _tiles.addLast(newTiles.get(i));
          }
        }
  
      }
    }
  
    for (int i = 0; i < 20; i++)
    {
      ILogger.instance().logInfo("TILES_VISITED LOD:%d -> %d\n", i, GlobalMembersPlanetRenderer.TILES_VISITED[i]);
      //printf("TILES_VISITED LOD:%d -> %d\n", i, TILES_VISITED[i]);
    }
  
    return urls;
  }

  public final int getNumberOfRenderedTiles()
  {
    return _renderedTiles.size();
  }

  public final float getVerticalExaggeration()
  {
    return _verticalExaggeration;
  }

  public final void setChangedRendererInfoListener(ChangedRendererInfoListener changedInfoListener, int rendererIdentifier)
  {
    if (_changedInfoListener != null)
    {
      ILogger.instance().logWarning("Changed Renderer Info Listener of PlanetRenderer already set");
    }
  
    _rendererIdentifier = rendererIdentifier;
    _changedInfoListener = changedInfoListener;
  
    if(_changedInfoListener != null)
    {
      _changedInfoListener.changedRendererInfo(rendererIdentifier, _layerSet.getInfo());
    }
  }

  public final LayerTilesRenderParameters getLayerTilesRenderParameters()
  {
    if (_layerTilesRenderParametersDirty)
    {
      _errors.clear();
      _layerTilesRenderParameters = null;
      _layerTilesRenderParameters = _layerSet.createLayerTilesRenderParameters(_tilesRenderParameters._forceFirstLevelTilesRenderOnStart, _errors);
      if (_layerTilesRenderParameters == null)
      {
        ILogger.instance().logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't render planet");
      }
      _layerTilesRenderParametersDirty = false;
    }
    return _layerTilesRenderParameters;
  }


  public final void changedInfo(java.util.ArrayList<String> info)
  {
    if (_changedInfoListener != null)
    {
      _changedInfoListener.changedRendererInfo(_rendererIdentifier, info);
    }
  }

  public final TileTessellator getTileTessellator()
  {
    return _tessellator;
  }

  public final void setFrustumCullingFactor(float frustumCullingFactor)
  {
    _frustumCullingFactor = frustumCullingFactor;
  }



//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning ÑAPA BANDAMA
  public static java.util.ArrayList<LODAugmentedSector> _lODAugmentedSectors = new java.util.ArrayList<LODAugmentedSector>();

  public final void addLODAugmentedForSector(Sector sector, double factor)
  {
    _lODAugmentedSectors.add(new LODAugmentedSector(sector, factor));
  }

  public final void setElevationDataProviderReadyListener(ElevationDataProviderReadyListener srl, boolean autodelete)
  {
  
    if (_elevationDataProviderReadyListener != null && _elevationDataProviderReadyListenerAutoDelete)
    {
      if (_elevationDataProviderReadyListener != null)
         _elevationDataProviderReadyListener.dispose();
    }
  
    _elevationDataProviderReadyListener = srl;
    _elevationDataProviderReadyListenerAutoDelete = autodelete;
  }

}