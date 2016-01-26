//
//  MaxLevelTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef MaxLevelForPolesTileLODTester_hpp
#define MaxLevelForPolesTileLODTester_hpp

#include "TileLODTesterResponder.hpp"


class MaxLevelTileLODTester : public TileLODTesterResponder {
private:
  int _maxLevel;
  int _maxLevelForPoles;

protected:

  bool _meetsRenderCriteria(Tile* tile,
                            const G3MRenderContext& rc) const;

  bool _isVisible(Tile* tile,
                  const G3MRenderContext& rc) const;

  void _onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);

public:

  MaxLevelTileLODTester(int maxLevel,
                        int maxLevelForPoles,
                        TileLODTester* nextTesterRightLOD,
                        TileLODTester* nextTesterWrongLOD,
                        TileLODTester* nextTesterVisible,
                        TileLODTester* nextTesterNotVisible);


  ~MaxLevelTileLODTester();

};

#endif
