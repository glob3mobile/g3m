//
//  G3MJSONBuilder.mm
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 30/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "G3MJSONBuilder_iOS.hpp"
#include "SceneParser.hpp"
#include "SQLiteStorage_iOS.hpp"
#include "CachedDownloader.hpp"
#include "Downloader_iOS.hpp"
#include "MarksRenderer.hpp"
#include "GEOJSONDownloadListener.hpp"

#include "IStorage.hpp"
#include "IDownloader.hpp"

G3MJSONBuilder_iOS::G3MJSONBuilder_iOS(std::string jsonSource, G3MWidget_iOS* g3mWidget): IG3MJSONBuilder(jsonSource), _g3mWidget(g3mWidget) {
};


void  G3MJSONBuilder_iOS::initWidgetWithCameraConstraints (std::vector<ICameraConstrainer*> cameraConstraints,  LayerSet* layerSet, bool incrementalTileQuality, std::vector<Renderer*> renderers, UserData* userData, GTask* initializationTask, std::vector<PeriodicalTask*> periodicalTasks, MarkTouchListener* markTouchListener){
    
    const bool readyWhenMarksReady = false;
    MarksRenderer* marksRenderer = new MarksRenderer(readyWhenMarksReady);
    
    if (markTouchListener != NULL) {
        marksRenderer->setMarkTouchListener(markTouchListener, true);
    }
    renderers.push_back(marksRenderer);
    
    SceneParser::instance()->parse(layerSet, _jsonSource);
    
    class CustomInitializationTask : public GTask {
    private:
        MarksRenderer* _marksRenderer;
        std::map<std::string, std::string> _mapGeoJSONSources;
    public:
        CustomInitializationTask(MarksRenderer* marksRenderer){
            _marksRenderer = marksRenderer;
            _mapGeoJSONSources = SceneParser::instance()->getMapGeoJSONSources();
        }
        
        void run() {
            printf("Running CustomInitializationTask\n");
            
            if(!_mapGeoJSONSources.empty()){
                for (std::map<std::string, std::string>::iterator it=_mapGeoJSONSources.begin(); it!=_mapGeoJSONSources.end(); it++){
                    IDownloader::instance()->requestBuffer(URL(it->first, false), 100000000L, new GEOJSONDownloadListener(_marksRenderer, it->second), true);
                }            
            }
        }
    };
    
    
    [_g3mWidget initWidgetWithCameraConstraints: cameraConstraints
                                       layerSet: layerSet
                         incrementalTileQuality: incrementalTileQuality
                                      renderers: renderers
                                       userData: userData
                             initializationTask: new CustomInitializationTask(marksRenderer)
                                periodicalTasks: periodicalTasks];
    
}