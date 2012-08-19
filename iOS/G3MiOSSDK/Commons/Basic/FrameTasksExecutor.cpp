//
//  FrameTasksExecutor.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/08/12.
//
//

#include "FrameTasksExecutor.hpp"

#include "Context.hpp"
#include "ITimer.hpp"
#include "ILogger.hpp"

bool FrameTasksExecutor::executeTask(const RenderContext* rc,
                                     int executedTasksCounter) const {
  if (_preRenderTasks.size() == 0) {
    return false;
  }
  
  if (executedTasksCounter < _minimumExecutionsPerFrame) {
    return true;
  }
  
  return rc->getFrameStartTimer()->elapsedTime().lowerThan(_maxTimePerFrame);
  
  //  return ((_preRenderTasks.size() > 0) &&
  //          (executedTasksCounter < _minimumExecutionsPerFrame) &&
  //          rc->getFrameStartTimer()->elapsedTime().lowerThan(_maxTimePerFrame) );
}

void FrameTasksExecutor::doPreRenderCycle(const RenderContext *rc) {
  int executedCounter = 0;
  while (executeTask(rc, executedCounter)) {
    FrameTask* task = _preRenderTasks.front();
    _preRenderTasks.pop_front();
    
    task->execute(rc);
    
    delete task;
    
    executedCounter++;
  }
  
//  if ((executedCounter > 0) ||
//      (_preRenderTasks.size() > 0)) {
//    rc->getLogger()->logInfo("Executed %d tasks in %ld ms, queued %d tasks.",
//                             executedCounter,
//                             rc->getFrameStartTimer()->elapsedTime().milliseconds(),
//                             _preRenderTasks.size());
//  }
}
