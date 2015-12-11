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
#include "Tile.hpp"
#include "ITimer.hpp"
#include "IFactory.hpp"
#include "Context.hpp"

class TimedTileLoDTester: public TileLoDTester{
protected:
  
  class TimedTileLoDTesterData: public TileLoDTesterData{
  public:
    double _lastMeetsRenderCriteriaTimeInMS;
  };
  
  bool _meetsRenderCriteria(int testerLevel,
                            Tile* tile, const G3MRenderContext& rc) const{
    
    TimedTileLoDTesterData* data = (TimedTileLoDTesterData*) tile->getDataForLoDTester(testerLevel);
    if (data == NULL){
      data = new TimedTileLoDTesterData();
      data->_lastMeetsRenderCriteriaTimeInMS = rc.getFrameStartTimer()->nowInMilliseconds();
      tile->setDataForLoDTester(testerLevel, data);
      return false;
    }
    
    const double lastTime = data->_lastMeetsRenderCriteriaTimeInMS;
    const double now = rc.getFrameStartTimer()->nowInMilliseconds();

    const bool res = ((now - lastTime) > _maxElapsedTimeInMS);
    if (res){
      data->_lastMeetsRenderCriteriaTimeInMS = now; //Update
    }

    return res;
    
  }
  
  bool _isVisible(int testerLevel,
                  Tile* tile, const G3MRenderContext& rc) const{
    return true;
  }
  
  double _maxElapsedTimeInMS;
  
public:
  
  TimedTileLoDTester(double maxElapsedTimeInMS,
                     TileLoDTester* nextTesterRightLoD,
                     TileLoDTester* nextTesterWrongLoD,
                     TileLoDTester* nextTesterVisible,
                     TileLoDTester* nextTesterNotVisible):
  _maxElapsedTimeInMS(maxElapsedTimeInMS),
  TileLoDTester(nextTesterRightLoD,
                nextTesterWrongLoD,
                nextTesterVisible,
                nextTesterNotVisible){}
  
  
  ~TimedTileLoDTester(){
  }
  
};

#endif /* TimedTileLoDTester_hpp */
