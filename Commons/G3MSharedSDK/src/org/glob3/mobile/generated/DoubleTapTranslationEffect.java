package org.glob3.mobile.generated;public class DoubleTapTranslationEffect extends EffectWithDuration
{
  private final Vector3D _translation = new Vector3D();
  private final double _distance;
  private double _lastAlpha;


  public DoubleTapTranslationEffect(TimeInterval duration, Vector3D translation, double distance)
  {
	  this(duration, translation, distance, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: DoubleTapTranslationEffect(const TimeInterval& duration, const Vector3D& translation, double distance, const boolean linearTiming =false): EffectWithDuration(duration, linearTiming), _translation(translation), _distance(distance)
  public DoubleTapTranslationEffect(TimeInterval duration, Vector3D translation, double distance, boolean linearTiming)
  {
	  super(duration, linearTiming);
	  _translation = new Vector3D(translation);
	  _distance = distance;
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: EffectWithDuration::start(rc, when);
	super.start(rc, new TimeInterval(when));
	_lastAlpha = 0;
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double alpha = getAlpha(when);
	final double alpha = getAlpha(new TimeInterval(when));
	Camera camera = rc.getNextCamera();
	final double step = alpha - _lastAlpha;
	camera.translateCamera(_translation.times(step));
	camera.moveForward(_distance * step);
	_lastAlpha = alpha;
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
	Camera camera = rc.getNextCamera();
	final double step = 1.0 - _lastAlpha;
	camera.translateCamera(_translation.times(step));
	camera.moveForward(_distance * step);
  }

  public final void cancel(TimeInterval when)
  {
  }

}
