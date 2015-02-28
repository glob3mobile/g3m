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
#include "EllipsoidShape.hpp"
#include "ShapesRenderer.hpp"
#include <queue>
#include <vector>

#pragma mark MarkWidget

ShapesRenderer *_shapesRenderer;
Shape *_geo3Dfeature;
float _radius = 100;

MarkWidget::MarkWidget(IImageBuilder* imageBuilder):
_image(NULL),
_imageBuilder(imageBuilder),
_viewportExtent(NULL),
_geo2Dfeature(NULL),
_glState(NULL),
/*_x(NANF),
_y(NANF),
_z(NANF),*/
_halfHeight(0),
_halfWidth(0),
_touchListener(NULL)
{

}

MarkWidget::~MarkWidget()
{
    delete _image;
    if(_imageBuilder) {
       delete _imageBuilder; 
    }
    delete _touchListener;
    
    _glState->_release();
}

void MarkWidget::init(const G3MRenderContext *rc, int viewportWidth, int viewportHeight){
    if (_glState == NULL){
        _glState = new GLState();
        _viewportExtent = new ViewportExtentGLFeature(viewportWidth, viewportHeight);
        
        _texHandler = rc->getTexturesHandler();
        _imageBuilder->build(rc, new WidgetImageListener(this), true);
        
        _glState->addGLFeature(_viewportExtent, false);
        _radius = 100000;
       // _planet = rc->getPlanet();
       
    }
}

void MarkWidget::prepareWidget(const IImage* image,
                               const std::string& imageName){
    _image = image;
    _imageName = imageName;
    
    _halfWidth = image->getWidth() / 2;
    _halfHeight = image->getHeight() / 2;
    
     _shapesRenderer = new ShapesRenderer();
    
    FloatBufferBuilderFromCartesian2D pos2D;
    pos2D.add( -_halfWidth, -_halfHeight); //vertex 1
    pos2D.add( -_halfWidth, _halfHeight); //vertex 2
    pos2D.add( _halfWidth, -_halfHeight); //vertex 3
    pos2D.add( _halfWidth, _halfHeight); //vertex 4
    
    
    //const int wheelSize = 7;
   // int _colorIndex = (_colorIndex + 1) % wheelSize;

   _geo3Dfeature =  new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(0),
                                      Angle::fromDegrees(0),
                                      5),
                       ABSOLUTE,
                       Vector3D(100, 100, 100),
                       16,
                       0,
                       false,
                       false,
                       Color::fromRGBA(1, 0, 0, .5));
    _geo3Dfeature->setScale(1000);
    
     _shapesRenderer->addShape(_geo3Dfeature);


   /* _geo2Dfeature = new Geometry2DGLFeature(pos2D.create(),
                                            2,
                                            0,
                                            true,
                                            0,
                                            1.0f,
                                            true,
                                            10.0f,
                                            Vector2F(_x, _y));*/
   
    
    
   /* _glState->addGLFeature(_geo2Dfeature,
                           false);*/
    
   /* FloatBufferBuilderFromCartesian2D texCoords;
    texCoords.add( 0.0f, 1.0f); //vertex 1
    texCoords.add( 0.0f, 0.0f); //vertex 2
    texCoords.add( 1.0f, 1.0f); //vertex 3
    texCoords.add( 1.0f, 0.0f); //vertex 4
    
    const TextureIDReference* textureID = _texHandler->getTextureIDReference(_image,
                                                                             GLFormat::rgba(),
                                                                             _imageName,
                                                                             false);
    
    SimpleTextureMapping* textureMapping = new SimpleTextureMapping(textureID,
                                                                    texCoords.create(),
                                                                    true,
                                                                    true);


    
    textureMapping->modifyGLState(*_glState);*/
}

void MarkWidget::render(const G3MRenderContext *rc, GLState *glState){
  // rc->getGL()->drawArrays(GLPrimitive::triangleStrip(), 0, 4, _glState, *rc->getGPUProgramManager());
    _shapesRenderer->render(rc, glState);
}

