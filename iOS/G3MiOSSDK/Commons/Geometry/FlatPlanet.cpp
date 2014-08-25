//
//  FlatPlanet.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

#include "FlatPlanet.hpp"
#include "Plane.hpp"
#include "CameraEffects.hpp"
#include "Camera.hpp"



FlatPlanet::FlatPlanet(const Vector2D& size):
_size(size)
{

}


Vector3D FlatPlanet::geodeticSurfaceNormal(const Angle& latitude,
                                           const Angle& longitude) const {
  return Vector3D(0, 0, 1);
}


std::vector<double> FlatPlanet::intersectionsDistances(const Vector3D& origin,
                                                       const Vector3D& direction) const {
  std::vector<double> intersections;

  // compute intersection with plane
  if (direction._z == 0) return intersections;
  const double t = -origin._z / direction._z;
  const double x = origin._x + t * direction._x;
  const double halfWidth = 0.5 * _size._x;
  if (x < -halfWidth || x > halfWidth) return intersections;
  const double y = origin._y + t * direction._y;
  const double halfHeight = 0.5 * _size._y;
  if (y < -halfHeight || y > halfHeight) return intersections;
  intersections.push_back(t);
  return intersections;
}


Vector3D FlatPlanet::toCartesian(const Angle& latitude,
                                 const Angle& longitude,
                                 const double height) const {
  return toCartesian(Geodetic3D(latitude, longitude, height));
}


Geodetic2D FlatPlanet::toGeodetic2D(const Vector3D& position) const {
  const double longitude = position._x * 360.0 / _size._x;
  const double latitude = position._y * 180.0 / _size._y;
  return (Geodetic2D(Angle::fromDegrees(latitude), Angle::fromDegrees(longitude)));
}


Geodetic3D FlatPlanet::toGeodetic3D(const Vector3D& position) const {
  return Geodetic3D(toGeodetic2D(position), position._z);
}


Vector3D FlatPlanet::scaleToGeodeticSurface(const Vector3D& position) const {
  return Vector3D(position._x, position._y, 0);
}


Vector3D FlatPlanet::scaleToGeocentricSurface(const Vector3D& position) const {
  return scaleToGeodeticSurface(position);
}


Geodetic2D FlatPlanet::getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const
{
  return Geodetic2D(P0._latitude.add(P1._latitude).times(0.5), P0._longitude.add(P1._longitude).times(0.5));
}


// compute distance from two points
double FlatPlanet::computePreciseLatLonDistance(const Geodetic2D& g1,
                                                const Geodetic2D& g2) const {
  return toCartesian(g1).sub(toCartesian(g2)).length();
}


// compute distance from two points
double FlatPlanet::computeFastLatLonDistance(const Geodetic2D& g1,
                                             const Geodetic2D& g2) const {
  return computePreciseLatLonDistance(g1, g2);
}


Vector3D FlatPlanet::closestIntersection(const Vector3D& pos,
                                         const Vector3D& ray) const {
  std::vector<double> distances = intersectionsDistances(pos , ray);
  if (distances.empty()) {
    return Vector3D::nan();
  }
  return pos.add(ray.times(distances[0]));
}


MutableMatrix44D FlatPlanet::createGeodeticTransformMatrix(const Geodetic3D& position) const {
  const MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix( toCartesian(position) );
  return translation;

  //const MutableMatrix44D rotation    = MutableMatrix44D::createGeodeticRotationMatrix( position );
  //return translation.multiply(rotation);
}


void FlatPlanet::beginSingleDrag(const Vector3D& origin, const Vector3D& touchedPosition) const
{
  _origin = origin.asMutableVector3D();
  //_initialPoint = Plane::intersectionXYPlaneWithRay(origin, initialRay).asMutableVector3D();
  _initialPoint = touchedPosition.asMutableVector3D();
  _dragHeight = toGeodetic3D(touchedPosition)._height;

  //printf("INiTIAL POINT EN %f, %f, %f\n ", _initialPoint.x(), _initialPoint.y(), _initialPoint.z());

  _lastFinalPoint = _initialPoint;
  _validSingleDrag = false;
}


MutableMatrix44D FlatPlanet::singleDrag(const Vector3D& finalRay) const
{
  // test if initialPoint is valid
  if (_initialPoint.isNan()) return MutableMatrix44D::invalid();

  // compute final point
  const Vector3D origin = _origin.asVector3D();
  MutableVector3D finalPoint = Plane::intersectionXYPlaneWithRay(origin, finalRay, _dragHeight).asMutableVector3D();
  if (finalPoint.isNan()) return MutableMatrix44D::invalid();

  // save params for possible inertial animations
  _validSingleDrag = true;
  _lastDirection = _lastFinalPoint.sub(finalPoint);
  _lastFinalPoint = finalPoint;

  // return rotation matrix
  return MutableMatrix44D::createTranslationMatrix(_initialPoint.sub(finalPoint).asVector3D());
}


