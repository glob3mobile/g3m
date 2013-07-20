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


//class GEOLine2DStyle;


public class GEOMultiLineRasterSymbol extends GEORasterSymbol
{
  private java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _coordinatesArray;

  private final Color _lineColor ;
  private final float _lineWidth;

  private GEOMultiLineRasterSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, Sector sector, Color lineColor, float lineWidth)
  {
     super(sector);
     _coordinatesArray = coordinatesArray;
     _lineColor = new Color(lineColor);
     _lineWidth = lineWidth;
  }

  public GEOMultiLineRasterSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, GEOLine2DStyle style)
  {
     super(calculateSectorFromCoordinatesArray(coordinatesArray));
     _coordinatesArray = copyCoordinatesArray(coordinatesArray);
     _lineColor = new Color(style.getColor());
     _lineWidth = style.getWidth();
  }

  public void dispose()
  {
    if (_coordinatesArray != null)
    {
      final int coordinatesArrayCount = _coordinatesArray.size();
      for (int i = 0; i < coordinatesArrayCount; i++)
      {
        java.util.ArrayList<Geodetic2D> coordinates = _coordinatesArray.get(i);
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
  }

  public final GEOMultiLineRasterSymbol createSymbol()
  {
    GEOMultiLineRasterSymbol result = new GEOMultiLineRasterSymbol(_coordinatesArray, new Sector(_sector), _lineColor, _lineWidth);
    _coordinatesArray = null;
    return result;
  }


  public final void rasterize(ICanvas canvas, GEORasterProjection projection)
  {
  //  int __REMOVE_DEBUG_CODE;
  //  canvas->setStrokeColor(Color::green());
    canvas.setStrokeColor(_lineColor);
    canvas.setStrokeWidth(_lineWidth);
  
    final int coordinatesArrayCount = _coordinatesArray.size();
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = _coordinatesArray.get(i);
      rasterLine(coordinates, canvas, projection);
    }
  }

}