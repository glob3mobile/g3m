package org.glob3.mobile.generated; 
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
  public MarkZoomInEffect(Mark mark, TimeInterval timeInterval, float initialSize)
  {
     super(timeInterval, false);
     _mark = mark;
     _initialSize = initialSize;
    _mark.setOnScreenSizeOnProportionToImage(_initialSize, _initialSize);
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);
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