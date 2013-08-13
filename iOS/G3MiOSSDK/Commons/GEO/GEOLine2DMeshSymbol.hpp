//
//  GEOLine2DMeshSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#ifndef __G3MiOSSDK__GEOLine2DMeshSymbol__
#define __G3MiOSSDK__GEOLine2DMeshSymbol__

#include "GEOMeshSymbol.hpp"

#include "Color.hpp"
class GEOLine2DStyle;


class GEOLine2DMeshSymbol : public GEOMeshSymbol {
private:
  const std::vector<Geodetic2D*>* _coordinates;

  const Color _lineColor;
  const float _lineWidth;

  double _deltaHeight;

public:

  GEOLine2DMeshSymbol(const std::vector<Geodetic2D*>* coordinates,
                      const GEOLine2DStyle& style,
                      double deltaHeight = 0.0);

  virtual ~GEOLine2DMeshSymbol() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  Mesh* createMesh(const G3MRenderContext* rc) const;
  
};

#endif
