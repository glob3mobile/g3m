package org.glob3.mobile.generated;import java.util.*;

//
//  GEORectangleRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by fpulido on 30/06/14.
//
//

//
//  GEORectangleRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by fpulido on 30/06/14.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DPolygonData;


public class GEORectangleRasterSymbol extends GEORasterSymbol
{
	private final GEO2DPolygonData _polygonData;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final GEO2DLineRasterStyle _lineStyle = new GEO2DLineRasterStyle();
	private final GEO2DSurfaceRasterStyle _surfaceStyle = new GEO2DSurfaceRasterStyle();
	private final Vector2F _rectangleSize = new Vector2F();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final GEO2DLineRasterStyle _lineStyle = new internal();
	public final GEO2DSurfaceRasterStyle _surfaceStyle = new internal();
	public final Vector2F _rectangleSize = new internal();
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRasterize(ICanvas* canvas, const GEORasterProjection* projection) const
	protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
	{
		final boolean rasterSurface = _surfaceStyle.apply(canvas);
		final boolean rasterBoundary = _lineStyle.apply(canvas);
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: rasterRectangle(_polygonData, _rectangleSize, rasterSurface, rasterBoundary, canvas, projection);
		rasterRectangle(_polygonData, new Vector2F(_rectangleSize), rasterSurface, rasterBoundary, canvas, projection);
	}


	public GEORectangleRasterSymbol(GEO2DPolygonData polygonData, Vector2F rectangleSize, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel)
	{
		this(polygonData, rectangleSize, lineStyle, surfaceStyle, minTileLevel, -1);
	}
	public GEORectangleRasterSymbol(GEO2DPolygonData polygonData, Vector2F rectangleSize, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle)
	{
		this(polygonData, rectangleSize, lineStyle, surfaceStyle, -1, -1);
	}
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GEORectangleRasterSymbol(const GEO2DPolygonData* polygonData, const Vector2F rectangleSize, const GEO2DLineRasterStyle& lineStyle, const GEO2DSurfaceRasterStyle& surfaceStyle, const int minTileLevel = -1, const int maxTileLevel = -1) : GEORasterSymbol(minTileLevel, maxTileLevel), _polygonData(polygonData), _rectangleSize(rectangleSize), _lineStyle(lineStyle), _surfaceStyle(surfaceStyle)
	public GEORectangleRasterSymbol(GEO2DPolygonData polygonData, Vector2F rectangleSize, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel, int maxTileLevel)
	{
		super(minTileLevel, maxTileLevel);
		_polygonData = polygonData;
		_rectangleSize = new Vector2F(rectangleSize);
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
