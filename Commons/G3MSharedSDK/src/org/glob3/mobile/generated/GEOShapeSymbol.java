package org.glob3.mobile.generated; 
//
//  GEOShapeSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEOShapeSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//


//class Shape;

public abstract class GEOShapeSymbol extends GEOSymbol
{
  protected abstract Shape createShape(G3MRenderContext rc);

  public final void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    ShapesRenderer shapeRenderer = sc.getShapesRenderer();
    if (shapeRenderer == null)
    {
      ILogger.instance().logError("Can't simbolize with Shape, ShapesRenderer was not set");
    }
    else
    {
      Shape shape = createShape(rc);
      if (shape != null)
      {
        shapeRenderer.addShape(shape);
      }
    }
  }

}