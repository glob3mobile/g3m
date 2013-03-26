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


//class Mesh;
//class G3MRenderContext;
//class GEOSymbol;
//class GEO2DMultiLineStringGeometry;
//class GEO2DLineStringGeometry;

public interface GEOSymbolizer
{
  GEOSymbolizer()
  {

  }

  public void dispose();

  java.util.ArrayList<GEOSymbol> createSymbols(GEO2DMultiLineStringGeometry geometry);
  java.util.ArrayList<GEOSymbol> createSymbols(GEO2DLineStringGeometry geometry);

}