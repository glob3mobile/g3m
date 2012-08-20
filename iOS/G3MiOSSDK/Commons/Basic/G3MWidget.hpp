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
class GL;
class TexturesHandler;
class Downloader;
class IDownloader;
class Camera;
class ITimer;
class EffectsScheduler;

#include <vector>
#include <string>

#include "Color.hpp"

class ICameraConstrainer;
class FrameTasksExecutor;


class G3MWidget {
public:
  
  static G3MWidget* create(FrameTasksExecutor* frameTasksExecutor,
                           IFactory*           factory,
                           ILogger*            logger,
                           GL*                 gl,
                           TexturesHandler*    texturesHandler,
                           IDownloader*        downloader,
                           const Planet*       planet,
                           std::vector<ICameraConstrainer *> cameraConstraint,
                           Renderer*           renderer,
                           Renderer*           busyRenderer,
                           EffectsScheduler*   scheduler,
                           int                 width,
                           int                 height,
                           Color               backgroundColor,
                           const bool          logFPS,
                           const bool          logDownloaderStatistics);
  
  ~G3MWidget(); 
  
  int render();
  
  void onTouchEvent(const TouchEvent* myEvent);
  
  void onResizeViewportEvent(int width, int height);
  
  GL* getGL() const {
    return _gl;
  }
  
/*  const Camera* getCurrentCamera() const {
    return _currentCamera;
  }*/
  
  Camera* getNextCamera() const {
    return _nextCamera;
  }
  
  
private:
  FrameTasksExecutor* _frameTasksExecutor;
  IFactory*         _factory;
  ILogger*          _logger;
  GL*               _gl;
  const Planet*     _planet;
  Renderer*         _renderer;
  Renderer*         _busyRenderer;
  EffectsScheduler* _effectsScheduler;
  
  std::vector<ICameraConstrainer *> _cameraConstraint;
  
  Camera*          _currentCamera;
  Camera*          _nextCamera;
  IDownloader*     _downloader;
  TexturesHandler* _texturesHandler;
  const Color      _backgroundColor;
  
  ITimer*          _timer;
  long             _renderCounter;
  long             _totalRenderTime;
  const bool       _logFPS;
  const bool       _logDownloaderStatistics;
  std::string      _lastCacheStatistics;
  
  bool      _rendererReady;
  Renderer* _selectedRenderer;
  
  ITimer* _renderStatisticsTimer;
  
  void initializeGL();
  
  G3MWidget(FrameTasksExecutor* frameTasksExecutor,
            IFactory*           factory,
            ILogger*            logger,
            GL*                 gl,
            TexturesHandler*    texturesHandler,
            IDownloader*        downloader,
            const Planet*       planet,
            std::vector<ICameraConstrainer*> cameraConstraint,
            Renderer*           renderer,
            Renderer*           busyRenderer,
            EffectsScheduler*   scheduler,
            int                 width,
            int                 height,
            Color               backgroundColor,
            const bool          logFPS,
            const bool          logDownloaderStatistics);
  
  void initializeDefault()
  {
    
  }
  
};

#endif
