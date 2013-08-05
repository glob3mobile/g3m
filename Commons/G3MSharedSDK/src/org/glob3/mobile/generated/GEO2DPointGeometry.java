package org.glob3.mobile.generated; 
//
//  GEO2DPointGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEO2DPointGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//




public class GEO2DPointGeometry extends GEOPointGeometry
{
  private final Geodetic2D _position ;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    return sc.getSymbolizer().createSymbols(this);
  }


  public GEO2DPointGeometry(Geodetic2D position)
  {
     _position = new Geodetic2D(position);

  }

  public final Geodetic2D getPosition()
  {
    return _position;
  }

}