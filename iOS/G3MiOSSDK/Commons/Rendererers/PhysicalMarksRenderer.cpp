//
//  PhysicalMarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/1/15.
//
//

//
//  PhysicalMarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "PhysicalMarksRenderer.hpp"
#include "RenderState.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"
#include "RectangleF.hpp"
#include "Mark.hpp"
#include "MarkTouchListener.hpp"
#include "DownloadPriority.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"
#include "Vector2F.hpp"
#include "LineShape.hpp"

void PhysicalMarksRenderer::setMarkTouchListener(MarkTouchListener* markTouchListener,
                                         bool autoDelete) {
  if ( _autoDeleteMarkTouchListener ) {
    delete _markTouchListener;
  }
  
  _markTouchListener = markTouchListener;
  _autoDeleteMarkTouchListener = autoDelete;
}

PhysicalMarksRenderer::PhysicalMarksRenderer(bool readyWhenMarksReady,
                                             ShapesRenderer *shapesRenderer) :
_readyWhenMarksReady(readyWhenMarksReady),
_shapesRenderer(shapesRenderer),
_lastCamera(NULL),
_markTouchListener(NULL),
_autoDeleteMarkTouchListener(false),
_downloadPriority(DownloadPriority::MEDIUM),
_glState(new GLState()),
_billboardTexCoords(NULL)
{
  _context = NULL;
}


PhysicalMarksRenderer::~PhysicalMarksRenderer() {
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  
  if ( _autoDeleteMarkTouchListener ) {
    delete _markTouchListener;
  }
  _markTouchListener = NULL;
  
  _glState->_release();
  
  delete _billboardTexCoords;
  
#ifdef JAVA_CODE
  super.dispose();
#endif
  
};


void PhysicalMarksRenderer::onChangedContext() {
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    mark->initialize(_context, _downloadPriority);
  }
}

void PhysicalMarksRenderer::addMark(Mark* mark, Geodetic3D anchor) {
  _anchors.push_back(new Geodetic3D(anchor));
  _marks.push_back(mark);
  if (_context != NULL) {
    mark->initialize(_context, _downloadPriority);
  }
}

void PhysicalMarksRenderer::removeMark(Mark* mark) {
  int pos = -1;
  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    if (_marks[i] == mark) {
      pos = i;
      break;
    }
  }
  if (pos != -1) {
#ifdef C_CODE
    _marks.erase(_marks.begin() + pos);
#endif
#ifdef JAVA_CODE
    _marks.remove(pos);
#endif
  }
}

void PhysicalMarksRenderer::removeAllMarks() {
  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  _marks.clear();
}

bool PhysicalMarksRenderer::onTouchEvent(const G3MEventContext* ec,
                                 const TouchEvent* touchEvent) {
  
  bool handled = false;
  if ( touchEvent->getType() == DownUp ) {
    
    if (_lastCamera != NULL) {
      const Vector2I touchedPixel = touchEvent->getTouch(0)->getPos();
      const Planet* planet = ec->getPlanet();
      
      double minSqDistance = IMathUtils::instance()->maxDouble();
      Mark* nearestMark = NULL;
      
      const int marksSize = _marks.size();
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
        
        const Vector3D* cartesianMarkPosition = mark->getCartesianPosition(planet);
        const Vector2F markPixel = _lastCamera->point2Pixel(*cartesianMarkPosition);
        
        const RectangleF markPixelBounds(markPixel._x - (textureWidth / 2),
                                         markPixel._y - (textureHeight / 2),
                                         textureWidth,
                                         textureHeight);
        
        if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y)) {
          const double sqDistance = markPixel.squaredDistanceTo(touchedPixel);
          if (sqDistance < minSqDistance) {
            nearestMark = mark;
            minSqDistance = sqDistance;
          }
        }
      }
      
      if (nearestMark != NULL) {
        handled = nearestMark->touched();
        if (!handled) {
          if (_markTouchListener != NULL) {
            handled = _markTouchListener->touchedMark(nearestMark);
          }
        }
      }
    }
  }
  
  return handled;
}

RenderState PhysicalMarksRenderer::getRenderState(const G3MRenderContext* rc) {
  if (_readyWhenMarksReady) {
    int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++) {
      if (!_marks[i]->isReady()) {
        return RenderState::busy();
      }
    }
  }
  
  return RenderState::ready();
}

IFloatBuffer* PhysicalMarksRenderer::getBillboardTexCoords() {
  if (_billboardTexCoords == NULL) {
    FloatBufferBuilderFromCartesian2D texCoor;
    texCoor.add(1,1);
    texCoor.add(1,0);
    texCoor.add(0,1);
    texCoor.add(0,0);
    _billboardTexCoords = texCoor.create();
  }
  return _billboardTexCoords;
}

void PhysicalMarksRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  const int marksSize = _marks.size();
  
  
  
  
  if (marksSize > 0) {
    const Camera* camera = rc->getCurrentCamera();
    _lastCamera = camera; // Saving camera for use in onTouchEvent
    
    const MutableVector3D cameraPosition = camera->getCartesianPositionMutable();
    const double cameraHeight = camera->getGeodeticPosition()._height;
    
    updateGLState(rc);
    
    const Planet* planet = rc->getPlanet();
    GL* gl = rc->getGL();
    
    IFloatBuffer* billboardTexCoord = getBillboardTexCoords();
    
    Vector2I pixels[] = {Vector2I(400, 200), Vector2I(600,400)};
    _shapesRenderer->removeAllShapes();


    
    for (int i = 0; i < marksSize; i++) {
      Mark* mark = _marks[i];
      
      const Geodetic2D point = planet->toGeodetic2D(camera->pixel2PlanetPoint(pixels[i]));
      Shape* line = new LineShape(new Geodetic3D(_anchors[i]->_latitude, _anchors[i]->_longitude, cameraHeight/100),
                                  new Geodetic3D(point, cameraHeight/100),
                                  ABSOLUTE,
                                  5,
                                  Color::fromRGBA(0, 0, 0, 1));
      _shapesRenderer->addShape(line);
      mark->setPosition(Geodetic3D(point,0));
      

      if (mark->isReady()) {
        mark->render(rc,
                     cameraPosition,
                     cameraHeight,
                     _glState,
                     planet,
                     gl,
                     billboardTexCoord);
      }
    }
  }
}


void PhysicalMarksRenderer::updateGLState(const G3MRenderContext* rc) {
  const Camera* cam = rc->getCurrentCamera();
  
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(cam), true);
  }
  else {
    f->setMatrix(cam->getModelViewMatrix44D());
  }
  
  if (_glState->getGLFeature(GLF_VIEWPORT_EXTENT) == NULL) {
    _glState->clearGLFeatureGroup(NO_GROUP);
    _glState->addGLFeature(new ViewportExtentGLFeature(cam->getViewPortWidth(), cam->getViewPortHeight()), false);
  }
}

void PhysicalMarksRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width, int height) {
  _glState->clearGLFeatureGroup(NO_GROUP);
  _glState->addGLFeature(new ViewportExtentGLFeature(width, height), false);
}
