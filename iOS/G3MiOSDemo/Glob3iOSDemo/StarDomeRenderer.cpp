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

double StarDomeRenderer::theta0[] = {18.79370913, 18.85941895, 18.92512878, 18.9908386, 19.05654842, 19.12225824, 19.18796806, 19.25367789, 19.31938771, 19.38509753, 19.45080735, 19.51651718, 19.582227, 19.64793682, 19.71364664, 19.77935646, 19.84506629, 19.91077611, 19.97648593, 20.04219575, 20.10790557, 20.1736154, 20.23932522, 20.30503504, 20.37074486, 20.43645469, 20.50216451, 20.56787433, 20.63358415, 20.69929397, 20.7650038, 20.83071362, 20.89642344, 20.96213326, 21.02784308, 21.09355291, 21.15926273, 21.22497255, 21.29068237, 21.3563922, 21.42210202, 21.48781184, 21.55352166, 21.61923148, 21.68494131, 21.75065113, 21.81636095, 21.88207077, 21.94778059, 22.01349042, 22.07920024, 22.14491006, 22.21061988, 22.27632971, 22.34203953, 22.40774935, 22.47345917, 22.53916899, 22.60487882, 22.67058864, 22.73629846, 22.80200828, 22.8677181, 22.93342793, 22.99913775, 23.06484757, 23.13055739, 23.19626722, 23.26197704, 23.32768686, 23.39339668, 23.4591065, 23.52481633, 23.59052615, 23.65623597, 23.72194579, 23.78765561, 23.85336544, 23.91907526, 0.0007, 0.066409822, 0.132119644, 0.197829466, 0.263539289, 0.329249111, 0.394958933, 0.460668755, 0.526378577, 0.592088399, 0.657798221, 0.723508044, 0.789217866, 0.854927688, 0.92063751, 0.986347332, 1.052057154, 1.117766976, 1.183476799, 1.249186621, 1.314896443, 1.380606265, 1.446316087, 1.512025909, 1.577735731, 1.643445553, 1.709155376, 1.774865198, 1.84057502, 1.906284842, 1.971994664, 2.037704486, 2.103414308, 2.169124131, 2.234833953, 2.300543775, 2.366253597, 2.431963419, 2.497673241, 2.563383063, 2.629092886, 2.694802708, 2.76051253, 2.826222352, 2.891932174, 2.957641996, 3.023351818, 3.089061641, 3.154771463, 3.220481285, 3.286191107, 3.351900929, 3.417610751, 3.483320573, 3.549030396, 3.614740218, 3.68045004, 3.746159862, 3.811869684, 3.877579506, 3.943289328, 4.008999151, 4.074708973, 4.140418795, 4.206128617, 4.271838439, 4.337548261, 4.403258083, 4.468967906, 4.534677728, 4.60038755, 4.666097372, 4.731807194, 4.797517016, 4.863226838, 4.92893666, 4.994646483, 5.060356305, 5.126066127, 5.191775949, 5.257485771, 5.323195593, 5.388905415, 5.454615238, 5.52032506, 5.586034882, 5.651744704, 5.717454526, 5.783164348, 5.84887417, 5.914583993, 5.980293815, 6.046003637, 6.111713459, 6.177423281, 6.243133103, 6.308842925, 6.374552748, 6.44026257, 6.505972392, 6.571682214, 6.637392036, 6.703101858, 6.76881168, 6.834521503, 6.900231325, 6.965941147, 7.031650969, 7.097360791, 7.163070613, 7.228780435, 7.294490258, 7.36020008, 7.425909902, 7.491619724, 7.557329546, 7.623039368, 7.68874919, 7.754459013, 7.820168835, 7.885878657, 7.951588479, 8.017298301, 8.083008123, 8.148717945, 8.214427767, 8.28013759, 8.345847412, 8.411557234, 8.477267056, 8.542976878, 8.6086867, 8.674396522, 8.740106345, 8.805816167, 8.871525989, 8.937235811, 9.002945633, 9.068655455, 9.134365277, 9.2000751, 9.265784922, 9.331494744, 9.397204566, 9.462914388, 9.52862421, 9.594334032, 9.660043855, 9.725753677, 9.791463499, 9.857173321, 9.922883143, 9.988592965, 10.05430279, 10.12001261, 10.18572243, 10.25143225, 10.31714208, 10.3828519, 10.44856172, 10.51427154, 10.57998136, 10.64569119, 10.71140101, 10.77711083, 10.84282065, 10.90853048, 10.9742403, 11.03995012, 11.10565994, 11.17136976, 11.23707959, 11.30278941, 11.36849923, 11.43420905, 11.49991887, 11.5656287, 11.63133852, 11.69704834, 11.76275816, 11.82846799, 11.89417781, 11.95988763, 12.02559745, 12.09130727, 12.1570171, 12.22272692, 12.28843674, 12.35414656, 12.41985638, 12.48556621, 12.55127603, 12.61698585, 12.68269567, 12.7484055, 12.81411532, 12.87982514, 12.94553496, 13.01124478, 13.07695461, 13.14266443, 13.20837425, 13.27408407, 13.33979389, 13.40550372, 13.47121354, 13.53692336, 13.60263318, 13.66834301, 13.73405283, 13.79976265, 13.86547247, 13.93118229, 13.99689212, 14.06260194, 14.12831176, 14.19402158, 14.2597314, 14.32544123, 14.39115105, 14.45686087, 14.52257069, 14.58828052, 14.65399034, 14.71970016, 14.78540998, 14.8511198, 14.91682963, 14.98253945, 15.04824927, 15.11395909, 15.17966891, 15.24537874, 15.31108856, 15.37679838, 15.4425082, 15.50821803, 15.57392785, 15.63963767, 15.70534749, 15.77105731, 15.83676714, 15.90247696, 15.96818678, 16.0338966, 16.09960642, 16.16531625, 16.23102607, 16.29673589, 16.36244571, 16.42815553, 16.49386536, 16.55957518, 16.625285, 16.69099482, 16.75670465, 16.82241447, 16.88812429, 16.95383411, 17.01954393, 17.08525376, 17.15096358, 17.2166734, 17.28238322, 17.34809304, 17.41380287, 17.47951269, 17.54522251, 17.61093233, 17.67664216, 17.74235198, 17.8080618, 17.87377162, 17.93948144, 18.00519127, 18.07090109, 18.13661091, 18.20232073, 18.26803055, 18.33374038, 18.3994502, 18.46516002, 18.53086984, 18.59657967, 18.66228949, 18.72799931};

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
  
  double siderealTime = getSiderealTime(_position->_longitude._degrees, _clockTimeInDegrees, _dayOfTheYear);
  
  Vector3D* firstStarPos = NULL;
  
  int size = _stars.size();
  for(int i = 0; i < size; i++){
    
    //Defining stars by true-north azimuth (heading) and altitude http://en.wikipedia.org/wiki/Azimuth
    Geodetic2D viewerPosition = _position->asGeodetic2D();
    Angle azimuth = Angle::fromDegrees(_stars[i].getTrueNorthAzimuthInDegrees(siderealTime, viewerPosition));
    Angle altitude = Angle::fromDegrees(_stars[i].getAltitude(siderealTime, viewerPosition));
    
    MutableMatrix44D mAltitude = MutableMatrix44D::createGeneralRotationMatrix(altitude, altitudeRotationAxis, origin);
    MutableMatrix44D mAzimuth = MutableMatrix44D::createGeneralRotationMatrix(azimuth, azimuthRotationAxis, origin);
    MutableMatrix44D m = mAzimuth.multiply(mAltitude);
    
    Vector3D starPos = startingStarPos.transformedBy(m, 1.0);
    
    fbb->add(starPos);
    
    colors.add(*_stars[i]._color);
    
    if (i == 0){
      firstStarPos = new Vector3D(starPos);
    }
    
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
                                     &_color,
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
  
  
  if (_mr != NULL && firstStarPos != NULL){
    
    
    //Computing Mark Position
    MutableMatrix44D* m = _starsShape->createTransformMatrix(context->getPlanet());
    
    delete _constelationPos;
    _constelationPos = new Vector3D( firstStarPos->transformedBy(*m, 1.0));
    
    LabelImageBuilder *lib = new  LabelImageBuilder(_name,
                                                    GFont::sansSerif(),
                                                    120);
    
    
    lib->build(context, new MarkLabelListener(this, context->getPlanet()), true);
    
    
    
    //    Geodetic3D g = context->getPlanet()->toGeodetic3D(pos);
    //
    //    Mark* mark = new   Mark(_name,
    //                            //Geodetic3D(_position->asGeodetic2D(), _position->_height + 1000),
    //                            g,
    //                            ABSOLUTE,
    //                            0 //Always visible
    //                            );
    //
    //    _mr->addMark(mark);
    
    delete firstStarPos;
    delete m;
  }
}

void StarDomeRenderer::MarkLabelListener::imageCreated(const IImage*      image, const std::string& imageName){
  
  Geodetic3D g = _planet->toGeodetic3D(*_sdr->_constelationPos);
  Geodetic3D g2(g._latitude, g._longitude, g._height - 2000);
  Mark* mark = new     Mark(image,
                            imageName,
                            g2,
                            ABSOLUTE,
                            0);
  
//  Mark(const IImage*      image,
//       const std::string& imageID,
//       const Geodetic3D&  position,
//       AltitudeMode       altitudeMode,
//       double             minDistanceToCamera=4.5e+06,
//       MarkUserData*      userData=NULL,
//       bool               autoDeleteUserData=true,
//       MarkTouchListener* listener=NULL,
//       bool               autoDeleteListener=false);
  
  _sdr->_mr->addMark(mark);
  
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

