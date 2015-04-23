//
//  ForceGraphRenderer.cpp
//  G3MiOSSDK
//
//  Created by Stefanie Alfonso on 2/1/15.
//
//

#include "ForceGraphRenderer.hpp"

#include "Camera.hpp"
#include "Planet.hpp"
#include "GLState.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"

#include "Context.hpp"
#include "TexturesHandler.hpp"
#include "Camera.hpp"
#include "Vector3D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "DirectMesh.hpp"
#include "TexturedMesh.hpp"
#include "HUDPosition.hpp"
#include "HUDSize.hpp"
#include "RenderState.hpp"
#include "IImageBuilder.hpp"
#include "IImageBuilderListener.hpp"
#include "SimpleTextureMapping.hpp"
#include "MultiTextureMapping.hpp"
#include "TextureIDReference.hpp"
#include "ITimer.hpp"
#include "IFactory.hpp"
#include "IMathUtils.hpp"
#include "TouchEvent.hpp"
#include "Shape.hpp"
#include <queue>
#include <vector>
#include "EllipsoidShape.hpp"
#include "BoxShape.cpp"
#include "Mesh.hpp"
#include "MeshShape.hpp"
#include "IFloatBuffer.hpp"
#include "FloatBufferBuilder.hpp"
#include "MeshRenderer.hpp"
#include "GEOLine2DMeshSymbol.hpp"
#include "QuadShape.cpp"
#include "IMathUtils.hpp"

#pragma mark ForceGraphNode

ForceGraphNode::ForceGraphNode(Shape* anchorShape,
                               Shape* nodeShape,
                               const Geodetic3D& position,
                               ShapeTouchListener* touchListener,
                               float springLengthInMeters,
                               float springK,
                               float minSpringLength,
                               float electricCharge,
                               float resistanceFactor):
_geoPosition(new Geodetic3D(position)),
_springLengthInMeters(springLengthInMeters),
_cartesianPos(NULL),
_dX(0),
_dY(0),
_dZ(0),
_fX(0),
_fY(0),
_fZ(0),
_tX(0),
_tY(0),
_tZ(0),
_springK(springK),
_minSpringLength(minSpringLength),
_electricCharge(electricCharge),
_resistanceFactor(resistanceFactor)

{
    
    //Initialize shape to something - TODO use parameter shape
    _anchorShape = new EllipsoidShape(new Geodetic3D(*_geoPosition),
                                      ABSOLUTE,
                                      Vector3D(100000.0, 100000.0, 100000.0),
                                      (short) 16,
                                      0,
                                      false,
                                      false,
                                      Color::fromRGBA((float).5, (float)1, (float).5, (float).9));
    
    _nodeShape = new EllipsoidShape(new Geodetic3D(*_geoPosition),
                                    ABSOLUTE,
                                    Vector3D(100000.0, 100000.0, 100000.0),
                                    (short) 16,
                                    0,
                                    false,
                                    false,
                                    Color::fromRGBA((float).5, (float) 0, (float) .5, (float) .9));
    
    //set value of shape to the thing passed in
    // _anchorShape = anchorShape;
    // _nodeShape = nodeShape;
    
    ( _nodeShape)->setPosition(*_geoPosition);
    (_anchorShape)->setPosition(*_geoPosition);
    
    _shapesRenderer = new ShapesRenderer();
    _shapesRenderer->addShape(_nodeShape);
    
}

ForceGraphNode::~ForceGraphNode()
{
    delete _cartesianPos;
    delete _anchorShape;
    delete _nodeShape;
}

