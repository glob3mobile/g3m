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

public abstract class GEOSymbolizer
{
///#ifdef C_CODE
//  virtual ~GEOSymbolizer() { }
///#endif
///#ifdef JAVA_CODE
//  public void dispose();
///#endif
  public void dispose()
  {
  }

  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DMultiLineStringGeometry geometry);
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DLineStringGeometry geometry);

}