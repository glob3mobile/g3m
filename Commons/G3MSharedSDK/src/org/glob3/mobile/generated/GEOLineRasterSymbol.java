package org.glob3.mobile.generated; 
//
//  GEOLineRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

//
//  GEOLineRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//



//class Geodetic2D;

public class GEOLineRasterSymbol extends GEORasterSymbol
{
  private java.util.ArrayList<Geodetic2D> _coordinates;
  //private java.util.ArrayList<Geodetic2D> _coordinates;
  private final GEO2DLineRasterStyle      _style;

  protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
    if (_style.apply(canvas))
    {
      rasterLine(_coordinates, canvas, projection);
    }
  }

  public GEOLineRasterSymbol(java.util.ArrayList<Geodetic2D> coordinates, GEO2DLineRasterStyle style, int minTileLevel)
  {
     this(coordinates, style, minTileLevel, -1);
  }
  public GEOLineRasterSymbol(java.util.ArrayList<Geodetic2D> coordinates, GEO2DLineRasterStyle style)
  {
     this(coordinates, style, -1, -1);
  }
  public GEOLineRasterSymbol(java.util.ArrayList<Geodetic2D> coordinates, GEO2DLineRasterStyle style, int minTileLevel, int maxTileLevel)
  {
     super(calculateSectorFromCoordinates(coordinates), minTileLevel, maxTileLevel);
     _coordinates = copyCoordinates(coordinates);
     _style = style;
  }

  public void dispose()
  {
    if (_coordinates != null)
    {
      final int size = _coordinates.size();
  
      for (int i = 0; i < size; i++)
      {
        final Geodetic2D coordinate = _coordinates.get(i);
        if (coordinate != null)
           coordinate.dispose();
      }
  
      _coordinates = null;
    }
  
    super.dispose();
  
  }

}