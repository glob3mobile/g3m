//
//  GEO2DLineRasterStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/22/13.
//
//

#ifndef __G3MiOSSDK__GEO2DLineRasterStyle__
#define __G3MiOSSDK__GEO2DLineRasterStyle__

#include "Color.hpp"
#include "ICanvas.hpp"

class GEO2DLineRasterStyle {
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
  private final float[]    _dashLengths;
#endif
  const int        _dashCount;
  const int        _dashPhase;

public:
  GEO2DLineRasterStyle(const Color&     color,
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
  _dashCount(dashCount),
  _dashPhase(dashPhase)
  {
#ifdef C_CODE
    _dashLengths = new float[_dashCount];
    std::copy(dashLengths,
              dashLengths + _dashCount,
              _dashLengths);
#endif
#ifdef JAVA_CODE
    //_dashLengths = java.util.Arrays.copyOf(dashLengths, _dashCount);
    _dashLengths = new float[_dashCount];
    System.arraycopy(dashLengths, 0, _dashLengths, 0, _dashCount);
#endif
  }

#ifdef JAVA_CODE
  public GEO2DLineRasterStyle(final Color color,
                              final float width,
                              final StrokeCap cap,
                              final StrokeJoin join,
                              final float miterLimit,
                              final float[] dashLengths,
                              final int dashPhase) {
    this(color, width, cap, join, miterLimit, dashLengths, dashLengths.length, dashPhase);
  }
#endif

  GEO2DLineRasterStyle(const GEO2DLineRasterStyle& that) :
  _color(that._color),
  _width(that._width),
  _cap(that._cap),
  _join(that._join),
  _miterLimit(that._miterLimit),
  _dashCount(that._dashCount),
  _dashPhase(that._dashPhase)
  {
#ifdef C_CODE
    _dashLengths = new float[_dashCount];
    std::copy(that._dashLengths,
              that._dashLengths + _dashCount,
              _dashLengths);
#endif
#ifdef JAVA_CODE
    //_dashLengths = java.util.Arrays.copyOf(that._dashLengths, _dashCount);
    _dashLengths = new float[_dashCount];
    System.arraycopy(that._dashLengths, 0, _dashLengths, 0, _dashCount);
#endif
  }

  virtual ~GEO2DLineRasterStyle() {
#ifdef C_CODE
    delete [] _dashLengths;
#endif
  }

  bool apply(ICanvas* canvas) const;

};

#endif
