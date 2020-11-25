package org.glob3.mobile.generated;
//
//  GEO3DGeometry.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//

//
//  GEO3DGeometry.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//


//class Geodetic3D;


//class GEORasterSymbol;

public abstract class GEO3DGeometry extends GEOGeometry
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

  public boolean contain(Geodetic3D point)
  {
    return false;
  }

}