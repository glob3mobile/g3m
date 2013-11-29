//
//  RasterPolygonShape.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 28/11/13.
//
//

#include "RasterPolygonShape.hpp"
#include "OrientedBox.hpp"
#include "Camera.hpp"
#include "GeoRasterPolygonSymbol.hpp"
#include "Geo2DPolygonData.hpp"


RasterPolygonShape::~RasterPolygonShape() {
  delete _borderColor;
  delete _surfaceColor;
  if (_boundingVolume)
    delete _boundingVolume;
  if (_cartesianStartPos)
    delete _cartesianStartPos;
  
  for (int i = 0; i < _coordinates->size(); i++) {
    const Geodetic2D* coordinate = _coordinates->at(i);
    delete coordinate;
  }
  delete _coordinates;

  
#ifdef JAVA_CODE
  super.dispose();
#endif
  
}


std::vector<double> RasterPolygonShape::intersectionsDistances(const Planet* planet,
                                                               const Camera* camera,
                                                               const Vector3D& origin,
                                                               const Vector3D& direction) {
  if (_boundingVolume != NULL)
    delete _boundingVolume;
  _boundingVolume = computeOrientedBox(planet, camera);
  return _boundingVolume->intersectionsDistances(origin, direction);
}


bool RasterPolygonShape::isVisible(const G3MRenderContext *rc)
{
  return true;
  //return getBoundingVolume(rc)->touchesFrustum(rc->getCurrentCamera()->getFrustumInModelCoordinates());
}


OrientedBox* RasterPolygonShape::computeOrientedBox(const Planet* planet,
                                                 const Camera* camera)
{
  if (_cartesianStartPos == NULL)
    computeOrientationParams(planet);
  double distanceToCamera = camera->getCartesianPosition().distanceTo(*_cartesianStartPos);
  FrustumData frustum = camera->getFrustumData();
  const int pixelWidth = 10;
  double scale = 2 * pixelWidth * distanceToCamera * frustum._top / camera->getHeight() / frustum._znear;
  double incZ = scale - (_maxZ - _minZ);
  if (incZ < 0) incZ = 0;
  const Vector3D upper = Vector3D(_maxX, _maxY, _maxZ+incZ);
  const Vector3D lower = Vector3D(_minX, _minY, _minZ-incZ);
  return new OrientedBox(lower, upper, *getTransformMatrix(planet));
}


void RasterPolygonShape::computeOrientationParams(const Planet* planet)
{
  _minX = _minY = _minZ = 0;
  _maxX = _maxY = _maxZ = 0;
  Geodetic2D* geodeticStartPos = _coordinates->at(0);
  _cartesianStartPos = new Vector3D(planet->toCartesian(*geodeticStartPos));
  const Vector3D normal = planet->geodeticSurfaceNormal(*_cartesianStartPos);
  Plane Z0 = Plane::fromPointAndNormal(*_cartesianStartPos, normal);
  const Vector3D north2D = planet->getNorth().projectionInPlane(normal);
  Plane Y0 = Plane::fromPointAndNormal(*_cartesianStartPos, north2D);
  const Vector3D east2D = north2D.cross(normal);
  Plane X0 = Plane::fromPointAndNormal(*_cartesianStartPos, east2D);
  for (int n=1; n<_coordinates->size(); n++) {
    Geodetic2D* geodeticVertex = _coordinates->at(n);
    Vector3D vertex = planet->toCartesian(*geodeticVertex);
    double x = X0.signedDistance(vertex);
    if (x < _minX) _minX = x;
    if (x > _maxX) _maxX = x;
    double y = Y0.signedDistance(vertex);
    if (y < _minY) _minY = y;
    if (y > _maxY) _maxY = y;
    double z = Z0.signedDistance(vertex);
    if (z < _minZ) _minZ = z;
    if (z > _maxZ) _maxZ = z;
  }
}


BoundingVolume* RasterPolygonShape::getBoundingVolume(const G3MRenderContext *rc)
{
  if (_boundingVolume != NULL)
    delete _boundingVolume;
  _boundingVolume = computeOrientedBox(rc->getPlanet(), rc->getCurrentCamera());
  return _boundingVolume;
}


GEORasterSymbol* RasterPolygonShape::createRasterSymbolIfNeeded() const
{
  float dashLengths[] = {};
  std::vector<Geodetic2D*>* coordinates = GEORasterSymbol::copyCoordinates(_coordinates);
  GEO2DPolygonData polygonData(coordinates, NULL);
  GEO2DLineRasterStyle lineStyle(*_borderColor, _borderWidth, CAP_ROUND, JOIN_ROUND, 1, dashLengths, 0, 0);
  GEO2DSurfaceRasterStyle surfaceStyle(*_surfaceColor);
  return new GEORasterPolygonSymbol(&polygonData, lineStyle, surfaceStyle);
}


