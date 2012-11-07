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

#include "IStorage.hpp"
#include "IDownloader.hpp"

G3MJSONBuilder_iOS::G3MJSONBuilder_iOS(std::string jsonSource, G3MWidget_iOS* g3mWidget): IG3MJSONBuilder(jsonSource), _g3mWidget(g3mWidget) {};


void  G3MJSONBuilder_iOS::initWidgetWithCameraConstraints (std::vector<ICameraConstrainer*> cameraConstraints,  LayerSet* layerSet, bool incrementalTileQuality, std::vector<Renderer*> renderers, UserData* userData, GTask* initializationTask, std::vector<PeriodicalTask*> periodicalTasks, MarkTouchListener* markTouchListener){
    
    SceneParser::instance()->parse(layerSet, IDownloader::instance(), &renderers, _jsonSource, markTouchListener);
    
    [_g3mWidget initWidgetWithCameraConstraints: cameraConstraints
                                             layerSet: layerSet
                               incrementalTileQuality: incrementalTileQuality
                                            renderers: renderers
                                             userData: userData
                                   initializationTask: initializationTask
                                      periodicalTasks: periodicalTasks];

}