Effect* FlatPlanet::createEffectFromLastSingleDrag() const
{
  if (!_validSingleDrag) return NULL;
  return new SingleTranslationEffect(_lastDirection.asVector3D());
}


void FlatPlanet::beginDoubleDrag(const Vector3D& origin,
                                 const Vector3D& centerRay,
                                 const Vector3D& centerPosition,
                                 const Vector3D& touchedPosition0,
                                 const Vector3D& touchedPosition1) const
{
  _origin = origin.asMutableVector3D();
  _centerRay = centerRay.asMutableVector3D();
  //_initialPoint0 = Plane::intersectionXYPlaneWithRay(origin, initialRay0).asMutableVector3D();
  //_initialPoint1 = Plane::intersectionXYPlaneWithRay(origin, initialRay1).asMutableVector3D();
  _initialPoint0 = touchedPosition0.asMutableVector3D();
  _dragHeight0 = toGeodetic3D(touchedPosition0)._height;
  _initialPoint1 = touchedPosition1.asMutableVector3D();
  _dragHeight1 = toGeodetic3D(touchedPosition1)._height;
  _distanceBetweenInitialPoints = _initialPoint0.sub(_initialPoint1).length();
  //_centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay).asMutableVector3D();
  _centerPoint = centerPosition.asMutableVector3D();
  //  _angleBetweenInitialRays = initialRay0.angleBetween(initialRay1).degrees();

  // middle point in 3D
  _initialPoint = _initialPoint0.add(_initialPoint1).times(0.5);
}


/* ********************
 TODO ESTA FUNCION ES PARA ORDENARLA, DOCUMENTARLA EN CHULETA PARA WEB, EXPLICARLA A JM
 Y QUE LA PASE AL PAPER GESTOS DESPUÉS DEL VERANO.
 Y TODAVIA FALTARA PROBARLO EN PLANETAS ESFERICOS
 AQUI DEBE SER PRACTICAMENTE IGUAL EXCEPTO LA MATRIZ DEL PASO 2 PARA PONER EL ORIGEN DE COORDENADAS
 SOBRE EL DRAGGED INITIAL POINT, QUE AHORA SOLO ES TRASLACION, PERO LUEGO SERA UNA MATRIZ
 DE CAMBIO DE SISTEMA
 *****/
