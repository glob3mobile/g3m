package org.glob3.mobile.generated; 
//
//  CircleImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/2/15.
//
//

//
//  CircleImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/2/15.
//
//



//class Color;
//class G3MContext;
//class ICanvas;


public class CircleImageBuilder extends CanvasImageBuilder
{

  private final Color _color ;
  private final int _radius;

  protected final void buildOnCanvas(G3MContext context, ICanvas canvas)
  {
    canvas.setFillColor(_color);
    canvas.fillEllipse(1, 1, _radius *2, _radius *2);
  }
  public CircleImageBuilder(Color color, int radius)
  {
     super(radius *2 + 2, radius *2 + 2);
     _color = new Color(color);
     _radius = radius;
  
  }

  public final boolean isMutable()
  {
    return false;
  }
}