package org.glob3.mobile.generated; 
//
//  GEORasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

//
//  GEORasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//




public abstract class GEORasterSymbol extends GEOSymbol
{
  protected final Sector _sector;

  protected GEORasterSymbol(Sector sector)
  {
     _sector = sector;
  }

  public void dispose()
  {
    if (_sector != null)
       _sector.dispose();
  }

  public final void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    if (_sector != null)
    {
      sc.getGEOTileRasterizer().addSymbol(createSymbol());
    }
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public abstract GEORasterSymbol createSymbol();

}