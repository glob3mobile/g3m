//
//  GEOMultiLine2DMeshSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//

#ifndef __G3MiOSSDK__GEOMultiLine2DMeshSymbol__
#define __G3MiOSSDK__GEOMultiLine2DMeshSymbol__

#include "GEOMeshSymbol.hpp"
#include "Color.hpp"
class GEOLine2DStyle;

class GEOMultiLine2DMeshSymbol : public GEOMeshSymbol {
private:
  const std::vector<std::vector<Geodetic2D*>*>* _coordinatesArray;

  const Color _lineColor;
  const float _lineWidth;

  double _deltaHeight;

public:
  GEOMultiLine2DMeshSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                           const GEOLine2DStyle& style,
                           double deltaHeight = 0.0);

  virtual ~GEOMultiLine2DMeshSymbol() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  Mesh* createMesh(const G3MRenderContext* rc) const;
  
};

#endif
