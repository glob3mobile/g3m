//
//  HUDRenderer.cpp
//  G3MiOSSDK
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
#include "Context.hpp"

HUDRenderer::HUDRenderer(bool readyWhenWidgetsReady) :
_glState(new GLState()),
_readyWhenWidgetsReady(readyWhenWidgetsReady)
{
  _context = NULL;
  _widgetsSize = _widgets.size();
}

HUDRenderer::~HUDRenderer() {
  for (int i = 0; i < _widgetsSize; i++) {
    HUDWidget* widget = _widgets[i];
    delete widget;
  }

  _glState->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void HUDRenderer::onChangedContext() {
  for (int i = 0; i < _widgetsSize; i++) {
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

RenderState HUDRenderer::getRenderState(const G3MRenderContext* rc) {
  if (_widgetsSize == 0) {
    return RenderState::ready();
  }

  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;

  for (int i = 0; i < _widgetsSize; i++) {
    HUDWidget* widget = _widgets[i];
    if (widget->isEnable()) {
      const RenderState childRenderState = widget->getRenderState(rc);

      const RenderState_Type childRenderStateType = childRenderState._type;

      if (childRenderStateType == RENDER_ERROR) {
        errorFlag = true;

        const std::vector<std::string> childErrors = childRenderState.getErrors();
#ifdef C_CODE
        _errors.insert(_errors.end(),
                       childErrors.begin(),
                       childErrors.end());
#endif
#ifdef JAVA_CODE
        _errors.addAll(childErrors);
#endif
      }
      else if (childRenderStateType == RENDER_BUSY) {
        busyFlag = true;
      }
    }
  }

  if (errorFlag) {
    return RenderState::error(_errors);
  }
  else if (busyFlag && _readyWhenWidgetsReady) {
    return RenderState::busy();
  }
  else {
    return RenderState::ready();
  }
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
//  double znear, double zfar
//  MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(0, width,
//                                                                                           0, height,
//                                                                                           -halfWidth, halfWidth);
  MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(0, width,
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

  for (int i = 0; i < _widgetsSize; i++) {
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

  for (int i = 0; i < _widgetsSize; i++) {
    HUDWidget* widget = _widgets[i];
    widget->render(rc, _glState);
  }

  nativeGL->depthMask(true);
}
