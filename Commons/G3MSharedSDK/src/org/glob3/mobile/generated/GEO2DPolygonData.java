package org.glob3.mobile.generated;import java.util.*;

//
//  GEO2DPolygonData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

//
//  GEO2DPolygonData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//


///#include <vector>
///#include "RCObject.hpp"
//class Geodetic2D;

public class GEO2DPolygonData extends GEO2DCoordinatesData
{
  private final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _holesCoordinatesArray;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const java.util.ArrayList<Geodetic2D*>* coordinates, const Geodetic2D& point) const
  private boolean contains(java.util.ArrayList<Geodetic2D> coordinates, Geodetic2D point)
  {
	int sidesCrossedMovingRight = 0;
	final int coordinatesCount = coordinates.size();
	int previousIndex = coordinatesCount - 1;
  
	for (int index = 0; index < coordinatesCount; index++)
	{
  
	  Geodetic2D firstCoordinate = coordinates.get(previousIndex);
	  Geodetic2D secondCoordinate = coordinates.get(index);
  
	  if (!firstCoordinate.isEquals(secondCoordinate))
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (firstCoordinate->isEquals(point))
		if (firstCoordinate.isEquals(new Geodetic2D(point)))
		{
		  return true;
		}
  
		final double pointLatInDegrees = point._latitude._degrees;
		final double firstCoorLatInDegrees = firstCoordinate._latitude._degrees;
		final double secondCoorLatInDegrees = secondCoordinate._latitude._degrees;
  
		final double pointLonInDegrees = point._longitude._degrees;
		final double firstCoorLonInDegrees = firstCoordinate._longitude._degrees;
		final double secondCoorLonInDegrees = secondCoordinate._longitude._degrees;
  
  
		if ((firstCoorLatInDegrees <= pointLatInDegrees && pointLatInDegrees < secondCoorLatInDegrees) || (secondCoorLatInDegrees <= pointLatInDegrees && pointLatInDegrees < firstCoorLatInDegrees))
		{
  
		  double intersectLongitudInDegrees = (secondCoorLonInDegrees - firstCoorLonInDegrees) * (pointLatInDegrees - firstCoorLatInDegrees) / (secondCoorLatInDegrees - firstCoorLatInDegrees) + firstCoorLonInDegrees;
  
  
		  if (pointLonInDegrees <= intersectLongitudInDegrees)
		  {
			sidesCrossedMovingRight++;
		  }
		}
	  }
	  previousIndex = index;
	}
	if (sidesCrossedMovingRight == 0)
	{
	  return false;
	}
  
	return sidesCrossedMovingRight % 2 == 1;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	if (_holesCoordinatesArray != null)
	{
	  final int holesCoordinatesArraySize = _holesCoordinatesArray.size();
	  for (int j = 0; j < holesCoordinatesArraySize; j++)
	  {
		final java.util.ArrayList<Geodetic2D> holeCoordinates = _holesCoordinatesArray.get(j);
  
		final int holeCoordinatesCount = holeCoordinates.size();
		for (int i = 0; i < holeCoordinatesCount; i++)
		{
		  final Geodetic2D holeCoordinate = holeCoordinates.get(i);
  
		  if (holeCoordinate != null)
			  holeCoordinate.dispose();
		}
  
		holeCoordinates = null;
	  }
	  _holesCoordinatesArray = null;
	}
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public GEO2DPolygonData(java.util.ArrayList<Geodetic2D> coordinates, java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray)
  {
	  super(coordinates);
	  _holesCoordinatesArray = holesCoordinatesArray;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<java.util.ArrayList<Geodetic2D*>*>* getHolesCoordinatesArray() const
  public final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> getHolesCoordinatesArray()
  {
	return _holesCoordinatesArray;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getCoordinatesCount() const
  public final long getCoordinatesCount()
  {
	long result = super.getCoordinatesCount();
	if (_holesCoordinatesArray != null)
	{
	  final int holesCoordinatesArraySize = _holesCoordinatesArray.size();
	  for (int j = 0; j < holesCoordinatesArraySize; j++)
	  {
		final java.util.ArrayList<Geodetic2D> holeCoordinates = _holesCoordinatesArray.get(j);
		result += holeCoordinates.size();
	  }
	}
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Geodetic2D& point) const
  public final boolean contains(Geodetic2D point)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (getSector()->contains(point))
	if (getSector().contains(new Geodetic2D(point)))
	{
	  if (_holesCoordinatesArray != null)
	  {
		final int holesCoordinatesArraySize = _holesCoordinatesArray.size();
		for (int j = 0; j < holesCoordinatesArraySize; j++)
		{
		  final java.util.ArrayList<Geodetic2D> holeCoordinates = _holesCoordinatesArray.get(j);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (contains(holeCoordinates, point))
		  if (contains(holeCoordinates, new Geodetic2D(point)))
		  {
			return false;
		  }
		}
	  }
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return contains(getCoordinates(), point);
	  return contains(getCoordinates(), new Geodetic2D(point));
	}
  
	return false;
  }

}
