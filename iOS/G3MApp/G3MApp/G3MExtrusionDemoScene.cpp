//
//  G3MExtrusionDemoScene.cpp
//  G3MApp
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/24/18.
//  Copyright © 2018 Igo Software SL. All rights reserved.
//

#include "G3MExtrusionDemoScene.hpp"

#include "G3MDemoModel.hpp"

#include <G3M/LayerSet.hpp>
#include <G3M/G3MWidget.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/OSMLayer.hpp>
#include <G3M/IBufferDownloadListener.hpp>
#include <G3M/IJSONParser.hpp>
#include <G3M/G3MMeshParser.hpp>
#include <G3M/MeshRenderer.hpp>
#include <G3M/URL.hpp>
#include <G3M/G3MContext.hpp>
#include <G3M/IDownloader.hpp>
#include <G3M/VectorStreamingRenderer.hpp>
#include <G3M/PlanetRenderer.hpp>
#include <G3M/SingleBILElevationDataProvider.hpp>
#include <G3M/LayerTilesRenderParameters.hpp>
#include <G3M/URLTemplateLayer.hpp>
#include <G3M/BingMapsLayer.hpp>
#include <G3M/IMathUtils.hpp>
#include <G3M/OSMLayer.hpp>


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
  G3MDemoModel* model = getModel();
  G3MWidget* g3mWidget = model->getG3MWidget();

  LayerSet* layerSet = model->getLayerSet();

//    OSMLayer* osmLayer = new OSMLayer(TimeInterval::fromDays(30));
//    layerSet->addLayer(osmLayer);

  BingMapsLayer* bingMapsAerialLayer = new BingMapsLayer(BingMapType::Aerial(),
                                                         "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                                         TimeInterval::fromDays(30));
  layerSet->addLayer(bingMapsAerialLayer);
//#error diego;

//  OSMLayer* osmLayer = new OSMLayer(TimeInterval::fromDays(30));
//  layerSet->addLayer(osmLayer);

  //  LayerTilesRenderParameters* parameters = LayerTilesRenderParameters::createDefaultWGS84(Sector::FULL_SPHERE,
  //                                                                                       1, // topSectorSplitsByLatitude
  //                                                                                       2, // topSectorSplitsByLongitude
  //                                                                                       1, // firstLevel
  //                                                                                       13 // maxLevel
  //                                                                                       );
  //  URLTemplateLayer* layer = new URLTemplateLayer("http://brownietech.ddns.net/maps/s2cloudless/{z}/{y}/{x}.jpg", // urlTemplate
  //                                              Sector::FULL_SPHERE,
  //                                              false,                      // isTransparent
  //                                              TimeInterval::fromDays(30), // timeToCache
  //                                              true, // readExpired
  //                                              NULL, // condition
  //                                              parameters);
  //  layerSet->addLayer(layer);




//  const Sector demSector = Sector::fromDegrees(39.1061661306491501, -6.8652429810672828,
//                                               39.6803752050700638, -6.0079354792574549);
//
////  const Sector demSector = Sector::fromDegrees(39.3249577152747989, -6.5277029119743890,
////                                               39.5082433963135529, -6.1796950996431388);
//
//  //  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.473307996475860193,
//  //                                                             -6.37246061136657449,
//  //                                                             4100) );

//  g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(39.460438280508554953,
//                                                               -6.3478641137059907962,
//                                                               720 - 400),
//                                       Angle::fromDegrees(60), // heading
//                                       Angle::fromDegrees(-15) // pitch
//                                       );

  //  Camera position=(lat=39.460438280508554953d, lon=-6.3478641137059907962d, height=721.03182836661108013)
  //                heading=60.977841
  //  pitch=-15.509722

  //  g3mWidget->setCameraPosition( Geodetic3D::fromDegrees(39.473307996475860193,
  //                                                     -6.37246061136657449,
  //                                                     4100) );

  //  g3mWidget->setAnimatedCameraPosition( Geodetic3D(demSector._center,
  //                                                4100) );

//  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
//  planetRenderer->setVerticalExaggeration(1);

  //  g3mWidget->setBackgroundColor( Color::fromRGBA255(185, 221, 209, 255).muchDarker() );

//  Sector renderedSector = demSector.shrinkedByPercent(0.1f);
//  g3mWidget->setRenderedSector(renderedSector);

//  const double deltaHeight = -400;
//  ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///MDT_CC_250.bil"),
//                                                                                    demSector,
//                                                                                    //Vector2I(1481, 780),
//                                                                                    Vector2I(1605, 1075),
//                                                                                    deltaHeight);
//  planetRenderer->setElevationDataProvider(elevationDataProvider, true);


  // context->getDownloader()->requestBuffer(URL("file:///casco_historico_3d.json"),
  //                                         1000000,
  //                                         TimeInterval::zero(),
  //                                         false,
  //                                         new G3MeshBufferDownloadListener(context->getPlanet(),
  //                                                                          model->getMeshRenderer()),
  //                                         true);

//    g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(40.425734544513382218,
//                                                                 -3.6642964068438992342,
//                                                                 606),
//                                         Angle::fromDegrees(-4.3), // heading
//                                         Angle::fromDegrees(-41)   // pitch
//                                         );

  const double deltaHeight = 0;

  // http://brownietech.ddns.net/test/comunidad_de_madrid/metadata.json

//  https://lhind.velimo.tv/bustrack/district-of-columbia/

  VectorStreamingRenderer* renderer = model->getVectorStreamingRenderer();
  renderer->addVectorSet(//URL("http://brownietech.ddns.net/"),
                         URL("https://lhind.velimo.tv/bustrack/"),
                         //"cc3d-buildings",         // name
//                         "test/comunidad_de_madrid", // name
//                         "test/spain",             // name
//                         "test/district-of-columbia", // name
                         "district-of-columbia",   // name
                         "",                       // properties
                         NULL,                     // symbolizer
                         true,                     // deleteSymbolizer
                         DownloadPriority::HIGHER,
                         TimeInterval::fromDays(30),
                         true,                     // readExpired
                         true,                     // verbose
                         false,                    // haltOnError
                         VectorStreamingRenderer::Format::PLAIN_FILES,
                         Angle::fromDegrees(90),   // minSectorSize
                         100000,                   // minProjectedArea
                         deltaHeight,              // minHeight
                         NAND                      // maxHeight
                         );

}
