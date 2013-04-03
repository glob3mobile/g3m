package org.glob3.mobile.generated; 
//
//  GEOSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

//
//  GEOSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//



//class G3MRenderContext;
//class GEOSymbolizationContext;

public abstract class GEOSymbol
{

  public void dispose()
  {
  }

  public abstract void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc);

}