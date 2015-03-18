package org.glob3.mobile.generated; 
//
//  EllipsoidalPlanet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//


//
//  EllipsoidalPlanet.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/05/13.
//
//







public class EllipsoidalPlanet extends Planet
{

  private final Ellipsoid _ellipsoid;

  private MutableVector3D _origin = new MutableVector3D();
  private MutableVector3D _initialPoint = new MutableVector3D();
  private MutableVector3D _oneOverDragRadiiSquared = new MutableVector3D();
  private MutableVector3D _centerPoint = new MutableVector3D();
  private MutableVector3D _centerRay = new MutableVector3D();
  private MutableVector3D _initialPoint0 = new MutableVector3D();
  private MutableVector3D _initialPoint1 = new MutableVector3D();
  private MutableVector3D _lastDragAxis = new MutableVector3D();
  private double _lastDragRadians;
  private double _lastDragRadiansStep;
  private double _angleBetweenInitialRays;
  private double _angleBetweenInitialPoints;
  private boolean _validSingleDrag;

  private SphericalPlanet _sphericalPlanetDoubleDragDelegate; //Same math applied for Double Drag


  public EllipsoidalPlanet(Ellipsoid ellipsoid)
  {
     _ellipsoid = ellipsoid;
     _sphericalPlanetDoubleDragDelegate = null;
  }

  public void dispose()
  {
    super.dispose();
    if (_sphericalPlanetDoubleDragDelegate != null)
       _sphericalPlanetDoubleDragDelegate.dispose();
  }

  public final Vector3D getRadii()
  {
    return _ellipsoid._radii;
  }

  public final Vector3D centricSurfaceNormal(Vector3D positionOnEllipsoidalPlanet)
  {
    return positionOnEllipsoidalPlanet.normalized();
  }

  public final Vector3D geodeticSurfaceNormal(Vector3D positionOnEllipsoidalPlanet)
  {
    return positionOnEllipsoidalPlanet.times(_ellipsoid._oneOverRadiiSquared).normalized();
  }

  public final Vector3D geodeticSurfaceNormal(MutableVector3D positionOnEllipsoidalPlanet)
  {
    return positionOnEllipsoidalPlanet.times(_ellipsoid._oneOverRadiiSquared).normalized().asVector3D();
  }

  public final Vector3D geodeticSurfaceNormal(Angle latitude, Angle longitude)
  {
    final double cosLatitude = java.lang.Math.cos(latitude._radians);
  
    return new Vector3D(cosLatitude * java.lang.Math.cos(longitude._radians), cosLatitude * java.lang.Math.sin(longitude._radians), java.lang.Math.sin(latitude._radians));
  }

  public final Vector3D geodeticSurfaceNormal(Geodetic3D geodetic)
  {
    return geodeticSurfaceNormal(geodetic._latitude, geodetic._longitude);
  }

  public final Vector3D geodeticSurfaceNormal(Geodetic2D geodetic)
  {
    return geodeticSurfaceNormal(geodetic._latitude, geodetic._longitude);
  }

  public final void geodeticSurfaceNormal(Angle latitude, Angle longitude, MutableVector3D result)
  {
    final double cosLatitude = java.lang.Math.cos(latitude._radians);
  
    result.set(cosLatitude * java.lang.Math.cos(longitude._radians), cosLatitude * java.lang.Math.sin(longitude._radians), java.lang.Math.sin(latitude._radians));
  }

