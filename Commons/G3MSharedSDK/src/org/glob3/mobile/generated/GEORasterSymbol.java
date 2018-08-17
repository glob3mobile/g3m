package org.glob3.mobile.generated;import java.util.*;

//
//  GEORasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

//
//  GEORasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEORasterProjection;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICanvas;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DPolygonData;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DCoordinatesData;

//C++ TO JAVA CONVERTER TODO TASK: Multiple inheritance is not available in Java:
public abstract class GEORasterSymbol extends GEOSymbol, QuadTree_Content
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GEORasterSymbol(GEORasterSymbol that);

  private final int _minTileLevel;
  private final int _maxTileLevel;

  protected GEORasterSymbol(int minTileLevel, int maxTileLevel)
  {
	  _minTileLevel = minTileLevel;
	  _maxTileLevel = maxTileLevel;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rasterLine(const GEO2DCoordinatesData* coordinatesData, ICanvas* canvas, const GEORasterProjection* projection) const
  protected final void rasterLine(GEO2DCoordinatesData coordinatesData, ICanvas canvas, GEORasterProjection projection)
  {
	if (coordinatesData != null)
	{
	  final java.util.ArrayList<Geodetic2D> coordinates = coordinatesData.getCoordinates();
  
	  final int coordinatesCount = coordinates.size();
	  if (coordinatesCount > 0)
	  {
		canvas.beginPath();
  
		canvas.moveTo(projection.project(coordinates.get(0)));
  
		for (int i = 1; i < coordinatesCount; i++)
		{
		  final Geodetic2D coordinate = coordinates.get(i);
  
		  canvas.lineTo(projection.project(coordinate));
		}
  
		canvas.stroke();
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rasterPolygon(const GEO2DPolygonData* polygonData, boolean rasterSurface, boolean rasterBoundary, ICanvas* canvas, const GEORasterProjection* projection) const
  protected final void rasterPolygon(GEO2DPolygonData polygonData, boolean rasterSurface, boolean rasterBoundary, ICanvas canvas, GEORasterProjection projection)
  {
	if (rasterSurface || rasterBoundary)
	{
	  final java.util.ArrayList<Geodetic2D> coordinates = polygonData.getCoordinates();
	  final int coordinatesCount = coordinates.size();
	  if (coordinatesCount > 1)
	  {
		canvas.beginPath();
  
		canvas.moveTo(projection.project(coordinates.get(0)));
  
		for (int i = 1; i < coordinatesCount; i++)
		{
		  final Geodetic2D coordinate = coordinates.get(i);
  
		  canvas.lineTo(projection.project(coordinate));
		}
  
		canvas.closePath();
  
		final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray = polygonData.getHolesCoordinatesArray();
		if (holesCoordinatesArray != null)
		{
		  final int holesCoordinatesArraySize = holesCoordinatesArray.size();
		  for (int j = 0; j < holesCoordinatesArraySize; j++)
		  {
			final java.util.ArrayList<Geodetic2D> holeCoordinates = holesCoordinatesArray.get(j);
  
			final int holeCoordinatesCount = holeCoordinates.size();
			if (holeCoordinatesCount > 1)
			{
			  canvas.moveTo(projection.project(holeCoordinates.get(0)));
  
			  for (int i = 1; i < holeCoordinatesCount; i++)
			  {
				final Geodetic2D holeCoordinate = holeCoordinates.get(i);
  
				canvas.lineTo(projection.project(holeCoordinate));
			  }
  
			  canvas.closePath();
			}
		  }
		}
  
  
		if (rasterBoundary)
		{
		  if (rasterSurface)
		  {
			canvas.fillAndStroke();
		  }
		  else
		  {
			canvas.stroke();
		  }
		}
		else
		{
		  canvas.fill();
		}
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rasterRectangle(const GEO2DPolygonData* rectangleData, const Vector2F rectangleSize, boolean rasterSurface, boolean rasterBoundary, ICanvas* canvas, const GEORasterProjection* projection) const
  protected final void rasterRectangle(GEO2DPolygonData rectangleData, Vector2F rectangleSize, boolean rasterSurface, boolean rasterBoundary, ICanvas canvas, GEORasterProjection projection)
  {
  
	if (rasterSurface || rasterBoundary)
	{
	  final java.util.ArrayList<Geodetic2D> coordinates = rectangleData.getCoordinates();
	  final int coordinatesCount = coordinates.size();
  
	  if (coordinatesCount > 1)
	  {
		canvas.beginPath();
  
		canvas.moveTo(projection.project(coordinates.get(0)));
  
		for (int i = 1; i < coordinatesCount; i++)
		{
		  final Geodetic2D coordinate = coordinates.get(i);
  
		  canvas.lineTo(projection.project(coordinate));
		}
  
		canvas.closePath();
  
		if (rasterBoundary)
		{
		  if (rasterSurface)
		  {
			canvas.fillAndStroke();
		  }
		  else
		  {
			canvas.stroke();
		  }
		}
		else
		{
		  canvas.fill();
		}
	  }
	  else if (coordinatesCount == 1)
	  {
  
		final Geodetic2D coordinate = coordinates.get(0);
  
		Vector2F center = projection.project(coordinate);
		Vector2F topLeft = new Vector2F(center._x-(rectangleSize._x/2.0f),center._y-(rectangleSize._y/2.0f));
  
		if (rasterBoundary)
		{
		  if (rasterSurface)
		  {
			canvas.fillAndStrokeRectangle(topLeft._x, topLeft._y, rectangleSize._x, rectangleSize._y);
		  }
		  else
		  {
			canvas.strokeRectangle(topLeft._x, topLeft._y, rectangleSize._x, rectangleSize._y);
		  }
		}
		else
		{
		  canvas.fillRectangle(topLeft._x, topLeft._y, rectangleSize._x, rectangleSize._y);
		}
	  }
  
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void rawRasterize(ICanvas* canvas, const GEORasterProjection* projection) const = 0;
  protected abstract void rawRasterize(ICanvas canvas, GEORasterProjection projection);


  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean symbolize(const G3MRenderContext* rc, const GEOSymbolizer* symbolizer, MeshRenderer* meshRenderer, ShapesRenderer* shapesRenderer, MarksRenderer* marksRenderer, GEOVectorLayer* geoVectorLayer) const
  public final boolean symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
	if (geoVectorLayer == null)
	{
	  ILogger.instance().logError("Can't symbolize with RasterSymbol, GEOVectorLayer was not set");
	  return true;
	}
  
	geoVectorLayer.addSymbol(this);
  
	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Sector* getSector() const = 0;
  public abstract Sector getSector();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rasterize(ICanvas* canvas, const GEORasterProjection* projection, int tileLevel) const
  public final void rasterize(ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
	if (((_minTileLevel < 0) || (tileLevel >= _minTileLevel)) && ((_maxTileLevel < 0) || (tileLevel <= _maxTileLevel)))
	{
	  rawRasterize(canvas, projection);
	}
  }

}
