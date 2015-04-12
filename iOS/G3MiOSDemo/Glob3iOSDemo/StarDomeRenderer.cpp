//
//  StarDomeRenderer.cpp
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 19/3/15.
//
//

#include "StarDomeRenderer.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "DirectMesh.hpp"
#include "Vector3D.hpp"
#include "GLState.hpp"
#include "GLFeature.hpp"
#include "Camera.hpp"
#include "TouchEvent.hpp"
#include "CoordinateSystem.hpp"
#include "CompositeMesh.hpp"

void StarDomeRenderer::initialize(const G3MContext* context) {
  
  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  FloatBufferBuilderFromColor colors;
  
  double domeHeight = 1e5;
  
  Vector3D north = Vector3D::upY();
  Vector3D azimuthRotationAxis = Vector3D::downZ();
  Vector3D altitudeRotationAxis = Vector3D::upX();
  Vector3D origin = Vector3D::zero;
  
  Vector3D startingStarPos = origin.add(north.normalized().times(domeHeight));
  
//  _position = new Geodetic3D( Geodetic3D::fromDegrees(27.973105, -15.597545, 500));

  double siderealTime = getSiderealTime(_position->_longitude._degrees, _clockTimeInDegrees, _siderealTimeOffset);
  
  int size = _stars.size();
  for(int i = 0; i < size; i++){
    
    //Defining stars by true-north azimuth (heading) and altitude http://en.wikipedia.org/wiki/Azimuth
    Angle azimuth = Angle::fromDegrees(_stars[i].getTrueNorthAzimuthInDegrees(siderealTime));
    Angle altitude = Angle::fromDegrees(_stars[i].getAltitude(siderealTime));
    
    MutableMatrix44D mAltitude = MutableMatrix44D::createGeneralRotationMatrix(altitude, altitudeRotationAxis, origin);
    MutableMatrix44D mAzimuth = MutableMatrix44D::createGeneralRotationMatrix(azimuth, azimuthRotationAxis, origin);
    //MutableMatrix44D m = mAltitude.multiply(mAzimuth);
    MutableMatrix44D m = mAzimuth.multiply(mAltitude);
    
    Vector3D starPos = startingStarPos.transformedBy(m, 1.0);
    
    fbb->add(starPos);
    
    colors.add(*_stars[i]._color);
    
    //printf("STAR %d COLOR = %s\n", i, _stars[i]._color->description().c_str());
  }
  
  DirectMesh* dm = new DirectMesh(GLPrimitive::points(),
                                  true,
                                  fbb->getCenter(),
                                  fbb->create(),
                                  1.0,
                                  4.0,
                                  Color::newFromRGBA(1.0, 1.0, 1.0, 1.0),
                                  colors.create(),
                                  1.0f,
                                  true,
                                  NULL);
  
  DirectMesh* lines = new DirectMesh(GLPrimitive::lineStrip(),
                                  true,
                                  fbb->getCenter(),
                                  fbb->create(),
                                  1.0,
                                  4.0,
                                  Color::newFromRGBA(1.0, 0.0, 1.0, 1.0),
                                  NULL,
                                  1.0f,
                                  true,
                                  NULL);
  
  CompositeMesh* cm = new CompositeMesh();
  cm->addMesh(lines);
  cm->addMesh(dm);
  
  delete fbb;
  
  _starsShape = new StarsMeshShape(new Geodetic3D(*_position),
                                   ABSOLUTE,
                                   cm);
  
  _starsShape->initialize(context);
}

void StarDomeRenderer::render(const G3MRenderContext* rc, GLState* glState){
  
  _currentCamera = rc->getCurrentCamera();
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(_currentCamera), true);
  }
  else {
    f->setMatrix(_currentCamera->getModelViewMatrix44D());
  }
  
  delete _position;
  _position = new Geodetic3D(_currentCamera->getGeodeticPosition());
  
  _starsShape->setPosition(*_position);
  
  _starsShape->render(rc, _glState, true);
  
}

bool StarDomeRenderer::onTouchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent){
  
  if (touchEvent->getType() != DownUp){
    return false;
  }
  
  Vector3D ray = _currentCamera->pixel2Ray(touchEvent->getTouch(0)->getPos());
  
  
  CoordinateSystem local = ec->getPlanet()->getCoordinateSystemAt(_currentCamera->getGeodeticPosition()); //GRAN CANARIA
  
  Vector3D rayOnGround = ray.projectionInPlane(local._z);
  
  Angle azimuth = rayOnGround.signedAngleBetween(local._y, local._z).normalized();
  Angle altitude = rayOnGround.angleBetween(ray);
  
  printf("SELECTED AZIMUTH: %f, ALTITUDE: %f\n", azimuth._degrees, altitude._degrees);
  
  selectStar(azimuth, altitude);
  
  
  delete _starsShape;
  initialize(ec);
  
  return true;
  
}

void StarDomeRenderer::selectStar(const Angle& trueNorthAzimuthInDegrees, const Angle& altitudeInDegrees){
  
#warning TODO
  double siderealTime = getSiderealTime(_position->_longitude._degrees, 0, 0);
  
  double minDist = 4; //No star will be selected above this threshold
  int index = -1;
  
  int size = _stars.size();
  for(int i = 0; i < size; i++){
    
    double dist = _stars[i].distanceInDegrees(trueNorthAzimuthInDegrees, altitudeInDegrees, siderealTime);
    
    //printf("STAR %d AZIMUTH: %f, ALTITUDE: %f -> %f\n", i, _stars[i]._trueNorthAzimuthInDegrees, _stars[i]._altitudeInDegrees, dist);

    if (dist < minDist){
      index = i;
      minDist = dist;
    }
    
  }
  
  if (index > -1){
    printf("SELECTED STAR %d - DIST = %f\n", index, minDist);
    
    if (_stars[index]._color->_blue == 1.0){
      _stars[index].setColor(Color::red());
    } else{
      _stars[index].setColor(Color::white());
    }
  }
  
  
}

