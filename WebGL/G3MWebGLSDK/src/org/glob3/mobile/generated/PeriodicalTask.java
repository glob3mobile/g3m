package org.glob3.mobile.generated; 
//
//  PeriodicalTask.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//Storing Scheduled Tasks
public class PeriodicalTask
{
  public PeriodicalTask(TimeInterval interval, GTask task)
  {
	  _interval = interval.milliseconds();
	  _task = task;
	  _lastExecution = 0;
  }

  public final void releaseTask()
  {
	if (_task != null)
		_task.dispose();
  }

  public final void executeIfNecessary()
  {
	ITimer t = IFactory.instance().createTimer();
	long now = t.now().milliseconds();
	IFactory.instance().deleteTimer(t);

	long interval = now - _lastExecution;

	if (interval >= _interval)
	{
	  _task.run();
	  _lastExecution = now;
	}
  }

  //Milliseconds
  private long _interval;
  private long _lastExecution;
  private GTask _task;
}