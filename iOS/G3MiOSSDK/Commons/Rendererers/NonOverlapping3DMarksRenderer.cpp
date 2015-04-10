//
//  NonOverlappingMarkers3DRenderer.cpp
//  G3MiOSSDK
//
//  Created by Stefanie Alfonso on 2/1/15.
//
//

#include "NonOverlapping3DMarksRenderer.hpp"

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

#pragma mark NonOverlappingMark

NonOverlapping3DMark::NonOverlapping3DMark(Shape* anchorShape,
                                           Shape* nodeShape,
                                       const Geodetic3D& position,
                                       ShapeTouchListener* touchListener,
                                       float springLengthInMeters,
                                       float springK,
                                       float maxSpringLength,
                                       float minSpringLength,
                                       float electricCharge,
                                       float maxWidgetSpeedInPixelsPerSecond,
                                       float minWidgetSpeedInPixelsPerSecond,
                                       float resistanceFactor):
_geoPosition(position),
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
_maxSpringLength(maxSpringLength),
_minSpringLength(minSpringLength),
_electricCharge(electricCharge),
_maxWidgetSpeedInPixelsPerSecond(maxWidgetSpeedInPixelsPerSecond),
_resistanceFactor(resistanceFactor),
_minWidgetSpeedInPixelsPerSecond(minWidgetSpeedInPixelsPerSecond)

{
    
    //Initialize shape to something - TODO use parameter shape
    _anchorShape = new EllipsoidShape(new Geodetic3D(_geoPosition),
                                      ABSOLUTE,
                                      Vector3D(100000.0, 100000.0, 100000.0),
                                      (short) 16,
                                      0,
                                      false,
                                      false,
                                      Color::fromRGBA((float).5, (float)1, (float).5, (float).9));

    _nodeShape = new EllipsoidShape(new Geodetic3D(_geoPosition),
                                    ABSOLUTE,
                                    Vector3D(100000.0, 100000.0, 100000.0),
                                    (short) 16,
                                    0,
                                    false,
                                    false,
                                    Color::fromRGBA((float).5, (float) 0, (float) .5, (float) .9));
    
    //set value of shape to the thing passed in
   // *_anchorShape = *anchorShape;
   // *_nodeShape = *nodeShape;

    ( _nodeShape)->setPosition(_geoPosition);
    (_anchorShape)->setPosition(_geoPosition);
    
    _shapesRenderer = new ShapesRenderer();
    _shapesRenderer->addShape(_nodeShape);

}

NonOverlapping3DMark::~NonOverlapping3DMark()
{
    delete _cartesianPos;
    delete _anchorShape;
    delete _nodeShape;
}