Vector3D ForceGraphNode::getCartesianPosition(const Planet* planet) const{
    // if (_cartesianPos == NULL){
    Vector3D translation = Vector3D(_tX, _tY, _tZ);
    /* if(this->getAnchor()) {
     _cartesianPos = new Vector3D(getAnchor()->getCartesianPosition(planet).add(translation));
     _anchorShape->setPosition(planet->toGeodetic3D(*_cartesianPos));
     // _anchorShape->setTranslation(translation);
     //_nodeShape->setTranslation(translation);
     _nodeShape->setPosition(planet->toGeodetic3D(*_cartesianPos));
     
     }
     else {*/
    _cartesianPos = new Vector3D(planet->toCartesian(*_geoPosition).add(translation));
    
    Geodetic3D gpos = planet->toGeodetic3D(*_cartesianPos);
    Vector3D test = planet->toCartesian(_anchorShape->getPosition());
    _anchorShape->setPosition(gpos);
    _nodeShape->setPosition(gpos);
    /*  _anchorShape->setTranslation(Vector3D(0, 0 ,0));
     _nodeShape->setTranslation(Vector3D(0, 0 ,0));
     _anchorShape->setTranslation(translation);
     _nodeShape->setTranslation(translation);*/
    // }
    //}
    return *_cartesianPos;
}

void ForceGraphNode::setPos(Geodetic3D pos) {
    const Geodetic3D *position = new Geodetic3D(pos);
    _geoPosition = position;
}

void ForceGraphNode::addNeighbor(ForceGraphNode* n) {
    _neighbors.push_back(n);
    std::vector<ForceGraphNode*> neighbefore = n->getNeighbors();
    
    n->getNeighbors().push_back(this);
    
    std::vector<ForceGraphNode*> neighafter = n->getNeighbors();
    
    
    /*if(n->isAnchor()) {
     setPos(n->_geoPosition);
     }
     else {*/
    //   n->setPos(Geodetic3D(Angle::fromDegrees(0), Angle::fromDegrees(0), 5));
    // }
    /* const Geodetic3D pos = Geodetic3D(n->getPos());
     
     //getting around the lack of copy contructor:
     _geoPosition.sub(_geoPosition);
     _geoPosition.add(pos);*/
    
}

void ForceGraphNode::addEdge(ForceGraphNode* n) {
    /* _neighbors.push_back(n);
     n->addNeighbor(n);
     
     const Geodetic3D pos = Geodetic3D(n->getPos());
     
     //getting around the lack of copy contructor:
     _geoPosition.sub(_geoPosition);
     _geoPosition.add(pos);*/
    _neighbors.push_back(n);
    n->getNeighbors().push_back(this);
    /*if(n->isAnchor()) {
     setPos(n->_geoPosition);
     }
     else {*/
    n->setPos(Geodetic3D(Angle::fromDegrees(0), Angle::fromDegrees(0), 5));
    // }
    /* const Geodetic3D pos = Geodetic3D(n->getPos());
     
     //getting around the lack of copy contructor:
     _geoPosition.sub(_geoPosition);
     _geoPosition.add(pos);*/
    
    
}

void ForceGraphNode::addAnchor(ForceGraphNode* anchor) {
    addNeighbor(anchor);
    _anchor = anchor;
    anchor->setAsAnchor();
    const Geodetic3D pos = Geodetic3D(anchor->getPos());
    this->setPos(pos);
    
}

void ForceGraphNode::setAsAnchor() {
    _isAnchor = true;
    // _shapesRenderer->removeAllShapes(false);
    //_shapesRenderer->addShape(_anchorShape);
    
}

ForceGraphNode* ForceGraphNode::getAnchor() const {
    return _anchor;
}

