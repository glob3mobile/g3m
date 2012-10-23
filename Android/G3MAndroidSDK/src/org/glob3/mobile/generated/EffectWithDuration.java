package org.glob3.mobile.generated; 
public abstract class EffectWithDuration extends Effect
{
  private long _started;
  private final long _duration;


  protected EffectWithDuration(TimeInterval duration)
  {
	  _started = 0;
	  _duration = duration.milliseconds();

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double percentDone(const TimeInterval& now) const
  protected final double percentDone(TimeInterval now)
  {
	final long elapsed = now.milliseconds() - _started;

	final double percent = (double) elapsed / _duration;
	if (percent > 1)
		return 1;
	if (percent < 0)
		return 0;
	return percent;
  }


  public void stop(RenderContext rc, TimeInterval now)
  {

  }

  public void start(RenderContext rc, TimeInterval now)
  {
	_started = now.milliseconds();
  }

  public boolean isDone(RenderContext rc, TimeInterval now)
  {
	final double percent = percentDone(now);
	return percent >= 1;
  }

}