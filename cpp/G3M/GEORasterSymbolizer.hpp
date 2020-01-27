//
//  GEORasterSymbolizer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 5/1/14.
//
//

#ifndef __G3M__GEORasterSymbolizer__
#define __G3M__GEORasterSymbolizer__

#include <vector>

class GEORasterSymbol;
class GEO2DPointGeometry;
class GEO3DPointGeometry;
class GEO2DLineStringGeometry;
class GEO2DMultiLineStringGeometry;
class GEO2DPolygonGeometry;
class GEO3DPolygonGeometry;
class GEO2DMultiPolygonGeometry;


class GEORasterSymbolizer {
public:
  virtual ~GEORasterSymbolizer() {
  }

  virtual GEORasterSymbolizer* copy() const = 0;

  virtual std::vector<GEORasterSymbol*>* createSymbols(const GEO2DPointGeometry*           geometry) const = 0;
  virtual std::vector<GEORasterSymbol*>* createSymbols(const GEO3DPointGeometry*           geometry) const = 0;

  virtual std::vector<GEORasterSymbol*>* createSymbols(const GEO2DLineStringGeometry*      geometry) const = 0;
  virtual std::vector<GEORasterSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const = 0;

  virtual std::vector<GEORasterSymbol*>* createSymbols(const GEO2DPolygonGeometry*         geometry) const = 0;
  virtual std::vector<GEORasterSymbol*>* createSymbols(const GEO3DPolygonGeometry*         geometry) const = 0;
  virtual std::vector<GEORasterSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry*    geometry) const = 0;
  
};

#endif
