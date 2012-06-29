//
//  InitializationContext.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_InitializationContext_h
#define G3MiOSSDK_InitializationContext_h

#include "IFactory.hpp"
#include "IGL.hpp"
#include "Planet.hpp"
#include "TexturesHandler.hpp"
#include "SQLiteStorage_iOS.hpp"

#define MAX_TIME_TO_RENDER 1000



class Camera;

class Context {
protected:
  const IFactory * _factory;
  const ILogger*   _logger;
  const Planet*    _planet;
  const IStorage* _storage;


  
  Context(const IFactory *factory,
          const ILogger* logger,
          const Planet* planet) :
  _factory(factory),
  _logger(logger),
  _planet(planet),_storage(new SQLiteStorage_iOS()){
    
      
      _storage->findFileFromFileName("ficheroprueba.pdf");
      
      
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
    
    const IStorage* getStorage() const {
        return _storage;
    }
    
    /*
    void setStorage(IStorage* storage){
        _storage(storage);
    }
     */
  
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
  IGL*             _gl;
  Camera*          _camera;
  TexturesHandler* _texturesHandler;
  
public:
  RenderContext(IFactory *factory,
                ILogger* logger,
                const Planet* planet,
                IGL *gl,
                Camera* camera,
                TexturesHandler* texturesHandler) :
  Context(factory, logger, planet),
  _gl(gl),
  _camera(camera),
  _texturesHandler(texturesHandler) {
    
  }
  
  IGL* getGL() const {
    return _gl;
  }
  
  Camera* getCamera() const {
    return _camera;
  }
  
  TexturesHandler* getTexturesHandler() const {
    return _texturesHandler;
  }
    
    
};

#endif
