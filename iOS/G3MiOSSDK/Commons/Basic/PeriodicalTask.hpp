//
//  PeriodicalTask.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_PeriodicalTask_hpp
#define G3MiOSSDK_PeriodicalTask_hpp

#include "GTask.hpp"
#include "IFactory.hpp"
#include "TimeInterval.hpp"
#include "ITimer.hpp"

//Storing Scheduled Tasks
class PeriodicalTask{
public:
  PeriodicalTask(const TimeInterval& interval, GTask* task):
  _interval(interval.milliseconds()), _task(task), _lastExecution(0){
  }
  
  void executeIfNecessary(){
    ITimer* t = IFactory::instance()->createTimer();
    long long now = t->now().milliseconds();
    IFactory::instance()->deleteTimer(t);
    
    long long interval = now - _lastExecution;
    
    if (interval >= _interval){
      _task->run();
      _lastExecution = now;
    }
  }
  
private:
  //Milliseconds
  long long     _interval;
  long long     _lastExecution;
  GTask         *_task;
};

#endif
