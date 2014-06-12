//
//  RasterLineShape.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 27/11/13.
//
//

#include "RasterLineShape.hpp"
#include "OrientedBox.hpp"
#include "Camera.hpp"
#include "GEO2DLineRasterStyle.hpp"
//#include "GEORasterLineSymbol.hpp"
#include "GEOLineRasterSymbol.hpp"


RasterLineShape::~RasterLineShape() {
  delete _color;
//  delete _originalColor;
  if (_boundingVolume)
    delete _boundingVolume;
  delete _geodeticStartPos;
  delete _geodeticEndPos;
  if (_cartesianStartPos)
    delete _cartesianStartPos;
  
#ifdef JAVA_CODE
  super.dispose();
#endif
  
}


std::vector<double> RasterLineShape::intersectionsDistances(const Planet* planet,
                                                            const Camera* camera,
                                                            const Vector3D& origin,
                                                            const Vector3D& direction) {
  if (_boundingVolume != NULL)
    delete _boundingVolume;
  _boundingVolume = computeOrientedBox(planet, camera);
  return _boundingVolume->intersectionsDistances(origin, direction);
}


bool RasterLineShape::isVisible(const G3MRenderContext *rc)
{
  return true;
  //return getBoundingVolume(rc)->touchesFrustum(rc->getCurrentCamera()->getFrustumInModelCoordinates());
}


OrientedBox* RasterLineShape::computeOrientedBox(const Planet* planet,
                                                 const Camera* camera)
{
  if (_cartesianStartPos == NULL)
    computeOrientationParams(planet);
  double distanceToCamera = camera->getCartesianPosition().distanceTo(*_cartesianStartPos);
  FrustumData frustum = camera->getFrustumData();
  const int pixelWidth = 10;
  double scale = 2 * pixelWidth * distanceToCamera * frustum._top / camera->getViewPortHeight() / frustum._znear;
  const Vector3D upper = Vector3D(scale, scale, 1);
  const Vector3D lower = Vector3D(-scale, -scale, 0);
  return new OrientedBox(lower, upper, *getTransformMatrix(planet));
}


void RasterLineShape::computeOrientationParams(const Planet* planet)
{
  _cartesianStartPos = new Vector3D(planet->toCartesian(*_geodeticStartPos));
  const Vector3D cartesianEndPos = Vector3D(planet->toCartesian(*_geodeticEndPos));
  const Vector3D line = cartesianEndPos.sub(*_cartesianStartPos);
  setScale(1, 1, line.length());
  const Vector3D normal = planet->geodeticSurfaceNormal(*_cartesianStartPos);
  setPitch(line.angleBetween(normal).times(-1));
  const Vector3D north2D = planet->getNorth().projectionInPlane(normal);
  const Vector3D projectedLine = line.projectionInPlane(normal);
  setHeading(projectedLine.signedAngleBetween(north2D, normal));
}


BoundingVolume* RasterLineShape::getBoundingVolume(const G3MRenderContext *rc)
{
  if (_boundingVolume != NULL)
    delete _boundingVolume;
  _boundingVolume = computeOrientedBox(rc->getPlanet(), rc->getCurrentCamera());
  return _boundingVolume;
}


GEORasterSymbol* RasterLineShape::createRasterSymbolIfNeeded() const
{
  std::vector<Geodetic2D*>* coordinates = new std::vector<Geodetic2D*>;
  coordinates->push_back(_geodeticStartPos);
  coordinates->push_back(_geodeticEndPos);
  float dashLengths[] = {};
  GEO2DLineRasterStyle lineStyle(*_color, _width, CAP_ROUND, JOIN_ROUND, 1, dashLengths, 0, 0);
  //return new GEORasterLineSymbol(&coordinates, lineStyle);
  
  const GEO2DCoordinatesData* coordinatesData = new GEO2DCoordinatesData(coordinates);
  return new GEOLineRasterSymbol(coordinatesData, lineStyle);
}
  
  
