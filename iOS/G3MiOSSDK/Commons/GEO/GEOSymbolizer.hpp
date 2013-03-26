//
//  GEOSymbolizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#ifndef __G3MiOSSDK__GEOSymbolizer__
#define __G3MiOSSDK__GEOSymbolizer__

//class Mesh;
//class G3MRenderContext;
class GEOSymbol;
class GEO2DMultiLineStringGeometry;
class GEO2DLineStringGeometry;
#include <vector>

class GEOSymbolizer {
public:
#ifdef C_CODE
  virtual ~GEOSymbolizer() { }
#endif
#ifdef JAVA_CODE
  public void dispose();
#endif

  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const = 0;
  virtual std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry*      geometry) const = 0;

};

#endif
