package org.glob3.mobile.generated;import java.util.*;

//
//  GEO2DMultiLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEO2DMultiLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DCoordinatesArrayData;

public class GEO2DMultiLineStringGeometry extends GEOGeometry2D
{
  private final GEO2DCoordinatesArrayData _coordinatesArrayData;

  private GEO2DMultiLineStringGeometry(GEO2DCoordinatesArrayData coordinatesArrayData)
  {
	  _coordinatesArrayData = coordinatesArrayData;
	if (_coordinatesArrayData != null)
	{
	  _coordinatesArrayData._retain();
	}
  }

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


  public GEO2DMultiLineStringGeometry(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
	_coordinatesArrayData = (coordinatesArray == null) ? null : new GEO2DCoordinatesArrayData(coordinatesArray);
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
//ORIGINAL LINE: const GEO2DCoordinatesArrayData* getCoordinatesArray() const
  public final GEO2DCoordinatesArrayData getCoordinatesArray()
  {
	return _coordinatesArrayData;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getCoordinatesCount() const
  public final long getCoordinatesCount()
  {
	return (_coordinatesArrayData == null) ? 0 : _coordinatesArrayData.getCoordinatesCount();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEO2DMultiLineStringGeometry* deepCopy() const
  public final GEO2DMultiLineStringGeometry deepCopy()
  {
	return new GEO2DMultiLineStringGeometry(_coordinatesArrayData);
  }

}
