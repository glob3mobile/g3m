//
//  GEOSymbolizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#ifndef __G3MiOSSDK__GEOSymbolizer__
#define __G3MiOSSDK__GEOSymbolizer__

#include <vector>

class GEOSymbol;
class GEO2DPointGeometry;
class GEO3DPointGeometry;
class GEO2DLineStringGeometry;
class GEO3DLineStringGeometry;
class GEO2DMultiLineStringGeometry;
class GEO3DMultiLineStringGeometry;
class GEO2DPolygonGeometry;
class GEO3DPolygonGeometry;
class GEO2DMultiPolygonGeometry;


class GEOSymbolizer {
public:
  virtual ~GEOSymbolizer() {
  }

  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const = 0;
  virtual std::vector<GEOSymbol*>* createSymbols(const GEO3DPointGeometry* geometry) const = 0;

  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry*      geometry) const = 0;
  virtual std::vector<GEOSymbol*>* createSymbols(const GEO3DLineStringGeometry*      geometry) const = 0;
  
  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const = 0;

  virtual std::vector<GEOSymbol*>* createSymbols(const GEO3DMultiLineStringGeometry* geometry) const = 0;
  
  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry*      geometry) const = 0;
  virtual std::vector<GEOSymbol*>* createSymbols(const GEO3DPolygonGeometry*      geometry) const = 0;
  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const = 0;

};

#endif
