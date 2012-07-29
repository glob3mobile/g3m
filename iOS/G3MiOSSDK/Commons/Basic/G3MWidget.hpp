//
//  G3MWidget.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_G3MWidget_h
#define G3MiOSSDK_G3MWidget_h

class Renderer;
class TouchEvent;
class Planet;
class ILogger;
class IFactory;
class IGL;
class TexturesHandler;
class Downloader;
class IDownloader;
class Camera;
class ITimer;
#include "Color.hpp"


class G3MWidget {
public:
  
  static G3MWidget* create(IFactory*        factory,
                           ILogger*         logger,
                           IGL*             gl,
                           TexturesHandler* texturesHandler,
                           Downloader *     downloaderOLD,
                           IDownloader*     downloader,
                           const Planet*    planet,
                           Renderer*        renderer,
                           Renderer*        busyRenderer,
                           int              width,
                           int              height,
                           Color            backgroundColor,
                           const bool       logFPS);
  
  ~G3MWidget();
  
  int render();
  
  void onTouchEvent(const TouchEvent* myEvent);
  
  void onResizeViewportEvent(int width, int height);
  
  IGL* getGL() const {
    return _gl;
  }
  
  Camera* getCamera() const {
    return _camera;
  }
  
  
private:
  IFactory*        _factory;
  ILogger*         _logger;
  IGL*             _gl;
  const Planet*    _planet;
  Renderer*        _renderer;
  Renderer*        _busyRenderer;
  Camera*          _camera;
  Downloader*      _downloaderOLD;
  IDownloader*     _downloader;
  TexturesHandler* _texturesHandler;
  const Color      _backgroundColor;
  
  ITimer*          _timer;
  long             _renderCounter;
  long             _totalRenderTime;
  const bool       _logFPS;
  
  bool _rendererReady;
  
  void initializeGL();
  
  G3MWidget(IFactory*        factory,
            ILogger*         logger,
            IGL*             gl,
            TexturesHandler* texturesHandler,
            Downloader*      downloaderOLD,
            IDownloader*     downloader,
            const Planet*    planet,
            Renderer*        renderer,
            Renderer*        busyRenderer,
            int              width,
            int              height,
            Color            backgroundColor,
            const bool       logFPS);
  
  void initializeDefault()
  {
    
  }
  
};

#endif
