//
//  G3MWidget.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "G3MWidget.hpp"


G3MWidget* G3MWidget::create(IFactory* factory,
                             ILogger *logger,
                             IGL* gl,
                             const Planet* planet,
                             Renderer* renderer, int width, int height)
{
  if (logger != NULL) {
    logger->logInfo("Creating G3MWidget...");
  }
  
  return new G3MWidget(factory, logger, gl, planet, renderer, width, height);
}

G3MWidget::~G3MWidget()
{
  delete _renderer;
  delete _planet;
  
  delete _factory;
  delete _gl;
}

bool G3MWidget::render()
{
  RenderContext rc(_factory, _logger, _planet, _gl, _camera);
  
  // Clear the scene
  _gl->clearScreen(0, 0.1, 0.2);
  _gl->enableVertices();
  _gl->enableTextures();

  
  int timeToRedraw = _renderer->render(&rc);
  
  return true;
}

void G3MWidget::onTouchEvent(const TouchEvent* event)
{
  _renderer->onTouchEvent(event);
}

void G3MWidget::onResizeViewportEvent(int width, int height)
{
  _renderer->onResizeViewportEvent(width, height);
}
