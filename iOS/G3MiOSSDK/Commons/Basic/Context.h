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
#include "Planet.hpp"

class Context {
protected:
  const IFactory * _factory;
  const ILogger*   _logger;
  const Planet*    _planet;
  
  Context(const IFactory *factory,
          const ILogger* logger,
          const Planet* planet) :
  _factory(factory),
  _logger(logger),
  _planet(planet) {
  }
  
public:
  
  const IFactory* getFactory() const {
    return _factory;
  }
  
  const ILogger* getLogger() const {
    return _logger;
  }
  
  const Planet* getPlanet() const {
    return _planet;
  }
  
};


class InitializationContext: public Context {
public:
  InitializationContext(IFactory *factory,
                        ILogger* logger,
                        const Planet* planet) :
  Context(factory, logger, planet) {
  }
};


class RenderContext: public Context {
private:
  IGL * _gl;
  
public:
  RenderContext(IFactory *factory,
                ILogger* logger,
                const Planet* planet,
                IGL *gl) :
  Context(factory, logger, planet),
  _gl(gl) {
    
  }
  
  IGL *getGL() const {
    return _gl;
  }
};

#endif
