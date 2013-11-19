//
//  G3MIsosurfaceDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/19/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MIsosurfaceDemoScene.hpp"

#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>

#include "G3MDemoModel.hpp"

class G3MIsosurfaceDemoScene_MeshLoadListener : public MeshLoadListener {
private:
  G3MWidget* _g3mWidget;
public:
  G3MIsosurfaceDemoScene_MeshLoadListener(G3MWidget* g3mWidget) :
  _g3mWidget(g3mWidget)
  {
  }

  void onBeforeAddMesh(Mesh* mesh) {
  }

  void onAfterAddMesh(Mesh* mesh) {
    // Camera position=(lat=31.613874455759333415d, lon=-97.828982315232607903d, height=281177.11811743001454)
    // heading=1.784589
    // pitch=54.669069

    _g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(31.61, -97.83, 281177),
                                          Angle::zero(), // heading
                                          Angle::fromDegrees(55) // pitch
                                          );

//    _g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(30.42, -97.77, 320962),
//                                          Angle::zero(), // heading
//                                          Angle::fromDegrees(65) // pitch
//                                          );
  }
};

void G3MIsosurfaceDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  MapBoxLayer* layer = new MapBoxLayer("examples.map-m0t0lrpu",
                                       TimeInterval::fromDays(30),
                                       true,
                                       2);
  model->getLayerSet()->addLayer(layer);

  MeshRenderer* meshRenderer = model->getMeshRenderer();
  meshRenderer->loadJSONMesh(URL("file:///isosurface-mesh.json"),
                             Color::newFromRGBA(1, 1, 0, 1),
                             new G3MIsosurfaceDemoScene_MeshLoadListener(g3mWidget),
                             true);

#warning Diego at work!
}
