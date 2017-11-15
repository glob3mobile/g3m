package org.glob3.mobile.generated; 
//
//  GEO2DLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEO2DLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//


//class Geodetic2D;

public class GEO2DLineStringGeometry extends GEOGeometry2D
{
  private final GEO2DCoordinatesData _coordinatesData;

  private GEO2DLineStringGeometry(GEO2DCoordinatesData coordinatesData)
  {
     _coordinatesData = coordinatesData;
    if (_coordinatesData != null)
    {
      _coordinatesData._retain();
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

  public final long getCoordinatesCount()
  {
    return _coordinatesData.size();
  }

  public final GEO2DLineStringGeometry deepCopy()
  {
    return new GEO2DLineStringGeometry(_coordinatesData);
  }

}