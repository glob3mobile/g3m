package org.glob3.mobile.generated;
//
//  GEO2DSurfaceRasterStyle.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEO2DSurfaceRasterStyle.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//


//class ICanvas;

public class GEO2DSurfaceRasterStyle
{
  private final Color _color ;

  public GEO2DSurfaceRasterStyle(Color color)
  {
     _color = color;
  }

  public GEO2DSurfaceRasterStyle(GEO2DSurfaceRasterStyle that)
  {
     _color = that._color;
  }

  public final boolean apply(ICanvas canvas)
  {
    final boolean applied = !_color.isFullTransparent();
    if (applied)
    {
      canvas.setFillColor(_color);
    }
    return applied;
  }


}