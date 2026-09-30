//
//  G3MCameraTransitionsDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 9/30/26.
//

#include "G3MCameraTransitionsDemoScene.hpp"

#include <G3M/ILogger.hpp>
#include <G3M/Angle.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/Vector2F.hpp>
#include <G3M/TimeInterval.hpp>
#include <G3M/Layer.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/G3MWidget.hpp>
#include <G3M/Camera.hpp>
#include <G3M/CameraPose.hpp>
#include <G3M/Mark.hpp>
#include <G3M/MarksRenderer.hpp>
#include <G3M/MeshRenderer.hpp>
#include <G3M/DirectMesh.hpp>
#include <G3M/FloatBufferBuilderFromGeodetic.hpp>
#include <G3M/GLConstants.hpp>
#include <G3M/Color.hpp>
#include <G3M/Planet.hpp>
#include <G3M/Geodetic2D.hpp>
#include <G3M/Vector3D.hpp>
#include <G3M/IMathUtils.hpp>
#include <G3M/CameraFlightArc.hpp>

#include "G3MDemoModel.hpp"


static const Geodetic3D MADRID    = Geodetic3D::fromDegrees(40.4719,  -3.5626, 0);
static const Geodetic3D TOLEDO    = Geodetic3D::fromDegrees(39.8628,  -4.0273, 0);
static const Geodetic3D LISBON    = Geodetic3D::fromDegrees(38.7813,  -9.1359, 0);
static const Geodetic3D AMSTERDAM = Geodetic3D::fromDegrees(52.3105,   4.7683, 0);
static const Geodetic3D TOKYO     = Geodetic3D::fromDegrees(35.5494, 139.7798, 0);
static const Geodetic3D SYDNEY    = Geodetic3D::fromDegrees(-33.9399, 151.1753, 0);
static const Geodetic3D WASHINGTON   = Geodetic3D::fromDegrees( 38.9531, -77.4565, 0);
static const Geodetic3D BUENOS_AIRES = Geodetic3D::fromDegrees(-34.8222, -58.5358, 0);

static Geodetic3D above(const Geodetic3D& place,
                        const double height) {
  return Geodetic3D(place._latitude, place._longitude, height);
}

static void addCityLabel(MarksRenderer*     marksRenderer,
                         const std::string& name,
                         const Geodetic3D&  city) {
  const double visibleFromAnyDistance = 1e9;
  marksRenderer->addMark( new Mark(name, city, ABSOLUTE, visibleFromAnyDistance) );
}

// arc of CameraGoToPositionEffect before CameraFlightArc, kept to compare both by eye
static double previousArcPeak(const Planet*     planet,
                              const Geodetic3D& from,
                              const Geodetic3D& to,
                              const double      fromValue,
                              const double      toValue) {
  const double maxHeight = planet->getRadii().axisAverage() * 5;

  const double deltaLatInDeg = from._latitude._degrees  - to._latitude._degrees;
  const double deltaLonInDeg = from._longitude._degrees - to._longitude._degrees;
  const double distanceInDeg = IMathUtils::instance()->sqrt((deltaLatInDeg * deltaLatInDeg) +
                                                            (deltaLonInDeg * deltaLonInDeg));
  if (distanceInDeg >= 180) {
    return maxHeight;
  }

  const double middleHeight  = (distanceInDeg / 180) * maxHeight;
  const double averageHeight = (fromValue + toValue) / 2;
  if (middleHeight < averageHeight) {
    return averageHeight + ((averageHeight - middleHeight) / 2);
  }
  return middleHeight;
}

static Geodetic3D alongTheWay(const Geodetic3D& from,
                              const Geodetic3D& to,
                              const double      pan,
                              const double      height) {
  return Geodetic3D(Angle::linearInterpolation(from._latitude,  to._latitude,  pan),
                    Angle::linearInterpolation(from._longitude, to._longitude, pan),
                    height);
}

static Mesh* createArcMesh(const Planet*                  planet,
                           const std::vector<Geodetic3D>& positions,
                           const Color&                   color) {
  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);

  const size_t count = positions.size();
  for (size_t i = 0; i < count; i++) {
    vertices->add(positions[i]);
  }

  Mesh* mesh = new DirectMesh(GLPrimitive::lineStrip(),
                              true,
                              vertices->getCenter(),
                              vertices->create(),
                              3 /* lineWidth */,
                              1 /* pointSize */,
                              new Color(color),
                              NULL  /* colors */,
                              false /* depthTest */);
  delete vertices;
  return mesh;
}


