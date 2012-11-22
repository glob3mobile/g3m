package org.glob3.mobile.generated; 
//***************************************************************

public class BusyMeshEffect extends EffectWithForce
{
  private BusyMeshRenderer _renderer;


  public BusyMeshEffect(BusyMeshRenderer renderer)
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
	_renderer.incDegrees(5);
  }

  public void stop(G3MRenderContext rc, TimeInterval now)
  {
  }

  public void cancel(TimeInterval now)
  {
	// do nothing, just leave the effect in the intermediate state
  }

}