package org.glob3.mobile.generated; 
public abstract class EffectWithDuration extends Effect {
  private long _started;
  private final long _duration;


  protected EffectWithDuration(TimeInterval duration) {
     _started = 0;
     _duration = duration.milliseconds();

  }

  protected final double percentDone(TimeInterval when) {
    final long elapsed = when.milliseconds() - _started;

    final double percent = (double) elapsed / _duration;
    if (percent > 1)
       return 1;
    if (percent < 0)
       return 0;
    return percent;
  }


  //  virtual void stop(const G3MRenderContext *rc,
  //                    const TimeInterval& when) {
  //
  //  }

  public void start(G3MRenderContext rc, TimeInterval when) {
    _started = when.milliseconds();
  }

  public boolean isDone(G3MRenderContext rc, TimeInterval when) {
    final double percent = percentDone(when);
    return percent >= 1;
  }

}