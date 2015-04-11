//
//  BuildingRendererListener.h
//  G3MiOSSDK
//
//  Created by Pratik Prakash on 2/27/15.
//
//

#ifndef __G3MiOSSDK__BuildingRendererListener__
#define __G3MiOSSDK__BuildingRendererListener__

#include <stdio.h>
#include <vector>
#include "TileRenderingListener.hpp"

class Tile;
/*
 * BuildingRendererListener listens for when a building should be rendered or removed. It extends the abstract listener tileRendereringListener
 */
class BuildingRendererListener : public TileRenderingListener {
public:
    virtual ~BuildingRendererListener() {
    }
    
    /*
     * Starts and stops the rendering of the given tiles
     */
    void changedTilesRendering(
                               const std::vector<const Tile*>* tilesStartedRendering,
                               const std::vector<std::string>* tilesStoppedRendering
                               );
};


#endif /* defined(__G3MiOSSDK__BuildingRendererListener__) */
