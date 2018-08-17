package org.glob3.mobile.generated;import java.util.*;

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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DCoordinatesArrayData;

public class GEOMultiLineRasterSymbol extends GEORasterSymbol
{
  private final GEO2DCoordinatesArrayData _coordinatesArrayData;

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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GEOMultiLineRasterSymbol(const GEO2DCoordinatesArrayData* coordinatesArrayData, const GEO2DLineRasterStyle& style, const int minTileLevel = -1, const int maxTileLevel = -1) : GEORasterSymbol(minTileLevel, maxTileLevel), _coordinatesArrayData(coordinatesArrayData), _style(style)
  public GEOMultiLineRasterSymbol(GEO2DCoordinatesArrayData coordinatesArrayData, GEO2DLineRasterStyle style, int minTileLevel, int maxTileLevel)
  {
	  super(minTileLevel, maxTileLevel);
	  _coordinatesArrayData = coordinatesArrayData;
	  _style = new GEO2DLineRasterStyle(style);
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector* getSector() const
  public final Sector getSector()
  {
	return (_coordinatesArrayData == null) ? null : _coordinatesArrayData.getSector();
  }

}
