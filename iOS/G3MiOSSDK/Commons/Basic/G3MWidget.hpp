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
#include "Camera.hpp"
#include "Color.hpp"

class G3MWidget {
public:
  
  static G3MWidget* create(IFactory* factory,
                           ILogger *logger,
                           IGL* gl,
                           const Planet* planet,
                           Renderer* renderer,
                           int width, int height,
                           Color backgroundColor);
  
  ~G3MWidget();
  
  bool render();
  
  void onTouchEvent(const TouchEvent* event);
  
  void onResizeViewportEvent(int width, int height);
  
  IGL * getGL() const{ return _gl; }
  
  
private:
  IFactory*     _factory;
  ILogger*      _logger;
  IGL*          _gl;
  const Planet* _planet;
  Renderer*     _renderer;
  Camera*       _camera;
  const Color   _backgroundColor;
  
  G3MWidget(IFactory* factory,
            ILogger *logger,
            IGL* gl,
            const Planet* planet,
            Renderer* renderer,
            int width, int height,
            Color backgroundColor):
  _factory(factory),
  _logger(logger),
  _gl(gl),
  _planet(planet),
  _renderer(renderer),
  _camera(new Camera(width, height)),
  _backgroundColor(backgroundColor)
  {
    InitializationContext ic(_factory, _logger, _planet);
    _renderer->initialize(&ic);
  }
};

#endif
