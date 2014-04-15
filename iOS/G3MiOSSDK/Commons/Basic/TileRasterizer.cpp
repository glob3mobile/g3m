//
//  TileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

#include "TileRasterizer.hpp"
#include "ILogger.hpp"
#include "ChangedListener.hpp"
#include "IImageListener.hpp"
#include "TileTextureBuilder.hpp"


void TileRasterizer::setChangeListener(ChangedListener* listener) {
  if (_listener != NULL) {
    ILogger::instance()->logError("Listener already set");
  }
  _listener = listener;
}

void TileRasterizer::notifyChanges() const {
  if (_listener != NULL) {
    _listener->changed();
  }
}


void TileRasterizer::rasterize(const IImage* image,
                               const TileRasterizerContext& trc,
                               IImageListener* listener,
                               bool autodelete) const {
  
    if (_enable) {
      
    TileRasterizer_AsyncTask* rawRasterizeTask = getRawRasterizeTask(image, trc, listener, autodelete);
      
      if(rawRasterizeTask != NULL && _threadUtils != NULL){
          _backgroundRasterize = true;
          _threadUtils->invokeAsyncTask(rawRasterizeTask, true);
      }
      else{
          rawRasterize(image,
                       trc,
                       listener,
                       autodelete);
      }
  }
  else {
    listener->imageCreated(image);
    if (autodelete) {
      delete listener;
    }
  }
}


void TileRasterizer::setEnable(bool enable) {
  if (enable != _enable) {
    _enable = enable;
    notifyChanges();
  }
}



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
    TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) _trc->_tile->getTexturizerData();
    if (builderHolder != NULL) {
        _builder = builderHolder->get();
        if(_builder != NULL){
            _builder->_retain();
        }
    }
}

bool TileRasterizer::backgroundRasterize() const {
    return _backgroundRasterize;
}

TileRasterizer_AsyncTask::~TileRasterizer_AsyncTask() {
    if(_builder != NULL){
        _builder->_release();
    }
}

