//
//  GEOSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#ifndef __G3MiOSSDK__GEOSymbol__
#define __G3MiOSSDK__GEOSymbol__

#include <vector>

class G3MRenderContext;
class GEOSymbolizationContext;

class GEOSymbol {

public:
  virtual ~GEOSymbol() { }

  virtual void symbolize(const G3MRenderContext* rc,
                         const GEOSymbolizationContext& sc) const = 0 ;

};

#endif
