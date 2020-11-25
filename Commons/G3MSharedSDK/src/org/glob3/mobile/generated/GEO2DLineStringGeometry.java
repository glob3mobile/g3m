package org.glob3.mobile.generated;
//
//  GEO2DLineStringGeometry.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEO2DLineStringGeometry.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//


//class Geodetic2D;

public class GEO2DLineStringGeometry extends GEO2DGeometry
{
  private final GEO2DCoordinatesData _coordinatesData;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }


  public GEO2DLineStringGeometry(java.util.ArrayList<Geodetic2D> coordinates)
  {
    _coordinatesData = (coordinates == null) ? null : new GEO2DCoordinatesData(coordinates);
  }

  public void dispose()
  {
    if (_coordinatesData != null)
    {
      _coordinatesData._release();
    }
  
    super.dispose();
  }

  public final GEO2DCoordinatesData getCoordinates()
  {
    return _coordinatesData;
  }

}