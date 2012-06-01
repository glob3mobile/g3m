//
//  G3MWidget.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_G3MWidget_h
#define G3MiOSSDK_G3MWidget_h

#include "Renderer.hpp"
#include "Planet.hpp"
#include "IFactory.hpp"

class Globe{};

class G3MWidget {
private:
  G3MWidget(IFactory* factory,
            ILogger *logger,
            IGL* gl,
            const Planet* planet,
            Renderer* renderer):
  _factory(factory),
  _logger(logger),
  _gl(gl),
  _planet(planet),
  _renderer(renderer)
  {
    InitializationContext ic(_factory, _logger, _planet);
    _renderer->initialize(&ic);
  }
  
public:
  
  static G3MWidget* create(IFactory* factory,
                           ILogger *logger,
                           IGL* gl,
                           const Planet* planet,
                           Renderer* renderer);
  
  ~G3MWidget();
  
  bool render();
  
  void onTouchEvent(const TouchEvent &event);
  
private:
  IFactory*     _factory;
  ILogger*      _logger;
  IGL*          _gl;
  const Planet* _planet;
  Renderer*     _renderer;
};

#endif
