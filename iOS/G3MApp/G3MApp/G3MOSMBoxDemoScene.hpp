//
//  G3MOSMBoxDemoScene.h
//  G3MApp
//
//  Created by Pratik Prakash on 4/23/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MOSMBoxDemoScene__
#define __G3MApp__G3MOSMBoxDemoScene__

#include <stdio.h>
#include "G3MDemoScene.hpp"
#include "G3MiOSSDK/Tile.hpp"
#include "G3MiOSSDK/Shape.hpp"

class Mark;

class G3MOSMBoxDemoScene : public G3MDemoScene {
private:
    long long _requestId;
    std::string url = "http://data.osmbuildings.org/0.2/%s/tile/%d/%d/%d.json";
    const std::string dataKey = "rkc8ywdl";
    
    //Hardcoding level, row and col. **TODO Later we will get this from the tile
    int level = 13;//14;
    int row = 2276;//4825;
    int col = 3089;//6156;
    
    //Geodetic2D* get2DCoordsFromTile(int xIndex, int yIndex, int zoom); **TODO: Won't implement this until we find a use for it
    std::string getURLFromTile(int xIndex, int yIndex, int zoom);
    
    //Gets the Tile's row and column given a latitude or longitude in degrees as well as a zoom level.
    int getTileRowFrom2DCoords(double lon, int zoom);
    int getTileColFrom2DCoords(double lat, int zoom);
    
    
protected:
    void rawActivate(const G3MContext* context);
    
    void rawSelectOption(const std::string& option,
                         int optionIndex);
    
public:
    
    G3MOSMBoxDemoScene(G3MDemoModel* model) :
    G3MDemoScene(model, "OSM Boxes", "", 0),
    _requestId(-1)
    {
    }
    
    void deactivate(const G3MContext* context);
    void addMesh(Mesh* mesh);
    void addShape(Shape* shape);
    
};


#endif /* defined(__G3MApp__G3MOSMBoxDemoScene__) */
