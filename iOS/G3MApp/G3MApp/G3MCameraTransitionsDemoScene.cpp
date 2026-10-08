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
#include <G3M/MarkBuilder.hpp>
#include <G3M/LabelImageFactory.hpp>
#include <G3M/LabelStyle.hpp>
#include <G3M/GFont.hpp>
#include <G3M/MarksRenderer.hpp>

#include "G3MDemoModel.hpp"


static const Geodetic3D MADRID    = Geodetic3D::fromDegrees(40.4719,  -3.5626, 0);
static const Geodetic3D TOLEDO    = Geodetic3D::fromDegrees(39.8628,  -4.0273, 0);
static const Geodetic3D LISBON    = Geodetic3D::fromDegrees(38.7813,  -9.1359, 0);
static const Geodetic3D AMSTERDAM = Geodetic3D::fromDegrees(52.3105,   4.7683, 0);
static const Geodetic3D TOKYO     = Geodetic3D::fromDegrees(35.5494, 139.7798, 0);
static const Geodetic3D SYDNEY    = Geodetic3D::fromDegrees(-33.9399, 151.1753, 0);
static const Geodetic3D WASHINGTON   = Geodetic3D::fromDegrees( 38.9531, -77.4565, 0);
static const Geodetic3D BUENOS_AIRES = Geodetic3D::fromDegrees(-34.8222, -58.5358, 0);
static const Geodetic3D NEW_YORK      = Geodetic3D::fromDegrees( 40.6413,  -73.7781, 0);
static const Geodetic3D SAN_FRANCISCO = Geodetic3D::fromDegrees( 37.6213, -122.3790, 0);

static Geodetic3D above(const Geodetic3D& place,
                        const double height) {
  return Geodetic3D(place._latitude, place._longitude, height);
}

static void addCityLabel(MarksRenderer*     marksRenderer,
                         const std::string& name,
                         const Geodetic3D&  city) {
  const double visibleFromAnyDistance = 1e9;
  MarkBuilder builder;
  builder.setMinDistanceToCamera(visibleFromAnyDistance);
  builder.setPosition(city);
  builder.addOutfit(new LabelImageFactory(name,
                                          LabelStyle::shadowed(GFont::sansSerif(20),
                                                               Color::WHITE,
                                                               Color::BLACK,
                                                               1,
                                                               Vector2F(2, 2))));
  marksRenderer->addMark( builder.build() );
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
  addCityLabel(marksRenderer, "New York",      NEW_YORK);
  addCityLabel(marksRenderer, "San Francisco", SAN_FRANCISCO);
}

void G3MCameraTransitionsDemoScene::animate(const Geodetic3D& fromPosition,
                                            const Angle&      fromHeading,
                                            const Angle&      fromPitch,
                                            const Geodetic3D& toPosition,
                                            const Angle&      toHeading,
                                            const Angle&      toPitch,
                                            const double      seconds) {
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

  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(6),
                                       pose._position,
                                       pose._heading,
                                       pose._pitch);
}

void G3MCameraTransitionsDemoScene::animatePointOfView(const Geodetic3D& fromTarget,
                                                       const Geodetic3D& toTarget,
                                                       const double      fromDistance,
                                                       const double      toDistance,
                                                       const Angle&      fromAzimuth,
                                                       const Angle&      toAzimuth,
                                                       const Angle&      fromAltitude,
                                                       const Angle&      toAltitude,
                                                       const double      seconds) {
  getModel()->getG3MWidget()->setAnimatedCameraPointOfView(TimeInterval::fromSeconds(seconds),
                                                           fromTarget,   toTarget,
                                                           fromDistance, toDistance,
                                                           fromAzimuth,  toAzimuth,
                                                           fromAltitude, toAltitude);
}

void G3MCameraTransitionsDemoScene::rawSelectOption(const std::string& option,
                                                    int optionIndex) {
  const Angle north   = Angle::zero();
  const Angle nadir   = Angle::fromDegrees(-90);
  const Angle oblique = Angle::fromDegrees(-35);

  const Angle lowAltitude     = Angle::fromDegrees(10);
  const Angle obliqueAltitude = Angle::fromDegrees(35);
  const Angle zenith          = Angle::fromDegrees(90);

  const Angle cameraSouthOfTarget = Angle::fromDegrees(180);

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
                         cameraSouthOfTarget, cameraSouthOfTarget,
                         lowAltitude, zenith,
                         6);
      break;

    case 6:
      animatePointOfView(MADRID, TOLEDO,
                         20000, 20000,
                         cameraSouthOfTarget, cameraSouthOfTarget,
                         lowAltitude, lowAltitude,
                         6);
      break;

    case 7:
      animatePointOfView(LISBON, TOKYO,
                         50000, 50000,
                         cameraSouthOfTarget, cameraSouthOfTarget,
                         obliqueAltitude, obliqueAltitude,
                         10);
      break;

    case 8:
      animatePointOfView(WASHINGTON, BUENOS_AIRES,
                         50000, 50000,
                         cameraSouthOfTarget, cameraSouthOfTarget,
                         obliqueAltitude, obliqueAltitude,
                         10);
      break;

    case 9:
      animatePointOfView(BUENOS_AIRES, WASHINGTON,
                         50000, 50000,
                         cameraSouthOfTarget, cameraSouthOfTarget,
                         zenith, zenith,
                         10);
      break;

    case 10:
      animatePointOfView(NEW_YORK, SAN_FRANCISCO,
                         30000, 5000,
                         Angle::fromDegrees(90), Angle::fromDegrees(270),
                         Angle::fromDegrees(20), Angle::fromDegrees(45),
                         12);
      break;

    case 11:
      animatePointOfView(MADRID, MADRID,
                         2000, 4000000,
                         cameraSouthOfTarget, cameraSouthOfTarget,
                         zenith, zenith,
                         8);
      break;

    default:
      ILogger::instance()->logError("option \"%s\" not supported", option.c_str());
      break;
  }
}
