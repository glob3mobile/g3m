//
//  TileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef TileLODTester_hpp
#define TileLODTester_hpp

class G3MRenderContext;
class PlanetRenderContext;
class Tile;
class LayerTilesRenderParameters;


class TileLODTester {
public:

  virtual ~TileLODTester() { }

  virtual bool meetsRenderCriteria(const G3MRenderContext* rc,
                                   const PlanetRenderContext* prc,
                                   const Tile* tile) const = 0;

  virtual void onTileHasChangedMesh(const Tile* tile) const = 0;

  virtual void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

  virtual void renderStarted() const = 0;

};


#endif
