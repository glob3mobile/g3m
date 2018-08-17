package org.glob3.mobile.generated;//***************************************************************

public class BusyMeshEffect extends EffectNeverEnding
{
  private BusyMeshRenderer _renderer;
  private long _lastMS;


  public BusyMeshEffect(BusyMeshRenderer renderer)
  {
	  super();
	  _renderer = renderer;
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
	_lastMS = when.milliseconds();
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: EffectNeverEnding::doStep(rc, when);
	super.doStep(rc, new TimeInterval(when));

	final long now = when.milliseconds();
	final long ellapsed = now - _lastMS;
	_lastMS = now;

	final double deltaDegrees = (360.0 / 1200.0) * ellapsed;

	_renderer.incDegrees(deltaDegrees);
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
  }

  public final void cancel(TimeInterval when)
  {
	// do nothing, just leave the effect in the intermediate state
  }

}
