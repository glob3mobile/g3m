package org.glob3.mobile.generated; 
//***************************************************************

public class BusyEffect extends EffectWithForce
{
  private BusyQuadRenderer _renderer;


  public BusyEffect(BusyQuadRenderer renderer)
  {
	  super(1, 1);
	  _renderer = renderer;
  }

  public void start(G3MRenderContext rc, TimeInterval now)
  {
  }

  public void doStep(G3MRenderContext rc, TimeInterval now)
  {
	super.doStep(rc, now);
	_renderer.incDegrees(3);
  }

  public void stop(G3MRenderContext rc, TimeInterval now)
  {
  }

  public void cancel(TimeInterval now)
  {
	// do nothing, just leave the effect in the intermediate state
  }

}