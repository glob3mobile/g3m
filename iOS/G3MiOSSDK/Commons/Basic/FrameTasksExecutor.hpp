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

#include "TimeInterval.hpp"

class RenderContext;


class FrameTask {
public:
  virtual ~FrameTask() {
    
  }
  
  virtual void execute(const RenderContext* rc) = 0;
};


class FrameTasksExecutor {
private:
  const TimeInterval _maxTimePerFrame;
  
  std::list<FrameTask*> _preRenderTasks;
  
  const int _minimumExecutionsPerFrame;
  
  inline bool executeTask(const RenderContext* rc,
                          int executedTasksCounter) const;
  
public:
  FrameTasksExecutor() :
  _maxTimePerFrame(TimeInterval::fromMilliseconds(15)),
  _minimumExecutionsPerFrame(1)
  {
    int __TODO_tune_render_budget;
  }
  
  void addPreRenderTask(FrameTask* preRenderTask) {
    _preRenderTasks.push_back(preRenderTask);
  }
  
  void doPreRenderCycle(const RenderContext* rc);
  
};

#endif
