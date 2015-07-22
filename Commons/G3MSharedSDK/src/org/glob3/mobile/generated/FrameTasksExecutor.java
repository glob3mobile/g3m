package org.glob3.mobile.generated; 
//
//  FrameTasksExecutor.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/08/12.
//
//

//
//  FrameTasksExecutor.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/08/12.
//
//



//class G3MRenderContext;
//class FrameTask;


public class FrameTasksExecutor
{
  private final int _minimumExecutionsPerFrame;
  private final int _maximumExecutionsPerFrame;
  private final int _maximumQueuedTasks;
  private final long _maxTimePerFrameMS;
  private final long _maxTimePerFrameStressedMS;
  private final boolean _debug;

  private java.util.LinkedList<FrameTask> _tasks = new java.util.LinkedList<FrameTask>();

  private boolean canExecutePreRenderStep(G3MRenderContext rc, int executedCounter)
  {
    final int tasksCount = _tasks.size();
    if (tasksCount <= _minimumExecutionsPerFrame)
    {
      if (_debug)
      {
        if (_stressed)
        {
          rc.getLogger().logWarning("FTE: Abandon STRESSED mode");
        }
      }
      _stressed = false;
    }
  
    if (tasksCount == 0)
    {
      return false;
    }
  
    if (executedCounter < _minimumExecutionsPerFrame)
    {
      return true;
    }
  
    if (tasksCount > _maximumQueuedTasks)
    {
      if (_debug)
      {
        if (!_stressed)
        {
          rc.getLogger().logWarning("FTE: Too many queued tasks (%d). Goes to STRESSED mode", _tasks.size());
        }
      }
      _stressed = true;
    }
  
    if (_stressed)
    {
      return rc.getFrameStartTimer().elapsedTimeInMilliseconds() < _maxTimePerFrameStressedMS;
    }
  
    if (executedCounter >= _maximumExecutionsPerFrame)
    {
      return false;
    }
    return rc.getFrameStartTimer().elapsedTimeInMilliseconds() < _maxTimePerFrameMS;
  }

  private boolean _stressed;

  private void showDebugInfo(G3MRenderContext rc, int executedCounter, int canceledCounter)
  {
    final int preRenderTasksSize = _tasks.size();
    if ((executedCounter > 0) || (canceledCounter > 0) || (preRenderTasksSize > 0))
    {
  
      IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addString("FTE: Tasks");
  
      if (canceledCounter > 0)
      {
        isb.addString(" canceled=");
        isb.addInt(canceledCounter);
      }
  
      if (executedCounter > 0)
      {
        isb.addString(" executed=");
        isb.addInt(executedCounter);
        isb.addString(" in ");
        isb.addLong(rc.getFrameStartTimer().elapsedTimeInMilliseconds());
        isb.addString("ms");
      }
  
      isb.addString(" queued=");
      isb.addInt(preRenderTasksSize);
  
      if (_stressed)
      {
        isb.addString(" *Stressed*");
      }
  
      final String msg = isb.getString();
      if (isb != null)
         isb.dispose();
  
      rc.getLogger().logInfo(msg);
    }
  }

  public FrameTasksExecutor()
  {
     _minimumExecutionsPerFrame = 1;
     _maximumExecutionsPerFrame = 4;
     _maximumQueuedTasks = 64;
     _maxTimePerFrameMS = 10;
     _maxTimePerFrameStressedMS = 20;
     _stressed = false;
     _debug = false;
  }

  public final void addPreRenderTask(FrameTask task)
  {
    _tasks.addLast(task);
  }

  public final void doPreRenderCycle(G3MRenderContext rc)
  {
    if (_tasks.isEmpty())
    {
      return;
    }
  
    // remove canceled tasks
    int canceledCounter = 0;
    java.util.Iterator<FrameTask> i = _tasks.iterator();
    while (i.hasNext())
    {
      FrameTask task = i.next();
  
      final boolean isCanceled = task.isCanceled(rc);
      if (isCanceled)
      {
        if (task != null)
           task.dispose();
        i.remove();
        canceledCounter++;
      }
      else
      {
      }
    }
  
    if (_debug)
    {
      if (canceledCounter > 0)
      {
        rc.getLogger().logInfo("FTE: Removed %d tasks, active %d tasks.", canceledCounter, _tasks.size());
      }
    }
  
  
    // execute some tasks
    int executedCounter = 0;
    while (canExecutePreRenderStep(rc, executedCounter))
    {
      FrameTask task = _tasks.getFirst();
      _tasks.removeFirst();
  
      task.execute(rc);
  
      if (task != null)
         task.dispose();
  
      executedCounter++;
    }
  
  
    if (_debug)
    {
      showDebugInfo(rc, executedCounter, canceledCounter);
    }
  }

  public void dispose()
  {
  }

}