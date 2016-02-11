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
  TileLODTester*         _tileLODTester;
  TileVisibilityTester*  _tileVisibilityTester;
  float                  _verticalExaggeration;
  TileTexturizer*        _texturizer;
  ITimer*                _lastSplitTimer;
  ElevationDataProvider* _elevationDataProvider;
  TileTessellator*       _tessellator;
  LayerSet*              _layerSet;
  long long              _tileDownloadPriority;
  double                 _texWidthSquared;
  double                 _texHeightSquared;
  long long              _nowInMS;
  bool                   _renderTileMeshes;
  bool                   _logTilesPetitions;

#ifdef C_CODE
  const Frustum*                    _frustumInModelCoordinates;
  const LayerTilesRenderParameters* _layerTilesRenderParameters;
  const TilesRenderParameters*      _tilesRenderParameters;
#else
  Frustum*                    _frustumInModelCoordinates;
  LayerTilesRenderParameters* _layerTilesRenderParameters;
  TilesRenderParameters*      _tilesRenderParameters;
#endif

  ~PlanetRenderContext() {
  }

};

#endif
