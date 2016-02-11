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


///#include "TileLODTester.hpp"

//class TileTexturizer;
//class Mesh;
//class TileElevationDataRequest;
//class Vector3D;
//class TilesRenderParameters;
//class LayerTilesRenderParameters;
//class Frustum;
//class TilesStatistics;
//class ElevationDataProvider;
//class ITimer;
//class GLState;
//class LayerSet;
//class ITexturizerData;
//class PlanetTileTessellatorData;
//class PlanetRenderer;
//class TileKey;
//class Geodetic3D;
//class TileLODTester;
//class TileData;
//class TileVisibilityTester;


public class Tile
{
  private TileTexturizer _texturizer;
  private Tile _parent;

  private Mesh _tessellatorMesh;
  private boolean _tessellatorMeshIsMeshHolder;

  private Mesh _debugMesh;
  private Mesh _texturizedMesh;
  private TileElevationDataRequest _elevationDataRequest;

  private Mesh _flatColorMesh;

  private boolean _textureSolved;
  private java.util.ArrayList<Tile> _subtiles;
  private boolean _justCreatedSubtiles;

  private boolean _texturizerDirty;

  private float _verticalExaggeration;
  private TileTessellatorMeshData _tileTessellatorMeshData = new TileTessellatorMeshData();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void prepareTestLODData(Planet planet);

  private Mesh getTessellatorMesh(G3MRenderContext rc, ElevationDataProvider elevationDataProvider, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters, TilesRenderParameters tilesRenderParameters)
  {
  
  
    if ((_elevationData == null) && (elevationDataProvider != null) && (elevationDataProvider.isEnabled()))
    {
      initializeElevationData(elevationDataProvider, tessellator, layerTilesRenderParameters._tileMeshResolution, rc.getPlanet(), tilesRenderParameters._renderDebug);
    }
  
    if ((_tessellatorMesh == null) || _mustActualizeMeshDueToNewElevationData)
    {
      _mustActualizeMeshDueToNewElevationData = false;
  
      _planetRenderer.onTileHasChangedMesh(this);
  
      if (_debugMesh != null)
      {
        if (_debugMesh != null)
           _debugMesh.dispose();
        _debugMesh = null;
      }
  
      if (elevationDataProvider == null)
      {
        // no elevation data provider, just create a simple mesh without elevation
        _tessellatorMesh = tessellator.createTileMesh(rc.getPlanet(), layerTilesRenderParameters._tileMeshResolution, this, null, _verticalExaggeration, tilesRenderParameters._renderDebug, _tileTessellatorMeshData);
        _tessellatorMeshIsMeshHolder = false;
      }
      else
      {
        Mesh tessellatorMesh = tessellator.createTileMesh(rc.getPlanet(), layerTilesRenderParameters._tileMeshResolution, this, _elevationData, _verticalExaggeration, tilesRenderParameters._renderDebug, _tileTessellatorMeshData);
  
        MeshHolder meshHolder = (MeshHolder) _tessellatorMesh;
        if (meshHolder == null)
        {
          meshHolder = new MeshHolder(tessellatorMesh);
          _tessellatorMesh = meshHolder;
          _tessellatorMeshIsMeshHolder = true;
        }
        else
        {
          meshHolder.setMesh(tessellatorMesh);
        }
  
        //      computeTileCorners(rc->getPlanet());
      }
  
      //Notifying when the tile is first created and every time the elevation data changes
      _planetRenderer.sectorElevationChanged(_elevationData);
    }
  
    return _tessellatorMesh;
  }

  private Mesh getDebugMesh(G3MRenderContext rc, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters)
  {
    if (_debugMesh == null)
    {
      final Vector2I tileMeshResolution = new Vector2I(layerTilesRenderParameters._tileMeshResolution);
  
      _debugMesh = tessellator.createTileDebugMesh(rc.getPlanet(), tileMeshResolution, this);
    }
    return _debugMesh;
  }

  private boolean isVisible(G3MRenderContext rc, Sector renderedSector, TileVisibilityTester tileVisibilityTester, long nowInMS, Frustum frustumInModelCoordinates)
  {
    if ((renderedSector != null) && !renderedSector.touchesWith(_sector)) //Incomplete world
    {
      return false;
    }
  
    return tileVisibilityTester.isVisible(this, rc, nowInMS, frustumInModelCoordinates);
  }

  private boolean meetsRenderCriteria(G3MRenderContext rc, TileLODTester tileLODTester, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, double texWidthSquared, double texHeightSquared, long nowInMS)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: move to an implementation of TileLODTester && remove this method when the code is moved from here
  //  if (tilesRenderParameters->_useTilesSplitBudget) {
  //    if (_subtiles == NULL) { // the tile needs to create the subtiles
  ////      if (lastSplitTimer->elapsedTimeInMilliseconds() < 67) {
  //      if (lastSplitTimer->elapsedTimeInMilliseconds() < 5) {
  //        // there are not more time-budget to spend
  //        return true;
  //      }
  //    }
  //  }
  
