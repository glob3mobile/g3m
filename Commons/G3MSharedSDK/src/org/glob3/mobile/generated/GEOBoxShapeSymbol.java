package org.glob3.mobile.generated; 
//
//  GEOBoxShapeSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//

//
//  GEOBoxShapeSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//



///#include "Color.hpp"
//class Color;
//class GEOBoxShapeStyle;
///#include "GEOBoxShapeStyle.hpp"

public class GEOBoxShapeSymbol extends GEOShapeSymbol
{
  private final Geodetic3D _position ;
  private final Vector3D _extent ;

  private final float _borderWidth;
  private Color _surfaceColor;
  private Color _borderColor;

  protected final Shape createShape(G3MRenderContext rc)
  {
    return new BoxShape(new Geodetic3D(_position), _extent, _borderWidth, _surfaceColor, _borderColor);
  }

  public GEOBoxShapeSymbol(Geodetic3D position, GEOBoxShapeStyle style)
  {
     _position = new Geodetic3D(position);
     _extent = new Vector3D(style.getExtent());
     _borderWidth = style.getBorderWidth();
     _surfaceColor = style.getSurfaceColor();
     _borderColor = style.getBorderColor();
  
  }

}