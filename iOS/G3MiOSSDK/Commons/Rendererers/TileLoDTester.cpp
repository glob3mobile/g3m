//
//  TileLoDTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#include "TileLoDTester.hpp"

TileLoDTester::~TileLoDTester(){
  delete _nextTesterNotVisible;
  delete _nextTesterRightLoD;
  delete _nextTesterVisible;
  delete _nextTesterWrongLoD;
}

bool TileLoDTester::meetsRenderCriteria(int testerLevel,
                                        Tile* tile,
                                        const G3MRenderContext& rc) const{
  
  //Right LOD
  if (_meetsRenderCriteria(testerLevel, tile, rc)){
    if (_nextTesterRightLoD != NULL){
      _lastMeetsRenderCriteriaResult = _nextTesterRightLoD->meetsRenderCriteria(testerLevel + 1, tile, rc);
      return _lastMeetsRenderCriteriaResult;
    }
    return true;
  }
  
  //Wrong LOD
  if (_nextTesterWrongLoD != NULL){
    _lastMeetsRenderCriteriaResult = _nextTesterWrongLoD->meetsRenderCriteria(testerLevel + 1, tile, rc);
    return _lastMeetsRenderCriteriaResult;
  }
  return false;
}


bool TileLoDTester::isVisible(int testerLevel,
                              Tile* tile,
                              const G3MRenderContext& rc) const{
  
  //Visible
  if (_isVisible(testerLevel, tile, rc)){
    if (_nextTesterVisible != NULL){
      _lastIsVisibleResult = _nextTesterVisible->isVisible(testerLevel+1, tile, rc);
      return _lastIsVisibleResult;
    }
    return true;
  }
  
  //Wrong LOD
  if (_nextTesterNotVisible != NULL){
    _lastIsVisibleResult = _nextTesterNotVisible->isVisible(testerLevel+1, tile, rc);
    return _lastIsVisibleResult;
  }
  return false;
  
}

void TileLoDTester::onTileHasChangedMesh(int testerLevel, Tile* tile) const{
  _onTileHasChangedMesh(testerLevel, tile);
  if (_nextTesterNotVisible != NULL)
    _nextTesterNotVisible->onTileHasChangedMesh(testerLevel+1, tile);
  if (_nextTesterVisible != NULL)
    _nextTesterVisible->onTileHasChangedMesh(testerLevel+1, tile);
  if (_nextTesterRightLoD != NULL)
    _nextTesterRightLoD->onTileHasChangedMesh(testerLevel+1, tile);
  if (_nextTesterWrongLoD != NULL)
    _nextTesterWrongLoD->onTileHasChangedMesh(testerLevel+1, tile);
}
