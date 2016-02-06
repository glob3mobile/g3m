//
//  PlanetRenderContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/4/16.
//
//

#ifndef PlanetRenderContext_hpp
#define PlanetRenderContext_hpp

class TileLODTester;
class TileVisibilityTester;
class Frustum;
class LayerTilesRenderParameters;
class TileTexturizer;
class TilesRenderParameters;
class ITimer;
class ElevationDataProvider;
class TileTessellator;
class LayerSet;

class PlanetRenderContext {
public:
  TileLODTester*                    _tileLODTester;
  TileVisibilityTester*             _tileVisibilityTester;
  const Frustum*                    _frustumInModelCoordinates;
  float                             _verticalExaggeration;
  const LayerTilesRenderParameters* _layerTilesRenderParameters;
  TileTexturizer*                   _texturizer;
  const TilesRenderParameters*      _tilesRenderParameters;
  ITimer*                           _lastSplitTimer;
  ElevationDataProvider*            _elevationDataProvider;
  const TileTessellator*            _tessellator;
  const LayerSet*                   _layerSet;
  long long                         _tileDownloadPriority;
  double                            _texWidthSquared;
  double                            _texHeightSquared;
  long long                         _nowInMS;
  bool                              _renderTileMeshes;
  bool                              _logTilesPetitions;


  ~PlanetRenderContext() {
  }

};

#endif
