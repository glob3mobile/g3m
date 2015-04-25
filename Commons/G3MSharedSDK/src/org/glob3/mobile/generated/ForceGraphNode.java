package org.glob3.mobile.generated; 
public class ForceGraphNode
{

    /*added things here:*/

    //unlike for 2D, not every node has to have an anchor, so make 3D mark a node, where some
    //may be anchors and some may not be.
    private boolean _isAnchor;
    private boolean _isVisited; //for graph traversals

    //shape for anchor and regular nodes:
    private Shape _anchorShape;
    private Shape _nodeShape;
    private ShapesRenderer _shapesRenderer;

    //nodes can have multiple nodes they are attached to, call these neighbors
    //edges go from this node to neighbor nodes
    private java.util.ArrayList<ForceGraphNode> _neighbors = new java.util.ArrayList<ForceGraphNode>();
    private ForceGraphNode _anchor; //anchor also included in neighbors. node can only have one anchor

    private float _springLengthInMeters;

    private Vector3D _cartesianPos;
    private final Geodetic3D _geoPosition;

    private float _dX; //Velocity vector (pixels per second)
    private float _dY;
    private float _dZ;
    private float _fX; //Applied Force
    private float _fY;
    private float _fZ;
    private float _tX; //current translation (cumulative dX, dY, dX)
    private float _tY;
    private float _tZ;

    private final float _springK;
    private final float _minSpringLength;
    private final float _electricCharge;
    private final float _resistanceFactor;



