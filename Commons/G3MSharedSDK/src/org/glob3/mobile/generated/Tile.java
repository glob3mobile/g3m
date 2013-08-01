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
//class PlanetRendererContext;
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



public class Tile
{
  private TileTexturizer _texturizer;
  private Tile _parent;
  private final Sector _sector ;
  private final int _level;
  private final int _row;
  private final int _column;

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
  private double _minHeight;
  private double _maxHeight;

  private BoundingVolume _boundingVolume;

  private Mesh getTessellatorMesh(G3MRenderContext rc, PlanetRendererContext prc)
  {
  
    ElevationDataProvider elevationDataProvider = prc.getElevationDataProvider();
  
  //  const TileTessellator* tessellator = trc->getTessellator();
  //  const bool renderDebug = trc->getParameters()->_renderDebug;
  //  const Planet* planet = rc->getPlanet();
  //
  //  const LayerTilesRenderParameters* layerTilesRenderParameters = trc->getLayerTilesRenderParameters();
  //  const Vector2I tileMeshResolution(layerTilesRenderParameters->_tileMeshResolution);
  
    if ((_elevationData == null) && (elevationDataProvider != null))
    {
      initializeElevationData(elevationDataProvider, prc.getTessellator(), prc.getLayerTilesRenderParameters()._tileMeshResolution, rc.getPlanet(), prc.getParameters()._renderDebug);
    }
  
    if ((_tessellatorMesh == null) || _mustActualizeMeshDueToNewElevationData)
    {
      _mustActualizeMeshDueToNewElevationData = false;
  
      final LayerTilesRenderParameters layerTilesRenderParameters = prc.getLayerTilesRenderParameters();
  
      if (elevationDataProvider == null)
      {
        // no elevation data provider, just create a simple mesh without elevation
        _tessellatorMesh = prc.getTessellator().createTileMesh(rc.getPlanet(), layerTilesRenderParameters._tileMeshResolution, this, null, _verticalExaggeration, layerTilesRenderParameters._mercator, prc.getParameters()._renderDebug);
      }
      else
      {
        Mesh tessellatorMesh = prc.getTessellator().createTileMesh(rc.getPlanet(), layerTilesRenderParameters._tileMeshResolution, this, _elevationData, _verticalExaggeration, layerTilesRenderParameters._mercator, prc.getParameters()._renderDebug);
  
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
      }
    }
  
    return _tessellatorMesh;
  }

  private Mesh getDebugMesh(G3MRenderContext rc, PlanetRendererContext prc)
  {
    if (_debugMesh == null)
    {
      final LayerTilesRenderParameters layerTilesRenderParameters = prc.getLayerTilesRenderParameters();
      final Vector2I tileMeshResolution = new Vector2I(layerTilesRenderParameters._tileMeshResolution);
  
      //TODO: CHECK
      _debugMesh = prc.getTessellator().createTileDebugMesh(rc.getPlanet(), tileMeshResolution, this);
    }
    return _debugMesh;
  }

  private boolean isVisible(G3MRenderContext rc, PlanetRendererContext prc, Planet planet, Vector3D cameraNormalizedPosition, double cameraAngle2HorizonInRadians, Frustum cameraFrustumInModelCoordinates)
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
  
  
    // test if sector is back oriented with respect to the camera
    if (_sector.isBackOriented(rc, getMinHeight(), planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians))
    {
      return false;
    }
  
    final BoundingVolume boundingVolume = getBoundingVolume(rc, prc);
  
