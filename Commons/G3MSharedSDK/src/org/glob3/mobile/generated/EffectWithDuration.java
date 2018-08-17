package org.glob3.mobile.generated;public abstract class EffectWithDuration extends Effect
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double percentDone(const TimeInterval& when) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getAlpha(const TimeInterval& when) const
  protected final double getAlpha(TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double percent = percentDone(when);
	final double percent = percentDone(new TimeInterval(when));
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double percent = percentDone(when);
	final double percent = percentDone(new TimeInterval(when));
	return (percent >= 1);
  }
}
