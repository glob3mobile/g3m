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
#include <G3MiOSSDK/Box.hpp>
#include <G3MiOSSDK/BoxShape.hpp>
#include <G3MiOSSDK/Shape.hpp>
#include <G3MiOSSDK/Mesh.hpp>
#include <G3MiOSSDK/Color.hpp>
#include <G3MiOSSDK/Geodetic3D.hpp>
#include <G3MiOSSDK/IStringUtils.hpp>
#include <G3MiOSSDK/MarksRenderer.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>
#include <G3MiOSSDK/ShapesRenderer.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <math.h>
#include <stdio.h>

#include "G3MDemoModel.hpp"

#define METERS_PER_LEVEL 3
#define DEFAULT_HEIGHT 0
#define DEFAULT_LEVEL 0

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
            
            //Parse for height and level
            const JSONObject* properties = feature->getAsObject("properties");
            double height = DEFAULT_HEIGHT; //Set the height to default
            double level = DEFAULT_LEVEL;   //Set the level to default
            if (properties != NULL) {
                height = properties->getAsNumber("height", DEFAULT_HEIGHT);
                level = properties->getAsNumber("levels", DEFAULT_LEVEL);
            }
            if (height == DEFAULT_HEIGHT && level != DEFAULT_LEVEL) {
                height = level * METERS_PER_LEVEL;
            }
            
            //Parse for latitute and longitude
            const JSONObject* geometry = feature->getAsObject("geometry");
            const JSONArray* coordArray = geometry->getAsArray("coordinates");
            
            double averageLon = 0;
            double averageLat = 0;
            double minLat = 0;
            double minLon = 0;
            double maxLat = 0;
            double maxLon = 0;
            
            if (coordArray->size() > 0) {
                minLat = coordArray->getAsArray(0)->getAsArray(0)->getAsNumber(1, 0);
                maxLat = coordArray->getAsArray(0)->getAsArray(0)->getAsNumber(1, 0);
                minLon = coordArray->getAsArray(0)->getAsArray(0)->getAsNumber(0, 0);
                maxLon = coordArray->getAsArray(0)->getAsArray(0)->getAsNumber(0, 0);
            }
            //TODO: get all the coordinates in geometry. We are getting the average instead.
            
            for (int j = 0; j < coordArray->size(); j++) {
                double lon = coordArray->getAsArray(0)->getAsArray(j)->getAsNumber(0, 0);
                double lat = coordArray->getAsArray(0)->getAsArray(j)->getAsNumber(1, 0);
                averageLon += lon;
                averageLat += lat;
                
                // update min/max lat/lon if necessary
                if (lon > maxLon) {
                    maxLon = lon;
                }
                if (lon < minLon) {
                    minLon = lon;
                }
                if (lat > maxLat) {
                    maxLat = lat;
                }
                if (lat < minLat) {
                    minLat = lat;
                }
            }
            averageLon /= coordArray->size();
            averageLat /= coordArray->size();
            
            Geodetic3D tempCoord = Geodetic3D::fromDegrees(averageLat, averageLon, height);
            Geodetic3D* buildingCenterBottom = new Geodetic3D(Angle::fromDegrees(averageLat),Angle::fromDegrees(averageLon), height/2);
            
            //Create and add the mark
            URL iconurl = URL::URL(iconURL);
            double minDistanceToCamera = 0;
            MarkUserData* userData = new MarkUserData::MarkUserData();
            bool autoDeleteUserData = false;
            MarkTouchListener* marksListener = NULL;
            bool autoDeleteListener = false;
            Mark* mark = new Mark(iconurl, tempCoord, ABSOLUTE, minDistanceToCamera, userData, autoDeleteUserData, marksListener, autoDeleteListener);
            
            // creating bounding box for building
            double absXExtent = 0;
            double absYExtent = 0;
            
            if (maxLon < 0) {
                maxLon = maxLon * -1;
            }
            if (maxLat < 0) {
                maxLat = maxLat * -1;
            }
            if (minLon < 0) {
                minLon = minLon * -1;
            }
            if (minLat < 0) {
                minLat = minLat * -1;
            }
            
            if (maxLon < minLon) {
                absXExtent = minLon - maxLon;
            }
            else {
                absXExtent = maxLon - minLon;
            }
            if (maxLat < minLat) {
                absYExtent = minLat - maxLat;
            }
            else {
                absYExtent = maxLat - minLat;
            }
            
            double x_extent = (absXExtent+0.0001) * 80000;
            double y_extent = (absYExtent+0.0001) * 80000;
            double z_extent = (abs(height) + 1);
            // setting some BoxShape constants
            float borderWidth = 2;
            bool useNormals = true;
            
            if (z_extent == 1) {
                height = 20; //TODO we should be making this the average height but we are hardcoding the value right now.
            }
            
            BoxShape* bs = new BoxShape(buildingCenterBottom,
                                        RELATIVE_TO_GROUND,
                                        Vector3D(x_extent, y_extent, z_extent),
                                        borderWidth,
                                        Color::fromRGBA(0.99f, 0.8f, 0.08f, 1.0f),
                                        Color::newFromRGBA(0.35f, 0.28f, 0.03f, 1.0f),
                                        useNormals);
            
            // Adding box to the demo scene
            //if (x_extent > 0 && y_extent > 0 && z_extent > 0) {
                _scene->addShape(bs);
               // _scene->addMark(mark);
            //}
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

void G3MOSMBuildingsDemoScene::addMesh(Mesh* mesh) {
    getModel()->getMeshRenderer()->addMesh(mesh);
}

void G3MOSMBuildingsDemoScene::addShape(Shape* shape) {
    getModel()->getShapesRenderer()->addShape(shape);
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
    g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(40.747930906661231631, -73.977181666542492167, 10000),
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

