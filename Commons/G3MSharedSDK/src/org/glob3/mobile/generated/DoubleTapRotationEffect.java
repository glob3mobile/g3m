package org.glob3.mobile.generated;public class DoubleTapRotationEffect extends EffectWithDuration
{
  private final Vector3D _axis = new Vector3D();
  private final Angle _angle = new Angle();
  private final double _distance;
  private double _lastAlpha;

  public DoubleTapRotationEffect(TimeInterval duration, Vector3D axis, Angle angle, double distance)
  {
	  this(duration, axis, angle, distance, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: DoubleTapRotationEffect(const TimeInterval& duration, const Vector3D& axis, const Angle& angle, double distance, const boolean linearTiming =false): EffectWithDuration(duration, linearTiming), _axis(axis), _angle(angle), _distance(distance)
  public DoubleTapRotationEffect(TimeInterval duration, Vector3D axis, Angle angle, double distance, boolean linearTiming)
  {
	  super(duration, linearTiming);
	  _axis = new Vector3D(axis);
	  _angle = new Angle(angle);
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: camera->rotateWithAxis(_axis, _angle.times(step));
	camera.rotateWithAxis(new Vector3D(_axis), _angle.times(step));
	camera.moveForward(_distance * step);
	_lastAlpha = alpha;
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
	Camera camera = rc.getNextCamera();
	final double step = 1.0 - _lastAlpha;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: camera->rotateWithAxis(_axis, _angle.times(step));
	camera.rotateWithAxis(new Vector3D(_axis), _angle.times(step));
	camera.moveForward(_distance * step);
  }

  public final void cancel(TimeInterval when)
  {
  }

}
