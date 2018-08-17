package org.glob3.mobile.generated;import java.util.*;

//
//  GEO2DCoordinatesData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//

//
//  GEO2DCoordinatesData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;

public class GEO2DCoordinatesData extends RCObject
{
  private final java.util.ArrayList<Geodetic2D> _coordinates;

  private Sector _sector;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector* calculateSector() const
  private Sector calculateSector()
  {
	final int size = _coordinates.size();
	if (size == 0)
	{
	  return null;
	}
  
	final Geodetic2D coordinate0 = _coordinates.get(0);
  
	double minLatInRadians = coordinate0._latitude._radians;
	double maxLatInRadians = minLatInRadians;
  
	double minLonInRadians = coordinate0._longitude._radians;
	double maxLonInRadians = minLonInRadians;
  
	for (int i = 1; i < size; i++)
	{
	  final Geodetic2D coordinate = _coordinates.get(i);
  
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
  
	Sector result = new Sector(Geodetic2D.fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001), Geodetic2D.fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));
  
	//  int __REMOVE_DEBUG_CODE;
	//  for (int i = 0; i < size; i++) {
	//    const Geodetic2D* coordinate = coordinates->at(i);
	//    if (!result->contains(*coordinate)) {
	//      printf("xxx\n");
	//    }
	//  }
  
	return result;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final int coordinatesCount = _coordinates.size();
	for (int i = 0; i < coordinatesCount; i++)
	{
	  Geodetic2D coordinate = _coordinates.get(i);
	  if (coordinate != null)
		  coordinate.dispose();
	}
	_coordinates = null;
//#endif
	if (_sector != null)
		_sector.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public GEO2DCoordinatesData(java.util.ArrayList<Geodetic2D> coordinates)
  {
	  _coordinates = coordinates;
	  _sector = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<Geodetic2D*>* getCoordinates() const
  public final java.util.ArrayList<Geodetic2D> getCoordinates()
  {
	return _coordinates;
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
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return (_coordinates == null) ? 0 : _coordinates.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D* get(int index) const
  public final Geodetic2D get(int index)
  {
	return _coordinates.get(index);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long getCoordinatesCount() const
  public long getCoordinatesCount()
  {
	return (_coordinates == null) ? 0 : _coordinates.size();
  }

}
