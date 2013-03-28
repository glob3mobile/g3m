//
//  GEOShapeSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#ifndef __G3MiOSSDK__GEOShapeSymbol__
#define __G3MiOSSDK__GEOShapeSymbol__

#include "GEOSymbol.hpp"
class Shape;

class GEOShapeSymbol : public GEOSymbol {
protected:
  virtual Shape* createShape(const G3MRenderContext* rc) const = 0;

public:
  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizationContext& sc) const;

};

#endif
