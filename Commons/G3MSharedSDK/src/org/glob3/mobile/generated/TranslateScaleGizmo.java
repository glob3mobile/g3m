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

  private TranslateScaleGizmo(CoordinateSystem cs, double size, double scale, double maxScale, double lineWidthRatio, double headLengthRatio, double headWidthRatio, double scaleArrowLengthSizeRatio, boolean scaleOption)
  {
     _cs = new CoordinateSystem(cs);
     _size = size;
     _scale = scale;
     _maxScale = maxScale;
     _listener = null;
    final double lineWidth = size * lineWidthRatio;
  
    _arrowListener = new TranslateScaleGizmoArrowListener(this);
  
    {
      final Vector3D x = _cs._x.normalized().times(_size);
  
      _xArrow = new Arrow(_cs._origin, x, lineWidth, Color.RED, size * headLengthRatio, headWidthRatio);
      _xArrow.setArrowListener(_arrowListener);
      addRenderer(_xArrow);
    }
  
    {
      final Vector3D y = _cs._y.normalized().times(_size);
  
      _yArrow = new Arrow(_cs._origin, y, lineWidth, Color.GREEN, size * headLengthRatio, headWidthRatio);
      _yArrow.setArrowListener(_arrowListener);
      addRenderer(_yArrow);
    }
  
    {
      final Vector3D z = _cs._z.normalized().times(_size);
  
      _zArrow = new Arrow(_cs._origin, z, lineWidth, Color.BLUE, size * headLengthRatio, headWidthRatio);
      _zArrow.setArrowListener(_arrowListener);
      addRenderer(_zArrow);
    }
  
    {
      if (scaleOption)
      {
        Vector3D scaleVector = getScale1Vector().times(scale); //Center of arrow
        _scaleArrow = new Arrow(_cs._origin.add(scaleVector), scaleVector.times(scaleArrowLengthSizeRatio), lineWidth, Color.fromRGBA255(255, 0, 255, 255), size * headLengthRatio, headWidthRatio, true);
        _scaleArrow.setArrowListener(_arrowListener);
        addRenderer(_scaleArrow);
      }
      else
      {
        _scaleArrow = null;
      }
    }
  }


  public static TranslateScaleGizmo translateAndScale(CoordinateSystem cs, double size, double scale, double maxScale, double lineWidthRatio, double headLengthRatio, double headWidthRatio, double scaleArrowLengthSizeRatio)
  {
    return new TranslateScaleGizmo(cs, size, scale, maxScale, lineWidthRatio, headLengthRatio, headWidthRatio, scaleArrowLengthSizeRatio, true); // scaleOption
  }

  public static TranslateScaleGizmo translate(CoordinateSystem cs, double size, double lineWidthRatio, double headLengthRatio, double headWidthRatio)
  {
    return new TranslateScaleGizmo(cs, size, 1, 1, lineWidthRatio, headLengthRatio, headWidthRatio, 1, false); // scaleOption -  scaleArrowLengthSizeRatio -  maxScale -  scale
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
      final Vector3D scaleVector = getScale1Vector();
      final Vector3D scaleV = arrow.getBase().sub(_cs._origin).div(scaleVector);
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
      // Translating
      final Vector3D base = arrow.getBase();
      final Vector3D disp = base.sub(_cs._origin);
  
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
  
      if (_scaleArrow != null)
      {
        _scaleArrow.setBase(_scaleArrow.getBase().add(disp), false);
      }
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