package org.glob3.mobile.generated;
//
//  PlanetRenderContext.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/4/16.
//
//

//
//  PlanetRenderContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/4/16.
//
//


//class TileLODTester;
//class TileVisibilityTester;
//class Frustum;
//class LayerTilesRenderParameters;
//class TileTexturizer;
//class TilesRenderParameters;
//class ITimer;
//class ElevationDataProvider;
//class DEMProvider;
//class TileTessellator;
//class LayerSet;

public class PlanetRenderContext
{
  public TileLODTester _tileLODTester;
  public TileVisibilityTester _tileVisibilityTester;
  public float _verticalExaggeration;
  public TileTexturizer _texturizer;
  public ITimer _lastSplitTimer;
  public ElevationDataProvider _elevationDataProvider;
  public DEMProvider _demProvider;
  public TileTessellator _tessellator;
  public LayerSet _layerSet;
  public long _tileTextureDownloadPriority;
  public double _texWidthSquared;
  public double _texHeightSquared;
  public long _nowInMS;
  public boolean _renderTileMeshes;
  public boolean _logTilesPetitions;

  public Frustum _frustumInModelCoordinates;
  public LayerTilesRenderParameters _layerTilesRenderParameters;
  public TilesRenderParameters _tilesRenderParameters;

  public final long getTileTextureDownloadPriority(int tileLevel)
  {
    return (_tilesRenderParameters._incrementalTileQuality ? _tileTextureDownloadPriority + _layerTilesRenderParameters._maxLevel - tileLevel : _tileTextureDownloadPriority + tileLevel);
  }

  public void dispose()
  {
  }

}
