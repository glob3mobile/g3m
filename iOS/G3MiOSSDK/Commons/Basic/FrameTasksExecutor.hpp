//
//  FrameTasksExecutor.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/08/12.
//
//

#ifndef __G3MiOSSDK__FrameTasksExecutor__
#define __G3MiOSSDK__FrameTasksExecutor__

#include <list>

class G3MRenderContext;
class FrameTask;


class FrameTasksExecutor {
private:
  const int       _minimumExecutionsPerFrame;
  const int       _maximumExecutionsPerFrame;
  const int       _maximumQueuedTasks;
  const long long _maxTimePerFrameMS;
  const long long _maxTimePerFrameStressedMS;
  const bool      _debug;

  std::list<FrameTask*> _tasks;

  inline bool canExecutePreRenderStep(const G3MRenderContext* rc,
                                      int executedTasksCounter);

  bool _stressed;

public:
  FrameTasksExecutor() :
  _minimumExecutionsPerFrame(1),
  _maximumExecutionsPerFrame(4),
  _maximumQueuedTasks(64),
  _maxTimePerFrameMS(10),
  _maxTimePerFrameStressedMS(20),
  _stressed(false),
  _debug(true)
  {
  }

  void addPreRenderTask(FrameTask* task) {
    _tasks.push_back(task);
  }

  void doPreRenderCycle(const G3MRenderContext* rc);

  ~FrameTasksExecutor() {
  }

};

#endif
