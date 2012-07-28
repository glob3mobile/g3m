//
//  CameraDoubleDragRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraDoubleDragRenderer.h"

void CameraDoubleDragRenderer::onDown(const TouchEvent& touchEvent) {
  //Saving Camera0
  _camera0 = Camera(*_camera);
  
  // depending on the number of fingers, the gesture is different
  switch (touchEvent.getTouchCount()) {
      
    case 1: {
      // dragging
      MutableVector2D pixel = touchEvent.getTouch(0)->getPos().asMutableVector2D();
      const Vector3D ray = _camera0.pixel2Ray(pixel.asVector2D());
      _initialPoint = _planet->closestIntersection(_camera0.getPosition(), ray).asMutableVector3D();
      _currentGesture = Drag; //Dragging
      break;
    }
      
    case 2: {
      // double dragging
      Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
      Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
      
      // middle point in 3D
      Vector3D ray0 = _camera0.pixel2Ray(pixel0);
      Vector3D P0 = _planet->closestIntersection(_camera0.getPosition(), ray0);
      Vector3D ray1 = _camera0.pixel2Ray(pixel1);
      Vector3D P1 = _planet->closestIntersection(_camera0.getPosition(), ray1);
      Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
      _initialPoint = _planet->toVector3D(g).asMutableVector3D();
      
      // fingers difference
      Vector2D difPixel = pixel1.sub(pixel0);
      _initialFingerSeparation = difPixel.length();
      _initialFingerInclination = difPixel.orientation().radians();
      _currentGesture = DoubleDrag;
      break;
    }
      
    default:
      break;
  }
  
}


