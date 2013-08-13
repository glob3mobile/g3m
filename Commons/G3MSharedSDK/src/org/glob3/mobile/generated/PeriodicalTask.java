package org.glob3.mobile.generated; 
//
//  PeriodicalTask.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class PeriodicalTask
{
  private long _intervalMS;
  private long _lastExecutionMS;
  private GTask _task;

  private ITimer _timer;

  private ITimer getTimer()
  {
    if (_timer == null)
    {
      _timer = IFactory.instance().createTimer();
    }
    return _timer;
  }

  public PeriodicalTask(TimeInterval interval, GTask task)
  {
     _intervalMS = interval.milliseconds();
     _task = task;
     _lastExecutionMS = 0;
     _timer = null;
  }

  public void dispose()
  {
    if (_task != null)
       _task.dispose();

    IFactory.instance().deleteTimer(_timer);
    super.dispose();
  }

  public final void resetTimeout()
  {
    _lastExecutionMS = 0;
  }

  public final void executeIfNecessary(G3MContext context)
  {
    long now = getTimer().now().milliseconds();

    long interval = now - _lastExecutionMS;

    if (interval >= _intervalMS)
    {
      _task.run(context);
      _lastExecutionMS = now;
    }
  }

}