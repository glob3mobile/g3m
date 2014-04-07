//
//  GEOTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEOTileRasterizer.hpp"

#include "GEORasterSymbol.hpp"
#include "Tile.hpp"
#include "IImageListener.hpp"
#include "IImage.hpp"
#include "ICanvas.hpp"
#include "Color.hpp"
#include "GFont.hpp"
#include "IStringBuilder.hpp"
#include "GEORasterProjection.hpp"
#include "IThreadUtils.hpp"
#include "MultiLayerTileTexturizer.hpp"


//----

void GEOTileRasterizer::initialize(const G3MContext* context) {
    _threadUtils = context->getThreadUtils();
    
}

void GEOTileRasterizer::clear() {
  _quadTree.clear();
  notifyChanges();
}

void GEOTileRasterizer::addSymbol(const GEORasterSymbol* symbol) {
  const Sector* sector = symbol->getSector();

  if (sector == NULL) {
    //    ILogger::instance()->logError("Symbol %s has not sector, can't symbolize",
    //                                  symbol->description().c_str());
    delete symbol;
  }
  else {
    const bool added = _quadTree.add(*sector, symbol);
    if (added) {
      notifyChanges();
    }
    else {
      delete symbol;
    }
  }
}



class GEOTileRasterizer_QuadTreeVisitor : public QuadTreeVisitor {
private:
  const GEOTileRasterizer*     _geoTileRasterizer;
#ifdef C_CODE
  mutable const IImage*        _originalImage;
#endif
#ifdef JAVA_CODE
  private IImage _originalImage;
#endif
  const TileRasterizerContext* _trc;
  IImageListener*              _listener;
  bool                         _autodelete;

  const int                    _tileLevel;

  mutable ICanvas*             _canvas;
  mutable GEORasterProjection* _projection;

public:
  GEOTileRasterizer_QuadTreeVisitor(const GEOTileRasterizer* geoTileRasterizer,
                                    const IImage* originalImage,
                                    const TileRasterizerContext* trc,
                                    IImageListener* listener,
                                    bool autodelete) :
  _geoTileRasterizer(geoTileRasterizer),
  _originalImage(originalImage),
  _trc(trc),
  _listener(listener),
  _autodelete(autodelete),
  _tileLevel(trc->_tile->_level),
  _canvas(NULL),
  _projection(NULL)
  {
  }
    

  bool visitElement(const Sector&           sector,
                    const QuadTree_Content* content) const {
    GEORasterSymbol* symbol = (GEORasterSymbol*) content;

    if (_canvas == NULL) {
      const int width  = _originalImage->getWidth();
      const int height = _originalImage->getHeight();

      const Tile*   tile     = _trc->_tile;
      const bool    mercator = _trc->_mercator;

      _canvas = _geoTileRasterizer->getCanvas(width, height);

      _canvas->drawImage(_originalImage, 0, 0);

      _projection = new GEORasterProjection(tile->_sector, mercator,
                                            width, height);
    }

    symbol->rasterize(_canvas, _projection, _tileLevel);

    return false;
  }

  void endVisit(bool aborted) const {
      //do nothing, delay to postExecute at the asyncTask
      
//    if (_canvas == NULL) {
//        _listener->imageCreated(_originalImage);
//        if (_autodelete) {
//            delete _listener;
//        }
//    }
//    else {
//        _canvas->createImage(_listener, _autodelete);
//
//        delete _originalImage;
//        _originalImage = NULL;
//
//        delete _projection;
//    }
      
  }
    
    void postExecuteEndVisit(bool aborted) const {
        
        if (_canvas == NULL) {
            _listener->imageCreated(_originalImage);
            if (_autodelete) {
                delete _listener;
            }
        }
        else {
            _canvas->createImage(_listener, _autodelete);
            
            delete _originalImage;
            _originalImage = NULL;
            
            delete _projection;
        }
    }
    
};



class GEOTileRasterizer_VisitAsyncTask : public TileRasterizer_AsyncTask {
private:
    const QuadTree *_qTree;
    const Sector          _sector;
    const GEOTileRasterizer_QuadTreeVisitor* _visitor;
     int _row, _column; //fpulido debug: quitar luego

public:
    GEOTileRasterizer_VisitAsyncTask(const IImage* image,
                                     const TileRasterizerContext* trc,
                                     IImageListener* listener,
                                     bool autodelete,
                                     const GEOTileRasterizer* rasterizer,
                                     const QuadTree* quadTree):
    _qTree(quadTree),
    _sector(trc->_tile->_sector),
    TileRasterizer_AsyncTask(image,
                             trc,
                             listener,
                             autodelete)
    {   _row = _trc->_tile->_row;
        _column = _trc->_tile->_column;
        ILogger::instance()->logInfo("_builder->_retain from GEOTileRasterizer_VisitAsyncTask: %p, [%d,%d]", _builder, _trc->_tile->_row, _trc->_tile->_column);
        _visitor = new GEOTileRasterizer_QuadTreeVisitor(rasterizer,
                                                         image,
                                                         trc,
                                                         listener,
                                                         autodelete);
    }

    ~GEOTileRasterizer_VisitAsyncTask() {
        delete _visitor;
        //ILogger::instance()->logInfo("terminando la tarea GEOTileRasterizer");
        ILogger::instance()->logInfo("terminando la tarea GEOTileRasterizer: [%d,%d]", _row, _column);
        //TileRasterizer_AsyncTask::~TileRasterizer_AsyncTask();
    }

    void runInBackground(const G3MContext* context) {
        ILogger::instance()->logInfo("runInBackground GEOTileRasterizer: [%d,%d]", _trc->_tile->_row, _trc->_tile->_column);
        _qTree->acceptVisitor(_sector,
                             *_visitor);
    }

    void onPostExecute(const G3MContext* context) {
        if(_visitor != NULL){
            _visitor->postExecuteEndVisit(true);
            //delete _visitor;
        }
    }
};

void GEOTileRasterizer::rawRasterize(const IImage* image,
                                     const TileRasterizerContext& trc,
                                     IImageListener* listener,
                                     bool autodelete) const {
 
  ILogger::instance()->logInfo("rawRasterize");
    
  const Tile* tile = trc._tile;
    
    _quadTree.acceptVisitor(tile->_sector, GEOTileRasterizer_QuadTreeVisitor(this,
                                                                             image,
                                                                             &trc,
                                                                             listener,
                                                                             autodelete));
    
}


TileRasterizer_AsyncTask* GEOTileRasterizer::getRawRasterizeTask(const IImage* image,
                                                      const TileRasterizerContext& trc,
                                                      IImageListener* listener,
                                                      bool autodelete) const {
    
    //return NULL;
    
    return new GEOTileRasterizer_VisitAsyncTask(image,
                                                &trc,
                                                listener,
                                                autodelete,
                                                this,
                                                &_quadTree);
    
}


//-------





