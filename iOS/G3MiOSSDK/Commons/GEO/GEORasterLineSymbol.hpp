//
//  GEORasterLineSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#ifndef __G3MiOSSDK__GEORasterLineSymbol__
#define __G3MiOSSDK__GEORasterLineSymbol__

#include "GEORasterSymbol.hpp"
#include <vector>
class Geodetic2D;

class GEORasterLineSymbol : public GEORasterSymbol {
private:
  const std::vector<Geodetic2D*>* _coordinates;

  static Sector* calculateSector(const std::vector<Geodetic2D*>* coordinates);

  GEORasterLineSymbol(const std::vector<Geodetic2D*>* coordinates,
                      const Sector* sector) :
  GEORasterSymbol(sector),
  _coordinates(coordinates)
  {
  }

public:
  GEORasterLineSymbol(const std::vector<Geodetic2D*>* coordinates):
  GEORasterSymbol( calculateSector(coordinates) ),
  _coordinates(coordinates)
  {
  }

  GEORasterLineSymbol* createSymbol() const;


};

#endif