    return tileLODTester.meetsRenderCriteria(this, rc, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS);
  }

  private void rawRender(G3MRenderContext rc, GLState glState, TileTexturizer texturizer, ElevationDataProvider elevationDataProvider, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters, LayerSet layerSet, TilesRenderParameters tilesRenderParameters, boolean forceFullRender, long tileDownloadPriority, boolean logTilesPetitions)
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
        _texturizedMesh = texturizer.texturize(rc, tessellator, layerTilesRenderParameters, layerSet, forceFullRender, tileDownloadPriority, this, tessellatorMesh, _texturizedMesh, logTilesPetitions);
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
      }
    }
  }

  private void debugRender(G3MRenderContext rc, GLState glState, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters)
  {
    Mesh debugMesh = getDebugMesh(rc, tessellator, layerTilesRenderParameters);
    if (debugMesh != null)
    {
      debugMesh.render(rc, glState);
    }
  }

  private Tile createSubTile(Angle lowerLat, Angle lowerLon, Angle upperLat, Angle upperLon, int level, int row, int column, boolean setParent)
  {
    Tile parent = setParent ? this : null;
    return new Tile(_texturizer, parent, new Sector(new Geodetic2D(lowerLat, lowerLon), new Geodetic2D(upperLat, upperLon)), _mercator, level, row, column, _planetRenderer);
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

  private boolean _rendered;

  private static String createTileId(int level, int row, int column)
  {
    return level + "/" + row + "/" + column;
  }

  private TileData[] _data;
  private int _dataSize;

  public final Sector _sector ;
  public final boolean _mercator;
  public final int _level;
  public final int _row;
  public final int _column;
  public final String _id;

  public Tile(TileTexturizer texturizer, Tile parent, Sector sector, boolean mercator, int level, int row, int column, PlanetRenderer planetRenderer)
  {
     _texturizer = texturizer;
     _parent = parent;
     _sector = new Sector(sector);
     _mercator = mercator;
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
     _planetRenderer = planetRenderer;
     _tessellatorData = null;
     _rendered = false;
     _id = createTileId(level, row, column);
     _tessellatorMeshIsMeshHolder = false;
     _data = null;
     _dataSize = 0;
  }

  public void dispose()
  {
    //  prune(NULL, NULL);
  
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
  
    for (int i = 0; i < _dataSize; i++)
    {
      TileData data = _data[i];
      if (data != null)
         data.dispose();
    }
    _data = null;
  }

  //Change to public for TileCache
  public final java.util.ArrayList<Tile> getSubTiles()
  {
    if (_subtiles == null)
    {
      _subtiles = createSubTiles(true);
    }
  
    return _subtiles;
  }

  public final Mesh getTexturizedMesh()
  {
    return _texturizedMesh;
  }

  public final Tile getParent()
  {
    return _parent;
  }

  public final void prepareForFullRendering(G3MRenderContext rc, TileTexturizer texturizer, ElevationDataProvider elevationDataProvider, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters, LayerSet layerSet, TilesRenderParameters tilesRenderParameters, boolean forceFullRender, long tileDownloadPriority, float verticalExaggeration, boolean logTilesPetitions)
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
  
    if (texturizer != null)
    {
      final boolean needsToCallTexturizer = (_texturizedMesh == null) || isTexturizerDirty();
  
      if (needsToCallTexturizer)
      {
        _texturizedMesh = texturizer.texturize(rc, tessellator, layerTilesRenderParameters, layerSet, forceFullRender, tileDownloadPriority, this, tessellatorMesh, _texturizedMesh, logTilesPetitions);
      }
    }
  }

  public final void render(G3MRenderContext rc, GLState parentState, java.util.ArrayList<Tile> toVisitInNextIteration, TileLODTester tileLODTester, TileVisibilityTester tileVisibilityTester, Frustum frustumInModelCoordinates, TilesStatistics tilesStatistics, float verticalExaggeration, LayerTilesRenderParameters layerTilesRenderParameters, TileTexturizer texturizer, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, ElevationDataProvider elevationDataProvider, TileTessellator tessellator, LayerSet layerSet, Sector renderedSector, boolean forceFullRender, long tileDownloadPriority, double texWidthSquared, double texHeightSquared, long nowInMS, boolean renderTileMeshes, boolean logTilesPetitions)
  {
  ///#warning REMOVE
  //  if (!_sector.contains(Angle::fromDegrees(28), Angle::fromDegrees(-15))) {
  //    return;
  //  }
  
  
    tilesStatistics.computeTileProcessed(this);
  
    if (verticalExaggeration != _verticalExaggeration)
    {
      // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
      _verticalExaggeration = verticalExaggeration;
    }
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO Remove: Forcing tessellator mesh generation before visibility test
    getTessellatorMesh(rc, elevationDataProvider, tessellator, layerTilesRenderParameters, tilesRenderParameters);
  
    boolean rendered = false;
    if (isVisible(rc, renderedSector, tileVisibilityTester, nowInMS, frustumInModelCoordinates))
    {
      setIsVisible(true, texturizer);
  
      tilesStatistics.computeVisibleTile(this);
  
      final boolean isRawRender = ((toVisitInNextIteration == null) || meetsRenderCriteria(rc, tileLODTester, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS) || (tilesRenderParameters._incrementalTileQuality && !_textureSolved));
  
      if (isRawRender)
      {
        final long tileTexturePriority = (tilesRenderParameters._incrementalTileQuality ? tileDownloadPriority + layerTilesRenderParameters._maxLevel - _level : tileDownloadPriority + _level);
  
        rendered = true;
        if (renderTileMeshes)
        {
          rawRender(rc, parentState, texturizer, elevationDataProvider, tessellator, layerTilesRenderParameters, layerSet, tilesRenderParameters, forceFullRender, tileTexturePriority, logTilesPetitions);
        }
        if (tilesRenderParameters._renderDebug)
        {
          debugRender(rc, parentState, tessellator, layerTilesRenderParameters);
        }
  
        tilesStatistics.computeTileRenderered(this);
  
        prune(texturizer, elevationDataProvider);
        //TODO: AVISAR CAMBIO DE TERRENO
      }
      else
      {
        java.util.ArrayList<Tile> subTiles = getSubTiles();
        if (_justCreatedSubtiles)
        {
          lastSplitTimer.start();
          _justCreatedSubtiles = false;
        }
  
        final int subTilesSize = subTiles.size();
        for (int i = 0; i < subTilesSize; i++)
        {
          Tile subTile = subTiles.get(i);
          toVisitInNextIteration.add(subTile);
        }
      }
    }
    else
    {
      setIsVisible(false, texturizer);
  
      prune(texturizer, elevationDataProvider);
      //TODO: AVISAR CAMBIO DE TERRENO
    }
  
    if (_rendered != rendered)
    {
      _rendered = rendered;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: Is it needed?
    }
  
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
    prune(texturizer, elevationDataProvider);
  
    if (texturizer != null)
    {
      texturizer.tileToBeDeleted(this, _texturizedMesh);
    }
  
    if (elevationDataProvider != null)
    {
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

  public final java.util.ArrayList<Tile> createSubTiles(boolean setParent)
  {
    _justCreatedSubtiles = true;
  
    final Geodetic2D lower = _sector._lower;
    final Geodetic2D upper = _sector._upper;
    final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
  
    final Angle splitLatitude = _mercator ? MercatorUtils.calculateSplitLatitude(lower._latitude, upper._latitude) : Angle.midAngle(lower._latitude, upper._latitude);
    /*                               */
    /*                               */
  
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
  
    subTiles.trimToSize();
  
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

  public final Vector2I getNormalizedPixelsFromPosition(Geodetic2D position2D, Vector2I tileDimension)
  {
    final IMathUtils math = IMathUtils.instance();
    final Vector2D uv = _sector.getUVCoordinates(position2D);
    return new Vector2I(math.toInt(tileDimension._x * uv._x), math.toInt(tileDimension._y * uv._y));
  }

  public final Mesh getTessellatorMesh()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: remove this method && _tessellatorMeshIsMeshHolder variable
    if (_tessellatorMeshIsMeshHolder)
    {
      return ((MeshHolder) _tessellatorMesh).getMesh();
    }
  
    return _tessellatorMesh;
  }

  public final TileData getData(int id)
  {
    return (id >= _dataSize) ? null : _data[id];
  }
  public final void setData(int id, TileData data)
  {
    final int requiredSize = id+1;
    if (_dataSize < requiredSize)
    {
      if (_dataSize == 0)
      {
        _data = new TileData[requiredSize];
        _dataSize = requiredSize;
      }
      else
      {
        TileData[] oldData = _data;
        final int oldDataSize = _dataSize;
        _data = new TileData[requiredSize];
        _dataSize = requiredSize;
        System.arraycopy(oldData, 0, _data, 0, oldDataSize);
      }
    }
  
    TileData current = _data[id];
    if (current != data)
    {
      if (current != null)
         current.dispose();
      _data[id] = data;
    }
  }

  public final TileTessellatorMeshData getTessellatorMeshData()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning ask JM
    return _tileTessellatorMeshData;
  }

  public final Mesh getCurrentTessellatorMesh()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: remove this method
    return _tessellatorMesh;
  }

  public final boolean areSubtilesCreated()
  {
    return _subtiles != null;
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark ElevationData methods
