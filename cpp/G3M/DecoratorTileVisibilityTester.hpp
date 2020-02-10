//
//  DecoratorTileVisibilityTester.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

#ifndef DecoratorTileVisibilityTester_hpp
#define DecoratorTileVisibilityTester_hpp

#include "TileVisibilityTester.hpp"

class DecoratorTileVisibilityTester : public TileVisibilityTester {
protected:
  TileVisibilityTester* _tileVisibilityTester;

  DecoratorTileVisibilityTester(TileVisibilityTester* tileVisibilityTester);

public:

  virtual ~DecoratorTileVisibilityTester();

  virtual void onTileHasChangedMesh(const Tile* tile) const;

  virtual void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);

};

#endif