MutableMatrix44D FlatPlanet::doubleDrag(const Vector3D& finalRay0,
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
  Vector3D finalPoint0 = Plane::intersectionXYPlaneWithRay(origin, finalRay0, _dragHeight0);
  if (finalPoint0.isNan()) return MutableMatrix44D::invalid();

  // drag initial point 0 to final point 0
  MutableMatrix44D matrix = MutableMatrix44D::createTranslationMatrix(_initialPoint0.sub(finalPoint0));
  
  // transform points to set axis origin in initialPoint0
  // (en el mundo plano es solo una traslacion)
  // (en el esférico será un cambio de sistema de referencia: traslacion + rotacion, usando el sistema local normal en ese punto)
  {
    Vector3D draggedCameraPos = positionCamera.transformedBy(matrix, 1.0).asVector3D();
    Vector3D finalPoint1 = Plane::intersectionXYPlaneWithRay(draggedCameraPos, finalRay1.transformedBy(matrix,0), _dragHeight1);
    MutableMatrix44D traslation = MutableMatrix44D::createTranslationMatrix(_initialPoint0.times(-1).asVector3D());
    Vector3D transformedInitialPoint1 = _initialPoint1.transformedBy(traslation, 1.0).asVector3D();
    Vector3D transformedFinalPoint1 = finalPoint1.transformedBy(traslation, 1.0);
    Vector3D transformedCameraPos = draggedCameraPos.transformedBy(traslation, 1.0);
    Vector3D v0 = transformedFinalPoint1.sub(transformedCameraPos);
    Vector3D v1 = transformedCameraPos.times(-1);
    Vector3D planeNormal = v0.cross(v1).normalized();
    double a = planeNormal._x;
    double b = planeNormal._y;
    double c = planeNormal._z;
    double xb = transformedInitialPoint1._x;
    double yb = transformedInitialPoint1._y;
    double zb = transformedInitialPoint1._z;
    double A = a*xb + b*yb;
    double B = b*xb - a*yb;
    double C = c*zb;
    double ap = A*A + B*B;
    double bp = 2*B*C;
    double cp = C*C - A*A;
    double root = bp*bp - 4*ap*cp;
    if (root<0) return MutableMatrix44D::invalid();
    double squareRoot = mu->sqrt(root);
    double sinTita1 = (-bp + squareRoot) / (2*ap);
    double sinTita2 = (-bp - squareRoot) / (2*ap);
    double cosTita1 = sqrt(1-sinTita1*sinTita1);
    double cosTita2 = sqrt(1-sinTita2*sinTita2);
    double eq1 = A*cosTita1 + B*sinTita1+C;
    double eq2 = -A*cosTita1 + B*sinTita1+C;
    double eq3 = A*cosTita2 + B*sinTita2+C;
    double eq4 = -A*cosTita2 + B*sinTita2+C;
    
    // estimamos el angulo entre dedos para decidir cual de las 4 soluciones trigonométricas escoger
    // de la pareja eq1,eq2, una de ellas no verifica la ecuación del plano y por tanto no es solución
    // idem con la pareja eq3,eq4
    double fingerAngle;
    {
    Vector3D finalPoint1 = Plane::intersectionXYPlaneWithRay(origin, finalRay1, _dragHeight1);
    Vector3D draggedCenterRay = _centerRay.asVector3D().transformedBy(matrix, 0.0);
    Vector3D projectedV0 = finalPoint1.sub(finalPoint0).projectionInPlane(draggedCenterRay);
    Vector3D projectedV1 = _initialPoint1.sub(_initialPoint0.asVector3D()).projectionInPlane(draggedCenterRay);
    fingerAngle = projectedV0.angleBetween(projectedV1)._degrees;
    double sign = projectedV0.cross(projectedV1).dot(draggedCenterRay);
    if (sign<0) fingerAngle = -fingerAngle;
    }
    
    printf ("cosTita1=%f cosTita2=%f    sinTita1=%f sinTita2=%f    eq1=%f eq2=%f eq3=%f eq4=%f\n",
            cosTita1, cosTita2, sinTita1, sinTita2, eq1, eq2, eq3, eq4);
    double angulo, angulo1, angulo2;
    if (mu->abs(eq1)<mu->abs(eq2))
      angulo1 = atan2(sinTita1, cosTita1);
    else
      angulo1 = atan2(sinTita1, -cosTita1);
    if (mu->abs(eq3)<mu->abs(eq4))
      angulo2 = atan2(sinTita2, cosTita2);
    else
      angulo2 = atan2(sinTita2, -cosTita2);
    
    if (fingerAngle > 45)
      angulo = angulo1;
    else if (fingerAngle < -45)
      angulo = angulo2;
    else
      angulo = (mu->abs(eq1)<mu->abs(eq2))? angulo1 : angulo2;
    
    
    double halfPi = 3.14159/2;
    double difAngles = mu->abs(angulo1+angulo2);
    /*
    if (difAngles > halfPi)
      angulo = (mu->abs(eq1)<mu->abs(eq2))? angulo1 : angulo2;
    else
      angulo = (mu->abs(eq1)<mu->abs(eq2))? angulo2 : angulo1;*/
    
   /* if (mu->abs(eq1)<mu->abs(eq2))
      angulo = angulo1;
      //angulo = (mu->abs(angulo1)<3.14159/4)? angulo1 : angulo2;
    else
      angulo = angulo2;
      //angulo = (mu->abs(angulo2)<3.14159/4)? angulo2 : angulo1;*/
    
    printf ("    angulo1=%.2f  angulo2=%.2f  ANGULO FINAL = %.2f   fingersAngle=%.2f, difAngles=%.2f\n",
            angulo1/3.14159*180, angulo2/3.14159*180, angulo/3.14159*180, fingerAngle, difAngles/3.14159*180);
    
    Vector3D normal0 = geodeticSurfaceNormal(_initialPoint0);
    MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromRadians(-angulo), normal0, _initialPoint0.asVector3D());
    matrix = rotation.multiply(matrix);
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
    double root   = bt*bt - 4*at*ct;
    if (root<0) return MutableMatrix44D::invalid();
    double squareRoot = mu->sqrt(root);
    double t = (-bt + squareRoot) / (2*at);
    MutableMatrix44D zoom = MutableMatrix44D::createTranslationMatrix(Ra.times(t));
    matrix = zoom.multiply(matrix);
  }
  
  return matrix;
}


