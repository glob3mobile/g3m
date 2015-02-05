//
//  GEORectangleRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by fpulido on 30/06/14.
//
//

#ifndef __G3MiOSSDK__GEORectangleRasterSymbol__
#define __G3MiOSSDK__GEORectangleRasterSymbol__

#include "GEORasterSymbol.hpp"

#include "GEO2DLineRasterStyle.hpp"
#include "GEO2DSurfaceRasterStyle.hpp"

class GEO2DPolygonData;


class GEORectangleRasterSymbol : public GEORasterSymbol {
private:
    const GEO2DPolygonData* _polygonData;
#ifdef C_CODE
    const GEO2DLineRasterStyle    _lineStyle;
    const GEO2DSurfaceRasterStyle _surfaceStyle;
    const Vector2F _rectangleSize;
#endif
#ifdef JAVA_CODE
    private final GEO2DLineRasterStyle    _lineStyle;
    private final GEO2DSurfaceRasterStyle _surfaceStyle;
    private final Vector2F _rectangleSize;
#endif
    
protected:
    void rawRasterize(ICanvas*                   canvas,
                      const GEORasterProjection* projection) const;
    
public:
    
    GEORectangleRasterSymbol(const GEO2DPolygonData*        polygonData,
                             const Vector2F rectangleSize,
                             const GEO2DLineRasterStyle&    lineStyle,
                             const GEO2DSurfaceRasterStyle& surfaceStyle,
                             const int minTileLevel = -1,
                             const int maxTileLevel = -1);
    
    ~GEORectangleRasterSymbol();
    
    const Sector* getSector() const;
    
};

#endif /* defined(__G3MiOSSDK__GEORectangleRasterSymbol__) */
