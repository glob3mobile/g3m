package org.glob3.mobile.generated; 
public class NonOverlappingMark
{
  private float _springLengthInPixels;

  private Vector3D _cartesianPos;
  private Geodetic3D _geoPosition ;

  private float _dX; //Velocity vector (pixels per second)
  private float _dY;
  private float _fX; //Applied Force
  private float _fY;

  private MarkWidget _widget;
  private MarkWidget _anchorWidget;

  private final float _springK;
  private final float _maxSpringLength;
  private final float _minSpringLength;
  private final float _electricCharge;
  private final float _anchorElectricCharge;
  private final float _maxWidgetSpeedInPixelsPerSecond;
  private final float _minWidgetSpeedInPixelsPerSecond;
  private final float _resistanceFactor;

  private String _id;

  private MarkWidgetTouchListener _touchListener;


  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength, float electricCharge, float anchorElectricCharge, float minWidgetSpeedInPixelsPerSecond, float maxWidgetSpeedInPixelsPerSecond)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, maxSpringLength, electricCharge, anchorElectricCharge, minWidgetSpeedInPixelsPerSecond, maxWidgetSpeedInPixelsPerSecond, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength, float electricCharge, float anchorElectricCharge, float minWidgetSpeedInPixelsPerSecond)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, maxSpringLength, electricCharge, anchorElectricCharge, minWidgetSpeedInPixelsPerSecond, 1000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength, float electricCharge, float anchorElectricCharge)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, maxSpringLength, electricCharge, anchorElectricCharge, 5.0f, 1000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength, float electricCharge)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, maxSpringLength, electricCharge, 2000.0f, 5.0f, 1000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, maxSpringLength, 3000.0f, 2000.0f, 5.0f, 1000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, minSpringLength, 0.0f, 3000.0f, 2000.0f, 5.0f, 1000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, springK, 0.0f, 0.0f, 3000.0f, 2000.0f, 5.0f, 1000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, springLengthInPixels, 70.0f, 0.0f, 0.0f, 3000.0f, 2000.0f, 5.0f, 1000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, touchListener, 100.0f, 70.0f, 0.0f, 0.0f, 3000.0f, 2000.0f, 5.0f, 1000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position)
  {
     this(imageBuilderWidget, imageBuilderAnchor, position, null, 100.0f, 70.0f, 0.0f, 0.0f, 3000.0f, 2000.0f, 5.0f, 1000.0f, 0.95f);
  }
  public NonOverlappingMark(IImageBuilder imageBuilderWidget, IImageBuilder imageBuilderAnchor, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float minSpringLength, float maxSpringLength, float electricCharge, float anchorElectricCharge, float minWidgetSpeedInPixelsPerSecond, float maxWidgetSpeedInPixelsPerSecond, float resistanceFactor)
  {
     _cartesianPos = null;
     _dX = 0F;
     _dY = 0F;
     _fX = 0F;
     _fY = 0F;
     _geoPosition = new Geodetic3D(position);
     _springLengthInPixels = springLengthInPixels;
     _springK = springK;
     _minSpringLength = minSpringLength > 0 ? minSpringLength : (springLengthInPixels * 0.85f);
     _maxSpringLength = maxSpringLength > 0 ? maxSpringLength : (springLengthInPixels * 1.15f);
     _electricCharge = electricCharge;
     _anchorElectricCharge = anchorElectricCharge;
     _minWidgetSpeedInPixelsPerSecond = minWidgetSpeedInPixelsPerSecond;
     _maxWidgetSpeedInPixelsPerSecond = maxWidgetSpeedInPixelsPerSecond;
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

  public final void render(G3MRenderContext rc, GLState glState)
  {
  
    if (_widget.isReady() && _anchorWidget.isReady())
    {
      _widget.render(rc, glState);
      _anchorWidget.render(rc, glState);
    }
    else
    {
      _widget.init(rc, rc.getCurrentCamera().getViewPortWidth(), rc.getCurrentCamera().getViewPortHeight());
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
  
    //  var d = point1.p.subtract(point2.p);
    //  var distance = d.magnitude() + 0.1; // avoid massive forces at small distances (and divide by zero)
    //  var direction = d.normalise();
    //
    //  // apply force to each end point
    //  point1.applyForce(direction.multiply(this.repulsion).divide(distance * distance * 0.5));
    //  point2.applyForce(direction.multiply(this.repulsion).divide(distance * distance * -0.5));
  
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
  
    applyForce((float)(direction._x * force), (float)(direction._y * force));
  
    //  var d = spring.point2.p.subtract(spring.point1.p); // the direction of the spring
    //  var displacement = spring.length - d.magnitude();
    //  var direction = d.normalise();
    //
    //  // apply force to each end point
    //  spring.point1.applyForce(direction.multiply(spring.k * displacement * -0.5));
    //  spring.point2.applyForce(direction.multiply(spring.k * displacement * 0.5));
  }

  public final void applyForce(float x, float y)
  {
    _fX += x;
    _fY += y;
  }

  public final void updatePositionWithCurrentForce(double elapsedMS, int viewportWidth, int viewportHeight, float viewportMargin)
  {
  
    Vector2D oldVelocity = new Vector2D(_dX, _dY);
    Vector2D force = new Vector2D(_fX, _fY);
  
    //Assuming Widget Mass = 1.0
    float time = (float)(elapsedMS / 1000.0);
    Vector2D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor);
  
    //Force has been applied and must be reset
    _fX = 0F;
    _fY = 0F;
  
    //Clamping Velocity
    double velocityPPS = velocity.length();
    if (velocityPPS > _maxWidgetSpeedInPixelsPerSecond)
    {
      _dX = (float)(velocity._x * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
      _dY = (float)(velocity._y * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
    }
    else if (velocityPPS < _minWidgetSpeedInPixelsPerSecond)
    {
      _dX = 0.0F;
      _dY = 0.0F;
    }
    else
    {
      _dX = (float)velocity._x;
      _dY = (float)velocity._y;
    }
  
    //Update position
    Vector2F position = _widget.getScreenPos();
  
    float newX = position._x + (_dX * time);
    float newY = position._y + (_dY * time);
  
    Vector2F anchorPos = _anchorWidget.getScreenPos();
  
    Vector2F spring = new Vector2F(newX,newY).sub(anchorPos).clampLength(_minSpringLength, _maxSpringLength);
    Vector2F finalPos = anchorPos.add(spring);
  
    _widget.setScreenPos(finalPos._x, finalPos._y);
    _widget.clampPositionInsideScreen(viewportWidth, viewportHeight, viewportMargin);
  }

  public final void onResizeViewportEvent(int width, int height)
  {
    _widget.onResizeViewportEvent(width, height);
    _anchorWidget.onResizeViewportEvent(width, height);
  }

  public final void resetWidgetPositionVelocityAndForce()
  {
    _widget.resetPosition();
    _dX = 0F;
    _dY = 0F;
    _fX = 0F;
    _fY = 0F;
  }

  public final boolean isMoving()
  {
    float velocitySquared = ((_dX *_dX) + (_dY *_dY));
    return velocitySquared > (_minWidgetSpeedInPixelsPerSecond * _minWidgetSpeedInPixelsPerSecond);
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