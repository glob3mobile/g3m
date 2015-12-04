//
//  TimedTileLoDTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef TimedTileLoDTester_hpp
#define TimedTileLoDTester_hpp

#include "TileLoDTester.hpp"

class TimedTileLoDTester: public TileLoDTester{
  
  
  
  
  bool _meetsRenderCriteria(int testerLevel,
                            Tile* tile,
                            TileTessellator * tessellator,
                            TileTexturizer* texturizer) const{
  
  }
  
  bool _isVisible(int testerLevel,
                  Tile* tile) const{
    
  }
  
public:
  
  TimedTileLoDTester(TileLoDTester* nextTesterRightLoD,
                TileLoDTester* nextTesterWrongLoD,
                TileLoDTester* nextTesterVisible,
                     TileLoDTester* nextTesterNotVisible):
  TileLoDTester(nextTesterRightLoD,
                nextTesterWrongLoD,
                nextTesterVisible,
                nextTesterNotVisible){}

  
};

#endif /* TimedTileLoDTester_hpp */
