package org.glob3.mobile.generated;import java.util.*;

//
//  GEOPolygonRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEOPolygonRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DPolygonData;


public class GEOPolygonRasterSymbol extends GEORasterSymbol
{
  private final GEO2DPolygonData _polygonData;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final GEO2DLineRasterStyle _lineStyle = new GEO2DLineRasterStyle();
  private final GEO2DSurfaceRasterStyle _surfaceStyle = new GEO2DSurfaceRasterStyle();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final GEO2DLineRasterStyle _lineStyle = new internal();
  public final GEO2DSurfaceRasterStyle _surfaceStyle = new internal();
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRasterize(ICanvas* canvas, const GEORasterProjection* projection) const
  protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
	final boolean rasterSurface = _surfaceStyle.apply(canvas);
	final boolean rasterBoundary = _lineStyle.apply(canvas);
  
	rasterPolygon(_polygonData, rasterSurface, rasterBoundary, canvas, projection);
  }


  public GEOPolygonRasterSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel)
  {
	  this(polygonData, lineStyle, surfaceStyle, minTileLevel, -1);
  }
  public GEOPolygonRasterSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle)
  {
	  this(polygonData, lineStyle, surfaceStyle, -1, -1);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GEOPolygonRasterSymbol(const GEO2DPolygonData* polygonData, const GEO2DLineRasterStyle& lineStyle, const GEO2DSurfaceRasterStyle& surfaceStyle, const int minTileLevel = -1, const int maxTileLevel = -1) : GEORasterSymbol(minTileLevel, maxTileLevel), _polygonData(polygonData), _lineStyle(lineStyle), _surfaceStyle(surfaceStyle)
  public GEOPolygonRasterSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel, int maxTileLevel)
  {
	  super(minTileLevel, maxTileLevel);
	  _polygonData = polygonData;
	  _lineStyle = new GEO2DLineRasterStyle(lineStyle);
	  _surfaceStyle = new GEO2DSurfaceRasterStyle(surfaceStyle);
	if (_polygonData != null)
	{
	  _polygonData._retain();
	}
  }

  public void dispose()
  {
	if (_polygonData != null)
	{
	  _polygonData._release();
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
	return (_polygonData == null) ? null : _polygonData.getSector();
  }

}
