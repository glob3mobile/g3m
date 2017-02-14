package org.glob3.mobile.generated;
//
//  Tile.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//

//
//  Tile.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//




//class TileTexturizer;
//class Mesh;
//class TileElevationDataRequest;
//class DEMSubscription;
//class GLState;
//class ITexturizerData;
//class PlanetTileTessellatorData;
//class ElevationDataProvider;
//class PlanetRenderer;
//class TileData;
//class TilesStatistics;
//class Geodetic3D;
//class Vector2I;


public class Tile
{
  private TileTexturizer _texturizer;
  private Tile _parent;

  private Mesh _tessellatorMesh;

  private Mesh _debugMesh;
  private Mesh _texturizedMesh;
  private TileElevationDataRequest _elevationDataRequest;
  private DEMSubscription _demSubscription;

  private boolean _textureSolved;
  private java.util.ArrayList<Tile> _subtiles;
  private boolean _justCreatedSubtiles;

  private boolean _texturizerDirty;

  private TileTessellatorMeshData _tileTessellatorMeshData = new TileTessellatorMeshData();

  private Mesh getDebugMesh(G3MRenderContext rc, PlanetRenderContext prc)
  {
    if (_debugMesh == null)
    {
      _debugMesh = prc._tessellator.createTileDebugMesh(rc, prc, this);
    }
    return _debugMesh;
  }

  private void rawRender(G3MRenderContext rc, PlanetRenderContext prc, GLState glState)
  {
  
    Mesh tessellatorMesh = getTessellatorMesh(rc, prc);
    if (tessellatorMesh == null)
    {
      return;
    }
  
  //  tessellatorMesh->getBoundingVolume()->render(rc,
  //                                               glState,
  //                                               Color::white());
  
    if (prc._texturizer == null)
    {
      tessellatorMesh.render(rc, glState);
    }
    else
    {
      final boolean needsToCallTexturizer = (_texturizedMesh == null) || isTexturizerDirty();
  
      if (needsToCallTexturizer)
      {
        _texturizedMesh = prc._texturizer.texturize(rc, prc, this, tessellatorMesh, _texturizedMesh);
      }
  
      if (_texturizedMesh == null)
      {
        tessellatorMesh.render(rc, glState);
      }
      else
      {
        _texturizedMesh.render(rc, glState);
      }
    }
  }

  private void debugRender(G3MRenderContext rc, PlanetRenderContext prc, GLState glState)
  {
    Mesh debugMesh = getDebugMesh(rc, prc);
    if (debugMesh != null)
    {
      debugMesh.render(rc, glState);
    }
  }

  private Tile createSubTile(Sector sector, int level, int row, int column, boolean setParent)
  {
    Tile parent = setParent ? this : null;
    return new Tile(_texturizer, parent, sector, _mercator, level, row, column, _planetRenderer);
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
  private PlanetTileTessellatorData _planetTileTessellatorData;

  private int _elevationDataLevel;
  private ElevationData _elevationData;
  private boolean _mustActualizeMeshDueToNewElevationData;
  private DEMGrid _grid;
  private ElevationDataProvider _lastElevationDataProvider;
  private int _lastTileMeshResolutionX;
  private int _lastTileMeshResolutionY;

  private final PlanetRenderer _planetRenderer;


  ///#include "BoundingVolume.hpp"
  ///#include "Color.hpp"
  
  
  private static String createTileID(int level, int row, int column)
  {
    return level + "/" + row + "/" + column;
  }

  private TileData[] _data;
  private int _dataSize;



  private static class TerrainListener implements DEMListener
  {
    private Tile _tile;
    public TerrainListener(Tile tile)
    {
       _tile = tile;
    }

    public void dispose()
    {
    }

    public final void onGrid(DEMGrid grid)
    {
      _tile.onGrid(grid);
    }

  }


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
     _grid = null;
     _demSubscription = null;
     _mustActualizeMeshDueToNewElevationData = false;
     _lastTileMeshResolutionX = -1;
     _lastTileMeshResolutionY = -1;
     _planetRenderer = planetRenderer;
     _planetTileTessellatorData = null;
     _id = createTileID(level, row, column);
     _data = null;
     _dataSize = 0;
  }

