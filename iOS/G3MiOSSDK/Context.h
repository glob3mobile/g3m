//
//  InitializationContext.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_InitializationContext_h
#define G3MiOSSDK_InitializationContext_h

class Factory;
class IGL;

class Context
{
public:
    
    Context(Factory *f, IGL *gl);
    
    Factory * _factory;
    IGL * _gl;
};

class InitializationContext: private Context
{
    InitializationContext(Factory * f);
    
    Factory *getFactory(){ return _factory;}
};

class RenderContext: private Context
{
    RenderContext(Factory * f, IGL gl);
    
    Factory *getFactory(){ return _factory;}
    IGL *getGL(){ return _gl;}
};

#endif
