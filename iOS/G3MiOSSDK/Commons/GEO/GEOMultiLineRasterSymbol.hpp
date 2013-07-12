//
//  GEOMultiLineRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#ifndef __G3MiOSSDK__GEOMultiLineRasterSymbol__
#define __G3MiOSSDK__GEOMultiLineRasterSymbol__

#include "GEORasterSymbol.hpp"
#include "Color.hpp"
#include "GEOLine2DStyle.hpp"
class GEOLine2DStyle;


class GEOMultiLineRasterSymbol : public GEORasterSymbol {
private:
#ifdef C_CODE
  mutable const std::vector<std::vector<Geodetic2D*>*>* _coordinatesArray;
#endif
#ifdef JAVA_CODE
  private java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _coordinatesArray;
#endif

  const Color _lineColor;
  const float _lineWidth;

  GEOMultiLineRasterSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                           const Sector* sector,
                           const Color&  lineColor,
                           const float   lineWidth) :
  GEORasterSymbol(sector),
  _coordinatesArray(coordinatesArray),
  _lineColor(lineColor),
  _lineWidth(lineWidth)
  {
  }
  
public:
  GEOMultiLineRasterSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                           const GEOLine2DStyle& style) :
  GEORasterSymbol( calculateSectorFromCoordinatesArray(coordinatesArray) ),
  _coordinatesArray( copyCoordinatesArray(coordinatesArray) ),
  _lineColor( style.getColor() ),
  _lineWidth( style.getWidth() )
  {
  }

  ~GEOMultiLineRasterSymbol();

  GEOMultiLineRasterSymbol* createSymbol() const;


  void rasterize(ICanvas*                   canvas,
                 const GEORasterProjection* projection) const;

};

#endif
