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
#include "IGL.h"

class Context {
protected:
  IFactory * _factory;
  
  Context(IFactory *factory) : _factory(factory) {}
  
public:
  
  IFactory *getFactory() {
    return _factory;
  }
  
};


class InitializationContext: private Context {
public:
  InitializationContext(IFactory * factory) : Context(factory) {}
};


class RenderContext: private Context {
private:
  IGL * _gl;
  
public:
  RenderContext(IFactory * factory,
                IGL *gl): Context(factory), _gl(gl) {
    
  }
  
  IGL *getGL(){
    return _gl;
  }
};

#endif
