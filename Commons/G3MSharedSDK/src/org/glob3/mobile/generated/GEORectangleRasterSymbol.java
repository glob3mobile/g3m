package org.glob3.mobile.generated; 
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




//class GEO2DPolygonData;


public class GEORectangleRasterSymbol extends GEORasterSymbol
{
    private final GEO2DPolygonData _polygonData;
    private final GEO2DLineRasterStyle    _lineStyle;
    private final GEO2DSurfaceRasterStyle _surfaceStyle;
    private final Vector2F _rectangleSize;

    protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
    {
        final boolean rasterSurface = _surfaceStyle.apply(canvas);
        final boolean rasterBoundary = _lineStyle.apply(canvas);
    
        rasterRectangle(_polygonData, _rectangleSize, rasterSurface, rasterBoundary, canvas, projection);
    }


    public GEORectangleRasterSymbol(GEO2DPolygonData polygonData, Vector2F rectangleSize, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel)
    {
       this(polygonData, rectangleSize, lineStyle, surfaceStyle, minTileLevel, -1);
    }
    public GEORectangleRasterSymbol(GEO2DPolygonData polygonData, Vector2F rectangleSize, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle)
    {
       this(polygonData, rectangleSize, lineStyle, surfaceStyle, -1, -1);
    }
    public GEORectangleRasterSymbol(GEO2DPolygonData polygonData, Vector2F rectangleSize, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel, int maxTileLevel)
    {
       super(minTileLevel, maxTileLevel);
       _polygonData = polygonData;
       _rectangleSize = rectangleSize;
       _lineStyle = lineStyle;
       _surfaceStyle = surfaceStyle;
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
        super.dispose();
    }

    public final Sector getSector()
    {
        return (_polygonData == null) ? null : _polygonData.getSector();
    }

}