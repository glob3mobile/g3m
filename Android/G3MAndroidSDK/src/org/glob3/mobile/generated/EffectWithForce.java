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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getForce() const
  protected final double getForce()
  {
	return _force;
  }

  public void doStep(RenderContext rc, TimeInterval now)
  {
	_force *= _friction;
  }

  public boolean isDone(RenderContext rc, TimeInterval now)
  {
	return (IMathUtils.instance().abs(_force) < 1e-6);
  }

}