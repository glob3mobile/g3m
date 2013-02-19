

package org.glob3.mobile.generated;

public class TileRenderer
    extends
      LeafRenderer
    implements
      LayerSetChangedListener {
  private final TileTessellator _tessellator;
  private final ElevationDataProvider _elevationDataProvider;
  private final TileTexturizer _texturizer;
  private final LayerSet _layerSet;
  private final TilesRenderParameters _parameters;
  private final boolean _showStatistics;
  private boolean _topTilesJustCreated;
  private ITileVisitor _tileVisitor = null;

  private Camera _lastCamera;
  private G3MContext _context;

  private final java.util.ArrayList<Tile> _topLevelTiles = new java.util.ArrayList<Tile>();

  private ITimer _lastSplitTimer; // timer to start every time a tile get
                                  // splitted into subtiles


  private void clearTopLevelTiles() {
    for (int i = 0; i < _topLevelTiles.size(); i++) {
      final Tile tile = _topLevelTiles.get(i);
      if (tile != null) {
        tile.dispose();
      }
    }

    _topLevelTiles.clear();
  }


  private void createTopLevelTiles(final G3MContext context) {
    final Angle fromLatitude = _parameters._topSector.lower().latitude();
    final Angle fromLongitude = _parameters._topSector.lower().longitude();

    final Angle deltaLan = _parameters._topSector.getDeltaLatitude();
    final Angle deltaLon = _parameters._topSector.getDeltaLongitude();

    final Angle tileHeight = deltaLan.div(_parameters._splitsByLatitude);
    final Angle tileWidth = deltaLon.div(_parameters._splitsByLongitude);

    for (int row = 0; row < _parameters._splitsByLatitude; row++) {
      final Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
      final Angle tileLatTo = tileLatFrom.add(tileHeight);

      for (int col = 0; col < _parameters._splitsByLongitude; col++) {
        final Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
        final Angle tileLonTo = tileLonFrom.add(tileWidth);

        final Geodetic2D tileLower = new Geodetic2D(tileLatFrom, tileLonFrom);
        final Geodetic2D tileUpper = new Geodetic2D(tileLatTo, tileLonTo);
        final Sector sector = new Sector(tileLower, tileUpper);

        final Tile tile = new Tile(_texturizer, null, sector,
            _parameters._topLevel, row, col);
        _topLevelTiles.add(tile);
      }
    }

    context.getLogger().logInfo("Created %d top level tiles",
        _topLevelTiles.size());

    _topTilesJustCreated = true;
  }

  private boolean _firstRender;


  private void pruneTopLevelTiles() {
    for (int i = 0; i < _topLevelTiles.size(); i++) {
      final Tile tile = _topLevelTiles.get(i);
      tile.prune(_texturizer, _elevationDataProvider);
    }
  }

  private Sector _lastVisibleSector;

  private final java.util.ArrayList<VisibleSectorListenerEntry> _visibleSectorListeners = new java.util.ArrayList<VisibleSectorListenerEntry>();


  private void visitSubTilesTouchesWith(final Tile tile,
                                        final Sector sectorToVisit,
                                        final int topLevel,
                                        final int maxLevel) {
    if (tile.getLevel() < maxLevel) {
      final int subTilesCount = tile.getSubTiles().size();
      for (int i = 0; i < subTilesCount; i++) {
        final Tile tl = tile.getSubTiles().get(i);
        if (tl.getSector().touchesWith(sectorToVisit)) {
          if ((tile.getLevel() >= topLevel)) {
            _tileVisitor.visitTile(tl);
          }
          visitSubTilesTouchesWith(tl, sectorToVisit, topLevel, maxLevel);
        }
      }
    }
  }

  private final float _verticalExaggeration;


  public TileRenderer(final TileTessellator tessellator,
                      final ElevationDataProvider elevationDataProvider,
                      final TileTexturizer texturizer,
                      final LayerSet layerSet,
                      final TilesRenderParameters parameters,
                      final boolean showStatistics) {
    _tessellator = tessellator;
    _elevationDataProvider = elevationDataProvider;
    _texturizer = texturizer;
    _layerSet = layerSet;
    _parameters = parameters;
    _showStatistics = showStatistics;
    _topTilesJustCreated = false;
    _lastSplitTimer = null;
    _lastCamera = null;
    _firstRender = false;
    _context = null;
    _lastVisibleSector = null;
    _layerSet.setChangeListener(this);

    _verticalExaggeration = 20F;
  }


  @Override
  public void dispose() {
    clearTopLevelTiles();

    if (_tessellator != null) {
      _tessellator.dispose();
    }
    if (_elevationDataProvider != null) {
      _elevationDataProvider.dispose();
    }
    if (_texturizer != null) {
      _texturizer.dispose();
    }
    if (_parameters != null) {
      _parameters.dispose();
    }

    if (_lastSplitTimer != null) {
      _lastSplitTimer.dispose();
    }

    if (_lastVisibleSector != null) {
      _lastVisibleSector.dispose();
    }

    final int visibleSectorListenersCount = _visibleSectorListeners.size();
    for (int i = 0; i < visibleSectorListenersCount; i++) {
      final VisibleSectorListenerEntry entry = _visibleSectorListeners.get(i);
      if (entry != null) {
        entry.dispose();
      }
    }
  }


  @Override
  public final void initialize(final G3MContext context) {
    _context = context;

    clearTopLevelTiles();
    createTopLevelTiles(context);

    if (_lastSplitTimer != null) {
      _lastSplitTimer.dispose();
    }
    _lastSplitTimer = context.getFactory().createTimer();

    _layerSet.initialize(context);
    _texturizer.initialize(context, _parameters);
    if (_elevationDataProvider != null) {
      _elevationDataProvider.initialize(context);
    }
  }


  @Override
  public final void render(final G3MRenderContext rc,
                           final GLState parentState) {
    // Saving camera for use in onTouchEvent
    _lastCamera = rc.getCurrentCamera();

    final TilesStatistics statistics = new TilesStatistics();

    final TileRenderContext trc = new TileRenderContext(_tessellator,
        _elevationDataProvider, _texturizer, _layerSet, _parameters,
        statistics, _lastSplitTimer, _firstRender, _verticalExaggeration); // if
                                                                           // first
                                                                           // render,
                                                                           // force
                                                                           // full
                                                                           // render

    final int topLevelTilesCount = _topLevelTiles.size();

    if (_firstRender && _parameters._forceTopLevelTilesRenderOnStart) {
      // force one render pass of the topLevel tiles to make the (toplevel)
      // textures loaded
      // as they will be used as last-chance fallback texture for any tile.
      _firstRender = false;

      for (int i = 0; i < topLevelTilesCount; i++) {
        final Tile tile = _topLevelTiles.get(i);
        tile.render(rc, trc, parentState, null);
      }
    }
    else {
      java.util.LinkedList<Tile> toVisit = new java.util.LinkedList<Tile>();
      for (int i = 0; i < topLevelTilesCount; i++) {
        toVisit.addLast(_topLevelTiles.get(i));
      }

      while (toVisit.size() > 0) {
        final java.util.LinkedList<Tile> toVisitInNextIteration = new java.util.LinkedList<Tile>();

        for (final Tile tile : toVisit) {
          tile.render(rc, trc, parentState, toVisitInNextIteration);
        }

        toVisit = toVisitInNextIteration;
      }
    }

    if (_showStatistics) {
      statistics.log(rc.getLogger());
    }


    final Sector renderedSector = statistics.getRenderedSector();
    if (renderedSector != null) {
      if ((_lastVisibleSector == null)
          || !renderedSector.isEqualsTo(_lastVisibleSector)) {
        if (_lastVisibleSector != null) {
          _lastVisibleSector.dispose();
        }
        _lastVisibleSector = new Sector(renderedSector);
      }
    }

    if (_lastVisibleSector != null) {
      final int visibleSectorListenersCount = _visibleSectorListeners.size();
      for (int i = 0; i < visibleSectorListenersCount; i++) {
        final VisibleSectorListenerEntry entry = _visibleSectorListeners.get(i);

        entry.tryToNotifyListener(_lastVisibleSector, rc);
      }
    }

  }


  @Override
  public final boolean onTouchEvent(final G3MEventContext ec,
                                    final TouchEvent touchEvent) {
    boolean handled = false;

    if (touchEvent.getType() == TouchEventType.LongPress) {

      if (_lastCamera != null) {
        final Vector2I pixel = touchEvent.getTouch(0).getPos();
        final Vector3D ray = _lastCamera.pixel2Ray(pixel);
        final Vector3D origin = _lastCamera.getCartesianPosition();

        final Planet planet = ec.getPlanet();

        final Vector3D positionCartesian = planet.closestIntersection(origin,
            ray);
        if (positionCartesian.isNan()) {
          return false;
        }

        final Geodetic3D position = planet.toGeodetic3D(positionCartesian);

        for (int i = 0; i < _topLevelTiles.size(); i++) {
          final Tile tile = _topLevelTiles.get(i).getDeepestTileContaining(
              position);
          if (tile != null) {
            _texturizer.onTerrainTouchEvent(ec, position, tile, _layerSet);
            handled = true;
          }
        }
      }

    }

    return handled;
  }


  @Override
  public final void onResizeViewportEvent(final G3MEventContext ec,
                                          final int width,
                                          final int height) {

  }


  @Override
  public final boolean isReadyToRender(final G3MRenderContext rc) {
    if (_topTilesJustCreated) {
      _topTilesJustCreated = false;

      final int topLevelTilesCount = _topLevelTiles.size();

      if (_parameters._forceTopLevelTilesRenderOnStart) {
        final TilesStatistics statistics = new TilesStatistics();

        final TileRenderContext trc = new TileRenderContext(_tessellator,
            _elevationDataProvider, _texturizer, _layerSet, _parameters,
            statistics, _lastSplitTimer, true, _verticalExaggeration);

        for (int i = 0; i < topLevelTilesCount; i++) {
          final Tile tile = _topLevelTiles.get(i);
          tile.prepareForFullRendering(rc, trc);
        }
      }

      if (_texturizer != null) {
        for (int i = 0; i < topLevelTilesCount; i++) {
          final Tile tile = _topLevelTiles.get(i);
          _texturizer.justCreatedTopTile(rc, tile, _layerSet);
        }
      }
    }

    if (_parameters._forceTopLevelTilesRenderOnStart) {
      final int topLevelTilesCount = _topLevelTiles.size();
      for (int i = 0; i < topLevelTilesCount; i++) {
        final Tile tile = _topLevelTiles.get(i);
        if (!tile.isTextureSolved()) {
          return false;
        }
      }

      if (_tessellator != null) {
        if (!_tessellator.isReady(rc)) {
          return false;
        }
      }

      if (_texturizer != null) {
        if (!_texturizer.isReady(rc, _layerSet)) {
          return false;
        }
      }
    }

    return true;
  }


  public final void acceptTileVisitor(final ITileVisitor tileVisitor) {
    _tileVisitor = tileVisitor;
  }


  public final void visitTilesTouchesWith(final Sector sector,
                                          final int topLevel,
                                          final int maxLevel) {
    if (_tileVisitor != null) {
      final int topLevelCache = (topLevel < _parameters._topLevel)
                                                                  ? _parameters._topLevel
                                                                  : topLevel;

      final int maxLevelCache = (maxLevel > _parameters._maxLevel)
                                                                  ? _parameters._maxLevel
                                                                  : maxLevel;
      // Get Tiles to Cache
      final int topLevelTilesCount = _topLevelTiles.size();
      for (int i = 0; i < topLevelTilesCount; i++) {
        final Tile tile = _topLevelTiles.get(i);
        if (tile.getSector().touchesWith(sector)) {
          _tileVisitor.visitTile(tile);
          visitSubTilesTouchesWith(tile, sector, topLevelCache, maxLevelCache);
        }
      }
    }
  }


  @Override
  public final void start() {
    _firstRender = true;
  }


  @Override
  public final void stop() {
    _firstRender = false;
  }


  @Override
  public final void onResume(final G3MContext context) {

  }


  @Override
  public final void onPause(final G3MContext context) {
    recreateTiles();
  }


  @Override
  public final void onDestroy(final G3MContext context) {

  }


  @Override
  public final void setEnable(final boolean enable) {
    super.setEnable(enable);

    if (!enable) {
      pruneTopLevelTiles();
    }
  }


  public final void changed(final LayerSet layerSet) {
    // recreateTiles();

    // recreateTiles() delete tiles, then meshes, and delete textures from
    // the GPU so it has to be executed in the OpenGL thread
    _context.getThreadUtils().invokeInRendererThread(
        new RecreateTilesTask(this), true);
  }


  public final void recreateTiles() {
    pruneTopLevelTiles();
    clearTopLevelTiles();
    _firstRender = true;
    createTopLevelTiles(_context);
  }


  /**
   * Answer the visible-sector, it can be null if globe was not yet
   * rendered.
   */
  public final Sector getVisibleSector() {
    return _lastVisibleSector;
  }


  /**
   * Add a listener for notification of visible-sector changes.
   * 
   * @param stabilizationInterval
   *          How many time the visible-sector has to be settled (without
   *          changes) before triggering the event. Useful for avoid process
   *          while the camera is being moved (as in animations). If
   *          stabilizationInterval is zero, the event is triggered
   *          inmediatly.
   */
  public final void addVisibleSectorListener(final VisibleSectorListener listener,
                                             final TimeInterval stabilizationInterval) {
    _visibleSectorListeners.add(new VisibleSectorListenerEntry(listener,
        stabilizationInterval));
  }


  /**
   * Add a listener for notification of visible-sector changes.
   * The event is triggered immediately without waiting for the
   * visible-sector get settled.
   */
  public final void addVisibleSectorListener(final VisibleSectorListener listener) {
    addVisibleSectorListener(listener, TimeInterval.zero());
  }

}
