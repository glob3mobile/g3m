package org.glob3.mobile.generated; 
//***************************************************************

public class SingleTranslationEffect extends EffectWithForce
{

  public SingleTranslationEffect(Vector3D desp)
  {
     super(1, 0.92);
     _direction = new Vector3D(desp);
  }

  public void start(G3MRenderContext rc, TimeInterval when)
  {
  }

  public void doStep(G3MRenderContext rc, TimeInterval when)
  {
    super.doStep(rc, when);
    rc.getNextCamera().translateCamera(_direction.times(getForce()));
  }

  public void stop(G3MRenderContext rc, TimeInterval when)
  {
    rc.getNextCamera().translateCamera(_direction.times(getForce()));
  }

  public void cancel(TimeInterval when)
  {
  }

  private Vector3D _direction ;
}