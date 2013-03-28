package org.glob3.mobile.generated; 
//
//  GEOCircleShapeStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEOCircleShapeStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//




public class GEOCircleShapeStyle extends GEOShapeStyle
{
  private final float _radius;
  private final Color _color ;
  private final int _steps;

  public GEOCircleShapeStyle(float radius, Color color, int steps)
  {
     _radius = radius;
     _color = new Color(color);
     _steps = steps;

  }

  public final float getRadius()
  {
    return _radius;
  }

  public final Color getColor()
  {
    return _color;
  }

  public final int getSteps()
  {
    return _steps;
  }

}