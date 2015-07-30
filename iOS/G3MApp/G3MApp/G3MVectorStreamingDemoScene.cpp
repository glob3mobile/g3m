//
//  G3MVectorStreamingDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 7/30/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include "G3MVectorStreamingDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/VectorStreamingRenderer.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>

#include "G3MDemoModel.hpp"


void G3MVectorStreamingDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();


  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);


  VectorStreamingRenderer* renderer = model->getVectorStreamingRenderer();
  renderer->addVectorSet(URL("http://localhost:8080/server-mapboo/public/VectorialStreaming/"),
                         "GEONames-PopulatedPlaces_LOD",
                         DownloadPriority::LOWER,
                         TimeInterval::zero(),
                         true, // readExpired
                         true // verbose
                         );


  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(46.612016780685230799, 7.8587244849714883443, 5410460) );
  
}
