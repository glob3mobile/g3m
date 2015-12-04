//
//  TileLoDTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#include "TileLoDTester.hpp"

bool TileLoDTester::meetsRenderCriteria(int testerLevel,
                                        Tile* tile,
                                        TileTessellator * tessellator,
                                        TileTexturizer* texturizer) const{
  
  //Right LOD
  if (_meetsRenderCriteria(testerLevel, tile, tessellator, texturizer)){
    if (_nextTesterRightLoD != NULL){
      return _nextTesterRightLoD->meetsRenderCriteria(testerLevel + 1, tile, tessellator, texturizer);
    }
    return true;
  }
  
  //Wrong LOD
  if (_nextTesterWrongLoD != NULL){
    return _nextTesterWrongLoD->meetsRenderCriteria(testerLevel + 1, tile, tessellator, texturizer);
  }
  return false;
}


bool TileLoDTester::isVisible(int testerLevel,
                              Tile* tile) const{
  
  //Visible
  if (_isVisible(testerLevel, tile)){
    if (_nextTesterVisible != NULL){
      return _nextTesterVisible->isVisible(testerLevel+1, tile);
    }
    return true;
  }
  
  //Wrong LOD
  if (_nextTesterNotVisible != NULL){
    return _nextTesterNotVisible->isVisible(testerLevel+1, tile);
  }
  return false;
  
}
