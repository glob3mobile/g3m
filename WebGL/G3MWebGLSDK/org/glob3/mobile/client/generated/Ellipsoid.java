package org.glob3.mobile.generated; 
//
//  Ellipsoid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Ellipsoid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//





public class Ellipsoid
{
  private final Vector3D _radii ;
  private final Vector3D _radiiSquared ;
  private final Vector3D _radiiToTheFourth ;
  private final Vector3D _oneOverRadiiSquared ;



  public Ellipsoid(Vector3D radii)
  {
	  _radii = new Vector3D(radii);
	  _radiiSquared = new Vector3D(new Vector3D(radii.x() * radii.x(), radii.y() * radii.y(), radii.z() * radii.z()));
	  _radiiToTheFourth = new Vector3D(new Vector3D(_radiiSquared.x() * _radiiSquared.x(), _radiiSquared.y() * _radiiSquared.y(), _radiiSquared.z() * _radiiSquared.z()));
	  _oneOverRadiiSquared = new Vector3D(new Vector3D(1.0 / (radii.x() * radii.x()), 1.0 / (radii.y() * radii.y()), 1.0 / (radii.z() * radii.z())));
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getRadii() const
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
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const
  public final Vector3D geodeticSurfaceNormal(Geodetic3D geodetic)
  {
	final double cosLatitude = geodetic.latitude().cosinus();
  
	return new Vector3D(cosLatitude * geodetic.longitude().cosinus(), cosLatitude * geodetic.longitude().sinus(), geodetic.latitude().sinus());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const
  public final Vector3D geodeticSurfaceNormal(Geodetic2D geodetic)
  {
	final double cosLatitude = geodetic.latitude().cosinus();
  
	return new Vector3D(cosLatitude * geodetic.longitude().cosinus(), cosLatitude * geodetic.longitude().sinus(), geodetic.latitude().sinus());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<double> intersectionsDistances(const Vector3D& origin, const Vector3D& direction) const
  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, Vector3D direction)
  {
	java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
  
	int __ASK_Normalized_or_not;
	//direction.Normalize();
  
	// By laborious algebraic manipulation....
	final double a = (direction.x() * direction.x() * _oneOverRadiiSquared.x() + direction.y() * direction.y() * _oneOverRadiiSquared.y() + direction.z() * direction.z() * _oneOverRadiiSquared.z());
  
	final double b = 2.0 * (origin.x() * direction.x() * _oneOverRadiiSquared.x() + origin.y() * direction.y() * _oneOverRadiiSquared.y() + origin.z() * direction.z() * _oneOverRadiiSquared.z());
  
	final double c = (origin.x() * origin.x() * _oneOverRadiiSquared.x() + origin.y() * origin.y() * _oneOverRadiiSquared.y() + origin.z() * origin.z() * _oneOverRadiiSquared.z() - 1.0);
  
	// Solve the quadratic equation: ax^2 + bx + c = 0.
	// Algorithm is from Wikipedia's "Quadratic equation" topic, and Wikipedia credits
	// Numerical Recipes in C, section 5.6: "Quadratic and Cubic Equations"
	final double discriminant = b * b - 4 * a * c;
	if (discriminant < 0.0)
	{
	  // no intersections
	  return intersections;
	}
	else if (discriminant == 0.0)
	{
	  // one intersection at a tangent point
	  //return new double[1] { -0.5 * b / a };
	  intersections.add(-0.5 * b / a);
	  return intersections;
	}
  
	final double t = -0.5 * (b + (b > 0.0 ? 1.0 : -1.0) * IMathUtils.instance().sqrt(discriminant));
	final double root1 = t / a;
	final double root2 = c / t;
  
	// Two intersections - return the smallest first.
	if (root1 < root2)
	{
	  intersections.add(root1);
	  intersections.add(root2);
	}
	else
	{
	  intersections.add(root2);
	  intersections.add(root1);
	}
	return intersections;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D toCartesian(const Geodetic3D& geodetic) const
  public final Vector3D toCartesian(Geodetic3D geodetic)
  {
	final Vector3D n = geodeticSurfaceNormal(geodetic);
	final Vector3D k = _radiiSquared.times(n);
	final double gamma = IMathUtils.instance().sqrt((k.x() * n.x()) + (k.y() * n.y()) + (k.z() * n.z()));
  
	final Vector3D rSurface = k.div(gamma);
	return rSurface.add(n.times(geodetic.height()));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D toCartesian(const Geodetic2D& geodetic) const
  public final Vector3D toCartesian(Geodetic2D geodetic)
  {
	return toCartesian(new Geodetic3D(geodetic, 0.0));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D toGeodetic2D(const Vector3D& positionOnEllipsoid) const
  public final Geodetic2D toGeodetic2D(Vector3D positionOnEllipsoid)
  {
	final Vector3D n = geodeticSurfaceNormal(positionOnEllipsoid);
  
	return new Geodetic2D(Angle.fromRadians(IMathUtils.instance().asin(n.z())), Angle.fromRadians(IMathUtils.instance().atan2(n.y(), n.x())));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D toGeodetic3D(const Vector3D& position) const
  public final Geodetic3D toGeodetic3D(Vector3D position)
  {
	Vector3D p = scaleToGeodeticSurface(position);
	Vector3D h = position.sub(p);
  
	double height;
	if (h.dot(position) < 0)
	{
	  height = -1 * h.length();
	}
	else
	{
	  height = h.length();
	}
  
	return new Geodetic3D(toGeodetic2D(p), height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D scaleToGeodeticSurface(const Vector3D& position) const
  public final Vector3D scaleToGeodeticSurface(Vector3D position)
  {
	double beta = 1.0 / IMathUtils.instance().sqrt((position.x() * position.x()) * _oneOverRadiiSquared.x() + (position.y() * position.y()) * _oneOverRadiiSquared.y() + (position.z() * position.z()) * _oneOverRadiiSquared.z());
  
	double n = new Vector3D(beta * position.x() * _oneOverRadiiSquared.x(), beta * position.y() * _oneOverRadiiSquared.y(), beta * position.z() * _oneOverRadiiSquared.z()).length();
  
	double alpha = (1.0 - beta) * (position.length() / n);
  
	double x2 = position.x() * position.x();
	double y2 = position.y() * position.y();
	double z2 = position.z() * position.z();
  
	double da = 0.0;
	double db = 0.0;
	double dc = 0.0;
  
	double s = 0.0;
	double dSdA = 1.0;
  
	do
	{
	  alpha -= (s / dSdA);
  
	  da = 1.0 + (alpha * _oneOverRadiiSquared.x());
	  db = 1.0 + (alpha * _oneOverRadiiSquared.y());
	  dc = 1.0 + (alpha * _oneOverRadiiSquared.z());
  
	  double da2 = da * da;
	  double db2 = db * db;
	  double dc2 = dc * dc;
  
	  double da3 = da * da2;
	  double db3 = db * db2;
	  double dc3 = dc * dc2;
  
	  s = x2 / (_radiiSquared.x() * da2) + y2 / (_radiiSquared.y() * db2) + z2 / (_radiiSquared.z() * dc2) - 1.0;
  
	  dSdA = -2.0 * (x2 / (_radiiToTheFourth.x() * da3) + y2 / (_radiiToTheFourth.y() * db3) + z2 / (_radiiToTheFourth.z() * dc3));
	}
	while (IMathUtils.instance().abs(s) > 1e-10);
  
	return new Vector3D(position.x() / da, position.y() / db, position.z() / dc);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D scaleToGeocentricSurface(const Vector3D& position) const
  public final Vector3D scaleToGeocentricSurface(Vector3D position)
  {
	double beta = 1.0 / IMathUtils.instance().sqrt((position.x() * position.x()) * _oneOverRadiiSquared.x() + (position.y() * position.y()) * _oneOverRadiiSquared.y() + (position.z() * position.z()) * _oneOverRadiiSquared.z());
  
	return position.times(beta);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.LinkedList<Vector3D> computeCurve(const Vector3D& start, const Vector3D& stop, double granularity) const
  public final java.util.LinkedList<Vector3D> computeCurve(Vector3D start, Vector3D stop, double granularity)
  {
	if (granularity <= 0.0)
	{
	  //throw new ArgumentOutOfRangeException("granularity", "Granularity must be greater than zero.");
	  return new java.util.LinkedList<Vector3D>();
	}
  
	Vector3D normal = start.cross(stop).normalized();
	double theta = start.angleBetween(stop).radians();
  
	//int n = max((int)(theta / granularity) - 1, 0);
	int n = ((int)(theta / granularity) - 1) > 0 ? (int)(theta / granularity) - 1 : 0;
  
	java.util.LinkedList<Vector3D> positions = new java.util.LinkedList<Vector3D>();
  
	positions.addLast(start);
  
	for (int i = 1; i <= n; ++i)
	{
	  double phi = (i * granularity);
  
	  positions.addLast(scaleToGeocentricSurface(start.rotateAroundAxis(normal, Angle.fromRadians(phi))));
	}
  
	positions.addLast(stop);
  
	return positions;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const
  public final Geodetic2D getMidPoint (Geodetic2D P0, Geodetic2D P1)
  {
	final Vector3D v0 = toCartesian(P0);
	final Vector3D v1 = toCartesian(P1);
	final Vector3D normal = v0.cross(v1).normalized();
	final Angle theta = v0.angleBetween(v1);
	final Vector3D midPoint = scaleToGeocentricSurface(v0.rotateAroundAxis(normal, theta.times(0.5)));
	return toGeodetic2D(midPoint);
  }



  // compute distance from two points
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double computePreciseLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const
  public final double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
	final Vector3D radius = _radii;
	double R = (radius.x() + radius.y() + radius.z()) / 3;
	double medLat = g1.latitude().degrees();
	double medLon = g1.longitude().degrees();
  
	// spheric distance from P to Q
	// this is the right form, but it's the most complex
	// theres is a minimum error considering sphere instead of ellipsoid
	double latP = g2.latitude().radians();
	double lonP = g2.longitude().radians();
	double latQ = medLat / 180 * IMathUtils.instance().pi();
	double lonQ = medLon / 180 * IMathUtils.instance().pi();
	double coslatP = IMathUtils.instance().cos(latP);
	double sinlatP = IMathUtils.instance().sin(latP);
	double coslonP = IMathUtils.instance().cos(lonP);
	double sinlonP = IMathUtils.instance().sin(lonP);
	double coslatQ = IMathUtils.instance().cos(latQ);
	double sinlatQ = IMathUtils.instance().sin(latQ);
	double coslonQ = IMathUtils.instance().cos(lonQ);
	double sinlonQ = IMathUtils.instance().sin(lonQ);
	double pq = coslatP * sinlonP * coslatQ * sinlonQ + sinlatP * sinlatQ + coslatP * coslonP * coslatQ * coslonQ;
	return IMathUtils.instance().acos(pq) * R;
  }


  // compute distance from two points
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double computeFastLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const
  public final double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
	final Vector3D radius = _radii;
	double R = (radius.x() + radius.y() + radius.z()) / 3;
  
	double medLat = g1.latitude().degrees();
	double medLon = g1.longitude().degrees();
  
	// this way is faster, and works properly further away from the poles
	//double diflat = fabs(g.latitude()-medLat);
	double diflat = IMathUtils.instance().abs(g2.latitude().degrees() - medLat);
	if (diflat > 180)
		diflat = 360 - diflat;
	double diflon = IMathUtils.instance().abs(g2.longitude().degrees() - medLon);
	if (diflon > 180)
		diflon = 360 - diflon;
	double dist = IMathUtils.instance().sqrt(diflat * diflat + diflon * diflon);
	return dist * IMathUtils.instance().pi() / 180 * R;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D closestPointToSphere(const Vector3D& pos, const Vector3D& ray) const
  public final Vector3D closestPointToSphere(Vector3D pos, Vector3D ray)
  {
	double t = 0;
  
	// compute radius for the rotation
	double R0 = (_radii.x() + _radii.y() + _radii.y()) /3;
  
	// compute the point in this ray that are to a distance R from the origin.
	double U2 = ray.squaredLength();
	double O2 = pos.squaredLength();
	double OU = pos.dot(ray);
	double a = U2;
	double b = 2 * OU;
	double c = O2 - R0 * R0;
	double rad = b * b - 4 * a * c;
  
	// if there is solution, the ray intersects the sphere
	if (rad > 0)
	{
	  // compute the final point (the smaller positive t value)
	  t = (-b - IMathUtils.instance().sqrt(rad)) / (2 * a);
	  if (t < 1)
		  t = (-b + IMathUtils.instance().sqrt(rad)) / (2 * a);
	  // if the ideal ray intersects, but not the mesh --> case 2
	  if (t < 1)
		  rad = -12345;
	}
  
	// if no solution found, find a point in the contour line
	if (rad < 0)
	{
	  double D = IMathUtils.instance().sqrt(O2);
	  double co2 = R0 * R0 / (D * D);
	  double a_ = OU * OU - co2 * O2 * U2;
	  double b_ = 2 * OU * O2 - co2 * 2 * OU * O2;
	  double c_ = O2 * O2 - co2 * O2 * O2;
	  double rad_ = b_ * b_ - 4 * a_ * c_;
	  t = (-b_ - IMathUtils.instance().sqrt(rad_)) / (2 * a_);
	}
  
	// compute the final point
	Vector3D p = pos.add(ray.times(t));
	return p;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D closestIntersection(const Vector3D& pos, const Vector3D& ray) const
  public final Vector3D closestIntersection(Vector3D pos, Vector3D ray)
  {
	java.util.ArrayList<Double> distances = intersectionsDistances(pos, ray);
	if (distances.isEmpty())
	{
	  return Vector3D.nan();
	}
	return pos.add(ray.times(distances.get(0)));
  }
}