Vector3D NonOverlapping3DMark::getCartesianPosition(const Planet* planet) const{
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
    _cartesianPos = new Vector3D(planet->toCartesian(_geoPosition).add(translation));
    
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

void NonOverlapping3DMark::addNeighbor(NonOverlapping3DMark* n) {
    _neighbors.push_back(n);
}

void NonOverlapping3DMark::addEdge(NonOverlapping3DMark* n) {
    _neighbors.push_back(n);
     n->addNeighbor(n);
}

void NonOverlapping3DMark::addAnchor(NonOverlapping3DMark* anchor) {
    _neighbors.push_back(anchor);
    anchor->addNeighbor(this);
    _anchor = anchor;
    anchor->setAsAnchor();
    const Geodetic3D pos = Geodetic3D(anchor->getPos());
    
    //getting around the lack of copy contructor:
    _geoPosition.sub(_geoPosition);
    _geoPosition.add(pos);
}

void NonOverlapping3DMark::setAsAnchor() {
    _isAnchor = true;
    _shapesRenderer->removeAllShapes(false); 
    _shapesRenderer->addShape(_anchorShape);
    
}

NonOverlapping3DMark* NonOverlapping3DMark::getAnchor() const {
    return _anchor;
}

void NonOverlapping3DMark::applyCoulombsLaw(NonOverlapping3DMark *that, const Planet* planet){
    //get 3d positionf or both
    Vector3D pos = getCartesianPosition(planet);
    Vector3D d = getCartesianPosition(planet).sub(that->getCartesianPosition(planet));//.normalized();

    double distance = d.length();
    Vector3D direction = d.normalized();
    //float k = 5;
    float strength = (float) (this->_electricCharge * that->_electricCharge/(distance*distance));
    if(distance < .01) { //right on top of each other, pull them apart by a small random force before doing actual calculation
        strength = 1;
#if C_CODE
        Vector3D force = (Vector3D(rand() % 5, rand() % 5, rand() % 5)).times(strength);
#endif
        
#if JAVACODE
          Vector3D force = (new Vector3D((Math.random()*100) % 5, (Math.random()* 100) % 5, (Math.random()* 100) % 5)).times(strength);
#endif
        this->applyForce((float)force._x, (float) force._y, (float) force._z);
        
}
    else {
         Vector3D force = direction.times(strength);
         this->applyForce(force._x, force._y, force._z);
    }

    //force from center of planet: - TODO: it's making it go in the z direction instead of x direction?? why?
    Vector3D d2 =(getCartesianPosition(planet)).normalized();
    double distance2 = d2.length();
    float planetCharge = 1;
    Vector3D direction2 = d2.normalized();
    float strength2 = (float) ( planetCharge / distance2*distance2);
    Vector3D force2 = direction2.times(strength2);
    
    this->applyForce((float) force2._x, (float) force2._y, (float) force2._z); //why does it do what is expected only if I swap x and z...?
  //  this->applyForce(force._x, force._y, force._z);
    
    
}

void NonOverlapping3DMark::applyCoulombsLawFromAnchor(NonOverlapping3DMark* that, const Planet* planet){ //EM
    
    Vector3D dAnchor = getCartesianPosition(planet).sub(that->getCartesianPosition(planet));

    double distanceAnchor = dAnchor.length()  + 0.001;
    Vector3D directionAnchor = dAnchor.div((float) distanceAnchor);

    float strengthAnchor = (float)(this->_electricCharge * that->_electricCharge / (distanceAnchor * distanceAnchor));
    
    this->applyForce(directionAnchor._x * strengthAnchor,
                     directionAnchor._y * strengthAnchor, directionAnchor._z);
}

void NonOverlapping3DMark::applyHookesLaw(const Planet* planet){   //Spring
    //if(getAnchor() != NULL) {
    for(int i = 0; i < this->_neighbors.size(); i++) {
        Vector3D d = getCartesianPosition(planet).sub(_neighbors[i]->getCartesianPosition(planet));
        double mod = d.length();
        double displacement = _springLengthInMeters - mod;
        if(abs(mod) > 100) { //only do this if distance is above some threshold
        Vector3D direction = d.div((float)mod);
      // float k = _electricCharge/5;
        float force = (float)(_springK * displacement)/1000000;
        
        applyForce((float)(direction._x * force),
                   (float)(direction._y * force), (float) direction._z * force); //swap x and z here??
        }
    }
   // }
}

void NonOverlapping3DMark::render(const G3MRenderContext* rc, GLState* glState){
    //getCartesianPosition(rc->getPlanet());
    _shapesRenderer->render(rc, glState);
}
Vector3D NonOverlapping3DMark::clampVector(Vector3D &v, float min, float max) const {
    double l = v.length();
    if(l < min) {
        return (v.normalized()).times(min);
    }
    if(l > max) {
        return (v.normalized()).times(max);
    }
    return v;
}

void NonOverlapping3DMark::updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight, const Planet* planet){
    
    Vector3D oldVelocity(_dX, _dY, _dZ);
    Vector3D force(_fX, _fY, _fZ);
    
    //Assuming Widget Mass = 1.0
    float time = (float)(elapsedMS);// / 1000.0);
    Vector3D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor); //Resistance force applied as x0.85
    
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
    
    
    
    //find the maximum, but clamped spring length - TODO this is wrong
    float springx = 0;
    float springy = 0;
    float springz = 0;
    
    //TODO
   /* for(int i = 0; i < _neighbors.size(); i++) {
        Vector3D anchorPos = _neighbors[i]->getCartesianPosition(planet);
        Vector3D temp_spring = Vector3D(newX,newY,newZ).sub(anchorPos);
        Vector3D spring = clampVector(temp_spring, _minSpringLength, _maxSpringLength);
        if(spring.length() > Vector3D(springx, springy, springz).length()) {
            springx = spring._x;
            springy = spring._y;
            springz = spring._z;
        }
    }*/
    

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

void NonOverlapping3DMark::onResizeViewportEvent(int width, int height){
    //TODO
}

bool NonOverlapping3DMark::onTouchEvent(float x, float y){
    //TODO
    return false;
}

bool NonOverlapping3DMark::isVisited() const {
    return _isVisited;
}

bool NonOverlapping3DMark::isAnchor() const {
    return _isAnchor;
}
std::vector<NonOverlapping3DMark*> NonOverlapping3DMark::getNeighbors() const {
    return _neighbors;
}
void NonOverlapping3DMark::setVisited(bool visited) {
    _isVisited = visited;
}

void NonOverlapping3DMarksRenderer::setAllVisibleAsUnvisited() {
    for(int i = 0; i < _visibleMarks.size(); i++) {
        _visibleMarks[i]->setVisited(false);
    }
}


#pragma-mark Renderer

NonOverlapping3DMarksRenderer::NonOverlapping3DMarksRenderer(int maxVisibleMarks):
_maxVisibleMarks(maxVisibleMarks),
_lastPositionsUpdatedTime(0),
_connectorsGLState(NULL)
{
}


NonOverlapping3DMarksRenderer::~NonOverlapping3DMarksRenderer(){
    _connectorsGLState->_release();
    
    for (int i = 0; i < _marks.size(); i++) {
        delete _marks[i];
    }
}

