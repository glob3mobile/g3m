//
//  TileLODTesterResponder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//

#ifndef TileLODTesterResponder_hpp
#define TileLODTesterResponder_hpp

#include "TileLODTester.hpp"

class TileLODTesterResponder : public TileLODTester {

  TileLODTester* _nextTesterRightLOD;
  TileLODTester* _nextTesterWrongLOD;
  TileLODTester* _nextTesterVisible;
  TileLODTester* _nextTesterNotVisible;

protected:

  virtual bool _meetsRenderCriteria(Tile* tile,
                                    const G3MRenderContext& rc) const = 0;

  virtual bool _isVisible(Tile* tile,
                          const G3MRenderContext& rc) const = 0;

  virtual void _onTileHasChangedMesh(Tile* tile) const {}

  virtual void _onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

public:

  TileLODTesterResponder(TileLODTester* nextTesterRightLOD,
                         TileLODTester* nextTesterWrongLOD,
                         TileLODTester* nextTesterVisible,
                         TileLODTester* nextTesterNotVisible):
  _nextTesterRightLOD(nextTesterRightLOD),
  _nextTesterWrongLOD(nextTesterWrongLOD),
  _nextTesterVisible(nextTesterVisible),
  _nextTesterNotVisible(nextTesterNotVisible) {

  }

  virtual ~TileLODTesterResponder();

  bool meetsRenderCriteria(Tile* tile,
                           const G3MRenderContext& rc) const;

  bool isVisible(Tile* tile,
                 const G3MRenderContext& rc) const;

  void onTileHasChangedMesh(Tile* tile) const;

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);
};

#endif
