//
//  GEORasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#ifndef __G3MiOSSDK__GEORasterSymbol__
#define __G3MiOSSDK__GEORasterSymbol__

#include "GEOSymbol.hpp"

class GEORasterSymbol : public GEOSymbol {
private:
public:
  ~GEORasterSymbol() {
  }

  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizationContext& sc) const;

};

#endif