void MarkWidget::set3DPos(float x, float y, float z){
    if(_geo3Dfeature != NULL) {
     //_geo3Dfeature->setPosition(Geodetic3D::fromDegrees(x, y, 3));
     _geo3Dfeature->setTranslation(x, y, z);
    }
    /*_x = x;
    _y = y;
    _z = z;*/
}
/*
 
 void MarkWidget::setScreenPos(float x, float y){
 if (_geo2Dfeature != NULL){
 _geo2Dfeature->setTranslation(x, y);
 }
 if(_geo3Dfeature != NULL) {
 //_geo3Dfeature->setPosition(Geodetic3D::fromDegrees(x, y, 3));
 _geo3Dfeature->setTranslation(x, y, 3);
 }
 _x = x;
 _y = y;
 }
 /*
 */
/*
void MarkWidget::setScreenPos3d(Geodetic3D &geo3d){
    if (_geo2Dfeature != NULL){
        //_geo2Dfeature->setTranslation(x, y);
    }
    if(_geo3Dfeature != NULL) {
        _geo3Dfeature->setPosition(geo3d);
    }
    //_x = x;
    //_y = y;
}*/

void MarkWidget::resetPosition(){
    if (_geo2Dfeature != NULL){
        _geo2Dfeature->setTranslation(0,0);
    }
    if(_geo3Dfeature != NULL) {
        _geo3Dfeature->setPosition(Geodetic3D::fromDegrees(0, 0, 3));
    }
    /*_x = NANF;
    _y = NANF;*/
}

void MarkWidget::onResizeViewportEvent(int width, int height){
    if (_viewportExtent != NULL){
        _viewportExtent->changeExtent(width, height);
    }
    _radius = width;
}

/*const Planet* MarkWidget::getPlanet() {
    return _planet;
}*/


void MarkWidget::clampPositionInsideScreen(int viewportWidth, int viewportHeight, int margin){
   /* const IMathUtils* mu = IMathUtils::instance();
    float x = mu->clamp(_x, _halfWidth + margin, viewportWidth - _halfWidth - margin);
    float y = mu->clamp(_y, _halfHeight + margin, viewportHeight - _halfHeight - margin);
    
    setScreenPos(x, y);*/
    //TODO
}

bool MarkWidget::onTouchEvent(float x, float y){
    
    //TODO
    /*const IMathUtils* mu = IMathUtils::instance();
    //float _x = pointtoPixel(_geo3Dfeature->getPosition())
    if (mu->isBetween(x, _x - _halfWidth, _x + _halfWidth) &&
        mu->isBetween(y, _y - _halfHeight, _y + _halfHeight)){
        if (_touchListener != NULL){
            _touchListener->touchedMark(this, x, y);
        }
        return true;
        //todo: does zoom resize stuff go here?
    }
    return false;*/
}

#pragma mark NonOverlappingMark

NonOverlapping3DMark::NonOverlapping3DMark(IImageBuilder* imageBuilderWidget,
                                       const Geodetic3D& position,
                                       MarkWidgetTouchListener* touchListener,
                                       float springLengthInPixels,
                                       float springK,
                                       float maxSpringLength,
                                       float minSpringLength,
                                       float electricCharge,
                                     //  float anchorElectricCharge,
                                       float maxWidgetSpeedInPixelsPerSecond,
                                       float minWidgetSpeedInPixelsPerSecond,
                                       float resistanceFactor):
_geoPosition(position),
_springLengthInPixels(springLengthInPixels),
_cartesianPos(NULL),
_dX(0),
_dY(0),
_dZ(0),
_fX(0),
_fY(0),
_fZ(0),
_widget(MarkWidget(imageBuilderWidget)),
_springK(springK),
_maxSpringLength(maxSpringLength),
_minSpringLength(minSpringLength),
_electricCharge(electricCharge),
_maxWidgetSpeedInPixelsPerSecond(maxWidgetSpeedInPixelsPerSecond),
_resistanceFactor(resistanceFactor),
_minWidgetSpeedInPixelsPerSecond(minWidgetSpeedInPixelsPerSecond)
{
    
    if (touchListener != NULL){
        _widget.setTouchListener(touchListener);
    }
}

NonOverlapping3DMark::~NonOverlapping3DMark()
{
    delete _cartesianPos;
}

Vector3D NonOverlapping3DMark::getCartesianPosition(const Planet* planet) const{
    if (_cartesianPos == NULL){
        _cartesianPos = new Vector3D(planet->toCartesian(_geoPosition));
    }
    return *_cartesianPos;
}


