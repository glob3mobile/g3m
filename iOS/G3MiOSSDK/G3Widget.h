//
//  G3Widget.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_G3Widget_h
#define G3MiOSSDK_G3Widget_h

#include "Renderer.h"
#include "Planet.hpp"
#include "IFactory.h"

class Globe{};

class G3Widget
{
public:
    
    ~G3Widget();
    
    void create(Planet *g, Renderer *r);
    
    bool render();
    
private:
    Renderer *_renderer;
    Planet * _planet;
    IFactory * _factory;
    IGL * _gl;
};

#endif
