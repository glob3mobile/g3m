//
//  G3MTranslateScaleGizmoDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#include "G3MTranslateScaleGizmoDemoScene.hpp"

#include <G3M/TranslateScaleGizmo.hpp>
#include <G3M/EllipsoidShape.hpp>
#include <G3M/Planet.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/Color.hpp>
#include <G3M/G3MWidget.hpp>
#include <G3M/BingMapsLayer.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/G3MContext.hpp>
#include <G3M/GLConstants.hpp>
#include <G3M/ShapesRenderer.hpp>

#include "G3MDemoModel.hpp"


class GizmoListener : public TranslateScaleGizmoListener{
  EllipsoidShape* _shape;
  const Planet* _planet;
public:

  GizmoListener(EllipsoidShape* shape, const Planet* planet):
  _shape(shape), _planet(planet){}

  void onChanged(const TranslateScaleGizmo& gizmo) override{
    printf("Gizmo P: %s S: %0.2f\n", gizmo.getCoordinateSystem()._origin.description().c_str(), gizmo.getScale());

    Geodetic3D geoPos = _planet->toGeodetic3D(gizmo.getCoordinateSystem()._origin);
    _shape->setPosition(geoPos);
    _shape->setScale(gizmo.getScale());
  }

  void onChangeEnded(const TranslateScaleGizmo& gizmo) override{
    printf("Change ended on gizmo P: %s S: %0.2f\n", gizmo.getCoordinateSystem()._origin.description().c_str(), gizmo.getScale());
  }
};


void G3MTranslateScaleGizmoDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  g3mWidget->setBackgroundColor(Color::fromRGBA(0.9f, 0.21f, 0.21f, 1.0f));

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  const Geodetic3D ellipsoidPosition = Geodetic3D::fromDegrees(37.39996584, -1.75035672, 0);

  const double size = 100.0;

  g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(37.397230737816194335, -1.7473434604041810925, 540),
                                       Angle::fromDegrees(40.586649),
                                       Angle::fromDegrees(-53.421991));

  EllipsoidShape* ellipsoid = new EllipsoidShape(new Geodetic3D(ellipsoidPosition),               // Geodetic3D* position,
                                                 AltitudeMode::ABSOLUTE,                          // AltitudeMode altitudeMode
                                                 g3mWidget->getG3MContext()->getPlanet(),         // const Planet* planet,
                                                 URL("file:///Track_A-Sphere-70-2048x2048.png"),  // const URL& textureURL,
                                                 Vector3D(size, size, size),                      // const Vector3D& radius
                                                 24,                                              // short resolution
                                                 0,                                               // float borderWidth
                                                 true,                                            // bool texturedInside
                                                 false,                                           // bool mercator
                                                 false                                            // bool withNormals = true
                                                 );

  ellipsoid->setDepthTest(false);
  ellipsoid->setCullFace(true);
  ellipsoid->setCulledFace(GLCullFace::back());

  model->getShapesRenderer()->addShape(ellipsoid);

  const double scale                     = 1.0;
  const double maxScale                  = 2;
  const double lineWidthRatio            = 0.01;
  const double headLengthRatio           = 0.05;
  const double headWidthRatio            = 2.0;
  const double scaleArrowLengthSizeRatio = 0.15;

  _gizmo = TranslateScaleGizmo::translateAndScale(context->getPlanet()->getCoordinateSystemAt(ellipsoidPosition),
                                                  size,
                                                  scale,
                                                  maxScale,
                                                  lineWidthRatio,
                                                  headLengthRatio,
                                                  headWidthRatio,
                                                  scaleArrowLengthSizeRatio);

  _gizmo->setListener(new GizmoListener(ellipsoid, context->getPlanet()));

  model->getCompositeRenderer()->addRenderer(_gizmo);
}

void G3MTranslateScaleGizmoDemoScene::deactivate(const G3MContext* context) {
  getModel()->getCompositeRenderer()->removeRenderer(_gizmo);
  _gizmo = NULL;

  G3MDemoScene::deactivate(context);
}
