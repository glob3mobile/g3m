package org.glob3.mobile.generated; 
//
//  GEOSymbolizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

//
//  GEOSymbolizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//


//class GEOSymbol;
//class GEO2DMultiLineStringGeometry;
//class GEO2DLineStringGeometry;
//class GEO2DPointGeometry;
//class GEOObject;


public abstract class GEOSymbolizer
{
  public void dispose()
  {
  }

  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DMultiLineStringGeometry geometry);
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DLineStringGeometry geometry);

  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DPointGeometry geometry);

}