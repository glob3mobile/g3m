package org.glob3.mobile.generated; 
//
//  GEOBoxShapeStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//

//
//  GEOBoxShapeStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//



//class Color;

public class GEOBoxShapeStyle extends GEOShapeStyle
{
  private final Vector3D _extent ;
  private final float _borderWidth;
  private Color _surfaceColor;
  private Color _borderColor;

  public GEOBoxShapeStyle(Vector3D extent, float borderWidth, Color surfaceColor, Color borderColor)
  {
     _extent = new Vector3D(extent);
     _borderWidth = borderWidth;
     _surfaceColor = surfaceColor;
     _borderColor = borderColor;

  }

  public final Vector3D getExtent()
  {
    return _extent;
  }

  public final float getBorderWidth()
  {
    return _borderWidth;
  }

  public final Color getSurfaceColor()
  {
    return _surfaceColor;
  }

  public final Color getBorderColor()
  {
    return _borderColor;
  }
}