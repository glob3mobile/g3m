//
//  G3MOSMBuildingsDemoScene.cpp
//  G3MApp
//
//  Created by Pratik Prakash on 3/26/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include "G3MOSMBuildingsDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/IJSONParser.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/JSONArray.hpp>
#include <G3MiOSSDK/JSONNumber.hpp>
#include <G3MiOSSDK/Mark.hpp>
#include <G3MiOSSDK/Geodetic3D.hpp>
#include <G3MiOSSDK/IStringUtils.hpp>
#include <G3MiOSSDK/MarksRenderer.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <math.h>
#include <stdio.h>

#include "G3MDemoModel.hpp"


class G3MOSMBuildingsDemoScene_BufferDownloadListener : public IBufferDownloadListener {
private:
    G3MOSMBuildingsDemoScene* _scene;
    std::string iconURL = "http://files.softicons.com/download/toolbar-icons/fatcow-hosting-icons-by-fatcow/png/16/building.png";
public:
    G3MOSMBuildingsDemoScene_BufferDownloadListener(G3MOSMBuildingsDemoScene* scene) :
    _scene(scene)
    {
    }
    
    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired) {
        
        //Create the JSON parser
        IJSONParser* jParser = IJSONParser::instance();
        const JSONBaseObject* jsonBuildingData = jParser->parse(buffer);
        
        //Create a list of Geodetic
        std::vector<Geodetic3D*> coords;
        
        Geodetic3D c = Geodetic3D::fromDegrees(40.782, -73.980, 1000);
        coords.push_back(&c);
        //Gets the type field from the JSON building data
        const JSONObject* buildings = jsonBuildingData->asObject();
        const JSONArray* features = buildings->getAsArray("features");
        for (int i=0; i < features->size(); i++) {
            const JSONObject* feature = features->getAsObject(i);
            const JSONObject* geometry = feature->getAsObject("geometry");
            const JSONArray* coordArray = geometry->getAsArray("coordinates");
            
            double averageLon = 0;
            double averageLat = 0;
            //TODO: get all the coordinates in geometry. We are getting the average instead.
            
            for (int j = 0; j < coordArray->size(); j++) {
                double lon = coordArray->getAsArray(0)->getAsArray(j)->getAsNumber(0, 0);
                double lat = coordArray->getAsArray(0)->getAsArray(j)->getAsNumber(1, 0);
                averageLon += lon;
                averageLat += lat;
            }
            double height = 0;
            averageLon /= coordArray->size();
            averageLat /= coordArray->size();
            Geodetic3D tempCoord = Geodetic3D::fromDegrees(averageLat, averageLon, height);
            
            //Create and add the mark
            URL iconurl = URL::URL(iconURL);
            double minDistanceToCamera = 0;
            MarkUserData* userData = new MarkUserData::MarkUserData();
            bool autoDeleteUserData = false;
            MarkTouchListener* marksListener = NULL;
            bool autoDeleteListener = false;
            Mark* mark = new Mark(iconurl, tempCoord, ABSOLUTE, minDistanceToCamera, userData, autoDeleteUserData, marksListener, autoDeleteListener);
            
            _scene->addMark(mark);
            //TODO finish parsing all the other fields from building data
            
        }
        
        //Freeing memory allocations
        delete jsonBuildingData;
        delete jParser;
        delete buffer;
    }
    
    void onError(const URL& url) {
        ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());
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

void G3MOSMBuildingsDemoScene::addMark(Mark* mark) {
    getModel()->getMarksRenderer()->addMark(mark);
}

void G3MOSMBuildingsDemoScene::rawActivate(const G3MContext* context) {
    
    //Used for downloader->requestBuffer call
    bool readExpired = true;
    bool deleteListener = true;
    
    G3MDemoModel* model     = getModel();
    G3MWidget*    g3mWidget = model->getG3MWidget();
    
    g3mWidget->setBackgroundColor(Color::fromRGBA(0.9f, 0.21f, 0.21f, 1.0f));
    
    BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                             "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                             TimeInterval::fromDays(30));
    model->getLayerSet()->addLayer(layer);
    
    IDownloader* downloader = context->getDownloader();
    
    //This URL is temporary and will eventually be generalized for any x,y,z
    _requestId = downloader->requestBuffer(URL(getURLFromTile(G3MOSMBuildingsDemoScene::row, G3MOSMBuildingsDemoScene::col
                                                              , G3MOSMBuildingsDemoScene::level)),
                                           DownloadPriority::HIGHEST,
                                           TimeInterval::fromHours(1),
                                           readExpired,
                                           new G3MOSMBuildingsDemoScene_BufferDownloadListener(this),
                                           deleteListener);
    
    //Positioning the camera close to New York because of the request buffer URL.
    //TODO change the positioning and the URL when needed
    g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(40, -73, 10000),
                                         Angle::zero(), // heading
                                         Angle::fromDegrees(30 - 90) // pitch
                                         );
}

void G3MOSMBuildingsDemoScene::deactivate(const G3MContext* context) {
    context->getDownloader()->cancelRequest(_requestId);
    
    G3MDemoScene::deactivate(context);
}

void G3MOSMBuildingsDemoScene::rawSelectOption(const std::string& option,
                                               int optionIndex) {
    
}

/*
 * We won't finish implementing this function until we find a use for it.
 *
 * get2DCoordsFromTile: gets the latitude and longitude from the tile index
 
 Geodetic2D* G3MOSMBuildingsDemoScene::get2DCoordsFromTile(int xIndex, int yIndex, int zoom) {
 //Checks for valid tile data
 if(xIndex < 0 || yIndex < 0 || zoom > 19 || zoom < 0) {
 return NULL;
 }
 double lon = 360*(xIndex/(1<<zoom)) - 180;
 }
 */

/*
 * Formatting the OSM Url to get buildings for a specific tile (row, column, zoom)
 */
std::string G3MOSMBuildingsDemoScene::getURLFromTile(int xIndex, int yIndex, int zoom) {
    char charbuf[2048]; //Standard length of URL
    std::sprintf(charbuf,  G3MOSMBuildingsDemoScene::url.c_str(), G3MOSMBuildingsDemoScene::dataKey.c_str(), zoom, xIndex, yIndex);
    std::string buf = charbuf;
    return buf;
}

/*
 * This is the x-coordinate of the tile. Longitude must be in degrees.
 */
int G3MOSMBuildingsDemoScene::getTileRowFrom2DCoords(double lon, int zoom) {
    double xTile = floor((lon + 180) / 360 * (1 << zoom));
    return xTile;
}

/*
 * This is the y-coordinate of the tile. Latitude must be in degrees.
 */
int G3MOSMBuildingsDemoScene::getTileColFrom2DCoords(double lat, int zoom) {
    double yTile = floor((1 - log10(tan(lat * PI/180 + 1) / cos(lat * PI/180)) / PI) / 2 * (1 << zoom));
    return yTile;
}

