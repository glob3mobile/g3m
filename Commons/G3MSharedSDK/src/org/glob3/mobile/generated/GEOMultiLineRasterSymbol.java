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


//class GEO2DCoordinatesArrayData;

public class GEOMultiLineRasterSymbol extends GEORasterSymbol
{
  private final GEO2DCoordinatesArrayData _coordinatesArrayData;

  private final GEO2DLineRasterStyle _style;

  protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
    if (_coordinatesArrayData != null)
    {
      if (_style.apply(canvas))
      {
        final int coordinatesArrayCount = _coordinatesArrayData.size();
        for (int i = 0; i < coordinatesArrayCount; i++)
        {
          final GEO2DCoordinatesData coordinates = _coordinatesArrayData.get(i);
          if (coordinates != null)
          {
            rasterLine(coordinates, canvas, projection);
          }
        }
      }
    }
  }

  public GEOMultiLineRasterSymbol(GEO2DCoordinatesArrayData coordinatesArrayData, GEO2DLineRasterStyle style, int minTileLevel)
  {
     this(coordinatesArrayData, style, minTileLevel, -1);
  }
  public GEOMultiLineRasterSymbol(GEO2DCoordinatesArrayData coordinatesArrayData, GEO2DLineRasterStyle style)
  {
     this(coordinatesArrayData, style, -1, -1);
  }
  public GEOMultiLineRasterSymbol(GEO2DCoordinatesArrayData coordinatesArrayData, GEO2DLineRasterStyle style, int minTileLevel, int maxTileLevel)
  {
     super(minTileLevel, maxTileLevel);
     _coordinatesArrayData = coordinatesArrayData;
     _style = style;
    if (_coordinatesArrayData != null)
    {
      _coordinatesArrayData._retain();
    }
  }

  public void dispose()
  {
    if (_coordinatesArrayData != null)
    {
      _coordinatesArrayData._release();
    }
  
    super.dispose();
  }

  public final Sector getSector()
  {
    return (_coordinatesArrayData == null) ? null : _coordinatesArrayData.getSector();
  }

}