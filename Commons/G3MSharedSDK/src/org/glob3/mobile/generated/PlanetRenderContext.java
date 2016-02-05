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
//class TileTessellator;
//class LayerSet;

public class PlanetRenderContext
{
  public TileLODTester _tileLODTester;
  public TileVisibilityTester _tileVisibilityTester;
  public final Frustum _frustumInModelCoordinates;
  public float _verticalExaggeration;
  public final LayerTilesRenderParameters _layerTilesRenderParameters;
  public TileTexturizer _texturizer;
  public final TilesRenderParameters _tilesRenderParameters;
  public ITimer _lastSplitTimer;
  public ElevationDataProvider _elevationDataProvider;
  public final TileTessellator _tessellator;
  public final LayerSet _layerSet;
  public boolean _forceFullRender;
  public long _tileDownloadPriority;
  public double _texWidthSquared;
  public double _texHeightSquared;
  public long _nowInMS;
  public boolean _renderTileMeshes;
  public boolean _logTilesPetitions;
}