//
//  PeriodicalTask.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/10/12.
//

#ifndef G3MiOSSDK_PeriodicalTask
#define G3MiOSSDK_PeriodicalTask

#include "GTask.hpp"
#include "IFactory.hpp"
#include "TimeInterval.hpp"
#include "ITimer.hpp"

class PeriodicalTask {
private:
  long long _intervalMS;
  long long _lastExecutionMS;
  GTask*    _task;

  ITimer*   _timer;

  ITimer* getTimer() {
    if (_timer == NULL) {
      _timer = IFactory::instance()->createTimer();
    }
    return _timer;
  }

public:
  PeriodicalTask(const TimeInterval& interval,
                 GTask* task):
  _intervalMS(interval._milliseconds),
  _task(task),
  _lastExecutionMS(0),
  _timer(NULL)
  {
  }

  ~PeriodicalTask() {
    delete _task;
    delete _timer;
  }

  void resetTimeout() {
    _lastExecutionMS = 0;
  }

  void executeIfNecessary(const G3MContext* context) {
    long long now = getTimer()->nowInMilliseconds();

    long long interval = now - _lastExecutionMS;

    if (interval >= _intervalMS) {
      _task->run(context);
      _lastExecutionMS = now;
    }
  }

};

#endif