/*
MutableMatrix44D FlatPlanet::doubleDrag(const Vector3D& finalRay0,
                                        const Vector3D& finalRay1,
                                        bool allowRotation) const
{
  // test if initialPoints are valid
  if (_initialPoint0.isNan() || _initialPoint1.isNan())
    return MutableMatrix44D::invalid();
  
  // init params
  const IMathUtils* mu = IMathUtils::instance();
  MutableVector3D positionCamera = _origin;
  
  // compute distance to translate camera
  double d0 = _distanceBetweenInitialPoints;
  const Vector3D r1 = finalRay0;
  const Vector3D r2 = finalRay1;
  double zA = _dragHeight0;
  double zB = _dragHeight1;
  double zc = _origin.z();
  double uz = _centerRay.z();
  double Rx = r2._x / r2._z - r1._x / r1._z;
  double Ry = r2._y / r2._z - r1._y / r1._z;
  double Kx = zc*Rx + zA*r1._x/r1._z - zB*r2._x/r2._z;
  double Ky = zc*Ry + zA*r1._y/r1._z - zB*r2._y/r2._z;
  double a = uz*uz * (Rx*Rx + Ry*Ry);
  double b = 2 * uz * (Rx*Kx + Ry*Ky);
  double c = Kx*Kx + Ky*Ky + (zA-zB)*(zA-zB) - d0*d0;
  double root = b*b - 4*a*c;
  if (root<0) return MutableMatrix44D::invalid();
  double squareRoot = mu->sqrt(root);
  double t2 = (-b - squareRoot) / (2*a);
  
  // the first time, t2 must be corrected
  if (_firstDoubleDragMovement) {
    _firstDoubleDragMovement = false;
    _correctionT2 = t2;
    t2 = 0;
  } else {
    t2 -= _correctionT2;
  }
  
  // start to compound matrix
  MutableMatrix44D matrix = MutableMatrix44D::identity();
  positionCamera = _origin;
  MutableVector3D viewDirection = _centerRay;
  MutableVector3D ray0 = finalRay0.asMutableVector3D();
  MutableVector3D ray1 = finalRay1.asMutableVector3D();
  
  MutableVector3D delta = viewDirection.times(t2);
  MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(delta.asVector3D());
  positionCamera = positionCamera.transformedBy(translation, 1.0);
  matrix = translation.multiply(matrix);
  
  // compute final point
  MutableVector3D finalDragPoint = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), finalRay0, _dragHeight0).asMutableVector3D();
  if (finalDragPoint.isNan()) return MutableMatrix44D::invalid();
  MutableMatrix44D translationDrag = MutableMatrix44D::createTranslationMatrix(_initialPoint0.sub(finalDragPoint).asVector3D());
  matrix = translationDrag.multiply(matrix);
  
  return matrix;
}*/