  public void dispose()
  {
    //  prune(NULL, NULL);
  
    if (_debugMesh != null)
       _debugMesh.dispose();
  
    if (_tessellatorMesh != null)
       _tessellatorMesh.dispose();
  
    if (_texturizerData != null)
       _texturizerData.dispose();
  
    if (_texturizedMesh != null)
       _texturizedMesh.dispose();
  
    if (_elevationData != null)
       _elevationData.dispose();
  
    if (_grid != null)
    {
      _grid._release();
    }
  
    if (_elevationDataRequest != null)
    {
      _elevationDataRequest.cancelRequest(); //The listener will auto delete
      if (_elevationDataRequest != null)
         _elevationDataRequest.dispose();
    }
  
    if (_demSubscription != null)
    {
      _demSubscription.cancel();
      _demSubscription._release();
      _demSubscription = null;
    }
  
    if (_planetTileTessellatorData != null)
       _planetTileTessellatorData.dispose();
  
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

  public final void prepareForFullRendering(G3MRenderContext rc, PlanetRenderContext prc)
  {
  
    Mesh tessellatorMesh = getTessellatorMesh(rc, prc);
    if (tessellatorMesh == null)
    {
      return;
    }
  
    if (prc._texturizer != null)
    {
      final boolean needsToCallTexturizer = (_texturizedMesh == null) || isTexturizerDirty();
  
      if (needsToCallTexturizer)
      {
        _texturizedMesh = prc._texturizer.texturize(rc, prc, this, tessellatorMesh, _texturizedMesh);
      }
    }
  }

  public final void render(G3MRenderContext rc, PlanetRenderContext prc, GLState parentState, TilesStatistics tilesStatistics, java.util.ArrayList<Tile> toVisitInNextIteration)
  {
  
    final boolean visible = prc._tileVisibilityTester.isVisible(rc, prc, this);
    boolean rendered = false;
    if (visible)
    {
      setIsVisible(true, prc._texturizer);
  
      rendered = ((toVisitInNextIteration == null) || prc._tileLODTester.meetsRenderCriteria(rc, prc, this) || (prc._tilesRenderParameters._incrementalTileQuality && !_textureSolved));
  
      if (rendered)
      {
        if (prc._renderTileMeshes)
        {
          rawRender(rc, prc, parentState);
        }
        if (prc._tilesRenderParameters._renderDebug)
        {
          debugRender(rc, prc, parentState);
        }
  
        prune(prc._texturizer, prc._elevationDataProvider);
        //TODO: AVISAR CAMBIO DE TERRENO
      }
      else
      {
        java.util.ArrayList<Tile> subTiles = getSubTiles();
        if (_justCreatedSubtiles)
        {
          prc._lastSplitTimer.start();
          _justCreatedSubtiles = false;
        }
  
        if (toVisitInNextIteration != null)
        {
          final int subTilesSize = subTiles.size();
          for (int i = 0; i < subTilesSize; i++)
          {
            Tile subTile = subTiles.get(i);
            toVisitInNextIteration.add(subTile);
          }
        }
      }
    }
    else
    {
      setIsVisible(false, prc._texturizer);
  
      prune(prc._texturizer, prc._elevationDataProvider);
      //TODO: AVISAR CAMBIO DE TERRENO
    }
  
    tilesStatistics.computeTileProcessed(this, visible, rendered);
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

  public final PlanetTileTessellatorData getPlanetTileTessellatorData()
  {
    return _planetTileTessellatorData;
  }

  public final void setPlanetTileTessellatorData(PlanetTileTessellatorData planetTileTessellatorData)
  {
    if (planetTileTessellatorData != _planetTileTessellatorData)
    {
      if (_planetTileTessellatorData != null)
         _planetTileTessellatorData.dispose();
      _planetTileTessellatorData = planetTileTessellatorData;
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
  
      final int subtilesSize = _subtiles.size();
      for (int i = 0; i < subtilesSize; i++)
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
  
    if (_demSubscription != null)
    {
      _demSubscription.cancel();
      _demSubscription._release();
      _demSubscription = null;
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
  
    final Sector s0 = new Sector(new Geodetic2D(lower._latitude, lower._longitude), new Geodetic2D(splitLatitude, splitLongitude));
    if (renderedSector == null || renderedSector.touchesWith(s0))
    {
      subTiles.add(createSubTile(s0, nextLevel, row2, column2, setParent));
    }
  
    final Sector s1 = new Sector(new Geodetic2D(lower._latitude, splitLongitude), new Geodetic2D(splitLatitude, upper._longitude));
    if (renderedSector == null || renderedSector.touchesWith(s1))
    {
      subTiles.add(createSubTile(s1, nextLevel, row2, column2 + 1, setParent));
    }
  
    final Sector s2 = new Sector(new Geodetic2D(splitLatitude, lower._longitude), new Geodetic2D(upper._latitude, splitLongitude));
    if (renderedSector == null || renderedSector.touchesWith(s2))
    {
      subTiles.add(createSubTile(s2, nextLevel, row2 + 1, column2, setParent));
    }
  
    final Sector s3 = new Sector(new Geodetic2D(splitLatitude, splitLongitude), new Geodetic2D(upper._latitude, upper._longitude));
    if (renderedSector == null || renderedSector.touchesWith(s3))
    {
      subTiles.add(createSubTile(s3, nextLevel, row2 + 1, column2 + 1, setParent));
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

  public final void onGrid(DEMGrid grid)
  {
    if (grid != _grid)
    {
      if (_grid != null)
      {
        _grid._release();
      }
      _grid = grid;
      _mustActualizeMeshDueToNewElevationData = true;
    }
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

  public final void initializeElevationData(G3MRenderContext rc, PlanetRenderContext prc)
  {
  
    final Vector2S tileMeshResolution = prc._layerTilesRenderParameters._tileMeshResolution;
  
    //Storing for subviewing
    _lastElevationDataProvider = prc._elevationDataProvider;
    _lastTileMeshResolutionX = tileMeshResolution._x;
    _lastTileMeshResolutionY = tileMeshResolution._y;
    if (_elevationDataRequest == null)
    {
  
      final Vector2S res = prc._tessellator.getTileMeshResolution(rc, prc, this);
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning should ElevationData res should be short?
      _elevationDataRequest = new TileElevationDataRequest(this, res.asVector2I(), prc._elevationDataProvider);
      _elevationDataRequest.sendRequest();
    }
  
    //If after petition we still have no data we request from ancestor (provider asynchronous)
    if (_elevationData == null)
    {
      getElevationDataFromAncestor(tileMeshResolution.asVector2I());
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

  public final Vector2I getNormalizedPixelFromPosition(Geodetic2D position, Vector2I tileDimension)
  {
    final IMathUtils math = IMathUtils.instance();
    final Vector2D uv = _sector.getUVCoordinates(position);
    return new Vector2I(math.toInt(tileDimension._x * uv._x), math.toInt(tileDimension._y * uv._y));
  }

  public final TileData getData(int id)
  {
    return (id >= _dataSize) ? null : _data[id];
  }

  public final void setData(TileData data)
  {
    final int id = data._id;
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

  public final void clearDataWithID(int id)
  {
    if (_dataSize > id)
    {
      //Assuming we won't reduce _data capacity by erasing items
      if (_data[id] != null)
         _data[id].dispose();
      _data[id] = null;
    }
  }

  public final TileTessellatorMeshData getTileTessellatorMeshData()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning ask JM
    return _tileTessellatorMeshData;
  }

  public final Mesh getTessellatorMesh(G3MRenderContext rc, PlanetRenderContext prc)
  {
  
    ElevationDataProvider elevationDataProvider = prc._elevationDataProvider;
  
    if ((_elevationData == null) && (elevationDataProvider != null) && (elevationDataProvider.isEnabled()))
    {
      initializeElevationData(rc, prc);
    }
  
    DEMProvider demProvider = prc._demProvider;
    if (demProvider != null)
    {
      if (_demSubscription == null)
      {
        final Vector2S tileMeshResolution = prc._layerTilesRenderParameters._tileMeshResolution;
  
        _demSubscription = demProvider.subscribe(_sector, tileMeshResolution, new TerrainListener(this), true);
      }
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
  
  //    if (elevationDataProvider == NULL) {
  //      // no elevation data provider, just create a simple mesh without elevation
  //      _tessellatorMesh = prc->_tessellator->createTileMesh(rc,
  //                                                           prc,
  //                                                           this,
  //                                                           NULL,
  //                                                           _tileTessellatorMeshData);
  //    }
  //    else {
      Mesh tessellatorMesh = prc._tessellator.createTileMesh(rc, prc, this, _elevationData, _grid, _tileTessellatorMeshData);
  
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
  //    }
  
      //Notifying when the tile is first created and every time the elevation data changes
      _planetRenderer.sectorElevationChanged(_elevationData);
    }
  
    return _tessellatorMesh;
  }

  public final boolean hasSubtiles()
  {
    return (_subtiles != null);
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark ElevationData methods