void G3MCameraTransitionsDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model = getModel();

  Layer* layer = model->createRasterLayer();
  model->getLayerSet()->addLayer(layer);

  MarksRenderer* marksRenderer = model->getMarksRenderer();
  addCityLabel(marksRenderer, "Madrid",    MADRID);
  addCityLabel(marksRenderer, "Toledo",    TOLEDO);
  addCityLabel(marksRenderer, "Lisbon",    LISBON);
  addCityLabel(marksRenderer, "Amsterdam", AMSTERDAM);
  addCityLabel(marksRenderer, "Tokyo",     TOKYO);
  addCityLabel(marksRenderer, "Sydney",    SYDNEY);
  addCityLabel(marksRenderer, "Washington",   WASHINGTON);
  addCityLabel(marksRenderer, "Buenos Aires", BUENOS_AIRES);
}

void G3MCameraTransitionsDemoScene::animate(const Geodetic3D& fromPosition,
                                            const Angle&      fromHeading,
                                            const Angle&      fromPitch,
                                            const Geodetic3D& toPosition,
                                            const Angle&      toHeading,
                                            const Angle&      toPitch,
                                            const double      seconds) {
  showArcs(fromPosition, toPosition, fromPosition._height, toPosition._height);

  // the effect places the camera at the origin on its first step, so the transition is seen whole
  getModel()->getG3MWidget()->setAnimatedCameraPosition(TimeInterval::fromSeconds(seconds),
                                                        fromPosition, toPosition,
                                                        fromHeading,  toHeading,
                                                        fromPitch,    toPitch);
}

void G3MCameraTransitionsDemoScene::animateToTwoAnchorsPose(const Geodetic3D& position1,
                                                            const Geodetic3D& position2,
                                                            const Angle&      pitch) {
  G3MWidget* g3mWidget = getModel()->getG3MWidget();
  const Camera* camera = g3mWidget->getNextCamera();

  // anchors along the long axis of the viewport, 15% in from each edge
  const bool landscape = (camera->getViewPortWidth() >= camera->getViewPortHeight());
  const Vector2F screenPosition1 = landscape ? Vector2F(0.15f, 0.5f) : Vector2F(0.5f, 0.15f);
  const Vector2F screenPosition2 = landscape ? Vector2F(0.85f, 0.5f) : Vector2F(0.5f, 0.85f);

  const CameraPose pose = camera->computeCameraPose(position1, screenPosition1,
                                                    position2, screenPosition2,
                                                    pitch);
  if (pose.isNan()) {
    ILogger::instance()->logError("Camera Transitions: no camera pose shows both anchors");
    return;
  }

  ILogger::instance()->logInfo("Camera Transitions: pose position=%s heading=%s pitch=%s",
                               pose._position.description().c_str(),
                               pose._heading.description().c_str(),
                               pose._pitch.description().c_str());

  const Geodetic3D cameraPosition = camera->getGeodeticPosition();
  showArcs(cameraPosition, pose._position, cameraPosition._height, pose._position._height);

  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(6),
                                       pose._position,
                                       pose._heading,
                                       pose._pitch);
}

void G3MCameraTransitionsDemoScene::animatePointOfView(const Geodetic3D& fromTarget,
                                                       const Geodetic3D& toTarget,
                                                       const double      fromDistance,
                                                       const double      toDistance,
                                                       const Angle&      fromAltitude,
                                                       const Angle&      toAltitude,
                                                       const double      seconds) {
  showArcs(fromTarget, toTarget, fromDistance, toDistance);

  const Angle cameraSouthOfTarget = Angle::fromDegrees(180);
  getModel()->getG3MWidget()->setAnimatedCameraPointOfView(TimeInterval::fromSeconds(seconds),
                                                           fromTarget,   toTarget,
                                                           fromDistance, toDistance,
                                                           cameraSouthOfTarget, cameraSouthOfTarget,
                                                           fromAltitude, toAltitude);
}

static double bearingDegrees(const Geodetic3D& from,
                             const Geodetic3D& to) {
  const IMathUtils* mu = IMathUtils::instance();
  const double lat1     = from._latitude._radians;
  const double lat2     = to._latitude._radians;
  const double deltaLon = to._longitude._radians - from._longitude._radians;
  const double y = mu->sin(deltaLon) * mu->cos(lat2);
  const double x = (mu->cos(lat1) * mu->sin(lat2)) - (mu->sin(lat1) * mu->cos(lat2) * mu->cos(deltaLon));
  return Angle::fromRadians(mu->atan2(y, x))._degrees;
}

G3MCameraTransitionsDemoScene::~G3MCameraTransitionsDemoScene() {
  delete _arcMidpoint;
}

