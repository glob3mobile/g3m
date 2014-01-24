package org.glob3.mobile.generated; 
//
//  Tile.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  Tile.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




//class G3MRenderContext;
//class Mesh;
//class TileTessellator;
//class TileTexturizer;
//class TilesRenderParameters;
//class ITimer;
//class TilesStatistics;
//class TileKey;
//class Vector3D;
//class GLState;
//class BoundingVolume;
//class ElevationDataProvider;
//class ElevationData;
//class MeshHolder;
//class Vector2I;
//class GPUProgramState;
//class TileElevationDataRequest;
//class Frustum;
//class Box;
//class PlanetRenderer;
//class GLState;
//class PlanetTileTessellatorData;
//class LayerTilesRenderParameters;
//class TileRasterizer;
//class LayerSet;


public class Tile
{
  private TileTexturizer _texturizer;
  private Tile _parent;

  private Mesh _tessellatorMesh;

  private Mesh _debugMesh;
  private Mesh _texturizedMesh;
  private TileElevationDataRequest _elevationDataRequest;

  private Mesh _flatColorMesh;

  private boolean _textureSolved;
  private java.util.ArrayList<Tile> _subtiles;
  private boolean _justCreatedSubtiles;

  private boolean _texturizerDirty; //Texturizer needs to be called

  private float _verticalExaggeration;
  private TileTessellatorMeshData _tileTessellatorMeshData = new TileTessellatorMeshData();

  private BoundingVolume _boundingVolume;

  //LOD TEST DATA
  private Vector3D _middleNorthPoint;
  private Vector3D _middleSouthPoint;
  private Vector3D _middleEastPoint;
  private Vector3D _middleWestPoint;
  private void computeTileCorners(Planet planet)
  {
  
    if (_tessellatorMesh == null)
    {
      ILogger.instance().logError("Error in Tile::computeTileCorners");
      return;
    }
  
    if (_middleWestPoint != null)
       _middleWestPoint.dispose();
    if (_middleEastPoint != null)
       _middleEastPoint.dispose();
    if (_middleNorthPoint != null)
       _middleNorthPoint.dispose();
    if (_middleSouthPoint != null)
       _middleSouthPoint.dispose();
  
    final double mediumHeight = _tileTessellatorMeshData._averageHeight;
  
    final Geodetic2D center = _sector.getCenter();
    final Geodetic3D gN = new Geodetic3D(new Geodetic2D(_sector.getNorth(), center._longitude), mediumHeight);
    final Geodetic3D gS = new Geodetic3D(new Geodetic2D(_sector.getSouth(), center._longitude), mediumHeight);
    final Geodetic3D gW = new Geodetic3D(new Geodetic2D(center._latitude, _sector.getWest()), mediumHeight);
    final Geodetic3D gE = new Geodetic3D(new Geodetic2D(center._latitude, _sector.getEast()), mediumHeight);
  
    _middleNorthPoint = new Vector3D(planet.toCartesian(gN));
    _middleSouthPoint = new Vector3D(planet.toCartesian(gS));
    _middleEastPoint = new Vector3D(planet.toCartesian(gE));
    _middleWestPoint = new Vector3D(planet.toCartesian(gW));
  }

  private double _latitudeArcSegmentRatioSquared;
  private double _longitudeArcSegmentRatioSquared;


  private void prepareTestLODData(Planet planet)
  {
  
    if (_middleNorthPoint == null)
    {
      ILogger.instance().logError("Error in Tile::prepareTestLODData");
      return;
    }
  
    final Vector3D nN = planet.centricSurfaceNormal(_middleNorthPoint);
    final Vector3D nS = planet.centricSurfaceNormal(_middleSouthPoint);
    final Vector3D nE = planet.centricSurfaceNormal(_middleEastPoint);
    final Vector3D nW = planet.centricSurfaceNormal(_middleWestPoint);
  
    /*
     Arco = ang * Cuerda / (2 * sen(ang/2))
     */
  
    Angle latitudeAngle = nN.angleBetween(nS);
    double latRad = latitudeAngle._radians;
    final double sin_lat_2 = java.lang.Math.sin(latRad / 2);
    final double latitudeArcSegmentRatio = sin_lat_2 == 0? 1 : latRad / (2 * sin_lat_2);
  
    Angle longitudeAngle = nE.angleBetween(nW);
    final double lonRad = longitudeAngle._radians;
    final double sin_lon_2 = java.lang.Math.sin(lonRad / 2);
    final double longitudeArcSegmentRatio = sin_lon_2 == 0? 1 : lonRad / (2 * sin_lon_2);
  
    _latitudeArcSegmentRatioSquared = latitudeArcSegmentRatio * latitudeArcSegmentRatio;
    _longitudeArcSegmentRatioSquared = longitudeArcSegmentRatio * longitudeArcSegmentRatio;
  }
  //////////////////////////////////////////

