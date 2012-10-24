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
public:   
    
    G3MJSONBuilder(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*> renderers, IDownloader* downloader, const URL jsonURL);
    
    G3MJSONBuilder(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*> renderers, const std::string jsonSource);
    
    void fromSceneJSON(const std::string json, CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*> renderers);
    
    ~G3MJSONBuilder();

};



#endif
