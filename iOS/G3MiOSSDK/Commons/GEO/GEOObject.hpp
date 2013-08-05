//
//  GEOObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#ifndef __G3MiOSSDK__GEOObject__
#define __G3MiOSSDK__GEOObject__

class G3MRenderContext;
class GEOSymbolizationContext;

class GEOObject {
public:
  virtual ~GEOObject() {

  }

  virtual void symbolize(const G3MRenderContext* rc,
                         const GEOSymbolizationContext& sc) const = 0 ;

};

#endif
