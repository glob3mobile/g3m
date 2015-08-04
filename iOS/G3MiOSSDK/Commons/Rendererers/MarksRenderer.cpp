//
//  MarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "MarksRenderer.hpp"
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

void MarksRenderer::setMarkTouchListener(MarkTouchListener* markTouchListener,
                                         bool autoDelete) {
  if ( _autoDeleteMarkTouchListener ) {
    delete _markTouchListener;
  }

  _markTouchListener = markTouchListener;
  _autoDeleteMarkTouchListener = autoDelete;
}

MarksRenderer::MarksRenderer(bool readyWhenMarksReady,
                             bool renderInReverse,
                             bool progressiveInitialization) :
_readyWhenMarksReady(readyWhenMarksReady),
_renderInReverse(renderInReverse),
_progressiveInitialization(progressiveInitialization),
_lastCamera(NULL),
_markTouchListener(NULL),
_autoDeleteMarkTouchListener(false),
_downloadPriority(DownloadPriority::MEDIUM),
_glState(new GLState()),
_billboardTexCoords(NULL),
_initializationTimer(NULL)
{
  _context = NULL;
}


MarksRenderer::~MarksRenderer() {
  delete _initializationTimer;

  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
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


void MarksRenderer::onChangedContext() {
  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    mark->initialize(_context, _downloadPriority);
  }
}

void MarksRenderer::addMark(Mark* mark) {
  _marks.push_back(mark);
  if ((_context != NULL) && !_progressiveInitialization) {
    mark->initialize(_context, _downloadPriority);
  }
}

void MarksRenderer::removeMark(Mark* mark) {
//  int pos = -1;
//  const int marksSize = _marks.size();
//  for (int i = 0; i < marksSize; i++) {
//    if (_marks[i] == mark) {
//      pos = i;
//      break;
//    }
//  }
//  if (pos != -1) {
//#ifdef C_CODE
//    _marks.erase(_marks.begin() + pos);
//#endif
//#ifdef JAVA_CODE
//    _marks.remove(pos);
//#endif
//  }

  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    if (_marks[i] == mark) {
#ifdef C_CODE
      _marks.erase(_marks.begin() + i);
#endif
#ifdef JAVA_CODE
      _marks.remove(i);
#endif
      break;
    }
  }

}

void MarksRenderer::removeAllMarks() {
  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  _marks.clear();
}

bool MarksRenderer::onTouchEvent(const G3MEventContext* ec,
                                 const TouchEvent* touchEvent) {

  bool handled = false;
  if ( touchEvent->getType() == DownUp ) {

    if (_lastCamera != NULL) {
      const Vector2F touchedPixel = touchEvent->getTouch(0)->getPos();
      const Planet* planet = ec->getPlanet();

      double minSqDistance = IMathUtils::instance()->maxDouble();
      Mark* nearestMark = NULL;

      const size_t marksSize = _marks.size();
      for (size_t i = 0; i < marksSize; i++) {
        Mark* mark = _marks[i];

        if (!mark->isReady()) {
          continue;
        }
        if (!mark->isRendered()) {
          continue;
        }

        const int markWidth = (int)mark->getTextureWidth();
        if (markWidth <= 0) {
          continue;
        }

        const int markHeight = (int)mark->getTextureHeight();
        if (markHeight <= 0) {
          continue;
        }

        const Vector3D* cartesianMarkPosition = mark->getCartesianPosition(planet);
        const Vector2F markPixel = _lastCamera->point2Pixel(*cartesianMarkPosition);

        const RectangleF markPixelBounds(markPixel._x - ((float) markWidth / 2),
                                         markPixel._y - ((float) markHeight / 2),
                                         markWidth,
                                         markHeight);

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

RenderState MarksRenderer::getRenderState(const G3MRenderContext* rc) {
  if (_readyWhenMarksReady) {
    const size_t marksSize = _marks.size();
    for (size_t i = 0; i < marksSize; i++) {
      if (!_marks[i]->isReady()) {
        return RenderState::busy();
      }
    }
  }

  return RenderState::ready();
}

IFloatBuffer* MarksRenderer::getBillboardTexCoords() {
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

void MarksRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  const size_t marksSize = _marks.size();

  if (marksSize > 0) {
    const Camera* camera = rc->getCurrentCamera();

    _lastCamera = camera; // Saving camera for use in onTouchEvent

    MutableVector3D cameraPosition;
    camera->getCartesianPositionMutable(cameraPosition);
    const double cameraHeight = camera->getGeodeticPosition()._height;

    updateGLState(rc);

    const Planet* planet = rc->getPlanet();
    GL* gl = rc->getGL();

    IFloatBuffer* billboardTexCoord = getBillboardTexCoords();


    if (_progressiveInitialization) {
      if (_initializationTimer == NULL) {
        _initializationTimer = rc->getFactory()->createTimer();
      }
      else {
        _initializationTimer->start();
      }

      for (size_t i = 0; i < marksSize; i++) {
        if (_initializationTimer->elapsedTimeInMilliseconds() > 5) {
          break;
        }

        const size_t ii = _renderInReverse ? i : (marksSize-1-i);
        Mark* mark = _marks[ii];
        if (!mark->isInitialized()) {
          mark->initialize(_context, _downloadPriority);
        }
      }
    }

    for (size_t i = 0; i < marksSize; i++) {
      const size_t ii = _renderInReverse ? (marksSize-1-i) : i;
      Mark* mark = _marks[ii];
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

void MarksRenderer::updateGLState(const G3MRenderContext* rc) {
  const Camera* camera = rc->getCurrentCamera();

  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }

  if (_glState->getGLFeature(GLF_VIEWPORT_EXTENT) == NULL) {
    _glState->clearGLFeatureGroup(NO_GROUP);
    _glState->addGLFeature(new ViewportExtentGLFeature(camera),
                           false);
  }
}

void MarksRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width, int height) {
  _glState->clearGLFeatureGroup(NO_GROUP);
  _glState->addGLFeature(new ViewportExtentGLFeature(width, height), false);
}

size_t MarksRenderer::removeAllMarks(const MarksFilter& filter,
                                     bool deleteMarks) {
  size_t removed = 0;
  std::vector<Mark*> newMarks;

  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    if (filter.test(mark)) {
      if (deleteMarks) {
        delete mark;
      }
      removed++;
    }
    else {
      newMarks.push_back(mark);
    }
  }
  
  if (removed > 0) {
    _marks = newMarks;
  }
  
  return removed;
}

