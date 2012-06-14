package org.glob3.mobile.generated; 
public class DummyEffect extends Effect
{
  private int _counter;


  public void start(RenderContext rc, TimeInterval now)
  {
	rc.getLogger().logInfo("start %i", now.milliseconds());

	_counter = 0;
  }

  public void doStep(RenderContext rc, TimeInterval now)
  {
	rc.getLogger().logInfo("doStep %i", now.milliseconds());
  }

  public boolean isDone(RenderContext rc, TimeInterval now)
  {
	rc.getLogger().logInfo("isDone %i", now.milliseconds());

	return ++_counter >= 2;
  }

  public void stop(RenderContext rc, TimeInterval now)
  {
	rc.getLogger().logInfo("stop %i", now.milliseconds());
  }
}