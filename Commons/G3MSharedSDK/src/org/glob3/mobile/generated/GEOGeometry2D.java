package org.glob3.mobile.generated; 
//
//  GEOGeometry2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

//
//  GEOGeometry2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//


//class Geodetic2D;


//class GEORasterSymbol;

public abstract class GEOGeometry2D extends GEOGeometry
{
  protected abstract java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer);


  public void dispose()
  {
  super.dispose();
  }

  public final void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
  
    java.util.ArrayList<GEORasterSymbol> symbols = createRasterSymbols(symbolizer);
    if (symbols != null)
    {
      final int symbolsSize = symbols.size();
      for (int i = 0; i < symbolsSize; i++)
      {
        GEORasterSymbol symbol = symbols.get(i);
        if (symbol != null)
        {
          symbol.rasterize(canvas, projection, tileLevel);
          if (symbol != null)
             symbol.dispose();
        }
      }
  
      symbols = null;
    }
  }

  public abstract GEOGeometry2D deepCopy();

  public boolean contain(Geodetic2D point)
  {
    return false;
  }


}