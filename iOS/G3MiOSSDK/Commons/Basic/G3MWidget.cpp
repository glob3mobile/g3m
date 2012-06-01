//
//  G3MWidget.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "G3MWidget.h"


G3MWidget* G3MWidget::create(IFactory* factory,
                             ILogger *logger,
                             IGL* gl,
                             const Planet* planet,
                             Renderer* renderer)
{
  logger->logInfo("Creating G3MWidget...");
  return new G3MWidget(factory,
                       logger,
                       gl,
                       planet,
                       renderer);
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
  RenderContext rc(_factory, _logger, _planet, _gl);
  
  _renderer->render(&rc);
  
  return true;
}

void G3MWidget::onTouchEvent(const TouchEvent &event)
{
  _renderer->onTouchEvent(event);
}
