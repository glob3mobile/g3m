//
//  G3MIsosurfaceDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/19/13.
//

#include "G3MIsosurfaceDemoScene.hpp"

#include <G3M/BingMapsLayer.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/MeshRenderer.hpp>
#include <G3M/G3MWidget.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/Color.hpp>

#include "G3MDemoModel.hpp"

class G3MIsosurfaceDemoScene_MeshLoadListener : public MeshLoadListener {
private:
  G3MWidget* _g3mWidget;
public:
  G3MIsosurfaceDemoScene_MeshLoadListener(G3MWidget* g3mWidget) :
  _g3mWidget(g3mWidget)
  {
  }
  
  void onError(const URL& url) {
  }

  void onBeforeAddMesh(Mesh* mesh) {
  }

  void onAfterAddMesh(Mesh* mesh) {
    _g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(31.61, -97.83, 281177),
                                          Angle::zero(),
                                          Angle::fromDegrees(-45));
  }
};

void G3MIsosurfaceDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  MeshRenderer* meshRenderer = model->getMeshRenderer();
  meshRenderer->loadJSONMesh(URL("file:///isosurface-mesh.json"),
                             Color::newFromRGBA(1, 1, 0, 1),
                             new G3MIsosurfaceDemoScene_MeshLoadListener(g3mWidget),
                             true);
}
