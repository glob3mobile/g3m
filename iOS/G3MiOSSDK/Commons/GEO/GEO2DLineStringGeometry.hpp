//
//  GEO2DLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEO2DLineStringGeometry__
#define __G3MiOSSDK__GEO2DLineStringGeometry__

#include "GEOLineStringGeometry.hpp"

#include <vector>
class Geodetic2D;

class GEO2DLineStringGeometry : public GEOLineStringGeometry {
private:
  std::vector<Geodetic2D*>* _coordinates;

protected:
  std::vector<GEOSymbol*>* createSymbols(const G3MRenderContext* rc,
                                         const GEOSymbolizationContext& sc) const;


public:

  GEO2DLineStringGeometry(std::vector<Geodetic2D*>* coordinates) :
  _coordinates(coordinates)
  {

  }

  ~GEO2DLineStringGeometry();

  const std::vector<Geodetic2D*>* getCoordinates() const {
    return _coordinates;
  }


};

#endif
