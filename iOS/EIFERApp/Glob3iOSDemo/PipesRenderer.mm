//
//  PipesRenderer.cpp
//  EIFER App
//
//  Created by Chano on 23/11/17.
//
//

#include "PipesRenderer.hpp"

#include <G3MiOSSDK/GLState.hpp>
#include <G3MiOSSDK/G3MRenderContext.hpp>
#include <G3MiOSSDK/TouchEvent.hpp>
#include <G3MiOSSDK/Camera.hpp>
#include <G3MiOSSDK/Sphere.hpp>

#include "PipesModel.hpp"

#include <vector>


void PipesRenderer::render(const G3MRenderContext *rc, GLState *glState){
    //Do stuff here
    _lastCamera = rc->getCurrentCamera();
    pipesRenderer->render(rc, glState);
}

bool PipesRenderer::onTouchEvent(const G3MEventContext *ec, const TouchEvent *touchEvent){
    //Do stuff here
    if ([vC isHole] && [vC getMapMode] == 3){
        Vector3D cameraDir = _lastCamera->getViewDirection();
        Camera c(_lastCamera->getTimestamp());
        c.copyFrom(*_lastCamera,true);
        c.translateCamera(cameraDir.normalized().times(25));
        Geodetic3D cameraPos = c.getGeodeticPosition();
        [vC changeHole:cameraPos];
    }
    
    
    if (_lastCamera == NULL || _listener == NULL)
    {
        return false;
    }
    
    if (touchEvent->getType() == TouchEventType::LongPress)
    {
        const Vector2F pixel = touchEvent->getTouch(0)->getPos();
        const Vector3D ray = _lastCamera->pixel2Ray(pixel);
        const Vector3D origin = _lastCamera->getCartesianPosition();
        double minDis = 1e20;
        Cylinder *touchedB = NULL;
        Cylinder::CylinderMeshInfo touchedInfoB;
        for (size_t i=0; i< PipesModel::cylinders.size(); i++){
            const Sphere *s = PipesModel::cylinders.at(i)->s;
            if (s != NULL){
                const std::vector<double> dists = s->intersectionsDistances(origin._x, origin._y, origin._z, ray._x, ray._y, ray._z);
                for (size_t j = 0; j < dists.size(); j++)
                {
                    if (dists.at(j) < minDis)
                    {
                        minDis = dists.at(j);
                        touchedB = PipesModel::cylinders.at(i);
                        touchedInfoB = PipesModel::cylinderInfo.at(i);
                    }
                }
            }
        }
        if (touchedB != NULL)
        {
            _listener->onPipeTouched(touchedB,touchedInfoB);
            return !_holeMode;
        }
        
    }
    return false;
}
