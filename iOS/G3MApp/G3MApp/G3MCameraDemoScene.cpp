//
//  G3MCameraDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MCameraDemoScene.hpp"

#include <G3MiOSSDK/ILogger.hpp>
#include <G3MiOSSDK/Angle.hpp>
#include <G3MiOSSDK/TimeInterval.hpp>
#include <G3MiOSSDK/ShapesRenderer.hpp>
#include <G3MiOSSDK/SGShape.hpp>
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>

#include "G3MDemoModel.hpp"

class G3MCameraDemoSceneShapeLoadListener : public ShapeLoadListener {
protected:
  G3MCameraDemoScene* _scene;

  G3MCameraDemoSceneShapeLoadListener(G3MCameraDemoScene* scene) :
  _scene(scene)
  {
  }
};

class ThePhynxShapeLoadListener : public G3MCameraDemoSceneShapeLoadListener {
public:
  ThePhynxShapeLoadListener(G3MCameraDemoScene* scene) :
  G3MCameraDemoSceneShapeLoadListener(scene)
  {
  }

  void onBeforeAddShape(SGShape* shape) {
    shape->setPitch(Angle::fromDegrees(90));
  }

  void onAfterAddShape(SGShape* shape) {
    _scene->setThePhynxShape(shape);
  }
};

class TheEiffelTowerShapeLoadListener : public G3MCameraDemoSceneShapeLoadListener {
public:
  TheEiffelTowerShapeLoadListener(G3MCameraDemoScene* scene) :
  G3MCameraDemoSceneShapeLoadListener(scene)
  {
  }

  void onBeforeAddShape(SGShape* shape) {
    shape->setScale(0.02);
  }

  void onAfterAddShape(SGShape* shape) {
    _scene->setTheEiffelTowerShape(shape);
  }
};

class ArcDeTriompheShapeLoadListener : public G3MCameraDemoSceneShapeLoadListener {
public:
  ArcDeTriompheShapeLoadListener(G3MCameraDemoScene* scene) :
  G3MCameraDemoSceneShapeLoadListener(scene)
  {
  }

  void onBeforeAddShape(SGShape* shape) {
    shape->setScale(0.5);
    shape->setPitch(Angle::fromDegrees(90));
  }

  void onAfterAddShape(SGShape* shape) {
    _scene->setArcDeTriompheShape(shape);
  }
};

void G3MCameraDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel*   model          = getModel();
  ShapesRenderer* shapesRenderer = model->getShapesRenderer();

  MapBoxLayer* layer = new MapBoxLayer("examples.map-m0t0lrpu",
                                       TimeInterval::fromDays(30),
                                       true,
                                       2);
  model->getLayerSet()->addLayer(layer);


  shapesRenderer->loadBSONSceneJS(URL("file:///sphinx.bson"),
                                  "file:///",
                                  false, // isTransparent
                                  new Geodetic3D(Angle::fromDegreesMinutesSeconds(29, 58, 30.99),
                                                 Angle::fromDegreesMinutesSeconds(31, 8, 15.84),
                                                 0),
                                  RELATIVE_TO_GROUND,
                                  new ThePhynxShapeLoadListener(this));

  shapesRenderer->loadBSONSceneJS(URL("file:///eifeltower.bson"),
                                  "file:///eifel/",
                                  true, // isTransparent
                                  new Geodetic3D(Angle::fromDegreesMinutesSeconds(48, 51, 29.06),
                                                 Angle::fromDegreesMinutesSeconds(2, 17, 40.48),
                                                 0), //
                                  RELATIVE_TO_GROUND,
                                  new TheEiffelTowerShapeLoadListener(this));

  shapesRenderer->loadBSONSceneJS(URL("file:///arcdeTriomphe.bson"),
                                  "file:///arc/",
                                  false,
                                  new Geodetic3D(Angle::fromDegreesMinutesSeconds(48, 52, 25.58),
                                                 Angle::fromDegreesMinutesSeconds(2, 17, 42.12),
                                                 0),
                                  RELATIVE_TO_GROUND,
                                  new ArcDeTriompheShapeLoadListener(this));
}

void G3MCameraDemoScene::rawSelectOption(const std::string& option,
                                         int optionIndex) {
  if (option == "The Phynx") {
    if (_thePhynxShape != NULL) {
      const double fromDistance = 6000;
      const double toDistance = 2000;

      const Angle fromAzimuth = Angle::fromDegrees(-90);
      const Angle toAzimuth   = Angle::fromDegrees(45);

      const Angle fromAltitude = Angle::fromDegrees(90);
      const Angle toAltitude   = Angle::fromDegrees(30);

      _thePhynxShape->orbitCamera(TimeInterval::fromSeconds(20),
                                  fromDistance, toDistance,
                                  fromAzimuth,  toAzimuth,
                                  fromAltitude, toAltitude);
    }
  }
  else if (option == "The Eiffel Tower") {
    if (_theEiffelTowerShape != NULL) {
      const double fromDistance = 10000;
      const double toDistance = 1000;

      const Angle fromAzimuth = Angle::fromDegrees(-90);
      const Angle toAzimuth   = Angle::fromDegrees(270);

      const Angle fromAltitude = Angle::fromDegrees(90);
      const Angle toAltitude   = Angle::fromDegrees(15);

      _theEiffelTowerShape->orbitCamera(TimeInterval::fromSeconds(20),
                                        fromDistance, toDistance,
                                        fromAzimuth,  toAzimuth,
                                        fromAltitude, toAltitude);
    }
  }
  else if (option == "Arc de Triomphe") {
    if (_arcDeTriompheShape != NULL) {
      const double fromDistance = 10000;
      const double toDistance = 1000;

      const Angle fromAzimuth = Angle::fromDegrees(-90);
      const Angle toAzimuth   = Angle::fromDegrees(270);

      const Angle fromAltitude = Angle::fromDegrees(90);
      const Angle toAltitude   = Angle::fromDegrees(15);

      _arcDeTriompheShape->orbitCamera(TimeInterval::fromSeconds(20),
                                       fromDistance, toDistance,
                                       fromAzimuth,  toAzimuth,
                                       fromAltitude, toAltitude);
    }
  }
  else {
    ILogger::instance()->logError("option \"%s\" not supported", option.c_str());
  }
}