// red: previous CameraGoToPositionEffect arc; blue: CameraFlightArc, shared by both effects now
void G3MCameraTransitionsDemoScene::showArcs(const Geodetic3D& from,
                                             const Geodetic3D& to,
                                             const double      fromValue,
                                             const double      toValue) {
  const Planet* planet = getModel()->getG3MWidget()->getNextCamera()->getPlanet();

  const double previousPeak = previousArcPeak(planet, from, to, fromValue, toValue);
  const CameraFlightArc flightArc(planet, from, to, fromValue, toValue);

  const int steps = 128;
  std::vector<Geodetic3D> previousArc;
  std::vector<Geodetic3D> currentArc;
  for (int i = 0; i <= steps; i++) {
    const double alpha = (double) i / steps;
    previousArc.push_back( alongTheWay(from, to, alpha, IMathUtils::instance()->quadraticBezierInterpolation(fromValue, previousPeak, toValue, alpha)) );
    currentArc.push_back(  alongTheWay(from, to, flightArc.panAt(alpha), flightArc.valueAt(alpha)) );
  }

  MeshRenderer* meshRenderer = getModel()->getMeshRenderer();
  meshRenderer->clearMeshes();
  meshRenderer->addMesh( createArcMesh(planet, previousArc, Color::RED) );
  meshRenderer->addMesh( createArcMesh(planet, currentArc,  Color::BLUE) );

  delete _arcMidpoint;
  _arcMidpoint       = new Geodetic3D( alongTheWay(from, to, 0.5, 0) );
  _arcBearingDegrees = bearingDegrees(*_arcMidpoint, to);
  _arcSeparation     = planet->computePreciseLatLonDistance(from.asGeodetic2D(), to.asGeodetic2D());
  _arcPeak           = IMathUtils::instance()->max(previousPeak, flightArc.valueAt(0.5));
}

void G3MCameraTransitionsDemoScene::viewArcsFromTheSide() {
  if (_arcMidpoint == NULL) {
    ILogger::instance()->logInfo("Camera Transitions: run a transition first to have arcs to look at");
    return;
  }

  const Geodetic3D target = above(*_arcMidpoint, _arcPeak / 2);
  const double distance   = 1.5 * IMathUtils::instance()->max(_arcSeparation, _arcPeak);
  const Angle perpendicularToTheFlight = Angle::fromDegrees(_arcBearingDegrees + 90);
  const Angle slightlyAbove            = Angle::fromDegrees(5);

  getModel()->getG3MWidget()->setCameraPointOfView(target, distance, perpendicularToTheFlight, slightlyAbove);
}

void G3MCameraTransitionsDemoScene::rawSelectOption(const std::string& option,
                                                    int optionIndex) {
  const Angle north   = Angle::zero();
  const Angle nadir   = Angle::fromDegrees(-90);
  const Angle oblique = Angle::fromDegrees(-35);

  const Angle lowAltitude     = Angle::fromDegrees(10);
  const Angle obliqueAltitude = Angle::fromDegrees(35);
  const Angle zenith          = Angle::fromDegrees(90);

  switch (optionIndex) {
    case 0:
      animate(above(MADRID, 100000), north, nadir,
              above(SYDNEY, 100000), north, nadir,
              10);
      break;

    case 1:
      animate(above(MADRID, 20000), north, Angle::fromDegrees(-45),
              above(MADRID, 20000), north, Angle::fromDegrees(-15),
              5);
      break;

    case 2:
      animateToTwoAnchorsPose(MADRID, AMSTERDAM, nadir);
      break;

    case 3:
      animateToTwoAnchorsPose(MADRID, AMSTERDAM, oblique);
      break;

    case 4:
      animateToTwoAnchorsPose(MADRID, SYDNEY, nadir);
      break;

    case 5:
      animatePointOfView(MADRID, MADRID,
                         20000, 20000,
                         lowAltitude, zenith,
                         6);
      break;

    case 6:
      animatePointOfView(MADRID, TOLEDO,
                         20000, 20000,
                         lowAltitude, lowAltitude,
                         6);
      break;

    case 7:
      animatePointOfView(LISBON, TOKYO,
                         50000, 50000,
                         obliqueAltitude, obliqueAltitude,
                         10);
      break;

    case 8:
      animatePointOfView(WASHINGTON, BUENOS_AIRES,
                         50000, 50000,
                         obliqueAltitude, obliqueAltitude,
                         10);
      break;

    case 9:
      animatePointOfView(MADRID, MADRID,
                         2000, 4000000,
                         zenith, zenith,
                         8);
      break;

    case 10:
      viewArcsFromTheSide();
      break;

    default:
      ILogger::instance()->logError("option \"%s\" not supported", option.c_str());
      break;
  }
}
