//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CameraRenderer.hpp"
#include "Camera.hpp"
#include "CameraEventHandler.hpp"
#include "TouchEvent.hpp"

#include "G3MWidget.hpp"
#include "MarksRenderer.hpp"
#include "Mark.hpp"


CameraRenderer::~CameraRenderer() {
  delete _cameraContext;
  const int handlersSize = _handlers.size();
  for (int i = 0; i < handlersSize; i++) {
    CameraEventHandler* handler = _handlers[i];
    delete handler;
  }
}

void CameraRenderer::render(const G3MRenderContext* rc, GLState* glState) {

  // create the CameraContext
  if (_cameraContext == NULL) {
    _cameraContext = new CameraContext(None, rc->getNextCamera());
  }

  // render camera object
//  rc->getCurrentCamera()->render(rc, parentState);

  const int handlersSize = _handlers.size();
  for (unsigned int i = 0; i < handlersSize; i++) {
    _handlers[i]->render(rc, _cameraContext);
  }
}

bool CameraRenderer::onTouchEvent(const G3MEventContext* ec,
                                  const TouchEvent* touchEvent) {
  if (_processTouchEvents) {
    // abort all the camera effect currently running
    if (touchEvent->getType() == Down) {
      EffectTarget* target = _cameraContext->getNextCamera()->getEffectTarget();
      ec->getEffectsScheduler()->cancelAllEffectsFor(target);
    }

    MarksRenderer* marksRenderer = ec->getWidget()->getMarksRenderer();
    std::vector<Mark*> marks = marksRenderer->getMarks();
    for (int i=0; i<marks.size(); i++) {
      Vector3D* posMark = marks[i]->getCartesianPosition(ec->getPlanet());
      Vector2F pixel = _cameraContext->getNextCamera()->point2Pixel(*posMark);
      Vector3D posZRender = ec->getWidget()->getScenePositionForPixel((int)(pixel._x+0.5),(int)(pixel._y+0.5));
      double distance = posMark->distanceTo(posZRender);
      if (distance<100)
        marks[i]->setVisible(true);
      else
        marks[i]->setVisible(false);
      printf("marca %d: \n",i);
      printf("   posMark en %f %f %f\n", posMark->_x, posMark->_y, posMark->_z);
      printf("   posZrender en %f %f %f\n", posZRender._x, posZRender._y, posZRender._z);
      printf("   distancia = %f\n", distance);
    }
    
    // this call is needed at this point. I don't know why
    ec->getWidget()->getScenePositionForCentralPixel();
    
    // pass the event to all the handlers
    const int handlersSize = _handlers.size();
    for (unsigned int i = 0; i < handlersSize; i++) {
      if (_handlers[i]->onTouchEvent(ec, touchEvent, _cameraContext)) {
        return true;
      }
    }


  }

  // if no handler processed the event, return not-handled
  return false;
}


void CameraRenderer::setDebugMeshRenderer(MeshRenderer* meshRenderer) {
  _meshRenderer = meshRenderer;
  for (int n=0; n<_handlers.size(); n++)
    _handlers[n]->setDebugMeshRenderer(meshRenderer);
}

