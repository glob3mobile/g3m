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
  private final GEO2DCoordinatesData _coordinates;
  private final GEO2DLineRasterStyle _style;

  protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
    if (_style.apply(canvas))
    {
      rasterLine(_coordinates, canvas, projection);
    }
  }

  public GEOLineRasterSymbol(GEO2DCoordinatesData coordinates, GEO2DLineRasterStyle style, int minTileLevel)
  {
     this(coordinates, style, minTileLevel, -1);
  }
  public GEOLineRasterSymbol(GEO2DCoordinatesData coordinates, GEO2DLineRasterStyle style)
  {
     this(coordinates, style, -1, -1);
  }
  public GEOLineRasterSymbol(GEO2DCoordinatesData coordinates, GEO2DLineRasterStyle style, int minTileLevel, int maxTileLevel)
  {
     super(minTileLevel, maxTileLevel);
     _coordinates = coordinates;
     _style = style;
    if (_coordinates != null)
    {
      _coordinates._retain();
    }
  }

  public void dispose()
  {
    if (_coordinates != null)
    {
      _coordinates._release();
    }
    super.dispose();
  }

  public final Sector getSector()
  {
    return (_coordinates == null) ? null : _coordinates.getSector();
  }

}