package org.glob3.mobile.generated; 
public abstract class EffectWithForce extends Effect
{
  private double _force;
  private final double _friction;

  protected EffectWithForce(double force, double friction)
  {
     _force = force;
     _friction = friction;
  }

  protected final double getForce()
  {
    return _force;
  }

  public void doStep(G3MRenderContext rc, TimeInterval when)
  {
    _force *= _friction;
  }

  public boolean isDone(G3MRenderContext rc, TimeInterval when)
  {
    return (IMathUtils.instance().abs(_force) < 0.005);
  }

}