package org.glob3.mobile.generated;
//C++ TO JAVA CONVERTER TODO TASK: Multiple inheritance is not available in Java:
public class TranslateScaleGizmo extends CompositeRenderer, ArrowListener
{
  private CoordinateSystem _cs;
  private double _scale;
  private final double _maxScale;
  private final double _size;

  private TranslateScaleGizmoListener _listener;

  private Arrow _xArrow;
  private Arrow _yArrow;
  private Arrow _zArrow;
  private Arrow _scaleArrow;

  private Vector3D getScale1Vector()
  {
    return _cs._x.add(_cs._y).add(_cs._z).normalized().times(_size);
  }

  public TranslateScaleGizmo(CoordinateSystem cs, double scale, double size)
  {
     this(cs, scale, size, 2.0);
  }
  public TranslateScaleGizmo(CoordinateSystem cs, double scale, double size, double maxScale)
  {
     _cs = new CoordinateSystem(cs);
     _size = size;
     _scale = scale;
     _maxScale = maxScale;
     _listener = null;
  
//#define LINE_WIDTH_RATIO 0.01
//#define HEAD_LENGTH_RATIO 0.05
//#define HEAD_WIDTH_RATIO 1.5
//#define SCALE_ARROW_LENGTH_SIZE_RATIO 0.15
  
    double lineWidth = size * DefineConstants.LINE_WIDTH_RATIO;
    Vector3D x = _cs._x.normalized().times(_size);
    Vector3D y = _cs._y.normalized().times(_size);
    Vector3D z = _cs._z.normalized().times(_size);
  
    _xArrow = new Arrow(_cs._origin, x, lineWidth, Color.RED, size * DefineConstants.HEAD_LENGTH_RATIO, DefineConstants.HEAD_WIDTH_RATIO);
    _xArrow.setArrowListener(this);
    addRenderer(_xArrow);
  
    _yArrow = new Arrow(_cs._origin, y, lineWidth, Color.GREEN, size * DefineConstants.HEAD_LENGTH_RATIO, DefineConstants.HEAD_WIDTH_RATIO);
    _yArrow.setArrowListener(this);
    addRenderer(_yArrow);
  
    _zArrow = new Arrow(_cs._origin, z, lineWidth, Color.BLUE, size * DefineConstants.HEAD_LENGTH_RATIO, DefineConstants.HEAD_WIDTH_RATIO);
    _zArrow.setArrowListener(this);
    addRenderer(_zArrow);
  
    Vector3D scaleVector = getScale1Vector().times(scale); //Center of arrow
    _scaleArrow = new Arrow(_cs._origin.add(scaleVector), scaleVector.times(DefineConstants.SCALE_ARROW_LENGTH_SIZE_RATIO), lineWidth, Color.fromRGBA255(255, 0, 255, 255), size * DefineConstants.HEAD_LENGTH_RATIO, DefineConstants.HEAD_WIDTH_RATIO, true);
    _scaleArrow.setArrowListener(this);
    addRenderer(_scaleArrow);
  }

  public void dispose()
  {
    if (_cs != null)
       _cs.dispose();
  }

  @Override
  public void onBaseChanged(Arrow arrow)
  {
  
    if (_scaleArrow == arrow)
    {
  
      Vector3D scaleVector = getScale1Vector();
      Vector3D scaleV = arrow.getBase().sub(_cs._origin).div(scaleVector);
      _scale = scaleV.maxAxis();
  
      if (_scale < 0)
      {
        _scaleArrow.setBase(_cs._origin, false);
        _scale = 0;
      }
      else if (_scale > _maxScale)
      {
        _scaleArrow.setBase(_cs._origin.add(scaleVector.times(_maxScale)), false);
        _scale = _maxScale;
      }
    }
    else
    {
      //Translating
      Vector3D base = arrow.getBase();
      Vector3D disp = base.sub(_cs._origin);
  
      CoordinateSystem cs = new CoordinateSystem(_cs.changeOrigin(base));
      if (_cs != null)
         _cs.dispose();
      _cs = cs;
  
      Vector3D xBase = _xArrow.getBase();
  
      Arrow[] arrows = {_xArrow, _yArrow, _zArrow};
      for (int i = 0; i < 3; i++)
      {
        arrows[i].setBase(_cs._origin, false);
      }
  
      Vector3D xBase2 = _xArrow.getBase();
      Vector3D xDisp = xBase2.sub(xBase);
  
      _scaleArrow.setBase(_scaleArrow.getBase().add(disp), false);
    }
  
    if (_listener != null)
    {
      _listener.onChanged(this);
    }
  }

  public final CoordinateSystem getCoordinateSystem()
  {
    return _cs;
  }

  public final double getScale()
  {
    return _scale;
  }

  public final void setListener(TranslateScaleGizmoListener listener)
  {
      _listener = listener;
  }

}