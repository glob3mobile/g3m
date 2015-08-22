package org.glob3.mobile.generated; 
public class NonOverlappingMark
{
  private float _springLengthInPixels;

//  MutableVector2F _widgetScreenPosition;
//  MutableVector2F _anchorScreenPosition;

  private Vector3D _cartesianPos;
  private Geodetic3D _geoPosition ;

  private MutableVector2F _speed = new MutableVector2F();
  private MutableVector2F _force = new MutableVector2F();

  private MarkWidget _widget;
  private MarkWidget _anchorWidget;

  private GLState _springGLState;
  private IFloatBuffer _springVertices;
  private ViewportExtentGLFeature _springViewportExtentGLFeature;

  private final float _springK;
  private final float _maxSpringLength;
  private final float _minSpringLength;
  private final float _electricCharge;
  private final float _anchorElectricCharge;
  private final float _resistanceFactor;

  private String _id;

  private NonOverlappingMarkTouchListener _touchListener;

//  float _enclosingRadius;


  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, NonOverlappingMarkTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength, float electricCharge, float anchorElectricCharge)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, maxSpringLength, electricCharge, anchorElectricCharge, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, NonOverlappingMarkTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength, float electricCharge)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, maxSpringLength, electricCharge, 2000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, NonOverlappingMarkTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, maxSpringLength, 3000.0f, 2000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, NonOverlappingMarkTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, 0.0f, 3000.0f, 2000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, NonOverlappingMarkTouchListener touchListener, float springLengthInPixels, float springK)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, 0.0f, 0.0f, 3000.0f, 2000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, NonOverlappingMarkTouchListener touchListener, float springLengthInPixels)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, 70.0f, 0.0f, 0.0f, 3000.0f, 2000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, NonOverlappingMarkTouchListener touchListener)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, 100.0f, 70.0f, 0.0f, 0.0f, 3000.0f, 2000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, null, 100.0f, 70.0f, 0.0f, 0.0f, 3000.0f, 2000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, NonOverlappingMarkTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength, float electricCharge, float anchorElectricCharge, float resistanceFactor)
  //_widgetScreenPosition(MutableVector2F::nan()),
  //_anchorScreenPosition(MutableVector2F::nan()),
  //_enclosingRadius(0)
  {
     _cartesianPos = null;
     _speed = new MutableVector2F(MutableVector2F.zero());
     _force = new MutableVector2F(MutableVector2F.zero());
     _geoPosition = new Geodetic3D(position);
     _springLengthInPixels = springLengthInPixels;
     _springK = springK;
     _minSpringLength = minSpringLength > 0 ? minSpringLength : (springLengthInPixels * 0.25f);
     _maxSpringLength = maxSpringLength > 0 ? maxSpringLength : (springLengthInPixels * 1.25f);
     _electricCharge = electricCharge;
     _anchorElectricCharge = anchorElectricCharge;
     _resistanceFactor = resistanceFactor;
     _touchListener = touchListener;
     _springGLState = null;
     _springVertices = null;
     _springViewportExtentGLFeature = null;
    _widget = new MarkWidget(imageBuilderWidget);
    _anchorWidget = new MarkWidget(imageBuilderAnchor);
  }

  public final void setID(String id)
  {
    _id = id;
  }

  public final String getID()
  {
    return _id;
  }

  public void dispose()
  {
    if (_touchListener != null)
       _touchListener.dispose();
  
    if (_widget != null)
       _widget.dispose();
    if (_anchorWidget != null)
       _anchorWidget.dispose();
    if (_cartesianPos != null)
       _cartesianPos.dispose();
  
    if (_springGLState != null)
    {
      _springGLState._release();
    }
  
    if (_springVertices != null)
       _springVertices.dispose();
  }

  public final Vector3D getCartesianPosition(Planet planet)
  {
  // #warning toCartesian without garbage
    if (_cartesianPos == null)
    {
      _cartesianPos = new Vector3D(planet.toCartesian(_geoPosition));
    }
    return _cartesianPos;
  }

  public final void computeAnchorScreenPos(Camera camera, Planet planet)
  {
    Vector2F sp = new Vector2F(camera.point2Pixel(getCartesianPosition(planet)));
  //  _anchorScreenPosition.put(sp._x, sp._y);
  //  if (_widgetScreenPosition.isNan()) {
  //    _widgetScreenPosition.put(sp._x, sp._y + 0.01f);
  //  }
  
    _anchorWidget.setScreenPos(sp._x, sp._y);
  
    if (_widget.getScreenPos().isNan())
    {
      final float deltaX = (float)(IMathUtils.instance().nextRandomDouble() * 2 - 1);
      final float deltaY = (float)(IMathUtils.instance().nextRandomDouble() * 2 - 1);
      _widget.setScreenPos(sp._x + deltaX, sp._y + deltaY);
    }
  }

  public final Vector2F getScreenPos()
  {
     return _widget.getScreenPos();
  }
  public final Vector2F getAnchorScreenPos()
  {
     return _anchorWidget.getScreenPos();
  }

  public final void renderWidget(G3MRenderContext rc, GLState glState)
  {
    if (_widget.isReady())
    {
      if (_anchorWidget.isReady())
      {
        _widget.render(rc, glState);
  //      if (_enclosingRadius == 0) {
  //        const float w = _widget->getWidth();
  //        const float h = _widget->getHeight();
  //        _enclosingRadius = IMathUtils::instance()->sqrt( w*w + h*h ) / 2;
  //      }
      }
    }
    else
    {
      _widget.init(rc);
    }
  }

  public final void renderAnchorWidget(G3MRenderContext rc, GLState glState)
  {
    if (_anchorWidget.isReady())
    {
      if (_widget.isReady())
      {
        _anchorWidget.render(rc, glState);
      }
    }
    else
    {
      _anchorWidget.init(rc);
    }
  }

  public final void renderSpringWidget(G3MRenderContext rc, GLState glState)
  {
    if (!_widget.isReady() || !_anchorWidget.isReady())
    {
      return;
    }
  
    final Vector2F sp = getScreenPos();
    final Vector2F asp = getAnchorScreenPos();
  
    if (_springGLState == null)
    {
      _springGLState = new GLState();
  
      _springGLState.addGLFeature(new FlatColorGLFeature(Color.black()), false);
  
      _springVertices = rc.getFactory().createFloatBuffer(2 * 2);
      _springVertices.rawPut(0, sp._x);
      _springVertices.rawPut(1, -sp._y);
      _springVertices.rawPut(2, asp._x);
      _springVertices.rawPut(3, -asp._y);
  
      _springGLState.addGLFeature(new Geometry2DGLFeature(_springVertices, 2, 0, true, 0, 3.0f, true, 1.0f, Vector2F.zero()), false); // translation -  pointSize -  needsPointSize -  lineWidth -  stride -  normalized -  index -  arrayElementSize -  buffer
  
      _springViewportExtentGLFeature = new ViewportExtentGLFeature(rc.getCurrentCamera());
      _springGLState.addGLFeature(_springViewportExtentGLFeature, false);
    }
    else
    {
      _springVertices.put(0, sp._x);
      _springVertices.put(1, -sp._y);
      _springVertices.put(2, asp._x);
      _springVertices.put(3, -asp._y);
    }
  
    rc.getGL().drawArrays(GLPrimitive.lines(), 0, 2, _springGLState, rc.getGPUProgramManager()); // count -  first
  }

  public final void applyCoulombsLaw(NonOverlappingMark that)
  {
    final Vector2F d = getScreenPos().sub(that.getScreenPos());
  //  double distance = d.length() - this->_enclosingRadius/3 - that->_enclosingRadius/3;
  //  if (distance <= 0) {
  //    distance = d.length() + 0.001;
  //  }
  
    final double distance = d.length() + 0.001;
    Vector2F direction = d.div((float)distance);
  
    float strength = (float)(this._electricCharge * that._electricCharge / (distance * distance));
  
    final Vector2F force = direction.times(strength);
  
    this.applyForce(force._x, force._y);
    that.applyForce(-force._x, -force._y);
  }
  public final void applyCoulombsLawFromAnchor(NonOverlappingMark that)
  {
    Vector2F dAnchor = getScreenPos().sub(that.getAnchorScreenPos());
    double distanceAnchor = dAnchor.length() + 0.001;
  //  double distanceAnchor = dAnchor.length() - this->_enclosingRadius/3;
  //  if (distanceAnchor <= 0) {
  //    distanceAnchor = dAnchor.length() + 0.001;
  //  }
  
    Vector2F directionAnchor = dAnchor.div((float)distanceAnchor);
  
    float strengthAnchor = (float)(this._electricCharge * that._anchorElectricCharge / (distanceAnchor * distanceAnchor));
  
    this.applyForce(directionAnchor._x * strengthAnchor, directionAnchor._y * strengthAnchor);
  }

  public final void applyHookesLaw()
  {
    Vector2F d = getScreenPos().sub(getAnchorScreenPos());
    double mod = d.length();
    double displacement = _springLengthInPixels - mod;
    Vector2F direction = d.div((float)mod);
  
    float force = (float)(_springK * displacement);
  
    applyForce(direction._x * force, direction._y * force);
  }

  public final void applyForce(float x, float y)
  {
    _force.add(x, y);
  }

  public final void updatePositionWithCurrentForce(float timeInSeconds, int viewportWidth, int viewportHeight, float viewportMargin)
  {
    _speed.add(_force._x * timeInSeconds, _force._y * timeInSeconds);
    _speed.times(_resistanceFactor);
  
    //Force has been applied and must be reset
    _force.set(0, 0);
  
    //Update position
    Vector2F widgetPosition = _widget.getScreenPos();
  
    final float newX = widgetPosition._x + (_speed._x * timeInSeconds);
    final float newY = widgetPosition._y + (_speed._y * timeInSeconds);
  
    Vector2F anchorPosition = _anchorWidget.getScreenPos();
  
  //  Vector2F spring = Vector2F(newX,newY).sub(anchorPosition).clampLength(_minSpringLength, _maxSpringLength);
    Vector2F spring = new Vector2F(newX - anchorPosition._x, newY - anchorPosition._y).clampLength(_minSpringLength, _maxSpringLength);
  
    _widget.setAndClampScreenPos(anchorPosition._x + spring._x, anchorPosition._y + spring._y, viewportWidth, viewportHeight, viewportMargin);
  }

  public final void onResizeViewportEvent(int width, int height)
  {
    _widget.onResizeViewportEvent(width, height);
    _anchorWidget.onResizeViewportEvent(width, height);
  
    if (_springViewportExtentGLFeature != null)
    {
      _springViewportExtentGLFeature.changeExtent(width, height);
    }
  }

  public final void resetWidgetPositionVelocityAndForce()
  {
    _widget.resetPosition();
//    _widgetScreenPosition.put(NANF, NANF);
    _speed.set(0, 0);
    _force.set(0, 0);
  }

  public final int getWidth()
  {
    return _widget.getWidth();
  }
  public final int getHeight()
  {
    return _widget.getHeight();
  }

  public final boolean onTouchEvent(Vector2F touchedPixel)
  {
    if (_touchListener != null)
    {
      return _touchListener.touchedMark(this, touchedPixel);
    }
    return false;
  }

}