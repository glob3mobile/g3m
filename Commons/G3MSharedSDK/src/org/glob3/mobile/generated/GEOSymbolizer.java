package org.glob3.mobile.generated;
//
//  GEOSymbolizer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//



//class GEOSymbol;
//class GEO2DPointGeometry;
//class GEO3DPointGeometry;
//class GEO2DLineStringGeometry;
//class GEO2DMultiLineStringGeometry;
//class GEO2DPolygonGeometry;
//class GEO3DPolygonGeometry;
//class GEO2DMultiPolygonGeometry;


public abstract class GEOSymbolizer
{
  public void dispose()
  {
  }

  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DPointGeometry geometry);
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO3DPointGeometry geometry);

  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DLineStringGeometry geometry);
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DMultiLineStringGeometry geometry);

  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DPolygonGeometry geometry);
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO3DPolygonGeometry geometry);
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DMultiPolygonGeometry geometry);

}