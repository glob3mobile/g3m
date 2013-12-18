//
//  HUDRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#include "HUDRenderer.hpp"

#include "HUDWidget.hpp"

#include "MutableMatrix44D.hpp"
#include "GLFeature.hpp"
#include "GLState.hpp"
#include "GL.hpp"
#include "Context.hpp"

HUDRenderer::HUDRenderer(bool readyWhenWidgetsReady) :
_glState(new GLState()),
_readyWhenWidgetsReady(readyWhenWidgetsReady),
_context(NULL)
{
}

HUDRenderer::~HUDRenderer() {
  const int size = _widgets.size();
  for (int i = 0; i < size; i++) {
    HUDWidget* widget = _widgets[i];
    delete widget;
  }

  _glState->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void HUDRenderer::initialize(const G3MContext* context) {
  _context = context;

  const int size = _widgets.size();
  for (int i = 0; i < size; i++) {
    HUDWidget* widget = _widgets[i];
    widget->initialize(context);
  }
}

void HUDRenderer::addWidget(HUDWidget* widget) {
  _widgets.push_back(widget);

  if (_context != NULL) {
    widget->initialize(_context);
  }
}

RenderState HUDRenderer::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;

  const int size = _widgets.size();
  for (int i = 0; i < size; i++) {
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

bool HUDRenderer::onTouchEvent(const G3MEventContext* ec,
                               const TouchEvent* touchEvent) {
  return false;
}


void HUDRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                        int width,
                                        int height) {
  const int halfWidth  = width  / 2;
  const int halfHeight = height / 2;
  MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth,  halfWidth,
                                                                                           -halfHeight, halfHeight,
                                                                                           -halfWidth,  halfWidth);

  ProjectionGLFeature* pr = (ProjectionGLFeature*) _glState->getGLFeature(GLF_PROJECTION);
  if (pr == NULL) {
    _glState->addGLFeature(new ProjectionGLFeature(projectionMatrix.asMatrix44D()),
                           false);
  }
  else {
    pr->setMatrix(projectionMatrix.asMatrix44D());
  }

  const int size = _widgets.size();
  for (int i = 0; i < size; i++) {
    HUDWidget* widget = _widgets[i];
    widget->onResizeViewportEvent(ec, width, height);
  }
}

void HUDRenderer::render(const G3MRenderContext* rc,
                         GLState* glState) {

  rc->getGL()->getNative()->depthMask(false);

  const int size = _widgets.size();
  for (int i = 0; i < size; i++) {
    HUDWidget* widget = _widgets[i];
    if (widget->isEnable()) {
      widget->render(rc, _glState);
    }
  }

  rc->getGL()->getNative()->depthMask(true);
}
