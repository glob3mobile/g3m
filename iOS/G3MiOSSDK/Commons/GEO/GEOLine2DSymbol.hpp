//
//  GEOLine2DSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#ifndef __G3MiOSSDK__GEOLine2DSymbol__
#define __G3MiOSSDK__GEOLine2DSymbol__

#include "GEOSymbol.hpp"

#include "Color.hpp"
class GEOLine2DStyle;


class GEOLine2DSymbol : public GEOSymbol {
private:
  const std::vector<Geodetic2D*>* _coordinates;

  const Color _lineColor;
  const float _lineWidth;

  double _deltaHeight;

public:

  GEOLine2DSymbol(const std::vector<Geodetic2D*>* coordinates,
                  const GEOLine2DStyle& style,
                  double deltaHeight = 0.0);
  
  virtual ~GEOLine2DSymbol() {

  }

  Mesh* createMesh(const G3MRenderContext* rc);
  
};

#endif
