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
  
  mutable long long _lastElapsedTime;
  mutable int _nSplitsInFrame;
  
public:
  
  MaxFrameTimeTileLODTester(const TimeInterval& maxFrameTimeInMs,
                     TileLODTester* nextTester):
  _maxFrameTimeInMs(maxFrameTimeInMs.milliseconds()),
  _nextTester(nextTester),
  _lastElapsedTime(0),
  _nSplitsInFrame(0)
  {}
  
  virtual ~MaxFrameTimeTileLODTester(){
    delete _nextTester;
  }
  
  virtual bool meetsRenderCriteria(int testerLevel,
                                   Tile* tile, const G3MRenderContext& rc) const{
    
    const bool hasSubtiles = tile->areSubtilesCreated();
    long long elapsedTime = rc.getFrameStartTimer()->elapsedTimeInMilliseconds();
    if (elapsedTime < _lastElapsedTime){
      //New frame
//      if (_nSplitsInFrame > 0){
//        printf("Tile splits on last frame: %d\n", _nSplitsInFrame);
//      }
      _nSplitsInFrame = 0;
    }
    _lastElapsedTime = elapsedTime;
    
    if (!hasSubtiles && elapsedTime > _maxFrameTimeInMs && _nSplitsInFrame > 0){
      return true;
    }
    
    bool res = (_nextTester == NULL)? true : _nextTester->meetsRenderCriteria(testerLevel+1, tile, rc);
    
    if (!res && !hasSubtiles){
      _nSplitsInFrame++;
    }
    
    return res;
  }
  
  virtual bool isVisible(int testerLevel, Tile* tile, const G3MRenderContext& rc) const{
    if (_nextTester == NULL){
      return true;
    }
    return _nextTester->isVisible(testerLevel+1, tile, rc);
  }
  
  virtual void onTileHasChangedMesh(int testerLevel, Tile* tile) const{
    if (_nextTester != NULL){
      _nextTester->onTileHasChangedMesh(testerLevel+1, tile);
    }
  }
  
  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp){
    if (_nextTester != NULL){
      _nextTester->onLayerTilesRenderParametersChanged(ltrp);
    }
  }
  
};


#endif /* MaxFrameTimeTileLODTester_hpp */