    public ForceGraphNode(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInMeters, float springK, float minSpringLength, float electricCharge)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInMeters, springK, minSpringLength, electricCharge, 0.80f);
    }
    public ForceGraphNode(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInMeters, float springK, float minSpringLength)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInMeters, springK, minSpringLength, 3500000.0f, 0.80f);
    }
    public ForceGraphNode(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInMeters, float springK)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInMeters, springK, 10.0f, 3500000.0f, 0.80f);
    }
    public ForceGraphNode(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInMeters)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInMeters, 15.0f, 10.0f, 3500000.0f, 0.80f);
    }
    public ForceGraphNode(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener)
    {
       this(anchorShape, nodeShape, position, touchListener, 75.0f, 15.0f, 10.0f, 3500000.0f, 0.80f);
    }
    public ForceGraphNode(Shape anchorShape, Shape nodeShape, Geodetic3D position)
    {
       this(anchorShape, nodeShape, position, null, 75.0f, 15.0f, 10.0f, 3500000.0f, 0.80f);
    }
    public ForceGraphNode(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInMeters, float springK, float minSpringLength, float electricCharge, float resistanceFactor)
    
    {
       _geoPosition = new Geodetic3D(position);
       _springLengthInMeters = springLengthInMeters;
       _cartesianPos = null;
       _dX = 0F;
       _dY = 0F;
       _dZ = 0F;
       _fX = 0F;
       _fY = 0F;
       _fZ = 0F;
       _tX = 0F;
       _tY = 0F;
       _tZ = 0F;
       _springK = springK;
       _minSpringLength = minSpringLength;
       _electricCharge = electricCharge;
       _resistanceFactor = resistanceFactor;
    
        //Initialize shape to something - TODO use parameter shape
        _anchorShape = new EllipsoidShape(new Geodetic3D(_geoPosition), AltitudeMode.ABSOLUTE, new Vector3D(100000.0, 100000.0, 100000.0), (short) 16, 0, false, false, Color.fromRGBA((float).5, (float)1, (float).5, (float).9));
    
        _nodeShape = new EllipsoidShape(new Geodetic3D(_geoPosition), AltitudeMode.ABSOLUTE, new Vector3D(100000.0, 100000.0, 100000.0), (short) 16, 0, false, false, Color.fromRGBA((float).5, (float) 0, (float).5, (float).9));
    
    
        (_nodeShape).setPosition(_geoPosition);
        (_anchorShape).setPosition(_geoPosition);
    
        _shapesRenderer = new ShapesRenderer();
        _shapesRenderer.addShape(_nodeShape);
    
    }

    public void dispose()
    {
        if (_cartesianPos != null)
           _cartesianPos.dispose();
        if (_anchorShape != null)
           _anchorShape.dispose();
        if (_nodeShape != null)
           _nodeShape.dispose();
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
    public final java.util.ArrayList<ForceGraphNode> getNeighbors()
    {
        return _neighbors;
    }
    public final void setVisited(boolean visited)
    {
        _isVisited = visited;
    }
    public final void setAsAnchor()
    {
        _isAnchor = true;
    
    }
    public final void addNeighbor(ForceGraphNode n)
    {
        _neighbors.add(n);
        java.util.ArrayList<ForceGraphNode> neighbefore = n.getNeighbors();
    
        n.getNeighbors().add(this);
    
        java.util.ArrayList<ForceGraphNode> neighafter = n.getNeighbors();
    
    
    }
    public final void addAnchor(ForceGraphNode anchor)
    {
        addNeighbor(anchor);
        _anchor = anchor;
        anchor.setAsAnchor();
        final Geodetic3D pos = new Geodetic3D(anchor.getPos());
        this.setPos(pos);
    
    }
    public final ForceGraphNode getAnchor()
    {
        return _anchor;
    }
    public final Shape getShape()
    {
        if(_isAnchor)
           return _anchorShape;
        else
           return _nodeShape;
    }
    public final void setPos(Geodetic3D pos)
    {
        final Geodetic3D position = new Geodetic3D(pos);
        _geoPosition = position;
    }
    public final void setScale(float s)
    {
        _anchorShape.setScale(s);
        _nodeShape.setScale(s);
    }
    //MarkWidget getWidget() const;

    public final Vector3D clampVector(Vector3D v, float min, float max)
    {
        double l = v.length();
        if(l < min)
        {
            return (v.normalized()).times(min);
        }
        if(l > max)
        {
            return (v.normalized()).times(max);
        }
        return v;
    }

    public final Vector3D getCartesianPosition(Planet planet)
    {
        // if (_cartesianPos == NULL){
        Vector3D translation = new Vector3D(_tX, _tY, _tZ);
        _cartesianPos = new Vector3D(planet.toCartesian_geoPosition.add(translation));
        Geodetic3D gpos = planet.toGeodetic3D(_cartesianPos);
        Vector3D test = planet.toCartesian(_anchorShape.getPosition());
        _anchorShape.setPosition(gpos);
        _nodeShape.setPosition(gpos);
    
        return _cartesianPos;
    }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    void computeAnchorScreenPos(Camera cam, Planet planet);

    //Vector2F getScreenPos() const{ return _widget.getScreenPos();}
    //Vector2F getAnchorScreenPos() const{ return _anchorWidget.getScreenPos();}

    public final void render(G3MRenderContext rc, GLState glState)
    {
        //getCartesianPosition(rc->getPlanet());
        //_shapesRenderer->render(rc, glState);
    }

    public final void applyCoulombsLaw(ForceGraphNode that, Planet planet, boolean planetForceAdjustment)
    {
    
        /**Force between this and that node**/
    
        //get 3d position and distance
        Vector3D pos = getCartesianPosition(planet);
        Vector3D d = getCartesianPosition(planet).sub(that.getCartesianPosition(planet));
    
        double distance = d.length();
        Vector3D direction = d.normalized();
    
        //coulomb's law
        float strength = (float)(this._electricCharge * that._electricCharge/(distance *distance));
    
        //if nodes start out right on top of each other, pull them apart by a small random force before doing actual calculation
        //this prevents divide by 0 -- "infinite energy"
        if(distance < 5)
        {
            strength = 100F;
            double r1 = IMathUtils.instance().nextRandomDouble();
            double r2 = IMathUtils.instance().nextRandomDouble();
            double r3 = IMathUtils.instance().nextRandomDouble();
            Vector3D force = (new Vector3D(r1 *5, r2 *5,r3 *5)).times(strength); //TODO - random not working in java
            this.applyForce((float)force._x, (float) force._y, (float) force._z);
        }
        else
        {
            Vector3D force = direction.times(strength);
            this.applyForce((float) force._x, (float) force._y, (float) force._z);
        }
    
    
        /**Planet's outward force (so nodes don't go into the earth) -- TODO - extract this as another method **/
    
        Vector3D d2 = (getCartesianPosition(planet)).normalized();
        double distance2 = d2.length();
        if(distance > 12e6)
           return;
    
        float planetCharge = 2F;
    
        //find max difference in angle that 2 nodes are -- gives an idea of how an edge might intersect with the earth:
        float maxDistance = 0F;
        Vector3D maxDirection = new Vector3D(0, 0, 0);
        float maxAngle = 0F;
        float maxAngle2 = 0F;
        for(ForceGraphNode* n : _neighbors)
        {
            Vector3D dv = pos.sub(n.getCartesianPosition(planet));
            float dist = dv.length();
            Geodetic3D vthis = (planet.toGeodetic3D(pos));
            Geodetic3D vthat = planet.toGeodetic3D(n.getCartesianPosition(planet));
            float lat = Math.abs(vthis._latitude._degrees - vthat._latitude._degrees);
            float lon = Math.abs(vthis._longitude._degrees - vthat._longitude._degrees);
    
            float angle = fmax(lat, lon);
            maxDistance = fmax(dist, maxDistance);
            if(angle > maxAngle)
            {
                maxAngle = angle;
                maxDirection = new Vector3D(dv.normalized());
            }
            //also keep track of the second max angle in case we need it
            else if (angle > maxAngle2)
            {
                maxAngle2 = angle;
            }
        }
    
        //adjust planet charge according to max angle - these numbers are tentative, need more test cases
        //possibly adjust planet force depending on number of nodes total or in this particular cluster of nodes?
        if(maxAngle > 30)
        {
           // planetCharge = 5; //picking this so
        }
        if(maxAngle > 45)
        {
            planetCharge = 5F;
        }
        if(maxAngle > 90)
        {
            planetCharge = 10F;
        }
        if(maxAngle > 80)
        {
            // planetCharge = 70;
        }
        if(planet.toGeodetic3D(pos)._height < 1e5)
        {
             //planetCharge*=3;
        }
    
        Vector3D direction2 = d2.normalized();
        float strength2 = (float)(planetCharge / distance2 *distance2);
        Vector3D force2 = direction2.times(strength2).times(_resistanceFactor);
        this.applyForce((float) force2._x, (float) force2._y, (float) force2._z);
    
    
    
        //case for a very large max angle -- how to handle?
        /*
         if(maxAngle > 60) {
         Vector3D force2 = direction2.add(*maxDirection).times(.5).times(strength2);
         this->applyForce((float) force2._x, (float) force2._y, (float) force2._z);
         }
         else {
         Vector3D force2 = direction2.times(strength2);
         //force2.times(_resistanceFactor);
         this->applyForce((float) force2._x, (float) force2._y, (float) force2._z);
         }*/
    
    }
    public final void applyCoulombsLawFromAnchor(ForceGraphNode that, Planet planet)
    {
    
        Vector3D dAnchor = getCartesianPosition(planet).sub(that.getCartesianPosition(planet));
    
        double distanceAnchor = dAnchor.length() + 0.001;
        Vector3D directionAnchor = dAnchor.div((float) distanceAnchor);
    
        float strengthAnchor = (float)(this._electricCharge * that._electricCharge / (distanceAnchor * distanceAnchor));
    
        this.applyForce((float) directionAnchor._x * strengthAnchor, (float) directionAnchor._y * strengthAnchor, (float) directionAnchor._z);
    }

    public final void applyHookesLaw(Planet planet)
    {
        //if(getAnchor() != NULL) {
        for(int i = 0; i < this._neighbors.size(); i++)
        {
            Vector3D d = getCartesianPosition(planet).sub(_neighbors.get(i).getCartesianPosition(planet));
            double mod = d.length();
            double displacement = _springLengthInMeters - mod;
            if(Math.abs(displacement) > 50) //only do this if distance is above some threshold
            {
                Vector3D direction = d.div((float)mod);
                // float k = _electricCharge/5;
                float force = (float)(_springK * displacement)/150000; //trying to get a good value for this
                force*=_resistanceFactor;
    
                applyForce((float)(direction._x * force), (float)(direction._y * force), (float) direction._z * force);
                /*      if(!_neighbors[i]->isAnchor()) {
                 _neighbors[i]->applyForce((float)(-direction._x * force),
                 (float)(-direction._y * force), (float) -direction._z * force);
                 }*/
            }
        }
        // }
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
        float time = (float)(elapsedMS); // / 1000.0);
        Vector3D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor); //Resistance force applied as x0.85
    
        if(oldVelocity.sub(velocity).length() > 5) //threshold for updating position
        {
    
            //testing this out:
            _dX = (float) velocity._x;
            _dY = (float) velocity._y;
            _dZ = (float) velocity._z;
    
    
            //Clamping Velocity
            /* double velocityPPS = velocity.length();
             if (velocityPPS > _maxWidgetSpeedInPixelsPerSecond){
             _dX = (float)(velocity._x * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
             _dY = (float)(velocity._y * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
             _dZ = (float)(velocity._z * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
            
             } else{
             if (velocityPPS < _minWidgetSpeedInPixelsPerSecond){
             _dX = 0.0;
             _dY = 0.0;
             _dZ = 0.0;
             } else{
             //Normal case
             _dX = (float)velocity._x;
             _dY = (float)velocity._y;
             _dZ = (float)velocity._z;
             }
             }*/
    
            //Update position
            Vector3D position = getCartesianPosition(planet);
    
            float newX = (float) position._x + (_dX * time);
            float newY = (float) position._y + (_dY * time);
            float newZ = (float) position._z + (_dZ * time);
    
            //update translation
            _tX+=_dX *time;
            _tY+=_dY *time;
            _tZ+=_dZ *time;
    
            Vector3D translation = new Vector3D(_tX, _tY, _tZ);
        }
    
    
    
        //Force has been applied and must be reset
        _fX = 0F;
        _fY = 0F;
        _fZ = 0F;
    
    }

    public final void onResizeViewportEvent(int width, int height)
    {
        //TODO
    }

    public final void resetShapePositionVelocityAndForce()
    {
        _dX = 0F;
        _dY = 0F;
        _dZ = 0F;
        _fX = 0F;
        _fY = 0F;
        _fZ = 0F;
        _tX = 0F;
        _tY = 0F;
        _tZ = 0F;
        // _anchorShape->setPosition(_geoPosition);
        //_nodeShape->setPosition(_geoPosition);
    }

    public final boolean onTouchEvent(float x, float y)
    {
        //TODO
        return false;
    }

}