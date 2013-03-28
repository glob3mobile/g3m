package org.glob3.mobile.generated; 
//
//  GEOCircleShapeSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEOCircleShapeSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//



//class Geodetic3D;
//class Color;
//class GEOCircleShapeStyle;

public class GEOCircleShapeSymbol extends GEOShapeSymbol
{
  private final Geodetic3D _position ;
  private final float _radius;
  private Color _color;
  private final int _steps;

  protected final Shape createShape(G3MRenderContext rc)
  {
    return new CircleShape(new Geodetic3D(_position), _radius, _color, _steps);
  }


  public GEOCircleShapeSymbol(Geodetic3D position, GEOCircleShapeStyle style)
  {
     _position = new Geodetic3D(position);
     _radius = style.getRadius();
     _color = new Color(style.getColor());
     _steps = style.getSteps();
  
  }

}