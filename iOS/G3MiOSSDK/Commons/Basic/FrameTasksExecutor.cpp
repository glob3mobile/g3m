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

bool FrameTasksExecutor::canExecutePreRenderStep(const G3MRenderContext* rc,
                                                 int executedCounter) {
  const int tasksCount = _preRenderTasks.size();
  if (tasksCount <= _minimumExecutionsPerFrame) {
    //if (_stressed) {
    //  rc->getLogger()->logWarning("Abandon STRESSED mode");
    //}
    _stressed = false;
  }

  if (tasksCount == 0) {
    return false;
  }

  if (executedCounter < _minimumExecutionsPerFrame) {
    return true;
  }

  if (tasksCount > _maximumQueuedTasks) {
    //if (!_stressed) {
    //  rc->getLogger()->logWarning("Too many queued tasks (%d). Goes to STRESSED mode",
    //                              _preRenderTasks.size());
    //}
    _stressed = true;
  }

  if (_stressed) {
    return rc->getFrameStartTimer()->elapsedTimeInMilliseconds() < _maxTimePerFrameStressedMS;
  }

  if (executedCounter >= _maximumExecutionsPerFrame) {
    return false;
  }
  return rc->getFrameStartTimer()->elapsedTimeInMilliseconds() < _maxTimePerFrameMS;
}

void FrameTasksExecutor::doPreRenderCycle(const G3MRenderContext* rc) {

  //  int canceledCounter = 0;
  std::list<FrameTask*>::iterator i = _preRenderTasks.begin();
  while (i != _preRenderTasks.end()) {
    FrameTask* task = *i;

    const bool isCanceled = task->isCanceled(rc);
    if (isCanceled) {
      delete task;
#ifdef C_CODE
      //_preRenderTasks.erase(i);
      i = _preRenderTasks.erase(i);
#endif
#ifdef JAVA_CODE
      i.remove();
#endif
      //      canceledCounter++;
    }
    else {
      i++;
    }
  }

  //  if (canceledCounter > 0) {
  //    rc->getLogger()->logInfo("Removed %d tasks, actived %d tasks.",
  //                             canceledCounter,
  //                             _preRenderTasks.size());
  //  }

  int executedCounter = 0;
  while (canExecutePreRenderStep(rc, executedCounter)) {
    FrameTask* task = _preRenderTasks.front();
    _preRenderTasks.pop_front();

    task->execute(rc);

    delete task;

    executedCounter++;
  }

  //  if (false) {
  //    //    if ( rc->getFrameStartTimer()->elapsedTime().milliseconds() > _maxTimePerFrame.milliseconds()*3 ) {
  //    //      rc->getLogger()->logWarning("doPreRenderCycle() took too much time, Tasks: canceled=%d, executed=%d in %ld ms, queued %d. STRESSED=%d",
  //    //                                  canceledCounter,
  //    //                                  executedCounter,
  //    //                                  rc->getFrameStartTimer()->elapsedTime().milliseconds(),
  //    //                                  _preRenderTasks.size(),
  //    //                                  _stressed);
  //    //
  //    //    }
  //    //    else {
  //    if ((executedCounter > 0) ||
  //        (canceledCounter > 0) ||
  //        (_preRenderTasks.size() > 0)) {
  //      rc->getLogger()->logInfo("Tasks: canceled=%d, executed=%d in %ld ms, queued %d. STRESSED=%d",
  //                               canceledCounter,
  //                               executedCounter,
  //                               rc->getFrameStartTimer()->elapsedTime().milliseconds(),
  //                               _preRenderTasks.size(),
  //                               _stressed);
  //    }
  //    //    }
  //  }
  
}
