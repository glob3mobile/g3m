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

HUDRenderer::HUDRenderer() :
_glState(new GLState())
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
//#warning todo ask widgets for ready
  return RenderState::ready();
}

bool HUDRenderer::onTouchEvent(const G3MEventContext* ec,
                               const TouchEvent* touchEvent) {
//#warning todo hud events
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
  const int size = _widgets.size();
  for (int i = 0; i < size; i++) {
    HUDWidget* widget = _widgets[i];
    widget->render(rc, glState);
  }
}
