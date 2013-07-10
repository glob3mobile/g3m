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

#include "Sector.hpp"

class GEORasterSymbol : public GEOSymbol {
protected:
  const Sector* _sector;

protected:
  GEORasterSymbol(const Sector* sector) :
  _sector(sector)
  {
  }

public:
  virtual ~GEORasterSymbol() {
  }

  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizationContext& sc) const;

  const Sector* getSector() const {
    return _sector;
  }

  virtual GEORasterSymbol* createSymbol() const = 0;
  
};

#endif