  private Mesh getTessellatorMesh(G3MRenderContext rc, ElevationDataProvider elevationDataProvider, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters, TilesRenderParameters tilesRenderParameters)
  {
  
  
    if ((_elevationData == null) && (elevationDataProvider != null) && (elevationDataProvider.isEnabled()))
    {
      initializeElevationData(elevationDataProvider, tessellator, layerTilesRenderParameters._tileMeshResolution, rc.getPlanet(), tilesRenderParameters._renderDebug);
    }
  
    if ((_tessellatorMesh == null) || _mustActualizeMeshDueToNewElevationData)
    {
      _mustActualizeMeshDueToNewElevationData = false;
  
      if (elevationDataProvider == null)
      {
        // no elevation data provider, just create a simple mesh without elevation
        _tessellatorMesh = tessellator.createTileMesh(rc.getPlanet(), layerTilesRenderParameters._tileMeshResolution, this, null, _verticalExaggeration, layerTilesRenderParameters._mercator, tilesRenderParameters._renderDebug, _tileTessellatorMeshData);
  
        computeTileCorners(rc.getPlanet()); //COMPUTING CORNERS
  
      }
      else
      {
        Mesh tessellatorMesh = tessellator.createTileMesh(rc.getPlanet(), layerTilesRenderParameters._tileMeshResolution, this, _elevationData, _verticalExaggeration, layerTilesRenderParameters._mercator, tilesRenderParameters._renderDebug, _tileTessellatorMeshData);
  
        MeshHolder meshHolder = (MeshHolder) _tessellatorMesh;
        if (meshHolder == null)
        {
          meshHolder = new MeshHolder(tessellatorMesh);
          _tessellatorMesh = meshHolder;
        }
        else
        {
          meshHolder.setMesh(tessellatorMesh);
        }
  
        computeTileCorners(rc.getPlanet()); //COMPUTING CORNERS
      }
  
      //Notifying when the tile is first created and every time the elevation data changes
      _planetRenderer.sectorElevationChanged(_elevationData);
    }
  
    //_tessellatorMesh->showNormals(true);
  
    return _tessellatorMesh;
  }

  private Mesh getDebugMesh(G3MRenderContext rc, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters)
  {
    if (_debugMesh == null)
    {
      final Vector2I tileMeshResolution = new Vector2I(layerTilesRenderParameters._tileMeshResolution);
  
      //TODO: CHECK
      _debugMesh = tessellator.createTileDebugMesh(rc.getPlanet(), tileMeshResolution, this);
    }
    return _debugMesh;
  }

  private boolean isVisible(G3MRenderContext rc, Planet planet, Vector3D cameraNormalizedPosition, double cameraAngle2HorizonInRadians, Frustum cameraFrustumInModelCoordinates, ElevationDataProvider elevationDataProvider, Sector renderedSector, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters, TilesRenderParameters tilesRenderParameters)
  {
  
    ////  const BoundingVolume* boundingVolume = getTessellatorMesh(rc, trc)->getBoundingVolume();
    //  const BoundingVolume* boundingVolume = getBoundingVolume(rc, trc);
    //  if (boundingVolume == NULL) {
    //    return false;
    //  }
    //
    //  if (!boundingVolume->touchesFrustum(cameraFrustumInModelCoordinates)) {
    //    return false;
    //  }
    //
    //  // test if sector is back oriented with respect to the camera
    //  return !_sector.isBackOriented(rc,
    //                                 getMinHeight(),
    //                                 planet,
    //                                 cameraNormalizedPosition,
    //                                 cameraAngle2HorizonInRadians);
  
  
    /* //AGUSTIN:now that zfar is located in the horizon, this test is not needed anymore
     // test if sector is back oriented with respect to the camera
     if (_sector.isBackOriented(rc,
     getMinHeight(),
     planet,
     cameraNormalizedPosition,
     cameraAngle2HorizonInRadians)) {
     return false;
     }*/
  
    if (renderedSector != null && !renderedSector.touchesWith(_sector)) //Incomplete world
    {
      return false;
    }
  
    final BoundingVolume boundingVolume = getBoundingVolume(rc, elevationDataProvider, tessellator, layerTilesRenderParameters, tilesRenderParameters);
  
    return ((boundingVolume != null) && boundingVolume.touchesFrustum(cameraFrustumInModelCoordinates));
  }

  private boolean _lastLodTest;
  private double _lastLodTimeInMS;

