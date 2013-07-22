//
//  GEOLine2DRasterStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/22/13.
//
//

#ifndef __G3MiOSSDK__GEOLine2DRasterStyle__
#define __G3MiOSSDK__GEOLine2DRasterStyle__

#include "Color.hpp"
#include "ICanvas.hpp"

class GEOLine2DRasterStyle {
private:
  const Color      _color;
  const float      _width;
  const StrokeCap  _cap;
  const StrokeJoin _join;
  const float      _miterLimit;

#ifdef C_CODE
  float*           _dashLengths;
#endif
#ifdef JAVA_CODE
  final float[]    _dashLengths;
#endif
  const int        _dashCount;
  const int        _dashPhase;

public:
  GEOLine2DRasterStyle(const Color&     color,
                       const float      width,
                       const StrokeCap  cap,
                       const StrokeJoin join,
                       const float      miterLimit,
                       float            dashLengths[],
                       const int        dashCount,
                       const int        dashPhase) :
  _color(color),
  _width(width),
  _cap(cap),
  _join(join),
  _miterLimit(miterLimit),
  _dashLengths(dashLengths),
  _dashCount(dashCount),
  _dashPhase(dashPhase)
  {
  }

  GEOLine2DRasterStyle(const GEOLine2DRasterStyle& that) :
  _color(that._color),
  _width(that._width),
  _cap(that._cap),
  _join(that._join),
  _miterLimit(that._miterLimit),
  _dashLengths( new float[that._dashCount] ),
  _dashCount(that._dashCount),
  _dashPhase(that._dashPhase)
  {
#ifdef C_CODE
    std::copy(that._dashLengths,
              that._dashLengths + that._dashCount,
              _dashLengths );
#endif
#ifdef JAVA_CODE
    _dashLengths = java.util.Arrays.copyOf(that._dashLengths, _dashCount);
#endif
  }

  virtual ~GEOLine2DRasterStyle() {

  }

  void apply(ICanvas* canvas) const;

};

#endif
