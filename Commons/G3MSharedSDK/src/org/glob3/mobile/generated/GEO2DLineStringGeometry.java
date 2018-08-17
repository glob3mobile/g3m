package org.glob3.mobile.generated;import java.util.*;

//
//  GEO2DLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEO2DLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;

public class GEO2DLineStringGeometry extends GEOGeometry2D
{
  private final GEO2DCoordinatesData _coordinatesData;

  private GEO2DLineStringGeometry(GEO2DCoordinatesData coordinatesData)
  {
	  _coordinatesData = coordinatesData;
	if (_coordinatesData != null)
	{
	  _coordinatesData._retain();
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


  public GEO2DLineStringGeometry(java.util.ArrayList<Geodetic2D> coordinates)
  {
	_coordinatesData = (coordinates == null) ? null : new GEO2DCoordinatesData(coordinates);
  }

  public void dispose()
  {
	if (_coordinatesData != null)
	{
	  _coordinatesData._release();
	}
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GEO2DCoordinatesData* getCoordinates() const
  public final GEO2DCoordinatesData getCoordinates()
  {
	return _coordinatesData;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getCoordinatesCount() const
  public final long getCoordinatesCount()
  {
	return _coordinatesData.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEO2DLineStringGeometry* deepCopy() const
  public final GEO2DLineStringGeometry deepCopy()
  {
	return new GEO2DLineStringGeometry(_coordinatesData);
  }

}
