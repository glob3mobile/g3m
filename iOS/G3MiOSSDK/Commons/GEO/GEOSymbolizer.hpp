//
//  GEOSymbolizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#ifndef __G3MiOSSDK__GEOSymbolizer__
#define __G3MiOSSDK__GEOSymbolizer__

class GEOSymbol;
class GEO2DMultiLineStringGeometry;
class GEO2DLineStringGeometry;
class GEO2DPointGeometry;
class GEOObject;
class GEO2DPolygonGeometry;
class GEO2DMultiPolygonGeometry;

#include <vector>


class GEOSymbolizer {
public:
  virtual ~GEOSymbolizer() {
  }

  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const = 0;

  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry*      geometry) const = 0;
  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const = 0;

  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const = 0;
  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const = 0;

};

#endif
