package org.glob3.mobile.generated;
public class MarkZoomOutAndRemoveEffect extends EffectWithDuration
{
  private Mark _mark;
  private MarksRenderer _renderer;
  private final boolean _deleteMarkOnDisappears;
  private final float _finalSize;

  public MarkZoomOutAndRemoveEffect(Mark mark, MarksRenderer renderer, boolean deleteMarkOnDisappears, TimeInterval timeInterval)
  {
     this(mark, renderer, deleteMarkOnDisappears, timeInterval, 0.01f);
  }
  public MarkZoomOutAndRemoveEffect(Mark mark, MarksRenderer renderer, boolean deleteMarkOnDisappears)
  {
     this(mark, renderer, deleteMarkOnDisappears, TimeInterval.fromMilliseconds(300), 0.01f);
  }
  public MarkZoomOutAndRemoveEffect(Mark mark, MarksRenderer renderer, boolean deleteMarkOnDisappears, TimeInterval timeInterval, float finalSize)
  {
     super(timeInterval, false);
     _mark = mark;
     _renderer = renderer;
     _deleteMarkOnDisappears = deleteMarkOnDisappears;
     _finalSize = finalSize;
    _mark.setScreenSizeScale(1, 1);
  }

  public void dispose()
  {
    if (_deleteMarkOnDisappears)
    {
      if (_mark != null)
      {
        Mark mark = _mark;
        _mark = null;
        if (mark != null)
           mark.dispose();
      }
    }
    super.dispose();
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    if (_mark != null)
    {
      final double alpha = getAlpha(when);
      final float s = 1.0f - (float)(((1.0 - _finalSize) * alpha) + _finalSize);
      _mark.setScreenSizeScale(s, s);
    }
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    if ((_mark != null) && (_renderer != null))
    {
      _renderer.removeMark(_mark);
      _renderer = null;
    }
  }

  public final void cancel(TimeInterval when)
  {
    if ((_mark != null) && (_renderer != null))
    {
      _renderer.removeMark(_mark);
      _renderer = null;
    }
  }

}