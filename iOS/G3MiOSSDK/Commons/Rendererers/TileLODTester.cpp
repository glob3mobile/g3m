//
//  TileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#include "TileLODTester.hpp"

TileLODTester::~TileLODTester(){
  delete _nextTesterNotVisible;
  delete _nextTesterRightLoD;
  delete _nextTesterVisible;
  delete _nextTesterWrongLoD;
}

bool TileLODTester::meetsRenderCriteria(int testerLevel,
                                        Tile* tile,
                                        const G3MRenderContext& rc) const{
  
  //Right LOD
  if (_meetsRenderCriteria(testerLevel, tile, rc)){
    if (_nextTesterRightLoD != NULL){
      return _nextTesterRightLoD->meetsRenderCriteria(testerLevel + 1, tile, rc);;
    }
    return true;
  }
  
  //Wrong LOD
  if (_nextTesterWrongLoD != NULL){
    return _nextTesterWrongLoD->meetsRenderCriteria(testerLevel + 1, tile, rc);;
  }
  return false;
}


bool TileLODTester::isVisible(int testerLevel,
                              Tile* tile,
                              const G3MRenderContext& rc) const{
  
  //Visible
  if (_isVisible(testerLevel, tile, rc)){
    if (_nextTesterVisible != NULL){
      return _nextTesterVisible->isVisible(testerLevel+1, tile, rc);;
    }
    return true;
  }
  
  //Not visible
  if (_nextTesterNotVisible != NULL){
    return _nextTesterNotVisible->isVisible(testerLevel+1, tile, rc);;
  }
  return false;
  
}

void TileLODTester::onTileHasChangedMesh(int testerLevel, Tile* tile) const{
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
