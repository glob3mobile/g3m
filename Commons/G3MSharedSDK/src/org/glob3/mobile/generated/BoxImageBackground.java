package org.glob3.mobile.generated;
//
//  BoxImageBackground.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

//
//  BoxImageBackground.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//






/*
 ..............................................
 . margin                                     .
 .  +-border-------------------------------+  .
 .  | padding                              |  .
 .  |  #content##########################  |  .
 .  |  ##################################  |  .
 .  |  ##################################  |  .
 .  |                                      |  .
 .  +--------------------------------------+  .
 .                                            .
 ..............................................
 */


public class BoxImageBackground extends ImageBackground
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  BoxImageBackground(BoxImageBackground that);

  private final Vector2F _margin;
  private final float _borderWidth;
  private final Color _borderColor ;
  private final Vector2F _padding;
  private final Color _backgroundColor ;
  private final float _cornerRadius;


  public BoxImageBackground(Vector2F margin, float borderWidth, Color borderColor, Vector2F padding, Color backgroundColor, float cornerRadius)
  {
     _margin = margin;
     _borderWidth = borderWidth;
     _borderColor = borderColor;
     _padding = padding;
     _backgroundColor = backgroundColor;
     _cornerRadius = cornerRadius;
  
  }


  public final Vector2F initializeCanvas(ICanvas canvas, float contentWidth, float contentHeight)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final float canvasWidth = contentWidth + ((_margin._x + _borderWidth + _padding._x) * 2);
    final float canvasHeight = contentHeight + ((_margin._y + _borderWidth + _padding._y) * 2);
  
    canvas.initialize((int) mu.ceil(canvasWidth), (int) mu.ceil(canvasHeight));
  
    final float boxWidth = canvasWidth - ((_margin._x + _borderWidth) * 2);
    final float boxHeight = canvasHeight - ((_margin._y + _borderWidth) * 2);
  
    ///#warning remove debug code
    //  canvas->setFillColor(Color::red());
    //  canvas->fillRectangle(0, 0, canvasWidth, canvasHeight);
  
  
    if (!_backgroundColor.isFullTransparent())
    {
      canvas.setFillColor(_backgroundColor);
      if (_cornerRadius > 0)
      {
        canvas.fillRoundedRectangle(_margin._x, _margin._y, boxWidth, boxHeight, _cornerRadius);
      }
      else
      {
        canvas.fillRectangle(_margin._x, _margin._y, boxWidth, boxHeight);
      }
    }
  
    if (_borderWidth > 0 && !_borderColor.isFullTransparent())
    {
      canvas.setLineColor(_borderColor);
      canvas.setLineWidth(_borderWidth);
      if (_cornerRadius > 0)
      {
        canvas.strokeRoundedRectangle(_margin._x, _margin._y, boxWidth, boxHeight, _cornerRadius);
      }
      else
      {
        canvas.strokeRectangle(_margin._x, _margin._y, boxWidth, boxHeight);
      }
    }
  
    final Vector2F contentPosition = new Vector2F((_margin._x + _borderWidth + _padding._x), (_margin._y + _borderWidth + _padding._y));
    return contentPosition;
  }


  // IStringBuilder keeps every float digit; IStringUtils::toString(float) keeps only 6 on iOS
  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Box/");
    isb.addString(_margin.description());
    isb.addString("/");
    isb.addFloat(_borderWidth);
    isb.addString("/");
    isb.addString(_borderColor.id());
    isb.addString("/");
    isb.addString(_padding.description());
    isb.addString("/");
    isb.addString(_backgroundColor.id());
    isb.addString("/");
    isb.addFloat(_cornerRadius);
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final BoxImageBackground copy()
  {
    return new BoxImageBackground(_margin, _borderWidth, _borderColor, _padding, _backgroundColor, _cornerRadius);
  }

}