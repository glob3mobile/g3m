package org.glob3.mobile.generated; 
public class MarkZoomInEffect extends EffectWithDuration
{
  private Mark _mark;
  private final float _initialSize;

  public MarkZoomInEffect(Mark mark)
  {
     this(mark, TimeInterval.fromMilliseconds(400));
  }
  public MarkZoomInEffect(Mark mark, TimeInterval timeInterval)
  {
     super(timeInterval, false);
     _mark = mark;
     _initialSize = 0.01f;
    _mark.setOnScreenSizeOnProportionToImage(_initialSize, _initialSize);
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);
    float s = (float)(alpha * (1.0 - _initialSize) + _initialSize);
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