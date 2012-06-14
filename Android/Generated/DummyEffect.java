package org.glob3.mobile.generated; 
public class DummyEffect extends Effect
{
  private int _started;
  private final int _duration;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double percentDone(const TimeInterval& now) const
  private double percentDone(TimeInterval now)
  {
	final int elapsed = now.milliseconds() - _started;

	final double percent = (double) elapsed / _duration;
	if (percent > 1)
		return 1;
	if (percent < 0)
		return 0;
	return percent;
  }


  public DummyEffect(TimeInterval duration)
  {
	  _started = 0;
	  _duration = duration.milliseconds();

  }

  public void start(RenderContext rc, TimeInterval now)
  {
	rc.getLogger().logInfo("start %i", now.milliseconds());

	_started = now.milliseconds();
  }

  public void doStep(RenderContext rc, TimeInterval now)
  {
//    rc->getLogger()->logInfo("doStep %i", now.milliseconds());
	final double percent = percentDone(now);

	rc.getCamera().zoom(1 - (percent / 25));
  }

  public boolean isDone(RenderContext rc, TimeInterval now)
  {
	final double percent = percentDone(now);

//    rc->getLogger()->logInfo("isDone %i, %f", now.milliseconds(), percent);

	return percent >= 1;
  }

  public void stop(RenderContext rc, TimeInterval now)
  {
	rc.getLogger().logInfo("stop %i", now.milliseconds());
  }
}