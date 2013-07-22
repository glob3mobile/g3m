package org.glob3.mobile.generated; 
//
//  GEOLine2DRasterStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/22/13.
//
//

//
//  GEOLine2DRasterStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/22/13.
//
//



public class GEOLine2DRasterStyle
{
  private final Color _color ;
  private final float _width;
  private final StrokeCap _cap;
  private final StrokeJoin _join;
  private final float _miterLimit;

  final float[]    _dashLengths;
  private final int _dashCount;
  private final int _dashPhase;

  public GEOLine2DRasterStyle(Color color, float width, StrokeCap cap, StrokeJoin join, float miterLimit, float[] dashLengths, int dashCount, int dashPhase)
  {
     _color = new Color(color);
     _width = width;
     _cap = cap;
     _join = join;
     _miterLimit = miterLimit;
     _dashLengths = dashLengths;
     _dashCount = dashCount;
     _dashPhase = dashPhase;
  }

  public GEOLine2DRasterStyle(GEOLine2DRasterStyle that)
  {
     _color = new Color(that._color);
     _width = that._width;
     _cap = that._cap;
     _join = that._join;
     _miterLimit = that._miterLimit;
     _dashCount = that._dashCount;
     _dashPhase = that._dashPhase;
    _dashLengths = java.util.Arrays.copyOf(that._dashLengths, _dashCount);
  }

  public void dispose()
  {

  }

  public final void apply(ICanvas canvas)
  {
    canvas.setLineColor(_color);
    canvas.setLineWidth(_width);
    canvas.setLineCap(_cap);
    canvas.setLineJoin(_join);
    canvas.setLineMiterLimit(_miterLimit);
  
    int _DGD_rasterizer;
  
    canvas.setLineDash(_dashLengths, _dashCount, _dashPhase);
  }

}