  private boolean meetsRenderCriteria(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, TileTexturizer texturizer, TilesRenderParameters tilesRenderParameters, TilesStatistics tilesStatistics, ITimer lastSplitTimer, double texWidthSquared, double texHeightSquared, double nowInMS)
  {
  
    if ((_level >= layerTilesRenderParameters._maxLevelForPoles) && (_sector.touchesPoles()))
    {
      return true;
    }
  
    if (_level >= layerTilesRenderParameters._maxLevel)
    {
      return true;
    }
  
    if (texturizer != null)
    {
      if (texturizer.tileMeetsRenderCriteria(this))
      {
        return true;
      }
    }
  
    if (_lastLodTimeInMS != 0 && (nowInMS - _lastLodTimeInMS) < 500)
    {
      return _lastLodTest;
    }
  
    if (tilesRenderParameters._useTilesSplitBudget)
    {
      if (_subtiles == null) // the tile needs to create the subtiles
      {
        if (tilesStatistics.getSplitsCountInFrame() > 0)
        {
          // there are not more splitsCount-budget to spend
          return true;
        }
  
        if (lastSplitTimer.elapsedTimeInMilliseconds() < 25)
        {
          // there are not more time-budget to spend
          return true;
        }
      }
    }
  
    _lastLodTimeInMS = nowInMS; //Storing time of result
  
    final Planet planet = rc.getPlanet();
  
    if ((_latitudeArcSegmentRatioSquared == 0) || (_longitudeArcSegmentRatioSquared == 0))
    {
      prepareTestLODData(planet);
    }
  
    final Camera camera = rc.getCurrentCamera();
    final Vector2F pN = camera.point2Pixel(_middleNorthPoint);
    final Vector2F pS = camera.point2Pixel(_middleSouthPoint);
    final Vector2F pE = camera.point2Pixel(_middleEastPoint);
    final Vector2F pW = camera.point2Pixel(_middleWestPoint);
  
    final double latitudeMiddleDistSquared = pN.squaredDistanceTo(pS);
    final double longitudeMiddleDistSquared = pE.squaredDistanceTo(pW);
  
    final double latitudeMiddleArcDistSquared = latitudeMiddleDistSquared * _latitudeArcSegmentRatioSquared;
    final double longitudeMiddleArcDistSquared = longitudeMiddleDistSquared * _longitudeArcSegmentRatioSquared;
  
    //Testing Area
    _lastLodTest = ((latitudeMiddleArcDistSquared * longitudeMiddleArcDistSquared) <= (texHeightSquared *texWidthSquared));
  
  
    return _lastLodTest;
  }

