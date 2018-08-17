package org.glob3.mobile.generated;import java.util.*;

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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEORasterSymbol;

public abstract class GEOGeometry2D extends GEOGeometry
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const = 0;
  protected abstract java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer);


  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rasterize(const GEORasterSymbolizer* symbolizer, ICanvas* canvas, const GEORasterProjection* projection, int tileLevel) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual GEOGeometry2D* deepCopy() const = 0;
  public abstract GEOGeometry2D deepCopy();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean contain(const Geodetic2D& point) const
  public boolean contain(Geodetic2D point)
  {
	return false;
  }


}
