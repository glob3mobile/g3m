//
//  MaxLevelTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef MaxLevelForPolesTileLODTester_hpp
#define MaxLevelForPolesTileLODTester_hpp

#include "TileLODTester.hpp"
#include "Tile.hpp"
#include "Context.hpp"

class MaxLevelTileLODTester: public TileLODTesterResponder{
protected:
  
  bool _meetsRenderCriteria(int testerLevel,
                            Tile* tile,
                            const G3MRenderContext& rc) const{
    
    if (tile->_level >= _maxLevel){
      return true;
    }
    
    if (tile->_sector.touchesPoles()){
      if (tile->_level >= _maxLevelForPoles){
        return true;
      }
    }
    
    return false;
  }
  
  bool _isVisible(int testerLevel,
                  Tile* tile,
                  const G3MRenderContext& rc) const{
    return true;
  }
  
  int _maxLevelForPoles;
  int _maxLevel;
  
public:
  
  MaxLevelTileLODTester(int maxLevel,
                        int maxLevelForPoles,
                        TileLODTester* nextTesterRightLoD,
                        TileLODTester* nextTesterWrongLoD,
                        TileLODTester* nextTesterVisible,
                        TileLODTester* nextTesterNotVisible):
  TileLODTesterResponder(nextTesterRightLoD,
                         nextTesterWrongLoD,
                         nextTesterVisible,
                         nextTesterNotVisible),
  _maxLevelForPoles(maxLevelForPoles),
  _maxLevel(maxLevel)
  {}
  
  
  ~MaxLevelTileLODTester(){
  }
  
};

#endif /* MaxLevelForPolesTileLODTester_hpp */