  private void rawRender(G3MRenderContext rc, GLState glState, TileTexturizer texturizer, ElevationDataProvider elevationDataProvider, TileTessellator tessellator, TileRasterizer tileRasterizer, LayerTilesRenderParameters layerTilesRenderParameters, LayerSet layerSet, TilesRenderParameters tilesRenderParameters, boolean isForcedFullRender, long texturePriority)
  {
  
    Mesh tessellatorMesh = getTessellatorMesh(rc, elevationDataProvider, tessellator, layerTilesRenderParameters, tilesRenderParameters);
    if (tessellatorMesh == null)
    {
      return;
    }
  
    if (texturizer == null)
    {
      tessellatorMesh.render(rc, glState);
    }
    else
    {
      final boolean needsToCallTexturizer = (_texturizedMesh == null) || isTexturizerDirty();
  
      if (needsToCallTexturizer)
      {
        _texturizedMesh = texturizer.texturize(rc, tessellator, tileRasterizer, layerTilesRenderParameters, layerSet, isForcedFullRender, texturePriority, this, tessellatorMesh, _texturizedMesh);
      }
  
      if (_texturizedMesh != null)
      {
        _texturizedMesh.render(rc, glState);
      }
      else
      {
        //Adding flat color if no texture set on the mesh
        if (_flatColorMesh == null)
        {
          _flatColorMesh = new FlatColorMesh(tessellatorMesh, false, Color.newFromRGBA((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0), true);
        }
        _flatColorMesh.render(rc, glState);
  
        //tessellatorMesh->render(rc, glState);
      }
    }
  
  
    //  const BoundingVolume* boundingVolume = getBoundingVolume(rc, trc);
    //  boundingVolume->render(rc, parentState);
  }

  private void debugRender(G3MRenderContext rc, GLState glState, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters)
  {
    Mesh debugMesh = getDebugMesh(rc, tessellator, layerTilesRenderParameters);
    if (debugMesh != null)
    {
      //debugMesh->render(rc);
      debugMesh.render(rc, glState);
    }
  }

  private Tile createSubTile(Angle lowerLat, Angle lowerLon, Angle upperLat, Angle upperLon, int level, int row, int column, boolean setParent)
  {
    Tile parent = setParent ? this : null;
    return new Tile(_texturizer, parent, new Sector(new Geodetic2D(lowerLat, lowerLon), new Geodetic2D(upperLat, upperLon)), level, row, column, _planetRenderer);
  }


  private java.util.ArrayList<Tile> getSubTiles(Angle splitLatitude, Angle splitLongitude)
  {
    if (_subtiles == null)
    {
      _subtiles = createSubTiles(splitLatitude, splitLongitude, true);
      _justCreatedSubtiles = true;
    }
    return _subtiles;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Tile(Tile that);

  private void ancestorTexturedSolvedChanged(Tile ancestor, boolean textureSolved)
  {
    if (textureSolved && isTextureSolved())
    {
      return;
    }
  
    if (_texturizer != null)
    {
      _texturizer.ancestorTexturedSolvedChanged(this, ancestor, textureSolved);
    }
  
    if (_subtiles != null)
    {
      final int subtilesSize = _subtiles.size();
      for (int i = 0; i < subtilesSize; i++)
      {
        Tile subtile = _subtiles.get(i);
        subtile.ancestorTexturedSolvedChanged(ancestor, textureSolved);
      }
    }
  }

  private boolean _isVisible;
  private void setIsVisible(boolean isVisible, TileTexturizer texturizer)
  {
    if (_isVisible != isVisible)
    {
      _isVisible = isVisible;
  
      if (!_isVisible)
      {
        deleteTexturizedMesh(texturizer);
      }
    }
  }

  private void deleteTexturizedMesh(TileTexturizer texturizer)
  {
    // check for (_parent != NULL) to avoid deleting the firstLevel tiles.
    // in this case, the mesh is always loaded (as well as its texture) to be the last option
    // falback texture for any tile
    if ((_parent != null) && (_texturizedMesh != null))
    {
  
      if (texturizer != null)
      {
        texturizer.tileMeshToBeDeleted(this, _texturizedMesh);
      }
  
      if (_texturizedMesh != null)
         _texturizedMesh.dispose();
      _texturizedMesh = null;
  
      if (_texturizerData != null)
         _texturizerData.dispose();
      _texturizerData = null;
  
      setTexturizerDirty(true);
      setTextureSolved(false);
    }
  }

  private ITexturizerData _texturizerData;
  private PlanetTileTessellatorData _tessellatorData;

  private int _elevationDataLevel;
  private ElevationData _elevationData;
  private boolean _mustActualizeMeshDueToNewElevationData;
  private ElevationDataProvider _lastElevationDataProvider;
  private int _lastTileMeshResolutionX;
  private int _lastTileMeshResolutionY;

  private final PlanetRenderer _planetRenderer;


  //Box* Tile::getTileBoundingVolume(const G3MRenderContext *rc) {
  //  if (_tileBoundingVolume == NULL) {
  //    const Planet* planet = rc->getPlanet();
  //
  //    const double minHeight = getMinHeight() * _verticalExaggeration;
  //    const double maxHeight = getMaxHeight() * _verticalExaggeration;
  //
  //    const Vector3D v0 = planet->toCartesian( _sector._center, maxHeight );
  //    const Vector3D v1 = planet->toCartesian( _sector.getNE(),     minHeight );
  //    const Vector3D v2 = planet->toCartesian( _sector.getNW(),     minHeight );
  //    const Vector3D v3 = planet->toCartesian( _sector.getSE(),     minHeight );
  //    const Vector3D v4 = planet->toCartesian( _sector.getSW(),     minHeight );
  //
  //    double lowerX = v0._x;
  //    if (v1._x < lowerX) { lowerX = v1._x; }
  //    if (v2._x < lowerX) { lowerX = v2._x; }
  //    if (v3._x < lowerX) { lowerX = v3._x; }
  //    if (v4._x < lowerX) { lowerX = v4._x; }
  //
  //    double upperX = v0._x;
  //    if (v1._x > upperX) { upperX = v1._x; }
  //    if (v2._x > upperX) { upperX = v2._x; }
  //    if (v3._x > upperX) { upperX = v3._x; }
  //    if (v4._x > upperX) { upperX = v4._x; }
  //
  //
  //    double lowerY = v0._y;
  //    if (v1._y < lowerY) { lowerY = v1._y; }
  //    if (v2._y < lowerY) { lowerY = v2._y; }
  //    if (v3._y < lowerY) { lowerY = v3._y; }
  //    if (v4._y < lowerY) { lowerY = v4._y; }
  //
  //    double upperY = v0._y;
  //    if (v1._y > upperY) { upperY = v1._y; }
  //    if (v2._y > upperY) { upperY = v2._y; }
  //    if (v3._y > upperY) { upperY = v3._y; }
  //    if (v4._y > upperY) { upperY = v4._y; }
  //
  //
  //    double lowerZ = v0._z;
  //    if (v1._z < lowerZ) { lowerZ = v1._z; }
  //    if (v2._z < lowerZ) { lowerZ = v2._z; }
  //    if (v3._z < lowerZ) { lowerZ = v3._z; }
  //    if (v4._z < lowerZ) { lowerZ = v4._z; }
  //
  //    double upperZ = v0._z;
  //    if (v1._z > upperZ) { upperZ = v1._z; }
  //    if (v2._z > upperZ) { upperZ = v2._z; }
  //    if (v3._z > upperZ) { upperZ = v3._z; }
  //    if (v4._z > upperZ) { upperZ = v4._z; }
  //
  //
  //    _tileBoundingVolume = new Box(Vector3D(lowerX, lowerY, lowerZ),
  //                                  Vector3D(upperX, upperY, upperZ));
  //  }
  //  return _tileBoundingVolume;
  //}
  
  
  private BoundingVolume getBoundingVolume(G3MRenderContext rc, ElevationDataProvider elevationDataProvider, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters, TilesRenderParameters tilesRenderParameters)
  {
    if (_boundingVolume == null)
    {
      Mesh mesh = getTessellatorMesh(rc, elevationDataProvider, tessellator, layerTilesRenderParameters, tilesRenderParameters);
      if (mesh != null)
      {
        //      _boundingVolume = mesh->getBoundingVolume()->createSphere();
        _boundingVolume = mesh.getBoundingVolume();
      }
    }
    return _boundingVolume;
  }

  public final Sector _sector ;
  public final int _level;
  public final int _row;
  public final int _column;

  public Tile(TileTexturizer texturizer, Tile parent, Sector sector, int level, int row, int column, PlanetRenderer planetRenderer)
  //_tileBoundingVolume(NULL),
  {
     _texturizer = texturizer;
     _parent = parent;
     _sector = new Sector(sector);
     _level = level;
     _row = row;
     _column = column;
     _tessellatorMesh = null;
     _debugMesh = null;
     _flatColorMesh = null;
     _texturizedMesh = null;
     _textureSolved = false;
     _texturizerDirty = true;
     _subtiles = null;
     _justCreatedSubtiles = false;
     _isVisible = false;
     _texturizerData = null;
     _elevationData = null;
     _elevationDataLevel = -1;
     _elevationDataRequest = null;
     _verticalExaggeration = 0F;
     _mustActualizeMeshDueToNewElevationData = false;
     _lastTileMeshResolutionX = -1;
     _lastTileMeshResolutionY = -1;
     _boundingVolume = null;
     _lastLodTimeInMS = 0;
     _planetRenderer = planetRenderer;
     _tessellatorData = null;
     _middleNorthPoint = null;
     _middleSouthPoint = null;
     _middleEastPoint = null;
     _middleWestPoint = null;
     _latitudeArcSegmentRatioSquared = 0;
     _longitudeArcSegmentRatioSquared = 0;
    //  int __remove_tile_print;
    //  printf("Created tile=%s\n deltaLat=%s deltaLon=%s\n",
    //         getKey().description().c_str(),
    //         _sector._deltaLatitude.description().c_str(),
    //         _sector._deltaLongitude.description().c_str()
    //         );
  }

  public void dispose()
  {
    prune(null, null);
  
    //  delete _boundingVolume;
  
    if (_debugMesh != null)
       _debugMesh.dispose();
    _debugMesh = null;
  
    if (_flatColorMesh != null)
       _flatColorMesh.dispose();
    _flatColorMesh = null;
  
    if (_tessellatorMesh != null)
       _tessellatorMesh.dispose();
    _tessellatorMesh = null;
  
    if (_texturizerData != null)
       _texturizerData.dispose();
    _texturizerData = null;
  
    if (_texturizedMesh != null)
       _texturizedMesh.dispose();
    _texturizedMesh = null;
  
  //  delete _tileBoundingVolume;
  //  _tileBoundingVolume = NULL;
  
    if (_elevationData != null)
       _elevationData.dispose();
    _elevationData = null;
  
    if (_elevationDataRequest != null)
    {
      _elevationDataRequest.cancelRequest(); //The listener will auto delete
      if (_elevationDataRequest != null)
         _elevationDataRequest.dispose();
      _elevationDataRequest = null;
    }
  
    if (_tessellatorData != null)
       _tessellatorData.dispose();
  
    if (_middleEastPoint != null)
       _middleEastPoint.dispose();
    if (_middleNorthPoint != null)
       _middleNorthPoint.dispose();
    if (_middleSouthPoint != null)
       _middleSouthPoint.dispose();
    if (_middleWestPoint != null)
       _middleWestPoint.dispose();
  }

  //Change to public for TileCache
  public final java.util.ArrayList<Tile> getSubTiles(boolean mercator)
  {
    final Geodetic2D lower = _sector._lower;
    final Geodetic2D upper = _sector._upper;
  
    final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
  
  
    final Angle splitLatitude = mercator ? MercatorUtils.calculateSplitLatitude(lower._latitude, upper._latitude) : Angle.midAngle(lower._latitude, upper._latitude);
    /*                               */
    /*                               */
  
    return getSubTiles(splitLatitude, splitLongitude);
  }

//  const Sector getSector() const {
//    return _sector;
//  }
//
//  int getLevel() const {
//    return _level;
//  }
//
//  int getRow() const {
//    return _row;
//  }
//
//  int getColumn() const {
//    return _column;
//  }

  public final Mesh getTexturizedMesh()
  {
    return _texturizedMesh;
  }

  public final Tile getParent()
  {
    return _parent;
  }

  public final void prepareForFullRendering(G3MRenderContext rc, TileTexturizer texturizer, ElevationDataProvider elevationDataProvider, TileTessellator tessellator, TileRasterizer tileRasterizer, LayerTilesRenderParameters layerTilesRenderParameters, LayerSet layerSet, TilesRenderParameters tilesRenderParameters, boolean isForcedFullRender, long texturePriority, float verticalExaggeration)
  {
  
    //You have to set _verticalExaggertion
    if (verticalExaggeration != _verticalExaggeration)
    {
      // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
      _verticalExaggeration = verticalExaggeration;
    }
  
  
    Mesh tessellatorMesh = getTessellatorMesh(rc, elevationDataProvider, tessellator, layerTilesRenderParameters, tilesRenderParameters);
    if (tessellatorMesh == null)
    {
      return;
    }
  
    //  TileTexturizer* texturizer = prc->getTexturizer();
    if (texturizer != null)
    {
      final boolean needsToCallTexturizer = (_texturizedMesh == null) || isTexturizerDirty();
  
      if (needsToCallTexturizer)
      {
        _texturizedMesh = texturizer.texturize(rc, tessellator, tileRasterizer, layerTilesRenderParameters, layerSet, isForcedFullRender, texturePriority, this, tessellatorMesh, _texturizedMesh);
      }
    }
  }

  public final void render(G3MRenderContext rc, GLState parentState, java.util.LinkedList<Tile> toVisitInNextIteration, Planet planet, Vector3D cameraNormalizedPosition, double cameraAngle2HorizonInRadians, Frustum cameraFrustumInModelCoordinates, TilesStatistics tilesStatistics, float verticalExaggeration, LayerTilesRenderParameters layerTilesRenderParameters, TileTexturizer texturizer, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, ElevationDataProvider elevationDataProvider, TileTessellator tessellator, TileRasterizer tileRasterizer, LayerSet layerSet, Sector renderedSector, boolean isForcedFullRender, long texturePriority, double texWidthSquared, double texHeightSquared, double nowInMS, boolean renderTileMeshes)
  {
  
    tilesStatistics.computeTileProcessed(this);
  
    if (verticalExaggeration != _verticalExaggeration)
    {
      // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
      _verticalExaggeration = verticalExaggeration;
    }
  
  
    if (isVisible(rc, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates, elevationDataProvider, renderedSector, tessellator, layerTilesRenderParameters, tilesRenderParameters))
    {
      setIsVisible(true, texturizer);
  
      tilesStatistics.computeVisibleTile(this);
  
      final boolean isRawRender = ((toVisitInNextIteration == null) || meetsRenderCriteria(rc, layerTilesRenderParameters, texturizer, tilesRenderParameters, tilesStatistics, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS) || (tilesRenderParameters._incrementalTileQuality && !_textureSolved));
  
      if (isRawRender)
      {
        if (renderTileMeshes)
        {
          rawRender(rc, parentState, texturizer, elevationDataProvider, tessellator, tileRasterizer, layerTilesRenderParameters, layerSet, tilesRenderParameters, isForcedFullRender, texturePriority);
        }
        if (tilesRenderParameters._renderDebug)
        {
          debugRender(rc, parentState, tessellator, layerTilesRenderParameters);
        }
  
        tilesStatistics.computePlanetRenderered(this);
  
        prune(texturizer, elevationDataProvider);
        //TODO: AVISAR CAMBIO DE TERRENO
      }
      else
      {
        final Geodetic2D lower = _sector._lower;
        final Geodetic2D upper = _sector._upper;
  
        final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
  
        final Angle splitLatitude = layerTilesRenderParameters._mercator ? MercatorUtils.calculateSplitLatitude(lower._latitude, upper._latitude) : Angle.midAngle(lower._latitude, upper._latitude);
        /*                               */
        /*                               */
  
        java.util.ArrayList<Tile> subTiles = getSubTiles(splitLatitude, splitLongitude);
        if (_justCreatedSubtiles)
        {
          lastSplitTimer.start();
          tilesStatistics.computeSplitInFrame();
          _justCreatedSubtiles = false;
        }
  
        final int subTilesSize = subTiles.size();
        for (int i = 0; i < subTilesSize; i++)
        {
          Tile subTile = subTiles.get(i);
          toVisitInNextIteration.addLast(subTile);
        }
      }
    }
    else
    {
      setIsVisible(false, texturizer);
  
      prune(texturizer, elevationDataProvider);
      //TODO: AVISAR CAMBIO DE TERRENO
    }
  }

  public final TileKey getKey()
  {
    return new TileKey(_level, _row, _column);
  }

  public final void setTextureSolved(boolean textureSolved)
  {
    if (textureSolved != _textureSolved)
    {
      _textureSolved = textureSolved;
  
      if (_textureSolved)
      {
        if (_texturizerData != null)
           _texturizerData.dispose();
        _texturizerData = null;
      }
  
      if (_subtiles != null)
      {
        final int subtilesSize = _subtiles.size();
        for (int i = 0; i < subtilesSize; i++)
        {
          Tile subtile = _subtiles.get(i);
          subtile.ancestorTexturedSolvedChanged(this, _textureSolved);
        }
      }
    }
  }

  public final boolean isTextureSolved()
  {
    return _textureSolved;
  }

  public final void setTexturizerDirty(boolean texturizerDirty)
  {
    _texturizerDirty = texturizerDirty;
  }

  public final boolean isTexturizerDirty()
  {
    return _texturizerDirty;
  }

  public final boolean hasTexturizerData()
  {
    return (_texturizerData != null);
  }

  public final ITexturizerData getTexturizerData()
  {
    return _texturizerData;
  }

  public final void setTexturizerData(ITexturizerData texturizerData)
  {
    if (texturizerData != _texturizerData)
    {
      if (_texturizerData != null)
         _texturizerData.dispose();
      _texturizerData = texturizerData;
    }
  }

  public final PlanetTileTessellatorData getTessellatorData()
  {
    return _tessellatorData;
  }

  public final void setTessellatorData(PlanetTileTessellatorData tessellatorData)
  {
    if (tessellatorData != _tessellatorData)
    {
      if (_tessellatorData != null)
         _tessellatorData.dispose();
      _tessellatorData = tessellatorData;
    }
  }

  public final Tile getDeepestTileContaining(Geodetic3D position)
  {
    if (_sector.contains(position._latitude, position._longitude))
    {
      if (_subtiles == null)
      {
        return this;
      }
  
      for (int i = 0; i < _subtiles.size(); i++)
      {
        final Tile subtile = _subtiles.get(i);
        final Tile subtileResult = subtile.getDeepestTileContaining(position);
        if (subtileResult != null)
        {
          return subtileResult;
        }
      }
    }
  
    return null;
  }

  public final void prune(TileTexturizer texturizer, ElevationDataProvider elevationDataProvider)
  {
    if (_subtiles != null)
    {
  
      //Notifying elevation event when LOD decreases
      _planetRenderer.sectorElevationChanged(_elevationData);
  
      final int subtilesSize = _subtiles.size();
      for (int i = 0; i < subtilesSize; i++)
      {
        Tile subtile = _subtiles.get(i);
  
        subtile.setIsVisible(false, texturizer);
  
        subtile.prune(texturizer, elevationDataProvider);
        if (texturizer != null)
        {
          texturizer.tileToBeDeleted(subtile, subtile._texturizedMesh);
        }
  
        if (subtile != null)
           subtile.dispose();
      }
  
      _subtiles = null;
      _subtiles = null;
  
    }
  }

  public final void toBeDeleted(TileTexturizer texturizer, ElevationDataProvider elevationDataProvider)
  {
    if (texturizer != null)
    {
      texturizer.tileToBeDeleted(this, _texturizedMesh);
    }
  
    if (elevationDataProvider != null)
    {
      //cancelElevationDataRequest(elevationDataProvider);
      if (_elevationDataRequest != null)
      {
        _elevationDataRequest.cancelRequest();
      }
    }
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(Tile");
    isb.addString(" level=");
    isb.addInt(_level);
    isb.addString(", row=");
    isb.addInt(_row);
    isb.addString(", column=");
    isb.addInt(_column);
    isb.addString(", sector=");
    isb.addString(_sector.description());
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

  public final java.util.ArrayList<Tile> createSubTiles(Angle splitLatitude, Angle splitLongitude, boolean setParent)
  {
    final Geodetic2D lower = _sector._lower;
    final Geodetic2D upper = _sector._upper;
  
    final int nextLevel = _level + 1;
  
    final int row2 = 2 * _row;
    final int column2 = 2 * _column;
  
    java.util.ArrayList<Tile> subTiles = new java.util.ArrayList<Tile>();
  
    final Sector renderedSector = _planetRenderer.getRenderedSector();
  
    Sector s1 = new Sector(new Geodetic2D(lower._latitude, lower._longitude), new Geodetic2D(splitLatitude, splitLongitude));
    if (renderedSector == null || renderedSector.touchesWith(s1))
    {
      subTiles.add(createSubTile(lower._latitude, lower._longitude, splitLatitude, splitLongitude, nextLevel, row2, column2, setParent));
    }
  
    Sector s2 = new Sector(new Geodetic2D(lower._latitude, splitLongitude), new Geodetic2D(splitLatitude, upper._longitude));
    if (renderedSector == null || renderedSector.touchesWith(s2))
    {
      subTiles.add(createSubTile(lower._latitude, splitLongitude, splitLatitude, upper._longitude, nextLevel, row2, column2 + 1, setParent));
    }
  
    Sector s3 = new Sector(new Geodetic2D(splitLatitude, lower._longitude), new Geodetic2D(upper._latitude, splitLongitude));
    if (renderedSector == null || renderedSector.touchesWith(s3))
    {
      subTiles.add(createSubTile(splitLatitude, lower._longitude, upper._latitude, splitLongitude, nextLevel, row2 + 1, column2, setParent));
    }
  
    Sector s4 = new Sector(new Geodetic2D(splitLatitude, splitLongitude), new Geodetic2D(upper._latitude, upper._longitude));
    if (renderedSector == null || renderedSector.touchesWith(s4))
    {
      subTiles.add(createSubTile(splitLatitude, splitLongitude, upper._latitude, upper._longitude, nextLevel, row2 + 1, column2 + 1, setParent));
    }
  
    return subTiles;
  }

  public final boolean isElevationDataSolved()
  {
    return (_elevationDataLevel == _level);
  }

  public final ElevationData getElevationData()
  {
    return _elevationData;
  }

  public final void setElevationData(ElevationData ed, int level)
  {
    if (_elevationDataLevel < level)
    {
  
      if (_elevationData != null)
      {
        if (_elevationData != null)
           _elevationData.dispose();
      }
  
      _elevationData = ed;
      _elevationDataLevel = level;
      _mustActualizeMeshDueToNewElevationData = true;
  
      //If the elevation belongs to tile's level, we notify the sub-tree
      if (isElevationDataSolved())
      {
        if (_subtiles != null)
        {
          final int subtilesSize = _subtiles.size();
          for (int i = 0; i < subtilesSize; i++)
          {
            Tile subtile = _subtiles.get(i);
            subtile.ancestorChangedElevationData(this);
          }
        }
      }
  
    }
  }

  public final void getElevationDataFromAncestor(Vector2I extent)
  {
    if (_elevationData == null)
    {
      Tile ancestor = getParent();
      while ((ancestor != null) && !ancestor.isElevationDataSolved())
      {
        ancestor = ancestor.getParent();
      }
  
      if (ancestor != null)
      {
        ElevationData subView = createElevationDataSubviewFromAncestor(ancestor);
        setElevationData(subView, ancestor._level);
      }
    }
    else
    {
      System.out.print("break point on me\n");
    }
  }

  public final void initializeElevationData(ElevationDataProvider elevationDataProvider, TileTessellator tessellator, Vector2I tileMeshResolution, Planet planet, boolean renderDebug)
  {
    //Storing for subviewing
    _lastElevationDataProvider = elevationDataProvider;
    _lastTileMeshResolutionX = tileMeshResolution._x;
    _lastTileMeshResolutionY = tileMeshResolution._y;
    if (_elevationDataRequest == null)
    {
  
      final Vector2I res = tessellator.getTileMeshResolution(planet, tileMeshResolution, this, renderDebug);
      _elevationDataRequest = new TileElevationDataRequest(this, res, elevationDataProvider);
      _elevationDataRequest.sendRequest();
    }
  
    //If after petition we still have no data we request from ancestor (provider asynchronous)
    if (_elevationData == null)
    {
      getElevationDataFromAncestor(tileMeshResolution);
    }
  
  }

  public final void ancestorChangedElevationData(Tile ancestor)
  {
  
    if (ancestor._level > _elevationDataLevel)
    {
      ElevationData subView = createElevationDataSubviewFromAncestor(ancestor);
      if (subView != null)
      {
        setElevationData(subView, ancestor._level);
      }
    }
  
    if (_subtiles != null)
    {
      final int subtilesSize = _subtiles.size();
      for (int i = 0; i < subtilesSize; i++)
      {
        Tile subtile = _subtiles.get(i);
        subtile.ancestorChangedElevationData(this);
      }
    }
  }

  public final ElevationData createElevationDataSubviewFromAncestor(Tile ancestor)
  {
    ElevationData ed = ancestor.getElevationData();
  
    if (ed == null)
    {
      ILogger.instance().logError("Ancestor can't have undefined Elevation Data.");
      return null;
    }
  
    if (ed.getExtentWidth() < 1 || ed.getExtentHeight() < 1)
    {
      ILogger.instance().logWarning("Tile too small for ancestor elevation data.");
      return null;
    }
  
    if ((_lastElevationDataProvider != null) && (_lastTileMeshResolutionX > 0) && (_lastTileMeshResolutionY > 0))
    {
      return new DecimatedSubviewElevationData(ed, _sector, new Vector2I(_lastTileMeshResolutionX, _lastTileMeshResolutionY));
    }
  
    ILogger.instance().logError("Can't create subview of elevation data from ancestor");
    return null;
  
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark ElevationData methods
