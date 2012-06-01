package org.glob3.mobile.generated; 
//
//  Ellipsoid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//





public class Ellipsoid
{
  private final Vector3D _radii = new Vector3D();
  private final Vector3D _radiiSquared = new Vector3D();
  private final Vector3D _radiiToTheFourth = new Vector3D();
  private final Vector3D _oneOverRadiiSquared = new Vector3D();



//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Ellipsoid(Vector3D radii);

  public final Vector3D getRadii()
  {
	return _radii;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D centricSurfaceNormal(const Vector3D& positionOnEllipsoid) const
  public final Vector3D centricSurfaceNormal(Vector3D positionOnEllipsoid)
  {
	return positionOnEllipsoid.normalized();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Vector3D& positionOnEllipsoid) const
  public final Vector3D geodeticSurfaceNormal(Vector3D positionOnEllipsoid)
  {
	return positionOnEllipsoid.times(_oneOverRadiiSquared).normalized();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector3D geodeticSurfaceNormal(Geodetic3D geodetic);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<double> intersections(const Vector3D& origin, const Vector3D& direction) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  java.util.ArrayList<double> intersections(Vector3D origin, Vector3D direction);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D toVector3D(const Geodetic3D& geodetic) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector3D toVector3D(Geodetic3D geodetic);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D toVector3D(const Geodetic2D& geodetic) const
  public final Vector3D toVector3D(Geodetic2D geodetic)
  {
	return toVector3D(new Geodetic3D(geodetic, 0.0));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D toGeodetic2D(const Vector3D& positionOnEllipsoid) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Geodetic2D toGeodetic2D(Vector3D positionOnEllipsoid);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D toGeodetic3D(const Vector3D& position) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Geodetic3D toGeodetic3D(Vector3D position);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D scaleToGeodeticSurface(const Vector3D& position) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector3D scaleToGeodeticSurface(Vector3D position);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D scaleToGeocentricSurface(const Vector3D& position) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector3D scaleToGeocentricSurface(Vector3D position);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.LinkedList<Vector3D> computeCurve(const Vector3D& start, const Vector3D& stop, double granularity) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  java.util.LinkedList<Vector3D> computeCurve(Vector3D start, Vector3D stop, double granularity);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double computePreciseLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double computeFastLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2);

}