//
//  G3MWidget.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "G3MWidget.h"

G3MWidget::~G3MWidget()
{
    delete _factory;
    delete _gl;
}

void G3MWidget::create(Planet *g, Renderer *r)
{
    //Factory = new Fac....
    
    _planet = g;
    _renderer = r;
    
    InitializationContext ic(_factory);
    _renderer->initialize(ic);
}

bool G3MWidget::render()
{
    RenderContext rc(_factory, _gl);
    
    _renderer->render(rc);
    
    return true;
}