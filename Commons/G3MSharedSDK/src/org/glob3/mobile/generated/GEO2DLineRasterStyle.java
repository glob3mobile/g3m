package org.glob3.mobile.generated; 
//
//  GEO2DLineRasterStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/22/13.
//
//

//
//  GEO2DLineRasterStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/22/13.
//
//



public class GEO2DLineRasterStyle extends Disposable
{
  private final Color _color ;
  private final float _width;
  private final StrokeCap _cap;
  private final StrokeJoin _join;
  private final float _miterLimit;

  final float[]    _dashLengths;
  private final int _dashCount;
  private final int _dashPhase;

  public GEO2DLineRasterStyle(Color color, float width, StrokeCap cap, StrokeJoin join, float miterLimit, float[] dashLengths, int dashCount, int dashPhase)
  {
     _color = new Color(color);
     _width = width;
     _cap = cap;
     _join = join;
     _miterLimit = miterLimit;
     _dashCount = dashCount;
     _dashPhase = dashPhase;
    //_dashLengths = java.util.Arrays.copyOf(dashLengths, _dashCount);
    _dashLengths = new float[_dashCount];
    System.arraycopy(dashLengths, 0, _dashLengths, 0, _dashCount);
  }

  public GEO2DLineRasterStyle(GEO2DLineRasterStyle that)
  {
     _color = new Color(that._color);
     _width = that._width;
     _cap = that._cap;
     _join = that._join;
     _miterLimit = that._miterLimit;
     _dashCount = that._dashCount;
     _dashPhase = that._dashPhase;
    //_dashLengths = java.util.Arrays.copyOf(that._dashLengths, _dashCount);
    _dashLengths = new float[_dashCount];
    System.arraycopy(that._dashLengths, 0, _dashLengths, 0, _dashCount);
  }

  public void dispose()
  {

    JAVA_POST_DISPOSE
  }

  public final boolean apply(ICanvas canvas)
  {
    final boolean applied = (_width > 0) && (!_color.isFullTransparent());
  
    if (applied)
    {
      canvas.setLineColor(_color);
      canvas.setLineWidth(_width);
      canvas.setLineCap(_cap);
      canvas.setLineJoin(_join);
      canvas.setLineMiterLimit(_miterLimit);
  
      canvas.setLineDash(_dashLengths, _dashCount, _dashPhase);
    }
  
    return applied;
  }

}