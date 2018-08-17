package org.glob3.mobile.generated;import java.util.*;

//
//  GEO2DCoordinatesArrayData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//

//
//  GEO2DCoordinatesArrayData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DCoordinatesData;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;

public class GEO2DCoordinatesArrayData extends RCObject
{
  private final java.util.ArrayList<GEO2DCoordinatesData> _coordinatesArray;

  private Sector _sector;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector* calculateSector() const
  private Sector calculateSector()
  {
	final IMathUtils mu = IMathUtils.instance();
  
	final double maxDouble = mu.maxDouble();
	final double minDouble = mu.minDouble();
  
	double minLatInRadians = maxDouble;
	double maxLatInRadians = minDouble;
  
	double minLonInRadians = maxDouble;
	double maxLonInRadians = minDouble;
  
	final int coordinatesArrayCount = _coordinatesArray.size();
	for (int i = 0; i < coordinatesArrayCount; i++)
	{
	  final GEO2DCoordinatesData coordinates = _coordinatesArray.get(i);
	  final int coordinatesCount = coordinates.size();
	  for (int j = 0; j < coordinatesCount; j++)
	  {
		final Geodetic2D coordinate = coordinates.get(j);
  
		final double latInRadians = coordinate._latitude._radians;
		if (latInRadians < minLatInRadians)
		{
		  minLatInRadians = latInRadians;
		}
		if (latInRadians > maxLatInRadians)
		{
		  maxLatInRadians = latInRadians;
		}
  
		final double lonInRadians = coordinate._longitude._radians;
		if (lonInRadians < minLonInRadians)
		{
		  minLonInRadians = lonInRadians;
		}
		if (lonInRadians > maxLonInRadians)
		{
		  maxLonInRadians = lonInRadians;
		}
	  }
	}
  
	if ((minLatInRadians == maxDouble) || (maxLatInRadians == minDouble) || (minLonInRadians == maxDouble) || (maxLonInRadians == minDouble))
	{
	  return null;
	}
  
	Sector result = new Sector(Geodetic2D.fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001), Geodetic2D.fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));
  
	//  int __REMOVE_DEBUG_CODE;
	//  for (int i = 0; i < coordinatesArrayCount; i++) {
	//    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
	//    const int coordinatesCount = coordinates->size();
	//    for (int j = 0; j < coordinatesCount; j++) {
	//      const Geodetic2D* coordinate = coordinates->at(j);
	//      if (!result->contains(*coordinate)) {
	//        printf("xxx\n");
	//      }
	//    }
	//  }
  
	return result;
  }

  public void dispose()
  {
	if (_coordinatesArray != null)
	{
	  final int coordinatesArrayCount = _coordinatesArray.size();
	  for (int i = 0; i < coordinatesArrayCount; i++)
	  {
		final GEO2DCoordinatesData coordinates = _coordinatesArray.get(i);
		//if (coordinates != NULL) {
		  coordinates._release();
		//}
	  }
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  _coordinatesArray = null;
//#endif
	}
  
	if (_sector != null)
		_sector.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public GEO2DCoordinatesArrayData(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
	  _sector = null;
	if (coordinatesArray == null)
	{
	  _coordinatesArray = null;
	}
	else
	{
	  _coordinatesArray = new java.util.ArrayList<const GEO2DCoordinatesData*>();
	  final int size = coordinatesArray.size();
	  for (int i = 0; i < size; i++)
	  {
		java.util.ArrayList<Geodetic2D> coordinates = coordinatesArray.get(i);
		if (coordinates != null)
		{
		  _coordinatesArray.add(new GEO2DCoordinatesData(coordinates));
		}
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return (_coordinatesArray == null) ? 0 : _coordinatesArray.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GEO2DCoordinatesData* get(int index) const
  public final GEO2DCoordinatesData get(int index)
  {
	return _coordinatesArray.get(index);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector* getSector() const
  public final Sector getSector()
  {
	if (_sector == null)
	{
	  _sector = calculateSector();
	}
	return _sector;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getCoordinatesCount() const
  public final long getCoordinatesCount()
  {
	long result = 0;
	if (_coordinatesArray != null)
	{
	  final int coordinatesArrayCount = _coordinatesArray.size();
	  for (int i = 0; i < coordinatesArrayCount; i++)
	  {
		final GEO2DCoordinatesData coordinates = _coordinatesArray.get(i);
		result += coordinates.getCoordinatesCount();
	  }
	}
	return result;
  }

}
