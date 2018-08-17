package org.glob3.mobile.generated;public class SingleTranslationEffect extends EffectWithForce
{
  private final Vector3D _direction = new Vector3D();


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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: EffectWithForce::doStep(rc, when);
	super.doStep(rc, new TimeInterval(when));
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
