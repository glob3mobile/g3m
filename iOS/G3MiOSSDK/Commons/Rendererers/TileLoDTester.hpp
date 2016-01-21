//
//  TileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef TileLODTester_hpp
#define TileLODTester_hpp

class Tile;
class G3MRenderContext;
class LayerTilesRenderParameters;


class TileLODTester {

public:

  TileLODTester() { }

  virtual ~TileLODTester() { }

  virtual bool meetsRenderCriteria(int testerLevel,
                                   Tile* tile,
                                   const G3MRenderContext& rc) const = 0;

  virtual bool isVisible(int testerLevel,
                         Tile* tile,
                         const G3MRenderContext& rc) const = 0;

  virtual void onTileHasChangedMesh(int testerLevel,
                                    Tile* tile) const = 0;

  virtual void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

};


#endif
