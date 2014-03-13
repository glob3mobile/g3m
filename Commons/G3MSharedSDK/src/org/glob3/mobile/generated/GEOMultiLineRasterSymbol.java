package org.glob3.mobile.generated; 
//
//  GEOMultiLineRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

//
//  GEOMultiLineRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//



public class GEOMultiLineRasterSymbol extends GEORasterSymbol
{
  private java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _coordinatesArray;
  private final GEO2DLineRasterStyle                           _style;

  private java.util.ArrayList<Geodetic2D> getCoordinateArray(int i)
  {
    return _coordinatesArray.get(i);
  }
  public GEOMultiLineRasterSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, GEO2DLineRasterStyle style, int minTileLevel)
  {
     this(coordinatesArray, style, minTileLevel, -1);
  }
  public GEOMultiLineRasterSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, GEO2DLineRasterStyle style)
  {
     this(coordinatesArray, style, -1, -1);
  }
  public GEOMultiLineRasterSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, GEO2DLineRasterStyle style, int minTileLevel, int maxTileLevel)
  {
     super(calculateSectorFromCoordinatesArray(coordinatesArray), minTileLevel, maxTileLevel);
     _coordinatesArray = copyCoordinatesArray(coordinatesArray);
     _style = style;
  }

  public void dispose()
  {
    if (_coordinatesArray != null)
    {
      final int coordinatesArrayCount = _coordinatesArray.size();
      for (int i = 0; i < coordinatesArrayCount; i++)
      {
        java.util.ArrayList<Geodetic2D> coordinates = this.getCoordinateArray(i);
        final int coordinatesCount = coordinates.size();
        for (int j = 0; j < coordinatesCount; j++)
        {
          final Geodetic2D coordinate = coordinates.get(j);
          if (coordinate != null)
             coordinate.dispose();
        }
        coordinates = null;
      }
      _coordinatesArray = null;
    }
  
    super.dispose();
  
  }

  public final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
    if (_style.apply(canvas))
    {
      final int coordinatesArrayCount = _coordinatesArray.size();
      for (int i = 0; i < coordinatesArrayCount; i++)
      {
        java.util.ArrayList<Geodetic2D> coordinates = this.getCoordinateArray(i);
        rasterLine(coordinates, canvas, projection);
      }
    }
  }



}