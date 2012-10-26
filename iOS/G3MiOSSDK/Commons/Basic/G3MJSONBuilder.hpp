//
//  G3MJSONBuilder.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 22/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_G3MJSONBuilder_hpp
#define G3MiOSSDK_G3MJSONBuilder_hpp

#include "G3MWidget.hpp"
#include "CameraRenderer.hpp"
#include "LayerSet.hpp"
#include "TilesRenderParameters.hpp"
#include "URL.hpp"
#include "INativeGL.hpp"
#include "IStorage.hpp"
#include "IBufferDownloadListener.hpp"

#include "CompositeRenderer.hpp"


class G3MJSONBuilder {
     std::string _jsonSource;
public:   
    
    G3MJSONBuilder(const std::string jsonSource){_jsonSource = jsonSource;};
//    
//    void initG3MJSONBuilder(std::vector<ICameraConstrainer*> cameraConstraints, std::vector<Renderer*> renderers, UserData* userData, GTask* initializationTask, std::vector<PeriodicalTask*> periodicalTasks);
    
//    G3MJSONBuilder(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*> renderers, IDownloader* downloader, const URL jsonURL);
//    
//    G3MJSONBuilder(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*>* renderers, IDownloader* downloader, const std::string jsonSource);
    
    void fromSceneJSON(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*>* renderers, IDownloader* downloader);
    
    ~G3MJSONBuilder();

};



#endif
