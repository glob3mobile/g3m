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
#include "Mark.hpp"

#include "ColumnLayoutImageBuilder.hpp"
#include "CanvasImageBuilder.hpp"
#include "IndexedMesh.hpp"
#include "IShortBuffer.hpp"
#include "G3MEventContext.hpp"

double StarDomeRenderer::theta0[] = {6.296579306,6.362289129,6.427998951,6.493708773,6.559418595,6.625128417,6.690838239,6.756548061,6.822257884,6.887967706,6.953677528,7.01938735,7.085097172,7.150806994,7.216516816,7.282226638,7.347936461,7.413646283,7.479356105,7.545065927,7.610775749,7.676485571,7.742195393,7.807905216,7.873615038,7.93932486,8.005034682,8.070744504,8.136454326,8.202164148,8.267873971,8.333583793,8.399293615,8.465003437,8.530713259,8.596423081,8.662132903,8.727842726,8.793552548,8.85926237,8.924972192,8.990682014,9.056391836,9.122101658,9.187811481,9.253521303,9.319231125,9.384940947,9.450650769,9.516360591,9.582070413,9.647780236,9.713490058,9.77919988,9.844909702,9.910619524,9.976329346,10.04203917,10.10774899,10.17345881,10.23916863,10.30487846,10.37058828,10.4362981,10.50200792,10.56771775,10.63342757,10.69913739,10.76484721,10.83055703,10.89626686,10.96197668,11.0276865,11.09339632,11.15910614,11.22481597,11.29052579,11.35623561,35.43786035,11.50357017,11.56928,11.63498982,11.70069964,11.76640946,11.83211929,11.89782911,11.96353893,12.02924875,12.09495857,12.1606684,12.22637822,12.29208804,12.35779786,12.42350768,12.48921751,12.55492733,12.62063715,12.68634697,12.7520568,12.81776662,12.88347644,12.94918626,13.01489608,13.08060591,13.14631573,13.21202555,13.27773537,13.34344519,13.40915502,13.47486484,13.54057466,13.60628448,13.6719943,13.73770413,13.80341395,13.86912377,13.93483359,14.00054342,14.06625324,14.13196306,14.19767288,14.2633827,14.32909253,14.39480235,14.46051217,14.52622199,14.59193181,14.65764164,14.72335146,14.78906128,14.8547711,14.92048093,14.98619075,15.05190057,15.11761039,15.18332021,15.24903004,15.31473986,15.38044968,15.4461595,15.51186932,15.57757915,15.64328897,15.70899879,15.77470861,15.84041844,15.90612826,15.97183808,16.0375479,16.10325772,16.16896755,16.23467737,16.30038719,16.36609701,16.43180683,16.49751666,16.56322648,16.6289363,16.69464612,16.76035595,16.82606577,16.89177559,16.95748541,17.02319523,17.08890506,17.15461488,17.2203247,17.28603452,17.35174434,17.41745417,17.48316399,17.54887381,17.61458363,17.68029346,17.74600328,17.8117131,17.87742292,17.94313274,18.00884257,18.07455239,18.14026221,18.20597203,18.27168185,18.33739168,18.4031015,18.46881132,18.53452114,18.60023097,18.66594079,18.73165061,18.79736043,18.86307025,18.92878008,18.9944899,19.06019972,19.12590954,19.19161936,19.25732919,19.32303901,19.38874883,19.45445865,19.52016848,19.5858783,19.65158812,19.71729794,19.78300776,19.84871759,19.91442741,19.98013723,20.04584705,20.11155687,20.1772667,20.24297652,20.30868634,20.37439616,20.44010599,20.50581581,20.57152563,20.63723545,20.70294527,20.7686551,20.83436492,20.90007474,20.96578456,21.03149438,21.09720421,21.16291403,21.22862385,21.29433367,21.3600435,21.42575332,21.49146314,21.55717296,21.62288278,21.68859261,21.75430243,21.82001225,21.88572207,21.95143189,22.01714172,22.08285154,22.14856136,22.21427118,22.27998101,22.34569083,22.41140065,22.47711047,22.54282029,22.60853012,22.67423994,22.73994976,22.80565958,22.8713694,22.93707923,23.00278905,23.06849887,23.13420869,23.19991852,23.26562834,23.33133816,23.39704798,23.4627578,23.52846763,23.59417745,23.65988727,23.72559709,23.79130691,23.85701674,23.92272656,23.98843638,0.054146203,0.119856025,0.185565847,0.25127567,0.316985492,0.382695314,0.448405136,0.514114958,0.57982478,0.645534602,0.711244425,0.776954247,0.842664069,0.908373891,0.974083713,1.039793535,1.105503357,1.171213179,1.236923002,1.302632824,1.368342646,1.434052468,1.49976229,1.565472112,1.631181934,1.696891757,1.762601579,1.828311401,1.894021223,1.959731045,2.025440867,2.091150689,2.156860512,2.222570334,2.288280156,2.353989978,2.4196998,2.485409622,2.551119444,2.616829267,2.682539089,2.748248911,2.813958733,2.879668555,2.945378377,3.011088199,3.076798022,3.142507844,3.208217666,3.273927488,3.33963731,3.405347132,3.471056954,3.536766777,3.602476599,3.668186421,3.733896243,3.799606065,3.865315887,3.931025709,3.996735532,4.062445354,4.128155176,4.193864998,4.25957482,4.325284642,4.390994464,4.456704286,4.522414109,4.588123931,4.653833753,4.719543575,4.785253397,4.850963219,4.916673041,4.982382864,5.048092686,5.113802508,5.17951233,5.245222152,5.310931974,5.376641796,5.442351619,5.508061441,5.573771263,5.639481085,5.705190907,5.770900729,5.836610551,5.902320374,5.968030196,6.033740018,6.09944984,6.165159662,6.230869484};

