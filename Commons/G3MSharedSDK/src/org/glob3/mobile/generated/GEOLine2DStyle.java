package org.glob3.mobile.generated; 
//
//  GEOLine2DStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//

//
//  GEOLine2DStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//





public class GEOLine2DStyle extends GEOStyle
{
  private final Color _color ;
  private final float _width;

  public GEOLine2DStyle(Color color, float width)
  {
     _color = new Color(color);
     _width = width;

  }

  public void dispose()
  {
  super.dispose();

  }

  public final Color getColor()
  {
    return _color;
  }

  public final float getWidth()
  {
    return _width;
  }

}