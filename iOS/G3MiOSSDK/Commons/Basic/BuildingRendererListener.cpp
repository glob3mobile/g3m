//
//  BuildingRendererListener.cpp
//  G3MiOSSDK
//
//  Created by Pratik Prakash on 2/27/15.
//
//

#include "BuildingRendererListener.h"
#include "Tile.hpp"

void BuildingRendererListener::changedTilesRendering(const std::vector<const Tile *> *tilesStartedRendering, const std::vector<std::string> *tilesStoppedRendering) {

    //Probably stating these are the tiles that have started rendering, pls get the building data or something?
    if (tilesStartedRendering != NULL) {
        //Starts the rendering of the building tiles
        for (int i = 0; i < tilesStartedRendering->size(); i++) {
            //Not sure what to do here because the tile class doesn't render itself.
            break;
        }
    }
    
    if (tilesStoppedRendering != NULL) {
        //Stops the rendering of the building tiles
        for (int i = 0; i < tilesStoppedRendering->size(); i++) {
            //tilesStoppedRendering->at(j)
            break;
        }
    }
    
    return;
}
