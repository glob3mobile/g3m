package org.glob3.mobile.generated;
//class TranslateScaleGizmoArrowListener;


public class TranslateScaleGizmo extends CompositeRenderer
{
  private CoordinateSystem _cs;
  private final double _size;
  private double _scale;
  private final double _maxScale;

  private TranslateScaleGizmoArrowListener _arrowListener;

  private TranslateScaleGizmoListener _listener;

  private Arrow _xArrow;
  private Arrow _yArrow;
  private Arrow _zArrow;
  private Arrow _scaleArrow;

  private Vector3D getScale1Vector()
  {
    return _cs._x.add(_cs._y).add(_cs._z).normalized().times(_size);
  }


  public TranslateScaleGizmo(CoordinateSystem cs, double size, double scale, double maxScale, double lineWidthRatio, double headLengthRatio, double headWidthRatio, double scaleArrowLengthSizeRatio)
  {
     _cs = new CoordinateSystem(cs);
     _size = size;
     _scale = scale;
     _maxScale = maxScale;
     _listener = null;
    final double lineWidth = size * lineWidthRatio;
    Vector3D x = _cs._x.normalized().times(_size);
    Vector3D y = _cs._y.normalized().times(_size);
    Vector3D z = _cs._z.normalized().times(_size);
  
    _arrowListener = new TranslateScaleGizmoArrowListener(this);
  
    _xArrow = new Arrow(_cs._origin, x, lineWidth, Color.RED, size * lineWidthRatio, headLengthRatio);
    _xArrow.setArrowListener(_arrowListener);
    addRenderer(_xArrow);
  
    _yArrow = new Arrow(_cs._origin, y, lineWidth, Color.GREEN, size * lineWidthRatio, headLengthRatio);
    _yArrow.setArrowListener(_arrowListener);
    addRenderer(_yArrow);
  
    _zArrow = new Arrow(_cs._origin, z, lineWidth, Color.BLUE, size * lineWidthRatio, headLengthRatio);
    _zArrow.setArrowListener(_arrowListener);
    addRenderer(_zArrow);
  
    Vector3D scaleVector = getScale1Vector().times(scale); //Center of arrow
    _scaleArrow = new Arrow(_cs._origin.add(scaleVector), scaleVector.times(scaleArrowLengthSizeRatio), lineWidth, Color.fromRGBA255(255, 0, 255, 255), size * lineWidthRatio, headLengthRatio, true);
    _scaleArrow.setArrowListener(_arrowListener);
    addRenderer(_scaleArrow);
  }

  public void dispose()
  {
    if (_listener != null)
       _listener.dispose();
    if (_arrowListener != null)
       _arrowListener.dispose();
    if (_cs != null)
       _cs.dispose();
  }

  public final void onBaseChanged(Arrow arrow)
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
  
      // Vector3D xBase = _xArrow->getBase();
  
      Arrow[] arrows = {_xArrow, _yArrow, _zArrow};
      for (int i = 0; i < 3; i++)
      {
        arrows[i].setBase(_cs._origin, false);
      }
  
      // Vector3D xBase2 = _xArrow->getBase();
      // Vector3D xDisp = xBase2.sub(xBase);
  
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

  public final void onDraggingEnded(Arrow arrow)
  {
    if (_listener != null)
    {
      _listener.onChangeEnded(this);
    }
  }

}