    return ((boundingVolume != null) && boundingVolume.touchesFrustum(cameraFrustumInModelCoordinates));
  }

  private ITimer _lodTimer;
  private boolean _lastLodTest;
  private boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRendererContext prc)
  {
    //  const TilesRenderParameters* parameters = trc->getParameters();
  
    final LayerTilesRenderParameters parameters = prc.getLayerTilesRenderParameters();
  
    if ((_level >= parameters._maxLevelForPoles) && (_sector.touchesPoles()))
    {
      return true;
    }
  
    if (_level >= parameters._maxLevel)
    {
      return true;
    }
  
    TileTexturizer texturizer = prc.getTexturizer();
    if (texturizer != null)
    {
      if (texturizer.tileMeetsRenderCriteria(this))
      {
        return true;
      }
    }
  
    if (prc.getParameters()._useTilesSplitBudget)
    {
      if (_subtiles == null) // the tile needs to create the subtiles
      {
        if (prc.getStatistics().getSplitsCountInFrame() > 1)
        {
          // there are not more splitsCount-budget to spend
          return true;
        }
  
        if (prc.getLastSplitTimer().elapsedTimeInMilliseconds() < 25)
        {
          // there are not more time-budget to spend
          return true;
        }
      }
    }
  
    //const Extent* extent = getTessellatorMesh(rc, trc)->getExtent();
    final Box boundingVolume = getTileBoundingVolume(rc);
    if (boundingVolume == null)
    {
      return true;
    }
  
    if ((_lodTimer != null) && (_lodTimer.elapsedTimeInMilliseconds() < 500))
    {
      return _lastLodTest;
    }
  
    if (_lodTimer == null)
    {
      _lodTimer = rc.getFactory().createTimer();
    }
    else
    {
      _lodTimer.start();
    }
  
    //  const double projectedSize = extent->squaredProjectedArea(rc);
    //  if (projectedSize <= (parameters->_tileTextureWidth * parameters->_tileTextureHeight * 2)) {
    //    return true;
    //  }
    final Vector2F ex = boundingVolume.projectedExtent(rc);
    final float t = (ex._x + ex._y);
    _lastLodTest = (t <= ((parameters._tileTextureResolution._x + parameters._tileTextureResolution._y) * 1.75f));
    return _lastLodTest;
  }

  private void rawRender(G3MRenderContext rc, PlanetRendererContext prc, GLState glState)
  {
    Mesh tessellatorMesh = getTessellatorMesh(rc, prc);
    if (tessellatorMesh == null)
    {
      return;
    }
  
    TileTexturizer texturizer = prc.getTexturizer();
    if (texturizer == null)
    {
      tessellatorMesh.render(rc, glState);
    }
    else
    {
      final boolean needsToCallTexturizer = (_texturizedMesh == null) || isTexturizerDirty();
  
      if (needsToCallTexturizer)
      {
        _texturizedMesh = texturizer.texturize(rc, prc, this, tessellatorMesh, _texturizedMesh);
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

  private void debugRender(G3MRenderContext rc, PlanetRendererContext prc, GLState glState)
  {
    Mesh debugMesh = getDebugMesh(rc, prc);
    if (debugMesh != null)
    {
      //debugMesh->render(rc);
      debugMesh.render(rc, glState);
    }
  }

  private Tile createSubTile(Angle lowerLat, Angle lowerLon, Angle upperLat, Angle upperLon, int level, int row, int column, boolean setParent)
  {
    Tile parent = setParent ? this : null;
    return new Tile(_texturizer, parent, new Sector(new Geodetic2D(lowerLat, lowerLon), new Geodetic2D(upperLat, upperLon)), level, row, column);
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

  private Box _tileBoundingVolume;
  private Box getTileBoundingVolume(G3MRenderContext rc)
  {
    if (_tileBoundingVolume == null)
    {
      final Planet planet = rc.getPlanet();
  
      final double minHeight = getMinHeight() * _verticalExaggeration;
      final double maxHeight = getMaxHeight() * _verticalExaggeration;
  
      final Vector3D v0 = planet.toCartesian(_sector._center, maxHeight);
      final Vector3D v1 = planet.toCartesian(_sector.getNE(), minHeight);
      final Vector3D v2 = planet.toCartesian(_sector.getNW(), minHeight);
      final Vector3D v3 = planet.toCartesian(_sector.getSE(), minHeight);
      final Vector3D v4 = planet.toCartesian(_sector.getSW(), minHeight);
  
      double lowerX = v0._x;
      if (v1._x < lowerX)
      {
         lowerX = v1._x;
      }
      if (v2._x < lowerX)
      {
         lowerX = v2._x;
      }
      if (v3._x < lowerX)
      {
         lowerX = v3._x;
      }
      if (v4._x < lowerX)
      {
         lowerX = v4._x;
      }
  
      double upperX = v0._x;
      if (v1._x > upperX)
      {
         upperX = v1._x;
      }
      if (v2._x > upperX)
      {
         upperX = v2._x;
      }
      if (v3._x > upperX)
      {
         upperX = v3._x;
      }
      if (v4._x > upperX)
      {
         upperX = v4._x;
      }
  
  
      double lowerY = v0._y;
      if (v1._y < lowerY)
      {
         lowerY = v1._y;
      }
      if (v2._y < lowerY)
      {
         lowerY = v2._y;
      }
      if (v3._y < lowerY)
      {
         lowerY = v3._y;
      }
      if (v4._y < lowerY)
      {
         lowerY = v4._y;
      }
  
      double upperY = v0._y;
      if (v1._y > upperY)
      {
         upperY = v1._y;
      }
      if (v2._y > upperY)
      {
         upperY = v2._y;
      }
      if (v3._y > upperY)
      {
         upperY = v3._y;
      }
      if (v4._y > upperY)
      {
         upperY = v4._y;
      }
  
  
      double lowerZ = v0._z;
      if (v1._z < lowerZ)
      {
         lowerZ = v1._z;
      }
      if (v2._z < lowerZ)
      {
         lowerZ = v2._z;
      }
      if (v3._z < lowerZ)
      {
         lowerZ = v3._z;
      }
      if (v4._z < lowerZ)
      {
         lowerZ = v4._z;
      }
  
      double upperZ = v0._z;
      if (v1._z > upperZ)
      {
         upperZ = v1._z;
      }
      if (v2._z > upperZ)
      {
         upperZ = v2._z;
      }
      if (v3._z > upperZ)
      {
         upperZ = v3._z;
      }
      if (v4._z > upperZ)
      {
         upperZ = v4._z;
      }
  
  
      _tileBoundingVolume = new Box(new Vector3D(lowerX, lowerY, lowerZ), new Vector3D(upperX, upperY, upperZ));
    }
    return _tileBoundingVolume;
  }

  private int _elevationDataLevel;
  private ElevationData _elevationData;
  private boolean _mustActualizeMeshDueToNewElevationData;
  private ElevationDataProvider _lastElevationDataProvider;
  private int _lastTileMeshResolutionX;
  private int _lastTileMeshResolutionY;

  private BoundingVolume getBoundingVolume(G3MRenderContext rc, PlanetRendererContext prc)
  {
    if (_boundingVolume == null)
    {
      Mesh mesh = getTessellatorMesh(rc, prc);
      if (mesh != null)
      {
  //      _boundingVolume = mesh->getBoundingVolume()->createSphere();
        _boundingVolume = mesh.getBoundingVolume();
      }
    }
    return _boundingVolume;
  }


  ///#include "Sphere.hpp"
  
  
  public Tile(TileTexturizer texturizer, Tile parent, Sector sector, int level, int row, int column)
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
     _tileBoundingVolume = null;
     _elevationData = null;
     _elevationDataLevel = -1;
     _elevationDataRequest = null;
     _minHeight = 0;
     _maxHeight = 0;
     _verticalExaggeration = 0F;
     _mustActualizeMeshDueToNewElevationData = false;
     _lastTileMeshResolutionX = -1;
     _lastTileMeshResolutionY = -1;
     _boundingVolume = null;
     _lodTimer = null;
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
  
    if (_tileBoundingVolume != null)
       _tileBoundingVolume.dispose();
    _tileBoundingVolume = null;
  
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
  
    if (_lodTimer != null)
       _lodTimer.dispose();
  }


  public final Sector getSector()
  {
    return _sector;
  }

  public final int getLevel()
  {
    return _level;
  }

  public final int getRow()
  {
    return _row;
  }

  public final int getColumn()
  {
    return _column;
  }

  public final Mesh getTexturizedMesh()
  {
    return _texturizedMesh;
  }

  public final Tile getParent()
  {
    return _parent;
  }

  public final void prepareForFullRendering(G3MRenderContext rc, PlanetRendererContext prc)
  {
    Mesh tessellatorMesh = getTessellatorMesh(rc, prc);
    if (tessellatorMesh == null)
    {
      return;
    }
  
    TileTexturizer texturizer = prc.getTexturizer();
    if (texturizer != null)
    {
      final boolean needsToCallTexturizer = (_texturizedMesh == null) || isTexturizerDirty();
  
      if (needsToCallTexturizer)
      {
        _texturizedMesh = texturizer.texturize(rc, prc, this, tessellatorMesh, _texturizedMesh);
      }
    }
  }

  public final void render(G3MRenderContext rc, PlanetRendererContext prc, GLState parentState, java.util.LinkedList<Tile> toVisitInNextIteration, Planet planet, Vector3D cameraNormalizedPosition, double cameraAngle2HorizonInRadians, Frustum cameraFrustumInModelCoordinates)
  {
  
    final float verticalExaggeration = prc.getVerticalExaggeration();
    if (verticalExaggeration != _verticalExaggeration)
    {
      // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
  
      _verticalExaggeration = prc.getVerticalExaggeration();
    }
  
    TilesStatistics statistics = prc.getStatistics();
    statistics.computeTileProcessed(this);
  
    if (isVisible(rc, prc, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates))
    {
      setIsVisible(true, prc.getTexturizer());
  
      statistics.computeVisibleTile(this);
  
      final boolean isRawRender = ((toVisitInNextIteration == null) || meetsRenderCriteria(rc, prc) || (prc.getParameters()._incrementalTileQuality && !_textureSolved));
  
      if (isRawRender)
      {
        rawRender(rc, prc, parentState);
        if (prc.getParameters()._renderDebug)
        {
          debugRender(rc, prc, parentState);
        }
  
        statistics.computePlanetRenderered(this);
  
        prune(prc.getTexturizer(), prc.getElevationDataProvider());
        //TODO: AVISAR CAMBIO DE TERRENO
      }
      else
      {
        final Geodetic2D lower = _sector._lower;
        final Geodetic2D upper = _sector._upper;
  
        final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
  
        final Angle splitLatitude = prc.getLayerTilesRenderParameters()._mercator ? MercatorUtils.calculateSplitLatitude(lower._latitude, upper._latitude) : Angle.midAngle(lower._latitude, upper._latitude);
        /*                               */
        /*                               */
  
        java.util.ArrayList<Tile> subTiles = getSubTiles(splitLatitude, splitLongitude);
        if (_justCreatedSubtiles)
        {
          prc.getLastSplitTimer().start();
          statistics.computeSplitInFrame();
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
      setIsVisible(false, prc.getTexturizer());
  
      prune(prc.getTexturizer(), prc.getElevationDataProvider());
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

  public final Tile getDeepestTileContaining(Geodetic3D position)
  {
    if (_sector.contains(position))
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
  
      //    printf("= pruned tile %s\n", getKey().description().c_str());
  
      //    TileTexturizer* texturizer = (trc == NULL) ? NULL : trc->getTexturizer();
  
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
  
        //      if (elevationDataProvider != NULL) {
        //        //subtile->cancelElevationDataRequest(elevationDataProvider);
        //      }
  
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


  public final double getMinHeight()
  {
    return _minHeight;
  }
  public final double getMaxHeight()
  {
    return _maxHeight;
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

  public final java.util.ArrayList<Tile> createSubTiles(Angle splitLatitude, Angle splitLongitude, boolean setParent)
  {
    final Geodetic2D lower = _sector._lower;
    final Geodetic2D upper = _sector._upper;
  
    final int nextLevel = _level + 1;
  
    final int row2 = 2 * _row;
    final int column2 = 2 * _column;
  
    java.util.ArrayList<Tile> subTiles = new java.util.ArrayList<Tile>();
  
    subTiles.add(createSubTile(lower._latitude, lower._longitude, splitLatitude, splitLongitude, nextLevel, row2, column2, setParent));
  
    subTiles.add(createSubTile(lower._latitude, splitLongitude, splitLatitude, upper._longitude, nextLevel, row2, column2 + 1, setParent));
  
    subTiles.add(createSubTile(splitLatitude, lower._longitude, upper._latitude, splitLongitude, nextLevel, row2 + 1, column2, setParent));
  
    subTiles.add(createSubTile(splitLatitude, splitLongitude, upper._latitude, upper._longitude, nextLevel, row2 + 1, column2 + 1, setParent));
  
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
        setElevationData(subView, ancestor.getLevel());
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
  //    const Sector caceresSector = Sector::fromDegrees(39.4642996294239623,
  //                                                     -6.3829977122432933,
  //                                                     39.4829891936013553,
  //                                                     -6.3645288909498845);
  //
  //    if (caceresSector.touchesWith(_sector)) {
  //      printf("break point on me\n");
  //    }
  
      final Vector2I res = tessellator.getTileMeshResolution(planet, tileMeshResolution, this, renderDebug);
      _elevationDataRequest = new TileElevationDataRequest(this, res, elevationDataProvider);
      _elevationDataRequest.sendRequest();
    }
  
    //If after petition we still have no data we request from ancestor
    if (_elevationData == null)
    {
      getElevationDataFromAncestor(tileMeshResolution);
    }
  
  }

  public final void ancestorChangedElevationData(Tile ancestor)
  {
  
    if (ancestor.getLevel() > _elevationDataLevel)
    {
      ElevationData subView = createElevationDataSubviewFromAncestor(ancestor);
      if (subView != null)
      {
        setElevationData(subView, ancestor.getLevel());
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
  //    ElevationData* subView = _lastElevationDataProvider->createSubviewOfElevationData(ed,
  //                                                                                      getSector(),
  //                                                                                      Vector2I(_lastTileMeshResolutionX, _lastTileMeshResolutionY));
  //    return subView;
  
      return new DecimatedSubviewElevationData(ed, getSector(), new Vector2I(_lastTileMeshResolutionX, _lastTileMeshResolutionY));
                                               //bool ownsElevationData,
    }
  
    ILogger.instance().logError("Can't create subview of elevation data from ancestor");
    return null;
  
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark ElevationData methods
