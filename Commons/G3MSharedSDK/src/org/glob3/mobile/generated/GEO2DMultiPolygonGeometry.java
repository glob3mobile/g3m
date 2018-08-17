package org.glob3.mobile.generated;import java.util.*;

//
//  GEO2DMultiPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

//
//  GEO2DMultiPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DPolygonData;


public class GEO2DMultiPolygonGeometry extends GEOGeometry2D
{
  private java.util.ArrayList<GEO2DPolygonData> _polygonsData;

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

  protected static java.util.ArrayList<GEO2DPolygonData> copy(java.util.ArrayList<GEO2DPolygonData> polygonsData)
  {
	if (polygonsData == null)
	{
	  return null;
	}
	java.util.ArrayList<GEO2DPolygonData> result = new java.util.ArrayList<GEO2DPolygonData*>();
	final int size = polygonsData.size();
	for (int i = 0; i < size; i++)
	{
	  GEO2DPolygonData each = polygonsData.get(i);
	  if (each != null)
	  {
		each._retain();
	  }
	  result.add(each);
	}
	return result;
  }



  public GEO2DMultiPolygonGeometry(java.util.ArrayList<GEO2DPolygonData> polygonsData)
  {
	  _polygonsData = polygonsData;
  }

  public void dispose()
  {
	if (_polygonsData != null)
	{
	  final int polygonsDataSize = _polygonsData.size();
	  for (int i = 0; i < polygonsDataSize; i++)
	  {
		GEO2DPolygonData polygonData = _polygonsData.get(i);
		if (polygonData != null)
		{
		  polygonData._release();
		}
	  }
	  _polygonsData = null;
	}
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<GEO2DPolygonData*>* getPolygonsData() const
  public final java.util.ArrayList<GEO2DPolygonData> getPolygonsData()
  {
	return _polygonsData;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getCoordinatesCount() const
  public final long getCoordinatesCount()
  {
	return (_polygonsData == null) ? 0 : _polygonsData.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEO2DMultiPolygonGeometry* deepCopy() const
  public final GEO2DMultiPolygonGeometry deepCopy()
  {
	return new GEO2DMultiPolygonGeometry(copy(_polygonsData));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contain(const Geodetic2D& point) const
  public final boolean contain(Geodetic2D point)
  {
	if (_polygonsData == null)
	{
	  return false;
	}
	final int polygonsDataSize = _polygonsData.size();
	for (int i = 0; i < polygonsDataSize; i++)
	{
	  GEO2DPolygonData polygonData = _polygonsData.get(i);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (polygonData->contains(point))
	  if (polygonData.contains(new Geodetic2D(point)))
	  {
		return true;
	  }
	}
  
	return false;
  }
}
