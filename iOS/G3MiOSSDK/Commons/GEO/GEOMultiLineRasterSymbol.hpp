//
//  GEOMultiLineRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#ifndef __G3MiOSSDK__GEOMultiLineRasterSymbol__
#define __G3MiOSSDK__GEOMultiLineRasterSymbol__

#include "GEORasterSymbol.hpp"
#include <vector>

class GEOMultiLineRasterSymbol : public GEORasterSymbol {
private:
  const std::vector<std::vector<Geodetic2D*>*>* _coordinatesArray;

  static Sector* calculateSector(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray);

  GEOMultiLineRasterSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                           const Sector* sector) :
  GEORasterSymbol(sector),
  _coordinatesArray(coordinatesArray)
  {
  }
  
public:
  GEOMultiLineRasterSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) :
  GEORasterSymbol( calculateSector(coordinatesArray) ),
  _coordinatesArray(coordinatesArray)
  {
  }

  GEOMultiLineRasterSymbol* createSymbol() const;

};

#endif
