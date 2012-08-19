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

bool FrameTasksExecutor::canExecutePreRenderStep(const RenderContext* rc,
                                                 int executedCounter) {
  const int tasksCount = _preRenderTasks.size();
  if (tasksCount == 0) {
    if (_stressed) {
//      rc->getLogger()->logWarning("Abandon STRESSED mode");
      _stressed = false;
    }
    return false;
  }
  
  if (executedCounter < _minimumExecutionsPerFrame) {
    return true;
  }
  
  if (tasksCount > _maximumQueuedTasks) {
//    if (!_stressed) {
//      rc->getLogger()->logWarning("Too many queued tasks (%d). Goes to STRESSED mode",
//                                  _preRenderTasks.size());
//    }
    
    _stressed = true;
  }
  
  if (_stressed) {
    return rc->getFrameStartTimer()->elapsedTime().lowerThan(_maxTimePerFrameStressed);
  }
  
  if (executedCounter >= _maximumExecutionsPerFrame) {
    return false;
  }
  return rc->getFrameStartTimer()->elapsedTime().lowerThan(_maxTimePerFrame);
  
  
  //  if (tasksCount > _maximumQueuedTasks) {
  //    rc->getLogger()->logWarning("Too many queued tasks (%d).",
  //                                _preRenderTasks.size());
  //
  ////    return true;
  //
  //    return rc->getFrameStartTimer()->elapsedTime().lowerThan(_maxTimePerFrameStressed);
  //  }
  //
  //  if (executedCounter > _maximumExecutionsPerFrame) {
  //    return false;
  //  }
  //
  //  return rc->getFrameStartTimer()->elapsedTime().lowerThan(_maxTimePerFrame);
}

void FrameTasksExecutor::doPreRenderCycle(const RenderContext *rc) {
  int executedCounter = 0;
  while (canExecutePreRenderStep(rc, executedCounter)) {
    FrameTask* task = _preRenderTasks.front();
    _preRenderTasks.pop_front();
    
    task->execute(rc);
    
    delete task;
    
    executedCounter++;
  }
  
//  if ((executedCounter > 0) ||
//      (_preRenderTasks.size() > 0)) {
//    rc->getLogger()->logInfo("Executed %d tasks in %ld ms, queued %d tasks. STRESSED=%d",
//                             executedCounter,
//                             rc->getFrameStartTimer()->elapsedTime().milliseconds(),
//                             _preRenderTasks.size(),
//                             _stressed);
//  }
  
}
