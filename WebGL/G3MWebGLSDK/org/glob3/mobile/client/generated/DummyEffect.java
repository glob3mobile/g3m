package org.glob3.mobile.client.generated; 
public class DummyEffect extends EffectWithDuration
{

  public DummyEffect(TimeInterval duration)
  {
	  super(duration);
  }

  public void start(RenderContext rc, TimeInterval now)
  {
	super.start(rc, now);
  }

  public void doStep(RenderContext rc, TimeInterval now)
  {
	final double percent = pace(percentDone(now));

	rc.getCamera().zoom(1 - (percent / 50));
//    rc->getCamera()->pivotOnCenter(Angle::fromDegrees(1));
  }

  public void stop(RenderContext rc, TimeInterval now)
  {
	super.stop(rc, now);
  }

}