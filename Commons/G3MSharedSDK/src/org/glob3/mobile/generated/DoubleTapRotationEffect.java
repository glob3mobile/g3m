package org.glob3.mobile.generated; 
//***************************************************************

public class DoubleTapRotationEffect extends EffectWithDuration
{

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

  public void start(G3MRenderContext rc, TimeInterval when)
  {
    super.start(rc, when);
    _lastAlpha = 0;
  }

  public void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);
    Camera camera = rc.getNextCamera();
    final double step = alpha - _lastAlpha;
    camera.rotateWithAxis(_axis, _angle.times(step));
    camera.moveForward(_distance * step);
    _lastAlpha = alpha;
  }

  public void stop(G3MRenderContext rc, TimeInterval when)
  {
    Camera camera = rc.getNextCamera();
    final double step = 1.0 - _lastAlpha;
    camera.rotateWithAxis(_axis, _angle.times(step));
    camera.moveForward(_distance * step);
  }

  public void cancel(TimeInterval when)
  {
  }

  private Vector3D _axis ;
  private Angle _angle ;
  private double _distance;
  private double _lastAlpha;
}