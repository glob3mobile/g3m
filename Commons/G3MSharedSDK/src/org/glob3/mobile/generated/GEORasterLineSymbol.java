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
//class GEOLine2DStyle;


public class GEORasterLineSymbol extends GEORasterSymbol
{
  private java.util.ArrayList<Geodetic2D> _coordinates;

  private final Color _lineColor ;
  private final float _lineWidth;

  private GEORasterLineSymbol(java.util.ArrayList<Geodetic2D> coordinates, Sector sector, Color lineColor, float lineWidth)
  {
     super(sector);
     _coordinates = coordinates;
     _lineColor = new Color(lineColor);
     _lineWidth = lineWidth;
  }

  public GEORasterLineSymbol(java.util.ArrayList<Geodetic2D> coordinates, GEOLine2DStyle style)

  {
     super(calculateSectorFromCoordinates(coordinates));
     _coordinates = copyCoordinates(coordinates);
     _lineColor = new Color(style.getColor());
     _lineWidth = style.getWidth();
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

  public final GEORasterLineSymbol createSymbol()
  {
    GEORasterLineSymbol result = new GEORasterLineSymbol(_coordinates, new Sector(_sector), _lineColor, _lineWidth);
    _coordinates = null;
    return result;
  }


  public final void rasterize(ICanvas canvas, GEORasterProjection projection)
  {
  
  //  int __REMOVE_DEBUG_CODE;
  //  canvas->setStrokeColor(Color::magenta());
    canvas.setStrokeColor(_lineColor);
    canvas.setStrokeWidth(_lineWidth);
  
    rasterLine(_coordinates, canvas, projection);
  }


}