void NonOverlapping3DMarksRenderer::addMark(NonOverlapping3DMark* mark){
    _marks.push_back(mark);
    if(mark->isAnchor()) {
        _anchors.push_back(mark);
    }
    
}
void NonOverlapping3DMark::resetShapePositionVelocityAndForce(){
     _dX = 0; _dY = 0; _dZ = 0; _fX = 0; _fY = 0; _fZ = 0;
     _tX = 0; _tY = 0; _tZ = 0;
   // _anchorShape->setPosition(_geoPosition);
    //_nodeShape->setPosition(_geoPosition);
 }
void NonOverlapping3DMarksRenderer::computeMarksToBeRendered(const Camera* cam, const Planet* planet) {
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
    
    
    NonOverlapping3DMark* center = new NonOverlapping3DMark(_shape, _shape, Geodetic3D::fromDegrees(0, 0, -_planet->getRadii()._x));
    _visibleMarks.push_back(center);*/
    
    const Frustum* frustrum = cam->getFrustumInModelCoordinates();
    
    for (int i = 0; i < _marks.size(); i++) {
        NonOverlapping3DMark* m = _marks[i];
        
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


void NonOverlapping3DMarksRenderer::renderConnectorLines(const G3MRenderContext* rc){
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
        std::queue<NonOverlapping3DMark*> q;
        if(!(_visibleMarks[i]->isVisited())) {
            _visibleMarks[i]->setVisited(true);
            q.push(_visibleMarks[i]);
            while(!q.empty()) {
                Vector3D sp = _visibleMarks[i]->getCartesianPosition(planet);
                NonOverlapping3DMark* mark = q.front();
                Vector3F sp2 = mark->getCartesianPosition(planet);
                pos3D.add(sp._x, -sp._y, sp._z);
                pos3D.add(sp2._x, -sp2._y, sp._z);
                for(int j = 0; j < mark->getNeighbors().size(); j++) {
                    NonOverlapping3DMark* n = mark->getNeighbors()[j];
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



void NonOverlapping3DMarksRenderer::computeForces(const Camera* cam, const Planet* planet){
    
    //Compute Mark Anchor Screen Positions
    /*for (int i = 0; i < _anchors.size(); i++) {
        _anchors[i]->computeAnchorScreenPos(cam, planet);
    }*/
    
    //Compute Mark Forces
   /* for (int i = 0; i < _visibleMarks.size(); i++) {
        NonOverlapping3DMark* mark = _visibleMarks[i];
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
    
    for (int i = 0; i < _visibleMarks.size(); i++) {
        NonOverlapping3DMark* mark = _visibleMarks[i];
        if(!mark->isAnchor()) {
           mark->applyHookesLaw(planet);
            
            for (int j = 0; j < _visibleMarks.size(); j++) {
                if(i == j) continue;
                mark->applyCoulombsLaw(_visibleMarks[j], planet);
                }
            }
    }
}

Shape* NonOverlapping3DMark::getShape() {
    if(_isAnchor) return _anchorShape;
    else return _nodeShape;
}

Geodetic3D NonOverlapping3DMark::getPos() const {
    return _geoPosition;
}

void NonOverlapping3DMarksRenderer::renderMarks(const G3MRenderContext *rc, GLState *glState){
    
    renderConnectorLines(rc);
    
    //Draw Anchors and Marks
    for (int i = 0; i < _visibleMarks.size(); i++) {
        _visibleMarks[i]->getCartesianPosition(rc->getPlanet()); //updates shapes positions
        _visibleMarks[i]->render(rc, glState);
    }
}

void NonOverlapping3DMarksRenderer::applyForces(long long now, const Camera* cam){
    
    if (_lastPositionsUpdatedTime != 0){ //If not First frame
        
        //Update Position based on last Forces
        for (int i = 0; i < _visibleMarks.size(); i++) {
                _visibleMarks[i]->updatePositionWithCurrentForce(now - _lastPositionsUpdatedTime,
                                                             cam->getViewPortWidth(), cam->getViewPortHeight(), _planet);
        }
    }
    
    _lastPositionsUpdatedTime = now;
}

void NonOverlapping3DMarksRenderer::render(const G3MRenderContext* rc, GLState* glState){

    const Camera* cam = rc->getCurrentCamera();
    _planet = rc->getPlanet();
  
        ShapesRenderer sr = ShapesRenderer();
        MeshRenderer _meshrender = MeshRenderer();
    
    
    //TODO: edges working as expected. Midpoint seems to be correct, but not being drawn in the right position.
    for(int i = 0; i < _visibleMarks.size(); i++) {
      
        for(int j = 0; j < _visibleMarks[i]->getNeighbors().size(); j++) {
            NonOverlapping3DMark* neighbor = _visibleMarks[i]->getNeighbors()[j];
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

void NonOverlapping3DMarksRenderer::onResizeViewportEvent(const G3MEventContext* ec, int width, int height){
    for (int i = 0; i < _marks.size(); i++) {
        _marks[i]->onResizeViewportEvent(width, height);
    }
    
}

bool NonOverlapping3DMarksRenderer::onTouchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent) {
    
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