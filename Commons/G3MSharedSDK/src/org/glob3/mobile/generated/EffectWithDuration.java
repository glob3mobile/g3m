package org.glob3.mobile.generated; 
public abstract class EffectWithDuration extends Effect
{
  private long _started;
  private final long _duration;
  private final boolean _linearTiming;


  protected EffectWithDuration(TimeInterval duration, boolean linearTiming)
  {
     _started = 0;
     _duration = duration.milliseconds();
     _linearTiming = linearTiming;

  }

  protected final double percentDone(TimeInterval when)
  {
    final long elapsed = when.milliseconds() - _started;

    final double percent = (double) elapsed / _duration;
    if (percent > 1)
       return 1;
    if (percent < 0)
       return 0;
    return percent;
  }

  protected final double getAlpha(TimeInterval when)
  {
    if (_linearTiming)
    {
      return percentDone(when);
    }
    return pace(percentDone(when));
  }


  //  virtual void stop(const G3MRenderContext *rc,
  //                    const TimeInterval& when) {
  //
  //  }

  public void start(G3MRenderContext rc, TimeInterval when)
  {
    _started = when.milliseconds();
  }

  public boolean isDone(G3MRenderContext rc, TimeInterval when)
  {
    final double percent = getAlpha(when);
    return percent >= 1;
  }

}