//
//  DecoratorTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/26/16.
//
//

#ifndef DecoratorTileLODTester_hpp
#define DecoratorTileLODTester_hpp

#include "TileLODTester.hpp"


class DecoratorTileLODTester : public TileLODTester {
protected:
  TileLODTester* _tileLODTester;

  DecoratorTileLODTester(TileLODTester* tileLODTester);

public:
  virtual ~DecoratorTileLODTester();

  virtual void onTileHasChangedMesh(const Tile* tile) const;

  virtual void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);

  virtual void renderStarted() const;

};

#endif
