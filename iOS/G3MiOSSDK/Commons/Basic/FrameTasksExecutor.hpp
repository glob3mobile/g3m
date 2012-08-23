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
  const int          _minimumExecutionsPerFrame;
  const int          _maximumExecutionsPerFrame;
  const int          _maximumQueuedTasks;
#ifdef C_CODE
  const TimeInterval _maxTimePerFrame;
  const TimeInterval _maxTimePerFrameStressed;
#endif
#ifdef JAVA_CODE
  private final TimeInterval _maxTimePerFrame;
  private final TimeInterval _maxTimePerFrameStressed;
#endif
  
  std::list<FrameTask*> _preRenderTasks;
  
  inline bool canExecutePreRenderStep(const RenderContext* rc,
                                      int executedTasksCounter);
  
  bool _stressed;
  
public:
  FrameTasksExecutor() :
  _minimumExecutionsPerFrame(1),
  _maximumExecutionsPerFrame(2),
  _maximumQueuedTasks(64),
//  _maximumQueuedTasks(128),
  _maxTimePerFrame(TimeInterval::fromMilliseconds(10)),
  _maxTimePerFrameStressed(TimeInterval::fromMilliseconds(25)),
//  _maxTimePerFrameStressed(TimeInterval::fromMilliseconds(30)),
//  _maxTimePerFrameStressed(TimeInterval::fromMilliseconds(45)),
  _stressed(false)
  {
    int __TODO_tune_render_budget;
  }
  
  void addPreRenderTask(FrameTask* preRenderTask) {
    _preRenderTasks.push_back(preRenderTask);
  }
  
  void doPreRenderCycle(const RenderContext* rc);
  
  ~FrameTasksExecutor(){}
  
};

#endif
