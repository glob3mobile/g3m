package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;

public class GEOLineRasterSymbol extends GEORasterSymbol
{
  private final GEO2DCoordinatesData _coordinates;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final GEO2DLineRasterStyle _style = new GEO2DLineRasterStyle();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final GEO2DLineRasterStyle _style = new internal();
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRasterize(ICanvas* canvas, const GEORasterProjection* projection) const
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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GEOLineRasterSymbol(const GEO2DCoordinatesData* coordinates, const GEO2DLineRasterStyle& style, const int minTileLevel = -1, const int maxTileLevel = -1): GEORasterSymbol(minTileLevel, maxTileLevel), _coordinates(coordinates), _style(style)
  public GEOLineRasterSymbol(GEO2DCoordinatesData coordinates, GEO2DLineRasterStyle style, int minTileLevel, int maxTileLevel)
  {
	  super(minTileLevel, maxTileLevel);
	  _coordinates = coordinates;
	  _style = new GEO2DLineRasterStyle(style);
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector* getSector() const
  public final Sector getSector()
  {
	return (_coordinates == null) ? null : _coordinates.getSector();
  }

}
