//
//  GEOVectorialLODRasterSymbolizer.h
//  G3MiOSSDK
//
//  Created by fpulido on 09/04/14.
//
//

#ifndef __G3MiOSSDK__GEOVectorialLODRasterSymbolizer__
#define __G3MiOSSDK__GEOVectorialLODRasterSymbolizer__

#include <iostream>
#include "GEORasterSymbolizer.hpp"

class GEO2DLineRasterStyle;
class GEO2DSurfaceRasterStyle;
class CircleShape;
class GEOGeometry;


class GEOVectorialLODRasterSymbolizer: public GEORasterSymbolizer {
    
private:
    
    GEO2DLineRasterStyle createPolygonLineRasterStyle(const GEOGeometry* geometry) const;
    
    GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(const GEOGeometry* geometry) const;
    
    GEO2DLineRasterStyle createLineRasterStyle(const GEOGeometry* geometry) const;
    
    CircleShape* createCircleShape(const GEO2DPointGeometry* geometry) const;
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DPointGeometry* geometry) const;
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DLineStringGeometry*      geometry) const;
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DMultiLineStringGeometry* geometry) const;
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DPolygonGeometry* geometry) const;
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DMultiPolygonGeometry* geometry) const;
    
public:
    
        virtual ~GEOVectorialLODRasterSymbolizer() {
        }
    
    //    std::vector<GEOSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const {
    //
    //        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    //    }
    //
    //    std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry*      geometry) const {
    //
    //        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    //    }
    //
    //    std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const {
    //
    //        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    //    }
    //
    //    std::vector<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const {
    //
    //        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    //    }
    //
    //    std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const{
    //
    //        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    //    }
    
};

#endif /* defined(__G3MiOSSDK__GEOVectorialLODRasterSymbolizer__) */
