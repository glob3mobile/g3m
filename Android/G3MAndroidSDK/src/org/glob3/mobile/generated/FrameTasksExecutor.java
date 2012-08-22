package org.glob3.mobile.generated; 
public class FrameTasksExecutor
{
  private final int _minimumExecutionsPerFrame;
  private final int _maximumExecutionsPerFrame;
  private final int _maximumQueuedTasks;
  private final TimeInterval _maxTimePerFrame = new TimeInterval();
  private final TimeInterval _maxTimePerFrameStressed = new TimeInterval();

  private java.util.LinkedList<FrameTask> _preRenderTasks = new java.util.LinkedList<FrameTask>();

  private boolean canExecutePreRenderStep(RenderContext rc, int executedCounter)
  {
	final int tasksCount = _preRenderTasks.size();
	if (tasksCount == 0)
	{
	  if (_stressed)
	  {
  //      rc->getLogger()->logWarning("Abandon STRESSED mode");
		_stressed = false;
	  }
	  return false;
	}
  
	if (executedCounter < _minimumExecutionsPerFrame)
	{
	  return true;
	}
  
	if (tasksCount > _maximumQueuedTasks)
	{
  //    if (!_stressed) {
  //      rc->getLogger()->logWarning("Too many queued tasks (%d). Goes to STRESSED mode",
  //                                  _preRenderTasks.size());
  //    }
  
	  _stressed = true;
	}
  
	if (_stressed)
	{
	  return rc.getFrameStartTimer().elapsedTime().lowerThan(_maxTimePerFrameStressed);
	}
  
	if (executedCounter >= _maximumExecutionsPerFrame)
	{
	  return false;
	}
	return rc.getFrameStartTimer().elapsedTime().lowerThan(_maxTimePerFrame);
  
  
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

  private boolean _stressed;

  public FrameTasksExecutor()
//  _maximumQueuedTasks(128),
//  _maxTimePerFrameStressed(TimeInterval::fromMilliseconds(30)),
//  _maxTimePerFrameStressed(TimeInterval::fromMilliseconds(45)),
  {
	  _minimumExecutionsPerFrame = 1;
	  _maximumExecutionsPerFrame = 2;
	  _maximumQueuedTasks = 64;
	  _maxTimePerFrame = TimeInterval.fromMilliseconds(10);
	  _maxTimePerFrameStressed = TimeInterval.fromMilliseconds(25);
	  _stressed = false;
	int __TODO_tune_render_budget;
  }

  public final void addPreRenderTask(FrameTask preRenderTask)
  {
	_preRenderTasks.addLast(preRenderTask);
  }

  public final void doPreRenderCycle(RenderContext rc)
  {
	int executedCounter = 0;
	while (canExecutePreRenderStep(rc, executedCounter))
	{
	  FrameTask task = _preRenderTasks.getFirst();
	  _preRenderTasks.removeFirst();
  
	  task.execute(rc);
  
	  if (task != null)
		  task.dispose();
  
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

}