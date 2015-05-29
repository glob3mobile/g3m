package org.glob3.mobile.generated; 
//
//  SphericalPlanet.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  SphericalPlanet.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




public class SphericalPlanet extends Planet
{
  private Sphere _sphere;
  private final Vector3D _radii ;

  private MutableVector3D _origin = new MutableVector3D();
  private MutableVector3D _initialPoint = new MutableVector3D();
  private double _dragRadius;
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

  private double _prevFactor;

  private double _dragRadius0;
  private double _dragRadius1;
  private double _lastDoubleDragAngle;

  private MutableMatrix44D _dragMatrix = new MutableMatrix44D();
  private MutableMatrix44D _doubleDragMatrix = new MutableMatrix44D();
  private MutableVector3D _translation = new MutableVector3D();
  private MutableVector3D _finalPoint0 = new MutableVector3D();
  private MutableVector3D _finalPoint1 = new MutableVector3D();
  private MutableVector3D _draggedCameraPos = new MutableVector3D();
  private MutableVector3D _transformedFinalRay1 = new MutableVector3D();
  private MutableMatrix44D _geodeticTransformMatrix = new MutableMatrix44D();
  private MutableMatrix44D _translationMatrix = new MutableMatrix44D();
  private MutableMatrix44D _rotationMatrix = new MutableMatrix44D();
  private MutableMatrix44D _generalRotationMatrix = new MutableMatrix44D();
  private MutableVector3D _transformedInitialPoint1 = new MutableVector3D();
  private MutableVector3D _transformedFinalPoint1 = new MutableVector3D();
  private MutableVector3D _transformedCameraPos = new MutableVector3D();
  private MutableVector3D _planeNormal = new MutableVector3D();
  private MutableVector3D _rayToFinalPoint1 = new MutableVector3D();
  private MutableVector3D _initialNormal0 = new MutableVector3D();
  private MutableVector2D _rotationAngles = new MutableVector2D();
  private MutableVector3D _P0 = new MutableVector3D();
  private MutableVector3D _B = new MutableVector3D();
  private MutableVector3D _B0 = new MutableVector3D();
  private MutableVector3D _Ra = new MutableVector3D();
  private MutableVector3D _Rb = new MutableVector3D();
  private int _iter;


  private double _lastCorrectingRollAngle;
  private MutableVector3D _lastCorrectingRollRotationAxis = new MutableVector3D();


