//
//  InitializationContext.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "Context.h"

Context::Context(Factory *f, IGL *gl):
_factory(f), _gl(gl){}

InitializationContext::InitializationContext(Factory * f):
Context(f,NULL)
{
    
};

RenderContext::RenderContext(Factory * f, IGL *gl): Context(f,gl){}