MutableMatrix44D FlatPlanet::doubleDrag_old(const Vector3D& finalRay0,
                                        const Vector3D& finalRay1,
                                        bool allowRotation) const
{
  // test if initialPoints are valid
  if (_initialPoint0.isNan() || _initialPoint1.isNan())
    return MutableMatrix44D::invalid();
  
  // init params
  const IMathUtils* mu = IMathUtils::instance();
  MutableVector3D positionCamera = _origin;
  
  // following math in http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/DoubleDrag.pdf
  /*
   // compute distance to translate camera
   double d0 = _distanceBetweenInitialPoints;
   const Vector3D r1 = finalRay0;
   const Vector3D r2 = finalRay1;
   double k = ((r1._x/r1._z - r2._x/r2._z) * (r1._x/r1._z - r2._x/r2._z) +
   (r1._y/r1._z - r2._y/r2._z) * (r1._y/r1._z - r2._y/r2._z));
   double zc = _origin.z();
   double uz = _centerRay.z();
   double t2 = (d0 / mu->sqrt(k) - zc) / uz;
   */
  
  // compute distance to translate camera
  double d0 = _distanceBetweenInitialPoints;
  const Vector3D r1 = finalRay0;
  const Vector3D r2 = finalRay1;
  double zA = _dragHeight0;
  double zB = _dragHeight1;
  double zc = _origin.z();
  double uz = _centerRay.z();
  double Rx = r2._x / r2._z - r1._x / r1._z;
  double Ry = r2._y / r2._z - r1._y / r1._z;
  double Kx = zc*Rx + zA*r1._x/r1._z - zB*r2._x/r2._z;
  double Ky = zc*Ry + zA*r1._y/r1._z - zB*r2._y/r2._z;
  double a = uz*uz * (Rx*Rx + Ry*Ry);
  double b = 2 * uz * (Rx*Kx + Ry*Ky);
  double c = Kx*Kx + Ky*Ky + (zA-zB)*(zA-zB) - d0*d0;
  double root = b*b - 4*a*c;
  if (root<0) return MutableMatrix44D::invalid();
  double squareRoot = mu->sqrt(root);
  double t2 = (-b - squareRoot) / (2*a);
    
  // start to compound matrix
  MutableMatrix44D matrix = MutableMatrix44D::identity();
  positionCamera = _origin;
  MutableVector3D viewDirection = _centerRay;
  MutableVector3D ray0 = finalRay0.asMutableVector3D();
  MutableVector3D ray1 = finalRay1.asMutableVector3D();
  
  // drag from initialPoint to centerPoint and move the camera forward
  {
    MutableVector3D delta = _initialPoint.sub((_centerPoint));
    delta = delta.add(viewDirection.times(t2));
    MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(delta.asVector3D());
    positionCamera = positionCamera.transformedBy(translation, 1.0);
    matrix = translation.multiply(matrix);
  }
  
  // compute middle point in 3D
  Vector3D P0 = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray0.asVector3D(), _dragHeight0);
  Vector3D P1 = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray1.asVector3D(), _dragHeight1);
  Vector3D finalPoint = P0.add(P1).times(0.5);
  
  // compute the corrected center point
  if (t2==0) {
    MutableVector3D delta = _initialPoint.sub((_centerPoint));
    _correctedCenterPoint = finalPoint.asMutableVector3D().sub(delta);
  }
  Vector3D correctedCenterPoint = _correctedCenterPoint.asVector3D();
  
  // drag globe from centerPoint to finalPoint
  {
    MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(correctedCenterPoint.sub(finalPoint));
    positionCamera = positionCamera.transformedBy(translation, 1.0);
    matrix = translation.multiply(matrix);
  }
  
  // camera rotation
  if (allowRotation) {
    Vector3D normal = geodeticSurfaceNormal(correctedCenterPoint);
    Vector3D v0     = _initialPoint0.asVector3D().sub(correctedCenterPoint).projectionInPlane(normal);
    Vector3D p0     = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray0.asVector3D(), _dragHeight0);
    Vector3D v1     = p0.sub(correctedCenterPoint).projectionInPlane(normal);
    double angle    = v0.angleBetween(v1)._degrees;
    double sign     = v1.cross(v0).dot(normal);
    if (sign<0) angle = -angle;
    MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle), normal, correctedCenterPoint);
    matrix = rotation.multiply(matrix);
  }
  
  return matrix;
}


Effect* FlatPlanet::createDoubleTapEffect(const Vector3D& origin,
                                          const Vector3D& centerRay,
                                          const Vector3D& touchedPosition) const
{
  //const Vector3D initialPoint = Plane::intersectionXYPlaneWithRay(origin, tapRay);
  if (touchedPosition.isNan()) return NULL;
  //const Vector3D centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay);
  double dragHeight = toGeodetic3D(touchedPosition)._height;
  const Vector3D centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay, dragHeight);

  // create effect
  double distanceToGround = toGeodetic3D(origin)._height - dragHeight;
  
  //printf("\n-- double tap to height %.2f, desde mi altura=%.2f\n", dragHeight, toGeodetic3D(origin)._height);
  
  return new DoubleTapTranslationEffect(TimeInterval::fromSeconds(0.75),
                                        touchedPosition.sub(centerPoint),
                                        distanceToGround*0.6);
}


double FlatPlanet::distanceToHorizon(const Vector3D& position) const
{
  double xCorner = 0.5 * _size._x;
  if (position._x > 0) xCorner *= -1;
  double yCorner = 0.5 * _size._y;
  if (position._y > 0) yCorner *= -1;
  const Vector3D fartherCorner(xCorner, yCorner, 0.0);
  return position.sub(fartherCorner).length();
}


MutableMatrix44D FlatPlanet::drag(const Geodetic3D& origin, const Geodetic3D& destination) const
{
  const Vector3D P0 = toCartesian(origin);
  const Vector3D P1 = toCartesian(destination);
  return MutableMatrix44D::createTranslationMatrix(P1.sub(P0));
}

void FlatPlanet::applyCameraConstrainers(const Camera* previousCamera,
                                         Camera* nextCamera) const{

//  Vector3D pos = nextCamera->getCartesianPosition();
//  Vector3D origin = _origin.asVector3D();
//  double maxDist = _size.length() * 1.5;
//
//  if (pos.distanceTo(origin) > maxDist) {
//    //    printf("TOO FAR %f\n", pos.distanceTo(origin) / maxDist);
//    nextCamera->copyFrom(*previousCamera);
//  }


}


