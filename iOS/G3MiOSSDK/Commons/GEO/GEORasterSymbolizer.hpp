//
//  GEORasterSymbolizer.h
//  G3MiOSSDK
//
//  Created by fpulido on 17/03/14.
//
//

#ifndef __G3MiOSSDK__GEORasterSymbolizer__
#define __G3MiOSSDK__GEORasterSymbolizer__


#include <vector>
#include "GEOSymbolizer.hpp"

class GEOSymbol;
class GEORasterSymbol;
class GEO2DMultiLineStringGeometry;
class GEO2DLineStringGeometry;
class GEO2DPointGeometry;
class GEOObject;
class GEO2DPolygonGeometry;
class GEO2DMultiPolygonGeometry;

class GEORasterSymbolizer : public GEOSymbolizer {
    

private:
    
    virtual std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DPointGeometry* geometry) const = 0;
    
    virtual std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DLineStringGeometry*      geometry) const = 0;
    
    virtual std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DMultiLineStringGeometry* geometry) const = 0;
    
    virtual std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DPolygonGeometry* geometry) const = 0;
    
    virtual std::vector<GEORasterSymbol*>* createRasterSymbols(const GEO2DMultiPolygonGeometry* geometry) const = 0;
    
public:
    
    virtual ~GEORasterSymbolizer() {
    }
    
    std::vector<GEOSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const {
        
        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    }
    
    std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry*      geometry) const {
        
        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    }
    
    std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const {
        
        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    }
    
    std::vector<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const {
        
        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    }
    
    std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const{
        
        return (std::vector<GEOSymbol*>*) createRasterSymbols(geometry);
    }
  
    
};

#endif /* defined(__G3MiOSSDK__GEORasterSymbolizer__) */
