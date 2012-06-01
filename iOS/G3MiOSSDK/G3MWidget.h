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

class G3MWidget
{
public:
    
    ~G3MWidget();
    
    void create(Planet *g, Renderer *r);
    
    bool render();
    
    bool onTapEvent(TapEvent& event);
    bool onTouchEvent(TouchEvent &event);
    
private:
    Renderer *_renderer;
    Planet * _planet;
    IFactory * _factory;
    IGL * _gl;
};

#endif
