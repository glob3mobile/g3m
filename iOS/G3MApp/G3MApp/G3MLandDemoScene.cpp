//
//  G3MLandDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/4/16.
//

#include "G3MLandDemoScene.hpp"

#include "G3MDemoModel.hpp"

#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/URLTemplateLayer.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/BILDownloader.hpp>
#include <G3MiOSSDK/URL.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>

void G3MLandDemoScene::rawSelectOption(const std::string& option,
                                       int optionIndex) {
  // no options
}


class G3MLandDemoSceneBILHandler : public BILDownloader::Handler {

};

void G3MLandDemoScene::rawActivate(const G3MContext* context) {
#warning Diego at work!

  PlanetRenderer* planetRenderer = getModel()->getPlanetRenderer();
  //  planetRenderer->setShowStatistics(true);
  planetRenderer->setIncrementalTileQuality(true);


  // https://mapzen.com/blog/elevation/
  URLTemplateLayer* layer = URLTemplateLayer::newMercator("https://terrain-preview.mapzen.com/normal/{z}/{x}/{y}.png",
                                                          Sector::FULL_SPHERE,
                                                          false,                     // isTransparent
                                                          2,                         // firstLevel
                                                          15,                        // maxLevel
                                                          TimeInterval::fromDays(30) // timeToCache
                                                          );

  getModel()->getLayerSet()->addLayer( layer );

  

  const double deltaHeight = -700.905;
  const short  noDataValue = -32768;
  BILDownloader::request(context,
                         URL("file:///0576.bil"),
                         DownloadPriority::MEDIUM,
                         TimeInterval::fromDays(0),
                         false,  // readExpired
                         Sector::fromDegrees(40.1665739916489, -5.85449532145337,
                                             40.3320215899527, -5.5116079822178570),
                         Vector2I(2516, 1335),
                         deltaHeight,
                         noDataValue,
                         new G3MLandDemoSceneBILHandler(),
                         true);
}

