//
//  G3MJSONBuilder.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 22/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "G3MJSONBuilder.hpp"

#include "CameraConstraints.hpp"

/*#include "IStringBuilder.hpp"
#include "IMathUtils.hpp"
#include "IFactory.hpp"
#include "IJSONParser.hpp"
#include "ILogger.hpp"
#include "IStringUtils.hpp"
#include "IThreadUtils.hpp"
*/
#include "GL.hpp"

#include "SceneParser.hpp"
#include "AppParser.hpp"

#include "TextureBuilder.hpp"
#include "CPUTextureBuilder.hpp"

#include "TexturesHandler.hpp"

#include "Planet.hpp"

#include "Renderer.hpp"

#include "Effects.hpp"
#include "FrameTasksExecutor.hpp"

#include "BusyMeshRenderer.hpp"
#include "GTask.hpp"

#include "MarksRenderer.hpp"
#include "IDownloader.hpp"
#include "URL.hpp"

#include "MultiLayerTileTexturizer.hpp"
#include "EllipsoidalTileTessellator.hpp"
#include "TileTexturizer.hpp"
#include "TileRenderer.hpp"

#include "G3MWidget.hpp"

//class SceneJSONDownloadListener : public IBufferDownloadListener {
//    
//    LayerSet* _layerSet;
//    CompositeRenderer* _composite;
//    TilesRenderParameters* _parameters;
//    std::vector<Renderer*> _renderers;
//    G3MJSONBuilder* _g3mJsonBuilder;
//    
//public:
//    SceneJSONDownloadListener(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*> renderers, G3MJSONBuilder* g3mJSonBuilder);
//    
//    void onDownload(const URL& url,
//                    const IByteBuffer* buffer);
//    
//    void onError(const URL& url){
//        ILogger::instance()->logError("The requested scene file could not be found!");    }
//    
//    void onCancel(const URL& url){}
//    void onCanceledDownload(const URL& url,
//                            const IByteBuffer* data) {}
//    
//    ~SceneJSONDownloadListener(){}
//};
//
//SceneJSONDownloadListener::SceneJSONDownloadListener(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*> renderers, G3MJSONBuilder* g3mJSonBuilder){
//    _layerSet = layerSet;
//    _composite = composite;
//    _parameters = parameters;
//    _renderers = renderers;
//    _g3mJsonBuilder = g3mJSonBuilder;
//}
//
//    
//void SceneJSONDownloadListener::onDownload(const URL& url,
//                                               const IByteBuffer* buffer){
//    std::string string = buffer->getAsString();
//    _g3mJsonBuilder->fromSceneJSON(string, _composite, _layerSet, _parameters, _renderers);
//}



//void G3MJSONBuilder::initG3MJSONBuilder(std::vector<ICameraConstrainer*> cameraConstraints, std::vector<Renderer*> renderers, UserData* userData, GTask* initializationTask, std::vector<PeriodicalTask*> periodicalTasks){
//    
//    LayerSet* layerSet = new LayerSet();
//    
//    SceneParser::instance()->parse(layerSet, _jsonSource);
//    initWidgetWithCameraConstraints(cameraConstraints, layerSet, renderers, userData, initializationTask, periodicalTasks);
//
//}


//G3MJSONBuilder::G3MJSONBuilder(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*> renderers, IDownloader* downloader, const URL jsonURL){
//    downloader->requestBuffer(jsonURL, 100000000L, new SceneJSONDownloadListener(composite, layerSet, parameters, renderers, this), true);
//}

//G3MJSONBuilder::G3MJSONBuilder(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*>* renderers, IDownloader* downloader, const std::string jsonSource){
//    fromSceneJSON(jsonSource, composite, layerSet, parameters, renderers, downloader);
//}

void G3MJSONBuilder::fromSceneJSON(CompositeRenderer* composite, LayerSet* layerSet, TilesRenderParameters* parameters, std::vector<Renderer*>* renderers, IDownloader* downloader){

    SceneParser::instance()->parse(layerSet, downloader, renderers, _jsonSource);
    
    if (layerSet != NULL) {
        TileTexturizer* texturizer = new MultiLayerTileTexturizer();
        
        const bool showStatistics = false;
        TileRenderer* tr = new TileRenderer(new EllipsoidalTileTessellator(parameters->_tileResolution, true),
                                            texturizer,
                                            layerSet,
                                            parameters,
                                            showStatistics);
        composite->addRenderer(tr);
    }
}
