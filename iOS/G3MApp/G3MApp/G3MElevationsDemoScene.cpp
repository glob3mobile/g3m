//
//  G3MElevationsDemoScene.cpp
//  G3MApp
//
//  Created by Sebastian Ortega Trujillo on 10/3/16.
//

#include "G3MElevationsDemoScene.hpp"
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/PyramidElevationDataProvider.hpp>
#include "G3MDemoModel.hpp"

void G3MElevationsDemoScene::rawActivate(const G3MContext* context) {
    G3MDemoModel* model     = getModel();
    BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                             "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                             TimeInterval::fromDays(30));
    
    model->getLayerSet()->addLayer(layer);
    
}

void G3MElevationsDemoScene::rawSelectOption(const std::string& option,
                     int optionIndex) {
    switch (optionIndex){
        case 0:
            loadElevs("http://193.145.147.50:8080/DemoElevs/elevs/fix-16/",Sector::fullSphere(),2.0f);
            break;
        case 1:
            loadElevs("http://193.145.147.50:8080/DemoElevs/elevs/var-16/",Sector::fullSphere(),2.0f);
            break;
        case 2:
            loadElevs("http://193.145.147.50:8080/DemoElevs/elevs/fix-16/",Sector::fromDegrees(34,-10,70,52),2.0f);
            break;
        case 3:
            loadElevs("http://193.145.147.50:8080/DemoElevs/elevs/fix-16/",Sector::fromDegrees(34,-10,70,52),2.0f);
            break;
    }
}


void G3MElevationsDemoScene::loadElevs(std::string layerServer,const Sector &layerSector, float vertEx){
    G3MDemoModel* model     = getModel();
    
    PlanetRenderer* planetRenderer = model->getPlanetRenderer();
    planetRenderer->setVerticalExaggeration(vertEx);

    planetRenderer->setElevationDataProvider(new PyramidElevationDataProvider(layerServer,layerSector), true);

}