void ForceGraphNode::applyCoulombsLaw(ForceGraphNode *that, const Planet* planet, bool planetForceAdjustment){
    
    /**Force between this and that node**/
    
    //get 3d position and distance
    Vector3D pos = getCartesianPosition(planet);
    Vector3D d = getCartesianPosition(planet).sub(that->getCartesianPosition(planet));
    
    double distance = d.length();
    Vector3D direction = d.normalized();
    
    //coulomb's law
    float strength = (float) (this->_electricCharge * that->_electricCharge/(distance*distance));
    
    //if nodes start out right on top of each other, pull them apart by a small random force before doing actual calculation
    //this prevents divide by 0 -- "infinite energy"
    if(distance < 5) {
        strength = 100;
        Vector3D force = (Vector3D(rand() % 5, rand() % 5,rand() % 5)).times(strength); //TODO - random not working in java
        this->applyForce((float)force._x, (float) force._y, (float) force._z);
    }
    else {
        Vector3D force = direction.times(strength);
        this->applyForce((float) force._x, (float) force._y, (float) force._z);
    }
    
    
    /**Planet's outward force (so nodes don't go into the earth) -- TODO - extract this as another method **/
    
    Vector3D d2 =(getCartesianPosition(planet)).normalized();
    double distance2 = d2.length();
    float planetCharge = 5;
    
    //find max difference in angle that 2 nodes are -- gives an idea of how an edge might intersect with the earth:
    float maxDistance = 0;
    Vector3D* maxDirection = new Vector3D(0, 0, 0);
    float maxAngle = 0;
    float maxAngle2 = 0;
    for(ForceGraphNode* n : _neighbors) {
        Vector3D dv = pos.sub(n->getCartesianPosition(planet));
        float dist = dv.length();
        Geodetic3D vthis = (planet->toGeodetic3D(pos));
        Geodetic3D vthat = planet->toGeodetic3D(n->getCartesianPosition(planet));
        float lat = abs(vthis._latitude._degrees - vthat._latitude._degrees);
        float lon = abs(vthis._longitude._degrees - vthat._longitude._degrees);
        
        float angle = fmax(lat, lon);
        maxDistance = fmax(dist, maxDistance);
        if(angle > maxAngle) {
            maxAngle = angle;
            maxDirection = new Vector3D(dv.normalized());
        }
        //also keep track of the second max angle in case we need it
        else if (angle > maxAngle2) {
            maxAngle2 = angle;
        }
    }
    
    //adjust planet charge according to max angle - these numbers are tentative, need more test cases
    if(maxAngle > 20) {
        planetCharge = 20; //picking this so
    }
    if(maxAngle > 35) {
        planetCharge = 25;
    }
    if(maxAngle > 60) {
        planetCharge = 40;
    }
    if(maxAngle > 80) {
        // planetCharge = 70;
    }
    if(planet->toGeodetic3D(pos)._height < 1e5) {
        // planetCharge*=3;
    }
    
    Vector3D direction2 = d2.normalized();
    float strength2 = (float) ( planetCharge / distance2*distance2);
    Vector3D force2 = direction2.times(strength2).times(_resistanceFactor);
    this->applyForce((float) force2._x, (float) force2._y, (float) force2._z);
    
    
    
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

void ForceGraphNode::applyCoulombsLawFromAnchor(ForceGraphNode* that, const Planet* planet){ //EM
    
    Vector3D dAnchor = getCartesianPosition(planet).sub(that->getCartesianPosition(planet));
    
    double distanceAnchor = dAnchor.length()  + 0.001;
    Vector3D directionAnchor = dAnchor.div((float) distanceAnchor);
    
    float strengthAnchor = (float)(this->_electricCharge * that->_electricCharge / (distanceAnchor * distanceAnchor));
    
    this->applyForce((float) directionAnchor._x * strengthAnchor,
                     (float) directionAnchor._y * strengthAnchor, (float) directionAnchor._z);
}

void ForceGraphNode::applyHookesLaw(const Planet* planet){   //Spring
    //if(getAnchor() != NULL) {
    for(int i = 0; i < this->_neighbors.size(); i++) {
        Vector3D d = getCartesianPosition(planet).sub(_neighbors[i]->getCartesianPosition(planet));
        double mod = d.length();
        double displacement = _springLengthInMeters - mod;
        if(abs(displacement) > 50) { //only do this if distance is above some threshold
            Vector3D direction = d.div((float)mod);
            // float k = _electricCharge/5;
            float force = (float)(_springK * displacement)/150000; //trying to get a good value for this
            force*=_resistanceFactor;
            
            applyForce((float)(direction._x * force),
                       (float)(direction._y * force), (float) direction._z * force);
            /*      if(!_neighbors[i]->isAnchor()) {
             _neighbors[i]->applyForce((float)(-direction._x * force),
             (float)(-direction._y * force), (float) -direction._z * force);
             }*/
        }
    }
    // }
}

void ForceGraphNode::render(const G3MRenderContext* rc, GLState* glState){
    //getCartesianPosition(rc->getPlanet());
    //_shapesRenderer->render(rc, glState);
}
Vector3D ForceGraphNode::clampVector(Vector3D &v, float min, float max) const {
    double l = v.length();
    if(l < min) {
        return (v.normalized()).times(min);
    }
    if(l > max) {
        return (v.normalized()).times(max);
    }
    return v;
}

void ForceGraphNode::updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight, const Planet* planet){
    
    Vector3D oldVelocity(_dX, _dY, _dZ);
    Vector3D force(_fX, _fY, _fZ);
    
    //Assuming Widget Mass = 1.0
    float time = (float)(elapsedMS);// / 1000.0);
    Vector3D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor); //Resistance force applied as x0.85
    
    if(oldVelocity.sub(velocity).length() > 10) {//threshold for updating position
        
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
        _tX+=_dX*time;
        _tY+=_dY*time;
        _tZ+=_dZ*time;
        
        
        //clamp translation
        //Vector3D temp_translation = Vector3D(_tX, _tY, _tZ);
        // Vector3D translation = clampVector(temp_translation, -1000000000, 1000000000);
        Vector3D translation = Vector3D(_tX, _tY, _tZ);
    }
    
    
    //find the maximum, but clamped spring length - TODO this is wrong
    //  float springx = 0;
    //float springy = 0;
    //float springz = 0;
    
    //TODO
    /* for(int i = 0; i < _neighbors.size(); i++) {
     Vector3D anchorPos = _neighbors[i]->getCartesianPosition(planet);
     Vector3D temp_spring = Vector3D(newX,newY,newZ).sub(anchorPos);
     Vector3D spring = clampVector(temp_spring, _minSpringLength, 100000000000); //todo: get rid of max spring length here
     if(spring.length() > Vector3D(springx, springy, springz).length()) {
     springx = spring._x;
     springy = spring._y;
     springz = spring._z;
     }
     }
     */
    
    /* if(this->getAnchor() != NULL) {
     
     Vector3D anchorPos = getAnchor()->getCartesianPosition(planet);//getScreenPos();
     
     Vector3D temp_spring = Vector3D(newX,newY, newZ).sub(anchorPos);
     Vector3D spring = clampVector(temp_spring, _minSpringLength, _maxSpringLength);
     Vector3D finalPos = anchorPos.add(spring);
     _anchorShape->setTranslation(finalPos);
     _nodeShape->setTranslation(finalPos);
     // _widget.set3DPos(  finalPos._x, finalPos._y, finalPos._z);
     //_widget.clampPositionInsideScreen((int)viewportWidth, (int)viewportHeight, 5); // 5 pixels of margin
     }
     */
    //  _anchorShape->setTranslation(translation);
    // _nodeShape->setTranslation(translation); //TODO: spring??
    
    //Force has been applied and must be reset
    _fX = 0;
    _fY = 0;
    _fZ = 0;
    
}

