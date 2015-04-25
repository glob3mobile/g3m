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
    _cartesianPos = new Vector3D(planet->toCartesian(*_geoPosition).add(translation));
    Geodetic3D gpos = planet->toGeodetic3D(*_cartesianPos);
    Vector3D test = planet->toCartesian(_anchorShape->getPosition());
    _anchorShape->setPosition(gpos);
    _nodeShape->setPosition(gpos);

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
        double r1 = IMathUtils::instance()->nextRandomDouble();
        double r2 = IMathUtils::instance()->nextRandomDouble();
        double r3 = IMathUtils::instance()->nextRandomDouble();
        Vector3D force = (Vector3D(r1*5, r2*5,r3*5)).times(strength); //TODO - random not working in java
        this->applyForce((float)force._x, (float) force._y, (float) force._z);
    }
    else {
        Vector3D force = direction.times(strength);
        this->applyForce((float) force._x, (float) force._y, (float) force._z);
    }
    
    
    /**Planet's outward force (so nodes don't go into the earth) -- TODO - extract this as another method **/
    
    Vector3D d2 =(getCartesianPosition(planet)).normalized();
    double distance2 = d2.length();
    if(distance > 12e6) return;
    
    float planetCharge = 2;
    
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
    //possibly adjust planet force depending on number of nodes total or in this particular cluster of nodes?
    if(maxAngle > 30) {
       // planetCharge = 5; //picking this so
    }
    if(maxAngle > 45) {
        planetCharge = 5;
    }
    if(maxAngle > 90) {
        planetCharge = 10;
    }
    if(maxAngle > 80) {
        // planetCharge = 70;
    }
    if(planet->toGeodetic3D(pos)._height < 1e5) {
         //planetCharge*=3;
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
    
    if(oldVelocity.sub(velocity).length() > 5) {//threshold for updating position
        
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
        
        Vector3D translation = Vector3D(_tX, _tY, _tZ);
    }
    

    
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
        //place randomly on the globe if it has no anchor
        Geodetic3D p = mark->getPos();
        double r1 = IMathUtils::instance()->nextRandomDouble();
        double r2 = IMathUtils::instance()->nextRandomDouble();
         float randlat = -90 + r1*90;
        float randlon = -180 + r2*180;
        mark->setPos(Geodetic3D(Angle::fromDegrees(randlat), Angle::fromDegrees(randlon), 1e5));
        Geodetic3D pafter = mark->getPos();
        
    }
    else {
        //place at a random position close to it's anchor
        double r1 = IMathUtils::instance()->nextRandomDouble();
        double r2 = IMathUtils::instance()->nextRandomDouble();
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
    //all done in renderMarks
}



void ForceGraphRenderer::computeForces(const Camera* cam, const Planet* planet){
    
    
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