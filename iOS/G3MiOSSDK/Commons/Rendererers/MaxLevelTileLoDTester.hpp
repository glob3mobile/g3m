//
//  MaxLevelTileLoDTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef MaxLevelForPolesTileLoDTester_hpp
#define MaxLevelForPolesTileLoDTester_hpp

#include "TileLoDTester.hpp"
#include "Tile.hpp"
#include "Context.hpp"

class MaxLevelTileLoDTester: public TileLoDTester{
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
  
  MaxLevelTileLoDTester(int maxLevel,
                        int maxLevelForPoles,
                        TileLoDTester* nextTesterRightLoD,
                        TileLoDTester* nextTesterWrongLoD,
                        TileLoDTester* nextTesterVisible,
                        TileLoDTester* nextTesterNotVisible):
  TileLoDTester(nextTesterRightLoD,
                nextTesterWrongLoD,
                nextTesterVisible,
                nextTesterNotVisible),
  _maxLevelForPoles(maxLevelForPoles),
  _maxLevel(maxLevel)
  {}
  
  
  ~MaxLevelTileLoDTester(){
  }
  
};

#endif /* MaxLevelForPolesTileLoDTester_hpp */
