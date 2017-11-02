package org.glob3.mobile.generated;
public class HUDRelativePosition extends HUDPosition
{

  private final float _factor;
  private final float _margin;
  private final HUDRelativePositionAnchor _relativeTo;
  private final HUDRelativePositionAlign _align;

  public HUDRelativePosition(float factor, HUDRelativePositionAnchor relativeTo, HUDRelativePositionAlign align)
  {
     this(factor, relativeTo, align, 0);
  }
  public HUDRelativePosition(float factor, HUDRelativePositionAnchor relativeTo, HUDRelativePositionAlign align, float margin)
  {
     _factor = factor;
     _relativeTo = relativeTo;
     _align = align;
     _margin = margin;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final float getPosition(int viewPortWidth, int viewPortHeight, float widgetWidth, float widgetHeight)
  {
  
    final float position = _factor * ((_relativeTo == HUDRelativePositionAnchor.VIEWPORT_WIDTH) ? viewPortWidth : viewPortHeight);
  
    switch (_align)
    {
      case LEFT:
        return position - widgetWidth - _margin;
      case RIGHT:
        return position + _margin;
      case CENTER:
        return position - (widgetWidth / 2) - _margin;
  
      case ABOVE:
        return position + _margin;
      case BELOW:
        return position - widgetHeight - _margin;
      case MIDDLE:
        return position - (widgetHeight / 2) - _margin;
    }
  
    return position;
  }

}
