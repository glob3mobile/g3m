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

public class GEOShapeSymbol extends GEOSymbol
{
  private Shape _shape;

  public GEOShapeSymbol(Shape shape)
  {
     _shape = shape;

  }

  public void dispose()
  {
    if (_shape != null)
       _shape.dispose();
  }

  public final boolean symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    if (_shape != null)
    {
      ShapesRenderer shapeRenderer = sc.getShapesRenderer();
      if (shapeRenderer == null)
      {
        ILogger.instance().logError("Can't simbolize with Shape, ShapesRenderer was not set");
        if (_shape != null)
           _shape.dispose();
      }
      else
      {
        shapeRenderer.addShape(_shape);
      }
      _shape = null;
    }
    return true;
  }

  public final boolean deleteAfterSymbolize()
  {
    return true;
  }

}