/*void NonOverlapping3DMark::computeAnchorScreenPos(const Camera* cam, const Planet* planet){
    
    Vector2F sp(cam->point2Pixel(getCartesianPosition(planet)));
  //  _anchorWidget.setScreenPos(sp._x, sp._y);
   // _widget.setScreenPos3d(_geoPosition);
    _widget.setScreenPos(sp._x, sp._y);
    
    if (_widget.getScreenPos().isNaN()){
       // _widget.setScreenPos(sp._x, sp._y + 0.01f);
        _widget.setScreenPos3d(_geoPosition);
    }
    
    
}*/

void NonOverlapping3DMark::computeAnchorScreenPos(const Camera* cam, const Planet* planet){
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
}

void NonOverlapping3DMark::setAsAnchor() {
    _isAnchor = true;
    // int _colorIndex = (_colorIndex + 1) % wheelSize;
    _geo3Dfeature =  new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(0),
                                                       Angle::fromDegrees(0),
                                                       1),
                                        ABSOLUTE,
                                        Vector3D(100, 100, 100),
                                        16,
                                        0,
                                        false,
                                        false,
                                        Color::fromRGBA(1, 1, 0, .5));
    _geo3Dfeature->setScale(1000);
    
   // _shapesRenderer->addShape(_geo3Dfeature);

    //todo - change color or geometry or something if it's an anchor?
    //todo - what if you add is as anchor after adding to renderer?
}

NonOverlapping3DMark* NonOverlapping3DMark::getAnchor() const {
    return _anchor;
}

void NonOverlapping3DMark::applyCoulombsLaw(NonOverlapping3DMark *that, const Planet* planet){
    //get 3d positionf or both
    Vector3D d = getCartesianPosition(planet).sub(that->getCartesianPosition(planet)).normalized();
    float distance = d.length();
    Vector3D direction = d.normalized();
    
    float strength = (float) (this->_electricCharge * that->_electricCharge/distance*distance);
    Vector3D force = direction.times(strength);
    
    this->applyForce(force._x, force._y, force._z);
    that->applyForce(-force._x, -force._y, -force._z);
}

void NonOverlapping3DMark::applyCoulombsLawFromAnchor(NonOverlapping3DMark* that, const Planet* planet){ //EM
    
    Vector3D dAnchor = getCartesianPosition(planet).sub(that->getCartesianPosition(planet));

    double distanceAnchor = dAnchor.length()  + 0.001;
    Vector3D directionAnchor = dAnchor.div((float)distanceAnchor);

    float strengthAnchor = (float)(this->_electricCharge * that->_electricCharge / (distanceAnchor * distanceAnchor));
    
    this->applyForce(directionAnchor._x * strengthAnchor,
                     directionAnchor._y * strengthAnchor, directionAnchor._z);
}

void NonOverlapping3DMark::applyHookesLaw(const Planet* planet){   //Spring
    if(getAnchor()) {
        Vector3D d = getCartesianPosition(planet).sub(getAnchor()->getCartesianPosition(planet));
        double mod = d.length();
        double displacement = _springLengthInPixels - mod;
        Vector3D direction = d.div((float)mod);
        
        float force = (float)(_springK * displacement);
        
        applyForce((float)(direction._x * force),
                   (float)(direction._y * force), (float) direction._z * force);
    }
}

