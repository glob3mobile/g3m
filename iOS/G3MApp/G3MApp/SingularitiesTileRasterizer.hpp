//
//  SingularitiesTileRasterizer.h
//  G3MApp
//
//  Created by fpulido on 09/04/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__SingularitiesTileRasterizer__
#define __G3MApp__SingularitiesTileRasterizer__

#include <iostream>
//#include "CanvasTileRasterizer.hpp"
#import <G3MiOSSDK/CanvasTileRasterizer.hpp>

class SingularitiesTileRasterizer: public CanvasTileRasterizer {
    
private:
    const Color _color;
    
    bool touchEquator(const Sector s) const;
    
    bool touchGreenwichMeridian(const Sector s) const;
    
    bool touchCancerTropic(const Sector s) const;
    
    bool touchCapricornTropic(const Sector s) const;
    
    void rasterLine(const Geodetic2D *startPos,
                    const Geodetic2D *endPos,
                    ICanvas* canvas,
                    const Sector s) const;
    
    
public:
    SingularitiesTileRasterizer() :
    _color (Color::red())
    {
    }
    
    SingularitiesTileRasterizer (const Color& color) :
    _color(color)
    {
    }
    
    
    void initialize(const G3MContext* context);
    
    std::string getId() const {
        return "SingularitiesTileRasterizer";
    }
    
    void rawRasterize(const IImage* image,
                      const TileRasterizerContext& trc,
                      IImageListener* listener,
                      bool autodelete) const;
    
    TileRasterizer_AsyncTask* getRawRasterizeTask(const IImage* image,
                                                  const TileRasterizerContext& trc,
                                                  IImageListener* listener,
                                                  bool autodelete) const;
    
};

#endif /* defined(__G3MApp__SingularitiesTileRasterizer__) */