  private MutableMatrix44D createDragMatrix(Vector3D initialPoint, Vector3D finalPoint)
  {
    // compute the rotation axis
    final Vector3D rotationAxis = initialPoint.cross(finalPoint);
  
    // compute the angle
    double sinus = rotationAxis.length()/initialPoint.length()/finalPoint.length();
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

  private void createDragMatrix(MutableVector3D initialPoint, MutableVector3D finalPoint, MutableMatrix44D matrix)
  {
    // compute the rotation axis
    final MutableVector3D rotationAxis = initialPoint.cross(finalPoint);
  
    // compute the angle
    double sinus = rotationAxis.length()/initialPoint.length()/finalPoint.length();
    double rotationDeltaInRadians = -IMathUtils.instance().asin(sinus);
    if (rotationDeltaInRadians != rotationDeltaInRadians)
    {
      matrix.setInvalid();
      return;
    }
  
    // return rotation matrix
    matrix.setValid();
    matrix.setRotationMatrix(rotationDeltaInRadians, rotationAxis);
  }


  /*
  // BEFORE REMOVING GARBAGE!
  double SphericalPlanet::testDoubleDragIteration(double factor,
                                                  const Vector3D& finalRay0,
                                                  const Vector3D& finalRay1,
                                                  MutableMatrix44D& matrix) const
  {
    MutableVector3D positionCamera = _origin;
    
    // move forward
    double distance = _centerPoint.sub(_origin).length();
    Vector3D traslation = _centerRay.asVector3D().times(distance*factor);
    matrix.copyValue(MutableMatrix44D::createTranslationMatrix(traslation));
    
    // compute new final points after moving forward
    const Vector3D traslatedOrigin = _origin.asVector3D().add(traslation);
    Vector3D finalPoint0 = Sphere::closestIntersectionCenteredSphereWithRay(traslatedOrigin,
                                                                            finalRay0, _dragRadius0); // A1
    if (finalPoint0.isNan()) {
      matrix.copyValue(MutableMatrix44D::invalid());
      return 0;
    }
    
    // drag initial point 0 to final point 0
    MutableMatrix44D dragMatrix = createDragMatrix(_initialPoint0.asVector3D(), finalPoint0);
    matrix.copyValueOfMultiplication(dragMatrix, matrix);
    
    // transform points to set axis origin in initialPoint0
    {
      Vector3D draggedCameraPos = positionCamera.transformedBy(matrix, 1.0).asVector3D();
      Vector3D finalPoint1 = Sphere::closestIntersectionCenteredSphereWithRay(draggedCameraPos,
                                                                              finalRay1.transformedBy(matrix, 0), _dragRadius1); // B1
      
      //Taking whole system to origin
      MutableMatrix44D M = createGeodeticTransformMatrix(toGeodetic3D(_initialPoint0.asVector3D()));
      MutableMatrix44D transform = M.inversed();
      
      Vector3D transformedInitialPoint1 = _initialPoint1.transformedBy(transform, 1.0).asVector3D();
      Vector3D transformedFinalPoint1 = finalPoint1.transformedBy(transform, 1.0);
      Vector3D transformedCameraPos = draggedCameraPos.transformedBy(transform, 1.0);
      Vector3D v0 = transformedFinalPoint1.sub(transformedCameraPos);
      
      //Angles to rotate transformedInitialPoint1 to adjust the plane that contains origin, TFP1 and TCP
      Vector3D planeNormal = transformedCameraPos.cross(v0).normalized();
      Plane plane(planeNormal, v0);
      Vector2D angles = plane.rotationAngleAroundZAxisToFixPointInRadians(transformedInitialPoint1);
      
      //Selecting best angle to rotate (smallest)
      double angulo1 = angles._x;
      double angulo2 = angles._y;
      double dif1 = Angle::distanceBetweenAnglesInRadians(angulo1, _lastDoubleDragAngle);
      double dif2 = Angle::distanceBetweenAnglesInRadians(angulo2, _lastDoubleDragAngle);
      double finalAngle = (dif1 < dif2)? angulo1 : angulo2;
      if (finalAngle != finalAngle) {
        matrix.copyValue(MutableMatrix44D::invalid());
        return 0;
      }
      
      //Creating rotation matrix
      _lastDoubleDragAngle = finalAngle;
      Vector3D normal0 = geodeticSurfaceNormal(_initialPoint0);
      MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromRadians(-_lastDoubleDragAngle),normal0, _initialPoint0.asVector3D());
      matrix.copyValueOfMultiplication(rotation, matrix); // = rotation.multiply(matrix);
    }
    
    {
      Vector3D P0   = positionCamera.transformedBy(matrix, 1.0).asVector3D();
      Vector3D B    = _initialPoint1.asVector3D();
      Vector3D B0   = B.sub(P0);
      Vector3D Ra   = finalRay0.transformedBy(matrix, 0.0).normalized();
      Vector3D Rb   = finalRay1.transformedBy(matrix, 0.0).normalized();
      return Ra.angleBetween(B0)._degrees - Ra.angleBetween(Rb)._degrees;
    }
  }
  */
  
  
  private double testDoubleDragIteration(double factor, Vector3D finalRay0, Vector3D finalRay1)
  {
    _iter++;
  
    // move forward
    factor *= MutableVector3D.distanceBetween(_centerPoint, _origin);
    _translation.set(_centerRay.x() * factor, _centerRay.y() * factor, _centerRay.z() * factor);
    _doubleDragMatrix.setValid();
    _doubleDragMatrix.setTranslationMatrix(_translation.x(), _translation.y(), _translation.z());
  
    // compute new final points after moving forward
    Sphere.setClosestIntersectionCenteredSphereWithRay(_origin.x() + _translation.x(), _origin.y() + _translation.y(), _origin.z() + _translation.z(), finalRay0._x, finalRay0._y, finalRay0._z, _dragRadius0, _finalPoint0); //A1
    if (_finalPoint0.isNan())
    {
      _doubleDragMatrix.setInvalid();
      return 0;
    }
  
    // drag initial point 0 to final point 0
    createDragMatrix(_initialPoint0, _finalPoint0, _dragMatrix);
    _doubleDragMatrix.copyValueOfMultiplication(_dragMatrix, _doubleDragMatrix);
  
    // transform points to set axis origin in initialPoint0
    _draggedCameraPos.transformPointByMatrix(_origin, _doubleDragMatrix, 1.0);
    _transformedFinalRay1.transformPointByMatrix(finalRay1, _doubleDragMatrix, 0);
    Sphere.setClosestIntersectionCenteredSphereWithRay(_draggedCameraPos.x(), _draggedCameraPos.y(), _draggedCameraPos.z(), _transformedFinalRay1.x(), _transformedFinalRay1.y(), _transformedFinalRay1.z(), _dragRadius1, _finalPoint1); //B1
  
    // Taking whole system to origin
    createInversedGeodeticTransformMatrix(_initialPoint0, _geodeticTransformMatrix);
    _transformedInitialPoint1.transformPointByMatrix(_initialPoint1, _geodeticTransformMatrix, 1.0);
    _transformedFinalPoint1.transformPointByMatrix(_finalPoint1, _geodeticTransformMatrix, 1.0);
    _transformedCameraPos.transformPointByMatrix(_draggedCameraPos, _geodeticTransformMatrix, 1.0);
  
    // Angles to rotate transformedInitialPoint1 to adjust the plane that contains origin, TFP1 and TCP
    _rayToFinalPoint1.set(_transformedFinalPoint1.x()-_transformedCameraPos.x(), _transformedFinalPoint1.y()-_transformedCameraPos.y(), _transformedFinalPoint1.z()-_transformedCameraPos.z());
    _planeNormal.copyValueOfCross(_transformedCameraPos, _rayToFinalPoint1);
    _planeNormal.normalize();
    Plane.rotationAngleAroundZAxisToFixPointInRadians(_planeNormal, _transformedInitialPoint1, _rotationAngles);
    double angle1InRadians = _rotationAngles.x();
    double angle2InRadians = _rotationAngles.y();
  
    // Selecting best angle to rotate (smallest)
    double dif1 = Angle.distanceBetweenAnglesInRadians(angle1InRadians, _lastDoubleDragAngle);
    double dif2 = Angle.distanceBetweenAnglesInRadians(angle2InRadians, _lastDoubleDragAngle);
    double finalAngle = (dif1 < dif2)? angle1InRadians : angle2InRadians;
    if (finalAngle != finalAngle)
    {
      _doubleDragMatrix.setInvalid();
      return 0;
    }
  
    //Creating general rotation matrix
    _lastDoubleDragAngle = finalAngle;
    _initialNormal0.set(_initialPoint0.x(), _initialPoint0.y(), _initialPoint0.z());
    _initialNormal0.normalize();
    _translationMatrix.setValid();
    _translationMatrix.setTranslationMatrix(_initialPoint0.x(), _initialPoint0.y(), _initialPoint0.z());
    _rotationMatrix.setValid();
    _rotationMatrix.setRotationMatrix(-_lastDoubleDragAngle, _initialNormal0);
    _generalRotationMatrix.setValid();
    _generalRotationMatrix.copyValueOfMultiplication(_translationMatrix, _rotationMatrix);
    _translationMatrix.setValid();
    _translationMatrix.setTranslationMatrix(-_initialPoint0.x(), -_initialPoint0.y(), -_initialPoint0.z());
    _generalRotationMatrix.copyValueOfMultiplication(_generalRotationMatrix, _translationMatrix);
    _doubleDragMatrix.copyValueOfMultiplication(_generalRotationMatrix, _doubleDragMatrix);
  
    // compute angle diference
    _P0.transformPointByMatrix(_origin, _doubleDragMatrix, 1.0);
    _B.set(_initialPoint1.x(), _initialPoint1.y(), _initialPoint1.z());
    _B0.set(_B.x()-_P0.x(), _B.y()-_P0.y(), _B.z()-_P0.z());
    _Ra.transformPointByMatrix(finalRay0, _doubleDragMatrix, 0.0);
    _Rb.transformPointByMatrix(finalRay1, _doubleDragMatrix, 0.0);
    double difAngleInRadians = MutableVector3D.angleInRadiansBetween(_Ra, _B0) - MutableVector3D.angleInRadiansBetween(_Ra, _Rb);
    return difAngleInRadians / 3.141592653589 * 180.0;
  }

  private void createInversedGeodeticTransformMatrix(MutableVector3D position, MutableMatrix44D result)
  {
    result.setValid();
    result.setTranslationMatrix(position.x(), position.y(), position.z());
    double mod = position.length();
    final IMathUtils mu = IMathUtils.instance();
    double latitudeInRadians = mu.asin(position.z()/mod);
    double longitudeInRadians = mu.atan2(position.y()/mod, position.x()/mod);
    _rotationMatrix.setValid();
    _rotationMatrix.setGeodeticRotationMatrix(latitudeInRadians, longitudeInRadians);
    result.copyValueOfMultiplication(result, _rotationMatrix);
    result.setInverse();
  }



  public SphericalPlanet(Sphere sphere)
  {
     _sphere = sphere;
     _radii = new Vector3D(new Vector3D(sphere._radius, sphere._radius, sphere._radius));
     _lastCorrectingRollAngle = 0;
     _prevFactor = 0;
  }

  public void dispose()
  {
  super.dispose();

  }

  public final Vector3D getRadii()
  {
    return _radii;
  }

  public final Vector3D centricSurfaceNormal(Vector3D position)
  {
    return position.normalized();
  }

  public final Vector3D geodeticSurfaceNormal(Vector3D position)
  {
    return position.normalized();
  }

  public final Vector3D geodeticSurfaceNormal(MutableVector3D position)
  {
    return position.normalized().asVector3D();
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
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
  
    // By laborious algebraic manipulation....
    final double a = directionX * directionX + directionY * directionY + directionZ * directionZ;
  
    final double b = 2.0 * (originX * directionX + originY * directionY + originZ * directionZ);
  
    final double c = originX * originX + originY * originY + originZ * originZ - _sphere._radiusSquared;
  
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
  
    final double rootDiscriminant = IMathUtils.instance().sqrt(discriminant);
    final double root1 = (-b + rootDiscriminant) / (2 *a);
    final double root2 = (-b - rootDiscriminant) / (2 *a);
  
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

  public final Vector3D toCartesian(Angle latitude, Angle longitude, double height)
  {
    return geodeticSurfaceNormal(latitude, longitude).times(_sphere._radius + height);
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
  
    final double K = _sphere._radius + height;
    result.set(nX * K, nY * K, nZ * K);
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

  public final Geodetic2D toGeodetic2D(Vector3D position)
  {
    final Vector3D n = geodeticSurfaceNormal(position);
  
    final IMathUtils mu = IMathUtils.instance();
    return new Geodetic2D(Angle.fromRadians(mu.asin(n._z)), Angle.fromRadians(mu.atan2(n._y, n._x)));
  }

  public final void toGeodetic2D(MutableVector3D position, MutableVector2D result)
  {
    double mod = position.length();
    double x = position.x() / mod;
    double y = position.y() / mod;
    double z = position.z() / mod;
    final IMathUtils mu = IMathUtils.instance();
    result.setValues(mu.asin(z), mu.atan2(y, x));
  }

  public final Geodetic3D toGeodetic3D(Vector3D position)
  {
    final Vector3D p = scaleToGeodeticSurface(position);
    final Vector3D h = position.sub(p);
  
    final double height = (h.dot(position) < 0) ? -1 * h.length() : h.length();
  
    return new Geodetic3D(toGeodetic2D(p), height);
  }

  public final void toGeodetic3D(MutableVector3D position, MutableVector3D result)
  {
    double px = position.x() * _sphere._radius;
    double py = position.y() * _sphere._radius;
    double pz = position.z() * _sphere._radius;
    double pmod = IMathUtils.instance().sqrt(px *px + py *py + pz *pz);
    px/=pmod;
    py/=pmod;
    pz/=pmod;
    double hx = position.x() - px;
    double hy = position.y() - py;
    double hz = position.z() - pz;
    double hmod = IMathUtils.instance().sqrt(hx *hx + hy *hy + hz *hz);
    double dot = hx *position.x() + hy *position.y() + hz *position.z();
    double height = (dot < 0) ? -1 * hmod : hmod;
    final IMathUtils mu = IMathUtils.instance();
    double mod = px *px + py *py + pz *pz;
    double latitudeInRadians = mu.asin(pz/mod);
    double longitudeInRadians = mu.atan2(py/mod, px/mod);
    result.set(latitudeInRadians, longitudeInRadians, height);
  }

  public final Vector3D scaleToGeodeticSurface(Vector3D position)
  {
    return geodeticSurfaceNormal(position).times(_sphere._radius);
  }

  public final Vector3D scaleToGeocentricSurface(Vector3D position)
  {
    return scaleToGeodeticSurface(position);
  }

  public final java.util.LinkedList<Vector3D> computeCurve(Vector3D start, Vector3D stop, double granularity)
  {
    if (granularity <= 0.0)
    {
      //throw new ArgumentOutOfRangeException("granularity", "Granularity must be greater than zero.");
      return new java.util.LinkedList<Vector3D>();
    }
  
    final Vector3D normal = start.cross(stop).normalized();
    final double theta = start.angleBetween(stop)._radians;
  
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
  
    final double R = _sphere._radius;
  
    // spheric distance from P to Q
    // this is the right form, but it's the most complex
    // theres is a minimum error considering SphericalPlanet instead of ellipsoid
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
  
    final double R = _sphere._radius;
  
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
    final double R0 = _sphere._radius;
  
    // compute the point in this ray that are to a distance R from the origin.
    final double U2 = ray.squaredLength();
    final double O2 = pos.squaredLength();
    final double OU = pos.dot(ray);
    final double a = U2;
    final double b = 2 * OU;
    final double c = O2 - R0 * R0;
    double rad = b * b - 4 * a * c;
  
    // if there is solution, the ray intersects the SphericalPlanet
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
    return Sphere.closestIntersectionCenteredSphereWithRay(pos, ray, _sphere._radius);
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
    _dragRadius = _sphere._radius + toGeodetic3D(touchedPosition)._height;
  
    _validSingleDrag = false;
  }

  public final MutableMatrix44D singleDrag(Vector3D finalRay)
  {
    // check if initialPoint is valid
    if (_initialPoint.isNan())
       return MutableMatrix44D.invalid();
  
    // compute final point
    final Vector3D origin = _origin.asVector3D();
    MutableVector3D finalPoint = Sphere.closestIntersectionCenteredSphereWithRay(origin, finalRay, _dragRadius).asMutableVector3D();
    if (finalPoint.isNan())
    {
      //printf ("--invalid final point in drag!!\n");
      //    finalPoint = closestPointToSphere(origin, finalRay).asMutableVector3D();
      finalPoint.copyFrom(closestPointToSphere(origin, finalRay));
      if (finalPoint.isNan())
      {
        ILogger.instance().logWarning("SphericalPlanet::singleDrag-> finalPoint is NaN");
        return MutableMatrix44D.invalid();
      }
    }
  
    return createDragMatrix(_initialPoint.asVector3D(), finalPoint.asVector3D());
  }

  public final Effect createEffectFromLastSingleDrag()
  {
    if (!_validSingleDrag || _lastDragAxis.isNan())
       return null;
    return new RotateWithAxisEffect(_lastDragAxis.asVector3D(), Angle.fromRadians(_lastDragRadiansStep));
  }

  public final void beginDoubleDrag(Vector3D origin, Vector3D centerRay, Vector3D centerPosition, Vector3D touchedPosition0, Vector3D touchedPosition1)
  {
    _origin = origin.asMutableVector3D();
    _centerRay = centerRay.normalized().asMutableVector3D();
    _initialPoint0 = touchedPosition0.asMutableVector3D();
  
    _dragRadius0 = _sphere._radius + toGeodetic3D(touchedPosition0)._height;
    _initialPoint1 = touchedPosition1.asMutableVector3D();
    _dragRadius1 = _sphere._radius + toGeodetic3D(touchedPosition1)._height;
    _centerPoint = centerPosition.asMutableVector3D();
    _lastDoubleDragAngle = 0;
    _lastCorrectingRollAngle = java.lang.Double.NaN;
    _lastCorrectingRollRotationAxis.set(java.lang.Double.NaN, java.lang.Double.NaN, java.lang.Double.NaN);
  
    _prevFactor = 0;
  }

  public final MutableMatrix44D doubleDrag(Vector3D finalRay0, Vector3D finalRay1)
  {
    // test if initialPoints are valid
    if (_initialPoint0.isNan() || _initialPoint1.isNan())
      return MutableMatrix44D.invalid();
  
    // search min & max factor for translation
    _iter = 0;
    double factor = 0;
    double minFactor;
    double maxFactor;
    double deltaFactor = 0.04;
    double difAngleInDegrees = testDoubleDragIteration(_prevFactor, finalRay0, finalRay1);
    if (difAngleInDegrees != difAngleInDegrees)
      return MutableMatrix44D.invalid();
    if (difAngleInDegrees < 0)
    {
      minFactor = _prevFactor;
      maxFactor = _prevFactor + deltaFactor;
      difAngleInDegrees = testDoubleDragIteration(maxFactor, finalRay0, finalRay1);
      if (difAngleInDegrees != difAngleInDegrees)
        return MutableMatrix44D.invalid();
      while (difAngleInDegrees < 0)
      {
        maxFactor += deltaFactor;
        difAngleInDegrees = testDoubleDragIteration(maxFactor, finalRay0, finalRay1);
        if (difAngleInDegrees != difAngleInDegrees)
          return MutableMatrix44D.invalid();
      }
    }
    else
    {
      minFactor = _prevFactor - deltaFactor;
      maxFactor = _prevFactor;
      difAngleInDegrees = testDoubleDragIteration(minFactor, finalRay0, finalRay1);
      if (difAngleInDegrees != difAngleInDegrees)
        return MutableMatrix44D.invalid();
      while (difAngleInDegrees > 0)
      {
        minFactor -= deltaFactor;
        difAngleInDegrees = testDoubleDragIteration(minFactor, finalRay0, finalRay1);
        if (difAngleInDegrees != difAngleInDegrees)
          return MutableMatrix44D.invalid();
      }
    }
  
    // iterations until find right translation distance
    while (maxFactor-minFactor > 1e-3)
    {
      factor = (minFactor + maxFactor) / 2;
      difAngleInDegrees = testDoubleDragIteration(factor, finalRay0, finalRay1);
      if (difAngleInDegrees != difAngleInDegrees)
      {
        return MutableMatrix44D.invalid();
      }
      //printf("difAngleInDegrees=%.4f.  Min=%.4f  Factor=%.4f  Max=%.4f)\n", difAngleInDegrees, minFactor, factor, maxFactor);
      if (difAngleInDegrees>0)
        maxFactor = factor;
      else
        minFactor = factor;
    }
  
    //printf("Converge doble drag %d iteraciones con factor=%.3f\n", _iter, factor);
  
    _prevFactor = factor;
    return _doubleDragMatrix;
  }


  /*
   MutableMatrix44D SphericalPlanet::doubleDrag(const Vector3D& finalRay0,
   const Vector3D& finalRay1,
   bool allowRotation) const
   {
   // test if initialPoints are valid
   if (_initialPoint0.isNan() || _initialPoint1.isNan())
   return MutableMatrix44D::invalid();
   
   // init params
   const IMathUtils* mu = IMathUtils::instance();
   const Vector3D origin = _origin.asVector3D();
   MutableVector3D positionCamera = _origin;
   
   // compute final points
   Vector3D finalPoint0 = Sphere::closestIntersectionCenteredSphereWithRay(origin,
   finalRay0, _dragRadius0); // A1
   if (finalPoint0.isNan()) return MutableMatrix44D::invalid();
   
   // drag initial point 0 to final point 0
   MutableMatrix44D matrix = createDragMatrix(_initialPoint0.asVector3D(), finalPoint0);
   
   // transform points to set axis origin in initialPoint0
   // (en el mundo plano es solo una traslacion)
   // (en el esférico será un cambio de sistema de referencia: traslacion + rotacion, usando el sistema local normal en ese punto)
   {
   Vector3D draggedCameraPos = positionCamera.transformedBy(matrix, 1.0).asVector3D();
   Vector3D finalPoint1 = Sphere::closestIntersectionCenteredSphereWithRay(draggedCameraPos,
   finalRay1.transformedBy(matrix, 0), _dragRadius1); // B1
   
   //Taking whole system to origin
   MutableMatrix44D M = createGeodeticTransformMatrix(toGeodetic3D(_initialPoint0.asVector3D()));
   MutableMatrix44D transform = M.inversed();
   
   Vector3D transformedInitialPoint1 = _initialPoint1.transformedBy(transform, 1.0).asVector3D();
   Vector3D transformedFinalPoint1 = finalPoint1.transformedBy(transform, 1.0);
   Vector3D transformedCameraPos = draggedCameraPos.transformedBy(transform, 1.0);
   Vector3D v0 = transformedFinalPoint1.sub(transformedCameraPos);
   
   //Angles to rotate transformedInitialPoint1 to adjust the plane that contains origin, TFP1 and TCP
   Vector3D planeNormal = transformedCameraPos.cross(v0).normalized();
   Plane plane(planeNormal, v0);
   Vector2D angles = plane.rotationAngleAroundZAxisToFixPointInRadians(transformedInitialPoint1);
   
   //Selecting best angle to rotate (smallest)
   double angulo1 = angles._x;
   double angulo2 = angles._y;
   double dif1 = Angle::distanceBetweenAnglesInRadians(angulo1, _lastDoubleDragAngle);
   double dif2 = Angle::distanceBetweenAnglesInRadians(angulo2, _lastDoubleDragAngle);
   _lastDoubleDragAngle = (dif1 < dif2)? angulo1 : angulo2;
   
   //Creating rotating matrix
   Vector3D normal0 = geodeticSurfaceNormal(_initialPoint0);
   MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromRadians(-_lastDoubleDragAngle),normal0, _initialPoint0.asVector3D());
   matrix.copyValueOfMultiplication(rotation, matrix); // = rotation.multiply(matrix);
   }
     
     
     {
       Vector3D P0   = positionCamera.transformedBy(matrix, 1.0).asVector3D();
       Vector3D B    = _initialPoint1.asVector3D();
       Vector3D B0   = B.sub(P0);
       Vector3D Ra   = finalRay0.transformedBy(matrix, 0.0).normalized();
       Vector3D Rb   = finalRay1.transformedBy(matrix, 0.0).normalized();
       printf("angulo buscado:%.4f. angulo final:%.4f,    ", Ra.angleBetween(Rb)._degrees, Ra.angleBetween(B0)._degrees);
     }
  
   // zoom camera (see chuleta en pdf)
   // ahora mismo lo que se hace es buscar cuánto acercar para que el angulo de las dos parejas de vectores
   // sea el mismo
   {
   Vector3D P0   = positionCamera.transformedBy(matrix, 1.0).asVector3D();
   Vector3D B    = _initialPoint1.asVector3D();
   Vector3D B0   = B.sub(P0);
   Vector3D Ra   = finalRay0.transformedBy(matrix, 0.0).normalized();
   Vector3D Rb   = finalRay1.transformedBy(matrix, 0.0).normalized();
   double b      = -2 * (B0.dot(Ra));
   double c      = B0.squaredLength();
   double k      = Ra.dot(B0);
   double RaRb2  = Ra.dot(Rb) * Ra.dot(Rb);
   double at     = RaRb2 - 1;
   double bt     = b*RaRb2 + 2*k;
   double ct     = c*RaRb2 - k*k;
   
   Vector2D sol = mu->solveSecondDegreeEquation(at, bt, ct);
   if (sol.isNan()){
   return MutableMatrix44D::invalid();
   }
   double t = sol._x;
   
   MutableMatrix44D zoom = MutableMatrix44D::createTranslationMatrix(Ra.times(t));
   matrix.copyValueOfMultiplication(zoom, matrix); // = zoom.multiply(matrix);
   }
     
     
     {
       Vector3D P0   = positionCamera.transformedBy(matrix, 1.0).asVector3D();
       Vector3D B    = _initialPoint1.asVector3D();
       Vector3D B0   = B.sub(P0);
       Vector3D Ra   = finalRay0.transformedBy(matrix, 0.0).normalized();
       Vector3D Rb   = finalRay1.transformedBy(matrix, 0.0).normalized();
       printf("angulo final:%.4f\n", Ra.angleBetween(B0)._degrees);
     }
  
   
   return matrix;
   }
  */
  
  
  
  public final Effect createDoubleTapEffect(Vector3D origin, Vector3D centerRay, Vector3D touchedPosition)
  {
    //const Vector3D initialPoint = closestIntersection(origin, tapRay);
    if (touchedPosition.isNan())
       return null;
  
    // compute central point of view
    //const Vector3D centerPoint = closestIntersection(origin, centerRay);
    double touchedHeight = toGeodetic3D(touchedPosition)._height;
    double dragRadius = _sphere._radius + touchedHeight;
    final Vector3D centerPoint = Sphere.closestIntersectionCenteredSphereWithRay(origin, centerRay, dragRadius);
  
    // compute drag parameters
    final IMathUtils mu = IMathUtils.instance();
    final Vector3D axis = touchedPosition.cross(centerPoint);
    final Angle angle = Angle.fromRadians(- mu.asin(axis.length()/touchedPosition.length()/centerPoint.length()));
  
    // compute zoom factor
    final double distanceToGround = toGeodetic3D(origin)._height - touchedHeight;
    final double distance = distanceToGround * 0.6;
  
    // create effect
    return new DoubleTapRotationEffect(TimeInterval.fromSeconds(0.75), axis, angle, distance);
  }

  public final double distanceToHorizon(Vector3D position)
  {
    double R = _sphere._radius;
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
  
    //  Vector3D pos = nextCamera->getCartesianPosition();
    //  Vector3D origin = _origin.asVector3D();
    //  double maxDist = _sphere.getRadius() * 5;
    //
    //  if (pos.distanceTo(origin) > maxDist) {
    //    nextCamera->copyFromForcingMatrixCreation(*previousCamera);
    ////    Vector3D pos2 = nextCamera->getCartesianPosition();
    ////    printf("TOO FAR %f -> pos2: %f\n", pos.distanceTo(origin) / maxDist, pos2.distanceTo(origin) / maxDist);
    //  }
  
  }

  public final Geodetic3D getDefaultCameraPosition(Sector rendereSector)
  {
    final Vector3D asw = toCartesian(rendereSector.getSW());
    final Vector3D ane = toCartesian(rendereSector.getNE());
    final double height = asw.sub(ane).length() * 1.9;

    return new Geodetic3D(rendereSector._center, height);
  }

  public final void correctPitchAfterDoubleDrag(Camera camera, Vector2F finalPixel0, Vector2F finalPixel1)
  {
  
    Vector3D finalPoint0 = camera.pixel2PlanetPoint(finalPixel0);
    Vector3D finalPoint1 = camera.pixel2PlanetPoint(finalPixel1);
    if (finalPoint0.isNan() || finalPoint1.isNan())
    {
      return;
    }
  
    //  printf("dist = %f\n", finalPoint0.distanceTo(finalPoint1));
  
    Vector3D axis = finalPoint0.sub(finalPoint1);
  
  
    //Avoiding big jumps
    boolean axisCorrect = true;
    boolean angleCorrect = true;
    if (_lastCorrectingRollRotationAxis.isNan())
    {
      _lastCorrectingRollRotationAxis.copyFrom(axis);
    }
    else
    {
      double axisDirectionJump = axis.angleBetween(_lastCorrectingRollRotationAxis.asVector3D())._degrees;
      axisCorrect = (axisDirectionJump < 5.0);
    }
  
    if (axisCorrect)
    {
  
      //Taking axis to camera coordinate system
      MutableMatrix44D csm = new MutableMatrix44D(camera.getModelMatrix44D());
      Vector3D axisCS = axis.transformedBy(csm, 0.0).normalized(); //ROTATION AXIS
      Vector3D rotationPointCS = finalPoint0.transformedBy(csm, 1.0); //ROTATION POINT
      Vector3D planetCenterCS = Vector3D.zero.transformedBy(csm, 1.0); //Point to be dragged
  
      //The angle should take the planet center to the center of the view (Plane ZY) -> X = 0
  
      double angleInRadians = _lastCorrectingRollAngle;
  
      java.util.ArrayList<Double> angs = planetCenterCS.rotationAngleInRadiansToYZPlane(axisCS, rotationPointCS);
      if (angs.size() > 0)
      {
  
        Angle a0 = Angle.fromRadians(angs.get(0));
        Angle a1 = Angle.fromRadians(angs.get(1));
        Angle last = Angle.fromRadians(_lastCorrectingRollAngle);
  
        //angleInRadians = a0.distanceTo(last)._radians < a1.distanceTo(last)._radians? a0._radians : a1._radians;
  
        if (a0.distanceTo(last)._radians < a1.distanceTo(last)._radians)
          angleInRadians = a0._radians;
        else
          angleInRadians = a1._radians;
  
        angleInRadians *= -1; //Inverting for camera
  
        if ((_lastCorrectingRollAngle != _lastCorrectingRollAngle))
        {
          _lastCorrectingRollAngle = angleInRadians;
        }
        else
        {
          double jump = Angle.fromRadians(angleInRadians).distanceTo(Angle.fromRadians(_lastCorrectingRollAngle))._degrees;
          angleCorrect = (jump < 20);
          //      if (jump > 20){
          //        printf("CORRECTED ROLL JUMPED %f DEGREES\n", jump);
          //        angleInRadians = _lastCorrectingRollAngle;
          //      }
        }
  
  
  
      }
      else
      {
        System.out.print("NONE CORRECT ROLL ANGLE FOR THIS FRAME\n");
      }
  
      if (angleCorrect) //In angle and axis haven't change much
      {
        Angle angle = Angle.fromRadians(angleInRadians);
  
        //printf("CORRECTING ROLL %f GRAD\n", angle->_degrees);
        MutableMatrix44D m = MutableMatrix44D.createGeneralRotationMatrix(angle, axis, finalPoint0);
        camera.applyTransform(m);
  
        //Storing for next frame
        _lastCorrectingRollAngle = angle._radians;
        _lastCorrectingRollRotationAxis.copyFrom(axis);
      }
      else
      {
        System.out.print("ROLL ANGLE JUMPED\n");
      }
  
    }
    else
    {
      System.out.print("ROLL AXIS JUMPED\n");
    }
  }

  public final String getType()
  {
    return "Spherical";
  }
}