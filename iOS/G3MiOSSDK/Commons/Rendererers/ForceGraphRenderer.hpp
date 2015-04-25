//
//  ForceGraphRenderer.hpp
//  G3MiOSSDK
//
//  Created by Stefanie Alfonso on 2/1/15.
//
//

#ifndef __G3MiOSSDK__ForceGraphRenderer__
#define __G3MiOSSDK__ForceGraphRenderer__

#include <stdio.h>
#include "DefaultRenderer.hpp"
#include "Geodetic3D.hpp"
#include "Vector2F.hpp"
#include "Vector3D.hpp"
#include "IImageBuilderListener.hpp"
#include "Shape.hpp"
#include "ShapesRenderer.hpp"

class Geodetic3D;
class Vector2D;
class Camera;
class Planet;
class GLState;


class ShapeTouchListener {
public:
    virtual ~ShapeTouchListener() {
    }
    
    virtual bool touchedShape(Shape* shape, float x, float y) = 0;
};

class ForceGraphNode{
    
    /*added things here:*/
    
    //unlike for 2D, not every node has to have an anchor, so make 3D mark a node, where some
    //may be anchors and some may not be.
    bool _isAnchor;
    bool _isVisited; //for graph traversals
    
    //shape for anchor and regular nodes:
    Shape* _anchorShape;
    Shape* _nodeShape;
    ShapesRenderer* _shapesRenderer;
    
    //nodes can have multiple nodes they are attached to, call these neighbors
    //edges go from this node to neighbor nodes
    std::vector<ForceGraphNode*> _neighbors;
    ForceGraphNode* _anchor; //anchor also included in neighbors. node can only have one anchor
    
    float _springLengthInMeters;
    
    mutable Vector3D* _cartesianPos;
    const Geodetic3D* _geoPosition;
    
    float _dX, _dY, _dZ; //Velocity vector (pixels per second)
    float _fX, _fY, _fZ; //Applied Force
    float _tX, _tY, _tZ; //current translation (cumulative dX, dY, dX)
    
    const float _springK;
    const float _minSpringLength;
    const float _electricCharge;
    const float _resistanceFactor;
    
    
public:
    
    ForceGraphNode(Shape* anchorShape, Shape* nodeShape,
                   const Geodetic3D& position,
                   ShapeTouchListener* touchListener = NULL,
                   float springLengthInMeters = 75.0f,
                   float springK = 15.0f,
                   float minSpringLength = 10.0f,
                   float electricCharge = 3500000.0f, //was 3000
                   float resistanceFactor = 0.80f);
    
    ~ForceGraphNode();
    Geodetic3D getPos() const;
    
    bool isVisited() const;
    bool isAnchor() const;
    std::vector<ForceGraphNode*>& getNeighbors();
    void setVisited(bool visited);
    void setAsAnchor(); //sets this node as an anchor, change shape (todo)
    void addNeighbor(ForceGraphNode* n); //adds n to list of neighbors
    void addAnchor(ForceGraphNode* anchor); //adds anchor to this node. Makes that node an anchor.
    ForceGraphNode* getAnchor() const;
    Shape* getShape();
    void setPos(Geodetic3D pos);
    void setScale(float s);
    //MarkWidget getWidget() const;
    
    Vector3D clampVector(Vector3D &v, float min, float max) const;
    
    Vector3D getCartesianPosition(const Planet* planet) const;
    
    void computeAnchorScreenPos(const Camera* cam, const Planet* planet);
    
    //Vector2F getScreenPos() const{ return _widget.getScreenPos();}
    //Vector2F getAnchorScreenPos() const{ return _anchorWidget.getScreenPos();}
    
    void render(const G3MRenderContext* rc, GLState* glState);
    
    void applyCoulombsLaw(ForceGraphNode* that, const Planet* planet, bool planetChargeAdjustment); //EM
    void applyCoulombsLawFromAnchor(ForceGraphNode* that, const Planet* planet);
    
    void applyHookesLaw(const Planet* planet);   //Spring
    
    void applyForce(float x, float y){
        _fX += x;
        _fY += y;
    }
    
    void applyForce(float x, float y, float z){
        _fX += x;
        _fY += y;
        _fZ += z;
    }
    
    void updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight, const Planet* planet);
    
    void onResizeViewportEvent(int width, int height);
    
    void resetShapePositionVelocityAndForce();
    
    bool onTouchEvent(float x, float y);
    
};

class ForceGraphRenderer: public DefaultRenderer{
    
    int _maxVisibleMarks;
#ifdef C_CODE
    const Planet* _planet;
#endif
#ifdef JAVA_CODE
    Planet _planet;
#endif
    
    std::vector<ForceGraphNode*> _visibleMarks;
    std::vector<ForceGraphNode*> _marks;
    std::vector<ForceGraphNode*> _anchors;
    ShapesRenderer* _shapesRenderer;
    bool _firstIteration;
    
    void computeMarksToBeRendered(const Camera* cam, const Planet* planet);
    
    long long _lastPositionsUpdatedTime;
    
    GLState * _connectorsGLState;
    void renderConnectorLines(const G3MRenderContext* rc);
    
    void computeForces(const Camera* cam, const Planet* planet);
    void renderMarks(const G3MRenderContext* rc, GLState* glState);
    void applyForces(long long now, const Camera* cam);
    
    
public:
    
    ForceGraphRenderer(ShapesRenderer* shapesRenderer, int maxVisibleMarks = 30);
    
    ~ForceGraphRenderer();
    
    void addMark(ForceGraphNode* mark);
    
    void setAllVisibleAsUnvisited();
    
    virtual RenderState getRenderState(const G3MRenderContext* rc) {
        return RenderState::ready();
    }
    
    virtual void render(const G3MRenderContext* rc,
                        GLState* glState);
    
    virtual bool onTouchEvent(const G3MEventContext* ec,
                              const TouchEvent* touchEvent);
    
    virtual void onResizeViewportEvent(const G3MEventContext* ec, int width, int height);
    
    virtual void start(const G3MRenderContext* rc) {
        
    }
    
    virtual void stop(const G3MRenderContext* rc) {
        
    }
    
    virtual SurfaceElevationProvider* getSurfaceElevationProvider() {
        return NULL;
    }
    
    virtual PlanetRenderer* getPlanetRenderer() {
        return NULL;
    }
    
    virtual bool isPlanetRenderer() {
        return false;
    }
    
};

#endif /* defined(__G3MiOSSDK__ForceGraphRenderer__) */
