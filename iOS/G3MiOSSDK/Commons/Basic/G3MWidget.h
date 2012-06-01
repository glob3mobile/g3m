//
//  G3MWidget.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_G3MWidget_h
#define G3MiOSSDK_G3MWidget_h

#include "Renderer.h"
#include "Planet.hpp"
#include "IFactory.h"

class Globe{};

class G3MWidget {
private:
  G3MWidget(const Planet& planet,
            Renderer& renderer,
            IFactory* factory,
            IGL* gl): _planet(planet), _renderer(renderer), _factory(factory), _gl(gl) {
    InitializationContext ic(_factory);
    _renderer.initialize(ic);
  }
  
public:
  
  static G3MWidget* create(const Planet& planet,
                           Renderer& renderer);
  
  ~G3MWidget();
  
  bool render();
  
  void onTouchEvent(TouchEvent &event);
  
private:
  Renderer& _renderer;
  const Planet&   _planet;
  IFactory* _factory;
  IGL*      _gl;
};

#endif
