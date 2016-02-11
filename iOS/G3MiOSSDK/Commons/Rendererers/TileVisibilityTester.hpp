//
//  TileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

#ifndef TileVisibilityTester_hpp
#define TileVisibilityTester_hpp

class Tile;
class G3MRenderContext;
class LayerTilesRenderParameters;
class Frustum;

class TileVisibilityTester {
public:

  TileVisibilityTester()
  {
  }

  virtual ~TileVisibilityTester() { }

  virtual bool isVisible(const Tile* tile,
                         const G3MRenderContext* rc,
                         long long nowInMS,
                         const Frustum* frustumInModelCoordinates) const = 0;

  virtual void onTileHasChangedMesh(const Tile* tile) const = 0;

  virtual void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

  virtual void renderStarted() const = 0;

};

#endif
