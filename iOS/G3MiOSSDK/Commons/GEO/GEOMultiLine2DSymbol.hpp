//
//  GEOMultiLine2DSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//

#ifndef __G3MiOSSDK__GEOMultiLine2DSymbol__
#define __G3MiOSSDK__GEOMultiLine2DSymbol__

#include "GEOSymbol.hpp"
#include "Color.hpp"
class GEOLine2DStyle;

class GEOMultiLine2DSymbol : public GEOSymbol {
private:
  const std::vector<std::vector<Geodetic2D*>*>* _coordinatesArray;

  const Color _lineColor;
  const float _lineWidth;

  double _deltaHeight;

public:
  GEOMultiLine2DSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                       const GEOLine2DStyle& style,
                       double deltaHeight = 0.0);
  
  virtual ~GEOMultiLine2DSymbol() {

  }

  Mesh* createMesh(const G3MRenderContext* rc);

};

#endif