void ForceGraphNode::onResizeViewportEvent(int width, int height){
    //TODO
}

bool ForceGraphNode::onTouchEvent(float x, float y){
    //TODO
    return false;
}

bool ForceGraphNode::isVisited() const {
    return _isVisited;
}

bool ForceGraphNode::isAnchor() const {
    return _isAnchor;
}
std::vector<ForceGraphNode*>& ForceGraphNode::getNeighbors() {
    return _neighbors;
}
void ForceGraphNode::setVisited(bool visited) {
    _isVisited = visited;
}

void ForceGraphRenderer::setAllVisibleAsUnvisited() {
    for(int i = 0; i < _visibleMarks.size(); i++) {
        _visibleMarks[i]->setVisited(false);
    }
}


#pragma-mark Renderer

ForceGraphRenderer::ForceGraphRenderer(ShapesRenderer* shapesRenderer, int maxVisibleMarks):
_maxVisibleMarks(maxVisibleMarks),
_lastPositionsUpdatedTime(0),
_connectorsGLState(NULL),
_shapesRenderer(shapesRenderer),
_firstIteration(true)
{
}


ForceGraphRenderer::~ForceGraphRenderer(){
    _connectorsGLState->_release();
    
    for (int i = 0; i < _marks.size(); i++) {
        delete _marks[i];
    }
}

