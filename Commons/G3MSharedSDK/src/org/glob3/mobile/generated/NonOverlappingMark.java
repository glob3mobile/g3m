package org.glob3.mobile.generated; 
public class NonOverlappingMark
{
  private float _springLengthInPixels;

  private Vector3D _cartesianPos;
  private Geodetic3D _geoPosition ;

  private MutableVector2F _speed = new MutableVector2F();
  private MutableVector2F _force = new MutableVector2F();

  private MarkWidget _widget;
  private MarkWidget _anchorWidget;

  private final float _springK;
  private final float _maxSpringLength;
  private final float _minSpringLength;
  private final float _electricCharge;
  private final float _anchorElectricCharge;
  private final float _resistanceFactor;

  private String _id;

  private NonOverlappingMarkTouchListener _touchListener;



  //void MarkWidget::clampPositionInsideScreen(int viewportWidth, int viewportHeight, float margin) {
  //  const IMathUtils* mu = IMathUtils::instance();
  //  float x = mu->clamp(_x, _halfWidth  + margin, viewportWidth  - _halfWidth  - margin);
  //  float y = mu->clamp(_y, _halfHeight + margin, viewportHeight - _halfHeight - margin);
  //
  //  setScreenPos(x, y);
  //}
  
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
  }

  public final Vector3D getCartesianPosition(Planet planet)
  {
    if (_cartesianPos == null)
    {
      _cartesianPos = new Vector3D(planet.toCartesian(_geoPosition));
    }
    return _cartesianPos;
  }

  public final void computeAnchorScreenPos(Camera cam, Planet planet)
  {
    Vector2F sp = new Vector2F(cam.point2Pixel(getCartesianPosition(planet)));
    _anchorWidget.setScreenPos(sp._x, sp._y);
  
    if (_widget.getScreenPos().isNaN())
    {
      _widget.setScreenPos(sp._x, sp._y + 0.01f);
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

//  void render(const G3MRenderContext* rc, GLState* glState);

  //void NonOverlappingMark::render(const G3MRenderContext* rc, GLState* glState) {
  //  if (_widget->isReady() && _anchorWidget->isReady()) {
  //    _widget->render(rc, glState);
  //    _anchorWidget->render(rc, glState);
  //  }
  //  else {
  //    _widget->init(rc, rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
  //    _anchorWidget->init(rc, rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
  //  }
  //}
  
  public final void renderWidget(G3MRenderContext rc, GLState glState)
  {
    if (_widget.isReady())
    {
      _widget.render(rc, glState);
    }
    else
    {
      _widget.init(rc, rc.getCurrentCamera().getViewPortWidth(), rc.getCurrentCamera().getViewPortHeight());
    }
  }
  public final void renderAnchorWidget(G3MRenderContext rc, GLState glState)
  {
    if (_anchorWidget.isReady())
    {
      _anchorWidget.render(rc, glState);
    }
    else
    {
      _anchorWidget.init(rc, rc.getCurrentCamera().getViewPortWidth(), rc.getCurrentCamera().getViewPortHeight());
    }
  }

  public final void applyCoulombsLaw(NonOverlappingMark that)
  {
    Vector2F d = getScreenPos().sub(that.getScreenPos());
    double distance = d.length() + 0.001;
    Vector2F direction = d.div((float)distance);
  
    float strength = (float)(this._electricCharge * that._electricCharge / (distance * distance));
  
    Vector2F force = direction.times(strength);
  
    this.applyForce(force._x, force._y);
    that.applyForce(-force._x, -force._y);
  }
  public final void applyCoulombsLawFromAnchor(NonOverlappingMark that)
  {
    Vector2F dAnchor = getScreenPos().sub(that.getAnchorScreenPos());
    double distanceAnchor = dAnchor.length() + 0.001;
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
  
    _speed.add(_force.x() * timeInSeconds, _force.y() * timeInSeconds);
    _speed.times(_resistanceFactor);
  
    //Force has been applied and must be reset
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning It's needed?
    _force.set(0, 0);
  
    //Update position
    Vector2F position = _widget.getScreenPos();
  
    float newX = position._x + (_speed.x() * timeInSeconds);
    float newY = position._y + (_speed.y() * timeInSeconds);
  
    Vector2F anchorPos = _anchorWidget.getScreenPos();
  
    Vector2F spring = new Vector2F(newX,newY).sub(anchorPos).clampLength(_minSpringLength, _maxSpringLength);
    Vector2F finalPos = anchorPos.add(spring);
  
  //  _widget->setScreenPos(finalPos._x, finalPos._y);
  //  _widget->clampPositionInsideScreen(viewportWidth, viewportHeight, viewportMargin);
    _widget.setAndClampScreenPos(finalPos._x, finalPos._y, viewportWidth, viewportHeight, viewportMargin);
  }

  public final void onResizeViewportEvent(int width, int height)
  {
    _widget.onResizeViewportEvent(width, height);
    _anchorWidget.onResizeViewportEvent(width, height);
  }

  public final void resetWidgetPositionVelocityAndForce()
  {
    _widget.resetPosition();
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