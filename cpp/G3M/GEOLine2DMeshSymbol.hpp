//
//  GEOLine2DMeshSymbol.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#ifndef __G3M__GEOLine2DMeshSymbol__
#define __G3M__GEOLine2DMeshSymbol__

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

  ~GEOLine2DMeshSymbol() {
#ifdef JAVA_CODE
  super.dispose();
#endif
  }

  Mesh* createMesh(const G3MRenderContext* rc) const;
  
};

#endif
