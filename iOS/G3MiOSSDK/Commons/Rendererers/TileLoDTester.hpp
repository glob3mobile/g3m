//
//  TileLoDTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef TileLoDTester_hpp
#define TileLoDTester_hpp

#include "TileTessellator.hpp"
#include "TileTexturizer.hpp"

class TileLoDTesterData{
  //Empty class. Each TileLoDTester will implement a different set of associated data and will
  //store it inside the tile using its unique level id
};

class TileLoDTester{
  
  TileLoDTester* _nextTesterRightLoD;
  TileLoDTester* _nextTesterWrongLoD;
  TileLoDTester* _nextTesterVisible;
  TileLoDTester* _nextTesterNotVisible;
  
protected:
  
  virtual bool _meetsRenderCriteria(int testerLevel,
                                    Tile* tile,
                                    TileTessellator * tessellator,
                                    TileTexturizer* texturizer) const;
  
  virtual bool _isVisible(int testerLevel,
                          Tile* tile) const;
  
public:
  
  TileLoDTester(TileLoDTester* nextTesterRightLoD,
                TileLoDTester* nextTesterWrongLoD,
                TileLoDTester* nextTesterVisible,
                TileLoDTester* nextTesterNotVisible):
  _nextTesterRightLoD(nextTesterRightLoD),
  _nextTesterWrongLoD(nextTesterWrongLoD),
  _nextTesterVisible(nextTesterVisible),
  _nextTesterNotVisible(nextTesterNotVisible){
    
  }
  
  virtual ~TileLoDTester(){
    delete _nextTesterNotVisible;
    delete _nextTesterRightLoD;
    delete _nextTesterVisible;
    delete _nextTesterWrongLoD;
  }
  
  bool meetsRenderCriteria(int testerLevel,
                           Tile* tile,
                           TileTessellator * tessellator,
                           TileTexturizer* texturizer) const;
  
  bool isVisible(int testerLevel, Tile* tile) const;
};

#endif /* TileLoDTester_hpp */
