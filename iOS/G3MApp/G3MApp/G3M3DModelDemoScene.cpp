//
//  G3M3DModelDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3M3DModelDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/ShapesRenderer.hpp>
#include <G3MiOSSDK/SGShape.hpp>
#include "G3MDemoModel.hpp"

void G3M3DModelDemoScene::rawSelectOption(const std::string& option,
                                          int optionIndex) {

}

class G3M3DModelDemoScene_ShapeLoadListener : public ShapeLoadListener {
private:
  G3M3DModelDemoScene* _scene;
public:
  G3M3DModelDemoScene_ShapeLoadListener(G3M3DModelDemoScene* scene) :
  _scene(scene)
  {
  }

  void onBeforeAddShape(SGShape* shape) {
    shape->setScale(200);
    shape->setPitch(Angle::fromDegrees(90));
  }

  void onAfterAddShape(SGShape* shape) {
    shape->setAnimatedPosition(TimeInterval::fromSeconds(26),
                               Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                          Angle::fromDegreesMinutesSeconds(-78, 2, 10.92),
                                          10000),
                               true);

    const double fromDistance = 75000;
    const double toDistance   = 18750;

    const Angle fromAzimuth = Angle::fromDegrees(-90);
    const Angle toAzimuth   = Angle::fromDegrees(270);

    const Angle fromAltitude = Angle::fromDegrees(90);
    const Angle toAltitude   = Angle::fromDegrees(15);

    shape->orbitCamera(TimeInterval::fromSeconds(20),
                       fromDistance, toDistance,
                       fromAzimuth, toAzimuth,
                       fromAltitude, toAltitude);
  }
};

void G3M3DModelDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  g3mWidget->setBackgroundColor(Color::fromRGBA255(175, 221, 233, 255));

  MapBoxLayer* layer = new MapBoxLayer("examples.map-m0t0lrpu",
                                       TimeInterval::fromDays(30),
                                       true,
                                       2);

  model->getLayerSet()->addLayer(layer);


  model->getShapesRenderer()->loadBSONSceneJS(URL("file:///A320.bson"),
                                              "file:///textures-A320/",
                                              false,
                                              new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                                             Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                                             10000),
                                              ABSOLUTE,
                                              new G3M3DModelDemoScene_ShapeLoadListener(this),
                                              true);
}
