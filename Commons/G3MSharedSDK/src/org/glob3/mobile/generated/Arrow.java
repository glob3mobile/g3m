package org.glob3.mobile.generated;
public class Arrow extends MeshRenderer
{
  private boolean _grabbed;
  private MutableVector3D _grabbedPos = new MutableVector3D();
  private MutableVector3D _baseWhenGrabbed = new MutableVector3D();

  private GLState _state;
  private ModelTransformGLFeature _transformGLFeature;

  private MutableVector3D _base = new MutableVector3D();
  private MutableVector3D _vector = new MutableVector3D();
  private final double _radius;

  private ArrowListener _listener;

  private static Mesh createCylinderMesh(Cylinder cylinder, Color color, int nSegments)
  {
    Vector3D d = cylinder._end.sub(cylinder._start);
    Vector3D r = (d._z == 0) ? new Vector3D(0.0,0.0,1.0) : new Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
  
    Vector3D pStart = cylinder._start.add(r.times(cylinder._startRadius / r.length()));
    Vector3D pEnd = cylinder._end.add(r.times(cylinder._endRadius / r.length()));
    MutableMatrix44D mStart = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(360.0 / nSegments), d, cylinder._start);
    MutableMatrix44D mEnd = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(360.0 / nSegments), d, cylinder._end);
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
    MutableVector3D newStartPoint = new MutableVector3D(pStart);
    MutableVector3D newEndPoint = new MutableVector3D(pEnd);
    for (int i = 0; i < nSegments; ++i)
    {
  
      //Tube
      newStartPoint.set(newStartPoint.transformedBy(mStart, 1.0));
      newEndPoint.set(newEndPoint.transformedBy(mEnd, 1.0));
  
      vertices.add(newStartPoint.asVector3D());
      vertices.add(newEndPoint.asVector3D());
  
      normals.add(newStartPoint.sub(cylinder._start));
      normals.add(newEndPoint.sub(cylinder._end));
    }
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
    for (int i = 0; i < nSegments *2; ++i)
    {
      indices.add((short) i);
    }
    indices.add((short) 0);
    indices.add((short) 1);
  
    IndexedMesh im = new IndexedMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), vertices.create(), true, indices.create(), true, 1.0f, 1.0f, new Color(color), null, true, normals.create()); // bool depthTest -  IFloatBuffer* colors -  const Color* flatColor -  float pointSize -  float lineWidth -  bool ownsIndices -  bool ownsVertices
    if (normals != null)
       normals.dispose();
    if (vertices != null)
       vertices.dispose();
  
    return im;
  }

  public Arrow(Vector3D base, Vector3D vector, double radius, Color color, double headLength, double headWidthRatio)
  {
     this(base, vector, radius, color, headLength, headWidthRatio, false);
  }
  public Arrow(Vector3D base, Vector3D vector, double radius, Color color, double headLength)
  {
     this(base, vector, radius, color, headLength, 1.2, false);
  }
  public Arrow(Vector3D base, Vector3D vector, double radius, Color color)
  {
     this(base, vector, radius, color, 3.0, 1.2, false);
  }
  public Arrow(Vector3D base, Vector3D vector, double radius, Color color, double headLength, double headWidthRatio, boolean doubleHeaded)
  {
     super(false);
     _base = new MutableVector3D(base);
     _vector = new MutableVector3D(vector);
     _radius = radius;
     _grabbed = false;
     _listener = null;
    Vector3D tipVector = _vector.normalized().times(headLength).asVector3D();
    Vector3D headBase = _vector.sub(tipVector);
  
    if (doubleHeaded)
    {
      Cylinder arrowTip = new Cylinder(Vector3D.ZERO, tipVector, 0.0, radius * headWidthRatio);
      addMesh(createCylinderMesh(arrowTip, color, 10));
  
      Cylinder arrowTipCover = new Cylinder(tipVector, tipVector.add(_vector.normalized().times(0.0001).asVector3D()), radius * headWidthRatio, 0.0);
      addMesh(createCylinderMesh(arrowTipCover, color, 10));
    }
  
    Cylinder cylinder = new Cylinder(doubleHeaded? tipVector : Vector3D.ZERO, headBase, radius, radius);
  
    Cylinder arrowTip = new Cylinder(headBase, headBase.add(tipVector), radius * headWidthRatio, 0.0);
  
    Cylinder arrowTipCover = new Cylinder(headBase, headBase.sub(_vector.normalized().times(0.0001)), radius * headWidthRatio, 0.0);
  
    addMesh(createCylinderMesh(cylinder, color, 10));
    addMesh(createCylinderMesh(arrowTip, color, 20));
    addMesh(createCylinderMesh(arrowTipCover, color, 20));
  
    _state = new GLState();
    _transformGLFeature = new ModelTransformGLFeature(MutableMatrix44D.createTranslationMatrix(_base.asVector3D()).asMatrix44D());
    _state.addGLFeature(_transformGLFeature, false);
  }

  public void dispose()
  {
    _state._release();
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
  
    if (touchEvent.getTouchCount() != 1 || touchEvent.getTapCount() != 1 || touchEvent.getType() == TouchEventType.Up)
    {
      if (_grabbed && _listener != null)
      {
        _listener.onDraggingEnded(this);
      }
      _grabbed = false;
      return false;
    }
  
    final Touch touch = touchEvent.getTouch(0);
  
    Ray arrowRay = new Ray(_base.asVector3D(), _vector.asVector3D());
    Ray camRay = new Ray(ec.getCurrentCamera().getCartesianPosition(), ec.getCurrentCamera().pixel2Ray(touch.getPos()));
  
    MutableVector3D arrowPoint = new MutableVector3D();
    MutableVector3D camRayPoint = new MutableVector3D();
    Ray.closestPointsOnTwoRays(arrowRay, camRay, arrowPoint, camRayPoint);
  
//#define SELECTION_RADIUS_RATIO 4.0
  
    switch (touchEvent.getType())
    {
      case Down:
      {
        final double distArrow = arrowPoint.asVector3D().sub(arrowRay._origin).div(_vector.asVector3D()).maxAxis();
        final boolean onArrow = distArrow >= 0.&& distArrow <= 1.;
  
        if (onArrow)
        {
          final double dist = arrowPoint.asVector3D().distanceTo(camRayPoint.asVector3D());
  
          if (dist < _radius * DefineConstants.SELECTION_RADIUS_RATIO && onArrow)
          {
            _grabbedPos.set(arrowPoint);
            _baseWhenGrabbed.set(_base);
            _grabbed = true;
            return true;
          }
        }
  
        break;
      }
  
      case Move:
      {
        if (_grabbed)
        {
          MutableVector3D disp = arrowPoint.sub(_grabbedPos);
          setBase(_baseWhenGrabbed.add(disp).asVector3D());
        }
  
        break;
      }
      default:
        break;
    }
  
    return false;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    _state.setParent(glState);
    super.render(rc, _state);
  }

  public final void setBase(Vector3D base)
  {
     setBase(base, true);
  }
  public final void setBase(Vector3D base, boolean notifyListeners)
  {
    if (!base.isEquals(_base.asVector3D()))
    {
      _base.set(base);
      _transformGLFeature.setMatrix(MutableMatrix44D.createTranslationMatrix(_base.asVector3D()).asMatrix44D());
      if (_listener != null && notifyListeners)
      {
        _listener.onBaseChanged(this);
      }
    }
  }

  public final Vector3D getBase()
  {
    return _base.asVector3D();
  }

  public final void setArrowListener(ArrowListener listener)
  {
    _listener = listener;
  }

}