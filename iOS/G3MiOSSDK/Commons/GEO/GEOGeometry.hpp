//
//  GEOGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEOGeometry__
#define __G3MiOSSDK__GEOGeometry__

#include "GEOObject.hpp"

#include <vector>
class GEOSymbol;
class GEOFeature;

class GEOGeometry : public GEOObject {
private:
  GEOFeature* _feature;

protected:
  virtual std::vector<GEOSymbol*>* createSymbols(const G3MRenderContext* rc,
                                                 const GEOSymbolizationContext& sc) const = 0;

public:
  GEOGeometry() :
  _feature(NULL)
  {

  }

  ~GEOGeometry();

  void setFeature(GEOFeature* feature);

  const GEOFeature* getFeature() const {
    return _feature;
  }

  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizationContext& sc) const;

};

#endif
