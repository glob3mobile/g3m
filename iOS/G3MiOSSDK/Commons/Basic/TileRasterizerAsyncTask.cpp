//
//  TileRasterizerAsyncTask.cpp
//  G3MiOSSDK
//
//  Created by fpulido on 29/03/14.
//
//

#include "TileRasterizerAsyncTask.hpp"
#include "TileTextureBuilder.hpp"

TileRasterizer_AsyncTask::TileRasterizer_AsyncTask(const IImage* image,
                                                   const TileRasterizerContext* trc,
                                                   IImageListener* listener,
                                                   bool autodelete) :
_builder(NULL),
_image(image),
_trc(trc),
_listener(listener),
_autodelete(autodelete)
{
    //_trc = new TileRasterizerContext(trc->_tile, trc->_mercator);
    TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) _trc->_tile->getTexturizerData();
    if (builderHolder != NULL) {
        _builder = builderHolder->get();
        if(_builder != NULL){
            _builder->_retain();
        }
    }
}


TileRasterizer_AsyncTask::~TileRasterizer_AsyncTask() {
    //ILogger::instance()->logInfo("terminando la tarea..");
    if(_builder != NULL){
        _builder->_release();
    }
}
