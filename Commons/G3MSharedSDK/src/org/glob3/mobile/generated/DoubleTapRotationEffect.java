package org.glob3.mobile.generated; 
public class DoubleTapRotationEffect extends EffectWithDuration
{
  private final Vector3D _axis ;
  private final Angle _angle ;
  private final double _distance;
  private double _lastAlpha;

  public DoubleTapRotationEffect(TimeInterval duration, Vector3D axis, Angle angle, double distance)
  {
     this(duration, axis, angle, distance, false);
  }
  public DoubleTapRotationEffect(TimeInterval duration, Vector3D axis, Angle angle, double distance, boolean linearTiming)
  {
     super(duration, linearTiming);
     _axis = new Vector3D(axis);
     _angle = new Angle(angle);
     _distance = distance;
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
    super.start(rc, when);
    _lastAlpha = 0;
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);
    Camera camera = rc.getNextCamera();
    final double step = alpha - _lastAlpha;
    camera.rotateWithAxis(_axis, _angle.times(step));
    camera.moveForward(_distance * step);
    _lastAlpha = alpha;
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    Camera camera = rc.getNextCamera();
    final double step = 1.0 - _lastAlpha;
    camera.rotateWithAxis(_axis, _angle.times(step));
    camera.moveForward(_distance * step);
  }

  public final void cancel(TimeInterval when)
  {
  }

}