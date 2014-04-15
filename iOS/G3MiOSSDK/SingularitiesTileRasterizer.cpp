//
//  SingularitiesTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by fpulido on 08/04/14.
//
//

#include "SingularitiesTileRasterizer.hpp"
#include "ICanvas.hpp"
#include "GEORasterProjection.hpp"
#include "IImageListener.hpp"
#include "IImage.hpp"


void SingularitiesTileRasterizer::initialize(const G3MContext* context) {
    //TODO
    //_threadUtils = context->getThreadUtils(); //init for background rasterize
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
    
    return NULL;
    
}
