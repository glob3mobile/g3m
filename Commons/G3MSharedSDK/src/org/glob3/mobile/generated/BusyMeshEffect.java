<<<<<<< HEAD
package org.glob3.mobile.generated; 
//***************************************************************

public class BusyMeshEffect extends EffectWithForce
{
  private BusyMeshRenderer _renderer;


  public BusyMeshEffect(BusyMeshRenderer renderer)
  {
     super(1, 1);
     _renderer = renderer;
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    super.doStep(rc, when);
    _renderer.incDegrees(5);
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
  }

  public final void cancel(TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }

=======
package org.glob3.mobile.generated; 
//***************************************************************

public class BusyMeshEffect extends EffectWithForce
{
  private BusyMeshRenderer _renderer;
  private long _lastMS;


  public BusyMeshEffect(BusyMeshRenderer renderer)
  {
     super(1, 1);
     _renderer = renderer;
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
    _lastMS = when.milliseconds();
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    super.doStep(rc, when);

    final long now = when.milliseconds();
    final long ellapsed = now - _lastMS;
    _lastMS = now;

    final double deltaDegrees = (360.0 / 1200.0) * ellapsed;

    _renderer.incDegrees(deltaDegrees);
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
  }

  public final void cancel(TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }

>>>>>>> point-cloud
}