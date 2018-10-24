//
//  G3MExtrusionDemoScene.cpp
//  G3MApp
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/24/18.
//  Copyright © 2018 Igo Software SL. All rights reserved.
//

#include "G3MExtrusionDemoScene.hpp"

#include "G3MDemoModel.hpp"

#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/Geodetic3D.hpp>
#include <G3MiOSSDK/OSMLayer.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/IJSONParser.hpp>
#include <G3MiOSSDK/G3MMeshParser.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>
#include <G3MiOSSDK/URL.hpp>
#include <G3MiOSSDK/G3MContext.hpp>
#include <G3MiOSSDK/IDownloader.hpp>


void G3MExtrusionDemoScene::rawSelectOption(const std::string& option,
                                            int optionIndex) {
  // no options
}

class G3MeshBufferDownloadListener : public IBufferDownloadListener {
  const Planet* _planet;
  MeshRenderer* _meshRenderer;

public:
  G3MeshBufferDownloadListener(const Planet* planet,
                               MeshRenderer* meshRenderer) :
  _planet(planet),
  _meshRenderer(meshRenderer)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    const JSONBaseObject* jsonObject = IJSONParser::instance()->parse(buffer);
    std::vector<Mesh*> meshes = G3MMeshParser::parse(jsonObject->asObject(), _planet);
    const size_t meshesSize = meshes.size();
    for (size_t i = 0; i < meshesSize; i++) {
      _meshRenderer->addMesh( meshes[i] );
    }

    delete jsonObject;
    delete buffer;
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url._path.c_str());
  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }

};


void G3MExtrusionDemoScene::rawActivate(const G3MContext* context) {
  LayerSet* layerSet = getModel()->getLayerSet();

  OSMLayer* osmLayer = new OSMLayer(TimeInterval::fromDays(30));
  layerSet->addLayer(osmLayer);

  getModel()->getG3MWidget()->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.473307996475860193,
                                                                                 -6.37246061136657449,
                                                                                 4100) );

  context->getDownloader()->requestBuffer(URL("file:///casco_historico_3d.json"),
                                          1000000,
                                          TimeInterval::zero(),
                                          false,
                                          new G3MeshBufferDownloadListener(context->getPlanet(),
                                                                           getModel()->getMeshRenderer()),
                                          true);
}
