package org.glob3.mobile.generated; 
//
//  GEO2DMultiLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEO2DMultiLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//


//class Geodetic2D;
//class GEO2DCoordinatesArrayData;

public class GEO2DMultiLineStringGeometry extends GEOGeometry2D
{
  private final GEO2DCoordinatesArrayData _coordinatesArrayData;

  private GEO2DMultiLineStringGeometry(GEO2DCoordinatesArrayData coordinatesArrayData)
  {
     _coordinatesArrayData = coordinatesArrayData;
    if (_coordinatesArrayData != null)
    {
      _coordinatesArrayData._retain();
    }
  }

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

  public final long getCoordinatesCount()
  {
    return (_coordinatesArrayData == null) ? 0 : _coordinatesArrayData.getCoordinatesCount();
  }

  public final GEO2DMultiLineStringGeometry deepCopy()
  {
    return new GEO2DMultiLineStringGeometry(_coordinatesArrayData);
  }

}