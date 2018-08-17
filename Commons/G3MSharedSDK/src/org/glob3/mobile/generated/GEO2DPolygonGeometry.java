package org.glob3.mobile.generated;import java.util.*;

//
//  GEO2DPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEO2DPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DPolygonData;

public class GEO2DPolygonGeometry extends GEOGeometry2D
{
  private final GEO2DPolygonData _polygonData;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const
  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
	return symbolizer.createSymbols(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const
  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
	return symbolizer.createSymbols(this);
  }

  public GEO2DPolygonGeometry(GEO2DPolygonData polygonData)
  {
	  _polygonData = polygonData;
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
//ORIGINAL LINE: const GEO2DPolygonData* getPolygonData() const
  public final GEO2DPolygonData getPolygonData()
  {
	return _polygonData;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<Geodetic2D*>* getCoordinates() const
  public final java.util.ArrayList<Geodetic2D> getCoordinates()
  {
	return _polygonData.getCoordinates();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<java.util.ArrayList<Geodetic2D*>*>* getHolesCoordinatesArray() const
  public final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> getHolesCoordinatesArray()
  {
	return _polygonData.getHolesCoordinatesArray();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getCoordinatesCount() const
  public final long getCoordinatesCount()
  {
	return (_polygonData == null) ? 0 : _polygonData.getCoordinatesCount();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEO2DPolygonGeometry* deepCopy() const
  public final GEO2DPolygonGeometry deepCopy()
  {
	if (_polygonData != null)
	{
	  _polygonData._retain();
	}
	return new GEO2DPolygonGeometry(_polygonData);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contain(const Geodetic2D& point) const
  public final boolean contain(Geodetic2D point)
  {
	if (_polygonData != null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return _polygonData->contains(point);
	  return _polygonData.contains(new Geodetic2D(point));
	}
	return false;
  }

}
