package org.glob3.mobile.generated;import java.util.*;

//
//  LayoutUtils.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo on 18/03/13.
//
//

//
//  LayoutUtils.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo on 18/03/13.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class EllipsoidalPlanet;


public class LayoutUtils
{
  private LayoutUtils()
  {
  }

  public static java.util.ArrayList<Geodetic3D> splitOverCircle(EllipsoidalPlanet EllipsoidalPlanet, Geodetic3D center, double radiusInMeters, int splits)
  {
	  return splitOverCircle(EllipsoidalPlanet, center, radiusInMeters, splits, Angle.zero());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static java.util.ArrayList<Geodetic3D*> splitOverCircle(const EllipsoidalPlanet* EllipsoidalPlanet, const Geodetic3D& center, double radiusInMeters, int splits, const Angle& startAngle = Angle::zero())
  public static java.util.ArrayList<Geodetic3D> splitOverCircle(EllipsoidalPlanet EllipsoidalPlanet, Geodetic3D center, double radiusInMeters, int splits, Angle startAngle)
  {
	java.util.ArrayList<Geodetic3D> result = new java.util.ArrayList<Geodetic3D>();
  
	final double startAngleInRadians = startAngle._radians;
	final double deltaInRadians = (DefineConstants.PI * 2.0) / splits;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D cartesianCenter = EllipsoidalPlanet->toCartesian(center);
	final Vector3D cartesianCenter = EllipsoidalPlanet.toCartesian(new Geodetic3D(center));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D normal = EllipsoidalPlanet->geodeticSurfaceNormal(center);
	final Vector3D normal = EllipsoidalPlanet.geodeticSurfaceNormal(new Geodetic3D(center));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D northInPlane = Vector3D::upZ().projectionInPlane(normal).normalized().times(radiusInMeters);
	final Vector3D northInPlane = Vector3D.upZ().projectionInPlane(new Vector3D(normal)).normalized().times(radiusInMeters);
  
	for (int i = 0; i < splits; i++)
	{
	  final double angleInRadians = startAngleInRadians + (deltaInRadians * i);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D finalVector = northInPlane.rotateAroundAxis(normal, Angle::fromRadians(angleInRadians));
	  final Vector3D finalVector = northInPlane.rotateAroundAxis(new Vector3D(normal), Angle.fromRadians(angleInRadians));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D cartesianPosition = cartesianCenter.add(finalVector);
	  final Vector3D cartesianPosition = cartesianCenter.add(new Vector3D(finalVector));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: result.push_back(new Geodetic3D(EllipsoidalPlanet->toGeodetic3D(cartesianPosition)));
	  result.add(new Geodetic3D(EllipsoidalPlanet.toGeodetic3D(new Vector3D(cartesianPosition))));
	}
  
	return result;
  }
  public static java.util.ArrayList<Geodetic2D> splitOverCircle(EllipsoidalPlanet EllipsoidalPlanet, Geodetic2D center, double radiusInMeters, int splits)
  {
	  return splitOverCircle(EllipsoidalPlanet, center, radiusInMeters, splits, Angle.zero());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static java.util.ArrayList<Geodetic2D*> splitOverCircle(const EllipsoidalPlanet* EllipsoidalPlanet, const Geodetic2D& center, double radiusInMeters, int splits, const Angle& startAngle = Angle::zero())
  public static java.util.ArrayList<Geodetic2D> splitOverCircle(EllipsoidalPlanet EllipsoidalPlanet, Geodetic2D center, double radiusInMeters, int splits, Angle startAngle)
  {
	java.util.ArrayList<Geodetic2D> result2D = new java.util.ArrayList<Geodetic2D>();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: java.util.ArrayList<Geodetic3D*> result3D = splitOverCircle(EllipsoidalPlanet, Geodetic3D(center, 0), radiusInMeters, splits, startAngle);
	java.util.ArrayList<Geodetic3D> result3D = splitOverCircle(EllipsoidalPlanet, new Geodetic3D(center, 0), radiusInMeters, splits, new Angle(startAngle));
  
	for (int i = 0; i < splits; i++)
	{
	  result2D.add(new Geodetic2D(result3D.get(i).asGeodetic2D()));
	  if (result3D.get(i) != null)
		  result3D.get(i).dispose();
	}
  
	return result2D;
  }
}
