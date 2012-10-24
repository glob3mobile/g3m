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
#include "RectangleI.hpp"


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
  
  if ( (touchEvent->getType() == Down) && (touchEvent->getTouchCount() == 1) ) {
    
    if (_lastCamera != NULL) {
      const Vector2I touchedPixel = touchEvent->getTouch(0)->getPos();
      const Planet* planet = ec->getPlanet();
      
      double minSqDistance = IMathUtils::instance()->maxDouble();
      Mark* nearestMark = NULL;
      
      int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++) {
        Mark* mark = _marks[i];
        
        if (!mark->isReady()) {
          continue;
        }
        if (!mark->isRendered()) {
          continue;
        }
        
        const int textureWidth = mark->getTextureWidth();
        if (textureWidth <= 0) {
          continue;
        }
        
        const int textureHeight = mark->getTextureHeight();
        if (textureHeight <= 0) {
          continue;
        }
        
        const Vector3D cartesianMarkPosition = planet->toCartesian( mark->getPosition() );
        const Vector2I markPixel = _lastCamera->point2Pixel(cartesianMarkPosition);
        
        const RectangleI markPixelBounds(markPixel._x - (textureWidth / 2),
                                         markPixel._y - (textureHeight / 2),
                                         textureWidth,
                                         textureHeight);
        
        if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y)) {
          const double distance = markPixel.sub(touchedPixel).squaredLength();
          if (distance < minSqDistance) {
            nearestMark = mark;
            minSqDistance = distance;
          }
        }
      }
      
      if (nearestMark != NULL) {
        handled = _markTouchListener->touchedMark(nearestMark);
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
