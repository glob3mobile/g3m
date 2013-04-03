package org.glob3.mobile.generated; 
//
//  GEOGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEOGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//



//class GEOSymbol;
//class GEOFeature;

public abstract class GEOGeometry extends GEOObject
{
  private GEOFeature _feature;

  protected abstract java.util.ArrayList<GEOSymbol> createSymbols(G3MRenderContext rc, GEOSymbolizationContext sc);

  public GEOGeometry()
  {
     _feature = null;

  }

  public void dispose()
  {
  
  }

  public final void setFeature(GEOFeature feature)
  {
    if (_feature != feature)
    {
      if (_feature != null)
         _feature.dispose();
      _feature = feature;
    }
  }

  public final GEOFeature getFeature()
  {
    return _feature;
  }

  public final void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    java.util.ArrayList<GEOSymbol> symbols = createSymbols(rc, sc);
    if (symbols == null)
    {
      return;
    }
  
    final int symbolsSize = symbols.size();
    for (int i = 0; i < symbolsSize; i++)
    {
      final GEOSymbol symbol = symbols.get(i);
  
      symbol.symbolize(rc, sc);
  
      if (symbol != null)
         symbol.dispose();
    }
  
    symbols = null;
  }

}