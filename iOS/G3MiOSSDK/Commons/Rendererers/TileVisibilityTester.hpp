//
//  TileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

#ifndef TileVisibilityTester_hpp
#define TileVisibilityTester_hpp

class G3MRenderContext;
class PlanetRenderContext;
class Tile;
class LayerTilesRenderParameters;

class TileVisibilityTester {
public:

  TileVisibilityTester()
  {
  }

  virtual ~TileVisibilityTester() { }

  virtual bool isVisible(const G3MRenderContext* rc,
                         const PlanetRenderContext* prc,
                         Tile* tile) const = 0;

  virtual void onTileHasChangedMesh(const Tile* tile) const = 0;

  virtual void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

  virtual void renderStarted() const = 0;

};

#endif