double Star::getTrueNorthAzimuthInDegrees(double siderealTime, const Geodetic2D viewerPosition) const{
  const IMathUtils* mu = IMathUtils::instance();
  
  double delta = TO_RADIANS( _declination );
  double alpha = TO_RADIANS( _ascencion );
  double tetha = TO_RADIANS(siderealTime);
  
  double phi = (PI / 2) - viewerPosition._latitude._radians; // 90 - phi
//  printf("PHI: %f\n", viewerPosition._latitude._degrees );
  
  //ArcTan(-(Cos(Degree*delta)*Cos(Degree*(90 - phi))*Cos(Degree*(-alpha + tetha))) + Sin(Degree*delta)*Sin(Degree*(90 - phi)),-(Cos(Degree*delta)*Sin(Degree*(-alpha + tetha))))
  
//  double azimuth = mu->atan2(-(mu->cos(delta) * mu->cos(phi)* mu->cos(-alpha + tetha) ) + mu->sin(delta)* mu->sin(phi),
//                             -(mu->cos(delta)* mu->sin(-alpha + tetha)));
  
//  List(Pi + ArcTan(Cos(Degree*delta)*Cos(Degree*(90 - phi))*Cos(Degree*(-alpha + tetha)) -
//                   Sin(Degree*delta)*Sin(Degree*(90 - phi)),Cos(Degree*delta)*Sin(Degree*(-alpha + tetha))))
  
//  double vIn[] = {
//    mu->cos(tetha - alpha) * mu->cos(delta),
//    
//    mu->sin(tetha - alpha)* mu->cos(delta),
//    
//    mu->sin(delta)
//    
//  };
//  
//  double vOut1 = mu->cos(delta)*mu->cos((phi))*mu->cos((-alpha + tetha)) - mu->sin(delta)*mu->sin(phi);
//  double vOut2 = mu->cos(delta)*mu->sin((-alpha + tetha));
  
  double azimuth = PI + mu->atan2(mu->cos(delta)*mu->sin((-alpha + tetha)),
                                  mu->cos(delta)*mu->cos((phi))*mu->cos((-alpha + tetha)) - mu->sin(delta)*mu->sin(phi));
  
  
  return TO_DEGREES(azimuth);
}

double Star::getAltitude(double siderealTime, const Geodetic2D viewerPosition) const{
  const IMathUtils* mu = IMathUtils::instance();
  
  double delta = TO_RADIANS( _declination );
  double alpha = TO_RADIANS( _ascencion );
  double tetha = TO_RADIANS(siderealTime);
  
  double phi = (PI / 2) - viewerPosition._latitude._radians; // 90 - phi
  
  //ArcSin(Cos(Degree*(90 - phi))*Sin(Degree*delta) + Cos(Degree*delta)*Cos(Degree*(-alpha + tetha))*Sin(Degree*(90 - phi)))
  
//  double ascencion = mu->asin(mu->cos(phi)*mu->sin(delta) + mu->cos(delta)*mu->cos(-alpha + tetha)*mu->sin(phi));
  
//  ArcSin(Cos(Degree*(90 - phi))*Sin(Degree*delta) +
//         Cos(Degree*delta)*Cos(Degree*(-alpha + tetha))*Sin(Degree*(90 - phi)))
  
  double ascencion = mu->asin(mu->cos(phi)*mu->sin(delta) + mu->cos(delta)*mu->cos((-alpha + tetha))*mu->sin(phi));
  
  return TO_DEGREES(ascencion);
}

Vector3D Star::getStarDisplacementInDome(double domeHeight, double siderealTime, const Geodetic2D& domePos) const{

//  if (_name.find("Leo") != std::string::npos){
//    printf("ok");
//  }
  
  //Defining stars by true-north azimuth (heading) and altitude http://en.wikipedia.org/wiki/Azimuth
  Angle azimuth = Angle::fromDegrees(getTrueNorthAzimuthInDegrees(siderealTime, domePos));
  Angle altitude = Angle::fromDegrees(getAltitude(siderealTime, domePos));
  
  printf("STAR: %s - AZ: %f, AL: %f\n", _name.c_str(), azimuth._degrees, altitude._degrees);

  return getStarDisplacementInDome(domeHeight, azimuth, altitude);
}

