package org.glob3.mobile.generated; 
//
//  GEOObject.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//


//class G3MRenderContext;
//class GEOSymbolizationContext;

public abstract class GEOObject
{
  public void dispose()
  {

  }

  public abstract void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc);
}