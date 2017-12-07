//
//  PipesRenderer.hpp
//  EIFER App
//
//  Created by Chano on 23/11/17.
//
//

#ifndef PipesRenderer_hpp
#define PipesRenderer_hpp

#include <stdio.h>

#include <G3MiOSSDK/DefaultRenderer.hpp>
#include <G3MiOSSDK/Cylinder.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>

#include "ViewController.h"

class PipeTouchedListener{
public:
    virtual ~PipeTouchedListener(){}
    virtual void onPipeTouched(Cylinder* cylinder, Cylinder::CylinderMeshInfo info) = 0;
    
};

class PipesRenderer : public DefaultRenderer {
    
    MeshRenderer *pipesRenderer;
    __weak ViewController *vC;
    PipeTouchedListener *_listener;
    bool _holeMode;
    const Camera* _lastCamera;
    
public:
    
    PipesRenderer(MeshRenderer *pRenderer,ViewController *vc):
        pipesRenderer(pRenderer),
        _listener(NULL),
        _lastCamera(NULL),
        vC(vc),
        _holeMode(false){}
    
    ~PipesRenderer(){
        vC = NULL;
    }
    
    virtual void initialize(const G3MContext* context) {
        DefaultRenderer::initialize(context);
        pipesRenderer->initialize(context);
    }
    
    virtual void render(const G3MRenderContext* rc,
                        GLState* glState);
    
    void onResizeViewportEvent(const G3MEventContext* ec,
                               int width, int height){}
    
    bool onTouchEvent(const G3MEventContext* ec,
                      const TouchEvent* touchEvent);
    
    void setTouchListener(PipeTouchedListener* touchListener){
        _listener = touchListener;
    }

    void setHoleMode(bool mode){
        _holeMode = mode;
    }
};

#endif /* PipesRenderer_hpp */
