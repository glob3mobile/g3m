package org.glob3.mobile.generated;
//
//  UnderlinedBoxImageBackground.cpp
//  G3M
//

//
//  UnderlinedBoxImageBackground.hpp
//  G3M
//





/*
 ..............................................
 . margin                                     .
 .  /--------------------------------------\  .
 .  | padding                              |  .
 .  |  #content##########################  |  .
 .  |  ##################################  |  .
 .  |                                      |  .
 .  ========================================  .
 .                                            .
 ..............................................
 a box with rounded top corners, square bottom corners and a line along its
 bottom, inside the box: the image keeps the size of a BoxImageBackground's
 */


public class UnderlinedBoxImageBackground extends ImageBackground
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  UnderlinedBoxImageBackground(UnderlinedBoxImageBackground that);

  private final Vector2F _margin;
  private final Vector2F _padding;
  private final Color _backgroundColor ;
  private final float _cornerRadius;
  private final Color _lineColor ;
  private final float _lineWidth;

  public UnderlinedBoxImageBackground(Vector2F margin, Vector2F padding, Color backgroundColor, float cornerRadius, Color lineColor, float lineWidth)
  {
     _margin = margin;
     _padding = padding;
     _backgroundColor = backgroundColor;
     _cornerRadius = cornerRadius;
     _lineColor = lineColor;
     _lineWidth = lineWidth;
  
  }

  public final Vector2F initializeCanvas(ICanvas canvas, float contentWidth, float contentHeight)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final float canvasWidth = contentWidth + ((_margin._x + _padding._x) * 2);
    final float canvasHeight = contentHeight + ((_margin._y + _padding._y) * 2);
  
    canvas.initialize((int) mu.ceil(canvasWidth), (int) mu.ceil(canvasHeight));
  
    final float boxWidth = canvasWidth - (_margin._x * 2);
    final float boxHeight = canvasHeight - (_margin._y * 2);
    final float boxBottom = _margin._y + boxHeight;
  
    if (!_backgroundColor.isFullTransparent())
    {
      canvas.setFillColor(_backgroundColor);
      if (_cornerRadius > 0)
      {
        canvas.fillRoundedRectangle(_margin._x, _margin._y, boxWidth, boxHeight, _cornerRadius);
        // squares the bottom corners
        canvas.fillRectangle(_margin._x, boxBottom - _cornerRadius, boxWidth, _cornerRadius);
      }
      else
      {
        canvas.fillRectangle(_margin._x, _margin._y, boxWidth, boxHeight);
      }
    }
  
    if ((_lineWidth > 0) && !_lineColor.isFullTransparent())
    {
      canvas.setFillColor(_lineColor);
      canvas.fillRectangle(_margin._x, boxBottom - _lineWidth, boxWidth, _lineWidth);
    }
  
    return new Vector2F(_margin._x + _padding._x, _margin._y + _padding._y);
  }


  // IStringBuilder keeps every float digit; IStringUtils::toString(float) keeps only 6 on iOS
  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("UnderlinedBox/");
    isb.addString(_margin.description());
    isb.addString("/");
    isb.addString(_padding.description());
    isb.addString("/");
    isb.addString(_backgroundColor.id());
    isb.addString("/");
    isb.addFloat(_cornerRadius);
    isb.addString("/");
    isb.addString(_lineColor.id());
    isb.addString("/");
    isb.addFloat(_lineWidth);
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final UnderlinedBoxImageBackground copy()
  {
    return new UnderlinedBoxImageBackground(_margin, _padding, _backgroundColor, _cornerRadius, _lineColor, _lineWidth);
  }

}