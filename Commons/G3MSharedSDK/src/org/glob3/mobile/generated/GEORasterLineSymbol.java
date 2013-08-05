package org.glob3.mobile.generated; 
//
//  GEORasterLineSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

//
//  GEORasterLineSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//



//class Geodetic2D;

public class GEORasterLineSymbol extends GEORasterSymbol
{
  private java.util.ArrayList<Geodetic2D> _coordinates;
  private final GEO2DLineRasterStyle      _style;


  public GEORasterLineSymbol(java.util.ArrayList<Geodetic2D> coordinates, GEO2DLineRasterStyle style)
  {
     super(calculateSectorFromCoordinates(coordinates));
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
  }

  public final void rasterize(ICanvas canvas, GEORasterProjection projection)
  {
    if (_style.apply(canvas))
    {
      rasterLine(_coordinates, canvas, projection);
    }
  }


}