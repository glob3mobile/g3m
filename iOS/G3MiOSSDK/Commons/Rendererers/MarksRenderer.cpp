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

MarksRenderer::MarksRenderer(bool readyWhenMarksReady) :
_readyWhenMarksReady(readyWhenMarksReady),
_lastCamera(NULL),
_markTouchListener(NULL),
_autoDeleteMarkTouchListener(false),
_downloadPriority(DownloadPriority::MEDIUM),
_glState(new GLState()),
_billboardTexCoords(NULL)
{
  _context = NULL;
}


MarksRenderer::~MarksRenderer() {
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


void MarksRenderer::onChangedContext() {
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    mark->initialize(_context, _downloadPriority);
  }
}

void MarksRenderer::addMark(Mark* mark) {
  _marks.push_back(mark);
  if (_context != NULL) {
    mark->initialize(_context, _downloadPriority);
  }
}

void MarksRenderer::removeMark(Mark* mark) {
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

void MarksRenderer::removeAllMarks() {
  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  _marks.clear();
}

bool MarksRenderer::onTouchEvent(const G3MEventContext* ec,
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

RenderState MarksRenderer::getRenderState(const G3MRenderContext* rc) {
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
  const int marksSize = _marks.size();
  if (marksSize > 0) {
    const Camera* camera = rc->getCurrentCamera();

    _lastCamera = camera; // Saving camera for use in onTouchEvent

    const Vector3D cameraPosition = camera->getCartesianPosition();
    const int      cameraHeight   = camera->getHeight();

    updateGLState(rc);

    const Planet* planet = rc->getPlanet();
    GL* gl = rc->getGL();

    IFloatBuffer* billboardTexCoord = getBillboardTexCoords();

    for (int i = 0; i < marksSize; i++) {
      Mark* mark = _marks[i];
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
    _glState->addGLFeature(new ViewportExtentGLFeature(cam->getWidth(), cam->getHeight()), false);
  }
}

void MarksRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width, int height) {
  _glState->clearGLFeatureGroup(NO_GROUP);
  _glState->addGLFeature(new ViewportExtentGLFeature(width, height), false);
}
