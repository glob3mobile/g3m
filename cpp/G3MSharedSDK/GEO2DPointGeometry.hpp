//
//  GEO2DPointGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#ifndef __G3MiOSSDK__GEO2DPointGeometry__
#define __G3MiOSSDK__GEO2DPointGeometry__

#include "GEO2DGeometry.hpp"

#include "Geodetic2D.hpp"


class GEO2DPointGeometry : public GEO2DGeometry {
private:
  const Geodetic2D _position;
  
protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;
  
  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;
    
public:
  
  GEO2DPointGeometry(const Geodetic2D& position) :
  _position(position)
  {
  }
  
  const Geodetic2D getPosition() const {
    return _position;
  }
    
  int symbolize(const VectorStreamingRenderer::VectorSet* vectorSet,
                const VectorStreamingRenderer::Node*      node) const;
  
};

#endif
