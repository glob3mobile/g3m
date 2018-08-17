package org.glob3.mobile.generated;import java.util.*;

public class MarkZoomInEffect extends EffectWithDuration
{
  private Mark _mark;
  private final float _initialSize;

  public MarkZoomInEffect(Mark mark, TimeInterval timeInterval)
  {
	  this(mark, timeInterval, 0.01f);
  }
  public MarkZoomInEffect(Mark mark)
  {
	  this(mark, TimeInterval.fromMilliseconds(400), 0.01f);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: MarkZoomInEffect(Mark* mark, const TimeInterval& timeInterval = TimeInterval::fromMilliseconds(400), const float initialSize = 0.01f) : EffectWithDuration(timeInterval, false), _mark(mark), _initialSize(initialSize)
  public MarkZoomInEffect(Mark mark, TimeInterval timeInterval, float initialSize)
  {
	  super(timeInterval, false);
	  _mark = mark;
	  _initialSize = initialSize;
	_mark.setOnScreenSizeOnProportionToImage(_initialSize, _initialSize);
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double alpha = getAlpha(when);
	final double alpha = getAlpha(new TimeInterval(when));
	float s = (float)(((1.0 - _initialSize) * alpha) + _initialSize);
	_mark.setOnScreenSizeOnProportionToImage(s, s);
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
	_mark.setOnScreenSizeOnProportionToImage(1, 1);
  }

  public final void cancel(TimeInterval when)
  {
	_mark.setOnScreenSizeOnProportionToImage(1, 1);
  }

}
