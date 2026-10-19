//
//  HUDRenderer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#include "HUDRenderer.hpp"

#include "RenderState.hpp"
#include "HUDWidget.hpp"
#include "MutableMatrix44D.hpp"
#include "GLFeature.hpp"
#include "GLState.hpp"
#include "GL.hpp"
#include "G3MRenderContext.hpp"
#include "G3MEventContext.hpp"


HUDRenderer::HUDRenderer(bool readyWhenWidgetsReady) :
_glState(new GLState()),
_renderStateCollector(),
_readyWhenWidgetsReady(readyWhenWidgetsReady)
{
  _context = NULL;
  _widgetsSize = _widgets.size();
}

HUDRenderer::~HUDRenderer() {
  for (size_t i = 0; i < _widgetsSize; i++) {
    HUDWidget* widget = _widgets[i];
    delete widget;
  }

  _glState->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void HUDRenderer::onChangedContext() {
  for (size_t i = 0; i < _widgetsSize; i++) {
    HUDWidget* widget = _widgets[i];
    widget->initialize(_context);
  }
}

void HUDRenderer::addWidget(HUDWidget* widget) {
  _widgets.push_back(widget);
  _widgetsSize = _widgets.size();

  if (_context != NULL) {
    widget->initialize(_context);
  }
}

void HUDRenderer::removeAllWidgets() {
  for (size_t i = 0; i < _widgetsSize; i++) {
    HUDWidget* widget = _widgets[i];
    delete widget;
  }
  
  _widgets.clear();
  _widgetsSize = _widgets.size();

}

RenderState HUDRenderer::getRenderState(const G3MRenderContext* rc) {
  if (_widgetsSize == 0) {
    return RenderState::ready();
  }

  _renderStateCollector.reset();

  for (size_t i = 0; i < _widgetsSize; i++) {
    HUDWidget* widget = _widgets[i];
    if (widget->isEnable()) {
      _renderStateCollector.add(widget->getRenderState(rc));
    }
  }

  const RenderState renderState = _renderStateCollector.getState();
  if ((renderState._type == RENDER_BUSY) && !_readyWhenWidgetsReady) {
    return RenderState::ready();
  }
  return renderState;
}


void HUDRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                        int width,
                                        int height) {
//  const int halfWidth  = width  / 2;
//  const int halfHeight = height / 2;
//  MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth,  halfWidth,
//                                                                                           -halfHeight, halfHeight,
//                                                                                           -halfWidth,  halfWidth);
//  double left, double right,
//  double bottom, double top,
//  double zNear, double zFar
//  MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(0, width,
//                                                                                           0, height,
//                                                                                           -halfWidth, halfWidth);

  int logicWidth = width;
  if (ec->getViewMode() == STEREO) {
    logicWidth /= 2;
  }

  MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(0, logicWidth,
                                                                                           0, height,
                                                                                           -1, +1);

  ProjectionGLFeature* pr = (ProjectionGLFeature*) _glState->getGLFeature(GLF_PROJECTION);
  if (pr == NULL) {
    _glState->addGLFeature(new ProjectionGLFeature(projectionMatrix.asMatrix44D()),
                           false);
  }
  else {
    pr->setMatrix(projectionMatrix.asMatrix44D());
  }

  for (size_t i = 0; i < _widgetsSize; i++) {
    HUDWidget* widget = _widgets[i];
    widget->onResizeViewportEvent(ec, width, height);
  }
}

void HUDRenderer::render(const G3MRenderContext* rc,
                         GLState* glState) {
  if (_widgetsSize == 0) {
    return;
  }

  INativeGL* nativeGL = rc->getGL()->getNative();

  nativeGL->depthMask(false);

  for (size_t i = 0; i < _widgetsSize; i++) {
    HUDWidget* widget = _widgets[i];
    widget->render(rc, _glState);
  }

  nativeGL->depthMask(true);
}