void ForceGraphRenderer::addMark(ForceGraphNode* mark){
    _marks.push_back(mark);
    if(mark->isAnchor()) {
        _anchors.push_back(mark);
    }
    else if (mark->getAnchor() == NULL) {
        Geodetic3D p = mark->getPos();
        double r1 = (double) rand() / RAND_MAX;
        double r2 = (double) rand() / RAND_MAX; //TODO -replace with java compatible
        float randlat = -90 + r1*90;
        float randlon = -180 + r2*180;
        mark->setPos(Geodetic3D(Angle::fromDegrees(randlat), Angle::fromDegrees(randlon), 1e5));
        Geodetic3D pafter = mark->getPos();
        
    }
    else {
        double r1 = (double) rand() / RAND_MAX;
        double r2 = (double) rand() / RAND_MAX; //TODO -replace with java compatible
        Geodetic3D small = Geodetic3D::fromDegrees(10*r1, 10*r2, 1);
        mark->setPos(mark->getAnchor()->getPos().add(small));
    }
    
    _shapesRenderer->addShape(mark->getShape());
}

void ForceGraphNode::setScale(float s) {
    _anchorShape->setScale(s);
    _nodeShape->setScale(s);
}

void ForceGraphNode::resetShapePositionVelocityAndForce(){
    _dX = 0; _dY = 0; _dZ = 0; _fX = 0; _fY = 0; _fZ = 0;
    _tX = 0; _tY = 0; _tZ = 0;
    // _anchorShape->setPosition(_geoPosition);
    //_nodeShape->setPosition(_geoPosition);
}
void ForceGraphRenderer::computeMarksToBeRendered(const Camera* cam, const Planet* planet) {
    _visibleMarks.clear();
    
    //Initialize shape to something
    /* Shape* _shape = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(0),
     Angle::fromDegrees(0),
     5),
     ABSOLUTE,
     Vector3D(100000, 100000, 100000),
     16,
     0,
     false,
     false,
     Color::fromRGBA(1, 1, 1, .5));
     
     
     ForceGraphNode* center = new ForceGraphNode(_shape, _shape, Geodetic3D::fromDegrees(0, 0, -_planet->getRadii()._x));
     _visibleMarks.push_back(center);*/
    
    const Frustum* frustrum = cam->getFrustumInModelCoordinates();
    
    for (int i = 0; i < _marks.size(); i++) {
        ForceGraphNode* m = _marks[i];
        
        if (_visibleMarks.size() < _maxVisibleMarks &&
            frustrum->contains(m->getCartesianPosition(planet))){
            _visibleMarks.push_back(m);
        }
        else{
            //Resetting marks location of invisible anchors
            m->resetShapePositionVelocityAndForce();
        }
    }
}


void ForceGraphRenderer::renderConnectorLines(const G3MRenderContext* rc){
    //TODO - cylinders? lines? project 3d line onto 2d?
    
    
    
    /*if (_connectorsGLState == NULL){
     _connectorsGLState = new GLState();
     
     _connectorsGLState->addGLFeature(new FlatColorGLFeature(Color::black()), false);
     }
     
     _connectorsGLState->clearGLFeatureGroup(NO_GROUP);
     
     FloatBufferBuilderFromCartesian3D pos3D = FloatBufferBuilderFromCartesian3D(NO_CENTER, Vector3D(0, 0, 0));*/
    
    //TODO - traverse to find edges:
    /*for (int i = 0; i < _visibleMarks.size(); i++){
     Vector2F sp = _visibleMarks[i]->getScreenPos();
     // Vector2F asp = _visibleMarks[i]->getScreenPos();
     
     pos2D.add(sp._x, -sp._y);
     //pos2D.add(asp._x, -asp._y);
     
     }*/
    /*std::vector<bool> visited = std::vector<bool>(false);
     
     for(int i = 0; i < _visibleMarks.size(); i++) {
     std::queue<ForceGraphNode*> q;
     if(!(_visibleMarks[i]->isVisited())) {
     _visibleMarks[i]->setVisited(true);
     q.push(_visibleMarks[i]);
     while(!q.empty()) {
     Vector3D sp = _visibleMarks[i]->getCartesianPosition(planet);
     ForceGraphNode* mark = q.front();
     Vector3F sp2 = mark->getCartesianPosition(planet);
     pos3D.add(sp._x, -sp._y, sp._z);
     pos3D.add(sp2._x, -sp2._y, sp._z);
     for(int j = 0; j < mark->getNeighbors().size(); j++) {
     ForceGraphNode* n = mark->getNeighbors()[j];
     if(!(n->isVisited())) {
     n->setVisited(true);
     q.push(n);
     }
     }
     mark->setVisited(true);
     q.pop(); // and mark as visited here
     }
     }
     setAllVisibleAsUnvisited();
     }*/
    //TODO
    /* _connectorsGLState->addGLFeature( new Geometry2DGLFeature(pos2D.create(),
     2,
     0,
     true,
     0,
     3.0f,
     true,
     10.0f,
     Vector2F(0.0f,0.0f)),
     false);
     
     _connectorsGLState->addGLFeature(new ViewportExtentGLFeature((int)rc->getCurrentCamera()->getViewPortWidth(),
     (int)rc->getCurrentCamera()->getViewPortHeight()), false);
     
     rc->getGL()->drawArrays(GLPrimitive::lines(), 0, pos2D.size()/2, _connectorsGLState, *rc->getGPUProgramManager());*/
}



