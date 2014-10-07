package org.glob3.mobile.generated; 
public class SingleTranslationEffect extends EffectWithForce
{
  private final Vector3D _direction ;


  public SingleTranslationEffect(Vector3D desp)
  {
     super(1, 0.92);
     _direction = new Vector3D(desp);
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    super.doStep(rc, when);
    rc.getNextCamera().translateCamera(_direction.times(getForce()));
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    rc.getNextCamera().translateCamera(_direction.times(getForce()));
  }

  public final void cancel(TimeInterval when)
  {
  }

}