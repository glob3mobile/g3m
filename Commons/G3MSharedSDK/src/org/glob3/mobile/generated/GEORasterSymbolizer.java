package org.glob3.mobile.generated; 
//
//  GEORasterSymbolizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/1/14.
//
//

//
//  GEORasterSymbolizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/1/14.
//
//



//class GEORasterSymbol;
//class GEO2DPointGeometry;
//class GEO2DLineStringGeometry;
//class GEO2DMultiLineStringGeometry;
//class GEO2DPolygonGeometry;
//class GEO2DMultiPolygonGeometry;

public abstract class GEORasterSymbolizer
{
  public void dispose()
  {
  }

  public abstract GEORasterSymbolizer copy();

  public abstract java.util.ArrayList<GEORasterSymbol> createSymbols(GEO2DPointGeometry geometry);

  public abstract java.util.ArrayList<GEORasterSymbol> createSymbols(GEO2DLineStringGeometry geometry);
  public abstract java.util.ArrayList<GEORasterSymbol> createSymbols(GEO2DMultiLineStringGeometry geometry);

  public abstract java.util.ArrayList<GEORasterSymbol> createSymbols(GEO2DPolygonGeometry geometry);
  public abstract java.util.ArrayList<GEORasterSymbol> createSymbols(GEO2DMultiPolygonGeometry geometry);

}