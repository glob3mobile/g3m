

package org.glob3.mobile.generated;

public class PlanetRenderer
         extends
            LeafRenderer
         implements
            ChangedListener,
            SurfaceElevationProvider {
   private final TileTessellator           _tessellator;
   private final ElevationDataProvider     _elevationDataProvider;
   private final TileTexturizer            _texturizer;
   private final TileRasterizer            _tileRasterizer;
   private final LayerSet                  _layerSet;
   private final TilesRenderParameters     _parameters;
   private final boolean                   _showStatistics;
   private boolean                         _topTilesJustCreated;
   private ITileVisitor                    _tileVisitor     = null;

   private Camera                          _lastCamera;
   private G3MContext                      _context;

   private final java.util.ArrayList<Tile> _firstLevelTiles = new java.util.ArrayList<Tile>();
   private boolean                         _firstLevelTilesJustCreated;
   private boolean                         _allFirstLevelTilesAreTextureSolved;

   private ITimer                          _lastSplitTimer;                                   // timer to start every time a tile get splitted into subtiles


   private void clearFirstLevelTiles() {
      final int firstLevelTilesCount = _firstLevelTiles.size();
      for (int i = 0; i < firstLevelTilesCount; i++) {
         final Tile tile = _firstLevelTiles.get(i);

         tile.toBeDeleted(_texturizer, _elevationDataProvider);

         if (tile != null) {
            tile.dispose();
         }
      }

      _firstLevelTiles.clear();
   }


   private void createFirstLevelTiles(final G3MContext context) {

      final LayerTilesRenderParameters parameters = _layerSet.getLayerTilesRenderParameters();
      _validLayerTilesRenderParameters = (parameters != null);
      if (!_validLayerTilesRenderParameters) {
         ILogger.instance().logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create first-level tiles");
         return;
      }

      final java.util.ArrayList<Tile> topLevelTiles = new java.util.ArrayList<Tile>();

      final Angle fromLatitude = parameters._topSector._lower._latitude;
      final Angle fromLongitude = parameters._topSector._lower._longitude;

      final Angle deltaLan = parameters._topSector._deltaLatitude;
      final Angle deltaLon = parameters._topSector._deltaLongitude;

      final int topSectorSplitsByLatitude = parameters._topSectorSplitsByLatitude;
      final int topSectorSplitsByLongitude = parameters._topSectorSplitsByLongitude;

      final Angle tileHeight = deltaLan.div(topSectorSplitsByLatitude);
      final Angle tileWidth = deltaLon.div(topSectorSplitsByLongitude);

      for (int row = 0; row < topSectorSplitsByLatitude; row++) {
         final Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
         final Angle tileLatTo = tileLatFrom.add(tileHeight);

         for (int col = 0; col < topSectorSplitsByLongitude; col++) {
            final Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
            final Angle tileLonTo = tileLonFrom.add(tileWidth);

            final Geodetic2D tileLower = new Geodetic2D(tileLatFrom, tileLonFrom);
            final Geodetic2D tileUpper = new Geodetic2D(tileLatTo, tileLonTo);
            final Sector sector = new Sector(tileLower, tileUpper);

            final Tile tile = new Tile(_texturizer, null, sector, 0, row, col, this);
            if (parameters._firstLevel == 0) {
               _firstLevelTiles.add(tile);
            }
            else {
               topLevelTiles.add(tile);
            }
         }
      }

      if (parameters._firstLevel > 0) {
         final int topLevelTilesSize = topLevelTiles.size();
         for (int i = 0; i < topLevelTilesSize; i++) {
            final Tile tile = topLevelTiles.get(i);
            createFirstLevelTiles(_firstLevelTiles, tile, parameters._firstLevel, parameters._mercator);
         }
      }

      sortTiles(_firstLevelTiles);

      context.getLogger().logInfo("Created %d first level tiles", _firstLevelTiles.size());

      _firstLevelTilesJustCreated = true;
   }


   private void createFirstLevelTiles(final java.util.ArrayList<Tile> firstLevelTiles,
                                      final Tile tile,
                                      final int firstLevel,
                                      final boolean mercator) {
      if (tile.getLevel() == firstLevel) {
         firstLevelTiles.add(tile);
      }
      else {
         final Sector sector = tile.getSector();
         final Geodetic2D lower = sector._lower;
         final Geodetic2D upper = sector._upper;

         final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);

         final Angle splitLatitude = mercator ? MercatorUtils.calculateSplitLatitude(lower._latitude, upper._latitude)
                                             : Angle.midAngle(lower._latitude, upper._latitude);
         /*                               */
         /*                               */


         java.util.ArrayList<Tile> children = tile.createSubTiles(splitLatitude, splitLongitude, false);

         final int childrenSize = children.size();
         for (int i = 0; i < childrenSize; i++) {
            final Tile child = children.get(i);
            createFirstLevelTiles(firstLevelTiles, child, firstLevel, mercator);
         }

         children = null;
         if (tile != null) {
            tile.dispose();
         }
      }
   }


   private void sortTiles(final java.util.ArrayList<Tile> tiles) {
      java.util.Collections.sort(tiles, new java.util.Comparator<Tile>() {
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


   private void pruneFirstLevelTiles() {
      final int firstLevelTilesCount = _firstLevelTiles.size();
      for (int i = 0; i < firstLevelTilesCount; i++) {
         final Tile tile = _firstLevelTiles.get(i);
         tile.prune(_texturizer, _elevationDataProvider);
      }
   }

   private Sector                                                _lastVisibleSector;

   private final java.util.ArrayList<VisibleSectorListenerEntry> _visibleSectorListeners = new java.util.ArrayList<VisibleSectorListenerEntry>();


   private void visitSubTilesTouchesWith(final Tile tile,
                                         final Sector sectorToVisit,
                                         final int topLevel,
                                         final int maxLevel) {
      if (tile.getLevel() < maxLevel) {
         final java.util.ArrayList<Tile> subTiles = tile.getSubTiles(_layerSet.getLayerTilesRenderParameters()._mercator);


         final int subTilesCount = subTiles.size();
         for (int i = 0; i < subTilesCount; i++) {
            final Tile tl = subTiles.get(i);
            if (tl.getSector().touchesWith(sectorToVisit)) {
               if ((tile.getLevel() >= topLevel)) {
                  _tileVisitor.visitTile(tl);
               }
               visitSubTilesTouchesWith(tl, sectorToVisit, topLevel, maxLevel);
            }
         }
      }
   }

   private long        _texturePriority;

   private final float _verticalExaggeration;


   private boolean isReadyToRenderTiles(final G3MRenderContext rc) {
      if (!_validLayerTilesRenderParameters) {
         return false;
      }

      if (!_layerSet.isReady()) {
         return false;
      }

      if (_elevationDataProvider != null) {
         if (!_elevationDataProvider.isReadyToRender(rc)) {
            return false;
         }
      }

      if (_firstLevelTilesJustCreated) {
         _firstLevelTilesJustCreated = false;

         final int firstLevelTilesCount = _firstLevelTiles.size();

         if (_parameters._forceFirstLevelTilesRenderOnStart) {
            final TilesStatistics statistics = new TilesStatistics();

            final PlanetRendererContext prc = new PlanetRendererContext(_tessellator, _elevationDataProvider, _texturizer,
                     _tileRasterizer, _layerSet, _parameters, statistics, _lastSplitTimer, true, _texturePriority,
                     _verticalExaggeration);

            for (int i = 0; i < firstLevelTilesCount; i++) {
               final Tile tile = _firstLevelTiles.get(i);
               tile.prepareForFullRendering(rc, prc);
            }
         }

         if (_texturizer != null) {
            for (int i = 0; i < firstLevelTilesCount; i++) {
               final Tile tile = _firstLevelTiles.get(i);
               _texturizer.justCreatedTopTile(rc, tile, _layerSet);
            }
         }
      }

      if (_parameters._forceFirstLevelTilesRenderOnStart) {
         if (!_allFirstLevelTilesAreTextureSolved) {
            final int firstLevelTilesCount = _firstLevelTiles.size();
            for (int i = 0; i < firstLevelTilesCount; i++) {
               final Tile tile = _firstLevelTiles.get(i);
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

            _allFirstLevelTilesAreTextureSolved = true;
         }
      }

      return true;
   }

   private boolean             _recreateTilesPending;

   private final GLState       _glState;
   private ProjectionGLFeature _projection;
   private ModelGLFeature      _model;


   private void updateGLState(final G3MRenderContext rc) {

      final Camera cam = rc.getCurrentCamera();
      if (_projection == null) {
         _projection = new ProjectionGLFeature(cam.getProjectionMatrix44D());
         _glState.addGLFeature(_projection, true);
      }
      else {
         _projection.setMatrix(cam.getProjectionMatrix44D());
      }

      if (_model == null) {
         _model = new ModelGLFeature(cam.getModelMatrix44D());
         _glState.addGLFeature(_model, true);
      }
      else {
         _model.setMatrix(cam.getModelMatrix44D());
      }
   }

   private final SurfaceElevationProvider_Tree _elevationListenersTree = new SurfaceElevationProvider_Tree();

   private boolean                             _validLayerTilesRenderParameters;


   public PlanetRenderer(final TileTessellator tessellator,
                         final ElevationDataProvider elevationDataProvider,
                         final float verticalExaggeration,
                         final TileTexturizer texturizer,
                         final TileRasterizer tileRasterizer,
                         final LayerSet layerSet,
                         final TilesRenderParameters parameters,
                         final boolean showStatistics,
                         final long texturePriority) {
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
      _glState = new GLState();
      _validLayerTilesRenderParameters = false;
      _layerSet.setChangeListener(this);
      if (_tileRasterizer != null) {
         _tileRasterizer.setChangeListener(this);
      }
   }


   @Override
   public void dispose() {
      clearFirstLevelTiles();

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

      super.dispose();

   }


   @Override
   public final void initialize(final G3MContext context) {
      _context = context;

      clearFirstLevelTiles();
      createFirstLevelTiles(context);

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
                            final GLState glState) {

      updateGLState(rc);

      //  if (_recreateTilesPending) {
      //    recreateTiles();
      //    _recreateTilesPending = false;
      //  }

      // Saving camera for use in onTouchEvent
      _lastCamera = rc.getCurrentCamera();

      final TilesStatistics statistics = new TilesStatistics();

      final PlanetRendererContext prc = new PlanetRendererContext(_tessellator, _elevationDataProvider, _texturizer,
               _tileRasterizer, _layerSet, _parameters, statistics, _lastSplitTimer, _firstRender, _texturePriority,
               _verticalExaggeration); // if first render, force full render

      final int firstLevelTilesCount = _firstLevelTiles.size();

      final Planet planet = rc.getPlanet();
      final Vector3D cameraNormalizedPosition = _lastCamera.getNormalizedPosition();
      final double cameraAngle2HorizonInRadians = _lastCamera.getAngle2HorizonInRadians();
      final Frustum cameraFrustumInModelCoordinates = _lastCamera.getFrustumInModelCoordinates();

      if (_firstRender && _parameters._forceFirstLevelTilesRenderOnStart) {
         // force one render pass of the firstLevelTiles tiles to make the (toplevel) textures
         // loaded as they will be used as last-chance fallback texture for any tile.
         _firstRender = false;

         for (int i = 0; i < firstLevelTilesCount; i++) {
            final Tile tile = _firstLevelTiles.get(i);
            tile.render(rc, prc, _glState, null, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians,
                     cameraFrustumInModelCoordinates);
         }
      }
      else {
         java.util.LinkedList<Tile> toVisit = new java.util.LinkedList<Tile>();
         for (int i = 0; i < firstLevelTilesCount; i++) {
            toVisit.addLast(_firstLevelTiles.get(i));
         }

         while (toVisit.size() > 0) {
            final java.util.LinkedList<Tile> toVisitInNextIteration = new java.util.LinkedList<Tile>();

            for (final Tile tile : toVisit) {
               tile.render(rc, prc, _glState, toVisitInNextIteration, planet, cameraNormalizedPosition,
                        cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates);
            }

            toVisit = toVisitInNextIteration;
         }
      }

      if (_showStatistics) {
         statistics.log(rc.getLogger());
      }


      final Sector renderedSector = statistics.getRenderedSector();
      if (renderedSector != null) {
         if ((_lastVisibleSector == null) || !renderedSector.isEquals(_lastVisibleSector)) {
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
      if (_lastCamera == null) {
         return false;
      }

      if (touchEvent.getType() == TouchEventType.LongPress) {
         final Vector2I pixel = touchEvent.getTouch(0).getPos();
         final Vector3D ray = _lastCamera.pixel2Ray(pixel);
         final Vector3D origin = _lastCamera.getCartesianPosition();

         final Planet planet = ec.getPlanet();

         final Vector3D positionCartesian = planet.closestIntersection(origin, ray);
         if (positionCartesian.isNan()) {
            return false;
         }

         final Geodetic3D position = planet.toGeodetic3D(positionCartesian);

         final int firstLevelTilesCount = _firstLevelTiles.size();
         for (int i = 0; i < firstLevelTilesCount; i++) {
            final Tile tile = _firstLevelTiles.get(i).getDeepestTileContaining(position);
            if (tile != null) {
               ILogger.instance().logInfo("Touched on %s", tile.description());
               if (_texturizer.onTerrainTouchEvent(ec, position, tile, _layerSet)) {
                  return true;
               }
            }
         }

      }

      return false;
   }


   @Override
   public final void onResizeViewportEvent(final G3MEventContext ec,
                                           final int width,
                                           final int height) {

   }


   @Override
   public final boolean isReadyToRender(final G3MRenderContext rc) {
      return isReadyToRenderTiles(rc);
   }


   public final void acceptTileVisitor(final ITileVisitor tileVisitor) {
      _tileVisitor = tileVisitor;
   }


   public final void visitTilesTouchesWith(final Sector sector,
                                           final int firstLevel,
                                           final int maxLevel) {
      if (_tileVisitor != null) {
         final LayerTilesRenderParameters parameters = _layerSet.getLayerTilesRenderParameters();

         final int firstLevelCache = (firstLevel < parameters._firstLevel) ? parameters._firstLevel : firstLevel;

         final int maxLevelCache = (maxLevel > parameters._maxLevel) ? parameters._maxLevel : maxLevel;
         // Get Tiles to Cache

         final int firstLevelTilesCount = _firstLevelTiles.size();
         for (int i = 0; i < firstLevelTilesCount; i++) {
            final Tile tile = _firstLevelTiles.get(i);
            if (tile.getSector().touchesWith(sector)) {
               _tileVisitor.visitTile(tile);
               visitSubTilesTouchesWith(tile, sector, firstLevelCache, maxLevelCache);
            }
         }
      }
   }


   @Override
   public final void start(final G3MRenderContext rc) {
      _firstRender = true;
   }


   @Override
   public final void stop(final G3MRenderContext rc) {
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
         pruneFirstLevelTiles();
      }
   }


   @Override
   public final void changed() {
      if (!_recreateTilesPending) {
         _recreateTilesPending = true;
         // recreateTiles() delete tiles, then meshes, and delete textures from the GPU
         //   so it has to be executed in the OpenGL thread
         _context.getThreadUtils().invokeInRendererThread(new RecreateTilesTask(this), true);
      }
   }


   public final void recreateTiles() {
      pruneFirstLevelTiles();
      clearFirstLevelTiles();
      _firstRender = true;
      _allFirstLevelTilesAreTextureSolved = false;
      createFirstLevelTiles(_context);

      _recreateTilesPending = false;
   }


   /**
    * Answer the visible-sector, it can be null if globe was not yet rendered.
    */
   public final Sector getVisibleSector() {
      return _lastVisibleSector;
   }


   /**
    * Add a listener for notification of visible-sector changes.
    * 
    * @param stabilizationInterval
    *           How many time the visible-sector has to be settled (without changes) before triggering the event. Useful for avoid
    *           process while the camera is being moved (as in animations). If stabilizationInterval is zero, the event is
    *           triggered inmediatly.
    */
   public final void addVisibleSectorListener(final VisibleSectorListener listener,
                                              final TimeInterval stabilizationInterval) {
      _visibleSectorListeners.add(new VisibleSectorListenerEntry(listener, stabilizationInterval));
   }


   /**
    * Add a listener for notification of visible-sector changes.
    * 
    * The event is triggered immediately without waiting for the visible-sector get settled.
    */
   public final void addVisibleSectorListener(final VisibleSectorListener listener) {
      addVisibleSectorListener(listener, TimeInterval.zero());
   }


   /**
    * Set the download-priority used by Tiles (for downloading textures).
    * 
    * @param texturePriority
    *           : new value for download priority of textures
    */
   public final void setTexturePriority(final long texturePriority) {
      _texturePriority = texturePriority;
   }


   /**
    * Return the current value for the download priority of textures
    * 
    * @return _texturePriority: long
    */
   public final long getTexturePriority() {
      return _texturePriority;
   }


   /**
    * @see Renderer#isPlanetRenderer()
    */
   @Override
   public final boolean isPlanetRenderer() {
      return true;
   }


   @Override
   public final SurfaceElevationProvider getSurfaceElevationProvider() {
      return (_elevationDataProvider == null) ? null : this;
   }


   @Override
   public final PlanetRenderer getPlanetRenderer() {
      return this;
   }


   @Override
   public final void addListener(final Angle latitude,
                                 final Angle longitude,
                                 final SurfaceElevationListener listener) {
      _elevationListenersTree.add(new Geodetic2D(latitude, longitude), listener);
   }


   @Override
   public final void addListener(final Geodetic2D position,
                                 final SurfaceElevationListener listener) {
      _elevationListenersTree.add(position, listener);
   }


   @Override
   public final void removeListener(final SurfaceElevationListener listener) {
      _elevationListenersTree.remove(listener);
   }


   public final void sectorElevationChanged(final ElevationData elevationData) {
      if (elevationData != null) {
         _elevationListenersTree.notifyListeners(elevationData, _verticalExaggeration);
      }
   }
}
