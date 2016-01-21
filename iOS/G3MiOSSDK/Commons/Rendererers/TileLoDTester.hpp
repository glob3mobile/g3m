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

  virtual bool meetsRenderCriteria(Tile* tile,
                                   const G3MRenderContext& rc) const = 0;

  virtual bool isVisible(Tile* tile,
                         const G3MRenderContext& rc) const = 0;

  virtual void onTileHasChangedMesh(Tile* tile) const = 0;

  virtual void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

};


#endif