void NonOverlapping3DMark::render(const G3MRenderContext* rc, GLState* glState){
    
    if (_widget.isReady()){
        _widget.render(rc, glState);
        //_anchorWidget.render(rc, glState);
    } else{
        _widget.init(rc, rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
       // _anchorWidget.init(rc, rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
    }
}
Vector3D NonOverlapping3DMark::clampVector(Vector3D &v, float min, float max) const {
    float l = v.length();
    if(l < min) {
        return (v.normalized()).times(min);
    }
    if(l > max) {
        return (v.normalized()).times(max);
    }
}

void NonOverlapping3DMark::updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight, const Planet* planet){
    
    Vector3D oldVelocity(_dX, _dY, _dZ);
    Vector3D force(_fX, _fY, _fZ);
    
    //Assuming Widget Mass = 1.0
    float time = (float)(elapsedMS / 1000.0);
    Vector3D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor); //Resistance force applied as x0.85
    
    //Force has been applied and must be reset
    _fX = 0;
    _fY = 0;
    _fZ = 0;
    
    //Clamping Velocity
    double velocityPPS = velocity.length();
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
    }
    
    //Update position
    //Vector2F position = _widget.getScreenPos();
    Vector3D position = getCartesianPosition(planet);
    
    float newX = position._x + (_dX * time);
    float newY = position._y + (_dY * time);
    float newZ = position._z + (_dZ * time);
    
    if(this->getAnchor()) {
    Vector3D anchorPos = getAnchor()->getCartesianPosition(planet);//getScreenPos();
        
    Vector3D temp_spring = Vector3D(newX,newY, newZ).sub(anchorPos);
    Vector3D spring = clampVector(temp_spring, _minSpringLength, _maxSpringLength);
    Vector3D finalPos = anchorPos.add(spring);
    _widget.set3DPos(  finalPos._x, finalPos._y, finalPos._z);
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
    Vector2F anchorPos = getAnchor()->getScreenPos();//getScreenPos();
    
    
    Vector2F spring = Vector2F(newX,newY).sub(anchorPos).clampLength(_minSpringLength, _maxSpringLength);
    Vector2F finalPos = anchorPos.add(spring);
    _widget.setScreenPos(  finalPos._x, finalPos._y, finalPos._z);
    _widget.clampPositionInsideScreen((int)viewportWidth, (int)viewportHeight, 5); // 5 pixels of margin
    }
    
    //TODO: update this with new graph stuffs
    
}*/

void NonOverlapping3DMark::onResizeViewportEvent(int width, int height){
    _widget.onResizeViewportEvent(width, height);
    //_anchorWidget.onResizeViewportEvent(width, height);
}

bool NonOverlapping3DMark::onTouchEvent(float x, float y){
    return _widget.onTouchEvent(x, y);
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

void NonOverlapping3DMarksRenderer::computeMarksToBeRendered(const Camera* cam, const Planet* planet) {
    _visibleMarks.clear();
    
    const Frustum* frustrum = cam->getFrustumInModelCoordinates();
    
    for (int i = 0; i < _marks.size(); i++) {
        NonOverlapping3DMark* m = _marks[i];
        
        if (_visibleMarks.size() < _maxVisibleMarks &&
            frustrum->contains(m->getCartesianPosition(planet))){
            _visibleMarks.push_back(m);
        }
        else{
            //Resetting marks location of invisible anchors
            m->resetWidgetPositionVelocityAndForce();
        }
    }
}


void NonOverlapping3DMarksRenderer::renderConnectorLines(const G3MRenderContext* rc){
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
    }
    
    //Compute Mark Forces
    for (int i = 0; i < _visibleMarks.size(); i++) {
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
}

Shape* NonOverlapping3DMark::getShape() {
    return _geo3Dfeature;
}

Geodetic3D NonOverlapping3DMark::getPos() const {
    return _geoPosition;
}

void NonOverlapping3DMarksRenderer::renderMarks(const G3MRenderContext *rc, GLState *glState){

    //renderConnectorLines(rc);
    
    //Draw Anchors and Marks
    for (int i = 0; i < _visibleMarks.size(); i++) {
       // _visibleMarks[i]->getShape()->setTranslation(_visibleMarks[i]->getCartesianPosition(_planet));
        _visibleMarks[i]->render(rc, glState);
    }
}

/*MarkWidget NonOverlapping3DMark::getWidget() const {
    return _widget;
}*/

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
    
    //TODO: get rid of this stuff
    for(int i = 0; i < _visibleMarks.size(); i++) {
        _visibleMarks[i]->getShape()->setPosition(Geodetic3D::fromDegrees(0, 0, 1));
        _visibleMarks[i]->getShape()->setTranslation(_visibleMarks[i]->getCartesianPosition(_planet));
    }
    
    //todo: add this back
    
    //computeMarksToBeRendered(cam, _planet);
    _visibleMarks = _marks;
    
    computeForces(cam, _planet);
    
   // applyForces(rc->getFrameStartTimer()->nowInMilliseconds(), cam);
    
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