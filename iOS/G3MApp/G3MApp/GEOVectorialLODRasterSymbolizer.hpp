//
//  GEOVectorialLODRasterSymbolizer.h
//  G3MApp
//
//  Created by fpulido on 09/04/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__GEOVectorialLODRasterSymbolizer__
#define __G3MApp__GEOVectorialLODRasterSymbolizer__

#include <iostream>
#import <G3MiOSSDK/GEORasterSymbolizer.hpp>

class GEOVectorialLODRasterSymbolizer: public GEORasterSymbolizer {
    
private:
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DPointGeometry* geometry) const;
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DLineStringGeometry*      geometry) const;
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DMultiLineStringGeometry* geometry) const;
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DPolygonGeometry* geometry) const;
    
    std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DMultiPolygonGeometry* geometry) const;
    
public:
    
//    virtual ~GEORasterSymbolizer() {
//    }
//    
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

#endif /* defined(__G3MApp__GEOVectorialLODRasterSymbolizer__) */
