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

//  const Color _lineColor;
//  const float _lineWidth;
  private final GEOLine2DRasterStyle _style = new GEOLine2DRasterStyle();

//  GEOMultiLineRasterSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
//                           const Sector* sector,
//                           const Color&  lineColor,
//                           const float   lineWidth) :
//  GEORasterSymbol(sector),
//  _coordinatesArray(coordinatesArray),
//  _lineColor(lineColor),
//  _lineWidth(lineWidth)
//  {
//  }

  public GEOMultiLineRasterSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, GEOLine2DRasterStyle style)
  //_lineColor( style.getColor() ),
  //_lineWidth( style.getWidth() )
  {
     super(calculateSectorFromCoordinatesArray(coordinatesArray));
     _coordinatesArray = copyCoordinatesArray(coordinatesArray);
     _style = new GEOLine2DRasterStyle(style);
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

  public final void rasterize(ICanvas canvas, GEORasterProjection projection)
  {
  //  canvas->setLineColor(_lineColor);
  //  canvas->setLineWidth(_lineWidth);
    _style.apply(canvas);
  
    final int coordinatesArrayCount = _coordinatesArray.size();
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = _coordinatesArray.get(i);
      rasterLine(coordinates, canvas, projection);
    }
  }

}