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
#include "FrameTask.hpp"
#include "IStringBuilder.hpp"

bool FrameTasksExecutor::canExecutePreRenderStep(const G3MRenderContext* rc,
                                                 int executedCounter) {
  const int tasksCount = _tasks.size();
  if (tasksCount <= _minimumExecutionsPerFrame) {
    if (_debug) {
      if (_stressed) {
        rc->getLogger()->logWarning("FTE: Abandon STRESSED mode");
      }
    }
    _stressed = false;
  }

  if (tasksCount == 0) {
    return false;
  }

  if (executedCounter < _minimumExecutionsPerFrame) {
    return true;
  }

  if (tasksCount > _maximumQueuedTasks) {
    if (_debug) {
      if (!_stressed) {
        rc->getLogger()->logWarning("FTE: Too many queued tasks (%d). Goes to STRESSED mode",
                                    _tasks.size());
      }
    }
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
  if (_tasks.empty()) {
    return;
  }

  // remove canceled tasks
  int canceledCounter = 0;
  std::list<FrameTask*>::iterator i = _tasks.begin();
  while (i != _tasks.end()) {
    FrameTask* task = *i;

    const bool isCanceled = task->isCanceled(rc);
    if (isCanceled) {
      delete task;
#ifdef C_CODE
      i = _tasks.erase(i);
#endif
#ifdef JAVA_CODE
      i.remove();
#endif
      canceledCounter++;
    }
    else {
      i++;
    }
  }

  if (_debug) {
    if (canceledCounter > 0) {
      rc->getLogger()->logInfo("FTE: Removed %d tasks, active %d tasks.",
                               canceledCounter,
                               _tasks.size());
    }
  }


  // execute some tasks
  int executedCounter = 0;
  while (canExecutePreRenderStep(rc, executedCounter)) {
    FrameTask* task = _tasks.front();
    _tasks.pop_front();

    task->execute(rc);

    delete task;

    executedCounter++;
  }


  if (_debug) {
    showDebugInfo(rc, executedCounter, canceledCounter);
  }
}


void FrameTasksExecutor::showDebugInfo(const G3MRenderContext* rc,
                                       int executedCounter,
                                       int canceledCounter) {
  const int preRenderTasksSize = _tasks.size();
  if ((executedCounter > 0) ||
      (canceledCounter > 0) ||
      (preRenderTasksSize > 0)) {

    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("FTE: Tasks");

    if (canceledCounter > 0) {
      isb->addString(" canceled=");
      isb->addInt(canceledCounter);
    }

    if (executedCounter > 0) {
      isb->addString(" executed=");
      isb->addInt(executedCounter);
      isb->addString(" in ");
      isb->addLong(rc->getFrameStartTimer()->elapsedTimeInMilliseconds());
      isb->addString("ms");
    }

    isb->addString(" queued=");
    isb->addInt(preRenderTasksSize);

    if (_stressed) {
      isb->addString(" *Stressed*");
    }

    const std::string msg = isb->getString();
    delete isb;
    
    rc->getLogger()->logInfo(msg);
  }
}
