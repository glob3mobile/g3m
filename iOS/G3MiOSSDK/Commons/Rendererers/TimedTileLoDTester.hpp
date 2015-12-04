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

class TimedTileLoDTester: public TileLoDTester{
protected:
  
  class TimedTileLoDTesterData: public TileLoDTesterData{
  public:
    double _lastMeetsRenderCriteriaTimeInMS;
  };
  
  bool _meetsRenderCriteria(int testerLevel,
                            Tile* tile) const{
    
    if (_timer == NULL) {
      _timer = IFactory::instance()->createTimer();
    }
    
    TimedTileLoDTesterData* data = (TimedTileLoDTesterData*) tile->getDataForLoDTester(testerLevel);
    if (data == NULL){
      data = new TimedTileLoDTesterData();
      data->_lastMeetsRenderCriteriaTimeInMS = _timer->nowInMilliseconds();
      tile->setDataForLoDTester(testerLevel, data);
    }
    
    double lastTime = data->_lastMeetsRenderCriteriaTimeInMS;
    data->_lastMeetsRenderCriteriaTimeInMS = _timer->nowInMilliseconds(); //Update

    if ((_timer->nowInMilliseconds() - lastTime) > _maxElapsedTimeInMS){
      return true;
    } else{
      return false;
    }
    
  }
  
  bool _isVisible(int testerLevel,
                  Tile* tile) const{
    return true;
  }
  
  mutable ITimer* _timer;
  double _maxElapsedTimeInMS;
  
public:
  
  TimedTileLoDTester(double maxElapsedTimeInMS,
                     TileLoDTester* nextTesterRightLoD,
                     TileLoDTester* nextTesterWrongLoD,
                     TileLoDTester* nextTesterVisible,
                     TileLoDTester* nextTesterNotVisible):
  _timer(NULL),
  _maxElapsedTimeInMS(maxElapsedTimeInMS),
  TileLoDTester(nextTesterRightLoD,
                nextTesterWrongLoD,
                nextTesterVisible,
                nextTesterNotVisible){}
  
  
  ~TimedTileLoDTester(){
    delete _timer;
  }
  
};

#endif /* TimedTileLoDTester_hpp */
