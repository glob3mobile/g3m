//
//  MaxFrameTimeTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#ifndef MaxFrameTimeTileLODTester_hpp
#define MaxFrameTimeTileLODTester_hpp


#include "TileLODTester.hpp"
#include "Tile.hpp"

class MaxFrameTimeTileLODTester: public TileLODTester{
private:
  TileLODTester* _nextTester;
  long long _maxFrameTimeInMs;
  
public:
  
  MaxFrameTimeTileLODTester(long long maxFrameTimeInMs,
                     TileLODTester* nextTester):
  _maxFrameTimeInMs(maxFrameTimeInMs),
  _nextTester(nextTester)
  {}
  
  virtual ~MaxFrameTimeTileLODTester(){
    delete _nextTester;
  }
  
  virtual bool meetsRenderCriteria(int testerLevel,
                                   Tile* tile, const G3MRenderContext& rc) const{
    
    if (!tile->areSubtilesCreated() &&
        rc.getFrameStartTimer()->elapsedTimeInMilliseconds() > _maxFrameTimeInMs){
      return true;
    }
    
    return _nextTester->meetsRenderCriteria(testerLevel+1, tile, rc);
  }
  
  virtual bool isVisible(int testerLevel, Tile* tile, const G3MRenderContext& rc) const{
    return _nextTester->isVisible(testerLevel+1, tile, rc);
  }
  
  virtual void onTileHasChangedMesh(int testerLevel, Tile* tile) const{
    _nextTester->onTileHasChangedMesh(testerLevel+1, tile);
  }
  
};


#endif /* MaxFrameTimeTileLODTester_hpp */
