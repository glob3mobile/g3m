package org.glob3.mobile.generated; 
 //: public Renderer

public class DummyEffect extends EffectWithDuration
{

  public DummyEffect(TimeInterval duration)
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
	rc.getCamera().moveForward((percent-_lastPercent)*1e7);
	_lastPercent = percent;
  }

  public void stop(RenderContext rc, TimeInterval now)
  {
	super.stop(rc, now);
  }

  private double _lastPercent;
}