Vector3D Star::getStarDisplacementInDome(double domeHeight, Angle azimuth, Angle altitude){
  
  Vector3D north = Vector3D::upY();
  Vector3D azimuthRotationAxis = Vector3D::downZ();
  Vector3D altitudeRotationAxis = Vector3D::upX();
  Vector3D origin = Vector3D::zero;
  
  Vector3D startingStarPos = origin.add(north.normalized().times(domeHeight));
  
  MutableMatrix44D mAltitude = MutableMatrix44D::createGeneralRotationMatrix(altitude, altitudeRotationAxis, origin);
  MutableMatrix44D mAzimuth = MutableMatrix44D::createGeneralRotationMatrix(azimuth, azimuthRotationAxis, origin);
  MutableMatrix44D m = mAzimuth.multiply(mAltitude);
  
  Vector3D starPos = startingStarPos.transformedBy(m, 1.0);
  
  return starPos;
}

StarDomeRenderer::~StarDomeRenderer()
{
  clear();
  _glState->_release();
  delete _position;
}


void StarDomeRenderer::clear(){
  if (_mr != NULL){
    _mr->removeAllMarks();
  }
  delete _starsShape;
  _starsShape = NULL;
}

void StarDomeRenderer::initialize(const G3MContext* context) {
  
  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  FloatBufferBuilderFromColor colors;
  
  const double domeHeight = 1e5;
  
  double siderealTime = getSiderealTimeInDegrees(_position->_longitude._degrees, _clockTimeInDegrees, _dayOfTheYear);

//#warning CAMBIAR PUESTO EN GRADOS
//  siderealTime = 12.898573004736164 * 15;
  
  
  size_t size = _stars.size();
  for(int i = 0; i < size; i++){
    Vector3D starPos = _stars[i].getStarDisplacementInDome(domeHeight, siderealTime, _position->asGeodetic2D());
    
    fbb->add(starPos);
    
    colors.add(*_stars[i]._color);
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
  
  
  IShortBuffer* index = IFactory::instance()->createShortBuffer(_lines.size());
  for (size_t i = 0; i < _lines.size(); i++) {
    index->put(i, _lines[i]);
  }
//  
//  IndexedMesh(const int primitive,
//              const Vector3D& center,
//              IFloatBuffer* vertices,
//              bool ownsVertices,
//              IShortBuffer* indices,
//              bool ownsIndices,
//              float lineWidth,
//              float pointSize = 1,
//              const Color* flatColor = NULL,
//              IFloatBuffer* colors = NULL,
//              const float colorsIntensity = 0.0f,
//              bool depthTest = true,
//              IFloatBuffer* normals = NULL,
//              bool polygonOffsetFill = false,
//              float polygonOffsetFactor = 0,
//              float polygonOffsetUnits = 0);

  
  
  IndexedMesh* lines = new IndexedMesh(GLPrimitive::lines(),
                                     fbb->getCenter(),
                                     fbb->create(),
                                       true,
                                     index,
                                       true,
                                     1.0,
                                     4.0,
                                     new Color(_color),
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
  
  
  if (_mr != NULL){
    
    Vector3D firstStarPos = _stars[0].getStarDisplacementInDome(domeHeight, siderealTime, _position->asGeodetic2D());
    
    //Computing Mark Position
    MutableMatrix44D* m = _starsShape->createTransformMatrix(context->getPlanet());
    
    
    
    Geodetic3D g = context->getPlanet()->toGeodetic3D(firstStarPos.transformedBy(*m, 1.0));
    
    Mark* mark = new   Mark(_name,
                            //Geodetic3D(_position->asGeodetic2D(), _position->_height + 1000),
                            g,
                            ABSOLUTE,
                            0 //Always visible
                            );
    
    
    mark->setMarkAnchor(0.5, -0.5);
    
    _mr->addMark(mark);

    for(int i = 0; i < _stars.size(); i++){
      Vector3D firstStarPos = _stars[i].getStarDisplacementInDome(domeHeight, siderealTime, _position->asGeodetic2D());
      
      //Computing Mark Position
      MutableMatrix44D* m = _starsShape->createTransformMatrix(context->getPlanet());
      
      Geodetic3D g = context->getPlanet()->toGeodetic3D(firstStarPos.transformedBy(*m, 1.0));
      
      Mark* mark = new   Mark(_stars[i].getName(),
                              g,
                              ABSOLUTE,
                              0, //Always visible
                              15 // Font size
                              );
      
      
      mark->setMarkAnchor(0.5, 0.5);
      
      _mr->addMark(mark);
    }
 
    delete m;
  }
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
  
  if (_starsShape == NULL){
    initialize(rc);
  }
  
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
  double siderealTime = getSiderealTimeInDegrees(_position->_longitude._degrees, 0, 0);
  
  double minDist = 4; //No star will be selected above this threshold
  int index = -1;
  
  int size = _stars.size();
  for(int i = 0; i < size; i++){
    
    double dist = _stars[i].distanceInDegrees(trueNorthAzimuthInDegrees, altitudeInDegrees, siderealTime, _position->asGeodetic2D());
    
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

