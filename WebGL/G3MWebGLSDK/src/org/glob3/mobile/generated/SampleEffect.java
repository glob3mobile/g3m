package org.glob3.mobile.generated; 
//***************************************************************


public class SampleEffect extends EffectWithDuration
{

  public SampleEffect(TimeInterval duration)
  {
	  super(duration);
  }

  public void start(RenderContext rc, TimeInterval now)
  {
	super.start(rc, now);
	_lastPercent = 0;
  }

  public void doStep(RenderContext rc, TimeInterval now)
  {
	final double percent = pace(percentDone(now));
	rc.getNextCamera().moveForward((percent-_lastPercent)*1e7);
	_lastPercent = percent;
  }

  public void stop(RenderContext rc, TimeInterval now)
  {
	super.stop(rc, now);
  }

  public void cancel(TimeInterval now)
  {
	// do nothing, just leave the effect in the intermediate state
  }

  private double _lastPercent;
}