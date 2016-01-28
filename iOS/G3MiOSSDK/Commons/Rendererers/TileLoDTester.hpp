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
class TilesRenderParameters;
class ITimer;

class TileLODTester {
private:
  static int ID_COUNTER;

protected:
  const int _id;

public:

  TileLODTester() :
  _id(ID_COUNTER++)
  {
  }

  virtual ~TileLODTester() { }

  virtual bool meetsRenderCriteria(const Tile* tile,
                                   const G3MRenderContext* rc,
                                   const TilesRenderParameters* tilesRenderParameters,
                                   const ITimer* lastSplitTimer,
                                   const double texWidthSquared,
                                   const double texHeightSquared,
                                   long long nowInMS) const = 0;

  virtual void onTileHasChangedMesh(const Tile* tile) const = 0;

  virtual void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

};


#endif
