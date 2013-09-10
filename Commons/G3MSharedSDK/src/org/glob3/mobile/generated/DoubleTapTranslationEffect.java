package org.glob3.mobile.generated; 
//***************************************************************

public class DoubleTapTranslationEffect extends EffectWithDuration
{

  public DoubleTapTranslationEffect(TimeInterval duration, Vector3D translation, double distance)
  {
     this(duration, translation, distance, false);
  }
  public DoubleTapTranslationEffect(TimeInterval duration, Vector3D translation, double distance, boolean linearTiming)
  {
     super(duration, linearTiming);
     _translation = new Vector3D(translation);
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
    camera.translateCamera(_translation.times(step));
    camera.moveForward(_distance * step);
    _lastAlpha = alpha;
  }

  public void stop(G3MRenderContext rc, TimeInterval when)
  {
    Camera camera = rc.getNextCamera();
    final double step = 1.0 - _lastAlpha;
    camera.translateCamera(_translation.times(step));
    camera.moveForward(_distance * step);
  }

  public void cancel(TimeInterval when)
  {
  }

  private Vector3D _translation ;
  private double _distance;
  private double _lastAlpha;
}