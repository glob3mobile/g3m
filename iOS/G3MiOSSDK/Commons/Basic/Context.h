//
//  InitializationContext.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_InitializationContext_h
#define G3MiOSSDK_InitializationContext_h

#include "IFactory.h"
class IGL;

class Context
{
public:
    
    Context(IFactory *f, IGL *gl);
    
    IFactory * _factory;
    IGL * _gl;
};

class InitializationContext: private Context
{
public:
    InitializationContext(IFactory * f);
    
    IFactory *getFactory(){ return _factory;}
};

class RenderContext: private Context
{
public:
    RenderContext(IFactory * f, IGL *gl);
    
    IFactory *getFactory(){ return _factory;}
    IGL *getGL(){ return _gl;}
};

#endif
