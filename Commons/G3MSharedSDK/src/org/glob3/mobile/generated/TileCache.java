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




//class TileTexturizer;
//class Mesh;
//class TileElevationDataRequest;
//class BoundingVolume;
//class Vector3D;
//class TilesRenderParameters;
//class LayerTilesRenderParameters;
//class Frustum;
//class TilesStatistics;
//class ElevationDataProvider;
//class ITimer;
//class GLState;
//class TileRasterizer;
//class LayerSet;
//class ITexturizerData;
//class PlanetTileTessellatorData;
//class PlanetRenderer;
//class TileRenderingListener;
//class TileKey;
//class Geodetic3D;

public class TileCache
{
  private int _maxSize;


  private java.util.ArrayList<Tile> _tileCache = new java.util.ArrayList<Tile>();


  public TileCache(int maxSize)
  {
     _maxSize = maxSize;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setTileCacheSize(int size);
  public final void cropTileCache(int size)
  {
    while (_tileCache.size() > size)
    {
      Tile t = _tileCache.get(0);
      _tileCache.remove(0);
  
      TileTexturizer texturizer = t.getTexturizer();
      if (texturizer != null)
      {
        texturizer.tileToBeDeleted(t, t.getTexturizedMesh());
  
        t.deleteTexturizedMesh(texturizer);
      }
  
      t.dispose();
  
      t = null;
  
    }
  }
  public final void clearTile(Tile tile)
  {
  
    _tileCache.add(tile);
    cropTileCache(_maxSize);
  }
  public final Tile getSubTileFromCache(int level, int row, int column)
  {
  
    for (java.util.Iterator<Tile> it = _tileCache.iterator(); it.hasNext();)
    {
      Tile tile = it.next();
      if (tile._level == level && tile._row == row && tile._column == column)
      {
        _tileCache.remove(tile);
        return tile;
      }
    }
  
    return null;
  }

  public final boolean has4SubTilesCached(Tile tile)
  {
  
    final int nextLevel = tile._level + 1;
  
    final int row2 = 2 * tile._row;
    final int column2 = 2 * tile._column;
  
    int nSubtiles = 0;
  
    for (java.util.Iterator<Tile> it = _tileCache.iterator(); it.hasNext();)
    {
      Tile t = it.next();
      if (t._level == nextLevel)
      {
        if ((t._row == row2 && t._column == column2) || (t._row == row2+1 && t._column == column2) || (t._row == row2 && t._column == column2+1) || (t._row == row2+1 && t._column == column2+1))
        {
          nSubtiles++;
          if (nSubtiles == 4)
          {
            return true;
          }
        }
      }
    }
  
    return false;
  }
}