void ForceGraphRenderer::computeForces(const Camera* cam, const Planet* planet){
    
    //Compute Mark Anchor Screen Positions
    /*for (int i = 0; i < _anchors.size(); i++) {
     _anchors[i]->computeAnchorScreenPos(cam, planet);
     }*/
    
    //Compute Mark Forces
    /* for (int i = 0; i < _visibleMarks.size(); i++) {
     ForceGraphNode* mark = _visibleMarks[i];
     mark->applyHookesLaw(planet);
     
     for (int j = i+1; j < _visibleMarks.size(); j++) {
     mark->applyCoulombsLaw(_visibleMarks[j], planet);
     }
     
     for (int j = 0; j < _visibleMarks.size(); j++) {
     if (i != j && !_visibleMarks[j]->hasAnchor()){
     mark->applyCoulombsLawFromAnchor(_visibleMarks[j]);
     }
     for (int j = 0; j < _anchors.size(); j++) {
     _anchors[i]->applyCoulombsLaw(mark, planet);
     }
     }*/
    int iters = 1;
    //if(_firstIteration) iters = 10; //more iterations to start to get closer looking image
    for(int k = 0; k < iters; k++) {
        for (int i = 0; i < _visibleMarks.size(); i++) {
            ForceGraphNode* mark = _visibleMarks[i];
            if(!mark->isAnchor()) {
                mark->applyHookesLaw(planet);
                
                for (int j = 0; j < _visibleMarks.size(); j++) {
                    if(i == j) continue;
                    /*   if(_firstIteration) {
                     mark->applyCoulombsLaw(_visibleMarks[j], planet, false);
                     }
                     else {*/
                    //do planet adjustment only after getting nodes in somewhat right place (so after first iteration)
                    mark->applyCoulombsLaw(_visibleMarks[j], planet, true);
                    // }
                }
            }
        }
    }
    _firstIteration = false;
}

Shape* ForceGraphNode::getShape() {
    if(_isAnchor) return _anchorShape;
    else return _nodeShape;
}

Geodetic3D ForceGraphNode::getPos() const {
    return *_geoPosition;
}

void ForceGraphRenderer::renderMarks(const G3MRenderContext *rc, GLState *glState){
    
    renderConnectorLines(rc);
    
    //Draw Anchors and Marks
    for (int i = 0; i < _visibleMarks.size(); i++) {
        _visibleMarks[i]->getCartesianPosition(rc->getPlanet()); //updates shapes positions
        //_visibleMarks[i]->render(rc, glState);
    }
    _shapesRenderer->render(rc, glState);
}

