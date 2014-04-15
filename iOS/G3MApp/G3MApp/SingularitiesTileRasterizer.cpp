//
//  SingularitiesTileRasterizer.cpp
//  G3MApp
//
//  Created by fpulido on 09/04/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#include "SingularitiesTileRasterizer.hpp"

#import <G3MiOSSDK/ICanvas.hpp>
#import <G3MiOSSDK/GEORasterProjection.hpp>
#import <G3MiOSSDK/IImageListener.hpp>
#import <G3MiOSSDK/IImage.hpp>


void SingularitiesTileRasterizer::initialize(const G3MContext* context) {
    
    _threadUtils = context->getThreadUtils(); //init for background rasterize
}



bool SingularitiesTileRasterizer::touchEquator(const Sector s) const{
    
    if((s._upper._latitude._degrees >=0.0) && (s._lower._latitude._degrees <=0.0)){
        return true;
    }
    return false;
}

bool SingularitiesTileRasterizer::touchGreenwichMeridian(const Sector s) const{
    
    if((s._upper._longitude._degrees >=0.0) && (s._lower._longitude._degrees <=0.0)){
        return true;
    }
    return false;
}

bool SingularitiesTileRasterizer::touchCancerTropic(const Sector s) const{
    
    if((s._upper._latitude._degrees >=23.437778) && (s._lower._latitude._degrees <=23.437778)){
        return true;
    }
    return false;
}

bool SingularitiesTileRasterizer::touchCapricornTropic(const Sector s) const{
    
    if((s._upper._latitude._degrees >=-23.437778) && (s._lower._latitude._degrees <=-23.437778)){
        return true;
    }
    return false;
}


void SingularitiesTileRasterizer::rasterLine(const Geodetic2D *startPos,
                                             const Geodetic2D *endPos,
                                             ICanvas* canvas,
                                             const Sector s) const {
    
    GEORasterProjection *projection = new GEORasterProjection(s,
                                                              false,
                                                              canvas->getWidth(),
                                                              canvas->getHeight());
    
    canvas->beginPath();
    canvas->moveTo(projection->project(startPos));
    canvas->lineTo(projection->project(endPos));
    canvas->stroke();
}


class SingularitiesTileRasterizer_AsyncTask : public TileRasterizer_AsyncTask {
private:
    const SingularitiesTileRasterizer*     _singularTileRasterizer;
#ifdef C_CODE
    mutable ICanvas* _canvas;
#endif
#ifdef JAVA_CODE
    private ICanvas _canvas;
#endif
    int _row, _column; //fpulido debug: quitar luego
    
public:
    SingularitiesTileRasterizer_AsyncTask(const IImage* image,
                                          const TileRasterizerContext* trc,
                                          IImageListener* listener,
                                          bool autodelete,
                                          const SingularitiesTileRasterizer* tileRasterizer):
    TileRasterizer_AsyncTask(image,
                             trc,
                             listener,
                             autodelete),
    _singularTileRasterizer(tileRasterizer),
    _canvas(NULL)
    {
        _row = _trc->_tile->_row;
        _column = _trc->_tile->_column;
        ILogger::instance()->logInfo("_builder->_retain from SingularitiesTileRasterizer_AsyncTask: %p, [%d,%d]", _builder, _row, _column);
    }
    
    
    ~SingularitiesTileRasterizer_AsyncTask() {
        ILogger::instance()->logInfo("terminando la tarea SingularitiesTileRasterizer_AsyncTask: [%d,%d]", _row, _column);
    }
    
    void runInBackground(const G3MContext* context) {
        ILogger::instance()->logInfo("runInBackground SingularitiesTileRasterizer_AsyncTask: [%d,%d]", _trc->_tile->_row, _trc->_tile->_column);
        _singularTileRasterizer->rawRasterize(_image, *_trc, _listener, _autodelete);
        delete _image;
    }
    
    void onPostExecute(const G3MContext* context) {
         ILogger::instance()->logInfo("postExecute SingularitiesTileRasterizer_AsyncTask: [%d,%d]", _trc->_tile->_row, _trc->_tile->_column);
        //do nothing
    }
};


void SingularitiesTileRasterizer::rawRasterize(const IImage* image,
                                               const TileRasterizerContext& trc,
                                               IImageListener* listener,
                                               bool autodelete) const
{
    //const Tile*   tile  = trc._tile;
    const Sector sector = trc._tile->_sector;
    
    const int width  = image->getWidth();
    const int height = image->getHeight();
    
    ICanvas* canvas = getCanvas(width, height);
    canvas->removeShadow();
    canvas->drawImage(image, 0, 0);
    canvas->setLineColor(_color);
    canvas->setLineWidth(1);
    
    if(touchEquator(sector)){
        const Geodetic2D *startPos = new Geodetic2D(Angle::zero(), sector._lower._longitude);
        const Geodetic2D *endPos = new Geodetic2D(Angle::zero(), sector._upper._longitude);
        rasterLine(startPos, endPos, canvas, sector);
    }
    
    if(touchGreenwichMeridian(sector)){
        const Geodetic2D *startPos = new Geodetic2D(sector._lower._latitude, Angle::zero());
        const Geodetic2D *endPos = new Geodetic2D(sector._upper._latitude, Angle::zero());
        rasterLine(startPos, endPos, canvas, sector);
    }
    
    if(touchCancerTropic(sector)){
        const Geodetic2D *startPos = new Geodetic2D(Angle::fromDegrees(23.437778), sector._lower._longitude);
        const Geodetic2D *endPos = new Geodetic2D(Angle::fromDegrees(23.437778), sector._upper._longitude);
        rasterLine(startPos, endPos, canvas, sector);
    }
    
    if(touchCapricornTropic(sector)){
        const Geodetic2D *startPos = new Geodetic2D(Angle::fromDegrees(-23.437778), sector._lower._longitude);
        const Geodetic2D *endPos = new Geodetic2D(Angle::fromDegrees(-23.437778), sector._upper._longitude);
        rasterLine(startPos, endPos, canvas, sector);
    }
    
    canvas->createImage(listener, autodelete);
    
}


TileRasterizer_AsyncTask* SingularitiesTileRasterizer::getRawRasterizeTask(const IImage* image,
                                                                           const TileRasterizerContext& trc,
                                                                           IImageListener* listener,
                                                                           bool autodelete) const {
    
    //return NULL;
    
    return new SingularitiesTileRasterizer_AsyncTask(image,
                                                     &trc,
                                                     listener,
                                                     autodelete,
                                                     this);
    
}
