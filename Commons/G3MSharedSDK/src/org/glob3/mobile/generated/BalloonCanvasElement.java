package org.glob3.mobile.generated; 
//
//  BalloonCanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

//
//  BalloonCanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//



public class BalloonCanvasElement extends CanvasElement
{
  private CanvasElement _child;

  private final Color _color ;
  private final float _arrowLenght;
  private final float _margin;
  private final float _radius;
  private final float _arrowPointSize;

  public BalloonCanvasElement(CanvasElement child, Color color, float margin, float radius, float arrowLenght)
  {
     this(child, color, margin, radius, arrowLenght, 12);
  }
  public BalloonCanvasElement(CanvasElement child, Color color, float margin, float radius)
  {
     this(child, color, margin, radius, 5, 12);
  }
  public BalloonCanvasElement(CanvasElement child, Color color, float margin)
  {
     this(child, color, margin, 10, 5, 12);
  }
  public BalloonCanvasElement(CanvasElement child, Color color)
  {
     this(child, color, 5, 10, 5, 12);
  }
  public BalloonCanvasElement(CanvasElement child)
  {
     this(child, Color.white(), 5, 10, 5, 12);
  }
  public BalloonCanvasElement(CanvasElement child, Color color, float margin, float radius, float arrowLenght, float arrowPointSize)
  {
     _child = child;
     _color = new Color(color);
     _margin = margin;
     _radius = radius;
     _arrowLenght = arrowLenght;
     _arrowPointSize = arrowPointSize;

  }

  public void dispose()
  {
    if (_child != null)
       _child.dispose();
  }

  public final Vector2F getExtent(ICanvas canvas)
  {
    final Vector2F childExtent = _child.getExtent(canvas);
  
    final float twoMargin = _margin * 2;
    return new Vector2F(childExtent._x + twoMargin, (childExtent._y + _arrowLenght + twoMargin) * 2);
  }

  public final void drawAt(float left, float top, ICanvas canvas)
  {
    final Vector2F childExtent = _child.getExtent(canvas);
    final Vector2F extent = getExtent(canvas);
  
    canvas.setFillColor(_color);
  
  //  canvas->setStrokeColor(Color::black());
  //  canvas->setStrokeWidth(0.2f);
  
    final float halfArrowPointSize = _arrowPointSize / 2;
  //  canvas->fillRoundedRectangle(left + (extent._x / 2) - halfArrowPointSize,
  //                               top  + (extent._y / 2) - halfArrowPointSize,
  //                               _arrowPointSize, _arrowPointSize,
  //                               halfArrowPointSize);
  
    canvas.fillRoundedRectangle(left + (extent._x / 2) - halfArrowPointSize, top + (extent._y / 2) - halfArrowPointSize, _arrowPointSize, _arrowPointSize, halfArrowPointSize);
  
    canvas.fillRoundedRectangle(left, top, extent._x, childExtent._y + (_margin * 2), _radius);
  
    _child.drawAt(left + _margin, top + _margin, canvas);
  }

}