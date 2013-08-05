//
//  GEO2DPointGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#ifndef __G3MiOSSDK__GEO2DPointGeometry__
#define __G3MiOSSDK__GEO2DPointGeometry__

#include "GEOGeometry2D.hpp"

#include "Geodetic2D.hpp"

class GEO2DPointGeometry : public GEOGeometry2D {
private:
  const Geodetic2D _position;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;

public:

  GEO2DPointGeometry(const Geodetic2D& position) :
  _position(position)
  {

  }

  const Geodetic2D getPosition() const {
    return _position;
  }

};

#endif
