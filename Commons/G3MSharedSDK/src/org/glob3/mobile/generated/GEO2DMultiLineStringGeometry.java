package org.glob3.mobile.generated;
//
//  GEO2DMultiLineStringGeometry.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEO2DMultiLineStringGeometry.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//


//class Geodetic2D;
//class GEO2DCoordinatesArrayData;

public class GEO2DMultiLineStringGeometry extends GEO2DGeometry
{
  private final GEO2DCoordinatesArrayData _coordinatesArrayData;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }


  public GEO2DMultiLineStringGeometry(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
    _coordinatesArrayData = (coordinatesArray == null) ? null : new GEO2DCoordinatesArrayData(coordinatesArray);
  }

  public void dispose()
  {
    if (_coordinatesArrayData != null)
    {
      _coordinatesArrayData._release();
    }
  
    super.dispose();
  }

  public final GEO2DCoordinatesArrayData getCoordinatesArray()
  {
    return _coordinatesArrayData;
  }

}
