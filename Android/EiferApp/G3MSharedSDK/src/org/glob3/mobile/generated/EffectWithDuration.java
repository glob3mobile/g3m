package org.glob3.mobile.generated; 
public abstract class EffectWithDuration extends Effect
{
  private long _started;
  private final long _durationMS;
  private final boolean _linearTiming;


  protected EffectWithDuration(TimeInterval duration, boolean linearTiming)
  {
     _durationMS = duration._milliseconds;
     _linearTiming = linearTiming;
     _started = 0;

  }

  protected final double percentDone(TimeInterval when)
  {
    final long elapsed = when._milliseconds - _started;

    final double percent = (double) elapsed / _durationMS;
    if (percent > 1)
       return 1;
    if (percent < 0)
       return 0;
    return percent;
  }

  protected final double getAlpha(TimeInterval when)
  {
    final double percent = percentDone(when);
    return _linearTiming ? percent : pace(percent);
  }


  //  virtual void stop(const G3MRenderContext* rc,
  //                    const TimeInterval& when) {
  //
  //  }

  public void start(G3MRenderContext rc, TimeInterval when)
  {
    _started = when._milliseconds;
  }

  public boolean isDone(G3MRenderContext rc, TimeInterval when)
  {
    final double percent = percentDone(when);
    return (percent >= 1);
  }
}