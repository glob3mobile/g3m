//
//  GEOMarkSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//

#ifndef __G3MiOSSDK__GEOMarkSymbol__
#define __G3MiOSSDK__GEOMarkSymbol__

#include "GEOSymbol.hpp"
class Mark;


class GEOMarkSymbol : public GEOSymbol {
private:
  mutable Mark* _mark;

public:
  GEOMarkSymbol(Mark* mark) :
  _mark(mark)
  {

  }

  virtual ~GEOMarkSymbol();
  
  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizationContext& sc) const;

};

#endif
