package org.glob3.mobile.generated;
//
//  GEO2DMultiPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

//
//  GEO2DMultiPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//



//class GEO2DPolygonData;


public class GEO2DMultiPolygonGeometry extends GEO2DGeometry
{
  private java.util.ArrayList<GEO2DPolygonData> _polygonsData;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected static java.util.ArrayList<GEO2DPolygonData> copy(java.util.ArrayList<GEO2DPolygonData> polygonsData)
  {
    if (polygonsData == null)
    {
      return null;
    }
    java.util.ArrayList<GEO2DPolygonData> result = new java.util.ArrayList<GEO2DPolygonData>();
    final int size = polygonsData.size();
    for (int i = 0; i < size; i++)
    {
      GEO2DPolygonData each = polygonsData.get(i);
      if (each != null)
      {
        each._retain();
      }
      result.add(each);
    }
    return result;
  }

  protected final Sector calculateSector()
  {
    if (_polygonsData == null)
    {
      return null;
    }
  
    final int polygonsDataSize = _polygonsData.size();
    if (polygonsDataSize == 0)
    {
      return null;
    }
  
    final Sector sector0 = _polygonsData.get(0).getSector();
  
    double minLatRad = sector0._lower._latitude._radians;
    double maxLatRad = sector0._upper._latitude._radians;
  
    double minLonRad = sector0._lower._longitude._radians;
    double maxLonRad = sector0._upper._longitude._radians;
  
    for (int i = 1; i < polygonsDataSize; i++)
    {
      final Sector sector = _polygonsData.get(i).getSector();
  
      final double lowerLatRad = sector._lower._latitude._radians;
      if (lowerLatRad < minLatRad)
      {
        minLatRad = lowerLatRad;
      }
      final double upperLatRad = sector._upper._latitude._radians;
      if (upperLatRad > maxLatRad)
      {
        maxLatRad = upperLatRad;
      }
  
      final double lowerLonRad = sector._lower._longitude._radians;
      if (lowerLonRad < minLonRad)
      {
        minLonRad = lowerLonRad;
      }
      final double upperLonRad = sector._upper._longitude._radians;
      if (upperLonRad > maxLonRad)
      {
        maxLonRad = upperLonRad;
      }
    }
  
    final double lowerLatRadians = (minLatRad == maxLatRad) ? minLatRad - 0.0001 : minLatRad;
    final double upperLatRadians = (minLatRad == maxLatRad) ? maxLatRad + 0.0001 : maxLatRad;
  
    final double lowerLonRadians = (minLonRad == maxLonRad) ? minLonRad - 0.0001 : minLonRad;
    final double upperLonRadians = (minLonRad == maxLonRad) ? maxLonRad + 0.0001 : maxLonRad;
  
    return Sector.newFromRadians(lowerLatRadians, lowerLonRadians, upperLatRadians, upperLonRadians);
  }


  public GEO2DMultiPolygonGeometry(java.util.ArrayList<GEO2DPolygonData> polygonsData)
  {
     _polygonsData = polygonsData;
  }

  public void dispose()
  {
    if (_polygonsData != null)
    {
      final int polygonsDataSize = _polygonsData.size();
      for (int i = 0; i < polygonsDataSize; i++)
      {
        GEO2DPolygonData polygonData = _polygonsData.get(i);
        if (polygonData != null)
        {
          polygonData._release();
        }
      }
      _polygonsData = null;
    }
  
    super.dispose();
  }

  public final java.util.ArrayList<GEO2DPolygonData> getPolygonsData()
  {
    return _polygonsData;
  }

  public final long getCoordinatesCount()
  {
    return (_polygonsData == null) ? 0 : _polygonsData.size();
  }

  public final GEO2DMultiPolygonGeometry deepCopy()
  {
    return new GEO2DMultiPolygonGeometry(copy(_polygonsData));
  }

  public final boolean contain(Geodetic2D point)
  {
    if (_polygonsData == null)
    {
      return false;
    }
    final int polygonsDataSize = _polygonsData.size();
    for (int i = 0; i < polygonsDataSize; i++)
    {
      GEO2DPolygonData polygonData = _polygonsData.get(i);
      if (polygonData.contains(point))
      {
        return true;
      }
    }
  
    return false;
  }
}
