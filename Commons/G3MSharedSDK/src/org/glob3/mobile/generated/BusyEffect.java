package org.glob3.mobile.generated; 
//***************************************************************

public class BusyEffect extends EffectNeverEnding
{
  private BusyQuadRenderer _renderer;


  public BusyEffect(BusyQuadRenderer renderer)
  {
     super();
     _renderer = renderer;
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    super.doStep(rc, when);
    _renderer.incDegrees(3);
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
  }

  public final void cancel(TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }

}