  public final java.util.ArrayList<Double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ)
  {
    return _ellipsoid.intersectionsDistances(originX, originY, originZ, directionX, directionY, directionZ);
  }

  public final Vector3D toCartesian(Angle latitude, Angle longitude, double height)
  {
    final Vector3D n = geodeticSurfaceNormal(latitude, longitude);
  
    final Vector3D k = _ellipsoid._radiiSquared.times(n);
    final double gamma = IMathUtils.instance().sqrt((k._x * n._x) + (k._y * n._y) + (k._z * n._z));
  
    final Vector3D rSurface = k.div(gamma);
    return rSurface.add(n.times(height));
  }

  public final Vector3D toCartesian(Geodetic3D geodetic)
  {
    return toCartesian(geodetic._latitude, geodetic._longitude, geodetic._height);
  }

  public final Vector3D toCartesian(Geodetic2D geodetic)
  {
    return toCartesian(geodetic._latitude, geodetic._longitude, 0.0);
  }

  public final Vector3D toCartesian(Geodetic2D geodetic, double height)
  {
    return toCartesian(geodetic._latitude, geodetic._longitude, height);
  }

  public final void toCartesian(Angle latitude, Angle longitude, double height, MutableVector3D result)
  {
    geodeticSurfaceNormal(latitude, longitude, result);
    final double nX = result.x();
    final double nY = result.y();
    final double nZ = result.z();
  
    final double kX = nX * _ellipsoid._radiiSquared._x;
    final double kY = nY * _ellipsoid._radiiSquared._y;
    final double kZ = nZ * _ellipsoid._radiiSquared._z;
  
    final double gamma = IMathUtils.instance().sqrt((kX * nX) + (kY * nY) + (kZ * nZ));
  
    final double rSurfaceX = kX / gamma;
    final double rSurfaceY = kY / gamma;
    final double rSurfaceZ = kZ / gamma;
  
    result.set(rSurfaceX + (nX * height), rSurfaceY + (nY * height), rSurfaceZ + (nZ * height));
  }

  public final void toCartesian(Geodetic3D geodetic, MutableVector3D result)
  {
    toCartesian(geodetic._latitude, geodetic._longitude, geodetic._height, result);
  }

  public final void toCartesian(Geodetic2D geodetic, MutableVector3D result)
  {
    toCartesian(geodetic._latitude, geodetic._longitude, 0, result);
  }
  public final void toCartesian(Geodetic2D geodetic, double height, MutableVector3D result)
  {
    toCartesian(geodetic._latitude, geodetic._longitude, height, result);
  }

  public final Geodetic2D toGeodetic2D(Vector3D positionOnEllipsoidalPlanet)
  {
    final Vector3D n = geodeticSurfaceNormal(positionOnEllipsoidalPlanet);
  
    final IMathUtils mu = IMathUtils.instance();
    return new Geodetic2D(Angle.fromRadians(mu.asin(n._z)), Angle.fromRadians(mu.atan2(n._y, n._x)));
  }

  public final Geodetic3D toGeodetic3D(Vector3D position)
  {
    final Vector3D p = scaleToGeodeticSurface(position);
    final Vector3D h = position.sub(p);
  
    final double height = (h.dot(position) < 0) ? -1 * h.length() : h.length();
  
    return new Geodetic3D(toGeodetic2D(p), height);
  }

  public final Vector3D scaleToGeodeticSurface(Vector3D position)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final Vector3D oneOverRadiiSquared = _ellipsoid._oneOverRadiiSquared;
    final Vector3D radiiSquared = _ellipsoid._radiiSquared;
    final Vector3D radiiToTheFourth = _ellipsoid._radiiToTheFourth;
  
    final double beta = 1.0 / mu.sqrt((position._x * position._x) * oneOverRadiiSquared._x + (position._y * position._y) * oneOverRadiiSquared._y + (position._z * position._z) * oneOverRadiiSquared._z);
  
    final double n = new Vector3D(beta * position._x * oneOverRadiiSquared._x, beta * position._y * oneOverRadiiSquared._y, beta * position._z * oneOverRadiiSquared._z).length();
  
    double alpha = (1.0 - beta) * (position.length() / n);
  
    final double x2 = position._x * position._x;
    final double y2 = position._y * position._y;
    final double z2 = position._z * position._z;
  
    double da = 0.0;
    double db = 0.0;
    double dc = 0.0;
  
    double s = 0.0;
    double dSdA = 1.0;
  
    do
    {
      alpha -= (s / dSdA);
  
      da = 1.0 + (alpha * oneOverRadiiSquared._x);
      db = 1.0 + (alpha * oneOverRadiiSquared._y);
      dc = 1.0 + (alpha * oneOverRadiiSquared._z);
  
      final double da2 = da * da;
      final double db2 = db * db;
      final double dc2 = dc * dc;
  
      final double da3 = da * da2;
      final double db3 = db * db2;
      final double dc3 = dc * dc2;
  
      s = (x2 / (radiiSquared._x * da2) + y2 / (radiiSquared._y * db2) + z2 / (radiiSquared._z * dc2) - 1.0);
  
      dSdA = (-2.0 * (x2 / (radiiToTheFourth._x * da3) + y2 / (radiiToTheFourth._y * db3) + z2 / (radiiToTheFourth._z * dc3)));
    }
    while (mu.abs(s) > 1e-10);
  
    return new Vector3D(position._x / da, position._y / db, position._z / dc);
  }

  public final Vector3D scaleToGeocentricSurface(Vector3D position)
  {
    Vector3D oneOverRadiiSquared = _ellipsoid._oneOverRadiiSquared;
  
    final double beta = 1.0 / IMathUtils.instance().sqrt((position._x * position._x) * oneOverRadiiSquared._x + (position._y * position._y) * oneOverRadiiSquared._y + (position._z * position._z) * oneOverRadiiSquared._z);
  
    return position.times(beta);
  }

  public final java.util.LinkedList<Vector3D> computeCurve(Vector3D start, Vector3D stop, double granularity)
  {
    if (granularity <= 0.0)
    {
      //throw new ArgumentOutOfRangeException("granularity", "Granularity must be greater than zero.");
      return new java.util.LinkedList<Vector3D>();
    }
  
    final Vector3D normal = start.cross(stop).normalized();
    final double theta = start.angleInRadiansBetween(stop);
  
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
  public final double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final Vector3D radius = _ellipsoid._radii;
    final double R = (radius._x + radius._y + radius._z) / 3;
  
    // spheric distance from P to Q
    // this is the right form, but it's the most complex
    // theres is a minimum error considering sphere instead of EllipsoidalPlanet
    final double latP = g2._latitude._radians;
    final double lonP = g2._longitude._radians;
    final double latQ = g1._latitude._radians;
    final double lonQ = g1._longitude._radians;
    final double coslatP = mu.cos(latP);
    final double sinlatP = mu.sin(latP);
    final double coslonP = mu.cos(lonP);
    final double sinlonP = mu.sin(lonP);
    final double coslatQ = mu.cos(latQ);
    final double sinlatQ = mu.sin(latQ);
    final double coslonQ = mu.cos(lonQ);
    final double sinlonQ = mu.sin(lonQ);
    final double pq = (coslatP * sinlonP * coslatQ * sinlonQ + sinlatP * sinlatQ + coslatP * coslonP * coslatQ * coslonQ);
    return mu.acos(pq) * R;
  }


  // compute distance from two points
  public final double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final Vector3D radius = _ellipsoid._radii;
    final double R = (radius._x + radius._y + radius._z) / 3;
  
    final double medLat = g1._latitude._degrees;
    final double medLon = g1._longitude._degrees;
  
    // this way is faster, and works properly further away from the poles
    //double diflat = fabs(g._latitude-medLat);
    double diflat = mu.abs(g2._latitude._degrees - medLat);
    if (diflat > 180)
    {
      diflat = 360 - diflat;
    }
  
    double diflon = mu.abs(g2._longitude._degrees - medLon);
    if (diflon > 180)
    {
      diflon = 360 - diflon;
    }
  
    double dist = mu.sqrt(diflat * diflat + diflon * diflon);
    return dist * DefineConstants.PI / 180 * R;
  }

  public final Vector3D closestPointToSphere(Vector3D pos, Vector3D ray)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    double t = 0;
  
    // compute radius for the rotation
    final double R0 = _ellipsoid.getMeanRadius();
  
    // compute the point in this ray that are to a distance R from the origin.
    final double U2 = ray.squaredLength();
    final double O2 = pos.squaredLength();
    final double OU = pos.dot(ray);
    final double a = U2;
    final double b = 2 * OU;
    final double c = O2 - R0 * R0;
    double rad = b * b - 4 * a * c;
  
    // if there is solution, the ray intersects the sphere
    if (rad > 0)
    {
      // compute the final point (the smaller positive t value)
      t = (-b - mu.sqrt(rad)) / (2 * a);
      if (t < 1)
         t = (-b + mu.sqrt(rad)) / (2 * a);
      // if the ideal ray intersects, but not the mesh --> case 2
      if (t < 1)
         rad = -12345;
    }
  
    // if no solution found, find a point in the contour line
    if (rad < 0)
    {
      final double D = mu.sqrt(O2);
      final double co2 = R0 * R0 / (D * D);
      final double a_ = OU * OU - co2 * O2 * U2;
      final double b_ = 2 * OU * O2 - co2 * 2 * OU * O2;
      final double c_ = O2 * O2 - co2 * O2 * O2;
      final double rad_ = b_ * b_ - 4 * a_ * c_;
      t = (-b_ - mu.sqrt(rad_)) / (2 * a_);
    }
  
    // compute the final point
    Vector3D result = pos.add(ray.times(t));
    return result;
  }

  public final Vector3D closestIntersection(Vector3D pos, Vector3D ray)
  {
    return Ellipsoid.closestIntersectionCenteredEllipsoidWithRay(pos, ray, _ellipsoid.getOneOverRadiiSquared());
  }

  public final MutableMatrix44D createGeodeticTransformMatrix(Geodetic3D position)
  {
    final MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(toCartesian(position));
    final MutableMatrix44D rotation = MutableMatrix44D.createGeodeticRotationMatrix(position);
  
    return translation.multiply(rotation);
  }

  public final boolean isFlat()
  {
     return false;
  }

  public final void beginSingleDrag(Vector3D origin, Vector3D touchedPosition)
  {
    _origin = origin.asMutableVector3D();
    //_initialPoint = closestIntersection(origin, initialRay).asMutableVector3D();
    _initialPoint = touchedPosition.asMutableVector3D();
    Vector3D _originalRadiusSquared = _ellipsoid.getRadiiSquared();
    double _dragRadiusFactorSquared = touchedPosition._x * touchedPosition._x / _originalRadiusSquared._x + touchedPosition._y * touchedPosition._y / _originalRadiusSquared._y + touchedPosition._z * touchedPosition._z / _originalRadiusSquared._z;
    _oneOverDragRadiiSquared = new MutableVector3D(1.0 / _dragRadiusFactorSquared / _originalRadiusSquared._x, 1.0 / _dragRadiusFactorSquared / _originalRadiusSquared._y, 1.0 / _dragRadiusFactorSquared / _originalRadiusSquared._z);
  
  /*
    =======
  //  _origin = origin.asMutableVector3D();
    _origin.copyFrom(origin);
  //  _initialPoint = closestIntersection(origin, initialRay).asMutableVector3D();
    _initialPoint.copyFrom(closestIntersection(origin, initialRay));
  >>>>>>> origin/purgatory
   */
  
    _validSingleDrag = false;
  }

  public final MutableMatrix44D singleDrag(Vector3D finalRay)
  {
    // test if initialPoint is valid
    if (_initialPoint.isNan())
       return MutableMatrix44D.invalid();
  
    // compute final point
    final Vector3D origin = _origin.asVector3D();
    MutableVector3D finalPoint = Ellipsoid.closestIntersectionCenteredEllipsoidWithRay(origin, finalRay, _oneOverDragRadiiSquared.asVector3D()).asMutableVector3D();
    if (finalPoint.isNan())
    {
      //printf ("--invalid final point in drag!!\n");
  //    finalPoint = closestPointToSphere(origin, finalRay).asMutableVector3D();
      finalPoint.copyFrom(closestPointToSphere(origin, finalRay));
      if (finalPoint.isNan())
      {
        ILogger.instance().logWarning("EllipsoidalPlanet::singleDrag-> finalPoint is NaN");
        return MutableMatrix44D.invalid();
      }
    }
  
    // compute the rotation axis
    final Vector3D rotationAxis = _initialPoint.cross(finalPoint).asVector3D();
  
    // compute the angle
    double sinus = rotationAxis.length()/_initialPoint.length()/finalPoint.length();
    final Angle rotationDelta = Angle.fromRadians(-IMathUtils.instance().asin(sinus));
    if (rotationDelta.isNan())
       return MutableMatrix44D.invalid();
  
    // save params for possible inertial animations
  //  _lastDragAxis = rotationAxis.asMutableVector3D();
    _lastDragAxis.copyFrom(rotationAxis);
    double radians = rotationDelta._radians;
    _lastDragRadiansStep = radians - _lastDragRadians;
    _lastDragRadians = radians;
    _validSingleDrag = true;
  
    // return rotation matrix
    return MutableMatrix44D.createRotationMatrix(rotationDelta, rotationAxis);
  }

  public final Effect createEffectFromLastSingleDrag()
  {
    if (!_validSingleDrag || _lastDragAxis.isNan())
       return null;
    return new RotateWithAxisEffect(_lastDragAxis.asVector3D(), Angle.fromRadians(_lastDragRadiansStep));
  }


  //This version of double drag gesture assumes that ellipsoidal is almost spherical
  //Subsequently many calculus have been simplified
  public final void beginDoubleDrag(Vector3D origin, Vector3D centerRay, Vector3D centerPosition, Vector3D touchedPosition0, Vector3D touchedPosition1)
  {
  
    if (_sphericalPlanetDoubleDragDelegate == null)
    {
      _sphericalPlanetDoubleDragDelegate = new SphericalPlanet(new Sphere(Vector3D.zero, _ellipsoid._radii.maxAxis()));
    }
    _sphericalPlanetDoubleDragDelegate.beginDoubleDrag(origin, centerRay, centerPosition, touchedPosition0, touchedPosition1);
  }


  public final MutableMatrix44D doubleDrag(Vector3D finalRay0, Vector3D finalRay1, boolean allowRotation)
  {
    return _sphericalPlanetDoubleDragDelegate.doubleDrag(finalRay0, finalRay1, allowRotation);
  }

  public final Effect createDoubleTapEffect(Vector3D origin, Vector3D centerRay, Vector3D touchedPosition)
  {
    //const Vector3D initialPoint = closestIntersection(origin, tapRay);
    if (touchedPosition.isNan())
       return null;
  
    // compute central point of view
    //const Vector3D centerPoint = closestIntersection(origin, centerRay);
    Vector3D originalRadiusSquared = _ellipsoid.getRadiiSquared();
    double dragRadiusFactorSquared = touchedPosition._x * touchedPosition._x / originalRadiusSquared._x + touchedPosition._y * touchedPosition._y / originalRadiusSquared._y + touchedPosition._z * touchedPosition._z / originalRadiusSquared._z;
    Vector3D oneOverDragRadiiSquared = new Vector3D(1.0 / dragRadiusFactorSquared / originalRadiusSquared._x, 1.0 / dragRadiusFactorSquared / originalRadiusSquared._y, 1.0 / dragRadiusFactorSquared / originalRadiusSquared._z);
    Vector3D centerPoint = Ellipsoid.closestIntersectionCenteredEllipsoidWithRay(origin, centerRay, oneOverDragRadiiSquared);
  
    // compute drag parameters
    final IMathUtils mu = IMathUtils.instance();
    final Vector3D axis = touchedPosition.cross(centerPoint);
    final Angle angle = Angle.fromRadians(- mu.asin(axis.length()/touchedPosition.length()/centerPoint.length()));
  
    // compute zoom factor
    final double distanceToGround = toGeodetic3D(origin)._height - toGeodetic3D(touchedPosition)._height;
    final double distance = distanceToGround * 0.6;
  
    // create effect
    return new DoubleTapRotationEffect(TimeInterval.fromSeconds(0.75), axis, angle, distance);
  }

  public final double distanceToHorizon(Vector3D position)
  {
    double R = getRadii().minAxis();
    double D = position.length();
    return Math.sqrt(D *D - R *R);
  }

  public final MutableMatrix44D drag(Geodetic3D origin, Geodetic3D destination)
  {
    final Vector3D P0 = toCartesian(origin);
    final Vector3D P1 = toCartesian(destination);
    final Vector3D axis = P0.cross(P1);
    if (axis.length()<1e-3)
       return MutableMatrix44D.invalid();
  
    final Angle angle = P0.angleBetween(P1);
    final MutableMatrix44D rotation = MutableMatrix44D.createRotationMatrix(angle, axis);
    final Vector3D rotatedP0 = P0.transformedBy(rotation, 1);
    final MutableMatrix44D traslation = MutableMatrix44D.createTranslationMatrix(P1.sub(rotatedP0));
    return traslation.multiply(rotation);
  }

  public final Vector3D getNorth()
  {
    return Vector3D.upZ();
  }

  public final void applyCameraConstrainers(Camera previousCamera, Camera nextCamera)
  {
  
  }

  public final Geodetic3D getDefaultCameraPosition(Sector rendereSector)
  {
    final Vector3D asw = toCartesian(rendereSector.getSW());
    final Vector3D ane = toCartesian(rendereSector.getNE());
    final double height = asw.sub(ane).length() * 1.9;

    return new Geodetic3D(rendereSector._center, height);
  }

  public final String getType()
  {
    return "Ellipsoidal";
  }

}