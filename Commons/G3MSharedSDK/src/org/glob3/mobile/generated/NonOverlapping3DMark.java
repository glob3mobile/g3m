package org.glob3.mobile.generated; 
public class NonOverlapping3DMark
{

    /*added things here:*/

    //unlike for 2D, not every node has to have an anchor, so make 3D mark a node, where some
    //may be anchors and some may not be.
    private boolean _isAnchor;
    private boolean _isVisited; //for graph traversals

    //nodes can have multiple nodes they are attached to, call these neighbors
    //edges go from this node to neighbor nodes
    private java.util.ArrayList<NonOverlapping3DMark> _neighbors = new java.util.ArrayList<NonOverlapping3DMark>();
    private NonOverlapping3DMark _anchor; //anchor also included in neighbors. node can only have one anchor

    private float _springLengthInPixels;

    private Vector3D _cartesianPos;
    private Geodetic3D _geoPosition ;

    private float _dX; //Velocity vector (pixels per second)
    private float _dY;
    private float _dZ;
    private float _fX; //Applied Force
    private float _fY;
    private float _fZ;

    private MarkWidget _widget;
  //  private MarkWidget _anchorWidget;

    private final float _springK;
    private final float _maxSpringLength;
    private final float _minSpringLength;
    private final float _electricCharge;
    //const float _anchorElectricCharge; //use regular electric charge
    private final float _maxWidgetSpeedInPixelsPerSecond;
    private final float _resistanceFactor;
    private final float _minWidgetSpeedInPixelsPerSecond;



    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength, float electricCharge, float maxWidgetSpeedInPixelsPerSecond, float minWidgetSpeedInPixelsPerSecond)
    {
       this(imageBuilderWidget, position, touchListener, springLengthInPixels, springK, maxSpringLength, minSpringLength, electricCharge, maxWidgetSpeedInPixelsPerSecond, minWidgetSpeedInPixelsPerSecond, 0.95f);
    }
    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength, float electricCharge, float maxWidgetSpeedInPixelsPerSecond)
    {
       this(imageBuilderWidget, position, touchListener, springLengthInPixels, springK, maxSpringLength, minSpringLength, electricCharge, maxWidgetSpeedInPixelsPerSecond, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength, float electricCharge)
    {
       this(imageBuilderWidget, position, touchListener, springLengthInPixels, springK, maxSpringLength, minSpringLength, electricCharge, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength)
    {
       this(imageBuilderWidget, position, touchListener, springLengthInPixels, springK, maxSpringLength, minSpringLength, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength)
    {
       this(imageBuilderWidget, position, touchListener, springLengthInPixels, springK, maxSpringLength, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK)
    {
       this(imageBuilderWidget, position, touchListener, springLengthInPixels, springK, 100.0f, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels)
    {
       this(imageBuilderWidget, position, touchListener, springLengthInPixels, 1.0f, 100.0f, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position, MarkWidgetTouchListener touchListener)
    {
       this(imageBuilderWidget, position, touchListener, 10.0f, 1.0f, 100.0f, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position)
    {
       this(imageBuilderWidget, position, null, 10.0f, 1.0f, 100.0f, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(IImageBuilder imageBuilderWidget, Geodetic3D position, MarkWidgetTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength, float electricCharge, float maxWidgetSpeedInPixelsPerSecond, float minWidgetSpeedInPixelsPerSecond, float resistanceFactor)
                                         //  float anchorElectricCharge,
    {
       _geoPosition = new Geodetic3D(position);
       _springLengthInPixels = springLengthInPixels;
       _cartesianPos = null;
       _dX = 0F;
       _dY = 0F;
       _dZ = 0F;
       _fX = 0F;
       _fY = 0F;
       _fZ = 0F;
       _widget = new MarkWidget(imageBuilderWidget);
       _springK = springK;
       _maxSpringLength = maxSpringLength;
       _minSpringLength = minSpringLength;
       _electricCharge = electricCharge;
       _maxWidgetSpeedInPixelsPerSecond = maxWidgetSpeedInPixelsPerSecond;
       _resistanceFactor = resistanceFactor;
       _minWidgetSpeedInPixelsPerSecond = minWidgetSpeedInPixelsPerSecond;
    
        if (touchListener != null)
        {
            _widget.setTouchListener(touchListener);
        }
    }

    public void dispose()
    {
        if (_cartesianPos != null)
           _cartesianPos.dispose();
    }
    public final Geodetic3D getPos()
    {
        return _geoPosition;
    }

    public final boolean isVisited()
    {
        return _isVisited;
    }
    public final boolean isAnchor()
    {
        return _isAnchor;
    }
    public final java.util.ArrayList<NonOverlapping3DMark> getNeighbors()
    {
        return _neighbors;
    }
    public final void setVisited(boolean visited)
    {
        _isVisited = visited;
    }
    public final void addEdge(NonOverlapping3DMark n)
    {
        _neighbors.add(n);
         n.addNeighbor(n);
    }
    public final void setAsAnchor()
    {
        _isAnchor = true;
        // int _colorIndex = (_colorIndex + 1) % wheelSize;
        GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(0), Angle.fromDegrees(0), 1), AltitudeMode.ABSOLUTE, new Vector3D(100, 100, 100), 16, 0, false, false, Color.fromRGBA(1, 1, 0, .5));
        GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature.setScale(1000);
    
       // _shapesRenderer->addShape(_geo3Dfeature);
    
        //todo - change color or geometry or something if it's an anchor?
        //todo - what if you add is as anchor after adding to renderer?
    }
    public final void addNeighbor(NonOverlapping3DMark n)
    {
        _neighbors.add(n);
    }
    public final void addAnchor(NonOverlapping3DMark anchor)
    {
        _neighbors.add(anchor);
        anchor.addNeighbor(this);
        _anchor = anchor;
        anchor.setAsAnchor();
    }
    public final NonOverlapping3DMark getAnchor()
    {
        return _anchor;
    }
    public final Shape getShape()
    {
        return GlobalMembersNonOverlapping3DMarksRenderer._geo3Dfeature;
    }
    //MarkWidget getWidget() const;

    public final Vector3D clampVector(Vector3D v, float min, float max)
    {
        float l = v.length();
        if(l < min)
        {
            return (v.normalized()).times(min);
        }
        if(l > max)
        {
            return (v.normalized()).times(max);
        }
    }


    public final Vector3D getCartesianPosition(Planet planet)
    {
        if (_cartesianPos == null)
        {
            _cartesianPos = new Vector3D(planet.toCartesian(_geoPosition));
        }
        return _cartesianPos;
    }

    /*void computeAnchorScreenPos(const Camera* cam, const Planet* planet){
    
        Vector2F sp(cam->point2Pixel(getCartesianPosition(planet)));
      //  _anchorWidget.setScreenPos(sp._x, sp._y);
       // _widget.setScreenPos3d(_geoPosition);
        _widget.setScreenPos(sp._x, sp._y);
    
        if (_widget.getScreenPos().isNaN()){
           // _widget.setScreenPos(sp._x, sp._y + 0.01f);
            _widget.setScreenPos3d(_geoPosition);
        }
    
    
    }*/
    
    public final void computeAnchorScreenPos(Camera cam, Planet planet)
    {
        //TODO
       // Vector2F sp(cam->point2Pixel(getCartesianPosition(planet)));
        //  _anchorWidget.setScreenPos(sp._x, sp._y);
        // _widget.setScreenPos3d(_geoPosition);
        // _widget.setScreenPos(sp._x, sp._y);
    
        //if (_widget.getScreenPos().isNaN()){
            // _widget.setScreenPos(sp._x, sp._y + 0.01f);
            //_widget.set3DPos(_geoPosition);
        //}
    
    
    }

    //Vector2F getScreenPos() const{ return _widget.getScreenPos();}
    //Vector2F getAnchorScreenPos() const{ return _anchorWidget.getScreenPos();}


    /*void NonOverlapping3DMark::applyHookesLaw(){ //Spring
        
        //Vector2F d = getScreenPos().sub(getAnchorScreenPos());
        if(getAnchor()) {
        Vector2F d = getScreenPos().sub(getAnchor()->getScreenPos());
        double mod = d.length();
        double displacement = _springLengthInPixels - mod;
        Vector2F direction = d.div((float)mod);
        
        float force = (float)(_springK * displacement);
        
        applyForce((float)(direction._x * force),
                   (float)(direction._y * force));
        }
        
        //  var d = spring.point2.p.subtract(spring.point1.p); // the direction of the spring
        //  var displacement = spring.length - d.magnitude();
        //  var direction = d.normalise();
        //
        //  // apply force to each end point
        //  spring.point1.applyForce(direction.multiply(spring.k * displacement * -0.5));
        //  spring.point2.applyForce(direction.multiply(spring.k * displacement * 0.5));
    }*/
    
    public final void render(G3MRenderContext rc, GLState glState)
    {
    
        if (_widget.isReady())
        {
            _widget.render(rc, glState);
            //_anchorWidget.render(rc, glState);
        }
        else
        {
            _widget.init(rc, rc.getCurrentCamera().getViewPortWidth(), rc.getCurrentCamera().getViewPortHeight());
           // _anchorWidget.init(rc, rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
        }
    }

    public final void applyCoulombsLaw(NonOverlapping3DMark that, Planet planet)
    {
        //get 3d positionf or both
        Vector3D d = getCartesianPosition(planet).sub(that.getCartesianPosition(planet)).normalized();
        float distance = d.length();
        Vector3D direction = d.normalized();
    
        float strength = (float)(this._electricCharge * that._electricCharge/distance *distance);
        Vector3D force = direction.times(strength);
    
        this.applyForce(force._x, force._y, force._z);
        that.applyForce(-force._x, -force._y, -force._z);
        //get distance
        //get strength
        //get force
        //apply force
    }

    /*void NonOverlapping3DMark::applyCoulombsLaw(NonOverlapping3DMark* that){ //EM
        
        Vector2F d = getScreenPos().sub(that->getScreenPos());
        double distance = d.length()  + 0.001;
        Vector2F direction = d.div((float)distance);
        
        float strength = (float)(this->_electricCharge * that->_electricCharge / (distance * distance));
        
        Vector2F force = direction.times(strength);
        
        this->applyForce(force._x, force._y);
        that->applyForce(-force._x, -force._y);
        
        //  var d = point1.p.subtract(point2.p);
        //  var distance = d.magnitude() + 0.1; // avoid massive forces at small distances (and divide by zero)
        //  var direction = d.normalise();
        //
        //  // apply force to each end point
        //  point1.applyForce(direction.multiply(this.repulsion).divide(distance * distance * 0.5));
        //  point2.applyForce(direction.multiply(this.repulsion).divide(distance * distance * -0.5));
        
    }*/
    
    public final void applyCoulombsLawFromAnchor(NonOverlapping3DMark that, Planet planet)
    {
    
        //Vector2F dAnchor = getScreenPos().sub(that->getAnchorScreenPos());
       //  Vector2F dAnchor = getScreenPos().sub(that->getScreenPos());
        Vector3D dAnchor = getCartesianPosition(planet).sub(that.getCartesianPosition(planet));
    
        double distanceAnchor = dAnchor.length() + 0.001;
        Vector3D directionAnchor = dAnchor.div((float)distanceAnchor);
    
       // float strengthAnchor = (float)(this->_electricCharge * that->_anchorElectricCharge / (distanceAnchor * distanceAnchor));
         float strengthAnchor = (float)(this._electricCharge * that._electricCharge / (distanceAnchor * distanceAnchor));
    
        this.applyForce(directionAnchor._x * strengthAnchor, directionAnchor._y * strengthAnchor, directionAnchor._z);
    }

    public final void applyHookesLaw(Planet planet)
    {
    
        //Vector2F d = getScreenPos().sub(getAnchorScreenPos());
        if (getAnchor() != null)
        {
            Vector3D d = getCartesianPosition(planet).sub(getAnchor().getCartesianPosition(planet));
            double mod = d.length();
            double displacement = _springLengthInPixels - mod;
            Vector3D direction = d.div((float)mod);
    
            float force = (float)(_springK * displacement);
    
            applyForce((float)(direction._x * force), (float)(direction._y * force), (float) direction._z * force);
        }
    
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

    public final void applyForce(float x, float y, float z)
    {
        _fX += x;
        _fY += y;
        _fZ += z;
    }

    public final void updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight, Planet planet)
    {
    
        Vector3D oldVelocity = new Vector3D(_dX, _dY, _dZ);
        Vector3D force = new Vector3D(_fX, _fY, _fZ);
    
        //Assuming Widget Mass = 1.0
        float time = (float)(elapsedMS / 1000.0);
        Vector3D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor); //Resistance force applied as x0.85
    
        //Force has been applied and must be reset
        _fX = 0F;
        _fY = 0F;
        _fZ = 0F;
    
        //Clamping Velocity
        double velocityPPS = velocity.length();
        if (velocityPPS > _maxWidgetSpeedInPixelsPerSecond)
        {
            _dX = (float)(velocity._x * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
            _dY = (float)(velocity._y * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
            _dZ = (float)(velocity._z * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
    
        }
        else
        {
            if (velocityPPS < _minWidgetSpeedInPixelsPerSecond)
            {
                _dX = 0.0F;
                _dY = 0.0F;
                _dZ = 0.0F;
            }
            else
            {
                //Normal case
                _dX = (float)velocity._x;
                _dY = (float)velocity._y;
                _dZ = (float)velocity._z;
            }
        }
    
        //Update position
        //Vector2F position = _widget.getScreenPos();
        Vector3D position = getCartesianPosition(planet);
    
        float newX = position._x + (_dX * time);
        float newY = position._y + (_dY * time);
        float newZ = position._z + (_dZ * time);
    
        if (this.getAnchor() != null)
        {
        Vector3D anchorPos = getAnchor().getCartesianPosition(planet); //getScreenPos();
    
        Vector3D temp_spring = new Vector3D(newX,newY, newZ).sub(anchorPos);
        Vector3D spring = clampVector(temp_spring, _minSpringLength, _maxSpringLength);
        Vector3D finalPos = anchorPos.add(spring);
        _widget.set3DPos(finalPos._x, finalPos._y, finalPos._z);
        _widget.clampPositionInsideScreen((int)viewportWidth, (int)viewportHeight, 5); // 5 pixels of margin
        }
    
        //TODO: update this with new graph stuffs
    
    }

    /*void NonOverlapping3DMark::updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight){
        
        Vector2D oldVelocity(_dX, _dY);
        Vector2D force(_fX, _fY);
        
        //Assuming Widget Mass = 1.0
        float time = (float)(elapsedMS / 1000.0);
        Vector2D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor); //Resistance force applied as x0.85
        
        //Force has been applied and must be reset
        _fX = 0;
        _fY = 0;
        
        //Clamping Velocity
        double velocityPPS = velocity.length();
        if (velocityPPS > _maxWidgetSpeedInPixelsPerSecond){
            _dX = (float)(velocity._x * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
            _dY = (float)(velocity._y * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
        } else{
            if (velocityPPS < _minWidgetSpeedInPixelsPerSecond){
                _dX = 0.0;
                _dY = 0.0;
            } else{
                //Normal case
                _dX = (float)velocity._x;
                _dY = (float)velocity._y;
            }
        }
        
        //Update position
        Vector2F position = _widget.getScreenPos();
        
        float newX = position._x + (_dX * time);
        float newY = position._y + (_dY * time);
        
       if(this->getAnchor()) {
        Vector2F anchorPos = getAnchor()->getScreenPos(); //getScreenPos();
        
        
        Vector2F spring = Vector2F(newX,newY).sub(anchorPos).clampLength(_minSpringLength, _maxSpringLength);
        Vector2F finalPos = anchorPos.add(spring);
        _widget.setScreenPos(  finalPos._x, finalPos._y, finalPos._z);
        _widget.clampPositionInsideScreen((int)viewportWidth, (int)viewportHeight, 5); // 5 pixels of margin
        }
        
        //TODO: update this with new graph stuffs
        
    }*/
    
    public final void onResizeViewportEvent(int width, int height)
    {
        _widget.onResizeViewportEvent(width, height);
        //_anchorWidget.onResizeViewportEvent(width, height);
    }

    public final void resetWidgetPositionVelocityAndForce()
    {
       _widget.resetPosition();
       _dX = 0F;
       _dY = 0F;
       _dZ = 0F;
       _fX = 0F;
       _fY = 0F;
       _fZ = 0F;
    }

    public final boolean onTouchEvent(float x, float y)
    {
        return _widget.onTouchEvent(x, y);
    }

}