void ForceGraphRenderer::applyForces(long long now, const Camera* cam){
    
    if (_lastPositionsUpdatedTime != 0){ //If not First frame
        
        //Update Position based on last Forces
        for (int i = 0; i < _visibleMarks.size(); i++) {
            _visibleMarks[i]->updatePositionWithCurrentForce(now - _lastPositionsUpdatedTime,
                                                             cam->getViewPortWidth(), cam->getViewPortHeight(), _planet);
        }
    }
    
    _lastPositionsUpdatedTime = now;
}

void ForceGraphRenderer::render(const G3MRenderContext* rc, GLState* glState){
    
    const Camera* cam = rc->getCurrentCamera();
    
    float altitude = cam->getGeodeticPosition()._height;
    
    _planet = rc->getPlanet();
    
    //  ShapesRenderer sr = ShapesRenderer();
    MeshRenderer _meshrender = MeshRenderer();
    
    
    float max_dist;
    for(int i = 0; i < _visibleMarks.size(); i++) {
        float s = fmax(.1 + altitude/20000000, 0);
        _visibleMarks[i]->setScale(s);
        for(int j = 0; j < _visibleMarks[i]->getNeighbors().size(); j++) {
            ForceGraphNode* neighbor = _visibleMarks[i]->getNeighbors()[j];
            Vector3D p1 =_visibleMarks[i]->getCartesianPosition(_planet);
            Vector3D p2 = neighbor->getCartesianPosition(_planet);
            Vector3D mid = (p2.add(p1)).times(0.5);
            Vector3D mid2 = Vector3D(mid._x, mid._y, mid._z);
            
            Geodetic3D p1g = Geodetic3D(_planet->toGeodetic3D(p1));
            Geodetic3D p2g = Geodetic3D(_planet->toGeodetic3D(p2));
            Geodetic3D midg = p1g.add(p2g).div(2.0f);
            
            Geodetic3D *position = new Geodetic3D(_planet->toGeodetic3D(mid2)); //midpoint
            //Geodetic3D *position = new Geodetic3D(midg);
            
            Vector3D extent = Vector3D(10000, 1000, p1.distanceTo(p2));
            //todo: rotation, mark as visited, don't allocate memory
            float borderWidth = 2;
            Color col = Color::fromRGBA((float) .5, (float) 1, (float) 1, (float) 1);
            
            // create vertices
            FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
            vertices->add(p1);
            vertices->add(p2);
            ShortBufferBuilder indices;
            indices.add((short) 0);
            indices.add((short) 1);
            
            
            Mesh* mesh = new IndexedMesh(GLPrimitive::lines(),
                                         true,
                                         vertices->getCenter(),
                                         vertices->create(),
                                         indices.create(),
                                         1,
                                         1,
                                         new Color(col));
            _meshrender.addMesh(mesh);
            
            
        }
    }
    
    // sr.render(rc, glState);
    _meshrender.render(rc, glState);
    // sr.removeAllShapes();
    //TODO: get rid of this stuff
    /*for(int i = 0; i < _visibleMarks.size(); i++) {
     _visibleMarks[i]->getShape()->setPosition(Geodetic3D::fromDegrees(0, 0, 1));
     _visibleMarks[i]->getShape()->setTranslation(_visibleMarks[i]->getCartesianPosition(_planet));
     }*/
    
    //todo: add this back
    
    //computeMarksToBeRendered(cam, _planet);
    _visibleMarks = _marks; //temporary
    
    computeForces(cam, _planet);
    
    applyForces(rc->getFrameStartTimer()->nowInMilliseconds(), cam);
    
    renderMarks(rc, glState);
}

void ForceGraphRenderer::onResizeViewportEvent(const G3MEventContext* ec, int width, int height){
    for (int i = 0; i < _marks.size(); i++) {
        _marks[i]->onResizeViewportEvent(width, height);
    }
    
}

bool ForceGraphRenderer::onTouchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent) {
    if (touchEvent->getTapCount() == 1){
        const float x = touchEvent->getTouch(0)->getPos()._x;
        const float y = touchEvent->getTouch(0)->getPos()._y;
        for (int i = 0; i < _visibleMarks.size(); i++) {
            if (_visibleMarks[i]->onTouchEvent(x,y)){
                return true;
            }
        }
    }
    return false;
}