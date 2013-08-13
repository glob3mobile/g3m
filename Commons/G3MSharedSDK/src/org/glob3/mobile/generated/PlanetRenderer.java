package org.glob3.mobile.generated; 
public class PlanetRenderer extends LeafRenderer implements ChangedListener, SurfaceElevationProvider
{
  private final TileTessellator _tessellator;
  private ElevationDataProvider _elevationDataProvider;
  private TileTexturizer _texturizer;
  private TileRasterizer _tileRasterizer;
  private LayerSet _layerSet;
  private final TilesRenderParameters _parameters;
  private final boolean _showStatistics;

  private Camera     _lastCamera;
  private G3MContext _context;

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
  
      tile.toBeDeleted(_texturizer, _elevationDataProvider);
  
      if (tile != null)
         tile.dispose();
    }
  
    _firstLevelTiles.clear();
  }
  private void createFirstLevelTiles(G3MContext context)
  {
  
    final LayerTilesRenderParameters parameters = _layerSet.getLayerTilesRenderParameters();
    if (parameters == null)
    {
      ILogger.instance().logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create first-level tiles");
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
  
        Tile tile = new Tile(_texturizer, null, sector, 0, row, col, this);
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
  
    if (parameters._firstLevel > 0)
    {
      final int topLevelTilesSize = topLevelTiles.size();
      for (int i = 0; i < topLevelTilesSize; i++)
      {
        Tile tile = topLevelTiles.get(i);
        createFirstLevelTiles(_firstLevelTiles, tile, parameters._firstLevel, parameters._mercator);
      }
    }
  
    sortTiles(_firstLevelTiles);
  
    context.getLogger().logInfo("Created %d first level tiles", _firstLevelTiles.size());
  
    _firstLevelTilesJustCreated = true;
  }
  private void createFirstLevelTiles(java.util.ArrayList<Tile> firstLevelTiles, Tile tile, int firstLevel, boolean mercator)
  {
    if (tile.getLevel() == firstLevel)
    {
      firstLevelTiles.add(tile);
    }
    else
    {
      final Sector sector = tile.getSector();
      final Geodetic2D lower = sector._lower;
      final Geodetic2D upper = sector._upper;
  
      final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
  
      final Angle splitLatitude = mercator ? MercatorUtils.calculateSplitLatitude(lower._latitude, upper._latitude) : Angle.midAngle(lower._latitude, upper._latitude);
      /*                               */
      /*                               */
  
  
      java.util.ArrayList<Tile> children = tile.createSubTiles(splitLatitude, splitLongitude, false);
  
      final int childrenSize = children.size();
      for (int i = 0; i < childrenSize; i++)
      {
        Tile child = children.get(i);
        createFirstLevelTiles(firstLevelTiles, child, firstLevel, mercator);
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
                                   final int rowI = i.getRow();
                                   final int rowJ = j.getRow();
                                   if (rowI < rowJ) {
                                     return -1;
                                   }
                                   if (rowI > rowJ) {
                                     return 1;
                                   }
  
                                   final int columnI = i.getColumn();
                                   final int columnJ = j.getColumn();
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
      tile.prune(_texturizer, _elevationDataProvider);
    }
  }

  private Sector _lastVisibleSector;

  private java.util.ArrayList<VisibleSectorListenerEntry> _visibleSectorListeners = new java.util.ArrayList<VisibleSectorListenerEntry>();

  private long _texturePriority;

  private float _verticalExaggeration;


  private boolean isReadyToRenderTiles(G3MRenderContext rc)
  {
    if (!_layerSet.isReady())
    {
      return false;
    }
  
    if (_elevationDataProvider != null)
    {
      if (!_elevationDataProvider.isReadyToRender(rc))
      {
        return false;
      }
    }
  
    if (_firstLevelTilesJustCreated)
    {
      _firstLevelTilesJustCreated = false;
  
      final int firstLevelTilesCount = _firstLevelTiles.size();
  
      if (_parameters._forceFirstLevelTilesRenderOnStart)
      {
        TilesStatistics statistics = new TilesStatistics();
  
        PlanetRendererContext prc = new PlanetRendererContext(_tessellator, _elevationDataProvider, _texturizer, _tileRasterizer, _layerSet, _parameters, statistics, _lastSplitTimer, true, _texturePriority, _verticalExaggeration);
  
        for (int i = 0; i < firstLevelTilesCount; i++)
        {
          Tile tile = _firstLevelTiles.get(i);
          tile.prepareForFullRendering(rc, prc);
        }
      }
  
      if (_texturizer != null)
      {
        for (int i = 0; i < firstLevelTilesCount; i++)
        {
          Tile tile = _firstLevelTiles.get(i);
          _texturizer.justCreatedTopTile(rc, tile, _layerSet);
        }
      }
    }
  
    if (_parameters._forceFirstLevelTilesRenderOnStart)
    {
      if (!_allFirstLevelTilesAreTextureSolved)
      {
        final int firstLevelTilesCount = _firstLevelTiles.size();
        for (int i = 0; i < firstLevelTilesCount; i++)
        {
          Tile tile = _firstLevelTiles.get(i);
          if (!tile.isTextureSolved())
          {
            return false;
          }
        }
  
        if (_tessellator != null)
        {
          if (!_tessellator.isReady(rc))
          {
            return false;
          }
        }
  
        if (_texturizer != null)
        {
          if (!_texturizer.isReady(rc, _layerSet))
          {
            return false;
          }
        }
  
        _allFirstLevelTilesAreTextureSolved = true;
      }
    }
  
    return true;
  }

  private boolean _recreateTilesPending;

  private GLState _glState = new GLState();
  private ProjectionGLFeature _projection;
  private ModelGLFeature _model;
  private void updateGLState(G3MRenderContext rc)
  {
  
    final Camera cam = rc.getCurrentCamera();
    if (_projection == null)
    {
      _projection = new ProjectionGLFeature(cam.getProjectionMatrix44D());
      _glState.addGLFeature(_projection, true);
    }
    else
    {
      _projection.setMatrix(cam.getProjectionMatrix44D());
    }
  
    if (_model == null)
    {
      _model = new ModelGLFeature(cam.getModelMatrix44D());
      _glState.addGLFeature(_model, true);
    }
    else
    {
      _model.setMatrix(cam.getModelMatrix44D());
    }
  }

  private SurfaceElevationProvider_Tree _elevationListenersTree = new SurfaceElevationProvider_Tree();

  public PlanetRenderer(TileTessellator tessellator, ElevationDataProvider elevationDataProvider, float verticalExaggeration, TileTexturizer texturizer, TileRasterizer tileRasterizer, LayerSet layerSet, TilesRenderParameters parameters, boolean showStatistics, long texturePriority)
  {
     _tessellator = tessellator;
     _elevationDataProvider = elevationDataProvider;
     _verticalExaggeration = verticalExaggeration;
     _texturizer = texturizer;
     _tileRasterizer = tileRasterizer;
     _layerSet = layerSet;
     _parameters = parameters;
     _showStatistics = showStatistics;
     _firstLevelTilesJustCreated = false;
     _lastSplitTimer = null;
     _lastCamera = null;
     _firstRender = false;
     _context = null;
     _lastVisibleSector = null;
     _texturePriority = texturePriority;
     _allFirstLevelTilesAreTextureSolved = false;
     _recreateTilesPending = false;
     _projection = null;
     _model = null;
    _layerSet.setChangeListener(this);
    if (_tileRasterizer != null)
    {
      _tileRasterizer.setChangeListener(this);
    }
  }

  public void dispose()
  {
    clearFirstLevelTiles();
  
    if (_tessellator != null)
       _tessellator.dispose();
    if (_elevationDataProvider != null)
       _elevationDataProvider.dispose();
    if (_texturizer != null)
       _texturizer.dispose();
    if (_parameters != null)
       _parameters.dispose();
  
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
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
  
    clearFirstLevelTiles();
    createFirstLevelTiles(context);
  
    if (_lastSplitTimer != null)
       _lastSplitTimer.dispose();
    _lastSplitTimer = context.getFactory().createTimer();
  
    _layerSet.initialize(context);
    _texturizer.initialize(context, _parameters);
    if (_elevationDataProvider != null)
    {
      _elevationDataProvider.initialize(context);
    }
  }

  public final void render(G3MRenderContext rc)
  {
  
    updateGLState(rc);
  
    //  if (_recreateTilesPending) {
    //    recreateTiles();
    //    _recreateTilesPending = false;
    //  }
  
    // Saving camera for use in onTouchEvent
    _lastCamera = rc.getCurrentCamera();
  
    TilesStatistics statistics = new TilesStatistics();
  
    PlanetRendererContext prc = new PlanetRendererContext(_tessellator, _elevationDataProvider, _texturizer, _tileRasterizer, _layerSet, _parameters, statistics, _lastSplitTimer, _firstRender, _texturePriority, _verticalExaggeration); // if first render, force full render
  
    final int firstLevelTilesCount = _firstLevelTiles.size();
  
    final Planet planet = rc.getPlanet();
    final Vector3D cameraNormalizedPosition = _lastCamera.getNormalizedPosition();
    double cameraAngle2HorizonInRadians = _lastCamera.getAngle2HorizonInRadians();
    final Frustum cameraFrustumInModelCoordinates = _lastCamera.getFrustumInModelCoordinates();
  
    if (_firstRender && _parameters._forceFirstLevelTilesRenderOnStart)
    {
      // force one render pass of the firstLevelTiles tiles to make the (toplevel) textures
      // loaded as they will be used as last-chance fallback texture for any tile.
      _firstRender = false;
  
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        Tile tile = _firstLevelTiles.get(i);
        tile.render(rc, prc, _glState, null, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates);
      }
    }
    else
    {
      java.util.LinkedList<Tile> toVisit = new java.util.LinkedList<Tile>();
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        toVisit.addLast(_firstLevelTiles.get(i));
      }
  
      while (toVisit.size() > 0)
      {
        java.util.LinkedList<Tile> toVisitInNextIteration = new java.util.LinkedList<Tile>();
  
        for (java.util.Iterator<Tile> iter = toVisit.iterator(); iter.hasNext();)
        {
          Tile tile = iter.next();
  
          tile.render(rc, prc, _glState, toVisitInNextIteration, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates);
        }
  
        toVisit = toVisitInNextIteration;
      }
    }
  
    if (_showStatistics)
    {
      statistics.log(rc.getLogger());
    }
  
  
    final Sector renderedSector = statistics.getRenderedSector();
    if (renderedSector != null)
    {
      if ((_lastVisibleSector == null) || !renderedSector.isEqualsTo(_lastVisibleSector))
      {
        if (_lastVisibleSector != null)
           _lastVisibleSector.dispose();
        _lastVisibleSector = new Sector(renderedSector);
      }
    }
  
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
  
    if (touchEvent.getType() == TouchEventType.LongPress)
    {
      final Vector2I pixel = touchEvent.getTouch(0).getPos();
      final Vector3D ray = _lastCamera.pixel2Ray(pixel);
      final Vector3D origin = _lastCamera.getCartesianPosition();
  
      final Planet planet = ec.getPlanet();
  
      final Vector3D positionCartesian = planet.closestIntersection(origin, ray);
      if (positionCartesian.isNan())
      {
        return false;
      }
  
      final Geodetic3D position = planet.toGeodetic3D(positionCartesian);
  
      final int firstLevelTilesCount = _firstLevelTiles.size();
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        final Tile tile = _firstLevelTiles.get(i).getDeepestTileContaining(position);
        if (tile != null)
        {
          ILogger.instance().logInfo("Touched on %s", tile.description());
          if (_texturizer.onTerrainTouchEvent(ec, position, tile, _layerSet))
          {
            return true;
          }
        }
      }
  
    }
  
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return isReadyToRenderTiles(rc);
  }


  public final void start(G3MRenderContext rc)
  {
    _firstRender = true;
  }

  public final void stop(G3MRenderContext rc)
  {
    _firstRender = false;
  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {
    recreateTiles();
  }

  public final void onDestroy(G3MContext context)
  {

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
      _context.getThreadUtils().invokeInRendererThread(new RecreateTilesTask(this), true);
    }
  }

  public final void recreateTiles()
  {
    pruneFirstLevelTiles();
    clearFirstLevelTiles();
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

   @param stabilizationInterval How many time the visible-sector has to be settled (without changes) before triggering the event.  Useful for avoid process while the camera is being moved (as in animations).  If stabilizationInterval is zero, the event is triggered inmediatly.
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
   * @param texturePriority: new value for download priority of textures
   */
  public final void setTexturePriority(long texturePriority)
  {
    _texturePriority = texturePriority;
  }

  /**
   * Return the current value for the download priority of textures
   *
   * @return _texturePriority: long
   */
  public final long getTexturePriority()
  {
    return _texturePriority;
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

  public final void addListener(Angle latitude, Angle longitude, SurfaceElevationListener observer)
  {
    _elevationListenersTree.add(new Geodetic2D(latitude, longitude), observer);
  }

  public final void addListener(Geodetic2D position, SurfaceElevationListener observer)
  {
    _elevationListenersTree.add(position, observer);
  }

  public final void removeListener(SurfaceElevationListener observer)
  {
    _elevationListenersTree.remove(observer);
  }

  public final void sectorElevationChanged(Sector sector, ElevationData elevationData)
  {
    _elevationListenersTree.notifyObservers(sector, elevationData, _verticalExaggeration);
  }

}