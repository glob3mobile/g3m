//
//  G3Widget.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "G3Widget.h"

G3Widget::~G3Widget()
{
    delete _factory;
    delete _gl;
}

void G3Widget::create(Globe *g, Renderer *r)
{
    //Factory = new Fac....
    
    _globe = g;
    _renderer = r;
    
    InitializationContext ic(_factory);
    _renderer->initialize(ic);
}

bool G3Widget::render()
{
    RenderContext rc(_factory, _gl);
    
    _renderer->render(rc);
    
    return true;
}