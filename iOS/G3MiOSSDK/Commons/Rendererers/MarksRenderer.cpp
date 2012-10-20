//
//  MarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "MarksRenderer.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"

const double MarksRenderer::SQUARED_DISTANCE_THRESHOLD = 50*50;

void MarksRenderer::initialize(const InitializationContext* ic) {
  _initializationContext = ic;
  
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    mark->initialize(_initializationContext);
  }
}

void MarksRenderer::addMark(Mark* mark) {
  _marks.push_back(mark);
  if (_initializationContext != NULL) {
    mark->initialize(_initializationContext);
  }
}

bool MarksRenderer::onTouchEvent(const EventContext* ec,
                                 const TouchEvent* touchEvent) {
  if (_markTouchListener == NULL) {
    return false;
  }
  
  bool handled = false;
  
  // if (touchEvent->getType() == LongPress) {
  if (touchEvent->getType() == Down) {
    
    if (_lastCamera != NULL) {
      const Vector2I touchedPixel = touchEvent->getTouch(0)->getPos();
//      const Vector3D ray = _lastCamera->pixel2Ray(touchedPixel);
//      const Vector3D origin = _lastCamera->getCartesianPosition();
      
      const Planet* planet = ec->getPlanet();
      
//      const Vector3D positionCartesian = planet->closestIntersection(origin, ray);
//      if (positionCartesian.isNan()) {
//        return false;
//      }
      
      // const Geodetic3D position = planet->toGeodetic3D(positionCartesian);
      
      double minDistance = IMathUtils::instance()->maxDouble();
      Mark* nearestMark = NULL;
      
      int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++) {
        Mark* mark = _marks[i];
        
        if (mark->isReady()) {
          if (mark->isRendered()) {
            const Vector3D cartesianMarkPosition = planet->toCartesian( mark->getPosition() );
            const Vector2I markPixel = _lastCamera->point2Pixel(cartesianMarkPosition);
            
            const double distance = markPixel.sub(touchedPixel).squaredLength();
            if (distance < minDistance) {
              nearestMark = mark;
              minDistance = distance;
            }
          }
        }
      }
      
      if (nearestMark != NULL) {
        if (minDistance <= SQUARED_DISTANCE_THRESHOLD) {
          handled = _markTouchListener->touchedMark(nearestMark);
        }
      }
      
    }
    
  }
  
  return handled;
  
  
  
  
}

bool MarksRenderer::isReadyToRender(const RenderContext* rc) {
  if (_readyWhenMarksReady) {
    int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++) {
      if (!_marks[i]->isReady()) {
        return false;
      }
    }
  }
  
  return true;
}

void MarksRenderer::render(const RenderContext* rc) {
  //  rc.getLogger()->logInfo("MarksRenderer::render()");
  
  // Saving camera for use in onTouchEvent
  _lastCamera = rc->getCurrentCamera();
  
  
  GL* gl = rc->getGL();
  
  gl->enableVerticesPosition();
  gl->enableTextures();
  
  gl->disableDepthTest();
  gl->enableBlend();
  
  const Vector3D radius = rc->getPlanet()->getRadii();
  const double minDistanceToCamera = (radius._x + radius._y + radius._z) / 3 * 0.75;
  
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());
    
    if (mark->isReady()) {
      mark->render(rc, minDistanceToCamera);
    }
  }
  
  gl->enableDepthTest();
  gl->disableBlend();
  
  gl->disableTextures();
  gl->disableVerticesPosition();
  gl->disableTexture2D();
}
